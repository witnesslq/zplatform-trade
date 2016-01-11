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

import java.util.List;

import org.hibernate.Session;
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

}
