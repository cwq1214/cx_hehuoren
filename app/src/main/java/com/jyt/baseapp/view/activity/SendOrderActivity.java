package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Output;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.SourceData;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.entity.ScanIdCardResult;
import com.jyt.baseapp.entity.SendOrderDetailResult;
import com.jyt.baseapp.entity.SendSelItemResult;
import com.jyt.baseapp.util.CaptureManager;
import com.jyt.baseapp.util.DensityUtil;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.ScreenUtils;
import com.jyt.baseapp.util.T;
import com.jyt.baseapp.view.dialog.QRCodeDialog;
import com.jyt.baseapp.view.dialog.ShowTextDialog;
import com.jyt.baseapp.view.dialog.TextDoneDialog;
import com.jyt.baseapp.view.widget.RadioGroupEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by chenweiqi on 2017/6/2.
 */

public class SendOrderActivity extends BaseActivity {
    @BindView(R.id.scannerView)

    DecoratedBarcodeView scannerView;
    @BindView(R.id.text_logisticsNum)
    EditText textLogisticsNum;
    @BindView(R.id.view_tabLayout)
    TabLayout viewTabLayout;
    @BindView(R.id.text_result)
    EditText textResult;
    @BindView(R.id.radioGroup)
    RadioGroupEx radioGroup;
    @BindView(R.id.text_idcardText)
    TextView textIdcardText;
    @BindView(R.id.btn_scanIdCard)
    TextView btnScanIdCard;
    @BindView(R.id.btn_done)
    TextView btnDone;
    @BindView(R.id.layout_idCard)
    RelativeLayout layoutIdCard;
    @BindView(R.id.label)
    TextView label;

    private CaptureManager capture;

    SendSelItemResult sendSelItemResult;
    String orderId;
    String signPerson;
    String failedReason;
    String idCard;
    Bitmap idCardBitmap;
    String scanOrderNo;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_order;
    }

    @Override
    protected View getContentView() {
        return null;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        capture = new CaptureManager(this, scannerView);
        capture.setOnScanResultCallback(new CaptureManager.OnScanResultCallback() {
            @Override
            public void result(Intent intent) {
                //扫码结果回调
                IntentResult result = IntentIntegrator.parseActivityResult(IntentIntegrator.REQUEST_CODE, RESULT_OK, intent);
                //扫描结果
                String content = result.getContents();

                scanOrderNo = content;
                setOrderNo(scanOrderNo);
                getOrderId(scanOrderNo);

                scannerView.resume();

            }
        });
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decodeContinuous();

        viewTabLayout.addTab(viewTabLayout.newTab().setText("成功"));
        viewTabLayout.addTab(viewTabLayout.newTab().setText("失败"));

        viewTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                radioGroup.removeAllViews();
                if (tab.getText().equals("成功")){
                    label.setText("签收人（填写／选择）");
                    btnScanIdCard.setVisibility(View.VISIBLE);
                    layoutIdCard.setVisibility(View.VISIBLE);
                    createRadioBtn(sendSelItemResult.singer);

                    textResult.setText(signPerson);
                }else if (tab.getText().equals("失败")){
                    label.setText("失败原因（填写／选择）");
                    btnScanIdCard.setVisibility(View.GONE);
                    layoutIdCard.setVisibility(View.GONE);
                    createRadioBtn(sendSelItemResult.reason);
                    textResult.setText(failedReason);
                }
                changeRadioGroup();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String text = ((RadioButton) group.findViewById(checkedId)).getText().toString();
                textResult.setText(text);
            }
        });

        textResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (viewTabLayout.getSelectedTabPosition() == 0){
                    signPerson = s.toString();
                }else if(viewTabLayout.getSelectedTabPosition() == 1 ){
                    failedReason = s.toString();
                }
            }
        });

        getSelItem();

        Intent intent = getIntent();
        if (intent!=null){
            orderId = intent.getStringExtra(IntentHelper.KEY_ORDER_ID);
            if (!TextUtils.isEmpty(orderId)){
                getSendOrderDetail(orderId);
            }
        }

        textLogisticsNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                scanOrderNo = s.toString();
                if (TextUtils.isEmpty(scanOrderNo))
                    getOrderId(scanOrderNo);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void createRadioBtn(List<String> msg){
        for (int i=0;i<msg.size();i++){
            RadioButton radioButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.layout_radiobutton,radioGroup,false);
            radioButton.setText(msg.get(i));
            radioGroup.addView(radioButton);
        }
    }

    private void changeRadioGroup(){
        int  childCount = radioGroup.getChildCount();

        int dp_10 = DensityUtil.dpToPx(getContext(),10);
        int dp_12 = DensityUtil.dpToPx(getContext(),12);
//        radioGroup.setPadding(dp_10,dp_12,dp_10,dp_12);
        int itemWidth = (ScreenUtils.getScreenWidth(getContext()) - DensityUtil.dpToPx(getContext(),80))/3;
        for (int i=0;i<childCount;i++){
            RadioButton radioButton = ((RadioButton) radioGroup.getChildAt(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, RadioGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(dp_10,dp_12,dp_10,dp_12);
            radioButton.setLayoutParams(params);
        }
    }

    @OnClick(R.id.btn_scanIdCard)
    public void onScanIdCard(){
        Bitmap bitmap = null;
        try {
            Field field = CameraPreview.class.getDeclaredField("textureView");
            field.setAccessible(true);
            TextureView textureView = (TextureView) field.get(scannerView.getBarcodeView());

            bitmap = textureView.getBitmap(textureView.getWidth(),textureView.getWidth());

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

//        rootView.setDrawingCacheEnabled(true);

//        QRCodeDialog dialog = new QRCodeDialog(getContext());
//        dialog.setBitmap(bitmap);
//        dialog.show();
//        rootView.setDrawingCacheEnabled(false);
        Http.ocrIdCard(bitmap, "face", new BeanCallback<ScanIdCardResult>(getContext()) {
            @Override
            public void onResponse(ScanIdCardResult response, int id) {
                super.onResponse(response, id);
                String json = ((ScanIdCardResult.Output) response.outputs.get(0)).outputValue.dataValue;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    signPerson = jsonObject.getString("name");
                    idCard = jsonObject.getString("num");

                    setSignPerson(signPerson);
                    setIdCardNum(idCard);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Call call, Exception e, int id) {
//                super.onError(call, e, id);
                ShowTextDialog dialog = new ShowTextDialog(getContext());
                dialog.setText("识别失败");
                dialog.show();
            }
        });
//        IntentHelper.openTakePhotoActivityForResult(getContext());
    }

    public void setOrderNo(final String orderNo){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textLogisticsNum.setText(orderNo);
            }
        });
    }

    public void setSignPerson(final String signPerson){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textResult.setText(signPerson);
            }
        });
    }

    public void setIdCardNum(final String idCard){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textIdcardText.setText(idCard);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    private void getSelItem(){
        Http.getSendSelItemList(getContext(), new BeanCallback<BaseJson<SendSelItemResult>>(getContext()) {
            @Override
            public void onResponse(BaseJson<SendSelItemResult> response, int id) {
                super.onResponse(response,id);

                T.showShort(getContext(),response.forUser);
                if (response.ret){
                    sendSelItemResult = response.data;

                    radioGroup.removeAllViews();
                    createRadioBtn(sendSelItemResult.singer);
                    changeRadioGroup();

                }
            }
        });
    }

    public void getOrderId(String orderNo){
        Http.getOrderIdByOrderNo(getContext(), orderNo, new BeanCallback<BaseJson<SendSelItemResult>>(getContext()) {
            @Override
            public void onResponse(BaseJson<SendSelItemResult> response, int id) {
                super.onResponse(response, id);
                T.showShort(getContext(),response.forUser);
                if (response.data!=null) {
                    orderId = response.data.orderId;
                }else {
                    orderId = null;
                }



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentHelper.REQUIRE_CODE_TAKE_PHOTO && resultCode == RESULT_OK){
             Bitmap resultBitmap = IntentHelper.getTakePhotoBitMap(getContext(),data);
            Http.ocrIdCard(resultBitmap, "face", new BeanCallback<ScanIdCardResult>(getContext()) {
                @Override
                public void onResponse(ScanIdCardResult response, int id) {
                    super.onResponse(response, id);

                    System.out.println(response.toString());
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    super.onError(call, e, id);
                }
            });

        }
    }

    private void getSendOrderDetail(String orderId){
        Http.getSendOrderDetail(getContext(), orderId, new BeanCallback<BaseJson<SendOrderDetailResult>>(getContext()) {
            @Override
            public void onResponse(BaseJson<SendOrderDetailResult> response, int id) {
                super.onResponse(response,id);

                T.showShort(getContext(),response.forUser);
                if (response.ret){
                     textLogisticsNum.setText(response.getData().trackingNo);
                }
            }
        });
    }

    @OnClick(R.id.btn_done)
    public void onDoneClick(){
        if (TextUtils.isEmpty(textLogisticsNum.getText().toString())){
            T.showShort(getContext(),"请输入或扫描运单号");
            return;
        }
        if (viewTabLayout.getSelectedTabPosition() == 0) {
            if (TextUtils.isEmpty(signPerson)) {
                T.showShort(getContext(), "请选择或扫描签收人");
                return;
            }
        }else {
            if (TextUtils.isEmpty(failedReason)){
                T.showShort(getContext(),"请选择或填写失败原因");
                return;
            }
        }
        confirmOrder();
    }

    public void confirmOrder(){
        Http.finishSendOrder(getContext(), orderId, viewTabLayout.getSelectedTabPosition() == 0 ? "1" : "0", failedReason, signPerson,textLogisticsNum.getText().toString(), textIdcardText.getText().toString(),idCardBitmap, new BeanCallback<BaseJson>(getContext()) {
            @Override
            public void onResponse(BaseJson response, int id) {
                super.onResponse(response,id);

                T.showShort(getContext(),response.forUser);
                if (response.ret){
                    finish();
                }
            }
        });
    }


}
