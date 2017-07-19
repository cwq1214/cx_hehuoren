package com.jyt.baseapp.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.BankCardListAdapter;
import com.jyt.baseapp.adapter.PaymentsDetailAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BankCard;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.util.DensityUtil;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenweiqi on 2017/6/5.
 */

public class BankCardListActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    BankCardListAdapter adapter;

    BankCard bankCard;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_refreshview_and_recyclerview;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bankCard = getIntent().getParcelableExtra(IntentHelper.KEY_BAND_CARD);
        if (bankCard == null){
            btnFunction.setText("添加");
        }


        refreshLayout.setPadding(0, DensityUtil.dpToPx(getContext(),11),0,0);

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL);
        recyclerview.addItemDecoration(dividerItemDecoration);
        recyclerview.setAdapter(adapter = new BankCardListAdapter());

        adapter.setOnViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener() {
            @Override
            public void onClick(BaseViewHolder holder, Object data, int position) {
                if (bankCard == null){
                    IntentHelper.openBindCardActivity(getContext(), (BankCard) data);
                }else {
                    Intent intent = new Intent();
                    intent.putExtra(IntentHelper.KEY_BAND_CARD, (Parcelable) data);
                    setResult(RESULT_OK,intent);
                    finish();
                }

            }
        });

        adapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {

                if (bankCard!=null){
                    return false;
                }
                new AlertDialog.Builder(getContext()).setMessage("移除这张银行卡吗？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BankCard bankCard = (BankCard) v.getTag();

                        deleteBankCard(bankCard.bankId);
                        dialog.dismiss();
                    }
                }).show();

                return true;
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
                getBankCardList();
            }
        });

        refreshLayout.startRefresh();
    }

    @Override
    public void onFunctionClick() {
        super.onFunctionClick();
        IntentHelper.openBindCardActivity(getContext());
    }

    private void getBankCardList(){
        Http.getBankCardList(getContext(), new BeanCallback<BaseJson<List<BankCard>>>(getContext()) {
            @Override
            public void onResponse(BaseJson<List<BankCard>> response, int id) {
                super.onResponse(response, id);
                if (response.ret) {
                    for (BankCard tb
                            : response.data) {
                        if (bankCard != null) {
                            if (tb.bankId.equals(bankCard.bankId)) {
                                tb.checked = true;

                            }
                            tb.canChecked = true;
                        }
                    }

                    adapter.setDataList(response.data);
                }
                adapter.notifyDataSetChanged();

                refreshLayout.finishRefreshing();
            }
        });
    }

    private void deleteBankCard(String id){
        Http.deleteBankCard(getContext(), id, new BeanCallback<BaseJson>(getContext()) {
            @Override
            public void onResponse(BaseJson response, int id) {
                super.onResponse(response, id);
                T.showShort(getContext(),response.forUser);
                if (response.ret){
                    refreshLayout.startRefresh();
                }
            }
        });
    }
}
