package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Http;
import com.jyt.baseapp.entity.BaseJson;
import com.jyt.baseapp.entity.CountMoneyResult;
import com.jyt.baseapp.entity.OrderTrackResult;
import com.jyt.baseapp.entity.PickUpOrderDetailResult;
import com.jyt.baseapp.entity.ScanIdCardResult;
import com.jyt.baseapp.entity.Track;
import com.jyt.baseapp.util.IntentHelper;
import com.jyt.baseapp.util.T;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/6/5.
 */

public class PickUpOrderDetailActivity extends BaseActivity {
    @BindView(R.id.text_newOrderState)
    TextView textNewOrderState;
    @BindView(R.id.text_newOrderDate)
    TextView textNewOrderDate;
    @BindView(R.id.btn_navigation)
    TextView btnNavigation;
    @BindView(R.id.text_send_name)
    TextView textSendName;
    @BindView(R.id.text_send_phone)
    TextView textSendPhone;
    @BindView(R.id.text_address)
    TextView textAddress;
    @BindView(R.id.label_idCard)
    TextView labelIdCard;
    @BindView(R.id.text_idCard)
    TextView textIdCard;
    @BindView(R.id.img_send)
    ImageView imgSend;
    @BindView(R.id.layout_send)
    RelativeLayout layoutSend;
    @BindView(R.id.text_receive_name)
    TextView textReceiveName;
    @BindView(R.id.text_receive_phone)
    TextView textReceivePhone;
    @BindView(R.id.img_receive)
    ImageView imgReceive;
    @BindView(R.id.layout_receive)
    RelativeLayout layoutReceive;
    @BindView(R.id.label_type)
    TextView labelType;
    @BindView(R.id.text_type)
    TextView textType;
    @BindView(R.id.layout_type)
    LinearLayout layoutType;
    @BindView(R.id.label_weight)
    TextView labelWeight;
    @BindView(R.id.text_weight)
    TextView textWeight;
    @BindView(R.id.label_weight_symbol)
    TextView labelWeightSymbol;
    @BindView(R.id.layout_weight)
    LinearLayout layoutWeight;
    @BindView(R.id.label_volume)
    TextView labelVolume;
    @BindView(R.id.text_volume)
    TextView textVolume;
    @BindView(R.id.label_volume_symbol)
    TextView labelVolumeSymbol;
    @BindView(R.id.layout_volume)
    LinearLayout layoutVolume;
    @BindView(R.id.layout_value)
    LinearLayout layoutValue;
    @BindView(R.id.img_goodsDetail)
    ImageView imgGoodsDetail;
    @BindView(R.id.layout_goods_detail)
    RelativeLayout layoutGoodsDetail;
    @BindView(R.id.label_payType)
    TextView labelPayType;
    @BindView(R.id.text_payType)
    TextView textPayType;
    @BindView(R.id.layout_payType)
    LinearLayout layoutPayType;
    @BindView(R.id.label_company)
    TextView labelCompany;
    @BindView(R.id.text_company)
    TextView textCompany;
    @BindView(R.id.layout_company)
    LinearLayout layoutCompany;
    @BindView(R.id.label_keepValue)
    TextView labelKeepValue;
    @BindView(R.id.text_keepValue)
    TextView textKeepValue;
    @BindView(R.id.layout_keepValue)
    LinearLayout layoutKeepValue;
    @BindView(R.id.img_pay)
    ImageView imgPay;
    @BindView(R.id.layout_pay)
    RelativeLayout layoutPay;
    @BindView(R.id.label_keepValuePrice)
    TextView labelKeepValuePrice;
    @BindView(R.id.text_keepValuePrice)
    TextView textKeepValuePrice;
    @BindView(R.id.layout_keepValuePrice)
    LinearLayout layoutKeepValuePrice;
    @BindView(R.id.label_otherPrice)
    TextView labelOtherPrice;
    @BindView(R.id.text_otherPrice)
    TextView textOtherPrice;
    @BindView(R.id.layout_otherPrice)
    LinearLayout layoutOtherPrice;
    @BindView(R.id.label_expressPrice)
    TextView labelExpressPrice;
    @BindView(R.id.text_expressPrice)
    TextView textExpressPrice;
    @BindView(R.id.layout_expressPrice)
    LinearLayout layoutExpressPrice;
    @BindView(R.id.btn_scanIdCard)
    TextView btnScanIdCard;
    @BindView(R.id.btn_done)
    TextView btnDone;
    @BindView(R.id.layout_btnGroup)
    LinearLayout layoutBtnGroup;
    @BindView(R.id.btn_wait)
    TextView btnWait;
    @BindView(R.id.img_orderDetail)
    ImageView imgOrderDetail;
    @BindView(R.id.layout_orderProgress)
    RelativeLayout layoutOrderProgress;
    @BindView(R.id.text_orderNum)
    TextView textOrderNum;
    @BindView(R.id.text_state)
    TextView textState;
    @BindView(R.id.text_receiveAddress)
    TextView textReceiveAddress;
    @BindView(R.id.text_value)
    TextView textValue;
    @BindView(R.id.text_allPrice)
    TextView textAllPrice;

    public static final int TYPE_PICKUP_READY = 0;
    public static final int TYPE_PICKUP_DOING = 1;
    public static final int TYPE_PICKUP_FINISH = 2;
    public static final int TYPE_PICKUP_CANCEL = 3;
    int type = -1;

    String orderId;

    OrderTrackResult orderTrackResult;
    PickUpOrderDetailResult pickUpOrderDetailResult;
    String idCard;
    Bitmap idCardBitmap;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_pickup_order;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra(IntentHelper.KEY_TYPE, -1);
        orderId = getIntent().getStringExtra(IntentHelper.KEY_ORDER_ID);
        initView();

        labelVolumeSymbol.setText(Html.fromHtml("m<sup>3</sup>"));
        getOrderDetail();
        getTrackOrder();
    }

    private void initView() {
        switch (type) {
            case TYPE_PICKUP_READY:
                btnWait.setVisibility(View.GONE);
                layoutSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = textSendName.getText().toString();
                        String phone = textSendPhone.getText().toString();
                        String address = textAddress.getText().toString();
                        IntentHelper.openEditSendAddressActivity(getContext(),name,phone,address,pickUpOrderDetailResult.latitude,pickUpOrderDetailResult.longitude,pickUpOrderDetailResult.orderId,"2");
                    }
                });
                layoutReceive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = textReceiveName.getText().toString();
                        String phone = textReceivePhone.getText().toString();
                        String address = textReceiveAddress.getText().toString();
                        IntentHelper.openEditReceiveAddressActivity(getContext(),name,phone,address,null,null,pickUpOrderDetailResult.orderId,"1");
                    }
                });
                layoutGoodsDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentHelper.openGoodsDetailActivity(getContext()
                                ,textType.getText().toString()
                        ,pickUpOrderDetailResult.goodsTypeList
                        ,textWeight.getText().toString()
                        ,pickUpOrderDetailResult.weightList
                        ,textVolume.getText().toString()
                        ,pickUpOrderDetailResult.volumeList
                        ,textValue.getText().toString());
                    }
                });
                layoutPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentHelper.openOrderDetailActivityForResult(getContext(),textCompany.getText().toString()
                                ,textPayType.getText().toString()
                                ,textKeepValue.getText().toString()
                                ,textOtherPrice.getText().toString());
                    }
                });
                break;
            case TYPE_PICKUP_DOING:
                btnNavigation.setVisibility(View.INVISIBLE);
                imgReceive.setVisibility(View.GONE);
                imgSend.setVisibility(View.GONE);
                imgGoodsDetail.setVisibility(View.GONE);
                imgPay.setVisibility(View.GONE);
                layoutBtnGroup.setVisibility(View.GONE);

                break;
            case TYPE_PICKUP_FINISH:
            case TYPE_PICKUP_CANCEL:
                btnNavigation.setVisibility(View.INVISIBLE);
                imgReceive.setVisibility(View.GONE);
                imgSend.setVisibility(View.GONE);
                imgGoodsDetail.setVisibility(View.GONE);
                imgPay.setVisibility(View.GONE);
                layoutBtnGroup.setVisibility(View.GONE);
                btnWait.setVisibility(View.GONE);
                break;
        }
    }

    @OnClick(R.id.layout_orderProgress)
    public void onLayoutOrderProgressClick() {
        IntentHelper.openTrackOrderActivity(getContext(),orderTrackResult);
    }

    @OnClick(R.id.btn_scanIdCard)
    public void onScanIdCardClick(){
        IntentHelper.openTakePhotoActivityForResult(getContext());
    }

    private void getOrderDetail() {
        Http.getReceiveOrderDetail(getContext(), orderId, new BeanCallback<BaseJson<PickUpOrderDetailResult>>(getContext() ) {
            @Override
            public void onResponse(BaseJson<PickUpOrderDetailResult> response, int id) {
                super.onResponse(response,id);

                if (response.ret) {
                    pickUpOrderDetailResult = response.data;
                    setViewContent(response.data);
                }
                T.showShort(getContext(), response.forUser);
            }
        });
    }


    private void setViewContent(PickUpOrderDetailResult result) {
        textOrderNum.setText("订单号："+result.orderNo);
        textState.setText(result.receiveStateMsg);

        textSendName.setText(result.sendName);
        textSendPhone.setText(result.sendMobile);
        textAddress.setText(result.sendAddress);
//        textIdCard.setText(result.);


        textReceiveName.setText(result.receiveName);
        textReceivePhone.setText(result.receiveMobile);
        textReceiveAddress.setText(result.receiveAddress);


        textType.setText(result.goodsType);
        textWeight.setText(result.weight);
        textValue.setText(result.value);
        textVolume.setText(result.volume);

        if (!TextUtils.isEmpty(result.payType)){
            if ("1".equals(result.payType)){
                textPayType.setText("线上支付");
            }else if ("2".equals(result.payType)){
                textPayType.setText("线下支付");
            }else if ("3".equals("到付")){
                textPayType.equals(result.payType);
            }
        }
        textCompany.setText(result.expressCompany);
        textKeepValue.setText(result.insured==null?"不保价":result.insured.equals("0")?"不保价":"保价");


        textKeepValuePrice.setText(result.insuredPrice);
        textOtherPrice.setText(result.otherPrice);
        textExpressPrice.setText(result.price);


        textAllPrice.setText(result.totalPrice);


        if (result.receiveState.equals("3")){
            btnWait.setText("确定到达中转中心");
            btnWait.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }


    private void setViewContentOrderTrack(OrderTrackResult result){
        if (result.track!=null&&result.track.size()!=0) {
            Track track = result.track.get(result.track.size()-1);
            textNewOrderState.setText(track.msg);
            textNewOrderDate.setText(track.msgTime);
        }else {
            textNewOrderState.setText("暂无订单信息");
        }

    }

    @OnClick(R.id.btn_wait)
    public void onWaitClick(){
        if (pickUpOrderDetailResult.receiveState.equals("3")){
            Http.finishOrder(getContext(), orderId, new BeanCallback<BaseJson>(getContext()) {
                @Override
                public void onResult(boolean success, BaseJson response, Exception e) {
                    super.onResult(success, response, e);
                    if (success){
                        T.showShort(getContext(),response.forUser);
                        if (response.ret){
                            finish();
                        }
                    }else{
                        T.showShort(getContext(),e.getMessage());
                    }
                }
            });
        }
    }

    //导航
    @OnClick(R.id.btn_navigation)
    public void onNavigationClick(){
        NaviParaOption para = new NaviParaOption()
                .startPoint(new LatLng(App.getLocation().getLatitude(),App.getLocation().getLongitude()))
                .endPoint(new LatLng(Double.valueOf(pickUpOrderDetailResult.latitude),Double.valueOf(pickUpOrderDetailResult.longitude)));
        BaiduMapNavigation.openWebBaiduMapNavi(para, getContext());
    }

    private void getTrackOrder(){
        Http.getTrackOrder(getContext(), orderId, new BeanCallback<BaseJson<OrderTrackResult>>(getContext()) {
            @Override
            public void onResponse(BaseJson<OrderTrackResult> response, int id) {
                super.onResponse(response,id);

                if (response.ret) {
                    orderTrackResult = response.data;
                    setViewContentOrderTrack(orderTrackResult);
                }
                T.showShort(getContext(),response.forUser);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IntentHelper.REQUIRE_CODE_EDIT_SEND_ADDRESS && resultCode == RESULT_OK){
            textAddress.setText(data.getStringExtra(IntentHelper.KEY_ADDRESS));
            textSendName.setText(data.getStringExtra(IntentHelper.KEY_NAME));
            textSendPhone.setText(data.getStringExtra(IntentHelper.KEY_PHONE));
            pickUpOrderDetailResult.longitude = data.getStringExtra(IntentHelper.KEY_LONGITUDE);
            pickUpOrderDetailResult.latitude = data.getStringExtra(IntentHelper.KEY_LATITUDE);
        }else if (requestCode==IntentHelper.REQUIRE_CODE_EDIT_RECEIVE_ADDRESS && resultCode == RESULT_OK){
            textReceiveAddress.setText(data.getStringExtra(IntentHelper.KEY_ADDRESS));
            textReceiveName.setText(data.getStringExtra(IntentHelper.KEY_NAME));
            textReceivePhone.setText(data.getStringExtra(IntentHelper.KEY_PHONE));
        }else if (requestCode == IntentHelper.REQUIRE_CODE_EDIT_ORDER_PAY_INFO && resultCode == RESULT_OK ){
            textPayType.setText(data.getStringExtra(IntentHelper.KEY_PAY_TYPE));
            textKeepValue.setText(data.getStringExtra(IntentHelper.KEY_KEEP_VALUE));
            textOtherPrice.setText(data.getStringExtra(IntentHelper.KEY_COST));
            //快递公司不用修改
            countMoney();
        }else if (requestCode == IntentHelper.REQUIRE_CODE_EDIT_GOODS_INFO && resultCode == RESULT_OK){
            textType.setText(data.getStringExtra(IntentHelper.KEY_TYPE));
            textValue.setText(data.getStringExtra(IntentHelper.KEY_VALUE));
            textWeight.setText(data.getStringExtra(IntentHelper.KEY_WEIGHT));
            textVolume.setText(data.getStringExtra(IntentHelper.KEY_VOLUME));
            countMoney();
        }else if(requestCode == IntentHelper.REQUIRE_CODE_TAKE_PHOTO && resultCode == RESULT_OK){
            idCardBitmap = IntentHelper.getTakePhotoBitMap(getContext(),data);

            Http.ocrIdCard(idCardBitmap, "face", new BeanCallback<ScanIdCardResult>(getContext()) {
                @Override
                public void onResult(boolean success, ScanIdCardResult response, Exception e) {
                    super.onResult(success, response, e);
                    if (success){
                        String json = ((ScanIdCardResult.Output) response.outputs.get(0)).outputValue.dataValue;
                        try {
                            JSONObject jsonObject = new JSONObject(json);
//                            signPerson = jsonObject.getString("name");
                            idCard = jsonObject.getString("num");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textIdCard.setText(idCard);
                                }
                            });

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }else {
                        T.showShort(getContext(),e.getMessage());
                    }
                }
            });
        }
    }

    @OnClick(R.id.btn_done)
    public void onDoneClick(){
        confirm();
    }


    private void confirm(){
//        if (TextUtils.isEmpty(idCard)){
//            T.showShort(getContext(),"请扫描寄件人身份证");
//            return;
//        }
        Http.sendConfirm(getContext(), orderId, idCard,idCardBitmap,new BeanCallback<BaseJson>(getContext()) {
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

    private void countMoney(){
        String pt = "";
        String value = textValue.getText().toString();
        String paytype = textPayType.getText().toString();
        if (paytype.contains("线上支付")){
            pt = "1";
        }else if(paytype.contains("线下支付")){
            pt = "2";
        }else if (paytype.contains("到付")){
            pt = "3";
        }

        Http.countMoney(getContext(), pickUpOrderDetailResult.orderId
                , textKeepValue.getText().toString().equals("保价") ? "1" : "0"
                , pt
                , textVolume.getText().toString()
                , textWeight.getText().toString()
                , textType.getText().toString()
                , pickUpOrderDetailResult.expressId
                ,value
                ,textOtherPrice.getText().toString()
                , new BeanCallback<BaseJson<CountMoneyResult>>(getContext(),false) {
                    @Override
                    public void onResponse(BaseJson<CountMoneyResult> response, int id) {
                        super.onResponse(response,id);

                        T.showShort(getContext(),response.forUser);
                        if (response.ret){
                            textKeepValuePrice.setText( response.data.insuredPrice );
                            textExpressPrice.setText(response.data.price);
                            textAllPrice.setText(response.data.totalPrice);
                            textOtherPrice.setText(response.data.otherPrice);
                        }
                    }
                });
    }
}
