package com.jyt.baseapp.view.viewholder;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyt.baseapp.R;

import butterknife.ButterKnife;

/**
 * Created by chenweiqi on 2017/6/1.
 */

public class DateViewHolder extends BaseViewHolder {
    public DateViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_date_pop,parent,false));
        ButterKnife.bind(this,itemView);
    }


}
