/* 
 * InsteadPayDetailDAO.java  
 * 
 * version v1.0
 *
 * 2015年11月24日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.dao;

import java.util.List;

import com.zlebank.zplatform.commons.dao.BasePagedQueryDAO;
import com.zlebank.zplatform.trade.bean.InsteadPayDetailQuery;
import com.zlebank.zplatform.trade.model.PojoInsteadPayDetail;

/**
 * 代付批次明细DAO
 *
 * @author Luxiaoshuai
 * @version
 * @date 2015年11月24日 下午12:29:58
 * @since 
 */
public interface InsteadPayDetailDAO extends   BasePagedQueryDAO<PojoInsteadPayDetail, InsteadPayDetailQuery>{
    /**
     * 通过批次号得到批次明细
     * @param batchNo
     * @return
     */
    public List<PojoInsteadPayDetail> getByBatchDetail(String batchNo);
    
    /**
     * 更新代付交易结果
     * @param insteadPayDetail
     */
    public void updateBatchDetailResult(PojoInsteadPayDetail insteadPayDetail);
    
    public PojoInsteadPayDetail getDetailByTxnseqno(String txnseqno,String status);
    
    
    }
