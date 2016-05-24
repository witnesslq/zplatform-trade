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

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.acc.bean.TradeInfo;
import com.zlebank.zplatform.acc.exception.AbstractBusiAcctException;
import com.zlebank.zplatform.acc.exception.AccBussinessException;
import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.acc.service.entry.EntryEvent;
import com.zlebank.zplatform.commons.dao.pojo.AccStatusEnum;
import com.zlebank.zplatform.trade.adapter.accounting.impl.ChargeAccounting;
import com.zlebank.zplatform.trade.adapter.quickpay.IRefundTrade;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsRefundModel;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.ITxnsRefundService;
import com.zlebank.zplatform.trade.utils.DateUtil;
import com.zlebank.zplatform.trade.utils.OrderNumber;
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
	private static final Log log = LogFactory.getLog(ChargeAccounting.class);
	
	private AccEntryService accEntryService;
	private ITxnsRefundService txnsRefundService;
	private ITxnsLogService txnsLogService;

	public AccountRefundTrade() {
		accEntryService = (AccEntryService) SpringContext.getContext().getBean(
				"accEntryServiceImpl");
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
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public ResultBean refund(TradeBean tradeBean) {
		log.info("交易:"+tradeBean.getTxnseqno()+"退款账务处理开始");
		ResultBean resultBean = null;
		TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(tradeBean.getTxnseqno());
		//记录退款的流水
		String payordno = updateRefund(tradeBean.getTxnseqno(),txnsLog.getAccmemberid());
		/**支付订单号**/
        
        /**交易类型**/
        String busiCode = txnsLog.getBusicode();
        /**付款方会员ID**/
        String payMemberId = txnsLog.getAccsecmerno();
        /**收款方会员ID**/
        String payToMemberId = txnsLog.getAccmemberid();
        /**收款方父级会员ID**/
        String payToParentMemberId="" ;
        /**渠道**/
        String channelId = "99999999";//支付机构代码
        /**产品id**/
        String productId = "";
        /**交易金额**/
        BigDecimal amount = new BigDecimal(txnsLog.getAmount());
        /**佣金**/
        BigDecimal commission = new BigDecimal(txnsLog.getTradcomm());
        /**手续费**/
        BigDecimal charge = new BigDecimal(txnsLog.getTxnfee());
        /**金额D**/
        BigDecimal amountD = new BigDecimal(0);
        /**金额E**/
        BigDecimal amountE = new BigDecimal(0);
        
        TradeInfo tradeInfo = new TradeInfo(txnsLog.getTxnseqno(), payordno, busiCode, payMemberId, payToMemberId, payToParentMemberId, channelId, productId, amount, commission, charge, amountD, amountE, false);
        tradeInfo.setCoopInstCode(txnsLog.getAccfirmerno());
        
        log.info(JSON.toJSONString(tradeInfo));
        try {
			accEntryService.accEntryProcess(tradeInfo,EntryEvent.TRADE_SUCCESS);
		}  catch (AccBussinessException e) {
            resultBean = new ResultBean(e.getCode(), e.getMessage());
            e.printStackTrace();
        } catch (AbstractBusiAcctException e) {
            resultBean = new ResultBean(e.getCode(), e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            resultBean = new ResultBean("T099", e.getMessage());
            e.printStackTrace();
        }
        //resultBean = new ResultBean("success");
        if(txnsLog==null){
            return resultBean;
        }
        if(resultBean.isResultBool()){
            txnsLog.setApporderstatus(AccStatusEnum.Finish.getCode());
            txnsLog.setApporderinfo("退款账务成功");
        }else{
            txnsLog.setApporderstatus(AccStatusEnum.AccountingFail.getCode());
            txnsLog.setApporderinfo(resultBean.getErrMsg());
        }
        txnsLogService.updateAppStatus(tradeBean.getTxnseqno(), txnsLog.getApporderstatus(), txnsLog.getApporderinfo());
		
        TxnsRefundModel refund = txnsRefundService.getRefundByTxnseqno(tradeBean.getTxnseqno());
        refund.setStatus("00");
        txnsRefundService.update(refund);
        
        log.info("交易:"+tradeBean.getTxnseqno()+"退款账务处理成功");
		return resultBean;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public String updateRefund(String txnseqno,String memberId){
		TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(txnseqno);
        txnsLog.setPaytype("03"); //支付类型（01：快捷，02：网银，03：账户）
        txnsLog.setPayordno(OrderNumber.getInstance().generateAppOrderNo());//支付定单号
        txnsLog.setPayinst("99999999");//支付所属机构
        txnsLog.setPayfirmerno(memberId);//支付一级商户号-个人会员
        //txnsLog.setPaysecmerno(payPartyBean.getPaysecmerno());//支付二级商户号
        txnsLog.setPayordcomtime(DateUtil.getCurrentDateTime());//支付定单提交时间
        
        //更新交易流水表交易位
        txnsLog.setRelate("01000000");
        txnsLog.setTradetxnflag("01000000");
        txnsLog.setCashcode("");
        //支付定单完成时间
        txnsLogService.update(txnsLog);
        return txnsLog.getPayordno();
	}

}
