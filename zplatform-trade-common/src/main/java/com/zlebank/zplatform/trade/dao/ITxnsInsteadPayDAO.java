package com.zlebank.zplatform.trade.dao;

import com.zlebank.zplatform.commons.dao.BaseDAO;
import com.zlebank.zplatform.trade.model.PojoBankTransferBatch;
import com.zlebank.zplatform.trade.model.PojoTxnsInsteadPay;

public interface ITxnsInsteadPayDAO extends BaseDAO<PojoTxnsInsteadPay>{

	/**
	 * 通过回盘文件名称获取代付交易流水
	 * @param fileName
	 * @return
	 */
	public PojoTxnsInsteadPay getByResponseFileName(String fileName);
}
