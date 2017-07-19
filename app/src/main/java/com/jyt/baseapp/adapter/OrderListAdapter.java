package com.jyt.baseapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.OrderListItemViewHolder;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by chenweiqi on 2017/6/1.
 */

public class OrderListAdapter extends BaseRcvAdapter {


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OrderListItemViewHolder orderListItemViewHolder = new OrderListItemViewHolder(parent);
        orderListItemViewHolder.setOnViewHolderClickListener(onViewHolderClickListener);
        return orderListItemViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
         holder.setData(dataList.get(position),position);
    }


}
