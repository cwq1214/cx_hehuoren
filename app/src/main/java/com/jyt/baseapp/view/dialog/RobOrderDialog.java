package com.jyt.baseapp.view.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.entity.OrderMessage;
import com.jyt.baseapp.util.T;
import com.jyt.baseapp.view.activity.MainActivity;
import com.jyt.baseapp.view.fragment.OrderFragment;
import com.jyt.baseapp.view.fragment.OrderPickUpFragment;
import com.jyt.baseapp.view.fragment.OrderSendFragment;

import org.apache.http.params.HttpParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/6/2.
 */

public class RobOrderDialog extends AlertDialog {
    @BindView(R.id.msg)
    TextView msg;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text_deadline)
    TextView textDeadline;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;
    @BindView(R.id.btn_done)
    TextView btnDone;

    OrderMessage orderMessage;

    OnClickListener onCancelClickListener;
    OnClickListener onDoneClickListener;


    int type = -1;
    public RobOrderDialog(@NonNull Context context) {
        super(context, R.style.customDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rob_order);
        ButterKnife.bind(this);

        if (orderMessage!=null) {
            msg.setText(Html.fromHtml(orderMessage.htmlStr));
            textDeadline.setText(orderMessage.endTime);
            if (orderMessage.msgType.equals("1")){
                //收件
                img.setBackground(getContext().getResources().getDrawable(R.mipmap.orderpush_pic_shoujian_n));
            }else if (orderMessage.msgType.equals("2")){
                //派件
                img.setBackground(getContext().getResources().getDrawable(R.mipmap.orderpush_pic_paijian_n));
            }
        }

    }



    public void setRobType(int type){
        this.type = type;
    }


    public void setMessage(OrderMessage orderMessage){
        this.orderMessage = orderMessage;
    }


    public void setOnCancelClickListener(OnClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }

    public void setOnDoneClickListener(OnClickListener onDoneClickListener) {
        this.onDoneClickListener = onDoneClickListener;
    }

    @OnClick(R.id.btn_cancel)
    public void onCancelClick(){
        if (onCancelClickListener!=null){
            onCancelClickListener.onClick(this,0);
        }else {
            if ("1".equals(orderMessage.msgType)){
                Http.doNotRob(getContext().getApplicationContext(), orderMessage.messageId, new BeanCallback<BaseJson>(getContext()) {
                    @Override
                    public void onResult(boolean success, BaseJson response, Exception e) {
                        super.onResult(success, response, e);
                        if (success){
                            T.showShort(getContext(),response.forUser);
                            if (response.ret){
                                Intent intent = new Intent("ACTION_CHANGE_PAGE");
                                intent.putExtra("page",1);
                                getContext().sendBroadcast(intent);
                                getContext().sendBroadcast(new Intent("ACTION_REFRESH"));
                            }
                        }else {
                            T.showShort(getContext(),e.getMessage());
                        }
//                        dismiss();
                    }
                });

                dismiss();
            }
        }
    }

    @OnClick(R.id.btn_done)
    public void onDoneClick(){
        if (onDoneClickListener!=null){
            onDoneClickListener.onClick(this,0);
        }else {
//            if (orderMessage!=null){
                Http.setRobOrder(getContext(), orderMessage.messageId, new BeanCallback<BaseJson>(getContext()) {
                    @Override
                    public void onResult(boolean success, BaseJson response, Exception e) {
                        super.onResult(success, response, e);
                        if (success){
                            T.showShort(getContext(),response.forUser);
                            if (response.ret){
                                Intent intent = new Intent("ACTION_CHANGE_PAGE");
                                intent.putExtra("page",1);
                                getContext().sendBroadcast(intent);
                                getContext().sendBroadcast(new Intent("ACTION_REFRESH"));
                            }

                        }else {
                            T.showShort(getContext(),e.getMessage());
                        }
                        dismiss();
                    }
                });

            dismiss();
        }
    }
}
