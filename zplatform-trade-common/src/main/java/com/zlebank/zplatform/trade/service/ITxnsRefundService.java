/* 
 * ITxnsRefundService.java  
 * 
 * version TODO
 *
 * 2015年10月22日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import com.zlebank.zplatform.trade.model.TxnsRefundModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年10月22日 下午4:47:07
 * @since 
 */
public interface ITxnsRefundService extends IBaseService<TxnsRefundModel, Long>{
    public Long saveRefundOrder(TxnsRefundModel refundOrder);
    /**
     * 通过退款订单号获取退款数据
     * @param refundorderno
     * @return
     */
    public TxnsRefundModel getRefundByRefundorderNo(String refundorderno,String merchno);
   
    /**
     * 更新退款结果
     * @param refundOrder
     */
    public void updateRefundResult(TxnsRefundModel refundOrder);
    public TxnsRefundModel getRefundByRefundor(String refundorderno);
    public void updateRefund(TxnsRefundModel txnsRefundModel);
    public TxnsRefundModel getRefundByOldTxnSeqno(String itxnsCode, String string);
    
    /**
     * 通过交易序列号获取退款
     * @param txnseqno
     * @return
     */
    public TxnsRefundModel getRefundByTxnseqno(String txnseqno);
    
    /**
     * 获取退款之和
     * @param txnseqno_old
     * @return
     */
    public Long getSumAmtByOldTxnseqno(String txnseqno_old);
}
