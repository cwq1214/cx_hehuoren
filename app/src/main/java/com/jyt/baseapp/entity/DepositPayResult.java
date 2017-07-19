package com.jyt.baseapp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenweiqi on 2017/6/23.
 */

public class DepositPayResult {

//    public String cashId ;//true string 流水id
    public String partnerId ;//true string 合伙人id
    public double cash ;//true number 支付金额
    public double userCash ;//true number 用户需要扣的余额
    public double timeStamp ;//true number 时间戳

    //支付宝
    public String sign ;//true string 签名
    //微信
    public String prepayId ;//true string 微信返回的支付id
    public String paySign ;//true string 签名

    @SerializedName("package")
    public String packageName ;//true string 包
    public String nonceStr ;//true string 随机数
}
