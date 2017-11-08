package com.jyt.baseapp.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.bean.WeiXinPayResult;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.entity.CheckDepositResult;
import com.jyt.baseapp.entity.Deposit;
import com.jyt.baseapp.entity.DepositPayResult;
import com.jyt.baseapp.entity.SelfInfoResult;
import com.jyt.baseapp.util.Cache;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;
import com.jyt.baseapp.view.dialog.PayDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/6/2.
 */

public class DepositAdjustActivity extends BaseActivity {
    @BindView(R.id.text_currentLevel)
    TextView textCurrentLevel;
    @BindView(R.id.text_adjustLevel)
    TextView textAdjustLevel;
    @BindView(R.id.label_balance)
    TextView labelBalance;
    @BindView(R.id.cb_userBalance)
    CheckBox cbUserBalance;
    @BindView(R.id.layout_userBalance)
    RelativeLayout layoutUserBalance;
    @BindView(R.id.text_moneyLabel)
    TextView textMoneyLabel;
    @BindView(R.id.text_payMoney)
    TextView textPayMoney;
    @BindView(R.id.btn_done)
    TextView btnDone;
    @BindView(R.id.text_tips)
    TextView textTips;
    @BindView(R.id.text_userBalance)
    TextView textUserBalance;


    String depositId;
    CheckDepositResult checkDepositResult;
    SelfInfoResult selfInfoResult;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_deposit_adjust;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @OnClick(R.id.btn_done)
    public void onDoneClick(){
        if (Double.valueOf(checkDepositResult.payMoney)==0){//返回
            refundMoney();
            return;
        }
        if (!(Double.valueOf(checkDepositResult.payMoney)==0)){
            if (cbUserBalance.isChecked()) {
                if (!TextUtils.isEmpty(selfInfoResult.cash)
                        &&(Double.valueOf(selfInfoResult.cash)-Double.valueOf(checkDepositResult.payMoney)>=0)) {
                    refundMoney();
                    return;
                }
            }

            PayDialog payDialog = new PayDialog(getContext());
            payDialog.setOnDoneClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which==1){
                        beforeWeiXinPay();
                    }else if (which==2){
                        beforeALiPay();
                    }
                    dialog.dismiss();
                }
            });
            payDialog.show();
        }


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        depositId =  getIntent().getStringExtra(IntentHelper.KEY_DEPOSIT_ID);
        checkDeposit(depositId);
        getSelfInfo();
    }


    @OnClick(R.id.layout_userBalance)
    public void onUseBalanceClick(){
        cbUserBalance.setChecked(!cbUserBalance.isChecked());
    }


    private void checkDeposit(String depositId){
        Http.checkDeposit(getContext(), depositId, new BeanCallback<BaseJson<CheckDepositResult>>(getContext()) {
            @Override
            public void onResponse(BaseJson<CheckDepositResult> response, int id) {
                super.onResponse(response,id);
                T.showShort(getContext(),response.forUser);
                checkDepositResult = response.data;
                if (response.ret){
                    textCurrentLevel.setText(checkDepositResult.oldTitle);
                    textAdjustLevel.setText(checkDepositResult.title);

                    if (!TextUtils.isEmpty(checkDepositResult.payMoney)
                            &&Double.valueOf(checkDepositResult.payMoney)==0){//降级
                        layoutUserBalance.setVisibility(View.GONE);
                        textMoneyLabel.setText("返回金额");
                        btnDone.setText("确认");
                        textPayMoney.setText(checkDepositResult.returnMoney);
                    }else {//升级
                        textTips.setVisibility(View.GONE);
//                        textUserBalance.setText(selfInfoResult.cash);
                        textPayMoney.setText(checkDepositResult.payMoney);
                    }

                }
            }
        });
    }

    public void getSelfInfo(){
        Http.getSelfInfo(getContext(), new BeanCallback<BaseJson<SelfInfoResult>>(getContext()) {
            @Override
            public void onResponse(BaseJson<SelfInfoResult> response, int id) {
                super.onResponse(response,id);

                T.showShort(getContext(),response.forUser);

                selfInfoResult = response.data;

                textUserBalance.setText(selfInfoResult.cash);

            }
        });
    }

        //退钱
    private void refundMoney(){
        Http.buyDeposit(getContext(), depositId, new BeanCallback<BaseJson>(getContext()) {
            @Override
            public void onResult(boolean success, BaseJson response, Exception e) {
                super.onResult(success, response, e);
                if (success){
                    if (response.ret){
                        T.showShort(getContext(),"操作成功");
                        finish();
                    }else {
                        T.showShort(getContext(),"操作失败");
                    }
                }else {
                    T.showShort(getContext(),"操作失败");
                }
            }
        });
    }


    private void beforeALiPay(){

        Http.aLiBeforeBuyDeposit(getContext(), cbUserBalance.isChecked() ? "1" : "2", depositId, new BeanCallback<BaseJson<DepositPayResult>>(getContext()) {
            @Override
            public void onResult(boolean success, BaseJson<DepositPayResult> response, Exception e) {
                super.onResult(success, response, e);
                if (success){
                    T.showShort(getContext(),response.forUser);
                    if (response.ret){
                        String cash = "";
                        if (cbUserBalance.isChecked()){
                            cash = Float.valueOf(checkDepositResult.payMoney)-Float.valueOf(selfInfoResult.cash) +"";
                        }else {
                            cash = checkDepositResult.payMoney;
                        }

                        IntentHelper.openPayActivityForResult_ALI(getContext(),response.data.sign,"购买保证金",cash+"",true);
                    }
                }else {
                    T.showShort(getContext(),e.getMessage());
                }
            }
        });
    }
    private void beforeWeiXinPay(){
        Http.WeiXinBeforeBuyDeposit(getContext(), cbUserBalance.isChecked() ? "1" : "2", depositId, new BeanCallback<BaseJson<WeiXinPayResult>>(getContext()) {
            @Override
            public void onResult(boolean success, BaseJson<WeiXinPayResult> response, Exception e) {
                super.onResult(success, response, e);
                if (success){
                    T.showShort(getContext(),response.forUser);
                    if (response.ret){
                        String cash = "";
                        if (cbUserBalance.isChecked()){
                            cash = Float.valueOf(checkDepositResult.payMoney)-Float.valueOf(selfInfoResult.cash) +"";
                        }else {
                            cash = checkDepositResult.payMoney;
                        }

                        IntentHelper.openPayActivityForResult_WeiXin(getContext(),response.data.partnerId
                                ,response.data.prepayId,response.data.timeStamp+"",response.data.paySign,response.data.packageValue
                                ,response.data.nonceStr,"购买保证金",cash,true);
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
        if (requestCode == IntentHelper.REQUIRE_PAY && resultCode == RESULT_OK){
            boolean payResult = data.getBooleanExtra(IntentHelper.KEY_PAY_RESULT,false);
            if (payResult){
                T.showShort(getContext(),"购买成功");

            }else {
                T.showShort(getContext(),"购买失败");
            }
            finish();
        }
    }
}
