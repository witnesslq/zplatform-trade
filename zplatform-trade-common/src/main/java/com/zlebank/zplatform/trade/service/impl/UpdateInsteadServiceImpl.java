/* 
 * UpdateInsteadServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年3月16日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.acc.bean.TradeInfo;
import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.trade.bean.UpdateData;
import com.zlebank.zplatform.trade.bean.enums.InsteadPayDetailStatusEnum;
import com.zlebank.zplatform.trade.bean.enums.TransferBusiTypeEnum;
import com.zlebank.zplatform.trade.dao.InsteadPayBatchDAO;
import com.zlebank.zplatform.trade.dao.InsteadPayDetailDAO;
import com.zlebank.zplatform.trade.model.PojoInsteadPayDetail;
import com.zlebank.zplatform.trade.service.ObserverListService;
import com.zlebank.zplatform.trade.service.UpdateInsteadService;
import com.zlebank.zplatform.trade.service.UpdateSubject;

/**
 * Class Description
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月16日 下午3:59:29
 * @since 
 */
@Service
public class UpdateInsteadServiceImpl implements UpdateInsteadService, UpdateSubject ,ApplicationListener<ContextRefreshedEvent> {
    
    private static final Log log = LogFactory.getLog(UpdateInsteadServiceImpl.class);
    
    @Autowired
    private InsteadPayBatchDAO insteadPayBatchDAO;
    
    @Autowired
    private InsteadPayDetailDAO insteadPayDetailDAO;
    
    @Autowired
    private AccEntryService accEntryService;
    
    /**
     *  更新状态和记账
     * @param data
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public void update(UpdateData data) {
        List<UpdateSubject> observerList = ObserverListService.getInstance().getObserverList();
        for (UpdateSubject subject : observerList) {
            System.out.println(subject.getBusiCode());
        }
        PojoInsteadPayDetail detail = insteadPayDetailDAO.getDetailByTxnseqno(data.getTxnSeqNo());
        if (detail == null) {
            log.error("没有找到需要记账的流水");
            return;
        }
        if ("00".equals(data.getResultCode())) {
            detail.setStatus(InsteadPayDetailStatusEnum.TRAN_FINISH.getCode());
        } else {
            detail.setStatus(InsteadPayDetailStatusEnum.TRAN_FAILED.getCode());
        }
        detail.setRespCode(data.getResultCode());
        detail.setRespMsg(data.getResultMessage());
        insteadPayDetailDAO.merge(detail);
        
        TradeInfo tradeInfo = new TradeInfo();
        tradeInfo.setPayMemberId(detail.getMerId());
        tradeInfo.setAmount(new BigDecimal(detail.getAmt()));
        tradeInfo.setCharge(new BigDecimal(detail.getTxnfee()));
        tradeInfo.setTxnseqno(detail.getTxnseqno());
        if ("00".equals(data.getResultCode())) {
            tradeInfo.setBusiCode("70000002");
            tradeInfo.setChannelId(data.getChannelCode());
        } else {
            tradeInfo.setBusiCode("70000003");
        }
        
        try {
            accEntryService.accEntryProcess(tradeInfo );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 得到业务代码
     */
    @Override
    public String getBusiCode() {
        return TransferBusiTypeEnum.INSTEAD.getCode();
    }


    /**
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ObserverListService.getInstance().add(this);
    }
}
