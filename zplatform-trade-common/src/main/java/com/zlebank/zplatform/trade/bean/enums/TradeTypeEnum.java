/* 
 * TradeTypeEnum.java  
 * 
 * version TODO
 *
 * 2015年11月17日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bean.enums;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年11月17日 上午10:58:54
 * @since 
 */
public enum TradeTypeEnum {
    SENDMARGINSMS(0),
    MARGINREGISTER(1),
    ONLINEDEPOSITSHORT(2),
    WITHDRAWNOTIFY(3),
    SUBMITPAY(4),
    QUERYTRADE(5),
    BANKSIGN(6),
    ACCOUNTING(7),
    UNKNOW(99);
   private int tradeType;
    
    private TradeTypeEnum(int tradeType){
        this.tradeType = tradeType;
    }
    
    public static TradeTypeEnum fromValue(int value) {
        for(TradeTypeEnum status:values()){
            if(status.tradeType==value){
                return status;
            }
        }
        return UNKNOW;
    }
    
    public int getTradeType(){
        return tradeType;
    }
}
