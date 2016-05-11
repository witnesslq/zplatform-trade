/* 
 * TradeStatusEnum.java  
 * 
 * version TODO
 *
 * 2016年5月11日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.chanpay.enums;


/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月11日 上午9:47:10
 * @since 
 */
public enum TradeStatusEnum {
	/**等待买家付款**/
	WAIT_BUYER_PAY("WAIT_BUYER_PAY","等待买家付款"),
    /**买家已付款**/
	PAY_FINISHED("PAY_FINISHED","买家已付款"),
    /**交易成功**/
	TRADE_SUCCESS("TRADE_SUCCESS","交易成功"),
	/**交易结束**/
	TRADE_FINISHED("TRADE_FINISHED","交易结束"),
	/**交易结束**/
	TRADE_CLOSED("TRADE_CLOSED","交易关闭"),
    /**未知状态**/
    UNKNOW("99","未知状态");

    private String code;
    private String messsage;
    
    private TradeStatusEnum(String code,String message){
        this.code = code;
        this.messsage = message;
    }
    
    public static TradeStatusEnum fromValue(String value) {
        for(TradeStatusEnum status:values()){
            if(status.code.equals(value)){
                return status;
            }
        }
        return UNKNOW;
    }
    
    public String getCode(){
        return code;
    }

	/**
	 * @return the messsage
	 */
	public String getMesssage() {
		return messsage;
	}
    
}
