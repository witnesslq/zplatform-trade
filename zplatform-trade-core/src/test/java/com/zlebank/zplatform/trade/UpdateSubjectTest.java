/* 
 * UpdateSubjectTest.java  
 * 
 * version TODO
 *
 * 2016年3月16日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.zlebank.zplatform.trade.batch.spliter.BatchSpliter;
import com.zlebank.zplatform.trade.bean.UpdateData;
import com.zlebank.zplatform.trade.service.UpdateInsteadService;
import com.zlebank.zplatform.trade.service.UpdateSubject;

/**
 * Class Description
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月16日 下午2:11:13
 * @since 
 */
public class UpdateSubjectTest {
    static ApplicationContext context = null;
    static BatchSpliter service = null;
    static UpdateSubject insteadBatchService = null;
    static {
        context = ApplicationContextUtil.get();
        insteadBatchService =  (UpdateSubject) context.getBean("updateInsteadServiceImpl");
    }

//    @Test   
    public void testSuccess() {
        UpdateData data = new UpdateData();
        data.setTxnSeqNo("1603159900051715");
        data.setResultCode("00");
        data.setResultMessage("成功");
        data.setChannelCode("93000001");
        insteadBatchService.update(data );
    }
    @Test   
    public void testFail() {
        UpdateData data = new UpdateData();
        data.setTxnSeqNo("1603159900051715");
        data.setResultCode("09");
        data.setResultMessage("失败");
        data.setChannelCode("93000001");
        insteadBatchService.update(data );
    }
}
