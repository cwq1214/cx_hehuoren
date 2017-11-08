package com.jyt.baseapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.entity.HomeMsgBox;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;
import com.jyt.baseapp.view.activity.MainActivity;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/6/1.
 */

public class MessageBoxFragment extends BaseFragment {


    @BindView(R.id.img_sys)
    ImageView imgSys;
    @BindView(R.id.text_sysDate)
    TextView textSysDate;
    @BindView(R.id.text_sysmsg)
    TextView textSysmsg;
    @BindView(R.id.layout_sys)
    RelativeLayout layoutSys;
    @BindView(R.id.img_order)
    ImageView imgOrder;
    @BindView(R.id.text_orderDate)
    TextView textOrderDate;
    @BindView(R.id.text_ordermsg)
    TextView textOrdermsg;
    @BindView(R.id.layout_order)
    RelativeLayout layoutOrder;
    @BindView(R.id.text_sysCount)
    TextView textSysCount;
    @BindView(R.id.text_orderCount)
    TextView textOrderCount;
//    @BindView(R.id.refreshLayout)
//    TwinklingRefreshLayout refreshLayout;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_msg_box;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
//                super.onRefresh(refreshLayout);
//                getMsg();
//            }
//            @Override
//            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
//                super.onLoadMore(refreshLayout);
//                refreshLayout.finishLoadmore();
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        refreshLayout.startRefresh();
           getMsg();
    }

    private void getMsg() {
        Http.messageBox(getContext(), new BeanCallback<BaseJson<HomeMsgBox>>(getContext()) {
            @Override
            public void onResponse(BaseJson<HomeMsgBox> response, int id) {
                super.onResponse(response,id);
                T.showShort(getContext(), response.forUser);
                if (response.ret) {
                    if (response.data.sys==null){
                        layoutSys.setVisibility(View.GONE);
                    }else {
                        layoutSys.setVisibility(View.VISIBLE);
                        textSysDate.setText(response.data.sys.createdTime);
                        textSysmsg.setText(response.data.sys.content);
                        if ("0".equals(response.data.countSys)|| TextUtils.isEmpty(response.data.countSys)) {
                            textSysCount.setVisibility(View.GONE);
                        }else {
                            textSysCount.setVisibility(View.VISIBLE);
                            if (Integer.valueOf(response.data.countSys)>99){
                                textSysCount.setText("99+");
                            }else {
                                textSysCount.setText(response.data.countSys);
                            }
                        }
                    }


                    if (response.data.order==null){
                        layoutOrder.setVisibility(View.GONE);
                    }else {
                        layoutOrder.setVisibility(View.VISIBLE);
                        textOrderDate.setText(response.data.order.createdTime);
                        textOrdermsg.setText(response.data.order.content);

                        if ("0".equals(response.data.countOrder)|| TextUtils.isEmpty(response.data.countOrder)) {
                            textOrderCount.setVisibility(View.GONE);
                        }else {
                            textOrderCount.setVisibility(View.VISIBLE);
                            if (Integer.valueOf(response.data.countOrder)>99){
                                textOrderCount.setText("99+");
                            }else {
                                textOrderCount.setText(response.data.countOrder);
                            }
                        }
                    }

                    int msgCount = 0;
                    if (!TextUtils.isEmpty(response.data.countOrder)){
                        msgCount += Integer.valueOf(response.data.countOrder);
                    }
                    if (!TextUtils.isEmpty(response.data.countSys)){
                        msgCount += Integer.valueOf(response.data.countSys);
                    }
                    if (msgCount!=0) {
                        ((MainActivity) getActivity()).textMsgCount.setVisibility(View.VISIBLE);
                        ((MainActivity) getActivity()).textMsgCount.setText(msgCount + "");
                    }else {
                        ((MainActivity) getActivity()).textMsgCount.setVisibility(View.GONE);
                    }
                }
//                refreshLayout.finishRefreshing();
            }
        });
    }

    @OnClick(R.id.layout_sys)
    public void onSysClick(){
        IntentHelper.openSystemMessageListActivity(getContext());
    }

    @OnClick(R.id.layout_order)
    public void onOrderClick(){
        IntentHelper.openOrderMessageListActivity(getContext());
    }
}
