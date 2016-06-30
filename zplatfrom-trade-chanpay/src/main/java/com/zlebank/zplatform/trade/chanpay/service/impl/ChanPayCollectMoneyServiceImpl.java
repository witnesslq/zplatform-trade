/* 
 * ChanPayCollectMoneyServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年6月29日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.chanpay.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.chanpay.bean.cj.G60001Bean;
import com.zlebank.zplatform.trade.chanpay.bean.cj.G60002Bean;
import com.zlebank.zplatform.trade.chanpay.service.ChanPayCollectMoneyService;
import com.zlebank.zplatform.trade.chanpay.utils.Cj;
import com.zlebank.zplatform.trade.chanpay.utils.CjSignHelper;
import com.zlebank.zplatform.trade.chanpay.utils.CjSignHelper.VerifyResult;
import com.zlebank.zplatform.trade.chanpay.utils.U;
import com.zlebank.zplatform.trade.service.IRouteConfigService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年6月29日 下午3:28:45
 * @since 
 */
@Service("chanPayCollectMoneyService")
public class ChanPayCollectMoneyServiceImpl implements ChanPayCollectMoneyService{

	
	private static final Log log = LogFactory.getLog(ChanPayCollectMoneyServiceImpl.class);
	@Autowired
	private IRouteConfigService routeConfigService;
	@Autowired
	private CjMsgSendService cjMsgSendService;
	/**
	 *
	 * @param tradeBean
	 * @return
	 * @throws InterruptedException 
	 */
	@Override
	public ResultBean realNameAuth(TradeBean tradeBean) throws InterruptedException {
		ResultBean resultBean = null;
		String bankNumber = routeConfigService.getCardPBCCode(tradeBean.getCardNo()).get("PBC_BANKCODE")+"";
		String subBankName = routeConfigService.getCardPBCCode(tradeBean.getCardNo()).get("BANKNAME")+"";
		String bankName = routeConfigService.getBankName(bankNumber);
		if(StringUtil.isEmpty(bankNumber)){
			resultBean = new ResultBean("T000", "无法获取有效的人行联行号");
			return resultBean;
		}
		G60001Bean data = new G60001Bean();
		data.setReqSn(U.createUUID());
		data.setMertid("cp2016011227674");
		data.setBankGeneralName(bankName);
		data.setSn(U.createUUID());
		data.setBankName(subBankName);
		data.setBankCode(bankNumber);
		data.setAccountType("00");
		data.setAccountNo(tradeBean.getCardNo());
		data.setAccountName(tradeBean.getAcctName());
		data.setIdType("0");
		data.setId(tradeBean.getCertId());
		data.setTel(tradeBean.getMobile());
		buildCjmsgAndSend(data);
		
		if("0000".equals(data.getRetCode())){
			for(int i = 0;i<5;i++){
				ResultBean queryResultBean = queryRealNameAuth(data.getReqSn());
				
				Thread.sleep(1000);
			}
		}
		
		
		return null;
	}
	
	/** 组织Cj报文，并发送 */
	private void buildCjmsgAndSend(G60001Bean data) {
		try {
			String cjReqmsg = buildCjmsg(data);
			// 签名
			CjSignHelper singHelper = new CjSignHelper();
			String signMsg = singHelper.signXml$Add(cjReqmsg);
			// 发送报文
			String cjRespmsg = cjMsgSendService.sendAndGetString(signMsg);
			// 验证签名
			VerifyResult verifyResult = singHelper.verifyCjServerXml(cjRespmsg);
			if (!verifyResult.result) {
				throw new Exception("验证CJ返回数据的签名失败！" + verifyResult.errMsg);
			}
			parseCjMsgToDto(cjRespmsg, data);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	private void parseCjMsgToDto(String cjRespmsg, G60001Bean data) throws Exception {
		Document reqDoc = DocumentHelper.parseText(cjRespmsg);
		Element msgEl = reqDoc.getRootElement();
		Element infoEl = msgEl.element("INFO");
		data.setRetCode(infoEl.elementText("RET_CODE"));
		data.setErrMsg(infoEl.elementText("ERR_MSG"));
		data.setTimestamp(infoEl.elementText("TIMESTAMP"));
		log.info("响应信息：retcode[" + data.getRetCode() + "], errmsg[" + data.getErrMsg() + "]");
	}//method
	
	public String buildCjmsg(G60001Bean data){
		Document doc = DocumentHelper.createDocument();
		Element msgEl = doc.addElement("MESSAGE");

		Element infoEl = msgEl.addElement("INFO");
		infoEl.addElement("TRX_CODE").setText(Cj.XMLMSG_TRANS_CODE_实名认证);
		infoEl.addElement("VERSION").setText(Cj.XMLMSG_VERSION_01);
		infoEl.addElement("MERCHANT_ID").setText(U.nvl(data.getMertid()));
		infoEl.addElement("REQ_SN").setText(U.nvl(data.getReqSn()));
		infoEl.addElement("TIMESTAMP").setText(U.getCurrentTimestamp());
		infoEl.addElement("SIGNED_MSG").setText("");

		Element bodyEl = msgEl.addElement("BODY");
		Element batch = bodyEl.addElement("BATCH");
		batch.addElement("VALIDATE_MODE").setText("V002");
		
		Element details = bodyEl.addElement("TRANS_DETAILS");
		Element dtl = details.addElement("DTL");
		dtl.addElement("SN").setText(U.createUUID());
		dtl.addElement("BANK_GENERAL_NAME").setText(U.nvl(data.getBankGeneralName()));
		dtl.addElement("BANK_NAME").setText(U.nvl(data.getBankName()));
		dtl.addElement("BANK_CODE").setText(U.nvl(data.getBankCode()));
		dtl.addElement("ACCOUNT_TYPE").setText(U.nvl(data.getAccountType()));
		dtl.addElement("ACCOUNT_NO").setText(U.nvl(data.getAccountNo()));
		dtl.addElement("ACCOUNT_NAME").setText(U.nvl(data.getAccountName()));
		dtl.addElement("ID_TYPE").setText(U.nvl(data.getIdType()));
		dtl.addElement("ID").setText(U.nvl(data.getId()));
		dtl.addElement("TEL").setText(U.nvl(data.getTel()));			
		
		String xml = Cj.formatXml_UTF8(doc);
		log.info("产生G60001实名认证：" + U.substringByByte(xml, 1024));
		return xml;
	}

	/**
	 *
	 * @param qry_req_sn
	 * @return
	 */
	@Override
	public ResultBean queryRealNameAuth(String qry_req_sn) {
		G60002Bean data = new G60002Bean();
		data.setReqSn(U.createUUID());
		data.setMertid("cp2016011227674");
		data.setQryReqSn(qry_req_sn);
		buildCjmsgAndSend(data);
		
		
		return null;
	}
	private void buildCjmsgAndSend(G60002Bean data) {
		try {
			String cjReqmsg = buildCjmsg(data);

			// 签名
			CjSignHelper singHelper = new CjSignHelper();
			String signMsg = singHelper.signXml$Add(cjReqmsg);
			// 发送报文
			String cjRespmsg = cjMsgSendService.sendAndGetString(signMsg);
			// 验证签名
			VerifyResult verifyResult = singHelper.verifyCjServerXml(cjRespmsg);
			if (!verifyResult.result) {
				throw new Exception("验证CJ返回数据的签名失败！" + verifyResult.errMsg);
			}
			parseCjMsgToDto(cjRespmsg, data);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	private void parseCjMsgToDto(String cjRespmsg, G60002Bean data) throws Exception {
		Document reqDoc = DocumentHelper.parseText(cjRespmsg);

		Element msgEl = reqDoc.getRootElement();
		Element infoEl = msgEl.element("INFO");

		data.setRetCode(infoEl.elementText("RET_CODE"));
		data.setErrMsg(infoEl.elementText("ERR_MSG"));
		data.setTimestamp(infoEl.elementText("TIMESTAMP"));
		log.info("响应信息：retcode[" + data.getRetCode() + "], errmsg[" + data.getErrMsg() + "]");
		
		Element bodyEl = msgEl.element("BODY");
		if (bodyEl == null) {
			return;
		}
		Element batchEl = bodyEl.element("BATCH");
		if(batchEl == null){
			return;
		}
		data.setBatchRryReqSn(batchEl.elementText("QRY_REQ_SN"));
		data.setBatchRetCode(batchEl.elementText("RET_CODE"));
		data.setBatchErrMsg(batchEl.elementText("ERR_MSG"));
		
		Element transEl = bodyEl.element("TRANS_DETAILS");
		if(transEl == null){
			return;
		}
		Element dtlEl = transEl.element("DTL");
		if(dtlEl == null){
			return;
		}
		data.setDtlsn(dtlEl.elementText("SN"));
		data.setDtlRetCode(dtlEl.elementText("RET_CODE"));
		data.setDtlErrMsg(dtlEl.elementText("ERR_MSG"));
		data.setDtlaccountNo(dtlEl.elementText("ACCOUNT_NO"));
		data.setDtlaccountName(dtlEl.elementText("ACCOUNT_NAME"));
		
	}//method
	
	public String buildCjmsg(G60002Bean data){
		Document doc = DocumentHelper.createDocument();
		Element msgEl = doc.addElement("MESSAGE");

		Element infoEl = msgEl.addElement("INFO");
		infoEl.addElement("TRX_CODE").setText(Cj.XMLMSG_TRANS_CODE_实名认证结果查询);
		infoEl.addElement("VERSION").setText(Cj.XMLMSG_VERSION_01);
		infoEl.addElement("MERCHANT_ID").setText(U.nvl(data.getMertid()));
		infoEl.addElement("REQ_SN").setText(U.nvl(data.getReqSn()));
		infoEl.addElement("TIMESTAMP").setText(U.getCurrentTimestamp());
		infoEl.addElement("SIGNED_MSG").setText("");

		Element bodyEl = msgEl.addElement("BODY");
		bodyEl.addElement("QRY_REQ_SN").setText(U.nvl(data.getQryReqSn()));		
		
		String xml = Cj.formatXml_UTF8(doc);
		log.info("产生G60002实名认证结果查询：" + U.substringByByte(xml, 1024));
		return xml;
	}

	/**
	 *
	 * @param tradeBean
	 * @return
	 */
	@Override
	public ResultBean protocolSign(TradeBean tradeBean) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 * @param qry_req_sn
	 * @return
	 */
	@Override
	public ResultBean queryProtocolSign(String qry_req_sn) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 * @param tradeBean
	 * @return
	 */
	@Override
	public ResultBean collectMoney(TradeBean tradeBean) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 * @param qry_req_sn
	 * @return
	 */
	@Override
	public ResultBean queryCollectMoney(String qry_req_sn) {
		// TODO Auto-generated method stub
		return null;
	}

}
