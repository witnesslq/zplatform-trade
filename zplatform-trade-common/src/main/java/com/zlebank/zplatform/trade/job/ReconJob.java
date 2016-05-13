/* 
 * ReconJob.java  
 * 
 * version TODO
 *
 * 2016年5月12日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.job;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.acc.bean.TradeInfo;
import com.zlebank.zplatform.acc.bean.enums.BusiType;
import com.zlebank.zplatform.acc.dao.BusinessDAO;
import com.zlebank.zplatform.acc.exception.AbstractBusiAcctException;
import com.zlebank.zplatform.acc.exception.AccBussinessException;
import com.zlebank.zplatform.acc.pojo.Money;
import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.acc.service.entry.EntryEvent;
import com.zlebank.zplatform.commons.dao.pojo.BusiTypeEnum;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.trade.bean.enums.BusinessEnum;
import com.zlebank.zplatform.trade.dao.ITxnsLogDAO;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.service.ITxnsLogService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月12日 下午5:19:58
 * @since 
 */
public class ReconJob {
	private static final Log log = LogFactory.getLog(ReconJob.class);
	
	@Autowired
	private ITxnsLogService txnsLogService;
	@Autowired
	private AccEntryService accEntryService;
	@Autowired
	private BusinessDAO businessDAO;
	
	@Transactional(propagation = Propagation.REQUIRED)
    public void execute() throws IOException {
		List<Map<String, Object>> selfTxnList = (List<Map<String, Object>>) txnsLogService.queryBySQL("SELECT * FROM T_SELF_TXN T WHERE STATUS = ? AND RESULT = ?", new Object[]{"9","02"});
		if(selfTxnList.size()>0){
			for(Map<String, Object> value:selfTxnList){
				TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(value.get("TXNSEQNO")+"");
				//txnsLog.getAcccoopinstino();
				//通道手续费
				Long channelFee = Long.valueOf(value.get("CFEE")+"")+Long.valueOf(value.get("DFEE")+"");
				String payMemberId = "";
        		String payToMemberId = "";
				 //记录提现账务
	            try {
	            	List<Map<String, Object>> businessList = (List<Map<String, Object>>) txnsLogService.queryBySQL("SELECT * FROM T_BUSINESS WHERE BUSICODE = ?", new Object[]{value.get("BUSICODE").toString()});
	            	if(businessList.size()>0){
	            		String busiType = businessList.get(0).get("BUSITYPE")+"";
	            		BusiTypeEnum busiTypeEnum = BusiTypeEnum.fromValue(busiType);
	            		
	            		switch (busiTypeEnum) {
							case charge:
								if(StringUtil.isEmpty(txnsLog.getAccsecmerno())){
					                payMemberId = txnsLog.getAccmemberid();//
					                payToMemberId = txnsLog.getAccmemberid();
					            }else{
					                String memberId = txnsLog.getAccmemberid();
					                if(StringUtil.isEmpty(memberId)){
					                    break;
					                }
					                payMemberId = memberId;
					                payToMemberId = memberId;
					            }
								break;
							case consumption:
								/**付款方会员ID**/
				                payMemberId = StringUtil.isNotEmpty(txnsLog.getAccmemberid())?txnsLog.getAccmemberid():"999999999999999";
				                /**收款方会员ID**/
				                payToMemberId = StringUtil.isEmpty(txnsLog.getAccsecmerno())?txnsLog.getAccfirmerno():txnsLog.getAccsecmerno();
				                /**渠道**/
				                String channelId = txnsLog.getPayinst();//支付机构代码
				                if("99999999".equals(channelId)){
				                    payMemberId = txnsLog.getPayfirmerno();
				                }else{
				                	
				                }
								break;
							case insteadPay:
								  payMemberId = StringUtil.isEmpty(txnsLog.getAccsecmerno())?txnsLog.getAccfirmerno():txnsLog.getAccsecmerno();
							      payToMemberId = "999999999999999";
								break;
							case refund:
								break;
							case withdrawal:
								if(StringUtil.isEmpty(txnsLog.getAccmemberid())||"999999999999999".equals(txnsLog.getAccmemberid())){
									payMemberId = txnsLog.getAccsecmerno();
							        payToMemberId = txnsLog.getAccsecmerno();
								}else{
									payMemberId = txnsLog.getAccmemberid();
							        payToMemberId = txnsLog.getAccmemberid();
								}
								break;
							default:
								break;
						}
	            	}
					TradeInfo tradeInfo = new TradeInfo();
					tradeInfo.setBusiCode(value.get("BUSICODE")+"");
					tradeInfo.setPayMemberId(payMemberId);
					tradeInfo.setPayToMemberId(payToMemberId);
					tradeInfo.setAmount(new BigDecimal(value.get("AMOUNT")+""));
					tradeInfo.setCharge(new BigDecimal(0));
					tradeInfo.setTxnseqno(value.get("TXNSEQNO")+"");
					tradeInfo.setChannelId(txnsLog.getPayinst());
					tradeInfo.setChannelFee(new BigDecimal(channelFee));
					tradeInfo.setCoopInstCode(txnsLog.getAcccoopinstino());
					accEntryService.accEntryProcess(tradeInfo, EntryEvent.RECON_SUCCESS);
					txnsLogService.executeBySQL("UPDATE T_SELF_TXN SET RESULT=? WHERE TID = ?", new Object[]{"03",value.get("TID")});
				} catch (AccBussinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AbstractBusiAcctException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
