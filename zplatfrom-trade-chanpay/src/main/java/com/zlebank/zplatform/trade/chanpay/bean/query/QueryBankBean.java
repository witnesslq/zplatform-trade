/* 
 * QueryBankBean.java  
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
 * @date 2016年4月29日 下午3:37:28
 * @since 
 */
public class QueryBankBean {
	private String service;// 接口名称
	private String version;// 接口版本
	private String partner_id;// 合作者身份ID
	@JSONField(name = "_input_charset")
	private String _input_charset;// 参数编码字符集
	private String sign;// 签名
	private String sign_type;// 签名方式
	private String return_url;// 页面跳转同步返回页面路径
	private String memo;// 备注
	
	private String product_code;//  产品码 20201

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
	 * @return the product_code
	 */
	public String getProduct_code() {
		return product_code;
	}

	/**
	 * @param product_code the product_code to set
	 */
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	
	
	
}
