/* 
 * RefundService.java  
 * 
 * version TODO
 *
 * 2016年5月23日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.zlebank.zplatform.trade.service.RefundService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月23日 上午9:32:32
 * @since
 */
public class RefundServiceTest {
	RefundService refundService;

	@Before
	public void init() {
		ApplicationContext context = ApplicationContextUtil.get();
		refundService = (RefundService) context.getBean("refundService");
	}

	@Test
	public void test_refund() {
		String refundOrderNo = "1604079300000102";
		String merchNo = "200000000000587";
		refundService.execute(refundOrderNo, merchNo);
	}

}
