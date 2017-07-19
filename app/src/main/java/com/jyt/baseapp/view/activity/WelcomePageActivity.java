package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.util.Cache;
import com.jyt.baseapp.util.IntentHelper;

/**
 * Created by chenweiqi on 2017/6/6.
 */

public class WelcomePageActivity extends BaseActivity {

    Handler handler = new Handler();
    Timer timer = new Timer();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome_page;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseActionBarVisible(false);
        handler.post(timer);
    }

    private void changePage(){

        if (TextUtils.isEmpty(Cache.getInstance().getToken())){

            IntentHelper.openLoginActivity(getContext());

        }else {
            IntentHelper.openMainActivity(getContext());
        }

        if (Cache.getInstance().isFirstOpen()) {
//        if (true) {
            IntentHelper.openGuideActivity(getContext());
            Cache.getInstance().setFirstOpen(false);
        }
        finish();
    }

    class Timer implements Runnable{
        int sec = 3;
        @Override
        public void run() {
            if (sec>=0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO 倒计时显示
                    }
                });
                sec--;
                handler.postDelayed(timer,1000);
            }else {
                changePage();

            }
        }
    }


}
