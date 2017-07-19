package com.jyt.baseapp.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chenweiqi on 2017/5/10.
 */

public class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    protected T data;
    protected int position;
    protected OnViewHolderClickListener<T> onViewHolderClickListener;

    public BaseViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onViewHolderClickListener!=null)
                    onViewHolderClickListener.onClick(BaseViewHolder.this,data,position);
            }
        });
    }

    public T getData() {
        return data;
    }

    public void setData(T data,int position) {
        this.data = data;
        this.position = position;
    }

    public OnViewHolderClickListener<T> getOnViewHolderClickListener() {
        return onViewHolderClickListener;
    }

    public void setOnViewHolderClickListener(OnViewHolderClickListener<T> onViewHolderClickListener) {
        this.onViewHolderClickListener = onViewHolderClickListener;
    }

    public interface OnViewHolderClickListener<T>{
        void onClick(BaseViewHolder holder,T data ,int position);
    }
}
