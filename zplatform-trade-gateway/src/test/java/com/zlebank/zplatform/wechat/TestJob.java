/* 
 * TestWX.java  
 * 
 * version TODO
 *
 * 2016年5月17日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.member.service.CoopInstiService;
import com.zlebank.zplatform.member.service.MerchMKService;
import com.zlebank.zplatform.trade.dao.ITxnsOrderinfoDAO;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.ITxnsRefundService;
import com.zlebank.zplatform.wechat.job.RefundJob;
import com.zlebank.zplatform.wechat.service.WeChatService;

/**
 * 微信测试类
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年5月17日 下午3:08:19
 * @since 
 */
public class TestJob {

    private static final Log log = LogFactory.getLog(TestJob.class);
    private ApplicationContext context;
    private RefundJob refundJob;

    @Before
    public void init(){
        context = new ClassPathXmlApplicationContext("ContextTest.xml");
        refundJob = (RefundJob) context.getBean("refundJob");
    }
    @Test
    public void test(){
    	try {
    		refundJob.doRefundQueryByCharege();
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    }
    
}