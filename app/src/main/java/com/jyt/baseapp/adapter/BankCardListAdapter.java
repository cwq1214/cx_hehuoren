package com.jyt.baseapp.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BankCardItemViewHolder;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;

/**
 * Created by chenweiqi on 2017/6/5.
 */

public class BankCardListAdapter extends BaseRcvAdapter {
    View.OnLongClickListener onLongClickListener;


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BankCardItemViewHolder holder = new BankCardItemViewHolder(parent);
        holder.setOnViewHolderClickListener(onViewHolderClickListener);
        holder.itemView.setOnLongClickListener(onLongClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(dataList.get(position),position);
    }


    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
