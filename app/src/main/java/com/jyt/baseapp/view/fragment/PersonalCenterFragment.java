package com.jyt.baseapp.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.entity.SelfInfoResult;
import com.jyt.baseapp.util.Cache;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by chenweiqi on 2017/6/1.
 */

public class PersonalCenterFragment extends BaseFragment {
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.identifiedIcon)
    ImageView identifiedIcon;
    @BindView(R.id.identifiedText)
    TextView identifiedText;
    @BindView(R.id.layout_personInfo)
    RelativeLayout layoutPersonInfo;
    @BindView(R.id.text_identified)
    TextView textIdentified;
    @BindView(R.id.layout_identified)
    RelativeLayout layoutIdentified;
    @BindView(R.id.layout_wallet)
    RelativeLayout layoutWallet;
    @BindView(R.id.text_deposit)
    TextView textDeposit;
    @BindView(R.id.layout_deposit)
    RelativeLayout layoutDeposit;
    @BindView(R.id.text_score)
    TextView textScore;
    @BindView(R.id.layout_score)
    RelativeLayout layoutScore;


    SelfInfoResult result;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_person_center;
    }
    @OnClick(R.id.layout_personInfo)
    public void onPersonInfoClick(){
        if (result!=null)

            IntentHelper.openPersonalInfoActivity(getContext());
    }
    @OnClick(R.id.layout_deposit)
    public void onDeposiClick(){
        if (result!=null)
            IntentHelper.openDepositActivity(getContext());
    }
    @OnClick(R.id.layout_wallet)
    public void onWalletClick(){
        if (result!=null)
        IntentHelper.openWalletActivity(getContext(),result.cash);
    }
    @OnClick(R.id.layout_identified)
    public void onIdentifiedClick(){
        if (result!=null)
            if (result.partnerReview.equals("1")
                    ||result.partnerReview.equals("2")){
            return;
        }
        IntentHelper.openIdentifiedActivity(getContext());
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        getSelfInfo();
    }

    public void getSelfInfo(){

        Http.getSelfInfo(getContext(), new BeanCallback<BaseJson<SelfInfoResult>>(getContext()) {
            @Override
            public void onResponse(BaseJson<SelfInfoResult> response, int id) {
                super.onResponse(response,id);

                T.showShort(getContext(),response.forUser);
                result = response.data;
                if (response.ret){
                    Cache appCache = Cache.getInstance();
                    appCache.setAvatar(response.data.partnerImg);
                    appCache.setIDCard(response.data.idCard);
                    appCache.setUserName(response.data.partnerName);

                    initView(response.data);
                }
            }
        });
    }

    private void initView(SelfInfoResult result){
        Glide.with(getContext()).load(result.partnerImg).bitmapTransform(new CenterCrop(getContext()),new CropCircleTransformation(getContext())).into(avatar);
        name.setText(result.partnerName);
        textDeposit.setText(result.depositTitle);
        textScore.setText(result.score);
        if (!TextUtils.isEmpty(result.partnerReview)){
            if (result.partnerReview.equals("1")){//审核中
                textIdentified.setText("审核中");
            }else if (result.partnerReview.equals("2")){//审核通过
                identifiedIcon.setVisibility(View.VISIBLE);
                identifiedText.setVisibility(View.VISIBLE);
                textIdentified.setText("已认证");
            }else if(result.partnerReview.equals("3")){//审核不通过
                textIdentified.setText("审核不通过");
            }else if (result.partnerReview.equals("0")){
                textIdentified.setText("未认证");
            }
            Cache.getInstance().setIdentified(result.partnerReview);
        }

    }
}
