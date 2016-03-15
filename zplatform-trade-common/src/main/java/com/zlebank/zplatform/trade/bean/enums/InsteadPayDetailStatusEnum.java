/* 
 * InsteadPayDetailStatusEnum.java  
 * 
 * version TODO
 *
 * 2016年3月13日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bean.enums;

/**
 * 状态(01:待代付审核，09代付审核拒绝，11：待划拨审核，19：划拨审核拒绝，21：待转账审核，29：划拨审核拒绝，31： 转账中 00：代付成功, 39：转账失败)
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月13日 下午3:16:06
 * @since 
 */
public enum InsteadPayDetailStatusEnum {
    /**待代付审核*/
    WAIT_INSTEAD_APPROVE("01"),
    /**代付审核拒绝*/
    INSTEAD_REFUSE("09"),
    /** 待划拨审核*/
    WAIT_TRAN_APPROVE("11"),
    /** 划拨审核拒绝*/
    TRAN_REFUSE("19"),
    /** 待转账审核*/
    WAIT_BANK_TRAN_APPROVE("21"),
    /** 划拨审核拒绝*/
    BANK_TRAN_REFUSE("29"),
    /** 转账中*/
    TRAN_ING("31"),
    /** 转账成功*/
    TRAN_FINISH("00"),
    /** 转账失败*/
    TRAN_FAILED("39"),
    
    
    UNKNOW("99");
   private String code;
    
    private InsteadPayDetailStatusEnum(String code){
        this.code = code;
    }
    
    public static InsteadPayDetailStatusEnum fromValue(String value) {
        for(InsteadPayDetailStatusEnum status:values()){
            if(status.code==value){
                return status;
            }
        }
        return UNKNOW;
    }
    
    public String getCode(){
        return code;
    }
}
