/* 
 * RefundStatusEnum.java  
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
 * @date 2016年5月11日 下午3:38:37
 * @since 
 */
public enum RefundStatusEnum {
	/**退款成功**/
	REFUND_SUCCESS("REFUND_SUCCESS","退款成功"),
    /**退款失败**/
	REFUND_FAIL("REFUND_FAIL","退款失败"),
   
	
    UNKNOW("99","未知状态");

    private String code;
    private String messsage;
    
    private RefundStatusEnum(String code,String message){
        this.code = code;
        this.messsage = message;
    }
    
    public static RefundStatusEnum fromValue(String value) {
        for(RefundStatusEnum status:values()){
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
