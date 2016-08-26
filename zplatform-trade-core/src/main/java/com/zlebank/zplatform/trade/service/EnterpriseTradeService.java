/* 
 * EnterpriseTradeService.java  
 * 
 * version TODO
 *
 * 2016年8月22日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import com.zlebank.zplatform.member.bean.EnterpriseRealNameBean;
import com.zlebank.zplatform.member.bean.EnterpriseRealNameConfirmBean;
import com.zlebank.zplatform.member.exception.DataCheckFailedException;
import com.zlebank.zplatform.member.exception.InvalidMemberDataException;
import com.zlebank.zplatform.trade.bean.FinancierReimbursementBean;
import com.zlebank.zplatform.trade.bean.MerchantReimbursementBean;
import com.zlebank.zplatform.trade.bean.OffLineChargeBean;
import com.zlebank.zplatform.trade.bean.RaiseMoneyTransferBean;
import com.zlebank.zplatform.trade.exception.TradeException;


/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年8月22日 上午10:59:24
 * @since 
 */
public interface EnterpriseTradeService {

	/**
	 * 创建企业实名认证订单
	 * @param enterpriseRealNameBean
	 * @return
	 * @throws TradeException
	 * @throws InvalidMemberDataException
	 */
	public String createEnterpriseRealNameOrder(EnterpriseRealNameBean enterpriseRealNameBean) throws TradeException,InvalidMemberDataException;
	
	/**
	 * 企业实名认证确认
	 * @param enterpriseRealNameConfirmBean
	 * @throws InvalidMemberDataException
	 */
	public void realNameConfirm(EnterpriseRealNameConfirmBean enterpriseRealNameConfirmBean) throws InvalidMemberDataException, DataCheckFailedException,TradeException;
	
	/**
	 * 线下充值申请
	 * @param offLineChargeBean
	 */
	public String offLineCharge(OffLineChargeBean offLineChargeBean);
	
	/**
	 * 创建融资人还款订单
	 * @param bean
	 * @return
	 */
	public String createFinancierOrder(FinancierReimbursementBean bean);
	
	/**
	 * 募集款划转
	 * @param bean
	 * @return
	 */
	public String raiseMoneyTransfer(RaiseMoneyTransferBean bean);
	
	/**
	 * 商户还款
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public String merchReimbusement(MerchantReimbursementBean bean) throws Exception;
}
