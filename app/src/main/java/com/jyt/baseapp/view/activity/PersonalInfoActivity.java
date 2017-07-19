package com.jyt.baseapp.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.util.Cache;
import com.jyt.baseapp.util.FileUtil;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;
import com.jyt.baseapp.view.dialog.TextDoneDialog;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by chenweiqi on 2017/6/1.
 */

public class PersonalInfoActivity extends BaseActivity {
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.layout_selImg)
    RelativeLayout layoutSelImg;
    @BindView(R.id.divider1)
    View divider1;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_phone)
    TextView textPhone;
    @BindView(R.id.text_idcard)
    TextView textIdcard;
    @BindView(R.id.btn_logout)
    RelativeLayout btnLogout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_self_info;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Glide.with(getContext()).load(Cache.getInstance().getAvatar()).bitmapTransform(new CenterCrop(getContext()),new CropCircleTransformation(getContext())).into(avatar);
        textName.setText(Cache.getInstance().getUserName());
        textPhone.setText(Cache.getInstance().getPhone());
        textIdcard.setText(Cache.getInstance().getIDCard());

    }
    @OnClick(R.id.layout_selImg)
    public void onSelImgClick(){
        IntentHelper.openPickPhotoActivityForResult(getContext(),123);
    }


    @OnClick(R.id.btn_logout)
    public void onLogoutClick(){
        TextDoneDialog doneDialog = new TextDoneDialog(getContext());
        doneDialog.setTextMsg("确定要退出吗？");
        doneDialog.setDoneListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Cache.getInstance().setUserId(null);
                Cache.getInstance().setToken(null);
                Cache.getInstance().setPhone(null);

                IntentHelper.openLoginActivity(getContext());
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK){
            Uri uri = IntentHelper.afterPickPhoto(data);
            Glide.with(getContext()).load(uri).bitmapTransform(new CenterCrop(getContext()),new CropCircleTransformation(getContext())).into(avatar);

            submitAvatar(FileUtil.getPath(getContext(),uri));
        }
    }


    private void submitAvatar(String filePath){
        Http.modifyAvatar(getContext(), filePath, new BeanCallback<BaseJson>(getContext()) {
            @Override
            public void onResult(boolean success, BaseJson response, Exception e) {
                super.onResult(success, response, e);
                if (success){
                    T.showShort(getContext(),response.forUser);
                }else {
                    T.showShort(getContext(),e.getMessage());
                }
            }
        });
    }
}
