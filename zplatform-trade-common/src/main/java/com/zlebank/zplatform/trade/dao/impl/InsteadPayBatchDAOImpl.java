/* 
 * InsteadPayBatchDAOImpl.java  
 * 
 * version TODO
 *
 * 2015年11月24日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.commons.dao.impl.AbstractPagedQueryDAOImpl;
import com.zlebank.zplatform.trade.bean.InsteadPayBatchQuery;
import com.zlebank.zplatform.trade.dao.InsteadPayBatchDAO;
import com.zlebank.zplatform.trade.model.PojoInsteadPayBatch;

/**
 * Class Description
 *
 * @author Luxiaoshuai
 * @version
 * @date 2015年11月24日 下午1:48:00
 * @since 
 */
@Repository("insteadPayBatchDAO")
public class InsteadPayBatchDAOImpl extends AbstractPagedQueryDAOImpl<PojoInsteadPayBatch, InsteadPayBatchQuery> implements InsteadPayBatchDAO {

    /**
     * 通过批次号得到批次
     * @param batchNo
     * @return
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor=Throwable.class)
    public PojoInsteadPayBatch getByBatchNo(String batchNo, String txnTime) {
        Criteria crite= this.getSession().createCriteria(PojoInsteadPayBatch.class);
        crite.add(Restrictions.eq("batchNo", Long.parseLong(batchNo)));
        crite.add(Restrictions.eq("txnTime", txnTime));
        return (PojoInsteadPayBatch) crite.uniqueResult();
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor=Throwable.class)
    public void updateBatchResult(String batchNo, String status) {
        try {
            String hql = " update PojoInsteadPayBatch set status = ? where id = ? ";
            Query query = getSession().createQuery(hql);
            query.setString(0, status);
            query.setString(1, batchNo);
            query.executeUpdate();
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor=Throwable.class)
    public PojoInsteadPayBatch getInsteadPayBatchByBatchNo(String batchNo,
            String merchId) {
        Criteria crite= this.getSession().createCriteria(PojoInsteadPayBatch.class);
        crite.add(Restrictions.eq("batchNo", Long.parseLong(batchNo)));
        crite.add(Restrictions.eq("merId", merchId));
        return (PojoInsteadPayBatch) crite.uniqueResult();
    }

    /**
     *
     * @param e
     * @return
     */
    @Override
    protected Criteria buildCriteria(InsteadPayBatchQuery e) {
        // TODO Auto-generated method stub
        return null;
    }
    


}
