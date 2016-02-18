/* 
 * SafeGuardMoneyAccounting.java  
 * 
 * version TODO
 *
 * 2015年9月10日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.adapter.accounting.impl;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zlebank.zplatform.acc.bean.TradeInfo;
import com.zlebank.zplatform.acc.exception.AbstractBusiAcctException;
import com.zlebank.zplatform.acc.exception.AccBussinessException;
import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.trade.adapter.accounting.IAccounting;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.utils.SpringContext;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年9月10日 下午6:28:23
 * @since 
 */
public class SafeGuardMoneyAccounting implements IAccounting{
    private static final Log log = LogFactory.getLog(SafeGuardMoneyAccounting.class);
    private ITxnsLogService txnsLogService;
    private AccEntryService accEntryService;
    /**
     *
     * @param txnseqno
     * @return
     */
    public ResultBean accountedFor(String txnseqno) {
        ResultBean resultBean = null;
        log.info("交易:"+txnseqno+"保障金入账开始");
        TxnsLogModel txnsLog = null;
            try {
                txnsLog = txnsLogService
                        .getTxnsLogByTxnseqno(txnseqno);
                /**支付订单号**/
                String payordno = txnsLog.getPayordno();
                /**交易类型**/
                String busiCode = txnsLog.getBusicode();
                /**付款方会员ID**/
                String payMemberId = txnsLog.getAccsecmerno().toString();
                /**收款方会员ID**/
                String payToMemberId = txnsLog.getAccsecmerno().toString();
                /**收款方父级会员ID**/
                String payToParentMemberId="" ;
                /**渠道**/
                String channelId = txnsLog.getPayinst();//支付机构代码
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
                
                TradeInfo tradeInfo = new TradeInfo();
                tradeInfo.setPayordno(payordno);
                tradeInfo.setTxnseqno(txnseqno);
                tradeInfo.setAmount(amount);;
                tradeInfo.setAmountD(amountD);
                tradeInfo.setAmountE(amountE);
                tradeInfo.setBusiCode(busiCode);
                tradeInfo.setChannelId(channelId);
                tradeInfo.setCharge(charge);
                tradeInfo.setCommission(commission);
                tradeInfo.setPayMemberId(payMemberId);
                tradeInfo.setPayToMemberId(payToMemberId);
                tradeInfo.setPayToParentMemberId(payToParentMemberId);
                tradeInfo.setProductId(productId);
                accEntryService.accEntryProcess(tradeInfo);
                resultBean = new ResultBean("success");
                log.info("保障金入账成功");
            } catch (AccBussinessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                resultBean = new ResultBean(e.getCode(), e.getMessage());
            } catch (AbstractBusiAcctException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                resultBean = new ResultBean(e.getCode(), e.getMessage());
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                resultBean = new ResultBean("T099", e.getMessage());
            }
            if(txnsLog==null){
                return resultBean;
            }
            if(resultBean.isResultBool()){
                txnsLog.setApporderstatus("00");
                txnsLog.setApporderinfo("保障金入账成功");
            }else{
                txnsLog.setApporderstatus(resultBean.getErrCode().substring(2));
                txnsLog.setApporderinfo(resultBean.getErrMsg());
            }
            txnsLogService.updateAppStatus(txnseqno, txnsLog.getApporderstatus(), txnsLog.getApporderinfo());
        return resultBean;
    }

    /**
     * 
     */
    public SafeGuardMoneyAccounting() {
        txnsLogService = (ITxnsLogService) SpringContext.getContext().getBean("txnsLogService");
        accEntryService = (AccEntryService) SpringContext.getContext().getBean("accEntryServiceImpl");
    }

    @Override
    public ResultBean accountedForInsteadPay(String batchno) {
        // TODO Auto-generated method stub
        return null;
    }

}
