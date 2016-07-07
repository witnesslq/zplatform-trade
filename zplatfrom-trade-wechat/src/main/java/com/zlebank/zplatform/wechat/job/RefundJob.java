package com.zlebank.zplatform.wechat.job;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.wechat.service.WeChatService;
@Service("refundJob")
public class RefundJob {
	private static final Log log = LogFactory.getLog(RefundJob.class);
	@Autowired
	private WeChatService weChatService;
	/***
	 * 跑批-查询支付的退款
	 * @throws IOException
	 */
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void execute(){
		log.info("start RefundJob");
		weChatService.dealRefundBatch();
		log.info("end RefundJob");
		
	}
	
}
