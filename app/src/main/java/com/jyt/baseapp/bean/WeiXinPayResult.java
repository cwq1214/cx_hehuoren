package com.jyt.baseapp.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenweiqi on 2017/8/29.
 */

public class WeiXinPayResult {
    public long timeStamp;//支付时间

    public String prepayId;//微信返回id

    public String partnerId;//微信的商户id

    public String paySign;//签名
    @SerializedName("package")
    public String packageValue;//包

    public String nonceStr;//随机字符串

    public String appid;

    public float cash;


}
