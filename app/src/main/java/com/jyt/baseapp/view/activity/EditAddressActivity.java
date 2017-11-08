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
import com.jyt.baseapp.util.SoftInputUtil;
import com.jyt.baseapp.util.T;
import com.jyt.baseapp.view.dialog.SelLocationDialog;

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
    @BindView(R.id.text_place)
    TextView textPlace;


    String longitude;
    String latitude;
    String name;
    String phone;
    String address;
    String orderId;
    String type;
    String detail;
    String province;
    String city;
    String district;

    String defProvince;
    String defCity;
    String defDistrict;


    SelLocationDialog dialog;

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
        detail = getIntent().getStringExtra(IntentHelper.KEY_DETAIL);
        province = getIntent().getStringExtra(IntentHelper.KEY_PROVINCE);
        city =  getIntent().getStringExtra(IntentHelper.KEY_CITY); ;
        district = getIntent().getStringExtra(IntentHelper.KEY_DISTRICT);
        defProvince = province;
        defCity = city;
        defDistrict = district;


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
                detail = s.toString();
            }
        });

        textAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    textAddress.setText("");
                    SoftInputUtil.showSoftKeyboard(getContext(),textAddress);
                }
            }
        });

        inputName.setText(name);
        inputContract.setText(phone);
        textAddress.setText(detail);



    }

    @OnClick(R.id.layout_selPlace)
    public void onSelPlaceClick(){
        if (dialog==null) {
            dialog = new SelLocationDialog(getContext());
            dialog.setOnSelFinishCallback(new SelLocationDialog.OnSelFinishCallback() {
                @Override
                public void onSelFinish(final String province, final String city, final String district) {
                    EditAddressActivity.this.province = province;
                    EditAddressActivity.this.city = city;
                    EditAddressActivity.this.district = district;
                    EditAddressActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textPlace.setText(province+" "+city+" "+district);
                        }
                    });
                }
            });
        }
        dialog.setSelectLocation(province,city,district);

        dialog.show();
    }

    @OnClick(R.id.btn_getLocation)
    public void onGetLocationClick(){
        if (!TextUtils.isEmpty(province)&&!TextUtils.isEmpty(city)&&!TextUtils.isEmpty(district)&&
                province.equals(defProvince)&&city.equals(defCity)&&district.equals(defDistrict)){
            IntentHelper.openSelAddressActivity(getContext(),latitude,longitude,province,city,district);
        }else {
            IntentHelper.openSelAddressActivity(getContext(),null,null,province,city,district);
        }
    }

    @OnClick(R.id.btn_done)
    public void onDoneClick(){


        editAddress();
    }

    private void setResultAndFinish(){
        Intent intent = new Intent();
        intent.putExtra(IntentHelper.KEY_LONGITUDE,longitude);
        intent.putExtra(IntentHelper.KEY_LATITUDE,latitude);
        intent.putExtra(IntentHelper.KEY_NAME,name);
        intent.putExtra(IntentHelper.KEY_PHONE,phone);
        intent.putExtra(IntentHelper.KEY_ADDRESS,address);
        intent.putExtra(IntentHelper.KEY_PROVINCE,province);
        intent.putExtra(IntentHelper.KEY_CITY,city);
        intent.putExtra(IntentHelper.KEY_DISTRICT,district);
        intent.putExtra(IntentHelper.KEY_DETAIL,detail);
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

    private void editAddress(){
        if (TextUtils.isEmpty(province)||TextUtils.isEmpty(city)||TextUtils.isEmpty(district)){
            T.showShort(getContext(),"请先选址所在地");
            return;
        }

        if (TextUtils.isEmpty(latitude)){
            latitude = "0";
        }
        if (TextUtils.isEmpty(longitude)) {
            longitude = "0";
        }
        detail = textAddress.getText().toString();
        address = province+city+district+detail;

        Http.setUpdateAddress(getContext(), type, name, phone, address, orderId, latitude, longitude,province,city,district,detail, new BeanCallback<BaseJson>(getContext()) {
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
