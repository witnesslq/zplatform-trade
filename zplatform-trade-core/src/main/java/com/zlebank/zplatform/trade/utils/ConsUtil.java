package com.zlebank.zplatform.trade.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConsUtil {
	private static ConsUtil util;
	public ConsModel cons;
	private ConsUtil(){
		try {
			cons = new ConsModel();
			String path = "/home/web/trade/";
			File file = new File(path+ "zlrt.properties");
			if(!file.exists()){
			    path = getClass().getResource("/").getPath();
			    file = null;
			}
			Properties prop = new Properties();
			InputStream stream = null;
			stream = new BufferedInputStream(new FileInputStream(new File(path+ "zlrt.properties")));
			prop.load(stream);
			String url=prop.getProperty("zlrt_url");
			cons.setZlrt_url(url);
			cons.setInstuId(prop.getProperty("instuId"));
			cons.setKeyStorePath(path+prop.getProperty("keyStorePath"));
			cons.setCerPath(path+prop.getProperty("cerPath"));
			cons.setKeyStorePath_zlrt(path+prop.getProperty("keyStorePath_zlrt"));
			cons.setCerPath_zlrt(path+prop.getProperty("cerPath_zlrt"));
			
			cons.setSendMarginSms_url(url+prop.getProperty("sendMarginSms_url"));
			cons.setMarginRegisterReq_url(url+prop.getProperty("marginRegisterReq_url"));
			cons.setOnlineDepositShort_url(url+prop.getProperty("onlineDepositShort_url"));
			cons.setWithdrawNotifyReq_url(url+prop.getProperty("withdrawNotifyReq_url"));
			cons.setQuerytranstatus_url(url+prop.getProperty("querytranstatus_url"));
			cons.setRout_switch(prop.getProperty("rout_switch"));
			
			cons.setEciti_chnl_code(prop.getProperty("eciti_chnl_code"));
			cons.setEciti_insti_code(prop.getProperty("eciti_insti_code"));
			cons.setEciti_safe_no(prop.getProperty("eciti_safe_no"));
			
			cons.setZlpay_chnl_code(prop.getProperty("zlpay_chnl_code"));
			cons.setZlpay_chnl_merch_name(prop.getProperty("zlpay_chnl_merch_name"));
			
			cons.setReapay_quickpay_version(prop.getProperty("reapay_quickpay_version"));
			cons.setReapay_quickpay_url(prop.getProperty("reapay_quickpay_url"));
			cons.setReapay_quickpay_debit_url(prop.getProperty("reapay_quickpay_debit_url"));
			cons.setReapay_quickpay_merchant_id(prop.getProperty("reapay_quickpay_merchant_id"));
			cons.setReapay_quickpay_seller_email(prop.getProperty("reapay_quickpay_seller_email"));
			cons.setReapay_quickpay_notify_url(prop.getProperty("reapay_quickpay_notify_url"));
			cons.setReapay_quickpay_key(prop.getProperty("reapay_quickpay_key"));
			cons.setReapay_quickpay_pubkey(prop.getProperty("reapay_quickpay_pubkey"));
			cons.setReapay_quickpay_prikey(prop.getProperty("reapay_quickpay_prikey"));
			cons.setReapay_quickpay_sms_url(prop.getProperty("reapay_quickpay_sms_url"));
			cons.setReapay_quickpay_credit_url(prop.getProperty("reapay_quickpay_credit_url"));
			cons.setReapay_quickpay_pay_url(prop.getProperty("reapay_quickpay_pay_url"));
			cons.setReapay_quickpay_prikey_pwd(prop.getProperty("reapay_quickpay_prikey_pwd"));
			cons.setReapay_quickpay_query_url(prop.getProperty("reapay_quickpay_query_url"));
			cons.setReapay_chnl_code(prop.getProperty("reapay_chnl_code"));
			cons.setReapay_quickpay_bind_url(prop.getProperty("reapay_quickpay_bind_url"));
			cons.setOrder_number_file_path(prop.getProperty("order_number_file_path"));
			cons.setMember_pay_password_key(prop.getProperty("member_pay_password_key"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static synchronized ConsUtil getInstance(){
		if(util==null){
			util = new ConsUtil();
		}
		return util;
	}
	
}
