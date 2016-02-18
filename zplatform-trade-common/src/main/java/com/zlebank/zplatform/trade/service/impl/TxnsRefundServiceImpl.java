/* 
 * TxnsRefundServiceImpl.java  
 * 
 * version TODO
 *
 * 2015年10月22日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.trade.dao.ITxnsRefundDAO;
import com.zlebank.zplatform.trade.model.TxnsRefundModel;
import com.zlebank.zplatform.trade.service.ITxnsRefundService;
import com.zlebank.zplatform.trade.service.base.BaseServiceImpl;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年10月22日 下午4:48:00
 * @since 
 */
@Service("txnsRefundService")
public class TxnsRefundServiceImpl extends BaseServiceImpl<TxnsRefundModel,Long> implements ITxnsRefundService{

    @Autowired
    private ITxnsRefundDAO txnsRefundDAO;
    /**
     *
     * @return
     */
    @Override
    public Session getSession() {
        // TODO Auto-generated method stub
        return txnsRefundDAO.getSession();
    }
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public Long saveRefundOrder(TxnsRefundModel refundOrder){
        return super.saveEntity(refundOrder);
    }
    
    public TxnsRefundModel getRefundByRefundorderNo(String refundorderno,String merchno) {
        String hql = " from TxnsRefundModel refundorderno=? and merchno=?";
        return super.getUniqueByHQL(hql, new Object[]{refundorderno,merchno});
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public void updateRefundResult(TxnsRefundModel refundOrder) {
        // TODO Auto-generated method stub
        String hql = "update TxnsWithdrawModel set finishtime = ?,status = ?,retcode = ?,retinfo = ? where refundorderno = ? and memberid = ? ";
        super.updateByHQL(hql, new Object[]{refundOrder.getFinishtime(),refundOrder.getStatus(),refundOrder.getRetcode(),refundOrder.getRetinfo(),refundOrder.getRefundorderno(),refundOrder.getMemberid()});
    }
}
