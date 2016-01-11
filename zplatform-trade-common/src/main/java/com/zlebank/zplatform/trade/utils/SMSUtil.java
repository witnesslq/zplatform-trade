package com.zlebank.zplatform.trade.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zlebank.zplatform.trade.model.TxnsSmsModel;
import com.zlebank.zplatform.trade.service.ITxnsQuickpayService;
import com.zlebank.zplatform.trade.service.ITxnsSMSService;

public class SMSUtil implements Runnable{
    private static final Log log = LogFactory.getLog(SMSUtil.class);
    private static final String ENCODE="UTF-8";
    private static final String CorpID=ConsUtil.getInstance().cons.getSms_username();//账户名
    private static final String Pwd=ConsUtil.getInstance().cons.getSms_pwd();//密码
    private static final String smsURL = ConsUtil.getInstance().cons.getSms_url();
    
    private String mobile;
    private String content;
    private String tn;
    private String sendtime;
    private String payorderno;
    private ITxnsSMSService txnsSMSService;
    private ITxnsQuickpayService txnsQuickpayService;
    /**
     * 发送短信
     * @param mobile
     * @param content
     * @return
     */
    public int sendSMS(String mobile,String content){
        int inputLine = 999;
        try {
            //String send_content=URLEncoder.encode(content.replaceAll("<br/>", " "), "GBK");//发送内容
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("Name", CorpID);
            paramMap.put("Passwd", Pwd);
            paramMap.put("Phone", mobile);
            paramMap.put("Content", content);
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.openConnection();
            log.info("start send sms,the mobile phone numbuer:"+mobile+"--- send content: "+content);
            String responseContent = "";//httpUtils.executeHttpPost(smsURL, setHttpParams(paramMap), ENCODE);
            if(ConsUtil.getInstance().cons.isSms_send_flag()){
                responseContent = httpUtils.executeHttpPost(smsURL, setHttpParams(paramMap), ENCODE);
            }else{
                responseContent = "100";
            }
            httpUtils.closeConnection();
            inputLine = Integer.valueOf(responseContent);
            TxnsSmsModel sms = new TxnsSmsModel();
            sms.setTn(tn);
            sms.setSendtime(sendtime);
            sms.setRetcode(responseContent);
            switch (inputLine) {
                case 100 :
                    sms.setRetinfo("短信发送成功");
                    break;
                case 101 :
                    sms.setRetinfo("账号密码不能为空");
                    break;
                case 102 :
                    sms.setRetinfo("手机号,短信内容均不能为空");
                    break;
                case 103 :
                    sms.setRetinfo("数据库连接失败");
                    break;
                case 104 :
                    sms.setRetinfo("账号密码错误");
                    break;
                case 105 :
                    sms.setRetinfo("短信发送成功,等待审核!");
                    break;
                case 106 :
                    sms.setRetinfo("短信发送失败");
                    break;
                case 999 :
                    sms.setRetinfo("未知错误");
                    break;
            }
            
            txnsSMSService.updateSMSResult(sms);
            txnsQuickpayService.updateCMBCSMSResult(payorderno, sms.getRetcode(), sms.getRetinfo());
            log.info("end send sms, response content is :"+responseContent);
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inputLine;
       
    }
    
    /**
     * 设置请求参数
     * @param
     * @return
     */
    private List<HttpRequestParam> setHttpParams(Map<String, String> paramMap) {
        List<HttpRequestParam> formparams = new ArrayList<HttpRequestParam>();
        Set<Map.Entry<String, String>> set = paramMap.entrySet();
        for (Map.Entry<String, String> entry : set) {
            formparams.add(new HttpRequestParam(entry.getKey(), entry.getValue()));
        }
        return formparams;
    }

    @Override
    public void run() {
        sendSMS(mobile, content);
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SMSUtil(String mobile, String content,String tn,String sendtime,String payorderno) {
        super();
        this.mobile = mobile;
        this.content = content;
        this.tn = tn;
        this.sendtime = sendtime;
        this.payorderno = payorderno;
        txnsSMSService = (ITxnsSMSService) SpringContext.getContext().getBean("txnsSMSService");
        txnsQuickpayService = (ITxnsQuickpayService) SpringContext.getContext().getBean("txnsQuickpayService");
    }

    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getPayorderno() {
        return payorderno;
    }

    public void setPayorderno(String payorderno) {
        this.payorderno = payorderno;
    }
    
    
    
}
