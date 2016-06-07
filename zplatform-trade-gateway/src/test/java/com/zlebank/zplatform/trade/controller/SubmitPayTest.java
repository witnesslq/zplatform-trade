/* 
 * SubmitPayTest.java  
 * 
 * version TODO
 *
 * 2016年5月31日 
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
 * @date 2016年5月31日 上午9:33:18
 * @since
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("/*.xml")
public class SubmitPayTest {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		mockMvc = webAppContextSetup(this.wac).build();

	}

	@Test
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void test_submitpay() {
		String identifyingCode = "123456";
		String bankCode = "0103";
		String orderId = "2016053110999585";
		String cardNo = "6228480018543668976";
		String acctName = "郭佳";
		String amount = "1455";
		String mobile = "18600806796";
		String certId = "110105198610094112";
		String certType = "00";
		String tradeType = "01";
		String merchId = "300000000000014";
		String txnseqno = "1605319900052552";
		String cashCode = "ZLC00001";
		String busicode = "10000001";
		String reaPayOrderNo = "1605319600002658";
		String merUserId = "100000000000576";
		String busitype = "1000";
		String bindCardId = "124";
		String cardId = "124";
		String tn = "160531001400051692";
		String cardType = "";

		try {
			this.mockMvc
					.perform(
							post("/gateway/toBankPay.htm")
									.param("identifyingCode", identifyingCode)
									.param("bankCode", bankCode)
									.param("orderId", orderId)
									.param("cardNo", cardNo)
									.param("acctName", acctName)
									.param("amount", amount)
									.param("mobile", mobile)
									.param("certId", certId)
									.param("certType", certType)
									.param("tradeType", tradeType)
									.param("merchId", merchId)
									.param("txnseqno", txnseqno)
									.param("txnseqno_", txnseqno)
									.param("cashCode", cashCode)
									.param("busicode", busicode)
									.param("reaPayOrderNo", reaPayOrderNo)
									.param("merUserId", merUserId)
									.param("busitype", busitype)
									.param("bindCardId", bindCardId)
									.param("cardId", cardId)
									.param("tn", tn)
									.param("cardType", cardType)

					).andExpect(status().is2xxSuccessful()).andDo(print());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
