package com.zlebank.zplatform.trade.adapter.insteadpay;

import com.zlebank.zplatform.trade.bean.ResultBean;

public interface IInsteadPayTrade extends Runnable{

	/**
	 * 实时代付
	 * @param insteadPaySeqNo
	 * @return
	 */
	public ResultBean realTimePay(String insteadPaySeqNo);
	
	/**
	 * 批量代付
	 * @param batchNo
	 * @return
	 */
	public ResultBean batchPay(String batchNo);
}
