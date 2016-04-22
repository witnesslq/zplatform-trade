package com.zlebank.zplatform.trade.bean.enums;

public enum ChannelEnmu {
    ZLPAY("98000001"),//证联支付
    REAPAY("96000001"),//融宝快捷
    TEST("95000001"),//测试渠道
    CMBCINSTEADPAY("93000001"),//民生银行跨行代扣
    CMBCWITHHOLDING("93000002"),//民生银行跨行代扣
    CMBCSELFWITHHOLDING("93000003"),//民生银行本行代扣
    BOSSPAYCOLLECTION("92000001"),//博士金电实时代收
    UNKNOW("");//未知
    private String chnlcode;
    
    private ChannelEnmu(String chnlcode){
        this.chnlcode = chnlcode;
    }
    
    public static ChannelEnmu fromValue(String value) {
        for(ChannelEnmu busi:values()){
            if(busi.chnlcode.equals(value)){
                return busi;
            }
        }
        return UNKNOW;
    }
    
    public String getChnlcode(){
        return chnlcode;
    }
}
