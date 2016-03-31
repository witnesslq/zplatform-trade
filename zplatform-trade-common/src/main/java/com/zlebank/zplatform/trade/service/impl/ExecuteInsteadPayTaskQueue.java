/* 
 * ExecuteInsteadPayTaskQueue.java  
 * 
 * version TODO
 *
 * 2016年3月30日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 执行代付任务
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月30日 下午4:36:20
 * @since 
 */
public class ExecuteInsteadPayTaskQueue implements Runnable{
    
    private static final Log log = LogFactory.getLog(ExecuteInsteadPayTaskQueue.class);

    private BlockingQueue<InsteadPayNotifyTask> queue;
    
    public ExecuteInsteadPayTaskQueue(BlockingQueue<InsteadPayNotifyTask> queue) {
        this.queue = queue;
    }
    
    /**
     * 发送HTTP 报文
     */
    @Override
    public void run() {
        while (true) {
            try {
                InsteadPayNotifyTask trade = queue.take();
                sendHttpPost(trade.getData(), trade.getUrl());
            } catch (InterruptedException e) {
                log.error("取代付通知任务时发生错误！");
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 发送HTTP报文
     * @param jsonData 报文数据
     * @param url 报文地址
     */
    private void sendHttpPost(String jsonData, String url) {
//        System.out.println("以下数据发送成功！");
//        System.out.println("URL："+ url);
//        System.out.println("Data：" + jsonData);
//        url = "http://localhost:8080/zplatform-merportal-api/mock/notify.htm";
        try {
            PostMethod post = null;
            post = new PostMethod(url);
            post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            post.addParameter("data", jsonData);
            post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");  
            HttpClient client = new HttpClient();
            client.getHttpConnectionManager().getParams().setSoTimeout(10000);// 10秒过期
            int status = client.executeMethod(post);
            // 成功接收
            if (status == 200) {
                if (log.isDebugEnabled())
                    log.debug("【成功】状态返回200");
            } else {
                if (log.isDebugEnabled())
                    log.debug("【失败】状态返回不是200，-->"+status);
            }
        } catch (Exception e) {
            log.error("发送代付通知时发生错误！");
            log.error(e.getMessage(), e);
        }
    }
}
