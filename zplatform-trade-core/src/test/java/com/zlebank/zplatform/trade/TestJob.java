/* 
 * TestWX.java  
 * 
 * version TODO
 *
 * 2016年5月17日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.member.service.CoopInstiService;
import com.zlebank.zplatform.member.service.MerchMKService;
import com.zlebank.zplatform.trade.adapter.quickpay.IRefundTrade;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.dao.ITxnsOrderinfoDAO;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.ITxnsRefundService;
import com.zlebank.zplatform.wechat.job.RefundJob;
import com.zlebank.zplatform.wechat.refund.WeChatRefundTrade;
import com.zlebank.zplatform.wechat.service.WeChatService;
import com.zlebank.zplatform.wechat.wx.WXApplication;
import com.zlebank.zplatform.wechat.wx.bean.PayResultBean;

/**
 * 微信测试类
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年5月17日 下午3:08:19
 * @since 
 */
public class TestJob {

    private static final Log log = LogFactory.getLog(TestJob.class);
    private RefundJob refundJob;
    private IRefundTrade weChatRefundTrade;
    private WeChatService weChatService;

    @Before
    public void init(){
    	  ApplicationContext context = ApplicationContextUtil.get();
        refundJob = (RefundJob) context.getBean("refundJob");
        weChatRefundTrade = (IRefundTrade) context.getBean("weChatRefundTrade");
        weChatService = (WeChatService) context.getBean("weChatService");
    }
    @Test
    public void test(){
    	try {
    		refundJob.doRefundQuery();
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    }
    @Test
    public void testrefund(){
    	try {
    		TradeBean tradeBean= new TradeBean();
        	//tradeBean.setTxnseqno("1606249900053699");
    		//tradeBean.setTxnseqno("1606279900053739");
    		tradeBean.setTxnseqno("1606279900053799");
        	ResultBean resultBean=weChatRefundTrade.refund(tradeBean);
        	if(resultBean.isResultBool()){
        		System.out.println("成功");
        	}else{
        		System.out.println(resultBean.getErrCode()+""+resultBean.getErrMsg());
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
    }
    
    @Test
    public void testAnsy(){
    	 StringBuffer sq= new StringBuffer();
	    	sq.append("<xml><appid><![CDATA[wx16a0b09dbf94f380]]></appid>");
	    	sq.append("<attach><![CDATA[证联]]></attach>");
	    	sq.append("<bank_type><![CDATA[CFT]]></bank_type>");
	    	sq.append("<cash_fee><![CDATA[1]]></cash_fee>");
	    	sq.append("<device_info><![CDATA[WEB]]></device_info>");
	     	sq.append("<fee_type><![CDATA[CNY]]></fee_type>");
	     	sq.append("<is_subscribe><![CDATA[N]]></is_subscribe>");
	     	sq.append("<mch_id><![CDATA[1345867901]]></mch_id>");
	     	sq.append("<nonce_str><![CDATA[815D0DECE9746E75A1FDA8D411D3AD2D]]></nonce_str>");
	     	sq.append("<openid><![CDATA[omBzYwICDExiIz1-ejI3v86oUsGU]]></openid>");
	     	sq.append("<out_trade_no><![CDATA[1606279000000363]]></out_trade_no>");
	     	sq.append("<result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code>");
	     	sq.append("<sign><![CDATA[60F0803BDBC0ED717C464425BB9154C9]]></sign>");
	     	sq.append("<time_end><![CDATA[20160627110422]]></time_end><total_fee>1</total_fee>");
	     	sq.append("<trade_type><![CDATA[APP]]></trade_type>");
	     	sq.append("<transaction_id><![CDATA[4003202001201606277955896260]]></transaction_id></xml>");
	     	try {
	     		WXApplication bean = new WXApplication();
	     		PayResultBean result =bean.parseResultXml(sq.toString());
	     		System.out.println(result.getTransaction_id());
	     		weChatService.asyncTradeResult(result);
	            // 返回响应数据
	            String responseXml = null;
	            if ("SUCCESS".equals(result.getReturn_code()) && "SUCCESS".equals(result.getResult_code())) {
	                responseXml = bean.createResponseResultXml("SUCCESS","OK");
	            } else {
	                responseXml = bean.createResponseResultXml("FAIL", result.getReturn_code() == null ? result.getResult_code() : result.getReturn_code());
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}
         
         
    }
    
}