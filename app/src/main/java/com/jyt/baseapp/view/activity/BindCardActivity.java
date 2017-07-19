package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BankCard;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/6/5.
 */

public class BindCardActivity extends BaseActivity {
    @BindView(R.id.input_username)
    EditText inputUsername;
    @BindView(R.id.input_bankname)
    EditText inputBankname;
    @BindView(R.id.input_cardNum)
    EditText inputCardNum;
    @BindView(R.id.btn_done)
    TextView btnDone;


    BankCard bankCard;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_card;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent!=null){
            bankCard = intent.getParcelableExtra(IntentHelper.KEY_BAND_CARD);
        }

        if (bankCard==null){

        }else {
            textTitle.setText("修改银行卡");
            inputBankname.setText(bankCard.openBank);
            inputCardNum.setText(bankCard.bankNumber);
            inputUsername.setText(bankCard.name);
        }

    }


    @OnClick(R.id.btn_done)
    public void onDoneClick(){
        String username = inputUsername.getText().toString();
        String bankName = inputBankname.getText().toString();
        String cardNum = inputCardNum.getText().toString();
        if (TextUtils.isEmpty(username)){
            T.showShort(getContext(),"请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(bankName)){
            T.showShort(getContext(),"请输入开户行");
            return;
        }
        if (TextUtils.isEmpty(cardNum)){
            T.showShort(getContext(),"请输入卡号");
            return;
        }
        String bandCardId = null;
        if (bankCard!=null){
            bandCardId = bankCard.bankId;
        }

        submit(bandCardId,username,bankName,cardNum);
    }


    private void submit(String bandCardId,String username,String bankName,String cardNum){
        Http.bindCard(getContext(),bandCardId, username, bankName, cardNum, new BeanCallback<BaseJson>(getContext()) {
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
