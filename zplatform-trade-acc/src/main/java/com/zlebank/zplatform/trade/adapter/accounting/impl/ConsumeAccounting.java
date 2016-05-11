/* 
 * ConsumeAccounting.java  
 * 
 * version TODO
 *
 * 2015年9月7日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.adapter.accounting.impl;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.acc.bean.TradeInfo;
import com.zlebank.zplatform.acc.exception.AbstractBusiAcctException;
import com.zlebank.zplatform.acc.exception.AccBussinessException;
import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.commons.dao.pojo.AccStatusEnum;
import com.zlebank.zplatform.commons.utils.StringUtil;
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
 * @date 2015年9月7日 上午11:48:09
 * @since 
 */
public class ConsumeAccounting implements IAccounting{
    private static final Log log = LogFactory.getLog(ConsumeAccounting.class);
    private ITxnsLogService txnsLogService;
    private AccEntryService accEntryService;
    
    /**
     *
     * @param txnseqno
     * @return
     */
    public ResultBean accountedFor(String txnseqno) {
        
        log.info("交易:"+txnseqno+"消费入账开始");
        ResultBean resultBean = null;
        TxnsLogModel txnsLog = null;
       
             try {
                txnsLog = txnsLogService
                        .getTxnsLogByTxnseqno(txnseqno);
                /**支付订单号**/
                String payordno = txnsLog.getPayordno();
                /**交易类型**/
                String busiCode = txnsLog.getBusicode();
                
                /**付款方会员ID**/
                String payMemberId = StringUtil.isNotEmpty(txnsLog.getAccmemberid())?txnsLog.getAccmemberid():"999999999999999";
                /**收款方会员ID**/
                String payToMemberId = StringUtil.isEmpty(txnsLog.getAccsecmerno())?txnsLog.getAccfirmerno():txnsLog.getAccsecmerno();
                /**收款方父级会员ID**/
                String payToParentMemberId=txnsLog.getAccfirmerno()+"";
                /**渠道**/
                String channelId = txnsLog.getPayinst();//支付机构代码
                if("99999999".equals(channelId)){
                    busiCode = "10000002";
                    payMemberId = txnsLog.getPayfirmerno();
                }else {
                    busiCode = "10000001";
                }
                /**产品id**/
                String productId = "";
                /**交易金额**/
                BigDecimal amount = new BigDecimal(txnsLog.getAmount());
                /**佣金**/
                BigDecimal commission = new BigDecimal(txnsLog.getTradcomm());
                /**手续费**/
                long txnfee = 0;
                if (txnsLog.getTxnfee() != null) {
                    txnfee = txnsLog.getTxnfee();
                }
                BigDecimal charge = new BigDecimal(txnfee);
                /**金额D**/
                BigDecimal amountD = new BigDecimal(0);
                /**金额E**/
                BigDecimal amountE = new BigDecimal(0);
                /** 分账标记**/
                boolean isSplit = false;
                if("10000004".equals(txnsLog.getBusicode())){
                    isSplit = true;
                }
                txnsLogService.updateAccBusiCode(txnseqno, busiCode);
                TradeInfo tradeInfo = new TradeInfo(txnseqno, payordno, busiCode, payMemberId, payToMemberId, payToParentMemberId, channelId, productId, amount, commission, charge, amountD, amountE, isSplit);
                /*tradeInfo.setPayordno(payordno);
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
                tradeInfo.setProductId(productId);*/
               
                
                log.info(JSON.toJSONString(tradeInfo));
                accEntryService.accEntryProcess(tradeInfo);
                resultBean = new ResultBean("00","交易成功");
                resultBean.setResultBool(true);
                log.info("交易:"+txnseqno+"消费入账成功");
            } catch (AccBussinessException e) {
                resultBean = new ResultBean("AP05", e.getMessage());
                e.printStackTrace();
            } catch (AbstractBusiAcctException e) {
                resultBean = new ResultBean("AP05", e.getMessage());
                e.printStackTrace();
            } catch (NumberFormatException e) {
                resultBean = new ResultBean("T099", e.getMessage());
                e.printStackTrace();
            }
        
        if(txnsLog==null){
            return resultBean;
        }
        if(resultBean.isResultBool()){
            txnsLog.setApporderstatus(AccStatusEnum.Finish.getCode());
            txnsLog.setApporderinfo("消费入账成功");
        }else{
            txnsLog.setApporderstatus(AccStatusEnum.AccountingFail.getCode());
            txnsLog.setApporderinfo(resultBean.getErrMsg());
        }
        txnsLogService.updateAppStatus(txnseqno, txnsLog.getApporderstatus(), txnsLog.getApporderinfo());
        return resultBean;
    }

    public ConsumeAccounting(){
        txnsLogService = (ITxnsLogService) SpringContext.getContext().getBean("txnsLogService");
        accEntryService = (AccEntryService) SpringContext.getContext().getBean("accEntryServiceImpl");
    }

    @Override
    public ResultBean accountedForInsteadPay(String batchno) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
