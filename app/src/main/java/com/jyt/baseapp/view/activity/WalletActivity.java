package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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

/**
 * Created by chenweiqi on 2017/6/5.
 */

public class WalletActivity extends BaseActivity {
    @BindView(R.id.text_balance)
    TextView textBalance;
    @BindView(R.id.btn_tixian)
    TextView btnTixian;
    @BindView(R.id.btn_bangding)
    TextView btnBangding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnFunction.setText("明细");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSelfInfo();
    }

    @OnClick(R.id.btn_tixian)
    public void onTiXianClick(){
        IntentHelper.openPostBalanceActivity(getContext());
    }

    @OnClick(R.id.btn_bangding)
    public void onBandDingClick(){
//        IntentHelper.openBindCardActivity(getContext());
        IntentHelper.openSelBankCardActivity(getContext());
    }

    @Override
    public void onFunctionClick() {
        IntentHelper.openPaymentsDetailActivity(getContext());
    }


    public void getSelfInfo(){
        Http.getSelfInfo(getContext(), new BeanCallback<BaseJson<SelfInfoResult>>(getContext()) {
            @Override
            public void onResponse(BaseJson<SelfInfoResult> response, int id) {
                super.onResponse(response,id);

                T.showShort(getContext(),response.forUser);

                    Cache appCache = Cache.getInstance();
                    appCache.setAvatar(response.data.partnerImg);
                    appCache.setIDCard(response.data.idCard);
                    appCache.setUserName(response.data.partnerName);

                textBalance.setText(response.data.cash);

            }
        });
    }
}
