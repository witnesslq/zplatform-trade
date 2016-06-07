/* 
 * ITxnsWithdrawDAO.java  
 * 
 * version TODO
 *
 * 2015年10月22日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.dao;

import org.hibernate.Session;

import com.zlebank.zplatform.commons.dao.BaseDAO;
import com.zlebank.zplatform.trade.model.TxnsWithdrawModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年10月22日 下午5:42:53
 * @since 
 */
public interface ITxnsWithdrawDAO extends BaseDAO<TxnsWithdrawModel>{

    public Session getSession();
    public TxnsWithdrawModel getWithdrawBySeqNo(String seqNo);
}
