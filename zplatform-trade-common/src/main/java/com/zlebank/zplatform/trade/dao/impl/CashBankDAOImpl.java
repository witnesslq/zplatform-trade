/* 
 * CashBankDAOImpl.java  
 * 
 * version TODO
 *
 * 2015年10月14日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zlebank.zplatform.commons.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.trade.dao.ICashBankDAO;
import com.zlebank.zplatform.trade.model.CashBankModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年10月14日 下午3:04:18
 * @since 
 */
@Repository("cashBankDAO")
public class CashBankDAOImpl extends HibernateBaseDAOImpl<CashBankModel> implements ICashBankDAO{
    public Session getSession(){
        return super.getSession();
    }
}
