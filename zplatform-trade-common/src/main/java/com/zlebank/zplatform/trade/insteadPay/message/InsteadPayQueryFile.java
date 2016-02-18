/* 
 * InsteadPayFile.java  
 * 
 * version TODO
 *
 * 2015年11月24日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.insteadPay.message;

/**
 * 代付查询文件
 *
 * @author Luxiaoshuai
 * @version
 * @date 2015年11月24日 下午3:02:08
 * @since 
 */
public class InsteadPayQueryFile extends InsteadPayFile{

    /**响应码**/
    private String respCode;
    /**应答信息**/
    private String respMsg;
    public String getRespCode() {
        return respCode;
    }
    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }
    public String getRespMsg() {
        return respMsg;
    }
    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
    
}
