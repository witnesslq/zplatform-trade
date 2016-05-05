/* 
 * BossPayInsteadPayThreadPool.java  
 * 
 * version TODO
 *
 * 2016年4月22日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bosspay.insteadpay;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.trade.adapter.insteadpay.IInsteadPayTrade;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bosspay.service.BossInsteadPayService;
import com.zlebank.zplatform.trade.utils.SpringContext;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年4月22日 下午3:26:00
 * @since 
 */
public class BossPayInsteadPayThreadPool implements IInsteadPayTrade{

	private BossInsteadPayService bossInsteadPayService;
	/**
	 *
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	/**
	 *
	 * @param insteadPaySeqNo
	 * @return
	 */
	@Override
	public ResultBean realTimePay(String insteadPaySeqNo) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 * @param batchNo
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResultBean batchPay(String batchNo) {
		// TODO Auto-generated method stub
		return bossInsteadPayService.batchPay(batchNo);
	}

	/**
	 * 
	 */
	public BossPayInsteadPayThreadPool() {
		super();
		// TODO Auto-generated constructor stub
		bossInsteadPayService = (BossInsteadPayService) SpringContext.getContext().getBean("bossInsteadPayService");
	}

	
}
