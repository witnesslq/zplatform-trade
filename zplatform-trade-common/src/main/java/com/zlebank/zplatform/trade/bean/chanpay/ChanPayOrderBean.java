/* 
 * OrderBean.java  
 * 
 * version TODO
 *
 * 2016年5月6日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bean.chanpay;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月6日 下午1:48:18
 * @since 
 */
public class ChanPayOrderBean {
	private String out_trade_no;// 商户网站唯一订单号
	private String trade_amount;// 交易金额
	private String pay_method;// 支付方式
	private String pay_type;// 借记贷记,对公对私
	private String bank_code;// 银行简码
	private String txnseqno;//交易序列号
	/**
	 * @return the out_trade_no
	 */
	public String getOut_trade_no() {
		return out_trade_no;
	}
	/**
	 * @param out_trade_no the out_trade_no to set
	 */
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	/**
	 * @return the trade_amount
	 */
	public String getTrade_amount() {
		return trade_amount;
	}
	/**
	 * @param trade_amount the trade_amount to set
	 */
	public void setTrade_amount(String trade_amount) {
		this.trade_amount = trade_amount;
	}
	/**
	 * @return the pay_method
	 */
	public String getPay_method() {
		return pay_method;
	}
	/**
	 * @param pay_method the pay_method to set
	 */
	public void setPay_method(String pay_method) {
		this.pay_method = pay_method;
	}
	/**
	 * @return the pay_type
	 */
	public String getPay_type() {
		return pay_type;
	}
	/**
	 * @param pay_type the pay_type to set
	 */
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	/**
	 * @return the bank_code
	 */
	public String getBank_code() {
		return bank_code;
	}
	/**
	 * @param bank_code the bank_code to set
	 */
	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}
	/**
	 * @return the txnseqno
	 */
	public String getTxnseqno() {
		return txnseqno;
	}
	/**
	 * @param txnseqno the txnseqno to set
	 */
	public void setTxnseqno(String txnseqno) {
		this.txnseqno = txnseqno;
	}
	
	
}
