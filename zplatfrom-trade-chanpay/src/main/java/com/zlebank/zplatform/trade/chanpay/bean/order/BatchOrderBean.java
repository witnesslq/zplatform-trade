/* 
 * BatchOrderBean.java  
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
 * @date 2016年4月29日 上午11:00:35
 * @since
 */
public class BatchOrderBean {
	private String service;// 接口名称
	private String version;// 接口版本
	private String partner_id;// 合作者身份ID
	@JSONField(name="_input_charset")
	private String _input_charset;// 参数编码字符集
	private String sign;// 签名
	private String sign_type;// 签名方式
	private String return_url;// 页面跳转同步返回页面路径
	private String memo;// 备注

	// 业务参数
	private String request_no;// 商户网站唯一请求号
	private String trade_amount;// 交易金额
	private String user_poundage;// 用户手续费
	private String mer_poundage;// 商户手续费
	private String prodInfo_list;//
	private String buyer_id;// 买家 ID
	private String buyer_id_type;// 买家 ID 类型
	private String buyer_mobile;// 买家手机号
	private String buyer_ip;// 用户在商户平台下单时候的ip 地址

	// 支付相关参数
	private String pay_method;// 支付方式
	private String pay_type;// 借记贷记, 对公对私
	private String bank_code;// 银行简码
	private String is_anonymous;// 是否匿名支付
	private String payer_truename;// 付款方名称
	private String payer_bankname;// 付款方银行名称（详细到支行）
	private String payer_bankaccountNo;// 付款方银行账号
	private String ext1;// 扩展字段
	private String ext2;// 扩展字段
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
	 * @return the request_no
	 */
	public String getRequest_no() {
		return request_no;
	}
	/**
	 * @param request_no the request_no to set
	 */
	public void setRequest_no(String request_no) {
		this.request_no = request_no;
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
	 * @return the user_poundage
	 */
	public String getUser_poundage() {
		return user_poundage;
	}
	/**
	 * @param user_poundage the user_poundage to set
	 */
	public void setUser_poundage(String user_poundage) {
		this.user_poundage = user_poundage;
	}
	/**
	 * @return the mer_poundage
	 */
	public String getMer_poundage() {
		return mer_poundage;
	}
	/**
	 * @param mer_poundage the mer_poundage to set
	 */
	public void setMer_poundage(String mer_poundage) {
		this.mer_poundage = mer_poundage;
	}
	/**
	 * @return the prodInfo_list
	 */
	public String getProdInfo_list() {
		return prodInfo_list;
	}
	/**
	 * @param prodInfo_list the prodInfo_list to set
	 */
	public void setProdInfo_list(String prodInfo_list) {
		this.prodInfo_list = prodInfo_list;
	}
	/**
	 * @return the buyer_id
	 */
	public String getBuyer_id() {
		return buyer_id;
	}
	/**
	 * @param buyer_id the buyer_id to set
	 */
	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}
	/**
	 * @return the buyer_id_type
	 */
	public String getBuyer_id_type() {
		return buyer_id_type;
	}
	/**
	 * @param buyer_id_type the buyer_id_type to set
	 */
	public void setBuyer_id_type(String buyer_id_type) {
		this.buyer_id_type = buyer_id_type;
	}
	/**
	 * @return the buyer_mobile
	 */
	public String getBuyer_mobile() {
		return buyer_mobile;
	}
	/**
	 * @param buyer_mobile the buyer_mobile to set
	 */
	public void setBuyer_mobile(String buyer_mobile) {
		this.buyer_mobile = buyer_mobile;
	}
	/**
	 * @return the buyer_ip
	 */
	public String getBuyer_ip() {
		return buyer_ip;
	}
	/**
	 * @param buyer_ip the buyer_ip to set
	 */
	public void setBuyer_ip(String buyer_ip) {
		this.buyer_ip = buyer_ip;
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
	 * @return the is_anonymous
	 */
	public String getIs_anonymous() {
		return is_anonymous;
	}
	/**
	 * @param is_anonymous the is_anonymous to set
	 */
	public void setIs_anonymous(String is_anonymous) {
		this.is_anonymous = is_anonymous;
	}
	/**
	 * @return the payer_truename
	 */
	public String getPayer_truename() {
		return payer_truename;
	}
	/**
	 * @param payer_truename the payer_truename to set
	 */
	public void setPayer_truename(String payer_truename) {
		this.payer_truename = payer_truename;
	}
	/**
	 * @return the payer_bankname
	 */
	public String getPayer_bankname() {
		return payer_bankname;
	}
	/**
	 * @param payer_bankname the payer_bankname to set
	 */
	public void setPayer_bankname(String payer_bankname) {
		this.payer_bankname = payer_bankname;
	}
	/**
	 * @return the payer_bankaccountNo
	 */
	public String getPayer_bankaccountNo() {
		return payer_bankaccountNo;
	}
	/**
	 * @param payer_bankaccountNo the payer_bankaccountNo to set
	 */
	public void setPayer_bankaccountNo(String payer_bankaccountNo) {
		this.payer_bankaccountNo = payer_bankaccountNo;
	}
	/**
	 * @return the ext1
	 */
	public String getExt1() {
		return ext1;
	}
	/**
	 * @param ext1 the ext1 to set
	 */
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	/**
	 * @return the ext2
	 */
	public String getExt2() {
		return ext2;
	}
	/**
	 * @param ext2 the ext2 to set
	 */
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	
	

}
