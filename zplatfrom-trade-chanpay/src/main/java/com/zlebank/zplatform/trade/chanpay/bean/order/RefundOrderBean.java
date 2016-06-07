/* 
 * RefundOrderBean.java  
 * 
 * version TODO
 *
 * 2016年4月29日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.chanpay.bean.order;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年4月29日 下午1:07:17
 * @since 
 */
public class RefundOrderBean {

	private String service;// 接口名称
	private String version;// 接口版本
	private String partner_id;// 合作者身份ID
	@JSONField(name="_input_charset")
	private String _input_charset;// 参数编码字符集
	private String sign;// 签名
	private String sign_type;// 签名方式
	private String return_url;// 页面跳转同步返回页面路径
	private String memo;// 备注
	
	//业务参数
	private String outer_trade_no;//  商户网站唯一订单号
	private String orig_outer_trade_no;//  原始的商户网站唯一订单号
	private String refund_amount;//  退款金额
	private String royalty_parameters;//  交易金额分润账号集
	private String notify_url;//  异步回调地址
	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
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
	 * @return the partner_id
	 */
	public String getPartner_id() {
		return partner_id;
	}
	/**
	 * @param partner_id the partner_id to set
	 */
	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
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
	 * @return the return_url
	 */
	public String getReturn_url() {
		return return_url;
	}
	/**
	 * @param return_url the return_url to set
	 */
	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
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
	 * @return the royalty_parameters
	 */
	public String getRoyalty_parameters() {
		return royalty_parameters;
	}
	/**
	 * @param royalty_parameters the royalty_parameters to set
	 */
	public void setRoyalty_parameters(String royalty_parameters) {
		this.royalty_parameters = royalty_parameters;
	}
	/**
	 * @return the notify_url
	 */
	public String getNotify_url() {
		return notify_url;
	}
	/**
	 * @param notify_url the notify_url to set
	 */
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	
	
}
