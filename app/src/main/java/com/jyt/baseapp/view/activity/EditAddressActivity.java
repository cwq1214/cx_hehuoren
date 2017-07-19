package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/6/5.
 */

public class EditAddressActivity extends BaseActivity {
    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.input_contract)
    EditText inputContract;
    @BindView(R.id.text_address)
    EditText textAddress;
    @BindView(R.id.layout_selAddress)
    RelativeLayout layoutSelAddress;
    @BindView(R.id.btn_getLocation)
    ImageView btnGetLocation;
    @BindView(R.id.btn_done)
    TextView btnDone;


    String longitude;
    String latitude;
    String name;
    String phone;
    String address;
    String orderId;
    String type;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_address;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        longitude = getIntent().getStringExtra(IntentHelper.KEY_LONGITUDE);
        latitude = getIntent().getStringExtra(IntentHelper.KEY_LATITUDE);
        name = getIntent().getStringExtra(IntentHelper.KEY_NAME);
        phone = getIntent().getStringExtra(IntentHelper.KEY_PHONE);
        address = getIntent().getStringExtra(IntentHelper.KEY_ADDRESS);
        orderId = getIntent().getStringExtra(IntentHelper.KEY_ORDER_ID);
        type = getIntent().getStringExtra(IntentHelper.KEY_TYPE);

        inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name = s.toString();
            }
        });

        inputContract.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phone = s.toString();
            }
        });

        textAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                address = s.toString();
            }
        });


        inputName.setText(name);
        inputContract.setText(phone);
        textAddress.setText(address);



    }

    @OnClick(R.id.btn_getLocation)
    public void onGetLocationClick(){
        IntentHelper.openSelLocationActivity(getContext(),longitude,latitude);
    }

    @OnClick(R.id.btn_done)
    public void onDoneClick(){


        editAddress(type,name,address,phone,latitude,longitude);
    }

    private void setResultAndFinish(){
        Intent intent = new Intent();
        intent.putExtra(IntentHelper.KEY_LONGITUDE,longitude);
        intent.putExtra(IntentHelper.KEY_LATITUDE,latitude);
        intent.putExtra(IntentHelper.KEY_NAME,name);
        intent.putExtra(IntentHelper.KEY_PHONE,phone);
        intent.putExtra(IntentHelper.KEY_ADDRESS,address);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentHelper.REQUIRE_CODE_SEL_LOCATION &&resultCode==RESULT_OK){
            String address = data.getStringExtra(IntentHelper.KEY_ADDRESS);
            LatLng latLng = data.getParcelableExtra(IntentHelper.KEY_LATLNG);
            latitude = latLng.latitude+"";
            longitude = latLng.longitude+"";

            textAddress.setText(address);
        }
    }

    private void editAddress(String type,String name,String address,String phone,String lat,String lon){
        if (TextUtils.isEmpty(lat)){
            lat = "0";
        }
        if (TextUtils.isEmpty(lon)){
            lon = "0";
        }
        Http.setUpdateAddress(getContext(), type, name, phone, address, orderId, lat, lon, new BeanCallback<BaseJson>(getContext()) {
            @Override
            public void onResponse(BaseJson response, int id) {
                super.onResponse(response,id);

                T.showShort(getContext(),response.forUser);
                if (response.ret){
                    setResultAndFinish();
                }
            }
        });
    }
}
