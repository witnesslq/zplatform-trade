/* 
 * NotInsteadWorkTimeException.java  
 * 
 * version v1.0
 *
 * 2015年11月25日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.exception;

import com.zlebank.zplatform.commons.exception.AbstractDescribeException;

/**
 * 商户白名单检查失败
 *
 * @author Luxiaoshuai
 * @version
 * @date 2015年11月25日 下午2:10:30
 * @since 
 */
public class MerchWhiteListCheckFailException extends AbstractDescribeException {

    public MerchWhiteListCheckFailException(Object... params) {
        this.params = params;
    }
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6063350038657235076L;

    /**
     *
     * @return
     */
    @Override
    public String getCode() {
        return "ETSIP0009";
    }
}
