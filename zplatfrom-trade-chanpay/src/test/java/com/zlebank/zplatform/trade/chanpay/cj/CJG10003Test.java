/* 
 * CJG10003Test.java  
 * 
 * version TODO
 *
 * 2016年5月19日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.chanpay.cj;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zlebank.zplatform.trade.chanpay.bean.cj.G10003Bean;
import com.zlebank.zplatform.trade.chanpay.service.impl.CjMsgSendService;
import com.zlebank.zplatform.trade.chanpay.utils.Cj;
import com.zlebank.zplatform.trade.chanpay.utils.CjSignHelper;
import com.zlebank.zplatform.trade.chanpay.utils.CjSignHelper.VerifyResult;
import com.zlebank.zplatform.trade.chanpay.utils.U;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月19日 下午5:24:45
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/*.xml")
public class CJG10003Test {
	public static final Log LOG = LogFactory.getLog(CJG10001Test.class);
	@Autowired
	private CjMsgSendService cjMsgSendService;
	
	@Test
	public void sendMessage() {
		G10003Bean data = new G10003Bean();
		data.setReqSn(U.createUUID());
		data.setMertid("5ee26ddb404a590");
		data.setBusinessCode("05101");
		data.setCorpAccNo("201407301524");
		data.setProductCode("50010001");
		data.setSubMertid("YS0001");
		data.setBankGeneralName("中国工商银行");
		data.setAccountNo("201407301524");
		data.setAccountName("畅捷通信息技术股份有限公司");
		data.setProvince("北京");
		data.setCity("北京");
		data.setBankName("中国工商银行北京海淀中关村分理处");
		data.setBankCode("710584000001");
		data.setDrctBankCode("710584000001");
		data.setProtocolNo("201407301524");
		data.setCurrency("CNY");
		data.setAmount(50L);
		data.setIdType("0");
		data.setId("1234567890");
		data.setTel("1234567890");
		data.setCorpFlowNo("1234567890");
		data.setSummary("测试DEMO");
		data.setPostscript("测试DEMO");
		data.setAccountProp("0");
		data.setAccountType("00");
		data.setTimeliness("0");
		data.setTotalCnt(2);
		data.setTotalAmt(100);
		buildCjmsgAndSend(data);
	}//method
	
	/** 组织Cj报文，并发送 */
	private void buildCjmsgAndSend(G10003Bean data) {
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
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}//method
	
	private void parseCjMsgToDto(String cjRespmsg, G10003Bean data) throws Exception {
		Document reqDoc = DocumentHelper.parseText(cjRespmsg);

		Element msgEl = reqDoc.getRootElement();
		Element infoEl = msgEl.element("INFO");

		data.setRetCode(infoEl.elementText("RET_CODE"));
		data.setErrMsg(infoEl.elementText("ERR_MSG"));
		data.setTimestamp(infoEl.elementText("TIMESTAMP"));
		LOG.info("响应信息：retcode[" + data.getRetCode() + "], errmsg[" + data.getErrMsg() + "]");
	}//method

	public String buildCjmsg(G10003Bean data) {
		Document doc = DocumentHelper.createDocument();
		Element msgEl = doc.addElement("MESSAGE");

		Element infoEl = msgEl.addElement("INFO");
		infoEl.addElement("TRX_CODE").setText(Cj.XMLMSG_TRANS_CODE_批量收款);
		infoEl.addElement("VERSION").setText(Cj.XMLMSG_VERSION_01);
		infoEl.addElement("MERCHANT_ID").setText(U.nvl(data.getMertid()));
		infoEl.addElement("REQ_SN").setText(U.nvl(data.getReqSn()));
		infoEl.addElement("TIMESTAMP").setText(U.getCurrentTimestamp());
		infoEl.addElement("SIGNED_MSG").setText("");

		Element bodyEl = msgEl.addElement("BODY");

		Element batch = bodyEl.addElement("BATCH");
		batch.addElement("BUSINESS_CODE").setText(U.nvl(data.getBusinessCode()));
		batch.addElement("CORP_ACCT_NO").setText(U.nvl(data.getCorpAccNo()));
		batch.addElement("PRODUCT_CODE").setText(U.nvl(data.getProductCode()));
		batch.addElement("ACCOUNT_PROP").setText(U.nvl(data.getAccountProp()));
		batch.addElement("SUB_MERCHANT_ID").setText(U.nvl(data.getSubMertid()));
		batch.addElement("TIMELINESS").setText(U.nvl(data.getTimeliness()));
		batch.addElement("APPOINTMENT_TIME").setText(U.nvl(data.getAppointmentTime()));
		batch.addElement("TOTAL_CNT").setText(U.nvl(data.getTotalCnt()));
		batch.addElement("TOTAL_AMT").setText(U.nvl(data.getTotalAmt()));

		Element detailTitle = bodyEl.addElement("TRANS_DETAILS");
		for (int i = 0; i < data.getTotalCnt(); i++) {
			Element dtl = detailTitle.addElement("DTL");
			dtl.addElement("SN").setText(U.createUUID());
			dtl.addElement("BANK_GENERAL_NAME").setText(U.nvl(data.getBankGeneralName()));
			dtl.addElement("ACCOUNT_TYPE").setText(U.nvl(data.getAccountType()));
			dtl.addElement("ACCOUNT_NO").setText(U.nvl(data.getAccountNo()));
			dtl.addElement("ACCOUNT_NAME").setText(U.nvl(data.getAccountName()));
			dtl.addElement("PROVINCE").setText(U.nvl(data.getProvince()));
			dtl.addElement("CITY").setText(U.nvl(data.getCity()));
			dtl.addElement("BANK_NAME").setText(U.nvl(data.getBankName()));
			dtl.addElement("BANK_CODE").setText(U.nvl(data.getBankCode()));
			dtl.addElement("DRCT_BANK_CODE").setText(U.nvl(data.getDrctBankCode()));
			dtl.addElement("CURRENCY").setText(U.nvl(data.getCurrency()));
			dtl.addElement("AMOUNT").setText(U.nvl(data.getAmount()));
			dtl.addElement("PROTOCOL_NO").setText(U.nvl(data.getProtocolNo()));
			dtl.addElement("ID_TYPE").setText(U.nvl(data.getIdType()));
			dtl.addElement("ID").setText(U.nvl(data.getId()));
			dtl.addElement("TEL").setText(U.nvl(data.getTel()));
			dtl.addElement("CORP_FLOW_NO").setText(U.nvl(data.getCorpFlowNo()));
			dtl.addElement("SUMMARY").setText(U.nvl(data.getSummary()));
			dtl.addElement("POSTSCRIPT").setText(U.nvl(data.getPostscript()));
		}//for

		String xml = Cj.formatXml_UTF8(doc);
		LOG.info("产生G10003批量收款：" + U.substringByByte(xml, 512));
		return xml;
	}//method
}
