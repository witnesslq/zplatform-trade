/* 
 * InsteadPayInterfaceParamBean.java  
 * 
 * version TODO
 *
 * 2016年3月15日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bean;

/**
 * Class Description
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月15日 下午5:31:50
 * @since 
 */
public class InsteadPayInterfaceParamBean {
    private long userId;
    private String ftpFileName;
    private String originalFileName;
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getFtpFileName() {
        return ftpFileName;
    }
    public void setFtpFileName(String ftpFileName) {
        this.ftpFileName = ftpFileName;
    }
    public String getOriginalFileName() {
        return originalFileName;
    }
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
}
