package com.jyt.baseapp.view.viewholder;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.entity.PaymentItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenweiqi on 2017/6/5.
 */

public class PaymentsDetailItemViewHolder extends BaseViewHolder {

    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_date)
    TextView textDate;
    @BindView(R.id.text_addOrSub)
    TextView textAddOrSub;

    public PaymentsDetailItemViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_payments_deail_item, parent, false));
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void setData(Object data, int position) {
        super.setData(data, position);
        PaymentItem paymentItem = (PaymentItem) data;

        textName.setText(paymentItem.msg);

        textDate.setText(paymentItem.createdTime);

        if (!TextUtils.isEmpty(paymentItem.cashType)){
            if (paymentItem.cashType.equals("1")){//收入
                textAddOrSub.setText(" + "+paymentItem.cash);
                textAddOrSub.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
            }else if (paymentItem.cashType.equals("2")){//支出
                textAddOrSub.setText(" - "+paymentItem.cash);
                textAddOrSub.setTextColor(Color.parseColor("#F0BB44"));

            }
        }
    }
}
