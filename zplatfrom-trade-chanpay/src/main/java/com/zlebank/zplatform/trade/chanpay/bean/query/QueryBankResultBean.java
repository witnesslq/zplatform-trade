/* 
 * QueryBankResultBean.java  
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
 * @date 2016年4月29日 下午3:53:27
 * @since 
 */
public class QueryBankResultBean {

	private String is_success;// 成功标识
	private String partner_id;// 合作者身份ID
	@JSONField(name="_input_charset")
	private String _input_charset;// 参数编码字符集
	private String error_code;// 返回错误码
	private String error_message;// 返回错误原因
	private String memo;// 备注
	
	private String pay_inst_list; //机构列表

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
	 * @return the pay_inst_list
	 */
	public String getPay_inst_list() {
		return pay_inst_list;
	}

	/**
	 * @param pay_inst_list the pay_inst_list to set
	 */
	public void setPay_inst_list(String pay_inst_list) {
		this.pay_inst_list = pay_inst_list;
	}
	
}
