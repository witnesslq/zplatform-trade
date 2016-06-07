/* 
 * ITradeService.java  
 * 
 * version TODO
 *
 * 2015年8月21日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年8月21日 上午9:24:21
 * @since 
 */
public interface ITradeService {
    
    /**
     * 充值
     * @return
     */
    public Object recharge();
    /**
     * 退款
     * @return
     */
    public Object refund();
    /**
     * 消费
     * @return
     */
    public Object consume();
    /**
     * 转账
     * @return
     */
    public Object transfer();
    /**
     * 提现
     * @return
     */
    public Object withdraw();
}
