package com.zlebank.zplatform.trade.bean.enums;

public enum InsteadPayTypeEnum {
    INITI("01"),
    Paying("02"),
    finish("00"),
    fail("03"),
    UNKNOW("99");
   private String code;
    
    private InsteadPayTypeEnum(String code){
        this.code = code;
    }
    
    public static InsteadPayTypeEnum fromValue(String value) {
        for(InsteadPayTypeEnum status:values()){
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
