/* 
 * TradeQueueScanServiceImp.java  
 * 
 * version TODO
 *
 * 2016年7月19日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.timeout.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.timeout.pool.TradeQueueThreadPool;
import com.zlebank.zplatform.timeout.service.TradeQueueQuery;
import com.zlebank.zplatform.timeout.service.TradeQueueScanService;
import com.zlebank.zplatform.timeout.trade.CMBCTradeThread;
import com.zlebank.zplatform.timeout.trade.ChanPayTradeThread;
import com.zlebank.zplatform.trade.bean.TradeQueueBean;
import com.zlebank.zplatform.trade.bean.enums.ChannelEnmu;
import com.zlebank.zplatform.trade.bean.enums.TradeQueueEnum;
import com.zlebank.zplatform.trade.bean.enums.TradeStatFlagEnum;
import com.zlebank.zplatform.trade.model.ChnlDetaModel;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.service.IChnlDetaService;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.TradeQueueService;
import com.zlebank.zplatform.trade.service.impl.ChnlDetaServiceImpl;
import com.zlebank.zplatform.trade.utils.ConsUtil;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年7月19日 下午5:28:00
 * @since 
 */
@Service("tradeQueueScanService")
public class TradeQueueScanServiceImpl implements TradeQueueScanService{

	private static final Log log = LogFactory.getLog(TradeQueueScanServiceImpl.class);
	@Autowired
	private TradeQueueService tradeQueueService;
	@Autowired
	private IChnlDetaService chnlDetaService;
	@Autowired
	private ITxnsLogService txnsLogService;
	
	
	public void scanTradeQueue(){
		long queueSize = tradeQueueService.getQueueSize(TradeQueueEnum.TRADEQUEUE);
		if(queueSize>0){
			for(int i=0;i<queueSize;i++){
				TradeQueueBean tradeQueueBean = tradeQueueService.tradeQueuePop();
				if(tradeQueueBean==null){//此处为空时很可能是队列中已经没有交易了，跳出循环
					break;
				}
				//判断交易是否已经完成，账务也处理完成
				TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(tradeQueueBean.getTxnseqno());
				TradeStatFlagEnum tradeStatFlagEnum = TradeStatFlagEnum.fromValue(txnsLog.getTradestatflag());
				if(tradeStatFlagEnum==TradeStatFlagEnum.PAYING||tradeStatFlagEnum==TradeStatFlagEnum.OVERTIME){//交易支付中或者交易超时
					ChnlDetaModel chnlDetaModel = chnlDetaService.getChannelByCode(tradeQueueBean.getPayInsti());
					//交易具体的java实现
					tradeQueueBean.setImpl(chnlDetaModel.getImpl());
					TradeQueueQuery tradeQueueQuery = null;
					if(ChannelEnmu.CHANPAYCOLLECTMONEY.getChnlcode().equals(tradeQueueBean.getPayInsti())){
						tradeQueueQuery = new ChanPayTradeThread();
						tradeQueueQuery.setTradeQueueBean(tradeQueueBean);
					}else if (ChannelEnmu.CMBCWITHHOLDING.getChnlcode().equals(tradeQueueBean.getPayInsti())) {
						tradeQueueQuery = new CMBCTradeThread();
						tradeQueueQuery.setTradeQueueBean(tradeQueueBean);
					}
					if(ConsUtil.getInstance().cons.getIs_junit()==1){
						tradeQueueQuery.run();
					}else{
						TradeQueueThreadPool.getInstance().executeMission(tradeQueueQuery);
					}
				}else{
					continue;
				}
				
				
			}
		}
	}
}
