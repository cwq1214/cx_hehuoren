package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.PaymentsDetailItemViewHolder;

/**
 * Created by chenweiqi on 2017/6/5.
 */

public class PaymentsDetailAdapter extends BaseRcvAdapter {
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PaymentsDetailItemViewHolder paymentsDetailItemViewHolder = new PaymentsDetailItemViewHolder(parent);

        return paymentsDetailItemViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(dataList.get(position),position);
    }
}
