package com.jyt.baseapp.entity;

import java.util.List;

/**
 * Created by chenweiqi on 2017/6/6.
 */

public class PickUpOrderDetailResult {
    public String orderId ;//true string 订单id
    public String orderNo ;//true string 订单号
    public String receiveName ;//true string 收件人姓名
    public String receiveMobile ;//true string 收件人联系方式
    public String receiveAddress ;//true 收件人地址
    public String sendName ;//true string 寄件人姓名
    public String sendMobile ;//true string 寄件人联系方式
    public String sendAddress ;//true string 寄件人地址
    public String goodsType ;//true 物品类型
    public String value ;//true string 声明价值
    public String weight ;//true string 重量
    public String volume ;//true string 体积
    public String payType ;//true 支付类型
    public String expressCompany ;//true 快递公司
    public String insured ;//true string 是否保价（1代表保，0代表不保）
    public String insuredPrice ;//true string 保价费用
    public String receiveStateMsg ;//true string 状态信息（例如：待收件，已完成）
    public String msg ;//true string 配送状态信息（如：您已成功接单，请尽快上门）
    public String msgTime ;//true string 配送状态时间
    public String totalPrice ;//true string 总价
    public String servicePrice ;//true string 服务费（其他费用）
    public String price ;//true string 快递费
    public String longitude ;//true string 经度
    public String latitude ;//true string 纬度
    public List<String> volumeList ;//true array[object] 体积一维数组
    public List<String> weightList ;//true array[object] 重量一维数组
    public List<String> goodsTypeList ;//true array[object] 物品类型一维数组
    public String otherPrice ;//true string 其他费用
    public String expressId;
    public String receiveState ;//true string 1已经给了服务费，待收件，2待支付运费，3正在配送至中转中心，4已完成，5订单被抢，等待服务，6达到中专中心

    public String province;
    public String city;
    public String district;
    public String detail;


    public String rec_province;
    public String rec_city;
    public String rec_district;
    public String rec_detail;
    public String rec_longitude;
    public String rec_latitude;


}
