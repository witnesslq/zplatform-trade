/* 
 * BusinessEnum.java  
 * 
 * version TODO
 *
 * 2015年11月27日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bean.enums;

import org.apache.commons.net.chargen.CharGenTCPClient;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年11月27日 上午11:45:57
 * @since 
 */
public enum BusinessEnum {
    CONSUMEQUICK("10000001"),//消费-快捷
    CONSUMEACCOUNT("10000002"),//消费-账户
    CONSUMESPLIT("10000004"),//消费-分账
    RECHARGE("20000001"),//充值
    WITHDRAWALS("30000001"),//提现
    REFUND_BANK("40000001"),//退款-银行卡
    INSTEADPAY("70000001"),//代付
    INSTEADPAY_REFUND("70000001"),//代付退汇
    WITHDRAWALS_REFUND("30000003"),//提现退汇
    REFUND_REFUND("40000003"),//退款退汇
    
    INSTEADPAY_SUCCESS("70000001"),//代付退汇
    WITHDRAWALS_SUCCESS("30000001"),//提现退汇
    REFUND_ACCOUNT("40000002"),//退款-账户
    CHARGE_OFFLINE("90000001"),//手工充值
    UNKNOW("");//未知
    private String busiCode;
    
    private BusinessEnum(String busiCode){
        this.busiCode = busiCode;
    }
    
    public static BusinessEnum fromValue(String value) {
        for(BusinessEnum busi:values()){
            if(busi.busiCode==value){
                return busi;
            }
        }
        return UNKNOW;
    }
    
    public String getBusiCode(){
        return busiCode;
    }
}
