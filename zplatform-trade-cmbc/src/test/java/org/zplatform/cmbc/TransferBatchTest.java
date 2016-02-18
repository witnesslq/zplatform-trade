package org.zplatform.cmbc;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.trade.cmbc.service.IInsteadPayService;
import com.zlebank.zplatform.trade.dao.TransferBatchDAO;
import com.zlebank.zplatform.trade.dao.TransferDataDAO;
import com.zlebank.zplatform.trade.model.PojoTransferBatch;

public class TransferBatchTest {
    private ApplicationContext context;
    private TransferBatchDAO transferBatchDAO;
    private TransferDataDAO transferDataDAO;
    private IInsteadPayService insteadPayService;
    public void init(){
        context = new ClassPathXmlApplicationContext("CmbcContextTest.xml");
        transferBatchDAO = (TransferBatchDAO) context.getBean("transferBatchDAO");
        transferDataDAO = (TransferDataDAO) context.getBean("transferDataDAO");
        insteadPayService = (IInsteadPayService) context.getBean("insteadPayService");
    }
    @Test
    public void testJob() { 
        init();
        try {
            List<PojoTransferBatch> batchList =  transferBatchDAO.findWaitAccountingTransferBatch();
            for(PojoTransferBatch batch:batchList){
                System.out.println(JSON.toJSONString(batch));
                int rows = transferBatchDAO.validateBatchResult(batch.getInsteadpaybatchno());
                System.out.println(rows);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         
    }
}
