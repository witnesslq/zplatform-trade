/* 
 * WeChatServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年5月25日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.wechat.service.impl;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.commons.dao.pojo.BusiTypeEnum;
import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.commons.utils.RSAUtils;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.member.bean.enums.TerminalAccessType;
import com.zlebank.zplatform.member.service.CoopInstiService;
import com.zlebank.zplatform.member.service.MerchMKService;
import com.zlebank.zplatform.trade.adapter.accounting.IAccounting;
import com.zlebank.zplatform.trade.bean.AppPartyBean;
import com.zlebank.zplatform.trade.bean.PayPartyBean;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.enums.ChannelEnmu;
import com.zlebank.zplatform.trade.bean.enums.OrderStatusEnum;
import com.zlebank.zplatform.trade.bean.gateway.AnonOrderAsynRespBean;
import com.zlebank.zplatform.trade.bean.gateway.OrderAsynRespBean;
import com.zlebank.zplatform.trade.dao.ITxnsOrderinfoDAO;
import com.zlebank.zplatform.trade.factory.AccountingAdapterFactory;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.utils.ConsUtil;
import com.zlebank.zplatform.trade.utils.ObjectDynamic;
import com.zlebank.zplatform.trade.utils.OrderNumber;
import com.zlebank.zplatform.trade.utils.SynHttpRequestThread;
import com.zlebank.zplatform.trade.utils.UUIDUtil;
import com.zlebank.zplatform.wechat.enums.ResultCodeEnum;
import com.zlebank.zplatform.wechat.exception.WXVerifySignFailedException;
import com.zlebank.zplatform.wechat.service.WeChatService;
import com.zlebank.zplatform.wechat.wx.PrintBean;
import com.zlebank.zplatform.wechat.wx.WXApplication;
import com.zlebank.zplatform.wechat.wx.bean.PayResultBean;
import com.zlebank.zplatform.wechat.wx.bean.QueryBillBean;
import com.zlebank.zplatform.wechat.wx.bean.WXOrderBean;
import com.zlebank.zplatform.wechat.wx.common.WXConfigure;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月25日 下午1:56:44
 * @since 
 */
@Service("weChatService")
public class WeChatServiceImpl implements WeChatService{

	@Autowired
	private ITxnsOrderinfoDAO txnsOrderinfoDAO; 
	@Autowired
	private ITxnsLogService txnsLogService;
	@Autowired
	private CoopInstiService coopInstiService;
	@Autowired
	private MerchMKService merchMKService;
	/**
	 *
	 * @param tn
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public JSONObject creatOrder(String tn) {
		//获取交易订单信息
		TxnsOrderinfoModel order = txnsOrderinfoDAO.getOrderByTN(tn);
		//获取交易流水数据
		TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(order.getRelatetradetxn());
		
		
		//更新支付方信息
		PayPartyBean payPartyBean = new PayPartyBean(txnsLog.getTxnseqno(), 
				"05", 
				OrderNumber.getInstance().generateWeChatOrderNO(),//payordno, 
				ChannelEnmu.WEBCHAT.getChnlcode(), 
				WXConfigure.getMchid(), 
				"", 
				DateUtil.getCurrentDateTime(), 
				"", 
				"");
		txnsLogService.updatePayInfo_Fast(payPartyBean);
		//创建微信订单并提交
		WXApplication instance = new WXApplication();
		WXOrderBean order_wechat = new WXOrderBean();
		order_wechat.setBody(StringUtil.isEmpty(order.getGoodsname())?"默认商品":order.getGoodsname());
		order_wechat.setDetail(StringUtil.isEmpty(order.getOrderdesc())?"证联支付默认订单":order.getOrderdesc());
		order_wechat.setAttach("证联");
		order_wechat.setOut_trade_no(payPartyBean.getPayordno());
		order_wechat.setTotal_fee(txnsLog.getAmount()+"");
		order_wechat.setTime_start(DateUtil.getCurrentDateTime());
		order_wechat.setTime_expire(DateUtil.formatDateTime("yyyyMMddHHmmss",
				DateUtil.skipDateTime(new Date(), 1)));
		order_wechat.setGoods_tag("WXG");
		order_wechat.setNotify_url(ConsUtil.getInstance().cons.getWechat_notify_url());
		// 下订单
		String payrettsnseqno = null;
		JSONObject json = null;
		try {
			json = instance.createOrder(order_wechat);
			payrettsnseqno = json.get("prepayid")+"";
			txnsLog = txnsLogService.getTxnsLogByTxnseqno(order.getRelatetradetxn());
			txnsLog.setPayrettsnseqno(payrettsnseqno);
			txnsLogService.update(txnsLog);
		} catch (WXVerifySignFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void asyncTradeResult(PayResultBean result){
		//支付订单号
		String payOrderNo = result.getOut_trade_no();
		//交易结果
		ResultCodeEnum resultCodeEnum = ResultCodeEnum.fromValue(result.getResult_code());
		//交易流水数据
		TxnsLogModel txnsLog = txnsLogService.getTxnsLogByPayOrderNo(payOrderNo);
		//交易订单数据
		TxnsOrderinfoModel order = txnsOrderinfoDAO.getOrderByTxnseqno(txnsLog.getTxnseqno());
		if(resultCodeEnum==ResultCodeEnum.SUCCESS){//交易成功
			txnsLog.setPayrettsnseqno(result.getTransaction_id());
			txnsLog.setPayretcode(resultCodeEnum.getCode());
			txnsLog.setPayretinfo("交易成功");
			txnsLog.setTradestatflag("00000001");//交易完成结束位
		    txnsLog.setTradetxnflag("10000000");
		    txnsLog.setRelate("10000000");
		    txnsLog.setRetdatetime(DateUtil.getCurrentDateTime());
		    txnsLog.setTradeseltxn(UUIDUtil.uuid());
		    txnsLog.setRetcode("0000");
		    txnsLog.setRetinfo("交易成功");
			order.setStatus(OrderStatusEnum.SUCCESS.getStatus());
		}else if(resultCodeEnum==ResultCodeEnum.FAIL){//交易失败
			txnsLog.setPayretcode(result.getErr_code());
			txnsLog.setPayretinfo(result.getErr_code_des());
			order.setStatus(OrderStatusEnum.FAILED.getStatus());
		}
		txnsLog.setPayordfintime(DateUtil.getCurrentDateTime());
        txnsLog.setRetdatetime(DateUtil.getCurrentDateTime());
		//更新支付方信息
		txnsLogService.updateTxnsLog(txnsLog);
		//更新交易订单信息
		txnsOrderinfoDAO.updateOrderinfo(order);
		
		if(resultCodeEnum==ResultCodeEnum.FAIL){
			return;
		}
		//处理账务
		/**账务处理开始 **/
        // 应用方信息
        try {
            AppPartyBean appParty = new AppPartyBean("",
                    "000000000000", DateUtil.getCurrentDateTime(),
                    DateUtil.getCurrentDateTime(), txnsLog.getTxnseqno(), "AC000000");
            txnsLogService.updateAppInfo(appParty);
            IAccounting accounting = AccountingAdapterFactory.getInstance().getAccounting(BusiTypeEnum.fromValue(txnsLog.getBusitype()));
            ResultBean accountResultBean = accounting.accountedFor(txnsLog.getTxnseqno());
            txnsLogService.updateAppStatus(txnsLog.getTxnseqno(), accountResultBean.getErrCode(), accountResultBean.getErrMsg());
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /**账务处理结束 **/
       
        /**异步通知处理开始 **/
        ResultBean orderResp = 
                generateAsyncRespMessage(txnsLog.getTxnseqno());
        if (orderResp.isResultBool()) {
        	if("000205".equals(order.getBiztype())){
        		AnonOrderAsynRespBean respBean = (AnonOrderAsynRespBean) orderResp
                        .getResultObj();
                new SynHttpRequestThread(
                		order.getFirmemberno(),
                		order.getRelatetradetxn(),
                		order.getBackurl(),
                        respBean.getNotifyParam()).start();
        	}else{
        		OrderAsynRespBean respBean = (OrderAsynRespBean) orderResp
                        .getResultObj();
                new SynHttpRequestThread(
                		order.getFirmemberno(),
                		order.getRelatetradetxn(),
                		order.getBackurl(),
                        respBean.getNotifyParam()).start();
        	}
            
        }
        /**异步通知处理结束 **/
	}

	public ResultBean generateAsyncRespMessage(String txnseqno){
        ResultBean resultBean = null;
        try {
            TxnsOrderinfoModel orderinfo = txnsOrderinfoDAO.getOrderByTxnseqno(txnseqno);
            if(StringUtil.isEmpty(orderinfo.getBackurl())){
            	return new ResultBean("09", "no need async");
            }else {
				if(orderinfo.getBackurl().indexOf("http")<0){
					return new ResultBean("09", "no need async");
				}
			}
            
            
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
             String privateKey= "";
             if("000204".equals(orderinfo.getBiztype())){
            	 privateKey = coopInstiService.getCoopInstiMK(orderinfo.getFirmemberno(), TerminalAccessType.WIRELESS).getZplatformPriKey();
             }else if("000201".equals(orderinfo.getBiztype())||"000205".equals(orderinfo.getBiztype())){
            	 if("0".equals(orderinfo.getAccesstype())){
                 	privateKey = merchMKService.get(orderinfo.getSecmemberno()).getLocalPriKey().trim();
                 }else if("2".equals(orderinfo.getAccesstype())){
                 	privateKey = coopInstiService.getCoopInstiMK(orderinfo.getFirmemberno(), TerminalAccessType.MERPORTAL).getZplatformPriKey();
                 }else if("1".equals(orderinfo.getAccesstype())){
                	 privateKey = merchMKService.get(orderinfo.getSecmemberno()).getLocalPriKey().trim();
                 }
            	 //privateKey = merchMKService.get(orderinfo.getFirmemberno()).getLocalPriKey();
             }
             
             if("000205".equals(orderinfo.getBiztype())){
            	 AnonOrderAsynRespBean asynRespBean = new AnonOrderAsynRespBean(version, encoding, txnType, txnSubType, bizType, "00", orderinfo.getSecmembername(), orderId, txnTime, orderinfo.getPaytimeout(), txnAmt, settleCurrencyCode, orderinfo.getOrderdesc(), reserved, orderinfo.getStatus(), orderinfo.getTn(), respCode, respMsg);
            	 return new ResultBean(generateAsyncOrderResult(asynRespBean, privateKey.trim()));
             }
            
            resultBean = new ResultBean(generateAsyncOrderResult(orderRespBean, privateKey.trim()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            resultBean = new ResultBean("RC99", "系统异常");
        }
        return resultBean;
    }
	public OrderAsynRespBean generateAsyncOrderResult(OrderAsynRespBean orderAsyncRespBean,String privateKey) throws Exception{   
        String[] unParamstring = {"signature"};
        String dataMsg = ObjectDynamic.generateParamer(orderAsyncRespBean, false, unParamstring).trim();
        byte[] data =  URLEncoder.encode(dataMsg,"utf-8").getBytes();
        orderAsyncRespBean.setSignature(URLEncoder.encode(RSAUtils.sign(data, privateKey),"utf-8"));
        return orderAsyncRespBean;
    }

	public AnonOrderAsynRespBean generateAsyncOrderResult(AnonOrderAsynRespBean orderAsyncRespBean,String privateKey) throws Exception{   
        String[] unParamstring = {"signature"};
        String dataMsg = ObjectDynamic.generateParamer(orderAsyncRespBean, false, unParamstring).trim();
        byte[] data =  URLEncoder.encode(dataMsg,"utf-8").getBytes();
        //orderAsyncRespBean.setSignature(URLEncoder.encode(RSAUtils.sign(data, privateKey),"utf-8"));
        return orderAsyncRespBean;
    }
	/**
	 *
	 * @param queryBillBean
	 * @return
	 */
	@Override
	public List<String[]> dowanWeChatBill(QueryBillBean queryBillBean) {
		WXApplication ap = new WXApplication();
        PrintBean printBean = ap.downLoadBill(queryBillBean);
		return printBean.getContent();
	}
}
