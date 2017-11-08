package com.jyt.baseapp.entity;

/**
 * Created by chenweiqi on 2017/6/6.
 */

public class Order {
    public String orderId ;//true string 订单id
    public String orderNo ;//true string 订单号
    public String receiveName;// true string 收件人姓名
    public String receiveMobile;// true string 收件人联系方式
    public String receiveAddress;// true 收件地址
    public String createdTime;// true string 创建时间
    public String trackingNo ;//true string 运单号
    public String expressCompany ;//true string 快递公司名称
    public String receiveStateMsg;
    public int type;
}
