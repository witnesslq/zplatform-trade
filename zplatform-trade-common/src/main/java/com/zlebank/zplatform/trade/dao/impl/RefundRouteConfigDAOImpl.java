/* 
 * RefundRouteConfigDAOImpl.java  
 * 
 * version TODO
 *
 * 2016年5月18日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zlebank.zplatform.commons.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.trade.dao.RefundRouteConfigDAO;
import com.zlebank.zplatform.trade.model.PojoRefundRouteConfigModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月18日 下午12:50:52
 * @since 
 */
@Repository("refundRouteConfigDAO")
public class RefundRouteConfigDAOImpl extends HibernateBaseDAOImpl<PojoRefundRouteConfigModel> implements RefundRouteConfigDAO{
	public Session getSession(){
        return super.getSession();
    }
	
}
