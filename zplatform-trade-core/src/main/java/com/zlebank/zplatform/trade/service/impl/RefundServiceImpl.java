/* 
 * RefundServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年5月17日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.trade.adapter.quickpay.IRefundTrade;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.factory.TradeAdapterFactory;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsRefundModel;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.ITxnsRefundService;
import com.zlebank.zplatform.trade.service.RefundRouteConfigService;
import com.zlebank.zplatform.trade.service.RefundService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月17日 下午4:25:32
 * @since 
 */
@Service("refundService")
public class RefundServiceImpl implements RefundService{
	
	@Autowired
	private ITxnsRefundService txnsRefundService;
	@Autowired
	private ITxnsLogService txnsLogService;
	@Autowired
	private RefundRouteConfigService refundRouteConfigService;

	/**
	 *
	 * @param refundOrderNo
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public ResultBean execute(String refundOrderNo,String merchNo) {
		ResultBean resultBean = null;
		// TODO Auto-generated method stub
		//退款订单
		TxnsRefundModel refundOrder = txnsRefundService.getRefundByRefundorderNo(refundOrderNo, merchNo);
		if(refundOrder==null){
			return new ResultBean("GW15", "找不到原始订单");
		}
		//原交易流水
		TxnsLogModel txnsLog_old = txnsLogService.getTxnsLogByTxnseqno(refundOrder.getOldtxnseqno());
		//退款的交易流水
		TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(refundOrder.getReltxnseqno());
		//原始的支付方式 （01：快捷，02：网银，03：账户）
		String payType = txnsLog_old.getPaytype();
		//匿名判断
		String payMember = txnsLog_old.getAccmemberid();
		boolean anonFlag = false;
		if("999999999999999".equals(payMember)){
			anonFlag = true;
		}
		//原交易渠道号
		String payChannelCode = txnsLog_old.getPayinst();
		//原交易类型  1000002为账户余额支付
		String accbusicode = txnsLog_old.getAccbusicode();
		//退款路由选择退款渠道或者退款的方式
		ResultBean refundRoutResultBean = refundRouteConfigService.getTransRout(DateUtil.getCurrentDateTime(), txnsLog.getAmount()+"", merchNo, accbusicode, txnsLog_old.getPan(), payChannelCode, anonFlag?"1":"0");
		if(refundRoutResultBean.isResultBool()){
			String refundRout = refundRoutResultBean.getResultObj().toString();
			try {
				IRefundTrade refundTrade = TradeAdapterFactory.getInstance().getRefundTrade(refundRout);
				TradeBean tradeBean = new TradeBean();
				tradeBean.setTxnseqno(txnsLog.getTxnseqno());
				refundTrade.refund(tradeBean);
				resultBean =new ResultBean("success");
			} catch (TradeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resultBean =new ResultBean(e.getCode(),e.getMessage());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resultBean =new ResultBean("","无退款实现类");
				resultBean.setResultBool(false);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				resultBean =new ResultBean("","退款失败");
				resultBean.setResultBool(false);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				resultBean =new ResultBean("","退款失败");
				resultBean.setResultBool(false);
				e.printStackTrace();
			}
		}else{
			resultBean = new ResultBean("此退款交易无有效路由");
			resultBean.setResultBool(false);
		}
		
		
		return resultBean;
	}

}
