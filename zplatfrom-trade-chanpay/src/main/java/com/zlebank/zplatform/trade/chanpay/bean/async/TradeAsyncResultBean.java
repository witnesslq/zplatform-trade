/* 
 * TradeAsyncResultBean.java  
 * 
 * version TODO
 *
 * 2016年4月29日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.chanpay.bean.async;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年4月29日 下午4:25:38
 * @since
 */
public class TradeAsyncResultBean {

	// 基本参数
	private String notify_id;// 通知 ID
	private String notify_type;// 通知类型
	private String notify_time;// 通知时间
	@JSONField(name="_input_charset")
	private String _input_charset;// 参数字符集编码
	private String sign;// 签名
	private String sign_type;// 签名方式
	private String version;// 版本号

	// 基本参数
	private String outer_trade_no;// 商户网站唯一订单号
	private String inner_trade_no;// 支付平台交易订单号
	private String trade_status;// 交易状态
	private String trade_amount;// 交易金额
	private String gmt_create;// 交易创建时间
	private String gmt_payment;// 交易支付时间
	private String gmt_close;// 交易关闭时间
	
	/**
	 * @return the notify_id
	 */
	public String getNotify_id() {
		return notify_id;
	}
	/**
	 * @param notify_id the notify_id to set
	 */
	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}
	/**
	 * @return the notify_type
	 */
	public String getNotify_type() {
		return notify_type;
	}
	/**
	 * @param notify_type the notify_type to set
	 */
	public void setNotify_type(String notify_type) {
		this.notify_type = notify_type;
	}
	/**
	 * @return the notify_time
	 */
	public String getNotify_time() {
		return notify_time;
	}
	/**
	 * @param notify_time the notify_time to set
	 */
	public void setNotify_time(String notify_time) {
		this.notify_time = notify_time;
	}
	/**
	 * @return the _input_charset
	 */
	public String get_input_charset() {
		return _input_charset;
	}
	/**
	 * @param _input_charset the _input_charset to set
	 */
	public void set_input_charset(String _input_charset) {
		this._input_charset = _input_charset;
	}
	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
	/**
	 * @return the sign_type
	 */
	public String getSign_type() {
		return sign_type;
	}
	/**
	 * @param sign_type the sign_type to set
	 */
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the outer_trade_no
	 */
	public String getOuter_trade_no() {
		return outer_trade_no;
	}
	/**
	 * @param outer_trade_no the outer_trade_no to set
	 */
	public void setOuter_trade_no(String outer_trade_no) {
		this.outer_trade_no = outer_trade_no;
	}
	/**
	 * @return the inner_trade_no
	 */
	public String getInner_trade_no() {
		return inner_trade_no;
	}
	/**
	 * @param inner_trade_no the inner_trade_no to set
	 */
	public void setInner_trade_no(String inner_trade_no) {
		this.inner_trade_no = inner_trade_no;
	}
	/**
	 * @return the trade_status
	 */
	public String getTrade_status() {
		return trade_status;
	}
	/**
	 * @param trade_status the trade_status to set
	 */
	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
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
	 * @return the gmt_create
	 */
	public String getGmt_create() {
		return gmt_create;
	}
	/**
	 * @param gmt_create the gmt_create to set
	 */
	public void setGmt_create(String gmt_create) {
		this.gmt_create = gmt_create;
	}
	/**
	 * @return the gmt_payment
	 */
	public String getGmt_payment() {
		return gmt_payment;
	}
	/**
	 * @param gmt_payment the gmt_payment to set
	 */
	public void setGmt_payment(String gmt_payment) {
		this.gmt_payment = gmt_payment;
	}
	/**
	 * @return the gmt_close
	 */
	public String getGmt_close() {
		return gmt_close;
	}
	/**
	 * @param gmt_close the gmt_close to set
	 */
	public void setGmt_close(String gmt_close) {
		this.gmt_close = gmt_close;
	}
	
	
}
