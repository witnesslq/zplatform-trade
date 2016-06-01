/* 
 * BindPayTest.java  
 * 
 * version TODO
 *
 * 2016年5月30日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月30日 下午5:36:38
 * @since
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("/*.xml")
public class BindPayTest {
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	
	@Before
	public void setup() throws Exception {
		mockMvc = webAppContextSetup(this.wac).build();
		
	}
	
	@Test
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void test_bindpay(){
		String payFlag="bindingpay";
		String orderId="2016053017468037" ;
		String amount="1" ;
		String txnseqno="1605309900052532" ;
		String merchId="300000000000014" ;
		String cashCode="ZLC00001" ;
		String busicode="10000001" ;
		String goodsName="iphone" ;
		String merchName="测试机构11" ;
		String subMerName="暴走大事件股份有限公司" ;
		String merUserId="100000000000576" ;
		String busitype="1000";
		String memberIP="192.168.13.73";
		String tn="160530001400051672";
		
		String bankCode = "0103";
		String bindCardId = "113";
		
		try {
			this.mockMvc.perform(post("/gateway/bindPay.htm").param("bankCode", bankCode)
					.param("amount", amount)
					.param("merchId", merchId)
					.param("busicode",busicode)
					.param("txnseqno_",txnseqno)
					.param("txnseqno",txnseqno)
					.param("orderId",orderId)
					.param("goodsName",goodsName)
					.param("merUserId",merUserId)
					.param("busitype",busitype)
					.param("memberIP",memberIP)
					.param("tn",tn)
					.param("payFlag",payFlag)
					.param("cashCode",cashCode)
					.param("merchName",merchName)
					.param("subMerName",subMerName)
					.param("bindCardId",bindCardId)
					).andExpect(status().is2xxSuccessful()).andDo(print());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
