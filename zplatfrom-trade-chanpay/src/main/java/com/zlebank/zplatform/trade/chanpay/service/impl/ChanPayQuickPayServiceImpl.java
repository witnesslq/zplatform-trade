/* 
 * ChanPayQuickPayServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年6月30日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.chanpay.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.enums.ChannelEnmu;
import com.zlebank.zplatform.trade.chanpay.bean.cj.G60001Bean;
import com.zlebank.zplatform.trade.chanpay.service.ChanPayCollectMoneyService;
import com.zlebank.zplatform.trade.chanpay.service.ChanPayQuickPayService;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.PojoRealnameAuth;
import com.zlebank.zplatform.trade.model.TxnsWithholdingModel;
import com.zlebank.zplatform.trade.service.ITxnsWithholdingService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年6月30日 下午4:50:07
 * @since 
 */
@Service("chanPayQuickPayService")
public class ChanPayQuickPayServiceImpl implements ChanPayQuickPayService{

	private static final Log log = LogFactory.getLog(ChanPayQuickPayServiceImpl.class);
	@Autowired
	private ChanPayCollectMoneyService chanPayCollectMoneyService;
	@Autowired
	private ITxnsWithholdingService txnsWithholdingService;
	
	public ResultBean protocolSign(TradeBean tradeBean){
		log.info("开始畅捷协议签约,txnseqno:"+tradeBean.getTxnseqno());
		//记录畅捷实名认证流水
		TxnsWithholdingModel txnsWithholdingModel = new TxnsWithholdingModel();
		
		
		//畅捷协议签约
		chanPayCollectMoneyService.protocolSign(tradeBean);
		
		log.info("畅捷协议签约结束,txnseqno:"+tradeBean.getTxnseqno());
		return null;
	}
	
	public ResultBean realNameAuth(TradeBean tradeBean) throws TradeException{
		log.info("开始畅捷实名认证,txnseqno:"+tradeBean.getTxnseqno());
		PojoRealnameAuth pojoRealnameAuth = new PojoRealnameAuth(tradeBean);
		TxnsWithholdingModel txnsWithholdingModel = new TxnsWithholdingModel(pojoRealnameAuth,ChannelEnmu.CHANPAYCOLLECTMONEY);
		tradeBean.setPayOrderNo(txnsWithholdingModel.getSerialno());
		txnsWithholdingService.saveWithholdingLog(txnsWithholdingModel);
		ResultBean resultBean = chanPayCollectMoneyService.realNameAuth(tradeBean);
		if(resultBean.isResultBool()){
			G60001Bean data = (G60001Bean) resultBean.getResultObj();
			txnsWithholdingModel.setExeccode(data.getRetCode());
			txnsWithholdingModel.setExecmsg(data.getErrMsg());
			txnsWithholdingService.updateRealNameResult(txnsWithholdingModel);
		}else{
			txnsWithholdingModel.setExeccode(resultBean.getErrCode());
			txnsWithholdingModel.setExecmsg(resultBean.getErrMsg());
			txnsWithholdingService.updateRealNameResult(txnsWithholdingModel);
		}
		log.info("畅捷实名认证结束,txnseqno:"+tradeBean.getTxnseqno());
		return resultBean;
	}
	
	
}
