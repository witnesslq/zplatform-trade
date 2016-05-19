/* 
 * AccountRefundTrade.java  
 * 
 * version TODO
 *
 * 2016年5月19日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.accounting.refund;

import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.trade.adapter.quickpay.IRefundTrade;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.dao.TransferBatchDAO;
import com.zlebank.zplatform.trade.dao.TransferDataDAO;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.ITxnsRefundService;
import com.zlebank.zplatform.trade.utils.SpringContext;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月19日 上午8:36:35
 * @since
 */
public class AccountRefundTrade implements IRefundTrade {

	private AccEntryService accEntryService;
	private ITxnsRefundService txnsRefundService;
	private ITxnsLogService txnsLogService;

	public AccountRefundTrade() {
		accEntryService = (AccEntryService) SpringContext.getContext().getBean(
				"accEntryService");
		txnsRefundService = (ITxnsRefundService) SpringContext.getContext()
				.getBean("txnsRefundService");
		txnsLogService = (ITxnsLogService) SpringContext.getContext().getBean(
				"txnsLogService");
	}

	/**
	 *
	 * @param tradeBean
	 * @return
	 */
	@Override
	public ResultBean refund(TradeBean tradeBean) {
		// TODO Auto-generated method stub
		return null;
	}

}
