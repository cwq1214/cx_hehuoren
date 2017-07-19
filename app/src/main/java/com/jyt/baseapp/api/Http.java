package com.jyt.baseapp.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import com.baidu.mapapi.map.Text;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.util.Util;
import com.jyt.baseapp.util.Cache;
import com.jyt.baseapp.util.FileUtil;
import com.jyt.baseapp.util.ImageUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by chenweiqi on 2017/6/6.
 */

public class Http {
    public static final String DOMAIN = "http://119.23.66.37";

    public static final String LOGIN = DOMAIN + "/logistics/partner/login/";

    public static final String GET_CODE = DOMAIN + "/logistics/partner/login/get_code";

    public static final String GET_BANNER = DOMAIN + "/logistics/partner/home/marquee";

    public static final String CHANGE_WORK_STATE = DOMAIN + "/logistics/partner/index/work";

    public static final String GET_QR_CODE = DOMAIN + "/logistics/partner/index/getqrcode";

    public static final String LIST_SEND = DOMAIN + "/logistics/partner/send";

    public static final String LIST_PICK_UP = DOMAIN + "/logistics/partner/receive";

    public static final String SEND_ORDER_DETAIL = DOMAIN + "/logistics/partner/send/order_detail";

    public static final String RECEIVE_ORDER_DETAIL = DOMAIN + "/logistics/partner/receive/order_detail";

    public static final String TRACK_ORDER = DOMAIN + "/logistics/partner/receive/get_track";

    public static final String UPDATE_ADDRESS = DOMAIN + "/logistics/partner/receive/update_address";

    public static final String COUNT_MONEY = DOMAIN + "/logistics/partner/receive/count_money";

    public static final String RECEIVE_CONFIRM = DOMAIN + "/logistics/partner/receive/confirm";

    public static final String SEND_SEL_ITEM = DOMAIN + "/logistics/partner/send/get_by_trackingNo";

    public static final String SEND_FINISH = DOMAIN + "/logistics/partner/send/finish";

    public static final String GET_SELF_INFO = DOMAIN + "/logistics/partner/home/";

    public static final String GET_DEPOSIT_LIST = DOMAIN + "/logistics/partner/deposit/";

    public static final String IS_HAVE_DEPOSIT = DOMAIN + "/logistics/partner/deposit/before_buy";

    public static final String GET_BANK_CARD_LIST = DOMAIN + "/logistics/partner/index/bank";

    public static final String BIND_CARD = DOMAIN + "/logistics/partner/index/bind_bank";

    public static final String PAYMENT_DETAIL = DOMAIN + "/logistics/partner/index/cash_detail";

    public static final String MSG_BOX_HOME = DOMAIN + "/logistics/partner/message";

    public static final String MSG_LIST_SYSTEM = DOMAIN + "/logistics/partner/message/get_sys";

    public static final String MSG_LIST_ORDER = DOMAIN + "/logistics/partner/message/get_order";

    public static final String  DELETE_BAND_CARD = DOMAIN + "/logistics/partner/index/delete_bank";

    public static final String POST_BALANCE = DOMAIN + "/logistics/partner/index/withdraw_cash";

    public static final String MODIFY_AVATAR = DOMAIN + "/logistics/partner/index/update_slef";

    public static final String UPLOAD_VERIFICATION = DOMAIN + "/logistics/partner/home/verification_upload";

    public static final String BUY_DEPOSIT = DOMAIN + "/logistics/partner/deposit/buy";

    public static final String ALI_BEFORE_BUY_DEPOSIT = DOMAIN + "/logistics/partner/pay/alipay";

    public static final String QUIT_DEPOSIT = DOMAIN + "/logistics/partner/deposit/quit";

    public static final String SET_LOCATION = DOMAIN + "/logistics/partner/index/set_location";

    public static final String ROB_ORDER = DOMAIN + "/logistics/partner/message/grab_order";

    public static final String FINISH_ORDER = DOMAIN + "/logistics/partner/receive/finish";

//    public static final String SEND_ORDER_DETAIL = DOMAIN + "/logistics/partner/send/order_detail";


    //到达中专中中心
    public static void finishOrder(Context context,String orderId,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(FINISH_ORDER)
                .addParams("orderId",orderId)
                .build().execute(callback);
    }


    //抢单
    public static void setRobOrder(Context context,String messageId,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(ROB_ORDER)
                .addParams("messageId",messageId)
                .build().execute(callback);
    }

    //设置经纬度
    public static void setSetLocation(Context context,String longitude,String latitude,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(SET_LOCATION)
                .addParams("longitude",longitude)
                .addParams("latitude",latitude)
                .build().execute(callback);

    }

    //退出保证金
    public static void quitDeposit(Context context,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(QUIT_DEPOSIT)
                .build().execute(callback);
    }

    //支付宝 购买保证金
    public static void aLiBeforeBuyDeposit(Context context,String type,String depositId,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(ALI_BEFORE_BUY_DEPOSIT)
                .addParams("type",type)
                .addParams("depositId",depositId)
                .build().execute(callback);

    }

    //购买保证金
    public static void buyDeposit(Context context,String depositId,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(BUY_DEPOSIT).addParams("depositId",depositId).build().execute(callback);
    }


    //认证
    public static void verificationUpload(Context context ,Bitmap idCardPositive,Bitmap idCardNegative,Bitmap driverLicense,Bitmap drivingLicense,Bitmap carPhoto,BeanCallback callback){
        PostFormBuilder builder = OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(UPLOAD_VERIFICATION);


        ByteArrayOutputStream byteArrayOutputStream ;


        byteArrayOutputStream= new ByteArrayOutputStream();
        idCardPositive.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        builder.addParams("idCardPositive",Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.NO_WRAP));


        byteArrayOutputStream= new ByteArrayOutputStream();
        idCardNegative.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        builder.addParams("idCardNegative",Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.NO_WRAP));


        byteArrayOutputStream= new ByteArrayOutputStream();
        driverLicense.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        builder.addParams("driverLicense",Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.NO_WRAP));

        byteArrayOutputStream= new ByteArrayOutputStream();
        drivingLicense.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        builder.addParams("drivingLicense",Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.NO_WRAP));


        byteArrayOutputStream= new ByteArrayOutputStream();
        carPhoto.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        builder.addParams("carPhoto",Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.NO_WRAP));


        builder.build().execute(callback);
    }

    //修改头像
    public static void modifyAvatar(Context context,String imagePath,BeanCallback callback){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ImageUtil.compress(imagePath,300);
        bitmap.compress(Bitmap.CompressFormat.PNG,90,stream);

        byte[] bytes = stream.toByteArray();

        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(MODIFY_AVATAR)
                .addParams("key","partnerImg")
                .addParams("value",Base64.encodeToString(bytes,Base64.NO_WRAP))
                .build().execute(callback);
    }

    //登录
    public static void login(Context context, String mobile , String code,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(LOGIN).addParams("mobile",mobile).addParams("code",code)
                .build().execute(callback);
    }

    //获取验证码
    public static void getCode(Context context,String mobile,BeanCallback callback){
        OkHttpUtils.post().url(GET_CODE).addParams("mobile",mobile).build().execute(callback);
    }

    //获取轮播图
    public static void setGetBanner(Context context,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(GET_BANNER).build().execute(callback);
    }

    //上下班
    public static void setChangeWorkState(Context context,boolean isWorking, BeanCallback callback){
        String jobState = isWorking?"1":"0";
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(CHANGE_WORK_STATE).addParams("jobState",jobState).build().execute(callback);
    }

    //获取二维码
    public static void getQrCode(Context context, final BeanCallback callback){
        OkHttpUtils.get().addHeader("tokenSession", Cache.getInstance().getToken()).url(GET_QR_CODE).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),"qrcode.jpg") {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call,e,id);
            }

            @Override
            public void onResponse(File response, int id) {
                callback.onResponse(response,id);
            }
        });
//        Glide.with(context).load(GET_QR_CODE).downloadOnly(new SimpleTarget<File>() {
//            @Override
//            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
//                callback.onResponse(resource,0);
//            }
//        });
    }

    //派件列表
    //1代表派件中，2代表派件失败，3代表已完成
    public static void getSendList(Context context,String type,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(LIST_SEND).addParams("type",type).build().execute(callback);
    }

    //收件列表
    //1代表待收件（已抢单），2代表进行中，3代表已完成，4代表取消
    public static void getPickUpList(Context context,String type ,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(LIST_PICK_UP).addParams("type",type).build().execute(callback);
    }

    //派件 订单详情
    public static void getSendOrderDetail(Context context,String orderId,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(SEND_ORDER_DETAIL).addParams("orderId",orderId).build().execute(callback);
    }

    //收件 订单详情
    public static void getReceiveOrderDetail(Context context,String orderId,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(RECEIVE_ORDER_DETAIL).addParams("orderId",orderId).build().execute(callback);
    }

    //订单追踪
    public static void getTrackOrder(Context context,String orderId,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(TRACK_ORDER).addParams("orderId",orderId).build().execute(callback);
    }

    //修改地址
    public static void setUpdateAddress(Context context,String type,String name ,String mobile,String address,String orderId,String latitude,String longitude,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(UPDATE_ADDRESS)
                .addParams("type",type)
                .addParams("name",name)
                .addParams("mobile",mobile)
                .addParams("address",address)
                .addParams("orderId",orderId)
                .addParams("latitude",latitude)
                .addParams("longitude",longitude)
                .build().execute(callback);
    }

    //计算价钱
    public static void countMoney(Context context, String orderId,String insured,String payType,String volume,String weight,String goodsType,String expressId ,String value,String otherPrice, BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(COUNT_MONEY)
                .addParams("orderId",orderId)
                .addParams("insured",insured)
                .addParams("payType",payType)
                .addParams("volume",volume)
                .addParams("weight",weight)
                .addParams("goodsType",goodsType)
                .addParams("expressId",expressId)
                .addParams("value",value)
                .addParams("otherPrice",otherPrice)
                .build().execute(callback);
    }

    //派件 成功 失败原因
    public static void getSendSelItemList(Context context,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(SEND_SEL_ITEM).build().execute(callback);
    }

    //收件 确认信息
    public static void sendConfirm(Context context, String orderId,String idCard,Bitmap idCardImg,BeanCallback callback){


        String imgStr ="";

        if (idCardImg!=null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            idCardImg.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            imgStr = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        PostFormBuilder builder = OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(RECEIVE_CONFIRM)
                .addParams("orderId",orderId);

        if (!TextUtils.isEmpty(idCard)
                &&!TextUtils.isEmpty(imgStr)){
            builder.addParams("idCard",idCard)
                    .addParams("idCardImg",imgStr);
        }

        builder.build().execute(callback);
    }

    //派件 确定
    public static void finishSendOrder(Context context,String orderId,String status,String failureReason,String signer,String packageNo,String idCard,Bitmap idCardImg , BeanCallback callback){
        PostFormBuilder builder = OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(SEND_FINISH)
                .addParams("orderId",orderId)
                .addParams("status",status)
                .addParams("packageNo",packageNo);
        if (status.equals("1")){
            builder.addParams("signer",signer);

        }else if (status.equals("0")){
            builder.addParams("failureReason",failureReason);

        }

        if (!TextUtils.isEmpty(idCard)&&
                idCardImg!=null){
            String imgStr = "";
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            idCardImg.compress(Bitmap.CompressFormat.JPEG,90,stream);

            imgStr = Base64.encodeToString(stream.toByteArray(),Base64.NO_WRAP);
            builder .addParams("idCard",idCard)
                    .addParams("idCardImg",imgStr);

        }
        builder.build().execute(callback);
    }

    //获取个人信息
    public static void getSelfInfo(Context context,BeanCallback callback){
        OkHttpUtils.post().url(GET_SELF_INFO).addHeader("tokenSession", Cache.getInstance().getToken())
                .build().execute(callback);
    }

    //获取保证金列表
    public static void getDepositList(Context context,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(GET_DEPOSIT_LIST).build().execute(callback);
    }

    //购买保证金前 检查
    public static void checkDeposit(Context context,String depositId,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(IS_HAVE_DEPOSIT)
                .addParams("depositId",depositId)
                .build().execute(callback);
    }

    //获取银行卡列表
    public static void getBankCardList(Context context,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(GET_BANK_CARD_LIST)
                .build().execute(callback);
    }

    //绑定银行卡
    public static void bindCard(Context context,String bankCardId,String userName , String bankName ,String cardNum , BeanCallback callback){
        PostFormBuilder builder = OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(BIND_CARD)
                .addParams("name",userName)
                .addParams("bankNumber",cardNum)
                .addParams("openBank",bankName);
                if (!TextUtils.isEmpty(bankCardId)){
                    builder.addParams("bankId",bankCardId);
                }

        builder.build().execute(callback);
    }

    //消费明细
    public static void paymentsDetail(Context context,String lastId,BeanCallback callback){
        GetBuilder builder = OkHttpUtils.get().addHeader("tokenSession", Cache.getInstance().getToken()).url(PAYMENT_DETAIL);
        if (!TextUtils.isEmpty(lastId)){
            builder.addParams("lastId",lastId);

        }
        builder.build().execute(callback);
    }

    //聊天主界面
    public static void messageBox(Context context, BeanCallback callback){
        OkHttpUtils.get().addHeader("tokenSession", Cache.getInstance().getToken()).url(MSG_BOX_HOME).build().execute(callback);
    }

    //获取系统信息
    public static void getSystemMessageList(Context context,String lastId,BeanCallback callback){
        GetBuilder builder = OkHttpUtils.get().addHeader("tokenSession", Cache.getInstance().getToken()).url(MSG_LIST_SYSTEM);

        if (!TextUtils.isEmpty(lastId))
            builder.addParams("lastId",lastId);

        builder.build().execute(callback);
    }

    //获取订单信息
    public static void getOrderMessageList(Context context,String lastId,BeanCallback callback){
        GetBuilder builder = OkHttpUtils.get().addHeader("tokenSession", Cache.getInstance().getToken()).url(MSG_LIST_ORDER);

        if (!TextUtils.isEmpty(lastId))
            builder.addParams("lastId",lastId);

        builder.build().execute(callback);
    }

    //身份证识别
    public static void ocrIdCard(Bitmap bitmap,String faceOrBack,BeanCallback callback){

        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);//参数100表示不压缩
        byte[] bytes=bos.toByteArray();
        String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        String content = "{\"inputs\": [ {\"image\": {\"dataType\": 50,\"dataValue\": \""+base64+"\"},\"configure\": {\"dataType\": 50,\"dataValue\": \"{\\\"side\\\":\\\""+faceOrBack+"\\\"}\"}}]}";
        String appcode = "7d325476734a44dcae9afeabfc7cee2e";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");
        OkHttpUtils.postString().url("http://dm-51.data.aliyun.com/rest/160601/ocr/ocr_idcard.json")
                .headers(headers)
                .content(content)
                .build().execute(callback);
    }

    //删除银行卡
    public static void deleteBankCard(Context context,String bankCardId,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(DELETE_BAND_CARD)
                .addParams("bankId",bankCardId)
                .build().execute(callback);
    }

    //提现申请
    public static void postBalance(Context context,String bankCardId,String cash,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(POST_BALANCE)
                .addParams("bankId",bankCardId)
                .addParams("cash",cash)
                .build().execute(callback);
    }

    //根据运单号查询订单id
    public static void getOrderIdByOrderNo(Context context , String trackingNo ,BeanCallback callback){
        OkHttpUtils.post().addHeader("tokenSession", Cache.getInstance().getToken()).url(SEND_SEL_ITEM)
                .addParams("trackingNo",trackingNo)
                .build().execute(callback);
    }


}
