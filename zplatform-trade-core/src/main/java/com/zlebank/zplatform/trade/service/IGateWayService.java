/* 
 * IGateOrderService.java  
 * 
 * version TODO
 *
 * 2015年8月27日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.trade.bean.ReaPayResultBean;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.ZLPayResultBean;
import com.zlebank.zplatform.trade.bean.gateway.OrderBean;
import com.zlebank.zplatform.trade.bean.gateway.OrderRespBean;
import com.zlebank.zplatform.trade.bean.gateway.QueryBean;
import com.zlebank.zplatform.trade.bean.gateway.QueryResultBean;
import com.zlebank.zplatform.trade.bean.gateway.RiskRateInfoBean;
import com.zlebank.zplatform.trade.bean.wap.WapCardBean;
import com.zlebank.zplatform.trade.bean.wap.WapDebitCardSingRespBean;
import com.zlebank.zplatform.trade.bean.wap.WapOrderRespBean;
import com.zlebank.zplatform.trade.bean.wap.WapRefundBean;
import com.zlebank.zplatform.trade.bean.wap.WapWithdrawAccBean;
import com.zlebank.zplatform.trade.bean.wap.WapWithdrawBean;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年8月27日 上午10:54:54
 * @since 
 */
public interface IGateWayService extends IBaseService<TxnsOrderinfoModel, Long>{
    public ResultBean verifyOrder(OrderBean order);
    public String dealWithOrder(OrderBean order,RiskRateInfoBean riskRateInfoBean) throws TradeException;
    public void saveSuccessTrade(String txnseqno,String gateWayOrderNo,ZLPayResultBean zlPayResultBean);
    public void saveErrorTrade();
    public TxnsOrderinfoModel getOrderinfoByOrderNo(String orderNo);
    public ResultBean generateRespMessage(String orderNo,String memberId);
    public void saveFailTrade(String txnseqno,String gateWayOrderNo,ZLPayResultBean zlPayResultBean);
    public void saveSuccessReaPayTrade(String txnseqno,String gateWayOrderNo,ReaPayResultBean payResultBean);
    public void saveFailReaPayTrade(String txnseqno,String gateWayOrderNo,ReaPayResultBean payResultBean);
    public ResultBean decryptCustomerInfo(String memberId,String encryptData);
    public void updateOrderToStartPay(String orderNo) throws TradeException;
    public ResultBean verifyQueryOrder(QueryBean queryBean);
    public ResultBean generateQueryResultBean(QueryResultBean queryResultBean);
    public String dealWithWebOrder(OrderBean order,RiskRateInfoBean riskRateInfoBean) throws TradeException;
    public TxnsOrderinfoModel getOrderinfoByTN(String tn) ;
    public void saveAcctTrade(String txnseqno,String gateWayOrderNo,ResultBean resultBean);
    public String createWapOrder(String json)throws TradeException, UnsupportedEncodingException;
    public void verifyWapOrder(JSONObject order) throws TradeException;
    public void verifyRepeatWapOrder(String orderNo,String txntime,String amount,String merchId,String memberId) throws TradeException;
    public void verifyRepeatWebOrder(String orderNo,String txntime,String amount,String merchId,String memberId) throws TradeException;
    public void verifyMerch(String merchId,String subMerchId) throws TradeException;
    public void signWapMessage(WapOrderRespBean orderRespBean);
    public String bankCardSign(String json) throws TradeException;
    public void signWapCardSignMessage(WapDebitCardSingRespBean respBean);
    public void sendSMSMessage(String json) throws TradeException;
    public void submitPay(String json) throws TradeException;
    public String querySignInfo(String json) throws TradeException;
    public void accountPay(String json) throws TradeException;
    public String queryUsableBankCard(String json);
    public String dealWithRefundOrder(WapRefundBean refundBean) throws TradeException;
    public String dealWithWithdrawOrder(WapWithdrawBean withdrawBean,WapWithdrawAccBean withdrawAccBean) throws TradeException;
    public String withdraw(String json) throws TradeException;
    public String refund(String json) throws TradeException;
    public ResultBean generateAsyncRespMessage(String orderNo,String memberId);
    public void updateOrderToFail(String orderNo);
    public OrderRespBean generateWithdrawRespMessage(String orderNo);
    /**
     * 校验会员业务
     * @param orderBean
     * @param rateInfoBean
     * @return
     */
    public ResultBean validateMemberBusiness(OrderBean orderBean,RiskRateInfoBean rateInfoBean);
    /**
     * 通过订单号和会员号获取交易订单信息
     * @param orderNo
     * @param memberId
     * @return
     */
    public TxnsOrderinfoModel getOrderinfoByOrderNoAndMemberId(String orderNo,String memberId) ;
    /**
     * 获取手续费
     * @param txnsLog
     * @return
     */
    public Long getTxnFee(TxnsLogModel txnsLog);

    /**
     * 绑定银行卡
     * @param memberId 合作机构号/商户号
     * @param personMemberId 个人会员号
     * @param cardBean
     * @return
     */
    public ResultBean bindingBankCard(String memberId,String personMemberId,WapCardBean cardBean);
    
    /**
     * 
     * @param memberId 会员号
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @param page 页数
     * @param rows 行数
     * @return
     */
    public List<Map<String, Object>> queryOrderInfo(String memberId,String beginDate,String endDate,int page,int rows);

    public String dealWithWapOrder(OrderBean order) throws TradeException;
    
    /**
     * 
     * @param orderNo
     * @param memberId
     * @return
     */
    public TxnsOrderinfoModel getPersonOrder(String orderNo,String memberId);
    
    /**
     * 
     * @param memberId
     * @param beginDate
     * @param endDate
     * @return
     */
    public long queryOrderInfoCount(String memberId,String beginDate,String endDate);
    
    /**
     * 检查资金账户状态
     * @param merchNo 商户号
     * @param memberId 会员号
     * @throws TradeException
     */
    public void checkBusiAcct(String merchNo,String memberId) throws TradeException;

    /**
     * 处理商户收银台的订单，保存交易流水数据和交易订单数据
     * @param order
     * @return
     * @throws TradeException
     */
    public String dealWithMerchOrder(OrderBean order) throws TradeException;
    
    /**
     * 验证会员支付密码
     * @param memberId
     * @param pwd
     * @param memberType
     * @return
     * @throws TradeException
     */
    public boolean validatePayPWD(String memberId,String pwd,MemberType memberType) throws TradeException;
    
    public String dealWithRefundOrder(String orderNo,String merchNo,String txnAmt) throws TradeException;
}
