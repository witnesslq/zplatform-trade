/* 
 * OrderBeanTest.java  
 * 
 * version TODO
 *
 * 2016年4月27日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatfrom.trade.chanpay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.trade.chanpay.bean.order.RefundOrderBean;
import com.zlebank.zplatform.trade.chanpay.bean.order.SingleOrderBean;
import com.zlebank.zplatform.trade.chanpay.bean.query.QueryBankBean;
import com.zlebank.zplatform.trade.chanpay.bean.query.QueryTradeBean;
import com.zlebank.zplatform.trade.chanpay.service.ChanPayService;
import com.zlebank.zplatform.trade.chanpay.utils.ChanPayUtil;
import com.zlebank.zplatform.trade.utils.ConsUtil;
import com.zlebank.zplatform.trade.utils.SpringContext;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年4月27日 下午3:56:34
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/*.xml")
public class OrderBeanTest {
	
	@Autowired
	private ChanPayService chanPayService;

	//@Test
	public void test_order_sign() throws Exception{
		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKHbwQ85YEd2IhKifQEzJNHAYDj29148+6lNg81mDOJ3s8F9xmThA8e7KFKYe0seUVal8+3VXe+XOHTKUgqhrmpRDc+DzRmK1QdeBFTliiryuG3ZIeaRITx9jLViEEPCXDbmSn5lNLeBjx5i1XBlpORQTrSAlXx7/R4axTp2lEMjAgMBAAECgYABlys6fxHXIe4LyNT5ogsGlKFdbe/YWTkP3NciuZH+17ZIfHfqndtvpwMqbJ0pi864z0CqYaJerFm9rA9KU3RnSwx0H9aPQAeTlW378pMy4+qLCq9YHCNHXemKKPW4KD1ExrBqsUl5raeZz4m1DNcPcuQtWr5/T7kFf0MxPsEEYQJBAMwskKFVQUVjSCQgyTvS61vYJb3/DN9iSmKNsZZZSmFze00c39HeOd2dq6R7FMmcA6hkYU/i7+UKAGO/ZyMjdwUCQQDK8XmZ2TN3M+NlyY6e7LpgPoJzOy4sbHWbbkpa3kgaYSyCcXFgY59gpu2FTWdRQgFFeuxD1k2x2TCuzGGoZ5oHAkAts/QUCQ95RsYJQEWLXKVOg82+/+6Tul7IPMt5yjb6JW1+T25SfhoZ34diZCK9Fm1DLmUSCsyESn7X1SpzFSc5AkEAj3wrfZsTyDPnkw/uxm6ZV3LayJ4PB1mnzT0tVRHT6NLLpW6Pupa1GKDtTlJrugfw3i8K3OuoAxaMVQosAeU+AQJATuPYAz1UPBhEVLlXANsSLixcHFOh3LjXhYsB9V8kwHH8BKn5enaALeyyHMEQ5HPa+hCRxu/gI4E7+TFjHjvfSQ==";
		
		SingleOrderBean orderBean = new SingleOrderBean();
		orderBean.setVersion(ConsUtil.getInstance().cons.getChanpay_version());
		orderBean.setPartner_id(ConsUtil.getInstance().cons.getChanpay_partner_id());
		orderBean.set_input_charset(ConsUtil.getInstance().cons.getChanpay_input_charset());
		orderBean.setIs_anonymous("Y");
		orderBean.setBank_code("ICBC");
		orderBean.setOut_trade_no((UUID.randomUUID().toString()).replace("-", ""));
		orderBean.setPay_method("1");
		orderBean.setPay_type("C,DC");
		orderBean.setService("cjt_create_instant_trade");
		orderBean.setTrade_amount("100.00");
		String msg = ChanPayUtil.generateParamer(orderBean, true, new String[]{"sign","sign_type"});
		//orderBean.setSign(RSA.sign(msg, privateKey, "utf-8"));
		//orderBean.setSign_type(ConsUtil.getInstance().cons.getChanpay_sign_type());
		System.out.println(JSON.toJSONString(orderBean));
		Map<String,Object> parseObject = JSON.parseObject(JSON.toJSONString(orderBean), Map.class);
		 List<String> keys = new ArrayList<String>(parseObject.keySet());
	        Collections.sort(keys);
	        String prestr = "";

	        //String charset = parseObject.get("_input_charset").toString();
	        for (int i = 0; i < keys.size(); i++) {
	            String key = keys.get(i);
	            String value = parseObject.get(key).toString();
	            

	            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
	                prestr = prestr + key + "=" + value;
	            } else {
	                prestr = prestr + key + "=" + value + "&";
	            }
	        }
		System.out.println("prestr:"+prestr);
		
		
	}
	
	@Test
	public void test_refund(){
		RefundOrderBean refundOrderBean = new RefundOrderBean();
		refundOrderBean.setVersion(ConsUtil.getInstance().cons.getChanpay_version());
		refundOrderBean.setPartner_id(ConsUtil.getInstance().cons.getChanpay_partner_id());
		refundOrderBean.set_input_charset(ConsUtil.getInstance().cons.getChanpay_input_charset());
		refundOrderBean.setOuter_trade_no((UUID.randomUUID().toString()).replace("-", ""));
		refundOrderBean.setOrig_outer_trade_no("151e2f0b7a5d489e95ddf328b04d285b");
		refundOrderBean.setService("cjt_create_refund");
		refundOrderBean.setNotify_url("http://192.168.101.209:8081/demo/ReciveNotifyServlet");
		refundOrderBean.setRefund_amount("100.00");
		chanPayService.refund(refundOrderBean);
	}
	
	
	public void test_query(){
		QueryTradeBean queryTradeBean = new QueryTradeBean();
		queryTradeBean.setVersion(ConsUtil.getInstance().cons.getChanpay_version());
		queryTradeBean.setPartner_id(ConsUtil.getInstance().cons.getChanpay_partner_id());
		queryTradeBean.set_input_charset(ConsUtil.getInstance().cons.getChanpay_input_charset());
		queryTradeBean.setOuter_trade_no("f9bcfd6db5ff45e98c5628629c6301fd");
		queryTradeBean.setTrade_type("INSTANT");
		queryTradeBean.setService("cjt_query_trade");
		chanPayService.queryTrade(queryTradeBean);
	}
	//@Test
	public void test_queryBank(){
		QueryBankBean queryTradeBean = new QueryBankBean();
		queryTradeBean.setVersion(ConsUtil.getInstance().cons.getChanpay_version());
		queryTradeBean.setPartner_id(ConsUtil.getInstance().cons.getChanpay_partner_id());
		queryTradeBean.set_input_charset(ConsUtil.getInstance().cons.getChanpay_input_charset());
		queryTradeBean.setService("cjt_get_paychannel");
		queryTradeBean.setProduct_code("20201");
		Object queryBank = chanPayService.queryBank(queryTradeBean);
		System.out.println(JSON.toJSON(queryBank));
	}
	
}
