package com.jyt.baseapp.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.util.IntentHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by chenweiqi on 2017/5/10.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Nullable
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @Nullable
    @BindView(R.id.text_title)
    TextView textTitle;
    @Nullable
    @BindView(R.id.content)
    FrameLayout content;
    @Nullable
    @BindView(R.id.base_actionBar)
    RelativeLayout baseActionBar;
    @BindView(R.id.btn_function)
    TextView btnFunction;


    String title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        content = (FrameLayout) findViewById(R.id.content);

        View contentView = getContentView();
        if (contentView != null) {
            content.addView(contentView);
        }
        int layoutId = getLayoutId();
        if (layoutId != 0) {
            content.addView(LayoutInflater.from(this).inflate(layoutId,content,false));
        }
        ButterKnife.bind(this);

        if (TextUtils.isEmpty(title)) {
            title = getTitle().toString();
        }
        textTitle.setText(title);

        content.requestFocus();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        this.title = title.toString();
    }

    @OnClick(R.id.btn_back)
    public void onBtnBackClick(){
        onBackPressed();
    }

    public void setBaseActionBarVisible(boolean visible){
        baseActionBar.setVisibility(visible?View.VISIBLE:View.GONE);
    }

    public Activity getContext(){
        return this;
    }

    public void hideBackBtn(){
        btnBack.setVisibility(View.GONE);
    }

    abstract protected int getLayoutId();

    abstract protected View getContentView();

    @OnClick(R.id.btn_function)
    public void onFunctionClick(){

    }


    @Override
    protected void onResume() {
        super.onResume();
        boolean exit = getIntent().getBooleanExtra(IntentHelper.KEY_EXIT,false);
        if (exit){
            finish();
            return;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
