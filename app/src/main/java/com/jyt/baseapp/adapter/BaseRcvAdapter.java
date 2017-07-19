package com.jyt.baseapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;

import java.util.List;

/**
 * Created by chenweiqi on 2017/6/2.
 */

public abstract class BaseRcvAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    protected List dataList;
    protected BaseViewHolder.OnViewHolderClickListener onViewHolderClickListener;
    @Override
    public abstract BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(BaseViewHolder holder, int position);

    @Override
    public int getItemCount() {
        if (dataList!=null){
            return dataList.size();
        }
        return 0;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    public void setOnViewHolderClickListener(BaseViewHolder.OnViewHolderClickListener onViewHolderClickListener) {
        this.onViewHolderClickListener = onViewHolderClickListener;
    }
}
