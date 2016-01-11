/* 
 * ITxnsNotifyTaskService.java  
 * 
 * version TODO
 *
 * 2015年11月9日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import java.util.List;

import com.zlebank.zplatform.trade.model.TxnsNotifyTaskModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年11月9日 上午9:52:48
 * @since 
 */
public interface ITxnsNotifyTaskService extends IBaseService<TxnsNotifyTaskModel, Long>{

    public void saveTask(TxnsNotifyTaskModel task);
    public boolean updateTask(TxnsNotifyTaskModel task);
    public List<TxnsNotifyTaskModel> findTaskByTxnseqno(String txnseqno,String memberId);
}
