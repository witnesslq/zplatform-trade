/* 
 * FTPException.java  
 * 
 * version TODO
 *
 * 2015年11月10日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.cmbc.exception;

import com.zlebank.zplatform.commons.exception.AbstractDescribeException;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年11月10日 下午12:13:52
 * @since 
 */
public class FTPException extends AbstractDescribeException{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3491502227581371748L;
    
    private String code;
    public FTPException(String code,Object ...param) {
        this.code=code;
        this.setParams(param);
    }
    public FTPException(String code) {
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
