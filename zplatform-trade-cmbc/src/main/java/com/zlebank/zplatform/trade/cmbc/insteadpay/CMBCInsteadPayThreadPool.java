package com.zlebank.zplatform.trade.cmbc.insteadpay;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.trade.adapter.insteadpay.IInsteadPayTrade;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.cmbc.service.ICMBCTransferService;
import com.zlebank.zplatform.trade.utils.SpringContext;

public class CMBCInsteadPayThreadPool implements IInsteadPayTrade{
	

	private ICMBCTransferService transferService;
	
	@Override
	public ResultBean realTimePay(String insteadPaySeqNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor=Throwable.class)
	public ResultBean batchPay(String batchNo) {
		return transferService.batchTransfer(Long.valueOf(batchNo));
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	public CMBCInsteadPayThreadPool() {
		transferService = (ICMBCTransferService) SpringContext.getContext().getBean("cmbcTransferService");
	}

	
}
