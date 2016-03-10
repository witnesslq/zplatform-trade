/* 
 * TransferResult.java  
 * 
 * version TODO
 *
 * 2016年3月7日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.batch.spliter;

/**
 * 返回结果
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月7日 下午6:02:07
 * @since 
 */
public class TransferDataResult {
    /**转账批次*/
    private Long bankTranBatchId;
    /**转账明细*/
    private Long bankTranDetaId;

    public Long getBankTranBatchId() {
        return bankTranBatchId;
    }
    public void setBankTranBatchId(Long bankTranBatchId) {
        this.bankTranBatchId = bankTranBatchId;
    }
    public Long getBankTranDetaId() {
        return bankTranDetaId;
    }
    public void setBankTranDetaId(Long bankTranDetaId) {
        this.bankTranDetaId = bankTranDetaId;
    }
}
