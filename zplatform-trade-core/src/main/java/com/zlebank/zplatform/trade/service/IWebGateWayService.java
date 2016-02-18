/* 
 * IWebGateService.java  
 * 
 * version TODO
 *
 * 2015年11月17日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年11月17日 下午2:04:14
 * @since 
 */
public interface IWebGateWayService extends IBaseService<TxnsOrderinfoModel, Long>{
    public void submitPay(TradeBean tradeBean) throws TradeException;
    public void bindPay(TradeBean trade)throws TradeException;
    public void bankCardSign(TradeBean trade) throws TradeException;
    public void initMemeberPwd(String memberId,String pwd) throws TradeException;
    public String accountPay(TradeBean tradeBean) throws TradeException,Exception;
}
