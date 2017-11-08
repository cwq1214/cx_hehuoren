package com.jyt.baseapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.jyt.baseapp.adapter.DepositLevelAdapter;
import com.jyt.baseapp.entity.BankCard;
import com.jyt.baseapp.entity.OrderTrackResult;
import com.jyt.baseapp.view.activity.BankCardListActivity;
import com.jyt.baseapp.view.activity.BindCardActivity;
import com.jyt.baseapp.view.activity.DepositActivity;
import com.jyt.baseapp.view.activity.DepositAdjustActivity;
import com.jyt.baseapp.view.activity.DepositLevelActivity;
import com.jyt.baseapp.view.activity.EditAddressActivity;
import com.jyt.baseapp.view.activity.GoodsDetailActivity;
import com.jyt.baseapp.view.activity.GuidePageActivity;
import com.jyt.baseapp.view.activity.IdentificationActivity;
import com.jyt.baseapp.view.activity.LoginActivity;
import com.jyt.baseapp.view.activity.MainActivity;
import com.jyt.baseapp.view.activity.MessageListActivity;
import com.jyt.baseapp.view.activity.OrderDetailActivity;
import com.jyt.baseapp.view.activity.PayActivity;
import com.jyt.baseapp.view.activity.PaymentsDetailActivity;
import com.jyt.baseapp.view.activity.PersonalInfoActivity;
import com.jyt.baseapp.view.activity.PickUpOrderDetailActivity;
import com.jyt.baseapp.view.activity.PostBalanceActivity;
import com.jyt.baseapp.view.activity.SearchLocationActivity;
import com.jyt.baseapp.view.activity.SelLocationActivity;
import com.jyt.baseapp.view.activity.SelLocationActivity2;
import com.jyt.baseapp.view.activity.SendOrderActivity;
import com.jyt.baseapp.view.activity.SendOrderDetailActivity;
import com.jyt.baseapp.view.activity.ShowLocationActivity;
import com.jyt.baseapp.view.activity.TrackOrderActivity;
import com.jyt.baseapp.view.activity.WalletActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenweiqi on 2017/6/1.
 */

public class IntentHelper {
    public static final String KEY_POIINFO = "key_poiinfo";
    public static final String KEY_LONGITUDE = "key_longitude";
    public static final String KEY_LATITUDE = "key_latitude";
    public static final String KEY_TYPE = "key_type";
    public static final String KEY_ORDER_ID = "key_order_id";
    public static final String KEY_NAME = "key_name";
    public static final String KEY_PHONE = "key_phone";
    public static final String KEY_ADDRESS = "key_address";
    public static final String KEY_LATLNG = "key_latlng";
    public static final String KEY_ORDER_TRACK_RESULT = "key_order_track_result";
    public static final String KEY_LIST_TYPE = "key_list_type";
    public static final String KEY_WEIGHT = "key_weight";
    public static final String KEY_LIST_WEIGHT = "key_list_weight";
    public static final String KEY_VOLUME = "key_volume";
    public static final String KEY_LIST_VOLUME = "key_list_volume";
    public static final String KEY_VALUE = "key_value";
    public static final String KEY_COMPANY = "key_company";
    public static final String KEY_PAY_TYPE = "key_pay_type";
    public static final String KEY_KEEP_VALUE = "key_keep_value";
    public static final String KEY_COST = "key_cost";
    public static final String KEY_CASH  = "key_cash";
    public static final String KEY_DEPOSIT_ID = "key_deposit_id";
    public static final String KEY_BAND_CARD = "key_band_card";
    public static final String KEY_ORDER_INFO = "key_order_info";
    public static final String KEY_AUTO_PAY = "key_auto_pay";
    public static final String KEY_MONEY = "key_money";
    public static final String KEY_PAY_RESULT = "key_pay_result";
    public static final String KEY_START_PAGE = "key_start_page";
    public static final String KEY_EXIT = "key_exit";
    public static final String KEY_EDIT_ADDRESS = "key_edit_address";
    public static final String KEY_PROVINCE = "key_province";
    public static final String KEY_CITY = "key_city";
    public static final String KEY_DISTRICT = "key_district";
    public static final String KEY_DETAIL = "key_detail";
    public static final String KEY_PARTNERID = "KEY_PARTNERID";
    public static final String KEY_PREPAYID = "KEY_PREPAYID";
    public static final String KEY_TIMESTAMP = "KEY_TIMESTAMP";
    public static final String KEY_SIGN = "KEY_SIGN";
    public static final String KEY_PACKAGEVALUE = "KEY_PACKAGEVALUE";
    public static final String KEY_NONCESTR = "KEY_NONCESTR";

    public static final int REQUIRE_CODE_SEARCH_LOCATION = 0;
    public static final int REQUIRE_CODE_SEL_LOCATION = 1;
    public static final int REQUIRE_CODE_EDIT_SEND_ADDRESS = 2;
    public static final int REQUIRE_CODE_EDIT_RECEIVE_ADDRESS = 3;
    public static final int REQUIRE_CODE_EDIT_ORDER_PAY_INFO = 4;
    public static final int REQUIRE_CODE_EDIT_GOODS_INFO = 5;
    public static final int REQUIRE_CODE_TAKE_PHOTO = 6;
    public static final int REQUIRE_CODE_SEL_BAND_CARD = 7;
    public static final int REQUIRE_PAY = 8;


    //
    public static void openSystemBrowser(Context context,String url){
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            context.startActivity(intent);
        }catch (Exception e){

        }

    }

    //引导液页
    public static void openGuideActivity(Context context){
        context.startActivity(new Intent(context, GuidePageActivity.class));
    }


    //支付宝 支付 并 返回结果
    public static void openPayActivityForResult_ALI(Context context,String sign,String name,String money,boolean autoPay){
        Intent intent = getIntent(context, PayActivity.class);
        intent.putExtra(IntentHelper.KEY_ORDER_INFO,sign);
        intent.putExtra(IntentHelper.KEY_NAME,name);
        intent.putExtra(IntentHelper.KEY_MONEY,money);
        intent.putExtra(IntentHelper.KEY_AUTO_PAY,autoPay);
        intent.putExtra(IntentHelper.KEY_PAY_TYPE,PayActivity.AUTO_PAY_ALI_PAY);
        if (context instanceof Activity){
            ((Activity) context).startActivityForResult(intent,REQUIRE_PAY);
        }
    }
    //微信 支付 并 返回结果
    public static void openPayActivityForResult_WeiXin(Context context,String partnerId,String prepayId,String timeStamp,String sign,String packageValue,String nonceStr,String name,String money,boolean autoPay){
        Intent intent = getIntent(context, PayActivity.class);
        intent.putExtra(IntentHelper.KEY_PARTNERID,partnerId);
        intent.putExtra(IntentHelper.KEY_PREPAYID,prepayId);
        intent.putExtra(IntentHelper.KEY_TIMESTAMP,timeStamp);
        intent.putExtra(IntentHelper.KEY_SIGN,sign);
        intent.putExtra(IntentHelper.KEY_PACKAGEVALUE,packageValue);
        intent.putExtra(IntentHelper.KEY_NONCESTR,nonceStr);
        intent.putExtra(IntentHelper.KEY_NAME,name);
        intent.putExtra(IntentHelper.KEY_MONEY,money);
        intent.putExtra(IntentHelper.KEY_AUTO_PAY,autoPay);
        intent.putExtra(IntentHelper.KEY_PAY_TYPE,PayActivity.AUTO_PAY_WXP_AY);
        if (context instanceof Activity){
            ((Activity) context).startActivityForResult(intent,REQUIRE_PAY);
        }
    }


    //选择照片
    public static void openPickPhotoActivityForResult(Context context,int requestCode){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.setType("image/*");

        if(context instanceof Activity){
            ((Activity) context).startActivityForResult(intent,requestCode);
        }
    }

    //选择照片后获取文件
    public static Uri afterPickPhoto(Intent data){
        return data.getData();
    }

    //主界面
    public static void openMainActivity(Context context){
        context.startActivity(new Intent(context, MainActivity.class));
    }
    //主界面
    public static void openMainActivity(Context context,int startPage){
        Intent intent = getIntent(context, MainActivity.class);
        intent.putExtra(KEY_START_PAGE,startPage);
        context.startActivity(intent);
    }


    //登录
    public static void openLoginActivity(Context context){
        Intent intent = getIntent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (context instanceof Activity) {
            ((Activity) context).startActivity(intent);
        }

    }
    //关闭app
    public static void exit(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra(KEY_EXIT,true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    // 个人信息
    public static void openPersonalInfoActivity(Context context){
        context.startActivity(new Intent(context, PersonalInfoActivity.class));
    }

    //保证金
    public static void openDepositActivity(Context context){
        context.startActivity(new Intent(context, DepositActivity.class));
    }

    //保证金等级
    public static void openDepositLevelActivity(Context context){
        context.startActivity(new Intent(context, DepositLevelActivity.class));
    }
    //保证金调整
    public static void openDepositAdjustActivity(Context context , String depositId){
        Intent intent = getIntent(context, DepositAdjustActivity.class);
        intent.putExtra(KEY_DEPOSIT_ID,depositId);
        context.startActivity(intent);
    }

    //派单详情
    public static void openSendOrderDetailActivity(Context context,String orderId){
        Intent intent = getIntent(context, SendOrderDetailActivity.class);
        intent.putExtra(KEY_ORDER_ID,orderId);
        context.startActivity(intent);
    }

    //派单业务
    public static void openSendOrderActivity(Context context,String orderId){
        Intent intent = getIntent(context, SendOrderActivity.class);
        intent.putExtra(KEY_ORDER_ID,orderId);
        context.startActivity(intent);
    }

    //查看账户
    public static void openSelBankCardActivity(Context context ){
        Intent intent = getIntent(context, BankCardListActivity.class);
        context.startActivity(intent);
    }
    //选择账户
    public static void openSelBankCardActivityForResult(Context context , BankCard bankCard) {
        Intent intent = getIntent(context, BankCardListActivity.class);
        intent.putExtra(IntentHelper.KEY_BAND_CARD,bankCard);
        if(context instanceof Activity)
        ((Activity) context).startActivityForResult(intent,REQUIRE_CODE_SEL_BAND_CARD);
    }


    //提现
    public static void openPostBalanceActivity(Context context){
        context.startActivity(new Intent(context, PostBalanceActivity.class));
    }

    //钱包
    public static void openWalletActivity(Context context,String cash){
        Intent intent = getIntent(context, WalletActivity.class);
        intent.putExtra(KEY_CASH,cash);
        context.startActivity(intent);
    }

    //绑定账户
    public static void openBindCardActivity(Context context){
        Intent intent = getIntent(context, BindCardActivity.class);
        context.startActivity(intent);
    }
    //修改绑定账户
    public static void openBindCardActivity(Context context, BankCard bankCard){
        Intent intent = getIntent(context, BindCardActivity.class);
        intent .putExtra(IntentHelper.KEY_BAND_CARD,bankCard);
        context.startActivity(intent);
    }

    //明细
    public static void openPaymentsDetailActivity(Context context){
        context.startActivity(new Intent(context, PaymentsDetailActivity.class));
    }

    //收件 待收件
    public static void openPickUpOrderReadyActivity(Context context, String orderId ){
        Intent intent = getIntent(context, PickUpOrderDetailActivity.class);
        intent.putExtra(KEY_TYPE,PickUpOrderDetailActivity.TYPE_PICKUP_READY);
        intent.putExtra(KEY_ORDER_ID,orderId);
        context.startActivity(intent);

    }
    //收件 进行中
    public static void openPickUpOrderDoingActivity(Context context , String orderId ){
        Intent intent = getIntent(context, PickUpOrderDetailActivity.class);
        intent.putExtra(KEY_TYPE,PickUpOrderDetailActivity.TYPE_PICKUP_DOING);
        intent.putExtra(KEY_ORDER_ID,orderId);
        context.startActivity(intent);

    }
    //收件 完成
    public static void openPickUpOrderFinishActivity(Context context , String orderId){
        Intent intent = getIntent(context, PickUpOrderDetailActivity.class);
        intent.putExtra(KEY_TYPE,PickUpOrderDetailActivity.TYPE_PICKUP_FINISH);
        intent.putExtra(KEY_ORDER_ID,orderId);
        context.startActivity(intent);

    }
    //收件 取消
    public static void openPickUpOrderCancelActivity(Context context , String orderId){
        Intent intent = getIntent(context, PickUpOrderDetailActivity.class);
        intent.putExtra(KEY_TYPE,PickUpOrderDetailActivity.TYPE_PICKUP_CANCEL);
        intent.putExtra(KEY_ORDER_ID,orderId);
        context.startActivity(intent);

    }

    //订单跟踪
    public static void openTrackOrderActivity(Context context, OrderTrackResult result){
        Intent intent = getIntent(context, TrackOrderActivity.class);
        intent.putExtra(KEY_ORDER_TRACK_RESULT,result);
        context.startActivity(intent);
    }

    //编辑地址 派件人
    public static void openEditSendAddressActivity(Context context,String name,String phone,String addressText,String lat,String lon,String orderId,String province,String city,String district,String detail,String type){
        Intent intent = getIntent(context, EditAddressActivity.class);
        intent.putExtra(KEY_NAME,name);
        intent.putExtra(KEY_PHONE,phone);
        intent.putExtra(KEY_ADDRESS,addressText);
        intent.putExtra(KEY_LATITUDE,lat);
        intent.putExtra(KEY_LONGITUDE,lon);
        intent.putExtra(KEY_ORDER_ID,orderId);
        intent.putExtra(KEY_TYPE,type);
        intent.putExtra(KEY_PROVINCE,province);
        intent.putExtra(KEY_CITY,city);
        intent.putExtra(KEY_DISTRICT,district);
        intent.putExtra(KEY_DETAIL,detail);
        if (context instanceof Activity)
        ((Activity) context).startActivityForResult(intent,REQUIRE_CODE_EDIT_SEND_ADDRESS);
    }

    //编辑地址 收件人
    public static void openEditReceiveAddressActivity(Context context,String name,String phone,String addressText,String lat,String lon,String orderId,String province,String city,String district,String detail,String type){
        Intent intent = getIntent(context, EditAddressActivity.class);
        intent.putExtra(KEY_NAME,name);
        intent.putExtra(KEY_PHONE,phone);
        intent.putExtra(KEY_ADDRESS,addressText);
        intent.putExtra(KEY_LATITUDE,lat);
        intent.putExtra(KEY_LONGITUDE,lon);
        intent.putExtra(KEY_ORDER_ID,orderId);
        intent.putExtra(KEY_TYPE,type);
        intent.putExtra(KEY_PROVINCE,province);
        intent.putExtra(KEY_CITY,city);
        intent.putExtra(KEY_DISTRICT,district);
        intent.putExtra(KEY_DETAIL,detail);
        if (context instanceof Activity)
            ((Activity) context).startActivityForResult(intent,REQUIRE_CODE_EDIT_RECEIVE_ADDRESS);
    }

    //物品信息
    public static void openGoodsDetailActivity(Context context , String currentType, List type,String currentWeight,List weight,String currentVolume,List volume,String currentValue){
        Intent intent = getIntent(context, GoodsDetailActivity.class);
        intent.putExtra(KEY_TYPE,currentType);
        intent.putCharSequenceArrayListExtra(KEY_LIST_TYPE, (ArrayList<CharSequence>) type);
        intent.putExtra(KEY_WEIGHT,currentWeight);
        intent.putCharSequenceArrayListExtra(KEY_LIST_WEIGHT, (ArrayList<CharSequence>) weight);
        intent.putExtra(KEY_VOLUME,currentVolume);
        intent.putCharSequenceArrayListExtra(KEY_LIST_VOLUME, (ArrayList<CharSequence>) volume);
        intent.putExtra(KEY_VALUE,currentValue);
        if (context instanceof Activity)
            ((Activity) context).startActivityForResult(intent,REQUIRE_CODE_EDIT_GOODS_INFO);
    }

    //快递信息
    public static void openOrderDetailActivityForResult(Context context,String company,String payType,String keepValue,String cost){
        Intent intent = getIntent(context, OrderDetailActivity.class);
        intent.putExtra(KEY_COMPANY,company);
        intent.putExtra(KEY_PAY_TYPE,payType);
        intent.putExtra(KEY_KEEP_VALUE,keepValue);
        intent.putExtra(KEY_COST,cost);
        if (context instanceof Activity)
        ((Activity) context).startActivityForResult(intent,REQUIRE_CODE_EDIT_ORDER_PAY_INFO);
    }
    //选择地址
    public static void openSearchLocationActivityForResult(Context context, LatLng currentLatlng) {
        Intent intent = getIntent(context, SearchLocationActivity.class);
        intent.putExtra(KEY_LATLNG,currentLatlng);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, REQUIRE_CODE_SEARCH_LOCATION);
        }
    }
    //选择地址
    public static void openSelLocationActivity(Context context,String longitude,String latitude){
        Intent intent = new Intent(context, SelLocationActivity.class);
        intent.putExtra(KEY_LONGITUDE,longitude);
        intent.putExtra(KEY_LATITUDE,latitude);
        if (context instanceof Activity){
            ((Activity) context).startActivityForResult(intent,REQUIRE_CODE_SEL_LOCATION);
        }
    }

    //查看定位
    @Deprecated
    public static void openShowLocationActivity(Context context,String longitude,String latitude){
        Intent intent = getIntent(context, ShowLocationActivity.class);
        intent.putExtra(KEY_LATITUDE,latitude);
        intent.putExtra(KEY_LONGITUDE,longitude);
        context.startActivity(intent);
    }

    //打开系统消息列表
    public static void openSystemMessageListActivity(Context context){
        Intent intent = getIntent(context, MessageListActivity.class);
        intent.putExtra(KEY_TYPE,0);
        context.startActivity(intent);
    }

    //打开订单消息列表
    public static void openOrderMessageListActivity(Context context){
        Intent intent = getIntent(context, MessageListActivity.class);
        intent.putExtra(KEY_TYPE,1);
        context.startActivity(intent);
    }

    //拍照
    public static void openTakePhotoActivityForResult(Context context){
        String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
        if (state.equals(Environment.MEDIA_MOUNTED)){   //如果可用
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            if (context instanceof  Activity) {
                ((Activity) context).startActivityForResult(intent, REQUIRE_CODE_TAKE_PHOTO);
            }
        }else {
            Toast.makeText(context,"sdcard不可用",Toast.LENGTH_SHORT).show();
        }
    }
    //拍照
    public static void openTakePhotoActivityForResult(Context context,int requestCode){
        String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
        if (state.equals(Environment.MEDIA_MOUNTED)){   //如果可用
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            if (context instanceof  Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);
            }
        }else {
            Toast.makeText(context,"sdcard不可用",Toast.LENGTH_SHORT).show();
        }
    }

    //拍照返回
    public static Bitmap getTakePhotoBitMap(Context context, Intent data){
        Bitmap photo = null;
        if (data.getData() != null|| data.getExtras() != null){ //防止没有返回结果
            Uri uri =data.getData();
            if (uri != null) {
                photo = BitmapFactory.decodeFile(FileUtil.getPath(context,uri)); //拿到图片
            }
            if (photo == null) {
                Bundle bundle =data.getExtras();
                if (bundle != null){
                    photo =(Bitmap) bundle.get("data");
                } else {
                    Toast.makeText(context, "找不到图片",Toast.LENGTH_SHORT).show();
                }
            }

        }
        return photo;
    }
    //查看地址
    public static void openBrowAddressActivity(){

    }
    //选址地址
    public static void openSelAddressActivity(Context context,String province,String city,String district,String lat,String lon){
        Intent intent = getIntent(context, SelLocationActivity2.class);
        intent.putExtra(IntentHelper.KEY_LATITUDE,lat);
        intent.putExtra(IntentHelper.KEY_LONGITUDE,lon);
        intent.putExtra(IntentHelper.KEY_PROVINCE,province);
        intent.putExtra(IntentHelper.KEY_CITY,city);
        intent.putExtra(IntentHelper.KEY_DISTRICT,district);
        intent.putExtra(IntentHelper.KEY_EDIT_ADDRESS,true);
        ((Activity) context).startActivityForResult(intent,IntentHelper.REQUIRE_CODE_SEL_LOCATION);
    }

    //身份认证
    public static void openIdentifiedActivity(Context context){
        context.startActivity(getIntent(context, IdentificationActivity.class));
    }

    private static Intent getIntent(Context context,Class activity){
        return new Intent(context,activity);
    }



}

