/* 
 * WeChatRefundTrade.java  
 * 
 * version TODO
 *
 * 2016年5月27日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.wechat.enums;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.config.TxNamespaceHandler;

import net.sf.json.JSONObject;

import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.trade.adapter.quickpay.IRefundTrade;
import com.zlebank.zplatform.trade.bean.PayPartyBean;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.enums.ChannelEnmu;
import com.zlebank.zplatform.trade.bean.enums.ChnlTypeEnum;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.utils.ConsUtil;
import com.zlebank.zplatform.trade.utils.OrderNumber;
import com.zlebank.zplatform.wechat.exception.WXVerifySignFailedException;
import com.zlebank.zplatform.wechat.wx.WXApplication;
import com.zlebank.zplatform.wechat.wx.bean.RefundBean;
import com.zlebank.zplatform.wechat.wx.bean.RefundResultBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月27日 下午1:39:20
 * @since
 */
public class WeChatRefundTrade implements IRefundTrade {

	private static final Log log = LogFactory.getLog(WeChatRefundTrade.class);

	private ITxnsLogService txnsLogService;

	/**
	 *
	 * @param tradeBean
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor=Throwable.class)
	public ResultBean refund(TradeBean tradeBean) {

		try {
			TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(tradeBean.getTxnseqno());
			TxnsLogModel txnsLog_old = txnsLogService.getTxnsLogByTxnseqno(txnsLog.getTxnseqnoOg());

			WXApplication instance = new WXApplication();
			RefundBean rb = new RefundBean();
			rb.setOut_refund_no(String.valueOf(System.currentTimeMillis()));
			// 退款流水号（唯一，可当场生成）
			rb.setOut_trade_no(OrderNumber.getInstance().generateWeChatOrderNO());// 原商户号（证联生成的）
			rb.setRefund_fee(txnsLog.getAmount()+""); // 退款金额
			rb.setTotal_fee(txnsLog_old.getAmount()+""); // 总金额
			rb.setTransaction_id(txnsLog_old.getPayrettsnseqno()); // 原微信订单号（微信返回的）
			String payordcomtime = DateUtil.getCurrentDateTime();
			//更新支付方信息
			PayPartyBean payPartyBean = new PayPartyBean(tradeBean.getTxnseqno(), 
					"05", 
					rb.getOut_trade_no(), 
					ChannelEnmu.WEBCHAT.getChnlcode(), 
					ConsUtil.getInstance().cons.getWechat_mchID(), 
					"", 
					payordcomtime, 
					DateUtil.getCurrentDateTime(), 
					"");
			txnsLogService.updatePayInfo_Fast(payPartyBean);
			RefundResultBean refund = instance.refund(rb); // 进行退款
			log.info("【退款返回结果】" + JSONObject.fromObject(refund));
			String retcode = "";
			String retinfo = "";
			if(ResultCodeEnum.fromValue(refund.getResult_code())==ResultCodeEnum.SUCCESS){
				retcode=refund.getResult_code();
				retinfo = "退款申请成功";
			}else{
				retcode=refund.getErr_code();
				retinfo = refund.getErr_code_des();
			}
			txnsLogService.updatePayInfo_Fast_result(tradeBean.getTxnseqno(),refund.getTransaction_id(),retcode, retinfo);
		} catch (WXVerifySignFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
