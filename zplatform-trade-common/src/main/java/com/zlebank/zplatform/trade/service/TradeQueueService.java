/* 
 * TradeQueueService.java  
 * 
 * version TODO
 *
 * 2016年7月19日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import com.zlebank.zplatform.trade.bean.TradeQueueBean;
import com.zlebank.zplatform.trade.bean.enums.TradeQueueEnum;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年7月19日 下午3:37:07
 * @since 
 */
public interface TradeQueueService {

	/**
	 * 添加交易进入交易队列
	 * @param tradeQueueBean
	 */
	public void addTradeQueue(TradeQueueBean tradeQueueBean);
	
	/**
	 * 添加超时交易进入超时交易队列
	 * @param tradeQueueBean
	 */
	public void addTimeOutQueue(TradeQueueBean tradeQueueBean);
	
	/**
	 * 取得交易队列中最左侧的交易
	 * @return
	 */
	public TradeQueueBean tradeQueuePop();
	
	/**
	 * 取得超时交易队列最左侧的交易
	 * @return
	 */
	public TradeQueueBean timeOutQueuePop();
	
	/**
	 * 根据交易队列枚举取得队列最左侧的交易
	 * @param tradeQueueEnum
	 * @return
	 */
	public TradeQueueBean queuePop(TradeQueueEnum tradeQueueEnum);
	
	/**
	 * 取得队列的大小
	 * @param queueEnum 
	 * @return
	 */
	public long getQueueSize(TradeQueueEnum queueEnum);
}
