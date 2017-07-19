package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.jyt.baseapp.R;
import com.jyt.baseapp.bean.PayResult;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;
import com.jyt.baseapp.view.widget.LoadingDialog;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/6/23.
 */

public class PayActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_order)
    LinearLayout layoutOrder;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.layout_allPrice)
    LinearLayout layoutAllPrice;
    @BindView(R.id.d3)
    View d3;
    @BindView(R.id.cb_zfb)
    CheckBox cbZfb;
    @BindView(R.id.tv_zfb)
    TextView tvZfb;
    @BindView(R.id.layout_alipay)
    LinearLayout layoutAlipay;
    @BindView(R.id.cb_wx)
    CheckBox cbWx;
    @BindView(R.id.tv_wx)
    TextView tvWx;
    @BindView(R.id.layout_wx)
    LinearLayout layoutWx;
    @BindView(R.id.btn_ok)
    Button btnOk;

    private static final int SDK_PAY_FLAG = 1;

    public static final int AUTO_PAY_ALI_PAY = 1;
    public static final int AUTO_PAY_WXP_AY = 2;

    String APPID = "2017061907519417";
    LoadingDialog loadingDialog ;

    Handler handler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Intent intent = new Intent();

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        T.showShort(getContext(), "支付成功");
                        intent.putExtra(IntentHelper.KEY_PAY_RESULT,true);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        T.showShort(getContext(), "支付失败");
                        intent.putExtra(IntentHelper.KEY_PAY_RESULT,false);
                    }

                    loadingDialog.dismiss();
                    setResult(RESULT_OK,intent);
                    finish();
                    break;
                }

                default:
                    break;
            }
        };
    };
    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    String name;
    String money;
    String orderInfo;
    boolean autoPay;
    int payType = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new LoadingDialog(getContext());
        Intent intent = getIntent();
        if (intent!=null){
            orderInfo = intent.getStringExtra(IntentHelper.KEY_ORDER_INFO);
            autoPay = intent.getBooleanExtra(IntentHelper.KEY_AUTO_PAY,true);
            name = intent.getStringExtra(IntentHelper.KEY_NAME);
            money = intent.getStringExtra(IntentHelper.KEY_MONEY);
            payType =intent.getIntExtra(IntentHelper.KEY_PAY_TYPE,-1);


            tvTitle.setText(name);
            tvMoney.setText(money);

            if (payType==AUTO_PAY_WXP_AY){
                cbWx.setChecked(true);
                cbZfb.setChecked(false);
            }else if (payType==AUTO_PAY_ALI_PAY){
                cbWx.setChecked(false);
                cbZfb.setChecked(true);
            }
        }

        if (autoPay&&payType==-1){
            T.showShort(getContext(),"启动异常");
            finish();
            return;
        }
        if (autoPay){
            loadingDialog.setCancelable(false);
            loadingDialog.show();
            if (payType==1){
                if (TextUtils.isEmpty(orderInfo)){
                    T.showShort(getContext(),"启动异常");
                    finish();
                    return;
                }
                zhiFuBaoPay();
            }
        }
    }


    @OnClick(R.id.btn_ok)
    public void onBtnOkClick(){
        if (cbWx.isChecked()){

        }else if (cbZfb.isChecked()){

        }else {
            T.showShort(getContext(),"请选择支付方式");
        }
    }


    private void zhiFuBaoPay(){

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(getContext());
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    private void wxPay(){

    }

}
