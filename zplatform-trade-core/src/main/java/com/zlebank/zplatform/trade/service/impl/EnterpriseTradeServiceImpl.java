/* 
 * EnterpriseTradeServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年8月22日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.acc.bean.TradeInfo;
import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.acc.service.entry.EntryEvent;
import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.member.bean.EnterpriseBean;
import com.zlebank.zplatform.member.bean.EnterpriseRealNameBean;
import com.zlebank.zplatform.member.bean.EnterpriseRealNameConfirmBean;
import com.zlebank.zplatform.member.exception.DataCheckFailedException;
import com.zlebank.zplatform.member.exception.InvalidMemberDataException;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.member.service.CoopInstiService;
import com.zlebank.zplatform.member.service.EnterpriseService;
import com.zlebank.zplatform.member.service.MemberService;
import com.zlebank.zplatform.sms.service.ISMSService;
import com.zlebank.zplatform.trade.bean.FinancierReimbursementBean;
import com.zlebank.zplatform.trade.bean.MerchantReimbursementBean;
import com.zlebank.zplatform.trade.bean.OffLineChargeBean;
import com.zlebank.zplatform.trade.bean.RaiseMoneyTransferBean;
import com.zlebank.zplatform.trade.bean.ReimbursementDetailBean;
import com.zlebank.zplatform.trade.bean.enums.TradeStatFlagEnum;
import com.zlebank.zplatform.trade.dao.ITxnsOrderinfoDAO;
import com.zlebank.zplatform.trade.dao.MerchReimburseBatchDAO;
import com.zlebank.zplatform.trade.dao.MerchReimburseDetaDAO;
import com.zlebank.zplatform.trade.dao.RaisemoneyApplyDAO;
import com.zlebank.zplatform.trade.dao.TxnsChargeDAO;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.PojoMerchReimburseBatch;
import com.zlebank.zplatform.trade.model.PojoMerchReimburseDeta;
import com.zlebank.zplatform.trade.model.PojoRaisemoneyApply;
import com.zlebank.zplatform.trade.model.PojoTxnsCharge;
import com.zlebank.zplatform.trade.model.TxncodeDefModel;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;
import com.zlebank.zplatform.trade.service.EnterpriseTradeService;
import com.zlebank.zplatform.trade.service.ITxncodeDefService;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.utils.OrderNumber;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年8月22日 上午11:02:13
 * @since 
 */
@Service("enterpriseTradeService")
public class EnterpriseTradeServiceImpl implements EnterpriseTradeService{

	@Autowired
	private EnterpriseService enterpriseService;
	@Autowired
	private ITxnsOrderinfoDAO txnsOrderinfoDAO; 
	@Autowired
	private CoopInstiService coopInstiService;
	@Autowired
	private ITxncodeDefService txncodeDefService;
	@Autowired
	private ITxnsLogService txnsLogService;
	@Autowired
	private ISMSService smsService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private TxnsChargeDAO txnsChargeDAO;
	@Autowired
	private AccEntryService accEntryService;
	@Autowired
	private RaisemoneyApplyDAO raisemoneyApplyDAO;
	@Autowired
	private MerchReimburseBatchDAO merchReimburseBatchDAO;
	@Autowired
	private MerchReimburseDetaDAO merchReimburseDetaDAO;
	
	/**
	 *
	 * @return
	 * @throws TradeException 
	 * @throws InvalidMemberDataException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public String createEnterpriseRealNameOrder(EnterpriseRealNameBean enterpriseRealNameBean) throws TradeException, InvalidMemberDataException {
		
		checkRealNameBean(enterpriseRealNameBean);
		//创建交易流水
		TxnsLogModel txnsLog = new TxnsLogModel();
		//由于企业会员 没有扣率，风控，路由版本，所以使用合作机构的
		TxncodeDefModel busiModel = txncodeDefService.getBusiCode(
				enterpriseRealNameBean.getTxnType(), enterpriseRealNameBean.getTxnSubType(), enterpriseRealNameBean.getBizType());
		
		// 10-产品版本,11-扣率版本,12-分润版本,13-风控版本,20-路由版本
		//txnsLog.setRiskver(txncodeDefService.getDefaultVerInfo(enterpriseRealNameBean.getCoopInsti(),busiModel.getBusicode(), 13));
		//txnsLog.setSplitver(txncodeDefService.getDefaultVerInfo(enterpriseRealNameBean.getCoopInsti(),busiModel.getBusicode(), 12));
		//txnsLog.setFeever(txncodeDefService.getDefaultVerInfo(enterpriseRealNameBean.getCoopInsti(),busiModel.getBusicode(), 11));
		//txnsLog.setPrdtver(txncodeDefService.getDefaultVerInfo(enterpriseRealNameBean.getCoopInsti(),busiModel.getBusicode(), 10));
		//txnsLog.setRoutver(txncodeDefService.getDefaultVerInfo(enterpriseRealNameBean.getCoopInsti(),busiModel.getBusicode(), 20));
		txnsLog.setAccsettledate(DateUtil.getSettleDate(1));
		txnsLog.setTxndate(DateUtil.getCurrentDate());
		txnsLog.setTxntime(DateUtil.getCurrentTime());
		txnsLog.setBusicode(busiModel.getBusicode());
		txnsLog.setBusitype(busiModel.getBusitype());
		// 核心交易流水号，交易时间（yymmdd）+业务代码+6位流水号（每日从0开始）
		txnsLog.setTxnseqno(OrderNumber.getInstance().generateTxnseqno(txnsLog.getBusicode()));
		txnsLog.setAmount(0L);
		txnsLog.setAccordno("E"+DateUtil.getCurrentDateTime());// 由于企业实名认证没有订单号，使用时间戳作为订单号
		txnsLog.setAccfirmerno(enterpriseRealNameBean.getCoopInsti());
		txnsLog.setAcccoopinstino(enterpriseRealNameBean.getCoopInsti());
		txnsLog.setAccsecmerno(enterpriseRealNameBean.getMemberId());

		txnsLog.setAccordcommitime(DateUtil.getCurrentDateTime());
		txnsLog.setTradestatflag(TradeStatFlagEnum.INITIAL.getStatus());// 交易初始状态
		txnsLog.setAccmemberid(enterpriseRealNameBean.getMemberId());
		
		//创建企业实名认证订单
		TxnsOrderinfoModel orderinfo = new TxnsOrderinfoModel();
		orderinfo.setId(-1L);
		orderinfo.setOrderno(txnsLog.getAccordno());// 由于企业实名认证没有订单号，使用时间戳作为订单号
		orderinfo.setOrderamt(0L);
		orderinfo.setOrderfee(0L);
		orderinfo.setOrdercommitime(DateUtil.getCurrentDateTime());
		orderinfo.setRelatetradetxn(txnsLog.getTxnseqno());// 关联的交易流水表中的交易序列号
		orderinfo.setFirmemberno(enterpriseRealNameBean.getCoopInsti());
		orderinfo.setFirmembername(coopInstiService.getInstiByInstiCode(
		enterpriseRealNameBean.getCoopInsti()).getInstiName());
		
		orderinfo.setSecmemberno(enterpriseRealNameBean.getMemberId());//企业会员号
		orderinfo.setSecmembername(enterpriseRealNameBean.getEnterpriseName());
		

		orderinfo.setSecmembershortname(enterpriseRealNameBean.getEnterpriseName());
		orderinfo.setFronturl(enterpriseRealNameBean.getFrontURL());
		orderinfo.setTxntype(enterpriseRealNameBean.getTxnType());
		orderinfo.setTxnsubtype(enterpriseRealNameBean.getTxnSubType());
		orderinfo.setBiztype(enterpriseRealNameBean.getBizType());
		orderinfo.setPaytimeout(DateUtil.formatDateTime(DateUtil.skipDateTime(new Date(), 7)));
		orderinfo.setTn(OrderNumber.getInstance().generateTN(enterpriseRealNameBean.getCoopInsti()));
		orderinfo.setMemberid(enterpriseRealNameBean.getMemberId());
		orderinfo.setCurrencycode("156");
		orderinfo.setStatus("01");
		//////////////////////////////////订单和交易日志保存完毕///////////////////////////////////////////
		txnsLogService.saveTxnsLog(txnsLog);
		txnsOrderinfoDAO.saveA(orderinfo);
		enterpriseRealNameBean.setTn(orderinfo.getTn());
		
		try {
			enterpriseService.realNameApply(enterpriseRealNameBean);
		} catch (InvalidMemberDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return orderinfo.getTn();
	}
	
	private void checkRealNameBean(EnterpriseRealNameBean enterpriseRealNameBean) throws InvalidMemberDataException{
		if(StringUtil.isEmpty(enterpriseRealNameBean.getAccName())){
			throw new InvalidMemberDataException("银行账户不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getAccNum())){
			throw new InvalidMemberDataException("银行账号不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getBizType())){
			throw new InvalidMemberDataException("产品类型不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getChannelType())){
			throw new InvalidMemberDataException("渠道类型不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getContact())){
			throw new InvalidMemberDataException("企业联系人姓名不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getContactsTelNo())){
			throw new InvalidMemberDataException("企业联系人电话不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getCoopInsti())){
			throw new InvalidMemberDataException("合作机构不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getCorpNo())){
			throw new InvalidMemberDataException("法人代表身份证号不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getCorporation())){
			throw new InvalidMemberDataException("法人姓名不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getEnterpriseName())){
			throw new InvalidMemberDataException("企业名称不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getFrontURL())){
			throw new InvalidMemberDataException("前台通知地址不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getLicenceNo())){
			throw new InvalidMemberDataException("工商营业执照号不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getMemberId())){
			throw new InvalidMemberDataException("企业会员ID不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getOrgCode())){
			throw new InvalidMemberDataException("组织机构代码不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getTaxNo())){
			throw new InvalidMemberDataException("企业税务登记号不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getTxnSubType())){
			throw new InvalidMemberDataException("交易子类不能为空");
		}
		if(StringUtil.isEmpty(enterpriseRealNameBean.getTxnType())){
			throw new InvalidMemberDataException("交易类型不能为空");
		}
		
	}

	public void realNameConfirm(EnterpriseRealNameConfirmBean enterpriseRealNameConfirmBean) throws InvalidMemberDataException, DataCheckFailedException, TradeException{
		TxnsOrderinfoModel orderinfo = txnsOrderinfoDAO.getOrderByTN(enterpriseRealNameConfirmBean.getTn());
		if("00".equals(orderinfo.getStatus())){
			throw new TradeException("T000","企业实名认证已完成，请不要重复认证");
		}
		//企业实名认证确认
		enterpriseService.realNameConfirm(enterpriseRealNameConfirmBean);
		//更新实名认证订单成功
		txnsOrderinfoDAO.updateOrderToSuccessByTN(enterpriseRealNameConfirmBean.getTn());
		
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public String offLineCharge(OffLineChargeBean offLineChargeBean){
		//保存线下充值订单
		PojoTxnsCharge charge = new PojoTxnsCharge();
		PojoMember member = memberService.getMbmberByMemberId(offLineChargeBean.getMemberId(), null);
		charge.setMemberid(member.getMemId()+"");
		charge.setAmount(Long.valueOf(offLineChargeBean.getTxnAmt()));
		charge.setChargecode(offLineChargeBean.getChargeCode());
		charge.setChargeno(offLineChargeBean.getOrderId());		
		charge.setChargenoinstid("99999999");//内部调账渠道
		charge.setChargetype("02");//商户充值
		charge.setStatus("01");//等待初审
		charge.setIntime(new Date());
		charge.setInuser(0L);
		txnsChargeDAO.saveA(charge);
		//保存订单数据
		TxnsOrderinfoModel orderinfo = new TxnsOrderinfoModel();
		orderinfo.setId(-1L);
		orderinfo.setOrderno(offLineChargeBean.getOrderId());//由于企业实名认证没有订单号，使用时间戳作为订单号
		orderinfo.setOrderamt(Long.valueOf(offLineChargeBean.getTxnAmt()));
		orderinfo.setOrderfee(0L);
		orderinfo.setOrdercommitime(DateUtil.getCurrentDateTime());
		orderinfo.setFirmemberno(offLineChargeBean.getCoopInsti());
		orderinfo.setFirmembername(coopInstiService.getInstiByInstiCode(offLineChargeBean.getCoopInsti()).getInstiName());
		EnterpriseBean enterprise = enterpriseService.getEnterpriseByMemberId(offLineChargeBean.getMemberId());
		orderinfo.setSecmemberno(offLineChargeBean.getMemberId());//企业会员号
		orderinfo.setSecmembername(enterprise.getEnterpriseName());
		orderinfo.setSecmembershortname(enterprise.getEnterpriseName());
		orderinfo.setBackurl(offLineChargeBean.getBackURL());
		orderinfo.setTxntype(offLineChargeBean.getTxnType());
		orderinfo.setTxnsubtype(offLineChargeBean.getTxnSubType());
		orderinfo.setBiztype(offLineChargeBean.getBizType());
		orderinfo.setPaytimeout(DateUtil.formatDateTime(DateUtil.skipDateTime(new Date(), 7)));
		orderinfo.setTn(OrderNumber.getInstance().generateTN(offLineChargeBean.getCoopInsti()));
		orderinfo.setMemberid(offLineChargeBean.getMemberId());
		orderinfo.setCurrencycode("156");
		orderinfo.setStatus("01");
		txnsOrderinfoDAO.saveA(orderinfo);
		return orderinfo.getTn();
	}
	
	/**
	 * 
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public String createFinancierOrder(FinancierReimbursementBean bean){
		TxnsOrderinfoModel orderinfo = new TxnsOrderinfoModel();
		TradeInfo tradeInfo = new TradeInfo();
		//tradeInfo.setAmount();
		/**支付订单号**/
        String payordno = "";
        /**交易类型**/
        String busiCode = "";
        /**付款方会员ID**/
        String payMemberId = bean.getMemberId();//融资人
        /**收款方会员ID**/
        String payToMemberId =bean.getProductCode();//产品
        /**收款方父级会员ID**/
        String payToParentMemberId="" ;
        /**渠道**/
        String channelId = "";//支付机构代码
        /**产品id**/
        String productId = "";
        /**交易金额**/
        BigDecimal amount = new BigDecimal(Long.valueOf(bean.getTotalAmt()));
        /**佣金**/
        BigDecimal commission = new BigDecimal(0);
        /**手续费**/
        BigDecimal charge = new BigDecimal(0);
        /**金额D**/
        BigDecimal amountD = new BigDecimal(0);
        /**金额E**/
        BigDecimal amountE = new BigDecimal(0);
        
		try {
			accEntryService.accEntryProcess(tradeInfo, EntryEvent.UNKNOW);
			orderinfo.setStatus("00");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			orderinfo.setStatus("03");
		}
		orderinfo.setId(-1L);
		orderinfo.setOrderno(bean.getOrderId());
		orderinfo.setOrderamt(Long.valueOf(bean.getTotalAmt()));
		orderinfo.setOrderfee(0L);
		orderinfo.setOrdercommitime(DateUtil.getCurrentDateTime());
		orderinfo.setFirmemberno(bean.getCoopInsti());
		orderinfo.setFirmembername(coopInstiService.getInstiByInstiCode(bean.getCoopInsti()).getInstiName());
		EnterpriseBean enterprise = enterpriseService.getEnterpriseByMemberId(bean.getMemberId());
		orderinfo.setSecmemberno(bean.getMemberId());//企业会员号
		orderinfo.setSecmembername(enterprise.getEnterpriseName());
		orderinfo.setSecmembershortname(enterprise.getEnterpriseName());
		orderinfo.setBackurl("");
		orderinfo.setTxntype(bean.getTxnType());
		orderinfo.setTxnsubtype(bean.getTxnSubType());
		orderinfo.setBiztype(bean.getBizType());
		orderinfo.setPaytimeout(DateUtil.formatDateTime(DateUtil.skipDateTime(new Date(), 7)));
		orderinfo.setTn(OrderNumber.getInstance().generateTN(bean.getCoopInsti()));
		orderinfo.setMemberid(bean.getMemberId());
		orderinfo.setCurrencycode("156");
		orderinfo.setProductcode(bean.getProductCode());
		txnsOrderinfoDAO.saveA(orderinfo);
		return orderinfo.getTn();
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public String raiseMoneyTransfer(RaiseMoneyTransferBean bean){
		TxnsOrderinfoModel orderinfo = new TxnsOrderinfoModel();
		//保存审核数据
		PojoRaisemoneyApply apply = new PojoRaisemoneyApply();
		apply.setMemberid(bean.getMemberId());
		apply.setFinancingid(bean.getFinancingId());
		apply.setProductcode(bean.getProductCode());
		apply.setOrderid(bean.getOrderId());
		String tn =  OrderNumber.getInstance().generateTN(bean.getCoopInsti());
		apply.setTn(tn);
		apply.setStatus("01");
		apply.setInTime(new Date());
		apply.setInUser(0L);
		raisemoneyApplyDAO.saveA(apply);
		orderinfo.setId(-1L);
		orderinfo.setOrderno(bean.getOrderId());
		orderinfo.setOrderamt(0L);
		orderinfo.setOrderfee(0L);
		orderinfo.setOrdercommitime(DateUtil.getCurrentDateTime());
		orderinfo.setFirmemberno(bean.getCoopInsti());
		orderinfo.setFirmembername(coopInstiService.getInstiByInstiCode(bean.getCoopInsti()).getInstiName());
		EnterpriseBean enterprise = enterpriseService.getEnterpriseByMemberId(bean.getMemberId());
		orderinfo.setSecmemberno(bean.getMemberId());//企业会员号
		orderinfo.setSecmembername(enterprise.getEnterpriseName());
		orderinfo.setSecmembershortname(enterprise.getEnterpriseName());
		orderinfo.setBackurl("");
		orderinfo.setTxntype(bean.getTxnType());
		orderinfo.setTxnsubtype(bean.getTxnSubType());
		orderinfo.setBiztype(bean.getBizType());
		orderinfo.setPaytimeout(DateUtil.formatDateTime(DateUtil.skipDateTime(new Date(), 7)));
		orderinfo.setTn(tn);
		orderinfo.setMemberid(bean.getMemberId());
		orderinfo.setCurrencycode("156");
		orderinfo.setProductcode(bean.getProductCode());
		txnsOrderinfoDAO.saveA(orderinfo);
		return orderinfo.getTn();
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public String merchReimbusement(MerchantReimbursementBean bean) throws Exception{
		
		PojoMerchReimburseBatch merchReimburseBatch = merchReimburseBatchDAO.getBatchInfoByBatchNo(bean.getBatchNo());
		if(merchReimburseBatch!=null){
			throw new Exception("批次号已经存在");
		}
		//检查批次号是否重复
		List<ReimbursementDetailBean> detaiList = bean.getDetaiList();
		int total = 0;
		PojoMerchReimburseDeta deta = null;
		for(ReimbursementDetailBean detailBean : detaiList){
			total++;
			deta = new PojoMerchReimburseDeta();
			deta.setBatchno(bean.getBatchNo());
			deta.setMerId(bean.getMemberId());
			deta.setInvestors(detailBean.getInvestors());
			deta.setPorductCode(bean.getProductCode());
			deta.setInterset(new BigDecimal(Long.valueOf(detailBean.getInterest())));
			deta.setTxnamt(new BigDecimal(Long.valueOf(detailBean.getTxnAmt())));
			deta.setStatus("01");
			deta.setIntime(new Date());
			deta.setInuser(0L);
			merchReimburseDetaDAO.saveA(deta);
		}
		
		PojoMerchReimburseBatch batch = new PojoMerchReimburseBatch();
		batch.setBatchNo(bean.getBatchNo());
		batch.setMerId(bean.getMemberId());
		batch.setProdcutcode(bean.getProductCode());
		batch.setTotalAmt(new BigDecimal(Long.valueOf(bean.getTotalAmt())));
		batch.setTotalQty(total);
		batch.setTxnTime(DateUtil.getCurrentDateTime());
		batch.setStatus("01");
		batch.setType("01");
		batch.setIntime(new Date());
		batch.setInuser(0L);
		batch.setTn(OrderNumber.getInstance().generateTN(bean.getMemberId()));
		merchReimburseBatchDAO.saveA(batch);
		return batch.getTn();
	}
}
