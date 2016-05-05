/* 
 * RealtmpmtRequestBean.java  
 * 
 * version TODO
 *
 * 2016年4月5日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bosspay.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年4月5日 下午2:17:01
 * @since
 */
public class BtchcolltnResponseBean {
	
	@JSONField(name = "CODE")
	private String code;// 返回状态码
	@JSONField(name = "MESSAGE")
	private String message;// 返回消息
	@JSONField(name = "SERIAL_NUM")
	private String serialNum;// 流水号
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the serialNum
	 */
	public String getSerialNum() {
		return serialNum;
	}
	/**
	 * @param serialNum the serialNum to set
	 */
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	
	
	
	

}
