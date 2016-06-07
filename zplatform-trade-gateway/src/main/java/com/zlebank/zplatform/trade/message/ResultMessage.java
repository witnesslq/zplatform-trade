/* 
 * ErrorMessage.java  
 * 
 * version TODO
 *
 * 2016年1月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.message;

import com.zlebank.zplatform.commons.utils.StringUtil;

/**
 * 结果报文
 * 正常情况下
 * errorCode=null && errorMessage=null
 * 异常情况下
 * errorCode<>null || errorMessage<>null
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年1月20日 下午1:57:14
 * @since 
 */
public class ResultMessage {
    private String errorCode;// 错误码
    private String errorMessage;// 错误信息
    
    /** 
     * 构造错误信息
     * @param errorCode
     * @param errorMessage
     */
    public ResultMessage(String errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    /**
     *  判断是否是错误信息
     * @return
     */
    public boolean isError() {
        return StringUtil.isNotEmpty(this.errorMessage) || StringUtil.isNotEmpty(this.errorCode) ? true : false; 
    }
    /**
     *  判断是否是正常信息
     * @return
     */
    public boolean isNormal() {
        return StringUtil.isEmpty(this.errorMessage) && StringUtil.isEmpty(this.errorCode) ? true : false; 
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
