/* 
 * SendMsgBean.java  
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
 * @date 2016年4月5日 下午4:38:29
 * @since 
 */
public class SendMsgBean {
	@JSONField(name = "ascT")
	private String ascT;
	@JSONField(name = "signature")
	private String signature;
	@JSONField(name = "encKey")
	private String encKey;
	@JSONField(name = "encData")
	private String encData;
	@JSONField(name = "USERID")
	private String userid;
	/**
	 * @return the ascT
	 */
	public String getAscT() {
		return ascT;
	}
	/**
	 * @param ascT the ascT to set
	 */
	public void setAscT(String ascT) {
		this.ascT = ascT;
	}
	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}
	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}
	/**
	 * @return the encKey
	 */
	public String getEncKey() {
		return encKey;
	}
	/**
	 * @param encKey the encKey to set
	 */
	public void setEncKey(String encKey) {
		this.encKey = encKey;
	}
	/**
	 * @return the encData
	 */
	public String getEncData() {
		return encData;
	}
	/**
	 * @param encData the encData to set
	 */
	public void setEncData(String encData) {
		this.encData = encData;
	}
	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}
	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	
}
