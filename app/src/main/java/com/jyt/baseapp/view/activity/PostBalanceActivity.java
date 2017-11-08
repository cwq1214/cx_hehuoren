package com.jyt.baseapp.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BankCard;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.entity.SelfInfoResult;
import com.jyt.baseapp.util.Cache;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;
import com.jyt.baseapp.view.dialog.ShowTextDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/6/5.
 */

public class PostBalanceActivity extends BaseActivity {
    @BindView(R.id.text_cardName)
    TextView textCardName;
    @BindView(R.id.text_cardNum)
    TextView textCardNum;
    @BindView(R.id.layout_selCard)
    RelativeLayout layoutSelCard;
    @BindView(R.id.label_tixian)
    TextView labelTixian;
    @BindView(R.id.input_money)
    EditText inputMoney;
    @BindView(R.id.text_money)
    TextView textMoney;
    @BindView(R.id.btn_done)
    TextView btnDone;


    List<BankCard> cardList;
    BankCard selBankCard;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_post_balance;
    }

    @Override
    protected View getContentView() {
        return null;
    }


    @OnClick(R.id.layout_selCard)
    public void onSelCardClick(){
        IntentHelper.openSelBankCardActivityForResult(getContext(),selBankCard);
    }

    @OnClick(R.id.btn_done)
    public void onDoneClick(){
        postBalance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCardList();
        getSelfInfo();
    }

    private void getCardList(){
        Http.getBankCardList(getContext(), new BeanCallback<BaseJson<List<BankCard>>>(getContext()) {
            @Override
            public void onResponse(BaseJson<List<BankCard>> response, int id) {
                super.onResponse(response,id);

                T.showShort(getContext(),response.forUser);
                cardList = response.data;
                if (response.ret){
                    if (cardList!=null
                            &&cardList.size()!=0) {
                        selBankCard = cardList.get(0);
                        textCardName.setText(selBankCard.openBank);
                        textCardNum.setText(selBankCard.bankNumber);
                    }else {
                        ShowTextDialog dialog = new ShowTextDialog(getContext());
                        dialog.setText("还绑定银行卡，请先绑定");
                        dialog.setOnClickListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        dialog.show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentHelper.REQUIRE_CODE_SEL_BAND_CARD
                && resultCode == RESULT_OK){
            selBankCard = data.getParcelableExtra(IntentHelper.KEY_BAND_CARD);
            textCardName.setText(selBankCard.openBank);
            textCardNum.setText(selBankCard.bankNumber);
        }
    }

    public void getSelfInfo(){
        Http.getSelfInfo(getContext(), new BeanCallback<BaseJson<SelfInfoResult>>(getContext()) {
            @Override
            public void onResponse(BaseJson<SelfInfoResult> response, int id) {
                super.onResponse(response,id);

                T.showShort(getContext(),response.forUser);

                Cache appCache = Cache.getInstance();
                appCache.setAvatar(response.data.partnerImg);
                appCache.setIDCard(response.data.idCard);
                appCache.setUserName(response.data.partnerName);

                textMoney.setText(response.data.cash);
            }
        });
    }

    public void postBalance(){
        if (selBankCard==null){
            T.showShort(getContext(),"请先选择银行卡");
            return;
        }
        if (TextUtils.isEmpty(inputMoney.getText().toString())){
            T.showShort(getContext(),"请输入提现金额");
            return;
        }


        Http.postBalance(getContext(), selBankCard.bankId, inputMoney.getText().toString(), new BeanCallback<BaseJson>(getContext()) {
            @Override
            public void onResult(boolean success, BaseJson response, Exception e) {
                super.onResult(success, response, e);
                if (success){
//                    T.showShort(getContext(),response.forUser);
                    if (response.ret){
                        T.showShort(getContext(),"操作成功");
                        finish();
                    }else{
                        T.showShort(getContext(),"操作失败");
                    }
                }else{
                    T.showShort(getContext(),"操作失败");
                }
            }
        });
    }
}
