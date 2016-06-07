/* 
 * BossInsteadPayService.java  
 * 
 * version TODO
 *
 * 2016年4月22日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bosspay.service;

import java.util.List;

import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bosspay.bean.BtchpmtResultBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年4月22日 下午3:27:23
 * @since 
 */
public interface BossInsteadPayService {
	/**
	 * 批量代付
	 * @param batchNo
	 * @return
	 */
	public ResultBean batchPay(String batchNo);
	
	/**
	 * 单笔实时代付
	 * @param insteadPaySeqNo
	 * @return
	 */
	public ResultBean realTimePay(String insteadPaySeqNo);
	
	/**
	 * 处理查询结果
	 * @param resultList
	 * @return
	 */
	public boolean dealWithResult(String serialNum,List<BtchpmtResultBean> resultList);
}
