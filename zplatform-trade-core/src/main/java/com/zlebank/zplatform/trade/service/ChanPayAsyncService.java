/* 
 * ChanPayAsyncService.java  
 * 
 * version TODO
 *
 * 2016年5月10日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.chanpay.bean.async.RefundAsyncResultBean;
import com.zlebank.zplatform.trade.chanpay.bean.async.TradeAsyncResultBean;
import com.zlebank.zplatform.trade.exception.TradeException;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月10日 下午4:46:13
 * @since 
 */
public interface ChanPayAsyncService {

	/**
	 * 处理交易异步通知
	 * @param tradeAsyncResultBean
	 * @return
	 * @throws TradeException
	 */
	public ResultBean dealWithTradeAsync(TradeAsyncResultBean tradeAsyncResultBean) throws TradeException;
	
	/**
	 * 处理退款异步通知
	 * @param refundAsyncResultBean
	 * @return
	 * @throws TradeException
	 */
	public ResultBean dealWithRefundAsync(RefundAsyncResultBean refundAsyncResultBean) throws TradeException;
}
