package com.jyt.baseapp.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.jyt.baseapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/6/1.
 */

public class ShowTextDialog extends AlertDialog {

    @BindView(R.id.text_msg)
    TextView textMsg;
    @BindView(R.id.btn_done)
    TextView btnDone;


    OnClickListener onClickListener;

    String text;
    public ShowTextDialog(@NonNull Context context) {
        super(context, R.style.customDialog);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_show_text);
        ButterKnife.bind(this);

        textMsg.setText(text);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @OnClick(R.id.btn_done)
    public void onDoneClick() {
        if (onClickListener != null) {
            onClickListener.onClick(this, 0);
        }else {
            dismiss();
        }
    }

}
