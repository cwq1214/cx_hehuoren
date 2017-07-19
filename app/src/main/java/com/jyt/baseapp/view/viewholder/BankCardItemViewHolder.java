package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.entity.BankCard;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenweiqi on 2017/6/5.
 */

public class BankCardItemViewHolder extends BaseViewHolder {
    @BindView(R.id.text_cardName)
    TextView textCardName;
    @BindView(R.id.text_cardNum)
    TextView textCardNum;
    @BindView(R.id.cb_check)
    CheckBox cbCheck;
    @BindView(R.id.layout_selCard)
    RelativeLayout layoutSelCard;

    public BankCardItemViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_bank_card_item, parent, false));
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void setData(Object data, int position) {
        super.setData(data, position);
        BankCard bankCard = (BankCard) data;

        itemView.setTag(data);
        textCardName.setText(bankCard.openBank);
        textCardNum.setText(bankCard.bankNumber);

        if (bankCard.canChecked){
            cbCheck.setVisibility(View.VISIBLE);
            cbCheck.setChecked(bankCard.checked);
        }else {
            cbCheck.setVisibility(View.GONE);
        }
    }
}
