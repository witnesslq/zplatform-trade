/* 
 * BankTransferBatchDAOImpl.java  
 * 
 * version TODO
 *
 * 2016年3月7日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zlebank.zplatform.commons.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.trade.dao.BankTransferBatchDAO;
import com.zlebank.zplatform.trade.model.PojoBankTransferBatch;

/**
 * 银行转账DAO
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月7日 下午2:21:25
 * @since 
 */
@Repository
public class BankTransferBatchDAOImpl  extends HibernateBaseDAOImpl<PojoBankTransferBatch> implements BankTransferBatchDAO {

    /**
     * 通过渠道返回相应的批次号
     * @param channelCode 渠道号
     * @return
     */
    @Override
    public PojoBankTransferBatch getByChannelCode(String channelCode) {
        Criteria crite= this.getSession().createCriteria(PojoBankTransferBatch.class);
        crite.add(Restrictions.eq("channel", channelCode));
        crite.addOrder(Order.asc("tid"));
        crite.setFirstResult(0);
        crite.setMaxResults(1);
        return (PojoBankTransferBatch) crite.uniqueResult();

    }

}
