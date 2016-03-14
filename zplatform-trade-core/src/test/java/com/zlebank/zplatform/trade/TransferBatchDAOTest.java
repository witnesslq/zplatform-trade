package com.zlebank.zplatform.trade;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.zlebank.zplatform.trade.dao.TransferBatchDAO;
import com.zlebank.zplatform.trade.model.PojoTranData;

public class TransferBatchDAOTest {
    private TransferBatchDAO transferBatchDAO;
    
    @Before
    public void init() {
        ApplicationContext context = ApplicationContextUtil.get();
        transferBatchDAO = (TransferBatchDAO) context.getBean("transferBatchDAO");
    }
    
    @Test
    public void testQueryWaitTrialTranData(){
        List<PojoTranData> tranDatas = transferBatchDAO.queryWaitTrialTranData(11);
        Assert.assertEquals(10, tranDatas.size());
    }
}
