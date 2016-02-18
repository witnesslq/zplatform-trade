/* 
 * TxnsGatewaypayServiceImpl.java  
 * 
 * version TODO
 *
 * 2015年9月7日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.dao.ITxnsGatewaypayDAO;
import com.zlebank.zplatform.trade.model.TxnsGatewaypayModel;
import com.zlebank.zplatform.trade.service.ITxnsGatewaypayService;
import com.zlebank.zplatform.trade.service.base.BaseServiceImpl;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年9月7日 下午7:17:27
 * @since 
 */
@Service("txnsGatewaypayService")
public class TxnsGatewaypayServiceImpl extends BaseServiceImpl<TxnsGatewaypayModel,Long> implements ITxnsGatewaypayService{
    
    @Autowired
    private ITxnsGatewaypayDAO txnsGatewaypayDAO;
    
    
    /**
     *
     * @return
     */
    @Override
    public Session getSession() {
        // TODO Auto-generated method stub
        return txnsGatewaypayDAO.getSession();
    }
    
    @Transactional
    public ResultBean saveGateWay(TxnsGatewaypayModel txnsGateway){
        super.save(txnsGateway);
        
        return null;
    }
    @Transactional
    public ResultBean updateGateWay(TxnsGatewaypayModel txnsGateway){
        super.update(txnsGateway);
        return null;
    }

    /**
     *
     * @param orderNo
     * @return
     */
    @Override
    public TxnsGatewaypayModel getOrderByOrderNo(String orderNo) {
        return super.getUniqueByHQL("from TxnsGatewaypayModel where payorderno = ?", new Object[]{orderNo});
    }
    
    

}
