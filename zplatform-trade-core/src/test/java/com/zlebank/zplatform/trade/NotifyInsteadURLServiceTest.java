/* 
 * NotifyInsteadURLServiceTest.java  
 * 
 * version TODO
 *
 * 2016年3月30日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.zlebank.zplatform.trade.batch.spliter.BatchSpliter;
import com.zlebank.zplatform.trade.service.NotifyInsteadURLService;

/**
 * 代付通知类
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月30日 下午3:44:52
 * @since 
 */
public class NotifyInsteadURLServiceTest {
    static ApplicationContext context = null;
    static BatchSpliter service = null;
    static NotifyInsteadURLService notifyInsteadURLService = null;
    static {
        context = ApplicationContextUtil.get();
        notifyInsteadURLService =  (NotifyInsteadURLService) context.getBean("notifyInsteadURLService");
    }
    
    @Test
    public void TestNotify() throws InterruptedException {
        int sleep = 2000;
        notifyInsteadURLService.addInsteadPayTask(997L);Thread.sleep(sleep);
//        notifyInsteadURLService.addInsteadPayTask(189L);Thread.sleep(sleep);
//        notifyInsteadURLService.addInsteadPayTask(190L);Thread.sleep(sleep);
//        notifyInsteadURLService.addInsteadPayTask(191L);Thread.sleep(sleep);
    }
}
