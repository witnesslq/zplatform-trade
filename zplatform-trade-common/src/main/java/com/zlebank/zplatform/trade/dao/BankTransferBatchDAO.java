/* 
 * BankTransferBatchDAO.java  
 * 
 * version TODO
 *
 * 2016年3月7日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.dao;

import com.zlebank.zplatform.commons.dao.BaseDAO;
import com.zlebank.zplatform.trade.model.PojoBankTransferBatch;

/**
 * 银行转账批次DAO
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月7日 下午2:20:36
 * @since 
 */
public interface BankTransferBatchDAO  extends BaseDAO<PojoBankTransferBatch>{
    /**
     * 通过渠道返回相应的批次号
     * @param channelCode 渠道号
     * @return
     */
    PojoBankTransferBatch getByChannelCode(String channelCode);
}
