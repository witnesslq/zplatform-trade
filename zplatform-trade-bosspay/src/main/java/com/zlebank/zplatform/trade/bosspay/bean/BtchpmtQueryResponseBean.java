/* 
 * RealtmcolltnQueryRequestBean.java  
 * 
 * version TODO
 *
 * 2016年4月8日 
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
 * @date 2016年4月8日 上午8:39:17
 * @since
 */
public class BtchpmtQueryResponseBean {
	
	@JSONField(name="REMOTE_SERIAL_NUM")
	private String remoteSerialNum;
	@JSONField(name="result")
	private String result;
	@JSONField(name="MESSAGE")
	private String message;
	/**
	 * @return the remoteSerialNum
	 */
	public String getRemoteSerialNum() {
		return remoteSerialNum;
	}
	/**
	 * @param remoteSerialNum the remoteSerialNum to set
	 */
	public void setRemoteSerialNum(String remoteSerialNum) {
		this.remoteSerialNum = remoteSerialNum;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
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
	
	
	
	
}
