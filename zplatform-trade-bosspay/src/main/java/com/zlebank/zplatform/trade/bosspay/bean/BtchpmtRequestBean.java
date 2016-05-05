/* 
 * BtchpmtRequestBean.java  
 * 
 * version TODO
 *
 * 2016年4月21日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bosspay.bean;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年4月21日 下午12:33:26
 * @since 
 */
public class BtchpmtRequestBean {
	@JSONField(name = "ACCOUNT_ID")
	private String accountId;// 付款帐号
	@JSONField(name = "AGREEMENT_ID")
	private String agreementId;// 合同协议号
	@JSONField(name = "REMARK")
	private String remark;// 附言
	@JSONField(name = "USER_ID")
	private String userId;// 用户
	@JSONField(name = "USER_KEY")
	private String userKey;// 用户
	@JSONField(name = "BtchpmtItem")
	private List<BtchpmtItemBean> btchpmtItem;
	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}
	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	/**
	 * @return the agreementId
	 */
	public String getAgreementId() {
		return agreementId;
	}
	/**
	 * @param agreementId the agreementId to set
	 */
	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
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
	 * @return the userKey
	 */
	public String getUserKey() {
		return userKey;
	}
	/**
	 * @param userKey the userKey to set
	 */
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	
	/**
	 * @return the btchpmtItem
	 */
	public List<BtchpmtItemBean> getBtchpmtItem() {
		return btchpmtItem;
	}
	/**
	 * @param btchpmtItem the btchpmtItem to set
	 */
	public void setBtchpmtItem(List<BtchpmtItemBean> btchpmtItem) {
		this.btchpmtItem = btchpmtItem;
	}
	/**
	 * @param accountId
	 * @param agreementId
	 * @param remark
	 * @param userId
	 * @param userKey
	 * @param btchpmtItem
	 */
	public BtchpmtRequestBean(String accountId, String agreementId,
			String remark, String userId, String userKey, List<BtchpmtItemBean> btchpmtItem) {
		super();
		this.accountId = accountId;
		this.agreementId = agreementId;
		this.remark = remark;
		this.userId = userId;
		this.userKey = userKey;
		this.btchpmtItem = btchpmtItem;
	}
	/**
	 * 
	 */
	public BtchpmtRequestBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
