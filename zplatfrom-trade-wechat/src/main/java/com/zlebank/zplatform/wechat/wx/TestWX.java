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
package com.zlebank.zplatform.wechat.wx;

import java.util.Date;

import net.sf.json.JSONObject;

import com.zlebank.zplatform.trade.utils.DateUtil;
import com.zlebank.zplatform.wechat.exception.WXVerifySignFailedException;
import com.zlebank.zplatform.wechat.wx.bean.WXOrderBean;

/**
 * Class Description
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年5月17日 下午3:08:19
 * @since
 */
public class TestWX {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WXApplication instance = new WXApplication();
		WXOrderBean order = new WXOrderBean();
		order.setBody("iPad");
		order.setDetail("iPad mini  16G  白色");
		order.setAttach("北京太阳宫");
		order.setOut_trade_no("ZL" + DateUtil.getCurrentDateTime());
		order.setTotal_fee("1");
		order.setTime_start(DateUtil.getCurrentDateTime());
		order.setTime_expire(DateUtil.formatDateTime("yyyyMMddHHmmss",
				DateUtil.skipDateTime(new Date(), 1)));
		order.setGoods_tag("WXG");
		order.setNotify_url("http://www.weixin.qq.com/wxpay/pay.php");
		// 下订单
		try {
			JSONObject xml = instance.createOrder(order);
			/*String rtnData = instance.sendXMLData(xml);
			WXOrderResponse res = instance.parseXml(rtnData);
			System.out.println(JSONObject.fromObject(res).toString(3));*/
		} catch (WXVerifySignFailedException e) {
			e.printStackTrace();
		}
		/*String rtnData = instance.sendXMLData(xml);
		WXOrderResponse res = instance.parseXml(rtnData);
		System.out.println(JSONObject.fromObject(res).toString(3));*/
	}

}
