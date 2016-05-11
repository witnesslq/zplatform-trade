package com.zlebank.zplatfrom.trade.chanpay;

import java.util.HashMap;
import java.util.Map;

import com.zlebank.zplatform.trade.chanpay.utils.RSA;

/**
 * <p>测试RSA签名验签</p>
 */
public class DefaultSignServiceTest {
	
	private static final String WALLET_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMPQg/NyltuuZqlfvir5r8BzBJDpJ+1Wc/r/x7ikW0ej11XzB68d9v5HYdZMxwKXRpXb9fyfrVkMVnZNy7MPfK83qhLL3iGOSsQ6I5VLz7zQj2O8g3THCP4mqmBFusMXKko9AwpsPVAcpoNJm5SLRJJYoToi4Z2u4Z4spZTzY7CbAgMBAAECgYATexMljSjlAhzEjPnmXKDXjJnPsOKAAjHXrsq32+IY5nvt2RfSnDCOa5uYLls+Lr6e+hsm3tvoGAoiCutRDSFiQKKdWq/8A4xeC7CxQNRujBAs6gS5fhhNUrm8471XFO1mF4vIdi2Je/hjX76QuHX1POE2T20DV7Oi5RW3tmz4QQJBAOFVnEbqWXdfJvxrJrtnSWahU3yn0NmMrsY2SiHXFQBzLofgk2/fYKK0KqJb0cvPaxjZEl2nFIVf96OwbIFmm4UCQQDedndA5RHES1r2Jx5n+olh0C9CnuERia6/WgfKR8U+bm4f28ol6Hiu9TL0ca+gGTWWORwfj915BDfwlwz0eYWfAkBsl1RDvKY2580i8gRtZb4yzmYsebclUC3d6cXZ/wvo9pki9DA5Rp4MauTs73DwVloXVG0MYvt5tyDhaqEvzyH9AkAWoYCSNntvN6dCQUqDk2YkcDROl7EXwqTnTHZcap6zMjK7xPU0lAiq68DKQ0J1i/r6lEa7IzyJkhdKv2MO/8nLAkEAhSkbHGpgDrhd//j+FjJgMBEzVlxOOj7s9h/wJmilGjWqd2mJPyyQYx+1q1hbDxaEzHdUvglGhsgRNIwfR9pX2A==";
	private static final String WALLET_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDD0IPzcpbbrmapX74q+a/AcwSQ6SftVnP6/8e4pFtHo9dV8wevHfb+R2HWTMcCl0aV2/X8n61ZDFZ2TcuzD3yvN6oSy94hjkrEOiOVS8+80I9jvIN0xwj+JqpgRbrDFypKPQMKbD1QHKaDSZuUi0SSWKE6IuGdruGeLKWU82OwmwIDAQAB";

   public static void main(String[] args) throws Exception {
	   Map<String, String> notifyData = new HashMap<String, String>();
       notifyData.put("_input_charset", "UTF-8");
       notifyData.put("extension", "{}");
       notifyData.put("gmt_create", "20160419162157");
       notifyData.put("gmt_payment", "20160419162157");
       notifyData.put("inner_trade_no", "101146105410658494119");
       notifyData.put("notify_id", "c5f827b72ada435c834d1222cfdc31fd");
       notifyData.put("notify_time", "20160419162157");
       notifyData.put("notify_type", "trade_status_sync");
       notifyData.put("outer_trade_no", "ZF20160419001");
       notifyData.put("trade_amount", "0.01");
       notifyData.put("trade_status", "TRADE_SUCCESS");
       notifyData.put("version", "1.0");

       String expPlain = "_input_charset=UTF-8&extension={}&gmt_create=20160419162157&gmt_payment=20160419162157&inner_trade_no=101146105410658494119&notify_id=c5f827b72ada435c834d1222cfdc31fd&notify_time=20160419162157&notify_type=trade_status_sync&outer_trade_no=ZF20160419001&trade_amount=0.01&trade_status=TRADE_SUCCESS&version=1.0";
       String signature = RSA.sign(expPlain, WALLET_PRIVATE_KEY, "UTF-8");
       		
       boolean testTrue = RSA.verify(expPlain, signature, WALLET_PUBLIC_KEY, "UTF-8");
       System.out.println(testTrue);
       //boolean testFalse = RSA.verify(expPlain, "1" + signature, WALLET_PUBLIC_KEY, "UTF-8");
} 

}
