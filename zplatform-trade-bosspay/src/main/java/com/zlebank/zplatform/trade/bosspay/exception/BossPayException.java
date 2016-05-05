/* 
 * BossPayException.java  
 * 
 * version TODO
 *
 * 2016年4月6日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bosspay.exception;

import java.util.ResourceBundle;

import com.zlebank.zplatform.commons.exception.AbstractDescribeException;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年4月6日 上午9:14:58
 * @since 
 */
public class BossPayException extends AbstractDescribeException{
	/**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3716682104238215841L;
    private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("exception_des");
    
    public ResourceBundle getResourceBundle() {
        return RESOURCE;
    }
    private String code;
    public BossPayException(String code,Object ...param) {
        this.code=code;
        this.setParams(param);
    }
    public BossPayException(String code) {
        this.code=code;
    }
    /**
     *
     * @return
     */
    @Override
    public String getCode() {
        return code;
    }
}
