/* 
 * BtchpmtItemBean.java  
 * 
 * version TODO
 *
 * 2016年4月21日 
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
 * @date 2016年4月21日 下午12:37:55
 * @since 
 */
public class BtchcolltnItemBean {
	
	@JSONField(name = "AGREEMENT_ID")
	private String agreementId;
	@JSONField(name = "BANK_ACCOUNT")
	private String bankAccount;// 收款人帐号
	@JSONField(name = "BANK_ACCOUNT_NAME")
	private String bankAccountName;// 收款人名称
	@JSONField(name = "BANK_NUMBER")
	private String bankNumber;// 收款人开户行号
	@JSONField(name = "BANK_NAME")
	private String bankName;// 收款人开户行名称
	@JSONField(name = "BANK_PROVINCE")
	private String bankProvince;// 省
	@JSONField(name = "BANK_CITY")
	private String bankCity;// 市
	@JSONField(name = "AMOUNT")
	private String amount;// 付款金额
	@JSONField(name = "USAGE_TYPE")
	private String usageType;// E 用途
	/**
	 * @return the bankAccount
	 */
	public String getBankAccount() {
		return bankAccount;
	}
	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	/**
	 * @return the bankAccountName
	 */
	public String getBankAccountName() {
		return bankAccountName;
	}
	/**
	 * @param bankAccountName the bankAccountName to set
	 */
	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}
	/**
	 * @return the bankNumber
	 */
	public String getBankNumber() {
		return bankNumber;
	}
	/**
	 * @param bankNumber the bankNumber to set
	 */
	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}
	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}
	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 * @return the bankProvince
	 */
	public String getBankProvince() {
		return bankProvince;
	}
	/**
	 * @param bankProvince the bankProvince to set
	 */
	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}
	/**
	 * @return the bankCity
	 */
	public String getBankCity() {
		return bankCity;
	}
	/**
	 * @param bankCity the bankCity to set
	 */
	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}
	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}
	/**
	 * @return the usageType
	 */
	public String getUsageType() {
		return usageType;
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
	 * @param usageType the usageType to set
	 */
	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}
	
	/**
	 * @param agreementId
	 * @param bankAccount
	 * @param bankAccountName
	 * @param bankNumber
	 * @param bankName
	 * @param bankProvince
	 * @param bankCity
	 * @param amount
	 * @param usageType
	 */
	public BtchcolltnItemBean(String agreementId, String bankAccount,
			String bankAccountName, String bankNumber, String bankName,
			String bankProvince, String bankCity, String amount,
			String usageType) {
		super();
		this.agreementId = agreementId;
		this.bankAccount = bankAccount;
		this.bankAccountName = bankAccountName;
		this.bankNumber = bankNumber;
		this.bankName = bankName;
		this.bankProvince = bankProvince;
		this.bankCity = bankCity;
		this.amount = amount;
		this.usageType = usageType;
	}
	/**
	 * 
	 */
	public BtchcolltnItemBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
