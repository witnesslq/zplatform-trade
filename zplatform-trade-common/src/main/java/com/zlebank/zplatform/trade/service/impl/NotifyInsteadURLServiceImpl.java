/* 
 * NotifyInsteadURLService.java  
 * 
 * version TODO
 *
 * 2016年3月30日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.commons.utils.BeanCopyUtil;
import com.zlebank.zplatform.commons.utils.RegExpValidatorUtil;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.member.service.MerchMKService;
import com.zlebank.zplatform.trade.dao.InsteadPayBatchDAO;
import com.zlebank.zplatform.trade.dao.InsteadPayDetailDAO;
import com.zlebank.zplatform.trade.insteadPay.message.BaseMessage;
import com.zlebank.zplatform.trade.insteadPay.message.InsteadPayQuery_Request;
import com.zlebank.zplatform.trade.insteadPay.message.InsteadPayQuery_Response;
import com.zlebank.zplatform.trade.model.PojoInsteadPayBatch;
import com.zlebank.zplatform.trade.service.InsteadPayService;
import com.zlebank.zplatform.trade.service.NotifyInsteadURLService;

/**
 * 代付URL通知类
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月30日 下午3:37:04
 * @since 
 */
@Service("notifyInsteadURLService")
public class NotifyInsteadURLServiceImpl implements NotifyInsteadURLService,  ApplicationListener<ContextRefreshedEvent>{

    private static final Log log = LogFactory.getLog(NotifyInsteadURLServiceImpl.class);
    
    @Autowired
    private InsteadPayBatchDAO insteadPayBatchDAO;
    
    @Autowired
    private InsteadPayDetailDAO insteadPayDetailDAO;
    

    @Autowired
    private InsteadPayService insteadPayService;
    
    @Autowired
    private MerchMKService merchMKService;
    
    // 线程数量
    private static final int THREAD_NUM = 1;
    
    // 初始化线程池
    ExecutorService exec=Executors.newFixedThreadPool(THREAD_NUM);

    // 初始化阻塞队列（10万个）
    private BlockingQueue<InsteadPayNotifyTask> taskQueue = new LinkedBlockingQueue<InsteadPayNotifyTask>(100000);
    
    /**
     * 添加一个代付任务
     * @param batchId
     */
    @Override
    @Transactional
    public void addInsteadPayTask(Long batchId) {
        
        PojoInsteadPayBatch batch = insteadPayBatchDAO.getByBatchId(batchId);
        System.out.println(batch.getBatchNo());
        // 请求报文
        InsteadPayQuery_Request requestBean = new InsteadPayQuery_Request();
        requestBean.setBatchNo(String.valueOf(batch.getBatchNo()));
        requestBean.setTxnTime(batch.getTxnTime());
        requestBean.setMerId(batch.getMerId());
        // 响应报文
        InsteadPayQuery_Response responseBean = BeanCopyUtil.copyBean(InsteadPayQuery_Response.class, requestBean);
        // 代付查询
        insteadPayService.insteadPayQuery(requestBean, responseBean);
        String data = responseData(responseBean);
        
        // 如果URL合法，则发送通知，否则不通知。
        if (StringUtil.isEmpty(batch.getNotifyUrl()) || RegExpValidatorUtil.IsUrl(batch.getNotifyUrl())) {
            // 添加一个代付任务
            InsteadPayNotifyTask task = new InsteadPayNotifyTask();
            task.setData(data);
            task.setUrl(batch.getNotifyUrl());
            taskQueue.add(task);
        } else {
            log.warn("URL为空或非法URL，无法执行通知任务。InsteadPayBatchSeqNo->"+batch.getInsteadPayBatchSeqNo());
        }

    }
    
    /**
     * 返回数据（同步response）
     * @param responseStream
     * @param bean
     */
    @SuppressWarnings("unchecked")
    private String responseData(BaseMessage bean) {
        JSONObject jsonData = JSONObject.fromObject(bean);
        jsonData.put("signature", "");
        // 排序
        Map<String, Object> map = new TreeMap<String, Object>();
        map =(Map<String, Object>) JSONObject.toBean(jsonData, TreeMap.class);
        jsonData = JSONObject.fromObject(map);

        // 加签名
        if (log.isDebugEnabled()) {
            log.debug("【应答报文】加签用ID：" + bean.getMerId());
            log.debug("【应答报文】加签原数据：" + jsonData.toString());
        }
        String signature  = merchMKService.sign(bean.getMerId(), jsonData.toString());
        jsonData.put("signature", signature);
        // 返回数据
        if (log.isDebugEnabled()) {
            log.debug("【应答报文】返回数据："+jsonData.toString());
        }
        return jsonData.toString();
    }

    /**
     * 代付
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 并发N个线程执行
        for (int index = 0; index < THREAD_NUM; index++) {
            exec.execute(new ExecuteInsteadPayTaskQueue(taskQueue));
        }
//        exec.shutdown();
    }
}
