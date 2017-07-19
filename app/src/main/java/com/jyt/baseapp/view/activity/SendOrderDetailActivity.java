package com.jyt.baseapp.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.entity.SendOrderDetailResult;
import com.jyt.baseapp.entity.Track;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;

import java.util.List;

import butterknife.BindView;

/**
 * Created by chenweiqi on 2017/6/2.
 */

public class SendOrderDetailActivity extends BaseActivity {
    @BindView(R.id.text_orderNum)
    TextView textOrderNum;
    @BindView(R.id.text_logisticsNum)
    TextView textLogisticsNum;
    @BindView(R.id.layout_hezuoren)
    LinearLayout layoutHezuoren;
    @BindView(R.id.label_state)
    TextView labelState;
    @BindView(R.id.text_state)
    TextView textState;
    @BindView(R.id.text_reason)
    TextView textReason;
    @BindView(R.id.view_divider)
    View viewDivider;
    @BindView(R.id.layout_orderProgress)
    LinearLayout layoutOrderProgress;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_phone)
    TextView textPhone;
    @BindView(R.id.layout_person)
    RelativeLayout layoutPerson;
    @BindView(R.id.label_reason)
    TextView labelReason;
    @BindView(R.id.label_idCard)
    TextView labelIdCard;
    @BindView(R.id.text_idCard)
    TextView textIdCard;

    public String orderId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_order_detail;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderId = getIntent().getStringExtra(IntentHelper.KEY_ORDER_ID);

//        addOrderProgress();
        getSendOrderDetail();

    }


    public void getSendOrderDetail() {
        Http.getSendOrderDetail(getContext(), orderId, new BeanCallback<BaseJson<SendOrderDetailResult>>(getContext()) {
            @Override
            public void onResult(boolean success, BaseJson<SendOrderDetailResult> response, Exception e) {
                super.onResult(success, response, e);
                if (success) {
                    T.showShort(getContext(), response.forUser);
                    if (response.ret) {
                        initView(response.data);
                    }
                } else {
                    T.showShort(getContext(), e.getMessage());
                }
            }
        });
    }


    private void initView(SendOrderDetailResult result) {
        textOrderNum.setText(result.trackingNo);
        textLogisticsNum.setText(result.orderNo);
        textName.setText(result.partnerName);
        textPhone.setText(result.mobile);
        if (result.sendState.equals("3")) {//成功
//            textIdCard.setText(result.);
            textReason.setText(result.signer);
            labelReason.setText("签收人");
            textState.setTextColor(getResources().getColor(R.color.colorPrimary));


        } else if (result.sendState.equals("4")) {//失败
            textState.setTextColor(Color.RED);

            textReason.setText(result.failureReason);

        }
        textState.setText(result.sendStateMsg);

        addOrderProgress(result.msgList);
    }

    private void addOrderProgress(List<Track> tracks) {
//        List list = new ArrayList();
//        list.add("");
//        list.add("");
//        list.add("");

        layoutOrderProgress.removeAllViews();
        for (int i = 0; i < tracks.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_order_progress, layoutOrderProgress, false);

            TextView textView = (TextView) view.findViewById(R.id.text_title);
            textView.setText(tracks.get(i).msg);
            TextView date = (TextView) view.findViewById(R.id.text_date);
            date.setText(tracks.get(i).msgTime);


            if (i == tracks.size() - 1) {
                view.findViewById(R.id.view_line).setVisibility(View.GONE);
            }

            layoutOrderProgress.addView(view);
        }
    }
}
