/* 
 * CJG20004Test.java  
 * 
 * version TODO
 *
 * 2016年5月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.chanpay.cj;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.zlebank.zplatform.trade.chanpay.bean.cj.G20004Bean;
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
 * @date 2016年5月20日 下午1:15:11
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/*.xml")
public class CJG20004Test {
	
	public static final Log LOG = LogFactory.getLog(CJG10001Test.class);
	@Autowired
	private CjMsgSendService cjMsgSendService;
	
	@Test
	public void sendMessage() {
		G20004Bean data = new G20004Bean();
		data.setReqSn(U.createUUID());
		data.setMertid("5ee26ddb404a590");
		data.setAccountNo("201407301524");
		data.setStartDay("05/18/2016 13:13:42");
		data.setEndDay("05/20/2016 13:13:42");
		data.setStatus("0");
		data.setQryPage(1);
		buildCjmsgAndSend(data);
		LOG.info("trade data:"+data.getRetDate());
	}//method
	
	/** 组织Cj报文，并发送 */
	private void buildCjmsgAndSend(G20004Bean data) {
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
	
	private void parseCjMsgToDto(String cjRespmsg, G20004Bean data) throws Exception {
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
		
		Element pageEl = bodyEl.element("PAGE");
		if(null!=pageEl){
			data.setTtlPage(pageEl.elementText("TTL_PAGE"));
			data.setTtlCnt(pageEl.elementText("TTL_CNT"));
			data.setCurrPage(pageEl.elementText("CURR_PAGE"));
			data.setCurrCnt(pageEl.elementText("CURR_CNT"));
		}
		
		data.setRetDate(bodyEl.elementText("RET_DATA"));
		
	}//method
	
	public String buildCjmsg(G20004Bean data) throws ParseException{
		Document doc = DocumentHelper.createDocument();
		Element msgEl = doc.addElement("MESSAGE");

		Element infoEl = msgEl.addElement("INFO");
		infoEl.addElement("TRX_CODE").setText(Cj.XMLMSG_TRANS_CODE_交易明细查询);
		infoEl.addElement("VERSION").setText(Cj.XMLMSG_VERSION_01);
		infoEl.addElement("MERCHANT_ID").setText(U.nvl(data.getMertid()));
		infoEl.addElement("REQ_SN").setText(U.nvl(data.getReqSn()));
		infoEl.addElement("TIMESTAMP").setText(U.getCurrentTimestamp());
		infoEl.addElement("SIGNED_MSG").setText("");

		Element bodyEl = msgEl.addElement("BODY");

		bodyEl.addElement("ACCOUNT_NO").setText(U.nvl(data.getAccountNo()));
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		String stime = data.getStartDay();
		Date sdate = sdf.parse(stime);		
		DateFormat dateFormat =new SimpleDateFormat("yyyyMMddHHmmss");
		String startTime= dateFormat.format(sdate);
		bodyEl.addElement("START_DAY").setText(startTime);
		String etime = data.getEndDay();
		Date edate = sdf.parse(etime);
		String endTime = dateFormat.format(edate);
		bodyEl.addElement("END_DAY").setText(endTime);
		bodyEl.addElement("STATUS").setText(U.nvl(data.getStatus()));
		bodyEl.addElement("QRY_PAGE").setText(U.nvl(data.getQryPage()));				
		
		String xml = Cj.formatXml_UTF8(doc);
		LOG.info("产生G20004交易明细查询：" + U.substringByByte(xml, 1024));
		return xml;
	}
	
}
