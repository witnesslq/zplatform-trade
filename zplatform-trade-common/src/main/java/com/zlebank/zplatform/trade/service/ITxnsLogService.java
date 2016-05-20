/* 
 * ITxnsLogService.java  
 * 
 * version TODO
 *
 * 2015年8月29日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import java.util.List;
import java.util.Map;

import com.zlebank.zplatform.trade.bean.AccountTradeBean;
import com.zlebank.zplatform.trade.bean.AppPartyBean;
import com.zlebank.zplatform.trade.bean.PayPartyBean;
import com.zlebank.zplatform.trade.bean.ReaPayResultBean;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.gateway.QueryBean;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.PojoBankTransferData;
import com.zlebank.zplatform.trade.model.PojoTranData;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsWithholdingModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年8月29日 下午3:26:46
 * @since 
 */
public interface ITxnsLogService extends IBaseService<TxnsLogModel, String>{
    public ResultBean verifyRepeatOrder(String orderId);
    public ResultBean updatePayInfo_Fast(PayPartyBean payPartyBean);
    public ResultBean updateRoutInfo(String txnseqno,String routId,String currentStep,String cashCode);
    public ResultBean updateAppInfo(AppPartyBean appParty);
    
    public TxnsLogModel getTxnsLogByTxnseqno(String txnseqno);
    public Long getTxnFee(TxnsLogModel txnsLog);
    public ResultBean updatePayInfo_ecitic(PayPartyBean payPartyBean);
    
    public void updateReaPayRetInfo(String txnseqno,ReaPayResultBean payResult);
    
    public TxnsLogModel queryTrade(QueryBean queryBean);
    public void saveAccountTrade(AccountTradeBean accountTrade) throws TradeException;
    public void updateAccountTrade(AccountTradeBean accountTrade,ResultBean resultBean) throws TradeException;
    public void updateWapRoutInfo(String txnseqno,String routId) throws TradeException;
    public TxnsLogModel queryLogByTradeseltxn(String queryId);
    public void updateAppStatus(String txnseqno,String appOrderStatus,String appOrderinfo);
    public void tradeRiskControl(String txnseqno,String merchId,String subMerchId,String memberId,String busiCode,String txnAmt,String cardType,String cardNo)throws TradeException;
    public void updateAccBusiCode(String txnseqno,String busicode);
    
    /**
     * 初始化支付方和中心应答信息
     * @param txnseqno
     */
    public void initretMsg(String txnseqno)  throws TradeException;
    
    /**
     * 更新民生跨行代扣应答信息
     * @param txnseqno
     * @param withholdin
     */
    public void updateCMBCWithholdingRetInfo(String txnseqno, TxnsWithholdingModel withholdin);
    
    /**
     * 更新支付方返回结果
     * @param txnseqno
     * @param retcode
     * @param retinfo
     */
    public void updatePayInfo_Fast_result(String txnseqno,String retcode,String retinfo);
    
    /**
     * 跟新交易流水
     * @param txnsLog
     */
    public void updateTxnsLog(TxnsLogModel txnsLog);
    
    /**
     * 保存划拨交易流水交易
     * @param transferDataList
     */
    public void saveTransferLogs(List<PojoTranData> transferDataList);
    
    /**
     * 保存划拨交易流水交易
     * @param transferDataList
     */
    public void saveBankTransferLogs(List<PojoBankTransferData> transferDataList);
    
    /**
     * 获取风控策略
     * @param orders
     * @return
     */
    public List<Map<String,String>> getRiskStrategy(int orders);
    
    /**
     * 保存交易流水日志
     * @param txnsLogModel
     * @throws TradeException
     */
    public void saveTxnsLog(TxnsLogModel txnsLogModel) throws TradeException;
    
    /**
     * 更新中心应答信息
     * @param txnseqno
     * @param retcode
     * @param retinfo
     */
    public void updateCoreRetResult(String txnseqno,String retcode,String retinfo);
    
    public List<?> getAllMemberByDate(String date);
    public List<?> getAllMemberByDateByCharge(String date);
    public List<?> getSumExpense(String memberId,String date);
    public List<?>  getAllMemberDetailedByDate(String memberId,String date);
    public List<?> getSumRefund(String memberId,String date);
    public List<?> getCountSpendingAccount(String memberId,String date);
    public List<?> getCountHandPay(String memberId,String date);
    
    /**
     * 保存博士金电交易流水
     * @param transferDataList
     */
    public void saveBossPayBankTransferLogs(List<PojoBankTransferData> transferDataList);
    
    /**

     * 更新网关支付结果
     * @param payPartyBean
     * @return
     */
    public ResultBean updateGateWayPayResult(PayPartyBean payPartyBean);
    /**
     * 对账任务
     */
    public void excuteRecon();

}
