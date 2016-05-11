/* 
 * ChanPayAsyncServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年5月10日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.chanpay.bean.async.RefundAsyncResultBean;
import com.zlebank.zplatform.trade.chanpay.bean.async.TradeAsyncResultBean;
import com.zlebank.zplatform.trade.chanpay.enums.TradeStatusEnum;
import com.zlebank.zplatform.trade.chanpay.service.ChanPayService;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.TxnsGatewaypayModel;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;
import com.zlebank.zplatform.trade.service.ChanPayAsyncService;
import com.zlebank.zplatform.trade.service.IGateWayService;
import com.zlebank.zplatform.trade.service.ITxnsGatewaypayService;
import com.zlebank.zplatform.trade.service.ITxnsLogService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月10日 下午4:47:02
 * @since 
 */
@Service("chanPayAsyncService")
public class ChanPayAsyncServiceImpl implements ChanPayAsyncService{

	@Autowired
	private ChanPayService chanPayService;
	@Autowired
	private ITxnsGatewaypayService gatewaypayService;
	@Autowired
	private ITxnsLogService txnsLogService;
	@Autowired
	private IGateWayService gateWayService;
	/**
	 *
	 * @param tradeAsyncResultBean
	 * @return
	 * @throws TradeException
	 */
	@Override
	public ResultBean dealWithTradeAsync(
			TradeAsyncResultBean tradeAsyncResultBean) throws TradeException {
		//验签
		boolean flag = chanPayService.asyncNotifyTrade(tradeAsyncResultBean);
		if(!flag){
			throw new TradeException("");//验签失败
		}
		//更新数据 网关流水
		String out_trade_no = tradeAsyncResultBean.getOuter_trade_no();
		TradeStatusEnum tradeStatusEnum = TradeStatusEnum.fromValue(tradeAsyncResultBean.getTrade_status());
		TxnsGatewaypayModel gatewayOrder = gatewaypayService.getOrderByOrderNo(out_trade_no);
		
		gatewayOrder.setClosetime(tradeAsyncResultBean.getGmt_close());
		gatewayOrder.setPayfinshtime(tradeAsyncResultBean.getGmt_payment());
		gatewayOrder.setPayorderno(tradeAsyncResultBean.getInner_trade_no());
		gatewayOrder.setPayretcode(tradeStatusEnum.getCode());
		gatewayOrder.setPayretcode(tradeStatusEnum.getMesssage());
		
		
		//更新数据 交易流水
		String txnseqno = gatewayOrder.getRelatetradetxn();
		TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(txnseqno);
		
		
		
		
		//更新数据 交易订单 
		TxnsOrderinfoModel orderinfo = gateWayService.getOrderinfoByOrderNoAndMemberId(txnsLog.getAccordno(), txnsLog.getAccsecmerno());
		
		//处理账务
		
		
		
		return null;
	}

	/**
	 *
	 * @param refundAsyncResultBean
	 * @return
	 * @throws TradeException
	 */
	@Override
	public ResultBean dealWithRefundAsync(
			RefundAsyncResultBean refundAsyncResultBean) throws TradeException {
		// TODO Auto-generated method stub
		return null;
	}

}
