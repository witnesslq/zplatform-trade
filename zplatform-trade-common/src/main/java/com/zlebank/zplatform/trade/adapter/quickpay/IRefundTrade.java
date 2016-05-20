/* 
 * IRefundTrade.java  
 * 
 * version TODO
 *
 * 2016年5月18日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.adapter.quickpay;

import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月18日 下午1:44:46
 * @since 
 */
public interface IRefundTrade {

	/**
	 * 退款
	 * @param tradeBean
	 * @return
	 */
	public ResultBean refund(TradeBean tradeBean);
}
