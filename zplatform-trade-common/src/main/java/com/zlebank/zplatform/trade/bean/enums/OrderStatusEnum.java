/* 
 * OrderStatusEnum.java  
 * 
 * version TODO
 *
 * 2016年5月25日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bean.enums;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月25日 下午6:55:07
 * @since 
 */
public enum OrderStatusEnum {
	/*01:初始，订单提交成功，但未支付；02：支付中；03：支付失败；00：支付成功，04：订单失效)*/

	
	INITIAL("01"),
    PAYING("02"),
    FAILED("03"),
    SUCCESS("00"),
    INVALID("04"),
    UNKNOW("");//未知
    private String status;
    
    private OrderStatusEnum(String status){
        this.status = status;
    }
    
    public static OrderStatusEnum fromValue(String value) {
        for(OrderStatusEnum orderStatusEnum:values()){
            if(orderStatusEnum.getStatus().equals(value)){
                return orderStatusEnum;
            }
        }
        return UNKNOW;
    }
    
    public String getStatus(){
        return status;
    }
}
