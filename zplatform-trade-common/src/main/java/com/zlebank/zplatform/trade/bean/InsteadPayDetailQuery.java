/* 
 * InsteadPayDetailQuery.java  
 * 
 * version TODO
 *
 * 2015年12月21日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bean;

import java.util.List;

import com.zlebank.zplatform.commons.bean.Bean;

/**
 * 代付明细查询条件
 *
 * @author yangpeng
 * @version
 * @date 2015年12月21日 下午7:56:48
 * @since 
 */
public class InsteadPayDetailQuery implements Bean{
    /**一级商户代码**/
    private String merId;
    /**账号类型01:银行卡;02:存折**/
    private String accType;
    /**商户订单号**/
    private String orderId;
    /**账号**/
    private String accNo;
    /**状态**/
    private String  status;
    /**批次号**/
    private String batchFileNo;
    /** 批次ID **/
    private String batchId;
    /** 状态列表 **/
    private List<String> statusList;
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMerId() {
        return merId;
    }
    public void setMerId(String merId) {
        this.merId = merId;
    }
    public String getBatchFileNo() {
        return batchFileNo;
    }
    public void setBatchFileNo(String batchFileNo) {
        this.batchFileNo = batchFileNo;
    }
    public String getAccType() {
        return accType;
    }
    public void setAccType(String accType) {
        this.accType = accType;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getAccNo() {
        return accNo;
    }
    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }
    public String getBatchId() {
        return batchId;
    }
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
    public List<String> getStatusList() {
        return statusList;
    }
    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }
}
