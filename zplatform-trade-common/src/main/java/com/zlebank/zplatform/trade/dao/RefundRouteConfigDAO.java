/* 
 * RefundRouteConfigDAO.java  
 * 
 * version TODO
 *
 * 2016年5月18日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.dao;

import org.hibernate.Session;

import com.zlebank.zplatform.commons.dao.BaseDAO;
import com.zlebank.zplatform.trade.model.PojoRefundRouteConfigModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月18日 下午12:50:18
 * @since 
 */
public interface RefundRouteConfigDAO extends BaseDAO<PojoRefundRouteConfigModel>{
	Session getSession();
}
