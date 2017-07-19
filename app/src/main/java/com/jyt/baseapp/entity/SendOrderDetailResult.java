package com.jyt.baseapp.entity;

import java.util.List;

/**
 * Created by chenweiqi on 2017/6/9.
 */

public class SendOrderDetailResult {
    public String orderId;// true string 订单id
    public String orderNo;// true string 订单号
    public String trackingNo;// true string 运单号
    public String packageNo;// true string 包号码
    public String expressCompany;// true string 快递公司名称
    public String sendState;// true string 1未签收,待派件；2，订单被抢，已签收，派件中；3成功；4失败，5订单待启动，还未到中转中心
    public String signer;// true string 签收人
    public String failureReason;// true string 失败原因
    public String sendStateMsg;// true string 派件状态信息（例如：已签收）
    public List<Track> msgList;
    public String mobile ;//true string 合作人电话
    public String partnerName ;//true string 合作人
}
