/* 
 * UpdateWithdrawServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年3月21日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import java.math.BigDecimal;

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
import com.zlebank.zplatform.acc.service.entry.EntryEvent;
import com.zlebank.zplatform.trade.bean.UpdateData;
import com.zlebank.zplatform.trade.bean.enums.BusinessEnum;
import com.zlebank.zplatform.trade.bean.enums.TransferBusiTypeEnum;
import com.zlebank.zplatform.trade.bean.enums.WithdrawEnum;
import com.zlebank.zplatform.trade.dao.ITxnsOrderinfoDAO;
import com.zlebank.zplatform.trade.dao.ITxnsWithdrawDAO;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;
import com.zlebank.zplatform.trade.model.TxnsWithdrawModel;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.ObserverListService;
import com.zlebank.zplatform.trade.service.UpdateSubject;
import com.zlebank.zplatform.trade.service.UpdateWithdrawService;

/**
 * 提现审核通过，划拨审核拒绝，回退更新。
 *
 * @author houyong
 * @version
 * @date 2016年3月21日 下午4:39:36
 * @since 
 */
@Service(value="UpdateWithdrawServiceImpl")
public class UpdateWithdrawServiceImpl implements UpdateWithdrawService,UpdateSubject,ApplicationListener<ContextRefreshedEvent>{

    private static final Log log = LogFactory.getLog(UpdateWithdrawServiceImpl.class);
    @Autowired
    private ITxnsWithdrawDAO withdrawDao;
    @Autowired
    private AccEntryService accEntryService;
    @Autowired
    private ITxnsOrderinfoDAO txnsOrderinfoDAO;
    @Autowired
    private ITxnsLogService txnsLogService;
    /**
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ObserverListService.getInstance().add(this);
    }

    /**
     *提现更新状态并记账
     * @param data
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor=Throwable.class)
    public void update(UpdateData data) {
        TxnsWithdrawModel withdrawModel=withdrawDao.getWithdrawBySeqNo(data.getTxnSeqNo());
        if (withdrawModel==null) {
            log.error("不存在的提现信息,交易序列号："+data.getTxnSeqNo());
            return;
        }
        TxnsOrderinfoModel orderinfo = txnsOrderinfoDAO.getOrderinfoByOrderNoAndMemberId(withdrawModel.getGatewayorderno(), withdrawModel.getMemberid());
        WithdrawEnum wdEnum=WithdrawEnum.fromValue(data.getResultCode());
        BusinessEnum businessEnum = null;
        
        if (wdEnum.getCode().equals(data.getResultCode())) {
            withdrawModel.setStatus(wdEnum.getCode());
        }else{
            withdrawModel.setStatus(WithdrawEnum.UNKNOW.getCode());
        }
        withdrawDao.merge(withdrawModel);
        EntryEvent entryEvent = null;
        if("00".equals(data.getResultCode())){
        	businessEnum = BusinessEnum.WITHDRAWALS;
        	orderinfo.setStatus("00");
        	entryEvent = EntryEvent.TRADE_SUCCESS;
        }else{
        	businessEnum = BusinessEnum.WITHDRAWALS;
        	orderinfo.setStatus("03");
        	entryEvent = EntryEvent.TRADE_FAIL;
        }
        TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(data.getTxnSeqNo());
        //更新订单信息
        txnsOrderinfoDAO.update(orderinfo);
        TradeInfo tradeInfo=new TradeInfo();
        tradeInfo.setPayMemberId(withdrawModel.getMemberid());
        tradeInfo.setAmount(new BigDecimal(withdrawModel.getAmount()));
        tradeInfo.setCharge(new BigDecimal(withdrawModel.getFee()));
        tradeInfo.setTxnseqno(withdrawModel.getTexnseqno());
        tradeInfo.setBusiCode(businessEnum.getBusiCode());
        tradeInfo.setChannelId(data.getChannelCode());
        tradeInfo.setCoopInstCode(txnsLog.getAccfirmerno());
        try {
            accEntryService.accEntryProcess(tradeInfo,entryEvent);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getBusiCode() {
       return TransferBusiTypeEnum.WITHDRAW.getCode();
    }

}
