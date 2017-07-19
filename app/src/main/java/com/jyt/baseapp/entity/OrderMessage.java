package com.jyt.baseapp.entity;

/**
 * Created by chenweiqi on 2017/6/13.
 */

public class OrderMessage {
    public String messageId ;//true string 信息id
    public String content ;//true string 内容
    public String createdTime ;//true string 时间
    public String grabState ;//true string 抢单状态，1为未抢，2为已经被抢了
    public boolean overtime ;//true boolean 是否超时（true代表超时，false代表未超时）
    public String endTime ;//true string 抢单结束时间
    public String htmlStr ;//true string html字符串
    public String msgType ;//true string 1代表收件，2代表派件
    public String partnerId;
    public String orderId;
}
