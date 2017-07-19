package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.PaymentsDetailAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.entity.PaymentItem;
import com.jyt.baseapp.util.DensityUtil;
import com.jyt.baseapp.util.T;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenweiqi on 2017/6/5.
 */

public class PaymentsDetailActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    PaymentsDetailAdapter adapter;
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
        refreshLayout.setPadding(0, DensityUtil.dpToPx(getContext(),11),0,0);

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL);
        recyclerview.addItemDecoration(dividerItemDecoration);
        recyclerview.setAdapter(adapter = new PaymentsDetailAdapter());
//        List list = new ArrayList();
//        list.add("");
//        list.add("");
//        list.add("");
//        adapter.setDataList(list);
//        adapter.notifyDataSetChanged();


        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);

                if (adapter.getDataList()==null
                        ||adapter.getDataList().size()==0){
                    refreshLayout.finishLoadmore();
                    return;
                }
                getPaymentList(((PaymentItem) adapter.getDataList().get(adapter.getDataList().size() - 1)).cashId);
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);

                getPaymentList(null);
            }
        });

        refreshLayout.startRefresh();
    }



    private void getPaymentList(final String lastId){
        Http.paymentsDetail(getContext(), lastId, new BeanCallback<BaseJson<List<PaymentItem>>>(getContext()) {
            @Override
            public void onResponse(BaseJson<List<PaymentItem>> response, int id) {
                super.onResponse(response,id);

                T.showShort(getContext(),response.forUser);
                if (response.ret){
                    if (lastId==null) {
                        adapter.setDataList(response.data);
                    }else {
                        adapter.getDataList().addAll(response.data);
                    }
                    adapter.notifyDataSetChanged();
                }
                refreshLayout.finishLoadmore();
                refreshLayout.finishRefreshing();
            }
        });
    }


}
