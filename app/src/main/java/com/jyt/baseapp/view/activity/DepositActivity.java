package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
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
 * Created by chenweiqi on 2017/6/1.
 */

public class DepositActivity extends BaseActivity {
    @BindView(R.id.text_levelName)
    TextView textLevelName;
    @BindView(R.id.label)
    TextView label;
    @BindView(R.id.text_depositMoney)
    TextView textDepositMoney;
    @BindView(R.id.layou_depositLevel)
    RelativeLayout layouDepositLevel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deposit;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @OnClick(R.id.layou_depositLevel)
    public void onDepositLevelClick(){
        IntentHelper.openDepositLevelActivity(getContext());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSelfInfo();
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

                if (TextUtils.isEmpty(response.data.depositTitle)){
                    textLevelName.setText("尚未购买保证金");
                    textDepositMoney.setText("0.00");
                }else {
                    textDepositMoney.setText(response.data.deposit.price);
                    textLevelName.setText(response.data.depositTitle);

                }
            }
        });
    }
}
