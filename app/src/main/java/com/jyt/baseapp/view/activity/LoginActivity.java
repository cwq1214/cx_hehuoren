package com.jyt.baseapp.view.activity;

import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.entity.LoginResult;
import com.jyt.baseapp.util.Cache;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;

import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by chenweiqi on 2017/5/31.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.input_phoneNumber)
    EditText inputPhoneNumber;
    @BindView(R.id.input_code)
    EditText inputCode;
    @BindView(R.id.btn_getCode)
    TextView btnGetCode;
    @BindView(R.id.btn_login)
    TextView btnLogin;

    Handler handler = new Handler();
    Timer timer = new Timer();
    boolean canGetCode = true;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cache.getInstance().setToken(null);

        hideBackBtn();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(timer);
    }

    @OnClick(R.id.btn_getCode)
    public void onGetCodeClick(){
        if (!canGetCode){
            return;
        }

        String phone = inputPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(phone)){
            T.showShort(getContext(),"请输入手机号");
            return;
        }

        Http.getCode(getContext(), phone, new BeanCallback<BaseJson>(getContext()) {
            @Override
            public void onResponse(BaseJson response, int id) {
                super.onResponse(response,id);

                if (response.ret){
                    handler.post(timer);
                    canGetCode = false;
                }else {
                    canGetCode = true;
                }
                T.showShort(getContext(),response.forUser);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                T.showShort(getContext(),e.getMessage());
                canGetCode = true;
            }
        });
    }
    @OnClick(R.id.btn_login)
    public void onLoginClick(){
        final String phone = inputPhoneNumber.getText().toString();
        String code = inputCode.getText().toString();
        if (TextUtils.isEmpty(phone)){
            T.showShort(getContext(),"请输入手机号");
            return;
        }else if (TextUtils.isEmpty(code)){
            T.showShort(getContext(),"请输入验证码");
            return;
        }
        Http.login(getContext(), phone, code, new BeanCallback<BaseJson<LoginResult>>(getContext()) {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call,e,id);
            }

            @Override
            public void onResponse(BaseJson<LoginResult> response, int id) {
                super.onResponse(response,id);
                if (response.ret){
                    Cache.getInstance().setToken(response.data.tokenSession);
                    Cache.getInstance().setPhone(phone);
                    Cache.getInstance().setUserId(response.data.partnerId);
                    IntentHelper.openMainActivity(getContext());
                    finish();
                    App.setJPushAliasByUserId();
                }else {
                    T.showShort(getContext(),response.forUser);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        IntentHelper.exit(getContext());
        finish();
//        System.exit(0);
//        super.onBackPressed();
    }

    class Timer implements Runnable{
        int begin = 60;
        int time = begin;
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnGetCode.setText(time+ "秒后重试");
                    btnGetCode.setBackground(getResources().getDrawable(R.drawable.btn_dark_gray_angle));
                }
            });
            time--;
            if (time>=0){
                handler.postDelayed(this,1000);
            }else {
                handler.removeCallbacks(this);
                time = begin;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        canGetCode = true;
                        btnGetCode.setText("获取验证码");
                        btnGetCode.setBackground(getResources().getDrawable(R.drawable.btn_blue_solid_angle));
                    }
                });
            }


        }
    }
}
