/* 
 * InsteadPayNotifyTask.java  
 * 
 * version TODO
 *
 * 2016年3月30日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

/**
 * 代付通知任务
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月30日 下午4:38:22
 * @since 
 */
public class InsteadPayNotifyTask {

    private String data;
    private String sign;
    private String addit;
    
    private String url;

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
    public String getAddit() {
        return addit;
    }
    public void setAddit(String addit) {
        this.addit = addit;
    }
    
}
