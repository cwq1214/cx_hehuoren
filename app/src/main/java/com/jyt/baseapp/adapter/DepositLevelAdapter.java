package com.jyt.baseapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.DepositLevelItemViewHolder;

import java.util.List;

/**
 * Created by chenweiqi on 2017/6/2.
 */

public class DepositLevelAdapter extends BaseRcvAdapter {

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DepositLevelItemViewHolder depositLevelItemViewHolder = new DepositLevelItemViewHolder(parent);
        depositLevelItemViewHolder.setOnViewHolderClickListener(onViewHolderClickListener);
        return depositLevelItemViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(dataList.get(position),position);
    }


}
