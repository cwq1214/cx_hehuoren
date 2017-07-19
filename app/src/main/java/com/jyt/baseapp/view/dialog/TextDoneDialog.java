package com.jyt.baseapp.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.TextView;

import com.jyt.baseapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/5/31.
 */

public class TextDoneDialog extends AlertDialog {
    @BindView(R.id.btn_cancel)
    TextView btnCancel;
    @BindView(R.id.btn_done)
    TextView btnDone;
    @BindView(R.id.text_msg)
    TextView textMsg;

    String msgStr;

    OnClickListener doneListener;
    OnClickListener cancelListener;

    public TextDoneDialog(@NonNull Context context) {
        super(context, R.style.customDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_off_work);
        ButterKnife.bind(this);

//        if (!TextUtils.isEmpty(msgStr)){
            textMsg.setText(msgStr);
//        }
    }

    public void setTextMsg(String text){
        this.msgStr = text;
    }

    public void setDoneListener(OnClickListener doneListener) {
        this.doneListener = doneListener;
    }

    public void setCancelListener(OnClickListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    @OnClick(R.id.btn_cancel)
    public void onCancelClick(){
        if (cancelListener!=null){
            cancelListener.onClick(this,0);
        }
    }

    @OnClick(R.id.btn_done)
    public void onDoneClick(){
        if (doneListener!=null){
            doneListener.onClick(this,1);
        }
    }


}
