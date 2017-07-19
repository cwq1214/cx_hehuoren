package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.entity.OrderTrackResult;
import com.jyt.baseapp.entity.Track;
import com.jyt.baseapp.util.IntentHelper;

import butterknife.BindView;

/**
 * Created by chenweiqi on 2017/6/5.
 */

public class TrackOrderActivity extends BaseActivity {


    @BindView(R.id.text_orderNo)
    TextView textOrderNo;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_phone)
    TextView textPhone;
    @BindView(R.id.layout_progress)
    LinearLayout layoutProgress;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_track_order;
    }

    @Override
    protected View getContentView() {
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OrderTrackResult result = getIntent().getParcelableExtra(IntentHelper.KEY_ORDER_TRACK_RESULT);

        textOrderNo.setText(result.orderNo);

        textName.setText(result.partnerName);

        textPhone.setText(result.mobile);

        for (int i=0,max = result.track.size(); i<max ;i++){
            Track track = result.track.get(i);

            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_order_progress,layoutProgress,false);
            ((TextView) view.findViewById(R.id.text_title)).setText(track.msg);
            ((TextView) view.findViewById(R.id.text_date)).setText(track.msgTime);

            if (i == max-1){
                view.findViewById(R.id.view_line).setVisibility(View.GONE);

            }
            layoutProgress.addView(view);
        }
    }
}
