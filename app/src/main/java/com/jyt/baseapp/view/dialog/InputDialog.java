package com.jyt.baseapp.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

import com.jyt.baseapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/6/21.
 */

public class InputDialog extends Dialog {
    @BindView(R.id.text_msg)
    TextView textMsg;
    @BindView(R.id.input_content)
    EditText inputContent;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;
    @BindView(R.id.btn_done)
    TextView btnDone;

    String title;

    int inputBackgroundDrawableId = -1;
    boolean inputSingleLine = true;
    int inputMinHeight = 0;

    OnClickListener onCancelClickListener;
    OnClickListener onDoneClickListener;
    public InputDialog(@NonNull Context context) {
        super(context, R.style.customDialog);


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_input);
        ButterKnife.bind(this);

        textMsg.setText(title);

        if ( inputBackgroundDrawableId!= -1 ){
            inputContent.setBackground(getContext().getResources().getDrawable(inputBackgroundDrawableId));
        }

        inputContent.setMinHeight(inputMinHeight);

        inputContent.setSingleLine(inputSingleLine);

    }

    @OnClick(R.id.btn_cancel)
    public void onCancelClick(){
        if (onCancelClickListener!=null){
            onCancelClickListener.onClick(this,0);
        }else {
            dismiss();
        }
    }
    @OnClick(R.id.btn_done)
    public void onDoneClick(){
        if (onDoneClickListener != null){
            onDoneClickListener.onClick(this,0);
        }else {
            dismiss();
        }
    }

    public void setInputBackgroundDrawableId(int id){
        inputBackgroundDrawableId = id;
    }

    public void setInputMinHeight(int inputMinHeight) {
        this.inputMinHeight = inputMinHeight;
    }

    public void setInputSingleLine(boolean inputSingleLine) {
        this.inputSingleLine = inputSingleLine;
    }

    public void setTitle(String text){
        title = text;
    }

    public String getInputContent(){
        return inputContent.getText().toString();
    }

    public void setOnCancelClickListener(OnClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }

    public void setOnDoneClickListener(OnClickListener onDoneClickListener) {
        this.onDoneClickListener = onDoneClickListener;
    }
}
