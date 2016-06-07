/* 
 * BossPayService.java  
 * 
 * version TODO
 *
 * 2016年4月6日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bosspay.service;

import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bosspay.bean.BtchcolltnRequestBean;
import com.zlebank.zplatform.trade.bosspay.bean.BtchpmtRequestBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年4月6日 上午11:18:19
 * @since 
 */
public interface BossPayService {
	/**
	 * 实时代收
	 * @param trade
	 * @return
	 */
	public ResultBean realCollecting(TradeBean trade);
	
	/**
	 * 查询实时代收交易
	 * @param serialNum 流水号 (博士金电)
	 * @return
	 */
	public ResultBean queryRealCollecting(String serialNum);
	
	/**
	 * 实时代付
	 * @param trade
	 * @return
	 */
	public ResultBean realInsteadPay(TradeBean trade);
	
	/**
	 * 查询代付结果
	 * @param serialNum
	 * @return
	 */
	public ResultBean queryRealInsteadPay(String serialNum);
	
	/**
	 * 批量代付
	 * @param requestBean
	 * @return
	 */
	public ResultBean batchInsteadPay(BtchpmtRequestBean requestBean);
	
	/**
	 * 查询批量代付结果
	 * @param serialNum
	 * @return
	 */
	public ResultBean queryBatchInsteadPay(String serialNum);
	
	/**
	 * 批量代扣
	 * @param requestBean
	 * @return
	 */
	public ResultBean batchCollecting(BtchcolltnRequestBean requestBean);
	
	/**
	 * 查询批量代收
	 * @param serialNum
	 * @return
	 */
	public ResultBean queryBatchCollection(String serialNum);
}
