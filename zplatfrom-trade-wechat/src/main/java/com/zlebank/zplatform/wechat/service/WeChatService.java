/* 
 * WeChatService.java  
 * 
 * version TODO
 *
 * 2016年5月25日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.wechat.service;

import net.sf.json.JSONObject;

import com.zlebank.zplatform.wechat.wx.bean.PayResultBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月25日 下午1:56:18
 * @since 
 */
public interface WeChatService {

	/**
	 * 创建微信支付订单
	 * @param tn
	 * @return
	 */
	public JSONObject creatOrder(String tn);
	
	/**
	 * 处理微信订单异步通知，更新交易结果
	 * @param result
	 */
	public void asyncTradeResult(PayResultBean result);
}
