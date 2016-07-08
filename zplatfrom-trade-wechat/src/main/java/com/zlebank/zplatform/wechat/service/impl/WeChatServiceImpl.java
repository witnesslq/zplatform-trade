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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.commons.dao.pojo.BusiTypeEnum;
import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.commons.utils.RSAUtils;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.commons.utils.security.AESHelper;
import com.zlebank.zplatform.commons.utils.security.AESUtil;
import com.zlebank.zplatform.commons.utils.security.RSAHelper;
import com.zlebank.zplatform.member.bean.MerchMK;
import com.zlebank.zplatform.member.bean.enums.TerminalAccessType;
import com.zlebank.zplatform.member.service.CoopInstiService;
import com.zlebank.zplatform.member.service.MerchMKService;
import com.zlebank.zplatform.trade.adapter.accounting.IAccounting;
import com.zlebank.zplatform.trade.bean.AppPartyBean;
import com.zlebank.zplatform.trade.bean.PayPartyBean;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.enums.ChannelEnmu;
import com.zlebank.zplatform.trade.bean.enums.ChnlTypeEnum;
import com.zlebank.zplatform.trade.bean.enums.OrderStatusEnum;
import com.zlebank.zplatform.trade.bean.gateway.AnonOrderAsynRespBean;
import com.zlebank.zplatform.trade.bean.gateway.OrderAsynRespBean;
import com.zlebank.zplatform.trade.dao.ITxnsOrderinfoDAO;
import com.zlebank.zplatform.trade.dao.RspmsgDAO;
import com.zlebank.zplatform.trade.factory.AccountingAdapterFactory;
import com.zlebank.zplatform.trade.model.PojoRspmsg;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;
import com.zlebank.zplatform.trade.model.TxnsRefundModel;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.ITxnsRefundService;
import com.zlebank.zplatform.trade.service.impl.InsteadPayNotifyTask;
import com.zlebank.zplatform.trade.service.impl.TxnsRefundServiceImpl;
import com.zlebank.zplatform.trade.utils.ConsUtil;
import com.zlebank.zplatform.trade.utils.ObjectDynamic;
import com.zlebank.zplatform.trade.utils.OrderNumber;
import com.zlebank.zplatform.trade.utils.SynHttpRequestThread;
import com.zlebank.zplatform.trade.utils.UUIDUtil;
import com.zlebank.zplatform.wechat.enums.ResultCodeEnum;
import com.zlebank.zplatform.wechat.enums.TradeStateCodeEnum;
import com.zlebank.zplatform.wechat.exception.WXVerifySignFailedException;
import com.zlebank.zplatform.wechat.service.WeChatService;
import com.zlebank.zplatform.wechat.wx.PrintBean;
import com.zlebank.zplatform.wechat.wx.WXApplication;
import com.zlebank.zplatform.wechat.wx.bean.PayResultBean;
import com.zlebank.zplatform.wechat.wx.bean.QueryBillBean;
import com.zlebank.zplatform.wechat.wx.bean.QueryOrderBean;
import com.zlebank.zplatform.wechat.wx.bean.QueryOrderResultBean;
import com.zlebank.zplatform.wechat.wx.bean.QueryRefundBean;
import com.zlebank.zplatform.wechat.wx.bean.QueryRefundResultBean;
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
	private static final Log log = LogFactory.getLog(WeChatServiceImpl.class);
    
	@Autowired
	private ITxnsOrderinfoDAO txnsOrderinfoDAO; 
	@Autowired
	private ITxnsLogService txnsLogService;
	@Autowired
	private CoopInstiService coopInstiService;
	@Autowired
	private MerchMKService merchMKService;
	@Autowired
	private AccEntryService accEntryService;
	@Autowired
	private ITxnsRefundService txnsRefundService;
	@Autowired
	private RspmsgDAO rspmsgDAO;
	
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
		txnsLogService.updatePayInfo_Fast(payPartyBean);		//创建微信订单并提交
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
                    DateUtil.getCurrentDateTime(), txnsLog.getTxnseqno(), "");
            txnsLogService.updateAppInfo(appParty);
            IAccounting accounting = AccountingAdapterFactory.getInstance().getAccounting(BusiTypeEnum.fromValue(txnsLog.getBusitype()));
            ResultBean accountResultBean = accounting.accountedFor(txnsLog.getTxnseqno());
            txnsLogService.updateAppStatus(txnsLog.getTxnseqno(), accountResultBean.getErrCode(), accountResultBean.getErrMsg());
            
        } catch (Exception e) {
           log.error(e.getMessage());
           e.printStackTrace();
        }
        /**账务处理结束 **/
        ResultBean orderResp = 
		        generateAsyncRespMessage(txnsLog.getTxnseqno());
		if (orderResp.isResultBool()) {
			if("000205".equals(order.getBiztype())){
        		AnonOrderAsynRespBean respBean = (AnonOrderAsynRespBean) orderResp.getResultObj();
        		
        		InsteadPayNotifyTask task = new InsteadPayNotifyTask();
        		//对匿名支付订单数据进行加密加签
        		responseData(respBean, txnsLog.getAccfirmerno(), txnsLog.getAccsecmerno(), task);
        		new SynHttpRequestThread(
                        StringUtil.isNotEmpty(order.getSecmemberno())?order.getSecmemberno():order.getFirmemberno(),
                        		order.getRelatetradetxn(),
                        		order.getBackurl(),
                        task).start();
        	}else{
        		OrderAsynRespBean respBean = (OrderAsynRespBean) orderResp
                        .getResultObj();
                new SynHttpRequestThread(
                		StringUtil.isNotEmpty(order.getSecmemberno())?order.getSecmemberno():order.getFirmemberno(),
                				order.getRelatetradetxn(),
                				order.getBackurl(),
                        respBean.getNotifyParam()).start();
        	}
		   
		}
      
        /**异步通知处理结束 **/
	}

	@SuppressWarnings("unchecked")
	private void responseData(AnonOrderAsynRespBean respBean, String coopInstCode,String merchNo,InsteadPayNotifyTask task) {
        if (log.isDebugEnabled()) {
            log.debug("【入参responseData】"+JSONObject.fromObject(respBean));
        }
        JSONObject jsonData = JSONObject.fromObject(respBean);
        // 排序
        Map<String, Object> map = new TreeMap<String, Object>();
        map =(Map<String, Object>) JSONObject.toBean(jsonData, TreeMap.class);
        jsonData = JSONObject.fromObject(map);
        
        JSONObject addit = new JSONObject();
        addit.put("accessType", "1");
        addit.put("coopInstiId", coopInstCode);
        addit.put("merId", merchNo);
        MerchMK merchMk = merchMKService.get(addit.getString("merId"));
        RSAHelper rsa = new RSAHelper(merchMk.getMemberPubKey(), merchMk.getLocalPriKey());
        String aesKey = null;
        try {
            aesKey = AESUtil.getAESKey();
            if (log.isDebugEnabled()) {
                log.debug("【AES KEY】" + aesKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        addit.put("encryKey", rsa.encrypt(aesKey));
        addit.put("encryMethod", "01");

        // 加签名
        StringBuffer originData = new StringBuffer(addit.toString());//业务数据
        originData.append(jsonData.toString());// 附加数据
        if (log.isDebugEnabled()) {
            log.debug("【应答报文】加签用字符串：" + originData.toString());
        }
        // 加签
        String sign = rsa.sign(originData.toString());
        AESHelper packer = new AESHelper(aesKey);
        JSONObject rtnSign = new JSONObject();
        rtnSign.put("signature", sign);
        rtnSign.put("signMethod", "01");
        
        // 业务数据
        task.setData(packer.pack(jsonData.toString()));
        // 附加数据
        task.setAddit(addit.toString());
        // 签名数据
        task.setSign(rtnSign.toString());
        if (log.isDebugEnabled()) {
            log.debug("【发送报文数据】【业务数据】："+task.getData());
            log.debug("【发送报文数据】【附加数据】："+task.getAddit());
            log.debug("【发送报文数据】【签名数据】："+ task.getSign());
        }
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


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void dealRefundBatch() {
	    log.info("查询退款定时任务开始【dealRefundBatch】");
		//1.查询待处理的退款的订单
		List<?> txnlogs= this.txnsLogService.getRefundOrderInfo(null,20);
		JSONArray jsonArray = JSONArray.fromObject(txnlogs);
		if(txnlogs.size()>0){
			 for (int i = 0; i < jsonArray.size(); i++) {
				 JSONObject entity = jsonArray.getJSONObject(i);
				 String txnseqno = entity.get("TXNSEQNO")+"";
				TxnsLogModel  txnsLog=this.txnsLogService.getTxnsLogByTxnseqno(txnseqno);
				TxnsOrderinfoModel order = txnsOrderinfoDAO.getOrderByTxnseqno(txnseqno);
				//原始交易流水
				TxnsLogModel txnsLog_old = txnsLogService.getTxnsLogByTxnseqno(txnsLog.getTxnseqnoOg());
				//2.调微信服务端
				WXApplication instance = new WXApplication();
				QueryRefundBean rb = new QueryRefundBean();
				//商户订单号
				rb.setOut_trade_no(txnsLog_old.getPayordno());
				rb.setTransaction_id(txnsLog_old.getPayrettsnseqno());
				log.info("调微信【查询退款】入参："+rb.toString());
				QueryRefundResultBean refund = instance.refundQuery(rb);
				log.info("调微信【查询退款】出参："+(null==refund?"无返回值":refund.getReturn_code().toString()));
				//3.根据返回结果处理
				if(ResultCodeEnum.SUCCESS.getCode().equals(refund.getReturn_code())){
					//成功
					txnsLog.setPayretcode(ResultCodeEnum.SUCCESS.getCode());
					txnsLog.setPayretinfo("交易成功");
					txnsLog.setTradestatflag("00000001");//交易完成结束位
				    txnsLog.setTradetxnflag("10000000");
				    txnsLog.setRelate("10000000");
				    txnsLog.setRetdatetime(DateUtil.getCurrentDateTime());
				    txnsLog.setTradeseltxn(UUIDUtil.uuid());
				    txnsLog.setRetcode("0000");
				    txnsLog.setRetinfo("交易成功");
				    //订单状态为成功
					order.setStatus(OrderStatusEnum.SUCCESS.getStatus());
		            //退款成功
		            TxnsRefundModel refundEn = txnsRefundService.getRefundByTxnseqno(txnseqno);
		            refundEn.setStatus("00");
		            txnsRefundService.updateRefund(refundEn);
				//3.2如果失败
				}else if(ResultCodeEnum.FAIL.getCode().equals(refund.getReturn_code())){
					txnsLog.setPayretcode(refund.getErr_code());
					txnsLog.setPayretinfo(refund.getErr_code_des());
					//
					try {
						PojoRspmsg rspmsg = rspmsgDAO.getRspmsgByChnlCode(ChnlTypeEnum.WECHAT, refund.getErr_code());
						txnsLog.setRetcode(rspmsg.getWebrspcode());
					    txnsLog.setRetinfo(rspmsg.getRspinfo());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						txnsLog.setRetcode("3499");
					    txnsLog.setRetinfo("交易失败");
					}
					
					 //订单状态为失败
					 order.setStatus(OrderStatusEnum.FAILED.getStatus());
					 log.info("退款跑批:"+txnseqno+"退款失败");
				//3.3需重新发起
				}else if(ResultCodeEnum.NOTSURE.getCode().equals(refund.getReturn_code())){
					txnsLog.setPayretcode(refund.getErr_code());
					txnsLog.setPayretinfo(refund.getErr_code_des());
					try {
						PojoRspmsg rspmsg = rspmsgDAO.getRspmsgByChnlCode(ChnlTypeEnum.WECHAT, refund.getErr_code());
						txnsLog.setRetcode(rspmsg.getWebrspcode());
					    txnsLog.setRetinfo(rspmsg.getRspinfo());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						txnsLog.setRetcode("3499");
					    txnsLog.setRetinfo("交易失败");
					}
					 //订单状态为失败
					  order.setStatus(OrderStatusEnum.FAILED.getStatus());
					  log.info("退款跑批:"+txnseqno+"需商户重新发起");
			    //3.4 //订单处理中不处理
				}
				txnsLog.setPayordfintime(DateUtil.getCurrentDateTime());
		        txnsLog.setRetdatetime(DateUtil.getCurrentDateTime());
				//更新支付方信息
				txnsLogService.updateTxnsLog(txnsLog);
				//更新交易订单信息
				txnsOrderinfoDAO.updateOrderinfo(order);
		        //更新订单状态
				//txnsOrderinfoDAO.update(order);
				 /**账务处理开始 **/
		        // 应用方信息
		        try {
		        	 AppPartyBean appParty = new AppPartyBean("",
		                     "000000000000", DateUtil.getCurrentDateTime(),
		                     DateUtil.getCurrentDateTime(), txnsLog.getTxnseqno(), "");
		        	txnsLogService.updateAppInfo(appParty);
		            AccountingAdapterFactory.getInstance().getAccounting(BusiTypeEnum.fromValue(txnsLog.getBusitype())).accountedFor(txnseqno);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        /**账务处理结束 **/
				log.info("查询退款定时任务结束【dealRefundBatch】 ");
				
		      }
	
		}
		
	}


	

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public ResultBean queryWechatOrder(TradeBean trade) {
		ResultBean resultBean = null;
		//1.查询订单的状态，如果是待支付，消费订单，并且已生成微信预订单号
		//当交易流水为空
		if(StringUtil.isEmpty(trade.getTn())){
			resultBean=new ResultBean("", "交易流水号【tn】不能为空！");
			return resultBean;
		}
		//获取交易订单信息
		TxnsOrderinfoModel order = txnsOrderinfoDAO.getOrderByTN(trade.getTn());
		if(order==null){
			resultBean=new ResultBean("", "请检查交易流水号【tn】！");
			return resultBean;
		}
		//获取交易流水数据
		TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(order.getRelatetradetxn());
		if(txnsLog ==null){
			resultBean=new ResultBean("", "请检查交易流水号【tn】！");
			return resultBean;
		}
		
		//订单状态为成功或失败
		Boolean isStatus = order.getStatus().equals(OrderStatusEnum.SUCCESS.getStatus())
				||order.getStatus().equals(OrderStatusEnum.FAILED.getStatus())
				||order.getStatus().equals(OrderStatusEnum.INVALID.getStatus());
		//成功或失败，支付返回
		if(isStatus){
			resultBean = new ResultBean(order);
			return resultBean;
		}
		//是否微信渠道
		Boolean isChnl = txnsLog.getPayinst().equals(ChannelEnmu.WEBCHAT.getChnlcode());
		//是否微信类型
		Boolean isChnlType = txnsLog.getPaytype().equals("05");
		//是否为消费类型
		Boolean isBusiType = txnsLog.getBusitype().equals(BusiTypeEnum.consumption.getCode());
		if(isChnl&&isChnlType &&isBusiType){
			//2.需要调微信查询订单 调微信服务端
			WXApplication instance = new WXApplication();
			QueryOrderBean rb = new QueryOrderBean();
			//商户订单号
			rb.setOut_trade_no(txnsLog.getPayordno());
			rb.setTransaction_id(txnsLog.getPayrettsnseqno());
			log.info("调微信【查询订单状态】入参："+rb.toString());
			QueryOrderResultBean result = instance.queryOrder(rb);
			log.info("调微信【查询订单状态】出参："+(null==result?"无返回值":result.getReturn_code().toString()));	
			//3.通信成功
			if(ResultCodeEnum.SUCCESS.getCode().equals(result.getReturn_code())){
			     //返回状态为：支付成功，或 退款中
				if(result.getTrade_state().equals(TradeStateCodeEnum.SUCCESS.getCode())
				  ||result.getTrade_state().equals(TradeStateCodeEnum.REFUND.getCode())){
					txnsLog.setPayrettsnseqno(result.getTransaction_id()+"");
					txnsLog.setPayretcode(TradeStateCodeEnum.SUCCESS.getCode());
					txnsLog.setPayretinfo("交易成功");
					txnsLog.setTradestatflag("00000001");//交易完成结束位
				    txnsLog.setTradetxnflag("10000000");
				    txnsLog.setRelate("10000000");
				    txnsLog.setRetdatetime(DateUtil.getCurrentDateTime());
				    txnsLog.setTradeseltxn(UUIDUtil.uuid());
				    txnsLog.setRetcode("0000");
				    txnsLog.setRetinfo("交易成功");
					order.setStatus(OrderStatusEnum.SUCCESS.getStatus());
				//返回状态为：支付失败，或 已撤消	
			     }else if(result.getTrade_state().equals(TradeStateCodeEnum.PAYERROR.getCode())
			    		 ||result.getTrade_state().equals(TradeStateCodeEnum.REVOKED.getCode())){
			    	txnsLog.setPayretcode(result.getTrade_state());
					txnsLog.setPayretinfo(result.getTrade_state_desc());
					order.setStatus(OrderStatusEnum.FAILED.getStatus());
			     //返回状态为：支付中 
			     }else if(result.getTrade_state().equals(TradeStateCodeEnum.USERPAYING.getCode())){
			    	 order.setStatus(OrderStatusEnum.PAYING.getStatus());
			     }
			//3.2无业务报文
			}else if(ResultCodeEnum.FAIL.getCode().equals(result.getReturn_code())){
				resultBean = new ResultBean(result.getErr_code(), result.getErr_code_des());
				return resultBean;
			}
			txnsLog.setPayordfintime(DateUtil.getCurrentDateTime());
	        txnsLog.setRetdatetime(DateUtil.getCurrentDateTime());
			//更新支付方信息
			txnsLogService.updateTxnsLog(txnsLog);
			//更新交易订单信息
			txnsOrderinfoDAO.updateOrderinfo(order);
			if((result.getTrade_state().equals(TradeStateCodeEnum.SUCCESS.getCode())
		    		 ||result.getTrade_state().equals(TradeStateCodeEnum.REFUND.getCode()))){
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
		            log.error(e.getMessage());
		            e.printStackTrace();
		            resultBean = new ResultBean("", result.getErr_code_des());
					return resultBean;
		        }
		        /**账务处理结束 **/
				/**异步通知处理开始  **/
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
		}
		resultBean = new ResultBean(order);
		return resultBean;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void dealAnsyOrder() {
		log.info("定时任务微信订单查询开始：dealAnsyOrder start");
		Map<String,Object> map= new HashMap<String,Object>();
		//订单状态
		List<String> statList=new ArrayList<String>();
		statList.add(OrderStatusEnum.INITIAL.getStatus());
		statList.add(OrderStatusEnum.PAYING.getStatus());
		map.put("statList",statList);
		//微信类型
		map.put("paytype", "05");
		//微信渠道
		map.put("painst", ChannelEnmu.WEBCHAT.getChnlcode());
		//消费类型
		map.put("busitype", BusiTypeEnum.consumption.getCode());
		List<Object> txnlogs = this.txnsLogService.queryTxnsLog(map);
		for(Object item : txnlogs){
			String txnseqno = item.toString();
			TxnsLogModel txnsLog= this.txnsLogService.getTxnsLogByTxnseqno(txnseqno); 
           TxnsOrderinfoModel order= this.txnsOrderinfoDAO.getOrderByTxnseqno(txnsLog.getTxnseqno());
			//2.需要调微信查询订单 调微信服务端
			WXApplication instance = new WXApplication();
			QueryOrderBean rb = new QueryOrderBean();
			//商户订单号
			rb.setOut_trade_no(txnsLog.getPayordno());
			rb.setTransaction_id(txnsLog.getPayrettsnseqno());
			log.info("调微信【查询订单状态】入参：out_trade_no="+rb.getOut_trade_no()+",transaction_id"+rb.getTransaction_id());
			QueryOrderResultBean result = instance.queryOrder(rb);
			log.info("调微信【查询订单状态】出参："+(null==result?"无返回值":result.getReturn_code().toString()));	
			//3.通信成功
			if(ResultCodeEnum.SUCCESS.getCode().equals(result.getReturn_code())){
				log.info("微信返回订单状态"+result.getTrade_state()+":"+result.getTrade_state_desc());
			     //返回状态为：支付成功，或 退款中
				if(result.getTrade_state().equals(TradeStateCodeEnum.SUCCESS.getCode())
				  ||result.getTrade_state().equals(TradeStateCodeEnum.REFUND.getCode())){
					txnsLog.setPayrettsnseqno(result.getTransaction_id()+"");
					txnsLog.setPayretcode(TradeStateCodeEnum.SUCCESS.getCode());
					txnsLog.setPayretinfo("交易成功");
					txnsLog.setTradestatflag("00000001");//交易完成结束位
				    txnsLog.setTradetxnflag("10000000");
				    txnsLog.setRelate("10000000");
				    txnsLog.setRetdatetime(DateUtil.getCurrentDateTime());
				    txnsLog.setTradeseltxn(UUIDUtil.uuid());
				    txnsLog.setRetcode("0000");
				    txnsLog.setRetinfo("交易成功");
					order.setStatus(OrderStatusEnum.SUCCESS.getStatus());
				//返回状态为：支付失败，或 已撤消	
			     }else if(result.getTrade_state().equals(TradeStateCodeEnum.PAYERROR.getCode())
			    		 ||result.getTrade_state().equals(TradeStateCodeEnum.REVOKED.getCode())){
			    	txnsLog.setPayretcode(result.getTrade_state());
					txnsLog.setPayretinfo(result.getTrade_state_desc());
					order.setStatus(OrderStatusEnum.FAILED.getStatus());
			     //返回状态为：支付中 
			     }else if(result.getTrade_state().equals(TradeStateCodeEnum.USERPAYING.getCode())){
			    	 order.setStatus(OrderStatusEnum.PAYING.getStatus());
			     }
			//3.2无业务报文
			}else if(ResultCodeEnum.FAIL.getCode().equals(result.getReturn_code())){
				log.error("微信订单查询报错:"+result.getReturn_code()+result.getReturn_msg());
				continue;
			}
			txnsLog.setPayordfintime(DateUtil.getCurrentDateTime());
	        txnsLog.setRetdatetime(DateUtil.getCurrentDateTime());
			//更新支付方信息
			txnsLogService.updateTxnsLog(txnsLog);
			//更新交易订单信息
			txnsOrderinfoDAO.updateOrderinfo(order);
			if((result.getTrade_state().equals(TradeStateCodeEnum.SUCCESS.getCode())
		    		 ||result.getTrade_state().equals(TradeStateCodeEnum.REFUND.getCode()))){
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
		            log.error(e.getMessage());
		            break;
		        }
		        /**账务处理结束 **/
				//**异步通知处理开始  **/
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
				
			}
		
		log.info("定时任务微信订单查询结束：dealAnsyOrder end");
		
	}
	
	
	


	


	
	
	
}
