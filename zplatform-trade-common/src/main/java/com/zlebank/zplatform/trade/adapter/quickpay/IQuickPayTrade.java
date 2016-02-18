/* 
 * IQuickPayTrade.java  
 * 
 * version TODO
 *
 * 2015年9月6日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.adapter.quickpay;

import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.enums.TradeTypeEnum;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年9月6日 下午1:29:19
 * @since 
 */
public interface IQuickPayTrade extends Runnable{
    /**
     * 发送/重发短信验证码
     * @param trade
     * @return
     */
    ResultBean sendMarginSms(TradeBean trade);
    /**
     * 开户/银行卡签约
     * @param trade
     * @return
     */
    ResultBean marginRegister(TradeBean trade);
    /**
     * 在线入金（基金产品）
     * @param trade
     * @return
     */
    ResultBean onlineDepositShort(TradeBean trade);
    /**
     * 在线出金（基金产品）
     * @param trade
     * @return
     */
    ResultBean withdrawNotify(TradeBean trade);
    /**
     * 确认支付（第三方快捷支付渠道）
     * @return
     */
    ResultBean submitPay(TradeBean trade);
    
    /**
     * 交易查询
     * @param trade
     * @return
     */
    ResultBean queryTrade(TradeBean trade);
    
    /**
     * 银行卡签约
     * @param trade
     * @return
     */
    ResultBean bankSign(TradeBean trade);
    
    void setTradeType(TradeTypeEnum tradeType);
    
    void setTradeBean(TradeBean tradeBean);
}
