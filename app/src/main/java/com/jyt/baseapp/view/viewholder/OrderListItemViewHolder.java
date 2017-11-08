package com.jyt.baseapp.view.viewholder;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.entity.Order;
import com.jyt.baseapp.view.fragment.OrderListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenweiqi on 2017/6/1.
 */

public class OrderListItemViewHolder extends BaseViewHolder {
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.text_state)
    TextView textState;

    public OrderListItemViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_orderlist_item, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setData(Object data, int position) {
        super.setData(data, position);
        Order order = (Order) data;

        if (order.type<=2){
            name.setText("运单号："+order.trackingNo);
            phone.setText(null);
            address.setText("快递公司："+order.expressCompany);
        }else {
            name.setText(order.receiveName);
            phone.setText(order.receiveMobile);
            address.setText(order.receiveAddress);

        }

        textState.setText(order.receiveStateMsg);

//        switch (order.type){
//            case OrderListFragment.TYPE_SEND_DOING:
//                textState.setText("派件中");
//                break;
//            case OrderListFragment.TYPE_SEND_FAILED:
//                textState.setTextColor(Color.parseColor("#FF4F4F"));
//                textState.setText("派件失败");
//                break;
//            case OrderListFragment.TYPE_SEND_FINISH:
//                textState.setText("已完成");
//
//                break;
//            case OrderListFragment.TYPE_PICKUP_READY:
//                textState.setText("待收件");
//                break;
//            case OrderListFragment.TYPE_PICKUP_CANCEL:
//                textState.setText("已取消");
//                break;
//            case OrderListFragment.TYPE_PICKUP_DOING:
//                textState.setText("进行中");
//                break;
//            case OrderListFragment.TYPE_PICKUP_FINISH:
//                textState.setText("已完成");
//                break;
//        }


    }
}
