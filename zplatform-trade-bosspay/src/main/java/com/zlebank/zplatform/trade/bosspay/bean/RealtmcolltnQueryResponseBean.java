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
public class RealtmcolltnQueryResponseBean {
	@JSONField(name="SERIAL_NUM")
	private String serialNum;// 流水号
	@JSONField(name="ACCOUNT_ID")
	private String accountId;// 收款人帐号
	@JSONField(name="ACCOUNT_NAME")
	private String accountName;// 收款人名称
	@JSONField(name="ACCOUNT_BANK")
	private String accountBank;// 收款人银行账号
	@JSONField(name="ACCOUNT_BANK_NAME")
	private String accountBankName;// 收款人银行名称
	@JSONField(name="BANK_ACCOUNT")
	private String bankAccount;// 付款人帐号
	@JSONField(name="BANK_ACCOUNT_NAME")
	private String bankAccountName;// 付款人名称
	@JSONField(name="BANK_NUMBER")
	private String bankNumber;// 付款人开户行号
	@JSONField(name="BANK_NAME")
	private String bankName;// 付款人开户行名称
	@JSONField(name="AMOUNT")
	private String amount;// 收款金额
	@JSONField(name="STATUS")
	private String status;// 状态
	@JSONField(name="CODE")
	private String code;
	
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
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}
	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	/**
	 * @return the accountBank
	 */
	public String getAccountBank() {
		return accountBank;
	}
	/**
	 * @param accountBank the accountBank to set
	 */
	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}
	/**
	 * @return the accountBankName
	 */
	public String getAccountBankName() {
		return accountBankName;
	}
	/**
	 * @param accountBankName the accountBankName to set
	 */
	public void setAccountBankName(String accountBankName) {
		this.accountBankName = accountBankName;
	}
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
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
	
}
