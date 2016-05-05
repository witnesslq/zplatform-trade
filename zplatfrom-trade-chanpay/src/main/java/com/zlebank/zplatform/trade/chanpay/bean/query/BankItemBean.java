/* 
 * BankItemBean.java  
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
 * @date 2016年4月29日 下午3:59:11
 * @since
 */
public class BankItemBean {
	@JSONField(name="instId")
	private String inst_id;// 机构 ID
	@JSONField(name="payMode")
	private String pay_mode;// 支付模式
	@JSONField(name="instCode")
	private String inst_code;// 机构代码
	@JSONField(name="instName")
	private String inst_name;// 机构名称
	@JSONField(name="cardType")
	private String card_type;// 卡类型
	@JSONField(name="cardAttribute")
	private String card_attribute;// 卡属性
	private String ext;// 扩展信息
	/**
	 * @return the inst_id
	 */
	public String getInst_id() {
		return inst_id;
	}
	/**
	 * @param inst_id the inst_id to set
	 */
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	/**
	 * @return the pay_mode
	 */
	public String getPay_mode() {
		return pay_mode;
	}
	/**
	 * @param pay_mode the pay_mode to set
	 */
	public void setPay_mode(String pay_mode) {
		this.pay_mode = pay_mode;
	}
	/**
	 * @return the inst_code
	 */
	public String getInst_code() {
		return inst_code;
	}
	/**
	 * @param inst_code the inst_code to set
	 */
	public void setInst_code(String inst_code) {
		this.inst_code = inst_code;
	}
	/**
	 * @return the inst_name
	 */
	public String getInst_name() {
		return inst_name;
	}
	/**
	 * @param inst_name the inst_name to set
	 */
	public void setInst_name(String inst_name) {
		this.inst_name = inst_name;
	}
	/**
	 * @return the card_type
	 */
	public String getCard_type() {
		return card_type;
	}
	/**
	 * @param card_type the card_type to set
	 */
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	/**
	 * @return the card_attribute
	 */
	public String getCard_attribute() {
		return card_attribute;
	}
	/**
	 * @param card_attribute the card_attribute to set
	 */
	public void setCard_attribute(String card_attribute) {
		this.card_attribute = card_attribute;
	}
	/**
	 * @return the ext
	 */
	public String getExt() {
		return ext;
	}
	/**
	 * @param ext the ext to set
	 */
	public void setExt(String ext) {
		this.ext = ext;
	}

	
}
