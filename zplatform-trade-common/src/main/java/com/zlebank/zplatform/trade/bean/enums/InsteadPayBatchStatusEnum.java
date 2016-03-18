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
 * 状态(00:全部处理完毕  01:未审核 02:部分审核完毕 03:全部审核完毕)
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月13日 下午3:16:06
 * @since 
 */
public enum InsteadPayBatchStatusEnum {
    /**全部处理完毕*/
    ALL_FINISH("00"),
    /**未审核*/
    WAIT_APPROVE("01"),
    /** 部分审核完毕*/
    PART_APPROVED("02"),
    /** 全部审核完毕*/
    ALL_APPROVED("03"),
    
    UNKNOW("99");
   private String code;
    
    private InsteadPayBatchStatusEnum(String code){
        this.code = code;
    }
    
    public static InsteadPayBatchStatusEnum fromValue(String value) {
        for(InsteadPayBatchStatusEnum status:values()){
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
