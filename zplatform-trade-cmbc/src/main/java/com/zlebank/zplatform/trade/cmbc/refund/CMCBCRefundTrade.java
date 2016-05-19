/* 
 * CMCBCRefundTrade.java  
 * 
 * version TODO
 *
 * 2016年5月19日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.cmbc.refund;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zlebank.zplatform.trade.adapter.quickpay.IRefundTrade;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.enums.InsteadPayDetailStatusEnum;
import com.zlebank.zplatform.trade.bean.enums.TransferBusiTypeEnum;
import com.zlebank.zplatform.trade.exception.RecordsAlreadyExistsException;
import com.zlebank.zplatform.trade.model.PojoTranData;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsRefundModel;
import com.zlebank.zplatform.trade.model.TxnsWithdrawModel;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.ITxnsRefundService;
import com.zlebank.zplatform.trade.service.ITxnsWithdrawService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月19日 上午8:35:22
 * @since 
 */
public class CMCBCRefundTrade implements IRefundTrade {

	@Autowired
	private ITxnsLogService txnsLogService;
	@Autowired
	private ITxnsRefundService txnsRefundService;
	
	
	/**
	 *
	 * @param tradeBean
	 * @return
	 */
	@Override
	public ResultBean refund(TradeBean tradeBean) {
		TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(tradeBean.getTxnseqno());
		TxnsRefundModel refund = txnsRefundService.getRefundByTxnseqno(tradeBean.getTxnseqno());
		
		
		//民生代扣走代付流程
		PojoTranData pojoTranData = new PojoTranData();
        List<PojoTranData> pojoTranDataList = new ArrayList<PojoTranData>();
        // 划拨批次号
        pojoTranData.setTranDataSeqNo(String.valueOf(System
                .currentTimeMillis()));
        // pojoTranData.setTranBatch(tranBatch);
        pojoTranData.setAccNo(txnsLog.getAccordno());
        // 划拨金额
        pojoTranData.setTranAmt(refund.getAmount());
        // pojoTranData.setBusiDataId("11111111111111");
        // pojoTranDataList.add(pojoTranData);

        // pojoTranData.setTxnseqno();
        pojoTranData.setStatus(InsteadPayDetailStatusEnum.WAIT_TRAN_APPROVE
                .getCode());
        // PojoTranData tmp = BeanCopyUtil.copyBean(PojoTranData.class,
        // pojoTranData);
        // pojoTranData.setTranAmt(detail.getAmt());
        // /** "业务流水号" **/
        pojoTranData.setBusiDataId(Long.parseLong(refund
                .getRefundorderno()));
        pojoTranData.setMemberId(refund.getMerchno());
        // 交易手续费0
        pojoTranData.setTranFee(0L);
        pojoTranData.setBusiType(TransferBusiTypeEnum.INSTEAD.getCode());
        //pojoTranData.setBankNo(refund.get);
        //pojoTranData.setBankName(job.get("ACCORDNO").toString());
        pojoTranDataList.add(pojoTranData);

        /*try {
            transferDataService.saveTransferData(
                    TransferBusiTypeEnum.REFUND, 1L, pojoTranDataList);
           
        } catch (RecordsAlreadyExistsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
		return null;
	}

}
