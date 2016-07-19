/* 
 * RoutBean.java  
 * 
 * version TODO
 *
 * 2015年9月23日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bean;

import com.zlebank.zplatform.trade.model.RouteProcessModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年9月23日 上午9:47:25
 * @since 
 */
public class RoutBean {
    /**
     * 渠道代码
     */
    private String chnlcode;
    /**
     * 交易代码-当前
     */
    private String txncode_current;
    /**
     * 交易代码-下一步
     */
    private String txncode_next;
    
    /**
     * @return the chnlcode
     */
    public String getChnlcode() {
        return chnlcode;
    }
    /**
     * @param chnlcode the chnlcode to set
     */
    public void setChnlcode(String chnlcode) {
        this.chnlcode = chnlcode;
    }
    
    /**
     * @return the txncode_current
     */
    public String getTxncode_current() {
        return txncode_current;
    }
    /**
     * @param txncode_current the txncode_current to set
     */
    public void setTxncode_current(String txncode_current) {
        this.txncode_current = txncode_current;
    }
    /**
     * @return the txncode_next
     */
    public String getTxncode_next() {
        return txncode_next;
    }
    /**
     * @param txncode_next the txncode_next to set
     */
    public void setTxncode_next(String txncode_next) {
        this.txncode_next = txncode_next;
    }
    
    
    /**
     * @param chnlcode
     * @param txncode_current
     * @param txncode_next
     */
    public RoutBean(String chnlcode, String txncode_current, String txncode_next) {
        super();
        this.chnlcode = chnlcode;
        this.txncode_current = txncode_current;
        this.txncode_next = txncode_next;
    }
    public RoutBean(RouteProcessModel routeProcess) {
        super();
        this.chnlcode = routeProcess.getChnlcode();
        this.txncode_current = routeProcess.getNowstep();
        this.txncode_next = routeProcess.getNextstep();
    }
    
}
