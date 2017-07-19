package com.jyt.baseapp.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.entity.OrderMessage;
import com.jyt.baseapp.entity.SystemMessage;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenweiqi on 2017/6/1.
 */

public class MsgItemViewHolder extends BaseViewHolder {
    @BindView(R.id.text_msgDate)
    TextView textMsgDate;
    @BindView(R.id.text_msgContent)
    TextView textMsgContent;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.btn_checkOrder)
    TextView btnCheckOrder;

    public MsgItemViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_msg_item, parent, false));
        ButterKnife.bind(this,itemView);

    }

    @Override
    public void setData(Object data, int position) {
        super.setData(data, position);


        if (data instanceof SystemMessage) {
            divider.setVisibility(View.GONE);
            btnCheckOrder.setVisibility(View.GONE);

            textMsgDate.setText(((SystemMessage) data).createdTime);
            textMsgContent.setText(((SystemMessage) data).content);

        }else if (data instanceof OrderMessage){
            divider.setVisibility(View.VISIBLE);
            btnCheckOrder.setVisibility(View.VISIBLE);

            textMsgContent.setText(((OrderMessage) data).content);
            textMsgDate.setText(((OrderMessage) data).createdTime);
        }
    }
}
