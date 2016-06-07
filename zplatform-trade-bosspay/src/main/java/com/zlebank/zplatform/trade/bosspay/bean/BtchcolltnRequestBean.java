/* 
 * BtchcolltnRequestBean.java  
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
 * @date 2016年4月21日 下午5:09:51
 * @since 
 */
public class BtchcolltnRequestBean {
	@JSONField(name = "ACCOUNT_ID")
	private String accountId;// 付款帐号
	@JSONField(name = "REMARK")
	private String remark;// 附言
	@JSONField(name = "USER_ID")
	private String userId;// 用户
	@JSONField(name = "USER_KEY")
	private String userKey;// 用户
	@JSONField(name = "BtchColltnItem")
	private List<BtchcolltnItemBean> BtchColltnItem;
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
	 * @return the btchColltnItem
	 */
	public List<BtchcolltnItemBean> getBtchColltnItem() {
		return BtchColltnItem;
	}
	/**
	 * @param btchColltnItem the btchColltnItem to set
	 */
	public void setBtchColltnItem(List<BtchcolltnItemBean> btchColltnItem) {
		BtchColltnItem = btchColltnItem;
	}
	/**
	 * @param accountId
	 * @param remark
	 * @param userId
	 * @param userKey
	 * @param btchColltnItem
	 */
	public BtchcolltnRequestBean(String accountId, String remark,
			String userId, String userKey, List<BtchcolltnItemBean> btchColltnItem) {
		super();
		this.accountId = accountId;
		this.remark = remark;
		this.userId = userId;
		this.userKey = userKey;
		BtchColltnItem = btchColltnItem;
	}
	/**
	 * 
	 */
	public BtchcolltnRequestBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
