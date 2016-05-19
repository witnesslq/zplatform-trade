package com.zlebank.zplatform.trade.chanpay.datahandl;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.zlebank.zplatform.trade.chanpay.datahandl.CjMsgSendService.sendAndGetBytes_Response;
import com.zlebank.zplatform.trade.chanpay.dto.G40004Dto_DownloadExtTrxBill;
import com.zlebank.zplatform.trade.chanpay.utils.Cj;
import com.zlebank.zplatform.trade.chanpay.utils.CjSignHelper;
import com.zlebank.zplatform.trade.chanpay.utils.ServiceLocation;
import com.zlebank.zplatform.trade.chanpay.utils.U;

/**
 * 特色对账文件下载 = "G40004";
 * @author SongCheng
 *
 */
public class G40004_DownloadExtTrxBill {
	public static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(G40004_DownloadExtTrxBill.class);

	private com.zlebank.zplatform.trade.chanpay.service.impl.CjMsgSendService cjMsgSendService = null;

	public G40004_DownloadExtTrxBill() {
		cjMsgSendService = ServiceLocation.getCjMsgSendService();
	}//constructor

	public void do_特色对账文件下载(G40004Dto_DownloadExtTrxBill data) {
		buildCjmsgAndSend(data);
	}//method

	/** 组织Cj报文，并发送 */
	private G40004Dto_DownloadExtTrxBill buildCjmsgAndSend(G40004Dto_DownloadExtTrxBill data) {
		try {
			String cjReqmsg = buildCjmsg(data);

			CjSignHelper singHelper = new CjSignHelper();
			String signMsg = singHelper.signXml$Add(cjReqmsg);

			com.zlebank.zplatform.trade.chanpay.service.impl.CjMsgSendService.sendAndGetBytes_Response cjRespmsg = cjMsgSendService.sendAndGetBytes(signMsg);
			LOG.info("响应信息：retcode[" + cjRespmsg.retcode + "], errmsg[" + cjRespmsg.errmsg + "], contentDisposition[" + cjRespmsg.contentDisposition
					+ "], data[" + cjRespmsg.data + "]");

			data.setRetCode(cjRespmsg.retcode);
			data.setErrMsg(cjRespmsg.errmsg);
			data.contentType = cjRespmsg.contentType;
			data.contentDisposition = cjRespmsg.contentDisposition;
			data.billData = cjRespmsg.data;

			return data;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}//method

	public String buildCjmsg(G40004Dto_DownloadExtTrxBill data) {
		Document doc = DocumentHelper.createDocument();
		Element msgEl = doc.addElement("MESSAGE");

		Element infoEl = msgEl.addElement("INFO");
		infoEl.addElement("TRX_CODE").setText(Cj.XMLMSG_TRANS_CODE_特色对账文件下载);
		infoEl.addElement("VERSION").setText(Cj.XMLMSG_VERSION_01);
		infoEl.addElement("MERCHANT_ID").setText(U.nvl(data.getMertid()));
		infoEl.addElement("REQ_SN").setText(U.createUUID());
		infoEl.addElement("TIMESTAMP").setText(Cj.currTransDatetime_string());
		infoEl.addElement("SIGNED_MSG").setText("");

		Element bodyEl = msgEl.addElement("BODY");
		bodyEl.addElement("BILL_TYPE").setText(U.nvl(Cj.ExtBillFileType_每日特色交易对账文件));
		bodyEl.addElement("BILL_DAY").setText(U.nvl(data.billDay));

		String xml = Cj.formatXml_UTF8(doc);
		LOG.info("产生G40004特色对账文件下载：" + U.substringByByte(xml, 512));
		return xml;
	}

}//class
