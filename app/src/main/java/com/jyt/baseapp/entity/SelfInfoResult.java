package com.jyt.baseapp.entity;

/**
 * Created by chenweiqi on 2017/6/12.
 */

public class SelfInfoResult {
    public String partnerId ;//true string 用户id
    public String cash;// true string 现金
    public String score;// true string 积分
    public String partnerReview;// true string 审核状态：1审核中，2审核通过，3审核不通过
    public String idCard ;//true string 身份证
    public String partnerName ;//true string 姓名
    public String partnerImg;// true string 头像
    public String idCardPositive;// true string 身份证正面照片
    public String idCardNegative;// true string 身份证反面照片
    public String driverLicense;// true string 驾驶证照片
    public String drivingLicense;// true string 行驶证照片
    public String carPhoto;// true string 人车合照
    public Deposit deposit;// true 保证金（null代表没买）
    public String  depositTitle;// true string 等级
    public boolean sign;// true boolean 今日签到（true代表签到，false代表没签到）
}
