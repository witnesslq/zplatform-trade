/* 
 * StatusEnum.java  
 * 
 * version TODO
 *
 * 2016年4月21日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bosspay.enums;


/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年4月21日 上午11:04:51
 * @since 
 */
public enum StatusEnum {
	
	PR10("PR10","已确认"),
	PR02("PR02","已付款"),
	PR09("PR09","业务拒绝"),
	RJ01("RJ01","账号不存在"),
	RJ02("RJ02","账号、户名不符"),
	RJ03("RJ03","账户余额不足支付"),
	RJ04("RJ04","当日业务累计金额超过规定金额"),
	RJ05("RJ05","业务检查错"),
	RJ06("RJ06","指定协议不存在"),
	RJ07("RJ07","超过协议授权范围"),
	RJ08("RJ08","账户类型非法"),
	RJ09("RJ08","退票"),
	RJ90("RJ08","其他（银行自定义原因，业务拒绝）"),
    UNKNOW("99","未知原因");

	private String code;
	private String message;
    
    private StatusEnum(String code,String message){
        this.code = code;
        this.message = message;
    }
    
    public static StatusEnum fromValue(String value) {
        for(StatusEnum status:values()){
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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	
    
}
