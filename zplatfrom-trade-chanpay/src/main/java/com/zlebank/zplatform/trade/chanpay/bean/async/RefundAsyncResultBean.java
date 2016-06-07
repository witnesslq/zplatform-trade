/* 
 * RefundAsyncResultBean.java  
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
 * @date 2016年4月29日 下午4:47:36
 * @since
 */
public class RefundAsyncResultBean {

	// 基本参数
	private String notify_id;// 通知 ID
	private String notify_type;// 通知类型
	private String notify_time;// 通知时间
	@JSONField(name="_input_charset")
	private String _input_charset="UTF-8";// 参数字符集编码
	private String sign;// 签名
	private String sign_type;// 签名方式
	private String version;// 版本号

	private String orig_outer_trade_no;// 原交易商户网站唯一订单号
	private String outer_trade_no;// 商户网站退款唯一订单号
	private String inner_trade_no;// 支付平台退款交易订单号
	private String refund_amount;// 退款金额
	private String refund_status;// 退款状态
	private String gmt_refund;// 交易退款时间
	private String extension="{}";
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
	 * @return the orig_outer_trade_no
	 */
	public String getOrig_outer_trade_no() {
		return orig_outer_trade_no;
	}
	/**
	 * @param orig_outer_trade_no the orig_outer_trade_no to set
	 */
	public void setOrig_outer_trade_no(String orig_outer_trade_no) {
		this.orig_outer_trade_no = orig_outer_trade_no;
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
	 * @return the refund_amount
	 */
	public String getRefund_amount() {
		return refund_amount;
	}
	/**
	 * @param refund_amount the refund_amount to set
	 */
	public void setRefund_amount(String refund_amount) {
		this.refund_amount = refund_amount;
	}
	/**
	 * @return the refund_status
	 */
	public String getRefund_status() {
		return refund_status;
	}
	/**
	 * @param refund_status the refund_status to set
	 */
	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}
	/**
	 * @return the gmt_refund
	 */
	public String getGmt_refund() {
		return gmt_refund;
	}
	/**
	 * @param gmt_refund the gmt_refund to set
	 */
	public void setGmt_refund(String gmt_refund) {
		this.gmt_refund = gmt_refund;
	}
	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}
	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	
	

}
