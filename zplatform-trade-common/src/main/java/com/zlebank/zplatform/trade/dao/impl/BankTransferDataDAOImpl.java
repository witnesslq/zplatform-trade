/* 
 * BankTransferDetaDAOImpl.java  
 * 
 * version TODO
 *
 * 2016年3月7日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zlebank.zplatform.commons.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.trade.dao.BankTransferDataDAO;
import com.zlebank.zplatform.trade.model.PojoBankTransferData;

/**
 * 银行转账流水DAO
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月7日 下午2:21:54
 * @since 
 */
@Repository
public class BankTransferDataDAOImpl  extends HibernateBaseDAOImpl<PojoBankTransferData> implements BankTransferDataDAO{

    /**
     * 根据划拨流水ID取出转账ID
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PojoBankTransferData> getByTranDataId(Long id) {
        Criteria crite= this.getSession().createCriteria(PojoBankTransferData.class);
        crite.add(Restrictions.eq("tranDataId", id));
        return crite.list();
    }
}
