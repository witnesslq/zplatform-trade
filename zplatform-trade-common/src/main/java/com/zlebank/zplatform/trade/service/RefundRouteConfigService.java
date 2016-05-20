/* 
 * RefundRouteConfigService.java  
 * 
 * version TODO
 *
 * 2016年5月18日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.model.PojoRefundRouteConfigModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月18日 下午12:58:21
 * @since 
 */
public interface RefundRouteConfigService extends IBaseService<PojoRefundRouteConfigModel, Long>{

	/**
	 * 退款交易路由
	 * @param transTime 交易时间
	 * @param transAmt 交易金额
	 * @param merchNo 商户号
	 * @param busiCode 原业务代码
	 * @param cardNo 银行卡号
	 * @param tradeRout 原交易路由号
	 * @param isanonymity 匿名标记
	 * @return
	 */
	public ResultBean getTransRout(String transTime,String transAmt,String merchNo,String busiCode,String cardNo,String tradeRout,String isanonymity);
}
