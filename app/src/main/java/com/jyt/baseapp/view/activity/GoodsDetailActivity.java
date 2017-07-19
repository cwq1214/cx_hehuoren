package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.util.DensityUtil;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.ScreenUtils;
import com.jyt.baseapp.view.widget.RadioGroupEx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenweiqi on 2017/6/5.
 */

public class GoodsDetailActivity extends BaseActivity {
    @BindView(R.id.text_type)
    TextView textType;
    @BindView(R.id.group_type)
    RadioGroupEx groupType;
    @BindView(R.id.text_weight)
    TextView textWeight;
    @BindView(R.id.group_weight)
    RadioGroupEx groupWeight;
    @BindView(R.id.text_volume)
    TextView textVolume;
    @BindView(R.id.group_volume)
    RadioGroupEx groupVolume;
    @BindView(R.id.input_money)
    EditText inputMoney;
    @BindView(R.id.text_volume_symbol)
    TextView textVolumeSymbol;

    String currentType;
    List type;
    String currentWeight;
    List weight;
    String currentVolume;
    List volume;
    String currentValue;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected View getContentView() {
        return null;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textVolumeSymbol.setText(Html.fromHtml("m<sup>3</sup>"));
        btnFunction.setVisibility(View.VISIBLE);
        btnFunction.setText("确定");

        groupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String msg = (String) group.findViewById(checkedId).getTag();
                textType.setText(msg);
            }
        });
        groupWeight.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String msg = (String) group.findViewById(checkedId).getTag();
                textWeight.setText(msg);
            }
        });
        groupVolume.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String msg = (String) group.findViewById(checkedId).getTag();
                textVolume.setText(msg);
            }
        });
        Intent intent = getIntent();
        if (intent!=null){
            currentType = intent.getStringExtra(IntentHelper.KEY_TYPE);
            currentWeight = intent.getStringExtra(IntentHelper.KEY_WEIGHT);
            currentVolume = intent.getStringExtra(IntentHelper.KEY_VOLUME);
            currentValue = intent.getStringExtra(IntentHelper.KEY_VALUE);

            type = intent.getStringArrayListExtra(IntentHelper.KEY_LIST_TYPE);
            weight = intent.getStringArrayListExtra(IntentHelper.KEY_LIST_WEIGHT);
            volume = intent.getStringArrayListExtra(IntentHelper.KEY_LIST_VOLUME);

            buildButton(type,0);
            buildButton(weight,1);
            buildButton(volume,2);

            for (int i=0;i<groupType.getChildCount();i++){
                if (((RadioButton) groupType.getChildAt(i)).getText().toString().equals(currentType)){
                    ((RadioButton) groupType.getChildAt(i)).setChecked(true);
                    break;
                }
            }
            for (int i=0;i<groupWeight.getChildCount();i++){
                if (((RadioButton) groupWeight.getChildAt(i)).getTag().toString().equals(currentWeight)){
                    ((RadioButton) groupWeight.getChildAt(i)).setChecked(true);
                    break;
                }
            }
            for (int i=0;i<groupVolume.getChildCount();i++){
                if (((RadioButton) groupVolume.getChildAt(i)).getTag().toString().equals(currentVolume)){
                    ((RadioButton) groupVolume.getChildAt(i)).setChecked(true);
                    break;
                }
            }
        }









        textType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentType = s.toString();
            }
        });
        textWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentWeight = s.toString();
            }
        });
        textVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentVolume = s.toString();
            }
        });
        inputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentValue = s.toString();
            }
        });

        textType.setText(currentType);
        textWeight.setText(currentWeight);
        textVolume.setText(currentVolume);
        inputMoney.setText(currentValue);
    }

    @Override
    public void onFunctionClick() {
        Intent intent = new Intent();
        intent.putExtra(IntentHelper.KEY_TYPE,currentType);
//        intent.putCharSequenceArrayListExtra(IntentHelper.KEY_LIST_TYPE, (ArrayList<CharSequence>) type);
        intent.putExtra(IntentHelper.KEY_WEIGHT,currentWeight);
//        intent.putCharSequenceArrayListExtra(IntentHelper.KEY_LIST_WEIGHT, (ArrayList<CharSequence>) weight);
        intent.putExtra(IntentHelper.KEY_VOLUME,currentVolume);
//        intent.putCharSequenceArrayListExtra(IntentHelper.KEY_LIST_VOLUME, (ArrayList<CharSequence>) volume);
        intent.putExtra(IntentHelper.KEY_VALUE,currentValue);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void buildButton(List<String> msg, int type){
        int dp_10 = DensityUtil.dpToPx(getContext(),10);
        int width = (ScreenUtils.getScreenWidth(getContext())-DensityUtil.dpToPx(getContext(),80))/3;
        RadioGroupEx groupEx;
        String formatText;
        if (type == 0 ){
            groupEx = groupType;
            formatText = "%s";
        }else if (type == 1 ){
            groupEx = groupWeight;
            formatText = "%sKG";

        }else if(type == 2){
            groupEx = groupVolume;
            formatText = "%sm<sup>3</sup>";
        }else {
            return;
        }

        for (int i=0,max = msg.size();i<max;i++){

            RadioButton radioButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.layout_radiobutton,groupEx,false);

            RadioGroupEx.LayoutParams params = new RadioGroupEx.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(dp_10,dp_10,dp_10,dp_10);
            radioButton.setLayoutParams(params);

            radioButton.setTag(msg.get(i));
            radioButton.setText(Html.fromHtml(String.format(formatText,msg.get(i))));


            groupEx.addView(radioButton);
        }
    }
}
