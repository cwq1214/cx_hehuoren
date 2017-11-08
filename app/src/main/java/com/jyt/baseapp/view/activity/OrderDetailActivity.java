package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.SoftInputUtil;

import butterknife.BindView;

/**
 * Created by chenweiqi on 2017/6/5.
 */

public class OrderDetailActivity extends BaseActivity {
    @BindView(R.id.text_company)
    TextView textCompany;
    @BindView(R.id.text_payType)
    TextView textPayType;
    @BindView(R.id.rbtn_online)
    RadioButton rbtnOnline;
    @BindView(R.id.rbtn_offline)
    RadioButton rbtnOffline;
    @BindView(R.id.rbtn_cash)
    RadioButton rbtnCash;
    @BindView(R.id.group_payType)
    RadioGroup groupPayType;
    @BindView(R.id.text_keepValue)
    TextView textKeepValue;
    @BindView(R.id.rbtn_notKeepValue)
    RadioButton rbtnNotKeepValue;
    @BindView(R.id.rbtn_keepValue)
    RadioButton rbtnKeepValue;
    @BindView(R.id.group_keepValue)
    RadioGroup groupKeepValue;
    @BindView(R.id.input_cost)
    EditText inputCost;



    String keepValue;
    String payType;
    String cost;
    String company;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnFunction.setText("确定");
        groupKeepValue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String text = ((RadioButton) group.findViewById(checkedId)).getText().toString();
                textKeepValue.setText(text);
            }
        });
        groupPayType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String text = ((RadioButton) group.findViewById(checkedId)).getText().toString();
                textPayType.setText(text);
            }
        });
        Intent intent = getIntent();
        if (intent!=null){
            payType = intent.getStringExtra(IntentHelper.KEY_PAY_TYPE);
            keepValue = intent.getStringExtra(IntentHelper.KEY_KEEP_VALUE);
            cost = intent.getStringExtra(IntentHelper.KEY_COST);
            company = intent.getStringExtra(IntentHelper.KEY_COMPANY);


            for (int i=0;i<groupKeepValue.getChildCount();i++){
                if (((RadioButton) groupKeepValue.getChildAt(i)).getText().toString().equals(keepValue)){
                    ((RadioButton) groupKeepValue.getChildAt(i)).setChecked(true);
                    break;
                }
            }
            for (int i=0;i<groupPayType.getChildCount();i++){
                if (((RadioButton) groupPayType.getChildAt(i)).getText().toString().equals(payType)){
                    ((RadioButton) groupPayType.getChildAt(i)).setChecked(true);
                    break;
                }
            }
        }


        textKeepValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keepValue = s.toString();
            }
        });
        textPayType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                payType = s.toString();
            }
        });
        inputCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cost = s.toString();
            }
        });



        textKeepValue.setText(keepValue);
        textPayType.setText(payType);
        textCompany.setText(company);
        inputCost.setText(cost);

        inputCost.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    inputCost.setText("");
                    SoftInputUtil.showSoftKeyboard(getContext(),inputCost);
                }
            }
        });
    }



    @Override
    public void onFunctionClick() {
        super.onFunctionClick();
        setResultAndFinish();
    }

    private void setResultAndFinish(){
        Intent intent = new Intent();
        intent.putExtra(IntentHelper.KEY_COMPANY,company);
        intent.putExtra(IntentHelper.KEY_PAY_TYPE,payType);
        intent.putExtra(IntentHelper.KEY_KEEP_VALUE,keepValue);
        intent.putExtra(IntentHelper.KEY_COST,cost);
        setResult(RESULT_OK,intent);
        finish();
    }


}
