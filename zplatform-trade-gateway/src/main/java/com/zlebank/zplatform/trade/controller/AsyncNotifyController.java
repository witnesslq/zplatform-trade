/* 
 * AsyncNotifyController.java  
 * 
 * version TODO
 *
 * 2016年5月25日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zlebank.zplatform.wechat.service.WeChatService;
import com.zlebank.zplatform.wechat.wx.WXApplication;
import com.zlebank.zplatform.wechat.wx.bean.PayResultBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月25日 下午7:13:11
 * @since 
 */
@Controller
@RequestMapping("/notify")
public class AsyncNotifyController {

	private static final Log log = LogFactory.getLog(AsyncNotifyController.class);
	
	@Autowired
	private WeChatService weChatService; 
	
	@RequestMapping("/wxResult.htm")
    public void wxResult(HttpSession httpSession, HttpServletResponse response,HttpServletRequest request) throws Exception{
        PrintWriter responseStream = getPrintWriter(response);
        StringBuffer sb = new StringBuffer();
        //获取HTTP请求的输入流
        InputStream is = request.getInputStream();
        //已HTTP请求输入流建立一个BufferedReader对象
        BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
        //读取HTTP请求内容
        String buffer = null;
        while ((buffer = br.readLine()) != null) {
            //在页面中显示读取到的请求参数
            sb.append(buffer);
        }
        log.debug("【收到XML数据】"+ sb.toString());
        System.out.println(sb);
        WXApplication app = new WXApplication();
        PayResultBean result = app.parseResultXml(sb.toString());
        
        weChatService.asyncTradeResult(result);
        // 返回响应数据
        String responseXml = null;
        if ("SUCCESS".equals(result.getReturn_code()) && "SUCCESS".equals(result.getResult_code())) {
            responseXml = app.createResponseResultXml("SUCCESS","OK");
        } else {
            responseXml = app.createResponseResultXml("FAIL", result.getReturn_code() == null ? result.getResult_code() : result.getReturn_code());
        }
        try {
            responseStream.write(responseXml);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        } finally{
            responseStream.flush();responseStream.close();
        }
    }
	
	/**
     * 得到输出流
     * @param response
     */
    private PrintWriter getPrintWriter(HttpServletResponse response) {
        PrintWriter responseStream = null;
        response.setContentType("textml;charset=UTF-8");
        try {
            responseStream = response.getWriter();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return responseStream;
    }
}
