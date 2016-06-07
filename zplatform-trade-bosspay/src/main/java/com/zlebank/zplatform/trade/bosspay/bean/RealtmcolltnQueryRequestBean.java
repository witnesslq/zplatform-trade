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
public class RealtmcolltnQueryRequestBean {
	@JSONField(name="USER_ID")
	private String userId;// 用户账号
	@JSONField(name="SERIAL_NUM")
	private String serialNum;// 流水号
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
