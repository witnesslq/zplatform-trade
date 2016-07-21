/* 
 * ChanPayTradeThread.java  
 * 
 * version TODO
 *
 * 2016年7月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.timeout.trade;

import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.timeout.service.TradeCompleteProcessingService;
import com.zlebank.zplatform.timeout.service.TradeQueueQuery;
import com.zlebank.zplatform.trade.adapter.quickpay.IQuickPayTrade;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.TradeQueueBean;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.service.TradeQueueService;
import com.zlebank.zplatform.trade.utils.SpringContext;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年7月20日 上午10:22:24
 * @since 
 */
public class ChanPayTradeThread implements TradeQueueQuery{

	private TradeQueueBean tradeQueueBean;
	
	private TradeCompleteProcessingService completeProcessingService = (TradeCompleteProcessingService) SpringContext.getContext().getBean("tradeCompleteProcessingService");
	private TradeQueueService tradeQueueService = (TradeQueueService) SpringContext.getContext().getBean("tradeQueueService");
	/**
	 *
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		TradeBean trade = new TradeBean();
		trade.setTxnseqno(tradeQueueBean.getTxnseqno());
		try {
			ResultBean resultBean = getTradeQuery().queryTrade(trade);
			if(resultBean.isResultBool()){
				if("0002".equals(resultBean.getErrCode())||"0001".equals(resultBean.getErrCode())){//待处理，重回队列，等待下次查询
					tradeQueueService.addTradeQueue(tradeQueueBean);
					return ;
				}
				completeProcessingService.chanPayCollectMoneyCompleteTrade(trade.getTxnseqno(), resultBean);
			}
			
		} catch (TradeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param tradeQueueBean the tradeQueueBean to set
	 */
	@Override
	public void setTradeQueueBean(TradeQueueBean tradeQueueBean) {
		this.tradeQueueBean = tradeQueueBean;
	}

	public IQuickPayTrade getTradeQuery() throws TradeException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		IQuickPayTrade  quickPayTrade = null;
        if(StringUtil.isNotEmpty(tradeQueueBean.getImpl())){
             quickPayTrade =(IQuickPayTrade) Class.forName(tradeQueueBean.getImpl()).newInstance();
        }
        return quickPayTrade;
    }
}
