/* 
 * ChargeAccounting.java  
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

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.acc.bean.TradeInfo;
import com.zlebank.zplatform.acc.exception.AbstractBusiAcctException;
import com.zlebank.zplatform.acc.exception.AccBussinessException;
import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.member.service.MemberService;
import com.zlebank.zplatform.trade.adapter.accounting.IAccounting;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.utils.SpringContext;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年9月10日 下午6:25:43
 * @since 
 */
public class ChargeAccounting implements IAccounting{
    private static final Log log = LogFactory.getLog(ChargeAccounting.class);
    private ITxnsLogService txnsLogService;
    private AccEntryService accEntryService;
    private MemberService memberService;
    /**
     *
     * @param txnseqno
     * @return
     */
   
    public ResultBean accountedFor(String txnseqno) {
        ResultBean resultBean = null;
        log.info("交易:"+txnseqno+"充值入账开始");
        TxnsLogModel txnsLog = null;
        try {
            txnsLog = txnsLogService
                    .getTxnsLogByTxnseqno(txnseqno);
            /**支付订单号**/
            String payordno = txnsLog.getPayordno();
            /**交易类型**/
            String busiCode = txnsLog.getBusicode();
            /**付款方会员ID**/
            String payMemberId = "";
            /**收款方会员ID**/
            String payToMemberId ="";
            if(StringUtil.isEmpty(txnsLog.getAccsecmerno())){
                payMemberId = txnsLog.getAccmemberid();//
                payToMemberId = txnsLog.getAccmemberid();
            }else{
                String memberId = txnsLog.getAccmemberid();
                if(StringUtil.isEmpty(memberId)){
                    throw new TradeException("AP08");
                }
                PojoMember member =memberService.getMbmberByMemberId(memberId, MemberType.Individual);
                if(member==null){
                    throw new TradeException("AP08");
                }
                payMemberId = memberId;
                payToMemberId = memberId;
            }
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
            txnsLogService.updateAccBusiCode(txnseqno, busiCode);
            TradeInfo tradeInfo = new TradeInfo(txnseqno, payordno, busiCode, payMemberId, payToMemberId, payToParentMemberId, channelId, productId, amount, commission, charge, amountD, amountE, false);
            tradeInfo.setPayordno(payordno);
            /*tradeInfo.setTxnseqno(txnseqno);
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
            resultBean = new ResultBean("success");
            log.info("交易:"+txnseqno+"充值入账成功");
        } catch (TradeException e) {
            resultBean = new ResultBean(e.getCode(), e.getMessage());
            e.printStackTrace();
        } catch (AccBussinessException e) {
            resultBean = new ResultBean(e.getCode(), e.getMessage());
            e.printStackTrace();
        } catch (AbstractBusiAcctException e) {
            resultBean = new ResultBean(e.getCode(), e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            resultBean = new ResultBean("T099", e.getMessage());
            e.printStackTrace();
        }
        if(txnsLog==null){
            return resultBean;
        }
        if(resultBean.isResultBool()){
            txnsLog.setApporderstatus("00");
            txnsLog.setApporderinfo("充值入账成功");
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
    public ChargeAccounting() {
        txnsLogService = (ITxnsLogService) SpringContext.getContext().getBean("txnsLogService");
        accEntryService = (AccEntryService) SpringContext.getContext().getBean("accEntryServiceImpl");
        memberService = (MemberService) SpringContext.getContext().getBean("memberServiceImpl");
    }
    @Override
    public ResultBean accountedForInsteadPay(String batchno) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
