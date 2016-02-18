/* 
 * IZlTradeServic.java  
 * 
 * version TODO
 *
 * 2015年8月21日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.zlpay.MarginRegisterBean;
import com.zlebank.zplatform.trade.bean.zlpay.MarginSmsBean;
import com.zlebank.zplatform.trade.bean.zlpay.OfflineDepositNotifyBean;
import com.zlebank.zplatform.trade.bean.zlpay.OnlineDepositShortBean;
import com.zlebank.zplatform.trade.bean.zlpay.QuerytranstatusBean;
import com.zlebank.zplatform.trade.bean.zlpay.WithdrawNotifyBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年8月21日 上午11:27:23
 * @since 
 */
public interface IZlTradeService {
        /**
         * 获取短信验证码
         * @param marginSmsBean
         * @return
         */
        ResultBean sendMarginSms(MarginSmsBean marginSmsBean);
        /**
         * 同步开户
         * @param marginRegisterBean
         * @return
         */
        ResultBean marginRegisterReq(MarginRegisterBean marginRegisterBean);
        /**
         * 在线入金
         * @param onlineDepositShortBean
         * @return
         */
        ResultBean onlineDepositShort(OnlineDepositShortBean onlineDepositShortBean);
        /**
         * 线下入金
         * @param offlineDepositNotifyBean
         * @return
         */
        ResultBean offlineDepositNotifyReq(OfflineDepositNotifyBean offlineDepositNotifyBean);
        /**
         * 出金
         * @param withdrawNotifyBean
         * @return
         */
        ResultBean withdrawNotifyReq(WithdrawNotifyBean withdrawNotifyBean);
        /**
         * 交易查询
         * @param querytranstatusBean
         * @return
         */
        ResultBean querytranstatus(QuerytranstatusBean querytranstatusBean);
        
}
