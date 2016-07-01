/* 
 * ChanpayQuickPayTradeThread.java  
 * 
 * version TODO
 *
 * 2016年6月30日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.chanpay.quickpay;

import com.zlebank.zplatform.commons.dao.ProvinceDAO;
import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.sms.service.ISMSService;
import com.zlebank.zplatform.trade.adapter.quickpay.IQuickPayTrade;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.enums.ChannelEnmu;
import com.zlebank.zplatform.trade.bean.enums.ChnlTypeEnum;
import com.zlebank.zplatform.trade.bean.enums.TradeTypeEnum;
import com.zlebank.zplatform.trade.chanpay.service.ChanPayQuickPayService;
import com.zlebank.zplatform.trade.dao.ITxnsOrderinfoDAO;
import com.zlebank.zplatform.trade.dao.RealnameAuthDAO;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.PojoCardBind;
import com.zlebank.zplatform.trade.model.PojoRealnameAuth;
import com.zlebank.zplatform.trade.service.CardBindService;
import com.zlebank.zplatform.trade.service.IQuickpayCustService;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.ITxnsQuickpayService;
import com.zlebank.zplatform.trade.utils.SMSThreadPool;
import com.zlebank.zplatform.trade.utils.SMSUtil;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年6月30日 下午4:19:43
 * @since 
 */
public class ChanpayQuickPayTradeThread implements IQuickPayTrade{
	private static final String PAYINSTID = "90000002";
	private TradeBean tradeBean;
	private TradeTypeEnum tradeType;
	private ITxnsQuickpayService txnsQuickpayService;
	private ProvinceDAO provinceDAO;
	private ITxnsLogService txnsLogService;
	private ITxnsOrderinfoDAO txnsOrderinfoDAO;
	private IQuickpayCustService quickpayCustService;
	private ISMSService smsService;
	private ChanPayQuickPayService chanPayQuickPayService;
	private RealnameAuthDAO realnameAuthDAO;
	private CardBindService cardBindService;
	/**
	 *
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (tradeBean == null) {
			return;
		}
		switch (tradeType) {
			case SENDSMS:
				sendSms(tradeBean);
				break;
			case SUBMITPAY:
				submitPay(tradeBean);
				break;
			case BANKSIGN:
				bankSign(tradeBean);
				break;
			default:
				break;
		}
	}
	/**
	 *
	 * @param trade
	 * @return
	 */
	@Override
	public ResultBean sendSms(TradeBean trade) {
		String mobile = trade.getMobile();
		String payorderNo = txnsQuickpayService.saveCMBCOuterBankSign(trade);
		SMSThreadPool.getInstance().executeMission(
				new SMSUtil(mobile, "", trade.getTn(), DateUtil
						.getCurrentDateTime(), payorderNo, trade
						.getMiniCardNo(), trade.getAmount_y()));
		return null;
	}
	/**
	 *
	 * @param trade
	 * @return
	 */
	@Override
	public ResultBean bankSign(TradeBean trade) {
		try {
			if(trade.getCardId()==0){//未绑定银行卡
				//检查系统实名认证表中是否有相关记录
				PojoRealnameAuth realnameAuth = new PojoRealnameAuth(trade);
				realnameAuth = realnameAuthDAO.getByCardInfo(realnameAuth);
				if(realnameAuth!=null){//已经完成实名认证，不需要再次调用畅捷的实名认证接口，需要调用畅捷的协议签约接口
					
			    }else{
			    	chanPayQuickPayService.realNameAuth(trade);
			    }
			}else{//绑定银行卡
				PojoCardBind cardBind = cardBindService.getCardBind(trade.getCardId(), ChannelEnmu.CHANPAYCOLLECTMONEY.getChnlcode());
				if(cardBind==null){//未签署协议
					
				}else{//已签署协议
					
				}
			}
		} catch (TradeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 *
	 * @param trade
	 * @return
	 */
	@Override
	public ResultBean submitPay(TradeBean trade) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 *
	 * @param trade
	 * @return
	 */
	@Override
	public ResultBean queryTrade(TradeBean trade) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 *
	 * @param tradeType
	 */
	@Override
	public void setTradeType(TradeTypeEnum tradeType) {
		// TODO Auto-generated method stub
		
	}
	/**
	 *
	 * @param tradeBean
	 */
	@Override
	public void setTradeBean(TradeBean tradeBean) {
		// TODO Auto-generated method stub
		
	}
}
