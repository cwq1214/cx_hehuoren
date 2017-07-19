package com.jyt.baseapp.view.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/5/31.
 */

public class QRCodeDialog extends AlertDialog {
    @BindView(R.id.img_qrcode)
    ImageView imgQrcode;
    @BindView(R.id.btn_close)
    TextView btnClose;

    boolean getQRCode = true;

    Bitmap bitmap;
    public QRCodeDialog(@NonNull Context context) {
        super(context, R.style.customDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_qrcode);
        ButterKnife.bind(this);

        if (!getQRCode){
            imgQrcode.setImageBitmap(bitmap);
        }

    }

    @OnClick(R.id.btn_close)
    public void onCloseClick(){
        dismiss();
    }

    @Override
    public void show() {
        if (getQRCode) {
            getQRCode();
        }else {
            super.show();
        }
    }

    public void setBitmap(Bitmap bitmap){
        getQRCode = false;
        this.bitmap = bitmap;
    }

    private void getQRCode(){
        Http.getQrCode(getContext(), new BeanCallback<File>(getContext()) {
            @Override
            public void onResponse(File response, int id) {
                super.onResponse(response,id);

                QRCodeDialog.super.show();
                imgQrcode.setImageBitmap(BitmapFactory.decodeFile(response.getPath()));
            }
        });
    }
}
