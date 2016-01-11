/* 
 * TxnsRefundDAOImpl.java  
 * 
 * version TODO
 *
 * 2015年10月22日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zlebank.zplatform.commons.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.trade.dao.ITxnsRefundDAO;
import com.zlebank.zplatform.trade.model.TxnsRefundModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年10月22日 下午3:47:30
 * @since 
 */
@Repository("txnsRefundDAO")
public class TxnsRefundDAOImpl extends HibernateBaseDAOImpl<TxnsRefundModel> implements ITxnsRefundDAO{

    public Session getSession(){
        return super.getSession();
    }
    
}
