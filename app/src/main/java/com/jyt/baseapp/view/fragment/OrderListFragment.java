package com.jyt.baseapp.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.OrderListAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.entity.Order;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenweiqi on 2017/6/1.
 */

public class OrderListFragment extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swipLayout)
    TwinklingRefreshLayout swipLayout;



    public static final int TYPE_SEND_DOING = 0;
    public static final int TYPE_SEND_FAILED = 1;
    public static final int TYPE_SEND_FINISH = 2;

    public static final int TYPE_PICKUP_READY = 3;
    public static final int TYPE_PICKUP_DOING = 4;
    public static final int TYPE_PICKUP_FINISH = 5;
    public static final int TYPE_PICKUP_CANCEL = 6;


    private int type=-1;
    RefreshBoardCast boardCast = new RefreshBoardCast();
    OrderListAdapter adapter;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_list;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (boardCast!=null)
            getActivity().registerReceiver(boardCast,new IntentFilter("ACTION_REFRESH"));

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_rcv));
        recyclerview.addItemDecoration(dividerItemDecoration);

        recyclerview.setAdapter(adapter = new OrderListAdapter());

        adapter.setOnViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener() {
            @Override
            public void onClick(BaseViewHolder holder, Object data, int position) {
                Order order = (Order) data;
                switch (type){
                    case TYPE_SEND_FAILED:
                    case TYPE_SEND_FINISH:
                        IntentHelper.openSendOrderDetailActivity(getContext(),order.orderId);
                        break;
                    case TYPE_SEND_DOING:
                        IntentHelper.openSendOrderActivity(getContext(),order.orderId);
                        break;
                    case TYPE_PICKUP_CANCEL:
                        IntentHelper.openPickUpOrderCancelActivity(getContext(),order.orderId);
                        break;
                    case TYPE_PICKUP_FINISH:
                        IntentHelper.openPickUpOrderFinishActivity(getContext(),order.orderId);
                        break;
                    case TYPE_PICKUP_DOING:
                        IntentHelper.openPickUpOrderDoingActivity(getContext(),order.orderId);
                        break;
                    case TYPE_PICKUP_READY:
                        IntentHelper.openPickUpOrderReadyActivity(getContext(),order.orderId);
                        break;

                }
            }
        });


        swipLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                refreshLayout.finishLoadmore();
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
//                refreshLayout.finishRefreshing();

                getOrderList();
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (boardCast!=null)
            getActivity().unregisterReceiver(boardCast);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    swipLayout.startRefresh();
                }catch (Exception E){
                    swipLayout.finishRefreshing();
                }
            }
        });

    }


    private void getOrderList(){
        BeanCallback<BaseJson<List<Order>>> callback = new BeanCallback<BaseJson<List<Order>>>(getContext().getApplicationContext()){

            @Override
            public void onResult(boolean success, BaseJson<List<Order>> response, Exception e) {
                super.onResult(success, response, e);
                if (success){
                    if (response.ret) {
                        for (Order o
                                : response.data) {
                            o.type = type;
                        }
                        adapter.setDataList(response.data);
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    T.showShort(getContext(),e.getMessage());
                }

                swipLayout.finishRefreshing();
                swipLayout.finishLoadmore();
            }
        };

        switch (type){
            case TYPE_SEND_FAILED:
                Http.getSendList(getContext(),"2",callback);
                break;
            case TYPE_SEND_FINISH:
                Http.getSendList(getContext(),"3",callback);
                break;
            case TYPE_SEND_DOING:
                Http.getSendList(getContext(),"1",callback);
                break;
            case TYPE_PICKUP_CANCEL:
                Http.getPickUpList(getContext(),"4",callback);
                break;
            case TYPE_PICKUP_FINISH:
                Http.getPickUpList(getContext(),"3",callback);
                break;
            case TYPE_PICKUP_DOING:
                Http.getPickUpList(getContext(),"2",callback);
                break;
            case TYPE_PICKUP_READY:
                Http.getPickUpList(getContext(),"1",callback);
                break;

        }
    }


    public class RefreshBoardCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    }
}
