package com.jyt.baseapp.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.DepositLevelAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.entity.Deposit;
import com.jyt.baseapp.entity.DepositListResult;
import com.jyt.baseapp.util.Cache;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;
import com.jyt.baseapp.view.dialog.TextDoneDialog;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/6/2.
 */

public class DepositLevelActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;
    @BindView(R.id.btn_outDeposit)
    TextView btnOutDeposit;

    DepositLevelAdapter adapter;
    DepositListResult result;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_deposit_level;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(adapter = new DepositLevelAdapter());


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_rcv));
        recyclerview.addItemDecoration(dividerItemDecoration);

//        List list = new ArrayList();
//        list.add("");
//        list.add("");
//        list.add("");
//        adapter.setDataList(list);
//        adapter.notifyDataSetChanged();

        adapter.setOnViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener() {
            @Override
            public void onClick(BaseViewHolder holder, Object data, int position) {
                if (((Deposit) data).depositId.equals(result.partnerDepositId)){
                    return;
                }
                IntentHelper.openDepositAdjustActivity(getContext(), ((Deposit) data).depositId);
            }
        });

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                refreshLayout.finishLoadmore();
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);


                getDepositList();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout.startRefresh();

    }

    @OnClick(R.id.btn_outDeposit)
    public void onOutDepositClick(){
        if (TextUtils.isEmpty(result.partnerDepositId)){
            T.showShort(getContext(),"您还没有购买保证金");
            return;
        }
        TextDoneDialog doneDialog = new TextDoneDialog(getContext());
        doneDialog.setTextMsg("是否确认退出？");
        doneDialog.setCancelListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        doneDialog.setDoneListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                quitDeposit();
                dialog.dismiss();
            }
        });
        doneDialog.show();
    }


    private void quitDeposit(){
        Http.quitDeposit(getContext(), new BeanCallback<BaseJson>(getContext()) {
            @Override
            public void onResult(boolean success, BaseJson response, Exception e) {
                super.onResult(success, response, e);
                if (success){
                    T.showShort(getContext(),response.forUser);
                    if (response.ret){
                        finish();
                    }
                }else {
                    T.showShort(getContext(),e.getMessage());
                }
            }
        });
    }

    private void getDepositList(){
        Http.getDepositList(getContext(), new BeanCallback<BaseJson<DepositListResult>>(getContext()) {
            @Override
            public void onResponse(BaseJson<DepositListResult> response, int id) {
                super.onResponse(response,id);

                T.showShort(getContext(),response.forUser);
                result = response.data;
                if (response.ret){
                    if (!TextUtils.isEmpty(result.partnerDepositId)){
                        for (Deposit d:
                             result.deposit) {
                            if (d.depositId.equals(result.partnerDepositId)){
                                d.checked = true;
                            }
                        };
                    }

                    adapter.setDataList(result.deposit);
                    adapter.notifyDataSetChanged();
                }
                refreshLayout.finishRefreshing();
            }
        });
    }
}
