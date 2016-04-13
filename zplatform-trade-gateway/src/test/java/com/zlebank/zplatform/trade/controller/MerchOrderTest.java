/* 
 * MerchOrderTest.java  
 * 
 * version TODO
 *
 * 2016年4月5日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.controller;

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
import org.springframework.web.context.WebApplicationContext;

import com.zlebank.zplatform.commons.utils.RSAUtils;
import com.zlebank.zplatform.trade.bean.gateway.OrderBean;
import com.zlebank.zplatform.trade.utils.ObjectDynamic;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年4月5日 上午10:02:29
 * @since 
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("/*.xml")
public class MerchOrderTest {
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	

	@Before
	public void setup() throws Exception {
		mockMvc = webAppContextSetup(this.wac).build();
		
	}
	
	@Test
	public void test_merchOrder(){
		// 证联公钥
	    String PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCtiaPjj78wWcfSgsEoKWK3xYj+64eVVmPIKqzKXMiiW8u4ShXsmqMB5j4KfJzv3OLjDVl8DjzQnE+k3XSRap4rg5fWmk9PrTZazu290Ay0oIqakVrVMAwemIzLMO1EjJwvmpqbin/UsQUx6hU+SMpXpgAQuZ3hEhE1pemf+lNWewIDAQAB";
	    // 商户私钥
	    String PRI_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIafBw+hz8/7LSkFMLwg0IFQDmU/JCR+jymilk5EPIn3z5pK6m3anbf1Y8Af0XKcTPDm1/eTPW2aB9YD/+GT+vSMJZsVKRqBJOkJ95ZkRFFPzWG8HzOKkwVkT9PTKM1v2lAv/hRH42GduIF58LgRqt9V4uhdcsHXVSkuDKxw76d9AgMBAAECgYBMQeE6FasmYV4fknUc3GnAFp1Q9QeANMDcEOGbzEOO0/WhrTZGrEQTZMnwlxWKGsfq3pkQATQW1d4CeZmFKtJIyjBKcCIgnJaVGAsGr9X2NgClqDVYdZBtMEW4q2vXFhMwFTj8DVY2zJ957iE5pLqtjwZpypy7LPaqnHI8ebyMgQJBAMWRCuB8+R+ty59yb+KKPnxCC5oInpqCKX2+wtBTkYEN6rXINZx9ONbpMic9DN6wf35wKgX/1SU93CyfH2FPm/UCQQCucALdyAMMSzfnzWZQip3Dany3WRSSqbTPdZ1YwE8I53DPrvqfd6x3jCeXlT76BDSwjllFAYSAifCNfWCf5/BpAkEAwNlH6MGU3BtlvAqUtGmFfP1rHbx0NRYBMkFXIMuyKGtRRIhd7Jc/OGkBD9RED8vxZ5ZNLvX/NQ8hMF1S4EY5UQJBAJ/JqUaBThF34YksBNoob+Wu6PmG1yqV3DPh2DWRS5SrdRI5hgiHtmt37liTdTHOonlMnAdzIW4hbUIepGE2NGkCQG3JQwq1D+1Hlp++UeNm09v5d9ZPngf/trIIXU2YyjC4Lj9IK1BuqbtuDGyHVWTjLx3/Q+gAQ2nCmiHmALZ8DT4=";
	    String version = "v1.0";
		//默认取值：01:UTF-8
		String encoding = "1";//M
		//01RSA02 MD5 (暂不支持)
		String signMethod = "01";//M
		//取值：01 
		String txnType ="01";//M
		//01：自助消费，通过地址的方式区分前台消费和后台消费（含无跳转支付）
		String txnSubType = "00";//M
		// 
		String bizType = "000201";//M
		String channelType = "07";//M
		//后台返回商户结果时使用，如上送，则发送商户后台交易结果通知
		String backUrl = "http://192.168.101.209:8081/demo/ReciveNotifyServlet";//M
		//0：普通商户直连接入2：平台类商户接入
		String accessType ="2";//M
		//　
		String coopInstiId = "300000000000014";
		//String merId = "300000000000014";// M
		//商户类型为平台类商户接入时必须上送
		String merId="200000000000597";//C
		//商户类型为平台类商户接入时必须上送
		String merName = "测试商户";//C
		//商户类型为平台类商户接入时必须上送
		String merAbbr="测试商户";//C
		String time = new SimpleDateFormat("yyyyMMddHH").format(new Date());
		//商户端生成
		String orderId = time + ((100001+new Integer(new Random().nextInt(899999)))+"").toString();//M
		//商户发送交易时间
		String txnTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());// M
		//后台类交易且卡号上送；跨行收单且收单机构收集银行卡信息时上送01：银行卡02：存折03：C卡默认取值：01取值“03”表示以IC终端发起的IC卡交易，IC作为普通银行卡进行支付时，此域填写为“01”
		String accType ="01";//C
		//1、  后台类消费交易时上送全卡号或卡号后4位 2、  跨行收单且收单机构收集银行卡信息时上送、  3、前台类交易可通过配置后返回，卡号可选上送
		String accNo = "";//C
		//交易单位为分
		String txnAmt = "100";//M
		//默认为156交易 参考公参
		String currencyCode = "156";//M
		//PC1、前台类消费交易时上送2、认证支付2.0，后台交易时可选
		String orderTimeout = "10000";//O
		
		//PC超过此时间用户支付成功的交易，不通知商户，系统自动退款，大约5个工作日金额返还到用户账户
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 1);
		String payTimeout =  new SimpleDateFormat("yyyyMMddHHmmss").format(calendar.getTime());//O
		//　
		String termId = "";//O
		//商户自定义保留域，交易应答时会原样返回
		String reqReserved="";//O
		//子域名： 活动号 marketId  移动支付订单推送时，特定商户可以通过该域上送该订单支付参加的活动号
		String reserved ="";//O
		//格式如下：{子域名1=值&子域名2=值&子域名3=值}
		String riskRateInfo = "shippingFlag=000&shippingCountryCode=0&shippingProvinceCode=0&shippingCityCode=0&shippingDistrictCode=0&shippingStreet=0&commodityCategory=0&commodityName=iphone&commodityUrl=0&commodityUnitPrice=0&commodityQty=0&shippingMobile=0&addressModifyTim=0&userRegisterTime=0&orderNameModifyTime=0&userId=0&orderName=0&userFlag=0&mobileModifyTime=0&riskLevel=0&merUserId=100000000000576&merUserRegDt=0&merUserEmail=0";//O
		//当使用银联公钥加密密码等信息时，需上送加密证书的CertID；说明一下？目前商户、机构、页面统一套
		
		//前台消费交易若商户上送此字段，则在支付失败时，页面跳转至商户该URL（不带交易信息，仅跳转）
		String frontFailUrl ="";//O
		//C  取值参考数据元说明
		String defaultPayType = "";//O
		//C当帐号类型为02-存折时需填写在前台类交易时填写默认银行代码，支持直接跳转到网银商户发卡银行控制系统应答返回
		String issInsCode ="";//O
		//仅仅pc使用，使用哪种支付方式 由收单机构填写，取值为以下内容的一种或多种，通过逗号（，）分割。取值参考数据元说明
		String supPayType = "";//O
		//移动支付业务需要上送
		String userMac ="";//O
		//前台交易，有IP防钓鱼要求的商户上送
		String customerIp = "";//C
		//有卡交易必填有卡交易信息域
		String cardTransData = "";//C
		//渠道类型为语音支付时使用
		String vpcTransData = "";//C

		
		
		//页面确实字段
		String certId = "1234";
		String frontUrl = "http://192.168.101.209:8081/demo/ReciveNotifyServlet";
		String customerInfo = "";
		String encryptCertId = "";
		String instalTransInfo = "";
		
		
		//移动支付上送
		String orderDesc = "";//C
		OrderBean order = new OrderBean(version, encoding, certId, frontUrl, backUrl, "", signMethod, coopInstiId, merId, merName, merAbbr, orderId, txnType, txnSubType, bizType, channelType, accessType, txnTime, accType, accNo, txnAmt, currencyCode, customerInfo, orderTimeout, payTimeout, reqReserved, reserved, riskRateInfo, encryptCertId, frontFailUrl, instalTransInfo, defaultPayType, issInsCode, supPayType, userMac, customerIp, cardTransData, orderDesc);
		
		String[] unParamstring = {"signature"};
		try {
			String singMsg = ObjectDynamic.generateParamer(order, false, unParamstring);
			String signature = RSAUtils.sign(URLEncoder.encode(singMsg,"utf-8").toString().getBytes(),PRI_KEY);
			order.setSignature(signature);
			this.mockMvc.perform(post("/merch/coporder.htm").param("version", version)
															.param("encoding", encoding)
															.param("signMethod", signMethod)
															.param("accNo", accNo)
															.param("accType",accType)
															.param("accessType",accessType)
															.param("backUrl",backUrl)
															.param("bizType",bizType)
															.param("cardTransData",cardTransData)
															.param("certId",certId)
															.param("channelType",channelType)
															.param("coopInstiId",coopInstiId)
															.param("currencyCode",currencyCode)
															.param("customerInfo",customerInfo)
															.param("customerIp",customerIp)
															.param("defaultPayType",defaultPayType)
															  .param("encryptCertId",encryptCertId)
															  .param("frontFailUrl",frontFailUrl)
															  .param("frontUrl",frontUrl)
															  .param("instalTransInfo",instalTransInfo)
															  .param("issInsCode",issInsCode)
															  .param("merAbbr",merAbbr)
															  .param("merId",merId)
															  .param("merName",merName)
															  .param("orderDesc",orderDesc)
															  .param("orderId",orderId)
															  .param("orderTimeout",orderTimeout)
															  .param("payTimeout",payTimeout)
															  .param("reqReserved",reqReserved)
															  .param("reserved",reserved)
															  .param("riskRateInfo",riskRateInfo)
															  .param("supPayType",supPayType)
															  .param("txnAmt",txnAmt)
															  .param("txnSubType",txnSubType)
															  .param("txnTime",txnTime)
															  .param("txnType",txnType)
															  .param("userMac",userMac)
															  .param("signature", signature)
															).andExpect(status().isOk()).andDo(print());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
