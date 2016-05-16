/* 
 * ITradeReceiveProcessor.java  
 * 
 * version TODO
 *
 * 2015年11月17日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.enums.TradeTypeEnum;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年11月17日 下午2:27:10
 * @since 
 */
public interface ITradeReceiveProcessor {
    public void onReceive(ResultBean resultBean,TradeBean tradeBean,TradeTypeEnum tradeType);
    public ResultBean generateAsyncRespMessage(String orderNo,String memberId);
}
