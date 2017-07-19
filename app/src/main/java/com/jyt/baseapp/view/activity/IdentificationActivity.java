package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/6/15.
 */

public class IdentificationActivity extends BaseActivity {
    @BindView(R.id.img_idCardFace)
    ImageView imgIdCardFace;
    @BindView(R.id.img_idCardBack)
    ImageView imgIdCardBack;
    @BindView(R.id.img_drivingPermit)
    ImageView imgDrivingPermit;
    @BindView(R.id.img_drivingLicense)
    ImageView imgDrivingLicense;
    @BindView(R.id.img_withCar)
    ImageView imgwithCar;
    @BindView(R.id.btn_done)
    TextView btnDone;


    Bitmap b1;
    Bitmap b2;
    Bitmap b3;
    Bitmap b4;
    Bitmap b5;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_identification;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @OnClick({R.id.img_idCardFace,R.id.img_idCardBack,R.id.img_drivingPermit,R.id.img_drivingLicense,R.id.img_withCar})
    public void onTakePhotoClick(View view){
        int requestCode = -1;
        switch (view.getId()){
            case R.id.img_idCardFace:
                requestCode = 0;
                break;
            case R.id.img_idCardBack:
                requestCode = 1;
                break;
            case R.id.img_drivingPermit:
                requestCode = 2;
                break;
            case R.id.img_drivingLicense:
                requestCode = 3;
                break;
            case R.id.img_withCar:
                requestCode = 4;
                break;
        }
        IntentHelper.openTakePhotoActivityForResult(getContext(),requestCode );
    }

    @OnClick(R.id.btn_done)
    public void onBtnDoneClick(){
        if (b1==null
                ||b2==null
                ||b3==null
                ||b4==null
                ||b5==null){
            T.showShort(getContext(),"请拍完照再上传");
            return;
        }

        Http.verificationUpload(getContext(), b1, b2, b3, b4, b5, new BeanCallback<BaseJson>(getContext()) {
            @Override
            public void onResult(boolean success, BaseJson response, Exception e) {
                super.onResult(success, response, e);
                if (success){
                    T.showShort(getContext(),response.forUser);
                    if (response.ret){
                        T.showShort(getContext(),"您的审核已提交，请耐心等待");
                        finish();
                    }
                }else {
                    T.showShort(getContext(),e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bitmap resultBitmap = IntentHelper.getTakePhotoBitMap(getContext(),data);
            if (requestCode == 0){
                b1 = resultBitmap;
                imgIdCardFace.setImageBitmap(resultBitmap);
            }else if (requestCode == 1){
                b2 = resultBitmap;
                imgIdCardBack.setImageBitmap(resultBitmap);
            }else if (requestCode == 2){
                b3 = resultBitmap;
                imgDrivingPermit.setImageBitmap(resultBitmap);
            }else if (requestCode == 3){
                b4 = resultBitmap;
                imgDrivingLicense.setImageBitmap(resultBitmap);
            }else if (requestCode == 4){
                b5 = resultBitmap;
                imgwithCar.setImageBitmap(resultBitmap);
            }
        }
    }
}
