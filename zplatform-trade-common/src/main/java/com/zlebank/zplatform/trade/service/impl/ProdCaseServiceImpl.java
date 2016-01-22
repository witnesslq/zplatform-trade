/* 
 * ProdCaseServiceImpl.java  
 * 
 * version TODO
 *
 * 2015年9月11日 
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

import com.zlebank.zplatform.acc.bean.enums.BusiType;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.gateway.OrderBean;
import com.zlebank.zplatform.trade.bean.wap.WapOrderBean;
import com.zlebank.zplatform.trade.dao.IProdCaseDAO;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.MemberBaseModel;
import com.zlebank.zplatform.trade.model.ProdCaseModel;
import com.zlebank.zplatform.trade.model.TxncodeDefModel;
import com.zlebank.zplatform.trade.service.IMemberService;
import com.zlebank.zplatform.trade.service.IProdCaseService;
import com.zlebank.zplatform.trade.service.ITxncodeDefService;
import com.zlebank.zplatform.trade.service.base.BaseServiceImpl;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年9月11日 下午5:28:40
 * @since 
 */
@Service("prodCaseService")
public class ProdCaseServiceImpl extends BaseServiceImpl<ProdCaseModel, Long> implements IProdCaseService{

    @Autowired
    private IProdCaseDAO prodCaseDAO;
    @Autowired
    private IMemberService memberService;
    @Autowired
    private ITxncodeDefService txncodeDefService;
    /**
     *
     * @return
     */
    @Override
    public Session getSession() {
        // TODO Auto-generated method stub
        return prodCaseDAO.getSession();
    }
    /**
     *
     * @param order
     * @return
     */
    @Override
    public ResultBean verifyBusiness(OrderBean order) {
        ResultBean resultBean = null;
        MemberBaseModel member = null;
        TxncodeDefModel busiModel = txncodeDefService.getBusiCode(order.getTxnType(), order.getTxnSubType(), order.getBizType());
        if(StringUtil.isNotEmpty(order.getMerId())){
        	member = memberService.getMemberByMemberId(order.getMerId());
        	ProdCaseModel prodCase= getMerchProd(member.getPrdtver(),busiModel.getBusicode());
            if(prodCase==null){
                resultBean = new ResultBean("RC36", "商户未开通此业务");
            }else {
                resultBean = new ResultBean("success");
            }
        }else{
        	BusiType busiType = BusiType.fromValue(busiModel.getBusitype());
            if(busiType==BusiType.CASH||busiType==BusiType.REPAIDP){
            	resultBean = new ResultBean("success");
            }else{
            	resultBean = new ResultBean("RC36", "个人用户未开通此业务");
            }
        }
        
        return resultBean;
    }
    
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public ProdCaseModel getMerchProd(String prdtver,String busicode){
        return super.getUniqueByHQL("from ProdCaseModel where prdtver=? and busicode=?", new Object[]{prdtver,busicode});
    }

    public void verifyWapBusiness(WapOrderBean order) throws TradeException {
        MemberBaseModel member = memberService.get(order.getMerId());
        TxncodeDefModel busiModel = txncodeDefService.getBusiCode(order.getTxnType(), order.getTxnSubType(), order.getBizType());
        if(busiModel==null){
            throw new TradeException("GW02");
        }
        ProdCaseModel prodCase= super.getUniqueByHQL("from ProdCaseModel where prdtver=? and busicode=?", new Object[]{member.getPrdtver(),busiModel.getBusicode()});
        if(prodCase==null){
             throw new TradeException("GW03");
        }
    }
}
