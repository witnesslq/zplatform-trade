/* 
 * BindCardTest.java  
 * 
 * version TODO
 *
 * 2016年5月30日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
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
 * @date 2016年5月30日 下午3:43:55
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("/*.xml")
public class BindCardTest {
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	
	@Before
	public void setup() throws Exception {
		mockMvc = webAppContextSetup(this.wac).build();
		
	}
	
	@Test
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void test_bindcard(){
		
		/*String bindCardId = "";
		String amount = "";
		String merchId = "";
		String subMerchId = "";
		String busicode="";
		String cardNo = "";
		String txnseqno_ ="1605309900059164";*/
		
		String bankCode="0103";
		String orderId ="2016053017468037";
		String amount="1";
		String txnseqno="1605309900052532";
		String merchId="300000000000014";
		String busicode="10000001";
		String goodsName="iphone";
		String merUserId="100000000000576";
		String busitype="1000";
		String memberIP="192.168.13.73";
		String tn="160530001400051672";
		String cardNo = "6228480018543668976";
		String acctName = "郭佳";
		String certId = "110105198710094112";
		String mobile = "18600806796";
		String cardType="1";
		
		
		try {
			this.mockMvc.perform(post("/gateway/toPay.htm").param("bankCode", bankCode)
					.param("amount", amount)
					.param("merchId", merchId)
					.param("busicode",busicode)
					.param("cardNo",cardNo)
					.param("txnseqno_",txnseqno)
					.param("txnseqno",txnseqno)
					.param("orderId",orderId)
					.param("goodsName",goodsName)
					.param("merUserId",merUserId)
					.param("busitype",busitype)
					.param("memberIP",memberIP)
					.param("tn",tn)
					.param("acctName",acctName)
					.param("certId",certId)
					.param("mobile",mobile)
					.param("cardType",cardType)
					).andExpect(status().is2xxSuccessful()).andDo(print());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
