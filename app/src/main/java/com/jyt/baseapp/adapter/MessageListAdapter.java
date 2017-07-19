package com.jyt.baseapp.adapter;

import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.MsgItemViewHolder;

/**
 * Created by chenweiqi on 2017/6/13.
 */

public class MessageListAdapter extends BaseRcvAdapter{
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MsgItemViewHolder holder = new MsgItemViewHolder(parent);
        holder.setOnViewHolderClickListener(onViewHolderClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
            holder.setData(dataList.get(position),position);
    }

}
