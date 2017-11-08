package com.jyt.baseapp.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.entity.Area;
import com.jyt.baseapp.util.ScreenUtils;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;

/**
 * Created by chenweiqi on 2017/8/4.
 */

public class SelAreaAdapter extends BaseRcvAdapter {
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        int dp_15 = DensityUtil.dp2px(parent.getContext(),15);
        textView.setPadding(dp_15,dp_15,dp_15,dp_15);

        TextViewViewHolder textViewViewHolder = new TextViewViewHolder(textView);
        textViewViewHolder.setOnViewHolderClickListener(onViewHolderClickListener);
        return textViewViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(dataList.get(position),position);
    }

    class TextViewViewHolder extends BaseViewHolder{

        public TextViewViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(Object data, int position) {
            super.setData(data, position);
            Area area = (Area) data;
            if (TextUtils.isEmpty(area.name)){
                ((TextView) itemView).setText(area.areaName);
            }else {
                ((TextView) itemView).setText(area.name);
            }

            if (area.isSel){
                ((TextView) itemView).setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
            }else {
                ((TextView) itemView).setTextColor(Color.BLACK);
            }
        }
    }
}
