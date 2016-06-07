/* 
 * QueryTradeResultBean.java  
 * 
 * version TODO
 *
 * 2016年4月29日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.chanpay.bean.query;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年4月29日 下午3:31:03
 * @since 
 */
public class QueryTradeResultBean {
	
	private String is_success;// 成功标识
	private String partner_id;// 合作者身份ID
	@JSONField(name="_input_charset")
	private String _input_charset;// 参数编码字符集
	private String error_code;// 返回错误码
	private String error_message;// 返回错误原因
	private String memo;// 备注
	
	
	private String outer_trade_no;  //商 户 网 站 唯一订单号（外部订单号）
	private String inner_trade_no ;  //支付 平台交易 订单号
	private String trade_amount;//  交易金额
	private String trade_status;//  交易状态	
	private String trade_date ; //交 易 状 态 对应的时间
	/**
	 * @return the is_success
	 */
	public String getIs_success() {
		return is_success;
	}
	/**
	 * @param is_success the is_success to set
	 */
	public void setIs_success(String is_success) {
		this.is_success = is_success;
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
	 * @return the error_code
	 */
	public String getError_code() {
		return error_code;
	}
	/**
	 * @param error_code the error_code to set
	 */
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	/**
	 * @return the error_message
	 */
	public String getError_message() {
		return error_message;
	}
	/**
	 * @param error_message the error_message to set
	 */
	public void setError_message(String error_message) {
		this.error_message = error_message;
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
	 * @return the trade_date
	 */
	public String getTrade_date() {
		return trade_date;
	}
	/**
	 * @param trade_date the trade_date to set
	 */
	public void setTrade_date(String trade_date) {
		this.trade_date = trade_date;
	}
	
	
}
