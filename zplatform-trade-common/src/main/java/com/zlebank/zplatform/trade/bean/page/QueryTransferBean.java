package com.zlebank.zplatform.trade.bean.page;

import java.io.Serializable;

public class QueryTransferBean implements Serializable{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8870101289238446680L;
    /**划拨批次ID*/
    public long tid;
    /**划拨批次号*/
    public String batchNo;
    /**开始时间*/
    public String beginDate;
    /**结束时间*/
    public String endDate;
    /*** 划拨状态*/
    public String status;
    
    public long getTid() {
        return tid;
    }
    public void setTid(long tid) {
        this.tid = tid;
    }
    public String getBatchNo() {
        return batchNo;
    }
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    public String getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}

