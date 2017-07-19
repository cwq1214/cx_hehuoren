package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.entity.Deposit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenweiqi on 2017/6/1.
 */

public class DepositLevelItemViewHolder extends BaseViewHolder {
    @BindView(R.id.text_deposit_level_name)
    TextView textDepositLevelName;
    @BindView(R.id.text_des)
    TextView textDes;
    @BindView(R.id.img_right)
    ImageView imgRight;

    public DepositLevelItemViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_deposit_item, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setData(Object data, int position) {
        super.setData(data, position);
        Deposit deposit = (Deposit) data;

        textDepositLevelName.setText(deposit.title);
        textDes.setText(String.format("可承接的价值范围：%s - %s 元",deposit.minValue,deposit.maxValue));


        if (deposit.checked){
            imgRight.setImageDrawable(itemView.getContext().getResources().getDrawable(R.mipmap.right_small));
        }else {
            imgRight.setImageDrawable(itemView.getContext().getResources().getDrawable(R.mipmap.arrow));

        }
    }
}
