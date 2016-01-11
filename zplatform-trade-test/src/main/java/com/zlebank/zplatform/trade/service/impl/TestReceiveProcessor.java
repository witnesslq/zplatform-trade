/* 
 * TestReceiveProcessor.java  
 * 
 * version TODO
 *
 * 2015年11月17日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.commons.utils.RSAUtils;
import com.zlebank.zplatform.member.service.MerchMKService;
import com.zlebank.zplatform.trade.bean.AppPartyBean;
import com.zlebank.zplatform.trade.bean.PayPartyBean;
import com.zlebank.zplatform.trade.bean.ReaPayResultBean;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.enums.TradeTypeEnum;
import com.zlebank.zplatform.trade.bean.gateway.OrderAsynRespBean;
import com.zlebank.zplatform.trade.dao.ITxnsOrderinfoDAO;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;
import com.zlebank.zplatform.trade.service.IQuickpayCustService;
import com.zlebank.zplatform.trade.service.ITradeReceiveProcessor;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.utils.ConsUtil;
import com.zlebank.zplatform.trade.utils.DateUtil;
import com.zlebank.zplatform.trade.utils.ObjectDynamic;
import com.zlebank.zplatform.trade.utils.UUIDUtil;


/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年11月17日 下午2:29:36
 * @since 
 */
@Service("testReceiveProcessor")
public class TestReceiveProcessor implements ITradeReceiveProcessor{
    @Autowired
    private ITxnsLogService txnsLogService;
    @Autowired
    private IQuickpayCustService quickpayCustService;
    @Autowired
    private ITxnsOrderinfoDAO txnsOrderinfoDAO;
    @Autowired
    private MerchMKService merchMKService;
    /**
     *
     * @param resultBean
     * @param tradeBean
     * @param tradeType
     */
    public void onReceive(ResultBean resultBean,TradeBean tradeBean,TradeTypeEnum tradeType) {
        // TODO Auto-generated method stub
        if(tradeType==TradeTypeEnum.SENDMARGINSMS){//发送/重发短信验证码
           
        }else if(tradeType==TradeTypeEnum.MARGINREGISTER){//开户/银行卡签约
           
        }else if(tradeType==TradeTypeEnum.ONLINEDEPOSITSHORT){//在线入金（基金产品）
           
        }else if(tradeType==TradeTypeEnum.WITHDRAWNOTIFY){//在线出金（基金产品）
            
        }else if(tradeType==TradeTypeEnum.SUBMITPAY){//确认支付（第三方快捷支付渠道）
            saveTestTradeResult(resultBean,tradeBean);
        }else if(tradeType==TradeTypeEnum.BANKSIGN){//交易查询
            
        }else if(tradeType==TradeTypeEnum.UNKNOW){//银行卡签约
           
        }
    }
    
    @Transactional
    public void saveTestTradeResult(ResultBean resultBean,TradeBean tradeBean){
            ReaPayResultBean payResult = (ReaPayResultBean) resultBean.getResultObj();
            if("0000".equals(payResult.getResult_code())||"3006".equals(payResult.getResult_code())||"3053".equals(payResult.getResult_code())||"3054".equals(payResult.getResult_code())||
                    "3056".equals(payResult.getResult_code())||"3083".equals(payResult.getResult_code())||"3081".equals(payResult.getResult_code())){
                //返回这些信息时，表示融宝已经接受到交易请求，但是没有同步处理，等待异步通知
                //对于没有绑定的卡进行绑卡确认，更新状态为00
                //quickpayCustService.updateCardStatus(card.getId());
            }else{
                //订单状态更新为失败
                txnsOrderinfoDAO.updateOrderToFail(tradeBean.getOrderId(),tradeBean.getMerUserId());
                //throw new TradeException("T000",payResult.getResult_msg());
            }
            //String txnseqno, String paytype, String payordno, String payinst, String payfirmerno, String paysecmerno, String payordcomtime, String payordfintime, String cardNo, String rout, String routlvl
            PayPartyBean payPartyBean = new PayPartyBean(tradeBean.getTxnseqno(),"01", tradeBean.getOrderId(), "95000001", ConsUtil.getInstance().cons.getReapay_quickpay_merchant_id(), "", DateUtil.getCurrentDateTime(), "",tradeBean.getCardNo(),"","");
            txnsLogService.updatePayInfo_Fast(payPartyBean);
            txnsLogService.updateReaPayRetInfo(tradeBean.getTxnseqno(), payResult);
            
            
            saveSuccessReaPayTrade(tradeBean.getTxnseqno(),
                    tradeBean.getOrderId(), payResult,tradeBean.getMerchId());
            String commiteTime = DateUtil.getCurrentDateTime();
           
            String reaPayOrderNo =  tradeBean.getOrderId();
            // 处理同步通知和异步通知
            // 根据原始订单拼接应答报文，异步通知商户
            TxnsOrderinfoModel gatewayOrderBean = txnsOrderinfoDAO.getOrderinfoByOrderNo(reaPayOrderNo,tradeBean.getOrderId());
            // 应用方信息
            AppPartyBean appParty = new AppPartyBean("123",
                    "000000000000", commiteTime,
                    DateUtil.getCurrentDateTime(), tradeBean.getTxnseqno(), "AC000000");
            txnsLogService.updateAppInfo(appParty);
            ResultBean orderResp = 
                    generateAsyncRespMessage(reaPayOrderNo,
                            tradeBean.getMerchId());
            if (orderResp.isResultBool()) {
                OrderAsynRespBean respBean = (OrderAsynRespBean) orderResp
                        .getResultObj();
            }
            
    }
    
    @Transactional
    public void saveSuccessReaPayTrade(String txnseqno,String gateWayOrderNo,ReaPayResultBean payResultBean,String merchId){
        TxnsLogModel txnsLog = txnsLogService.get(txnseqno);
        //txnsLog.setAccordfintime(DateUtil.getCurrentDateTime());
        txnsLog.setPayordfintime(DateUtil.getCurrentDateTime());
        txnsLog.setRetcode("RP00");
        txnsLog.setRetinfo("交易成功");
        txnsLog.setRetdatetime(DateUtil.getCurrentDateTime());
        txnsLog.setTradestatflag("00000001");//交易完成结束位
        txnsLog.setTradetxnflag("10000000");//证联支付快捷（基金交易）
        txnsLog.setRelate("10000000");
        txnsLog.setTxnfee(0L);
        txnsLog.setTradeseltxn(UUIDUtil.uuid());
        txnsLog.setPayrettsnseqno(payResultBean.getTrade_no());
        txnsLog.setPayretcode(payResultBean.getResult_code());
        txnsLog.setPayretinfo(payResultBean.getResult_msg());
        txnsLogService.update(txnsLog);
        
        
        TxnsOrderinfoModel orderinfo = txnsOrderinfoDAO.getOrderinfoByOrderNo(gateWayOrderNo,merchId);
        orderinfo.setStatus("00");
        orderinfo.setOrderfinshtime(DateUtil.getCurrentDateTime());
        txnsOrderinfoDAO.update(orderinfo);
        
    }
    
    public ResultBean generateAsyncRespMessage(String orderNo,String memberId){
        ResultBean resultBean = null;
        try {
            TxnsOrderinfoModel orderinfo = txnsOrderinfoDAO.getOrderinfoByOrderNo(orderNo,memberId);
            
            
            TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(orderinfo.getRelatetradetxn());
             String version="v1.0";// 网关版本
             String encoding="1";// 编码方式
             String certId="";// 证书 ID
             String signature="";// 签名
             String signMethod="01";// 签名方法
             String merId=txnsLog.getAccfirmerno();// 商户代码
             String orderId=txnsLog.getAccordno();// 商户订单号
             String txnType=orderinfo.getTxntype();// 交易类型
             String txnSubType=orderinfo.getTxnsubtype();// 交易子类
             String bizType=orderinfo.getBiztype();// 产品类型
             String accessType="2";// 接入类型
             String txnTime=orderinfo.getOrdercommitime();// 订单发送时间
             String txnAmt=orderinfo.getOrderamt()+"";// 交易金额
             String currencyCode="156";// 交易币种
             String reqReserved=orderinfo.getReqreserved();// 请求方保留域
             String reserved="";// 保留域
             String queryId=txnsLog.getTradeseltxn();// 交易查询流水号
             String respCode=txnsLog.getRetcode();// 响应码
             String respMsg=txnsLog.getRetinfo();// 应答信息
             String settleAmt="";// 清算金额
             String settleCurrencyCode="";// 清算币种
             String settleDate=txnsLog.getAccsettledate();// 清算日期
             String traceNo=txnsLog.getTradeseltxn();// 系统跟踪号
             String traceTime=DateUtil.getCurrentDateTime();// 交易传输时间
             String exchangeDate="";// 兑换日期
             String exchangeRate="";// 汇率
             String accNo="";// 账号
             String payCardType="";// 支付卡类型
             String payType="";// 支付方式
             String payCardNo="";// 支付卡标识
             String payCardIssueName="";// 支付卡名称
             String bindId="";// 绑定标识号
            
             OrderAsynRespBean orderRespBean = new OrderAsynRespBean(version, encoding, certId, signature, signMethod, merId, orderId, txnType, txnSubType, bizType, accessType, txnTime, txnAmt, currencyCode, reqReserved, reserved, queryId, respCode, respMsg, settleAmt, settleCurrencyCode, settleDate, traceNo, traceTime, exchangeDate, exchangeRate, accNo, payCardType, payType, payCardNo, payCardIssueName, bindId);
            
            
            String privateKey = merchMKService.get(orderinfo.getFirmemberno()).getLocalPriKey().trim();
            resultBean = new ResultBean(generateAsyncOrderResult(orderRespBean, privateKey));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            resultBean = new ResultBean("RC99", "系统异常");
        }
        return resultBean;
    }
    public  OrderAsynRespBean generateAsyncOrderResult(OrderAsynRespBean orderAsyncRespBean,String privateKey) throws Exception{   
        String[] unParamstring = {"signature"};
        String dataMsg = ObjectDynamic.generateParamer(orderAsyncRespBean, false, unParamstring).trim();
        byte[] data =  URLEncoder.encode(dataMsg,"utf-8").getBytes();
        orderAsyncRespBean.setSignature(URLEncoder.encode(RSAUtils.sign(data, privateKey),"utf-8"));
        return orderAsyncRespBean;
    }
}
