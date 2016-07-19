/* 
 * ProdCaseDAOImpl.java  
 * 
 * version TODO
 *
 * 2015年9月11日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zlebank.zplatform.commons.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.trade.dao.IProdCaseDAO;
import com.zlebank.zplatform.trade.model.ProdCaseModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年9月11日 下午4:55:07
 * @since 
 */
@Repository("prodCaseDAO")
public class ProdCaseDAOImpl extends HibernateBaseDAOImpl<ProdCaseModel> implements IProdCaseDAO{

    public Session getSession(){
        return super.getSession();
    }
}
