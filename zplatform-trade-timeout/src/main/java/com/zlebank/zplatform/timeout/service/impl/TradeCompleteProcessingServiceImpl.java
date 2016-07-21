/* 
 * TradeCompleteProcessingServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年7月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.timeout.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.timeout.service.TradeCompleteProcessingService;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.enums.TradeStatFlagEnum;
import com.zlebank.zplatform.trade.chanpay.bean.cj.G10001Bean;
import com.zlebank.zplatform.trade.chanpay.bean.cj.G20001Bean;
import com.zlebank.zplatform.trade.chanpay.service.ChanPayQuickPayService;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.service.ITxnsLogService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年7月20日 上午11:43:58
 * @since 
 */
@Service("tradeCompleteProcessingService")
public class TradeCompleteProcessingServiceImpl implements TradeCompleteProcessingService{

	private static final Log log = LogFactory.getLog(TradeCompleteProcessingServiceImpl.class);
	@Autowired
	private ITxnsLogService txnsLogService;
	@Autowired
	private ChanPayQuickPayService chanPayQuickPayService;
	
	/**
	 *
	 * @param txnseqno
	 * @param resultBean
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void chanPayCollectMoneyCompleteTrade(String txnseqno,ResultBean resultBean) {
		TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(txnseqno);
		TradeStatFlagEnum tradeStatFlagEnum = TradeStatFlagEnum.fromValue(txnsLog.getTradestatflag());
		if(tradeStatFlagEnum==TradeStatFlagEnum.PAYING||tradeStatFlagEnum==TradeStatFlagEnum.OVERTIME){//交易支付中或者交易超时
			G20001Bean data = (G20001Bean) resultBean.getResultObj();
			log.info("接收畅捷应答数据：" + JSON.toJSONString(data));
			txnsLogService.updatePayInfo_Fast_result(txnseqno,data.getRetCode(), data.getErrMsg());
			if (!"0000".equals(data.getRetCode())) {// 交易失败
				txnsLogService.updateCoreRetResult(txnseqno, "3699",data.getErrMsg());
			} else {//交易成功
				G10001Bean bean = new G10001Bean();
				bean.setRetCode(data.getRetCode());
				bean.setErrMsg(data.getErrMsg());
				resultBean.setResultObj(bean);
				chanPayQuickPayService.dealWithAccounting(resultBean, txnseqno);
			}
		}
	}

}
