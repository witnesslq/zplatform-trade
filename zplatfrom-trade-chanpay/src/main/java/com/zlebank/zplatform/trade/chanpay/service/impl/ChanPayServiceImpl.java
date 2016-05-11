/* 
 * ChanPayServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年4月29日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.chanpay.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.trade.chanpay.bean.ReturnMessageBean;
import com.zlebank.zplatform.trade.chanpay.bean.async.RefundAsyncResultBean;
import com.zlebank.zplatform.trade.chanpay.bean.async.TradeAsyncResultBean;
import com.zlebank.zplatform.trade.chanpay.bean.order.RefundOrderBean;
import com.zlebank.zplatform.trade.chanpay.bean.query.BankItemBean;
import com.zlebank.zplatform.trade.chanpay.bean.query.QueryBankBean;
import com.zlebank.zplatform.trade.chanpay.bean.query.QueryBankResultBean;
import com.zlebank.zplatform.trade.chanpay.bean.query.QueryTradeBean;
import com.zlebank.zplatform.trade.chanpay.service.ChanPayService;
import com.zlebank.zplatform.trade.chanpay.utils.ChanPayUtil;
import com.zlebank.zplatform.trade.chanpay.utils.HttpProtocolHandler;
import com.zlebank.zplatform.trade.chanpay.utils.HttpRequest;
import com.zlebank.zplatform.trade.chanpay.utils.HttpResponse;
import com.zlebank.zplatform.trade.chanpay.utils.HttpResultType;
import com.zlebank.zplatform.trade.chanpay.utils.RSA;
import com.zlebank.zplatform.trade.utils.ConsUtil;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年4月29日 下午1:23:37
 * @since
 */
@Service("chanPayService")
public class ChanPayServiceImpl implements ChanPayService {

	private static final Log log = LogFactory.getLog(ChanPayServiceImpl.class);
	/**
	 *
	 * @param refundOrderBean
	 * @return
	 */
	@Override
	public ReturnMessageBean refund(RefundOrderBean refundOrderBean) {
		// TODO Auto-generated method stub
		try {
			String sign = RSA.sign(buildParamter(refundOrderBean),
					ConsUtil.getInstance().cons.getChanpay_private_key(),
					ConsUtil.getInstance().cons.getChanpay_input_charset());
			refundOrderBean.setSign(sign);
			refundOrderBean.setSign_type(ConsUtil.getInstance().cons
					.getChanpay_sign_type());
			String resultString = buildRequest(refundOrderBean, "RSA", ConsUtil.getInstance().cons
					.getChanpay_input_charset(), ConsUtil.getInstance().cons
					.getChanpay_url());
			log.info("recevie message:"+resultString);
			return JSON.parseObject(resultString, ReturnMessageBean.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	
	public QueryTradeBean queryTrade(QueryTradeBean queryTradeBean){
		try {
			String sign = RSA.sign(buildParamter(queryTradeBean),
					ConsUtil.getInstance().cons.getChanpay_private_key(),
					ConsUtil.getInstance().cons.getChanpay_input_charset());
			queryTradeBean.setSign(sign);
			queryTradeBean.setSign_type(ConsUtil.getInstance().cons
					.getChanpay_sign_type());
			String resultString = buildRequest(queryTradeBean, "RSA", ConsUtil.getInstance().cons
					.getChanpay_input_charset(), ConsUtil.getInstance().cons
					.getChanpay_url());
			log.info("recevie message:"+resultString);
			return JSON.parseObject(resultString, QueryTradeBean.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<BankItemBean> queryBank(QueryBankBean queryBankBean){
		try {
			String sign = RSA.sign(buildParamter(queryBankBean),
					ConsUtil.getInstance().cons.getChanpay_private_key(),
					ConsUtil.getInstance().cons.getChanpay_input_charset());
			queryBankBean.setSign(sign);
			queryBankBean.setSign_type(ConsUtil.getInstance().cons
					.getChanpay_sign_type());
			String resultString = buildRequest(queryBankBean, "RSA", ConsUtil.getInstance().cons
					.getChanpay_input_charset(), ConsUtil.getInstance().cons
					.getChanpay_url());
			log.info("recevie message:"+resultString);
			 QueryBankResultBean resultBean = JSON.parseObject(resultString, QueryBankResultBean.class);
			 if("T".equals(resultBean.getIs_success())){
				 return JSON.parseArray(resultBean.getPay_inst_list(), BankItemBean.class);
			 }
			 return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean asyncNotifyTrade(TradeAsyncResultBean tradeAsyncResultBean){
		boolean testTrue = false;
		try {
			String msg = ChanPayUtil.generateParamer(tradeAsyncResultBean, true, new String[]{"sign","sign_type"});
			testTrue = RSA.verify(msg, tradeAsyncResultBean.getSign(), ConsUtil.getInstance().cons.getChanpay_public_key(), "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testTrue;
	}
	
	public boolean asyncNotifyRefund(RefundAsyncResultBean refundAsyncResultBean){
		boolean flag = false;
		try {
			String msg = ChanPayUtil.generateParamer(refundAsyncResultBean, true, new String[]{"sign","sign_type"});
			flag = RSA.verify(msg, refundAsyncResultBean.getSign(), ConsUtil.getInstance().cons.getChanpay_public_key(), "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return flag;
	}
	

	private String buildParamter(Object orderBean) {
		Map<String, Object> parseObject = JSON.parseObject(JSON.toJSONString(orderBean), Map.class);
		List<String> keys = new ArrayList<String>(parseObject.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = parseObject.get(key).toString();

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		System.out.println("prestr:" + prestr);
		return prestr;
	}

	public static String buildRequest(Object order,
			String signType, String inputCharset, String gatewayUrl)
			throws Exception {
		// 待请求参数数组
		Map<String, String> sPara = JSON.parseObject(JSON.toJSONString(order), Map.class);

		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler
				.getInstance();

		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		// 设置编码集
		request.setCharset(inputCharset);

		request.setMethod(HttpRequest.METHOD_POST);

		request.setParameters(generatNameValuePair(sPara, inputCharset));
		request.setUrl(gatewayUrl);

		HttpResponse response = httpProtocolHandler
				.execute(request, null, null);
		if (response == null) {
			return null;
		}

		String strResult = response.getStringResult();

		return strResult;
	}
	private static NameValuePair[] generatNameValuePair(Map<String, String> properties, String charset) throws Exception{
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
//            nameValuePair[i++] = new NameValuePair(entry.getKey(), URLEncoder.encode(entry.getValue(),charset));
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        return nameValuePair;
    }
}
