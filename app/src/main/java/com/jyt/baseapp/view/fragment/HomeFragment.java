package com.jyt.baseapp.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.entity.HomeBannerResult;
import com.jyt.baseapp.util.Cache;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;
import com.jyt.baseapp.view.activity.MainActivity;
import com.jyt.baseapp.view.dialog.QRCodeDialog;
import com.jyt.baseapp.view.dialog.TextDoneDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by chenweiqi on 2017/5/31.
 */

public class HomeFragment extends BaseFragment {


    @BindView(R.id.btn_showQRCode)
    TextView btnShowQRCode;
    @BindView(R.id.bannerView)
    BGABanner bannerView;
    @BindView(R.id.layout_offwork)
    LinearLayout layoutOffwork;
    @BindView(R.id.layout_pickup)
    LinearLayout layoutPickup;
    @BindView(R.id.layout_send)
    LinearLayout layoutSend;
    @BindView(R.id.img_work)
    ImageView imgWork;
    @BindView(R.id.text_work)
    TextView textWork;


    boolean isWorking = false;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (bannerView.getItemCount()==0){
            getBannerContent();
        }
    }

    private void getBannerContent() {
        Http.setGetBanner(getContext(), new BeanCallback<BaseJson<HomeBannerResult>>(getContext()) {
            @Override
            public void onResponse(BaseJson<HomeBannerResult> response, int id) {
                super.onResponse(response,id);

                if (response.ret) {
                    setView(response.data);
                } else {

                }
            }
        });
    }


    private void setView(HomeBannerResult result) {

        bannerView.setPageChangeDuration(Integer.parseInt(result.stayTime) * 1000);

        List<View> imageViews = new ArrayList<>();

        for (int i = 0; i < result.marquee.size(); i++) {

            ImageView imageView = new ImageView(getContext());
            Glide.with(getContext()).load(result.marquee.get(i).img).bitmapTransform(new CenterCrop(getContext())).into(imageView);

            final String url = result.marquee.get(i).link;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(url))
                        IntentHelper.openSystemBrowser(getContext(),url);
                }
            });

            imageViews.add(imageView);
        }

        bannerView.setData(imageViews);


        isWorking = result.jobState.endsWith("1");
        changeWorkStateView(isWorking);
    }

   @OnClick(R.id.layout_offwork)
    public void onWorkClick(){
       if (isWorking){
           TextDoneDialog doneDialog = new TextDoneDialog(getContext());

           doneDialog.setTextMsg("确定下班休息？");
           doneDialog.setDoneListener(new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   changeWorkSate(isWorking);
                   dialog.dismiss();
               }
           });
           doneDialog.setCancelListener(new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
               }
           });
           doneDialog.show();
       }else {
           changeWorkSate(isWorking);
       }
   }

    @OnClick(R.id.layout_pickup)
    public void onPickUpClick(){
        MainActivity activity = ((MainActivity) getActivity());
        activity.changePage(1);
    }
    @OnClick(R.id.layout_send)
    public void onSendClick(){
        if (Cache.getInstance().getIdentified().equals("0")){
            T.showShort(getContext(),"请先认证");
            return;
        }
        IntentHelper.openSendOrderActivity(getContext(),null);
    }


    private void changeWorkStateView(boolean isWorking){
        if (isWorking){
            imgWork.setImageDrawable(getResources().getDrawable(R.mipmap.home_icon_work_n));
            textWork.setText("正在工作");
        }else {
            imgWork.setImageDrawable(getResources().getDrawable(R.mipmap.home_icon_rest_n));
            textWork.setText("休息中");
        }
    }

    @OnClick(R.id.btn_showQRCode)
    public void onShowQRCodeClick(){
        QRCodeDialog dialog = new QRCodeDialog(getContext());
        dialog.show();
    }


    private void changeWorkSate( boolean isWorking){
        Http.setChangeWorkState(getContext(), isWorking, new BeanCallback<BaseJson>(getContext()) {
            @Override
            public void onResponse(BaseJson response, int id) {
                super.onResponse(response,id);

                if (response.ret){
                    HomeFragment.this.isWorking = !HomeFragment.this.isWorking;
                    changeWorkStateView(HomeFragment.this.isWorking);
                }else {
                    T.showShort(getContext(),response.forUser);
                }
            }
        });
    }
}
