package com.jyt.baseapp.entity;

import java.util.List;

/**
 * Created by chenweiqi on 2017/6/12.
 */

public class DepositListResult {
   public String partnerDepositId;// true 当前用户保证金id
   public List<Deposit> deposit;// true array[object] 保证金列表
}
