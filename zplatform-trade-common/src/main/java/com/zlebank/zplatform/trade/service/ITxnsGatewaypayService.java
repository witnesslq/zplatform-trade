/* 
 * ITxnsGatewaypayService.java  
 * 
 * version TODO
 *
 * 2015年9月7日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.chanpay.ChanPayOrderBean;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.TxnsGatewaypayModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年9月7日 下午7:16:14
 * @since 
 */
public interface ITxnsGatewaypayService extends IBaseService<TxnsGatewaypayModel, Long>{
    public ResultBean saveGateWay(TxnsGatewaypayModel txnsGateway);
    public ResultBean updateGateWay(TxnsGatewaypayModel txnsGateway);
    public TxnsGatewaypayModel getOrderByOrderNo(String orderNo);
    
    
    public void saveChanPayGateWay(ChanPayOrderBean orderBean) throws TradeException;
   
}
