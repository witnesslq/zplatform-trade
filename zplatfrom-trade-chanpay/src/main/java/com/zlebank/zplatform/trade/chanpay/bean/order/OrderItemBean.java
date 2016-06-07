/* 
 * OrderItemBean.java  
 * 
 * version TODO
 *
 * 2016年4月29日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.chanpay.bean.order;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年4月29日 上午11:09:26
 * @since
 */
public class OrderItemBean {
	private String out_trade_no;// 商户网站唯一订单号
	private String order_amount;// 订单金额
	private String sell_id;// 卖家标示ID
	private String sell_id_type;// 卖家标示ID类型
	private String sell_mobile;// 卖家手机号
	private String produc_url;// 商品展示URL
	private String product_name;// 商品名称
	private String action_desc;// 交易描述
	private String notify_url;// 服务器异步通知页面路径
	private String order_time;// 商户订单提交时间
	private String expired_time;// 支付过期时间
	private String royalty_parameters;// 交易金额分润账号集
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
	 * @return the order_amount
	 */
	public String getOrder_amount() {
		return order_amount;
	}
	/**
	 * @param order_amount the order_amount to set
	 */
	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}
	/**
	 * @return the sell_id
	 */
	public String getSell_id() {
		return sell_id;
	}
	/**
	 * @param sell_id the sell_id to set
	 */
	public void setSell_id(String sell_id) {
		this.sell_id = sell_id;
	}
	/**
	 * @return the sell_id_type
	 */
	public String getSell_id_type() {
		return sell_id_type;
	}
	/**
	 * @param sell_id_type the sell_id_type to set
	 */
	public void setSell_id_type(String sell_id_type) {
		this.sell_id_type = sell_id_type;
	}
	/**
	 * @return the sell_mobile
	 */
	public String getSell_mobile() {
		return sell_mobile;
	}
	/**
	 * @param sell_mobile the sell_mobile to set
	 */
	public void setSell_mobile(String sell_mobile) {
		this.sell_mobile = sell_mobile;
	}
	/**
	 * @return the produc_url
	 */
	public String getProduc_url() {
		return produc_url;
	}
	/**
	 * @param produc_url the produc_url to set
	 */
	public void setProduc_url(String produc_url) {
		this.produc_url = produc_url;
	}
	/**
	 * @return the product_name
	 */
	public String getProduct_name() {
		return product_name;
	}
	/**
	 * @param product_name the product_name to set
	 */
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	/**
	 * @return the action_desc
	 */
	public String getAction_desc() {
		return action_desc;
	}
	/**
	 * @param action_desc the action_desc to set
	 */
	public void setAction_desc(String action_desc) {
		this.action_desc = action_desc;
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
	/**
	 * @return the order_time
	 */
	public String getOrder_time() {
		return order_time;
	}
	/**
	 * @param order_time the order_time to set
	 */
	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}
	/**
	 * @return the expired_time
	 */
	public String getExpired_time() {
		return expired_time;
	}
	/**
	 * @param expired_time the expired_time to set
	 */
	public void setExpired_time(String expired_time) {
		this.expired_time = expired_time;
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
	
	
}
