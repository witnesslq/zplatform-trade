/* 
 * CashBankServiceImpl.java  
 * 
 * version TODO
 *
 * 2015年10月14日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.trade.dao.ICashBankDAO;
import com.zlebank.zplatform.trade.model.CashBankModel;
import com.zlebank.zplatform.trade.service.ICashBankService;
import com.zlebank.zplatform.trade.service.base.BaseServiceImpl;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年10月14日 下午3:07:10
 * @since 
 */
@Service("cashBankService")
public class CashBankServiceImpl extends BaseServiceImpl<CashBankModel, Long> implements ICashBankService{

    @Autowired
    private ICashBankDAO cashBankDAO;
    /**
     *
     * @return
     */
    @Override
    public Session getSession() {
        // TODO Auto-generated method stub
        return cashBankDAO.getSession();
    }
    
    public List<CashBankModel> findBankByCashCode(String cashCode){
        return (List<CashBankModel>) super.queryByHQL(" from CashBankModel where cashcode = ? and status = ? ", new Object[]{cashCode,"00"});
    }
    
    public List<CashBankModel> findBankByPaytype(String payType){
        return (List<CashBankModel>) super.queryByHQL(" from CashBankModel where paytype = ? and status = ? ", new Object[]{payType,"00"});
    }
    
    @SuppressWarnings("unchecked")
	public List<CashBankModel> findBankPage(int page,int pageSize){
    	Query query = cashBankDAO.getSession().createQuery("from CashBankModel where paytype = ? and status = ? order by  tid asc");
    	query.setString(0, "01");
    	query.setString(1, "00");
    	query.setFirstResult((pageSize)*((page==0?1:page)-1));
    	query.setMaxResults(pageSize);
		return query.list();
    }
    
    public long findBankCount(){
    	Query query = cashBankDAO.getSession().createSQLQuery("select count(1) as total from T_CASH_BANK where paytype = ? and status = ? ").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    	query.setString(0, "01");
    	query.setString(1, "00");
    	
    	Map<String, BigDecimal> valueMap = (Map<String, BigDecimal>) query.uniqueResult();
    	return valueMap.get("TOTAL").longValue();
    }

}
