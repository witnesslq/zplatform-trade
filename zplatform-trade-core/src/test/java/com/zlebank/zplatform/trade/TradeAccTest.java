/* 
 * TradeAccTest.java  
 * 
 * version TODO
 *
 * 2016年9月2日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zlebank.zplatform.commons.dao.pojo.BusiTypeEnum;
import com.zlebank.zplatform.trade.adapter.accounting.IAccounting;
import com.zlebank.zplatform.trade.factory.AccountingAdapterFactory;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月2日 下午3:54:26
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration("/AccountContextTest.xml") 
public class TradeAccTest {

	@Test
	public void test(){
		
		IAccounting accounting = AccountingAdapterFactory.getInstance().getAccounting(BusiTypeEnum.fromValue("1000"));
        accounting.accountedFor("1609029900057539");
	}
	
}
