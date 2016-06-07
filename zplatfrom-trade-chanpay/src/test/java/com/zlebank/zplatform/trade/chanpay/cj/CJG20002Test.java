/* 
 * CJG20002Test.java  
 * 
 * version TODO
 *
 * 2016年5月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.chanpay.cj;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.trade.chanpay.bean.cj.G20002Bean;
import com.zlebank.zplatform.trade.chanpay.bean.cj.G20002SubBean;
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
 * @date 2016年5月20日 下午1:03:48
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/*.xml")
public class CJG20002Test {
	public static final Log LOG = LogFactory.getLog(CJG10001Test.class);
	@Autowired
	private CjMsgSendService cjMsgSendService;
	
	@Test
	public void sendMessage() {
		G20002Bean data = new G20002Bean();
		data.setMertid("5ee26ddb404a590");
		data.setQryReqSn("2859b8456c3b4970a5c88df87c66b805");
		data.setReqSn(U.createUUID());
		buildCjmsgAndSend(data);
		LOG.info("receive Data:"+JSON.toJSONString(data.getList()));
	}//method
	
	/** 组织Cj报文，并发送 */
	private void buildCjmsgAndSend(G20002Bean data) {
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
	
	private void parseCjMsgToDto(String cjRespmsg, G20002Bean data) throws Exception {
		Document reqDoc = DocumentHelper.parseText(cjRespmsg);

		Element msgEl = reqDoc.getRootElement();
		Element infoEl = msgEl.element("INFO");

		data.setRetCode(infoEl.elementText("RET_CODE"));
		data.setErrMsg(infoEl.elementText("ERR_MSG"));
		data.setTimestamp(infoEl.elementText("TIMESTAMP"));
		LOG.info("响应信息：retcode[" + data.getRetCode() + "], errmsg[" + data.getErrMsg() + "]");
		
		Element bodyEl = msgEl.element("BODY");
		if (bodyEl == null) {
			return;
		}
		List<Element> detailsEl = bodyEl.element("TRANS_DETAILS").elements("DTL");
		List<G20002SubBean> list = new ArrayList<G20002SubBean>();
		for (Element element : detailsEl) {
			G20002SubBean sub = new G20002SubBean();
			sub.setSn(element.elementText("SN"));
			sub.setCharge(StringUtils.isBlank(element.elementText("CHARGE"))?0:Long.parseLong(element.elementText("CHARGE")));
			sub.setPayAcctNo(element.elementText("PAY_ACCT_NO"));
			sub.setPayAcctName(element.elementText("PAY_ACCT_NAME"));
			sub.setRetAcctNo(element.elementText("REC_ACCT_NO"));
			sub.setRetAcctName(element.elementText("REC_ACCT_NAME"));
			sub.setAmount(StringUtils.isBlank(element.elementText("AMOUNT"))?0:Long.parseLong(element.elementText("AMOUNT")));
			sub.setSummary(element.elementText("SUMMARY"));
			sub.setCorpFlowNo(element.elementText("CORP_FLOW_NO"));
			sub.setPostscript(element.elementText("POSTSCRIPT"));
			list.add(sub);
		}
		data.setList(list);
	}//method

	public String buildCjmsg(G20002Bean data) {
		Document doc = DocumentHelper.createDocument();
		Element msgEl = doc.addElement("MESSAGE");

		Element infoEl = msgEl.addElement("INFO");
		infoEl.addElement("TRX_CODE").setText(Cj.XMLMSG_TRANS_CODE_批量交易结果查询);
		infoEl.addElement("VERSION").setText(Cj.XMLMSG_VERSION_01);
		infoEl.addElement("MERCHANT_ID").setText(U.nvl(data.getMertid()));
		infoEl.addElement("REQ_SN").setText(U.nvl(data.getReqSn()));
		infoEl.addElement("TIMESTAMP").setText(U.getCurrentTimestamp());
		infoEl.addElement("SIGNED_MSG").setText("");

		Element bodyEl = msgEl.addElement("BODY");
		bodyEl.addElement("QRY_REQ_SN").setText(U.nvl(data.getQryReqSn()));

		if (StringUtils.isNotBlank(data.getSn())) {
			Element dtls = bodyEl.addElement("TRANS_DETAILS");
			String[] snArray = data.getSn().split(",");
			for (String sn : snArray) {
				dtls.addElement("SN").setText(sn);
			}
		}//if
		
		String xml = Cj.formatXml_UTF8(doc);
		LOG.info("产生G20002文件上传：" + U.substringByByte(xml, 512));
		return xml;
	}//method
}
