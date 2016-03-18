/* 
 * AuditDataBean.java  
 * 
 * version TODO
 *
 * 2016年3月14日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bean.page;

/**
 * 审核数据BEAN
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月14日 上午9:18:46
 * @since 
 */
public class AuditDataBean {
    private  String detailId;
    private String batchId;
    private boolean pass;

    public String getBatchId() {
        return batchId;
    }
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
    public boolean getPass() {
        return pass;
    }
    public void setPass(boolean pass) {
        this.pass = pass;
    }
    public String getDetailId() {
        return detailId;
    }
    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
}
