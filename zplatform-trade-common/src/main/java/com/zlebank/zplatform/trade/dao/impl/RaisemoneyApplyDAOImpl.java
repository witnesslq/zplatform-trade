/* 
 * RaisemoneyApplyDAOImpl.java  
 * 
 * version TODO
 *
 * 2016年8月25日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.dao.impl;

import org.springframework.stereotype.Repository;

import com.zlebank.zplatform.commons.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.trade.dao.RaisemoneyApplyDAO;
import com.zlebank.zplatform.trade.model.PojoRaisemoneyApply;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年8月25日 下午5:50:03
 * @since 
 */
@Repository("raisemoneyApplyDAO")
public class RaisemoneyApplyDAOImpl extends HibernateBaseDAOImpl<PojoRaisemoneyApply> implements RaisemoneyApplyDAO{

}
