package com.zlebank.zplatform.trade;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.zlebank.zplatform.trade.bean.enums.BankTransferBatchOpenStatusEnum;
import com.zlebank.zplatform.trade.dao.BankTransferBatchDAO;
import com.zlebank.zplatform.trade.dao.TransferBatchDAO;
import com.zlebank.zplatform.trade.model.PojoTranData;

public class TransferBatchDAOTest {
    private TransferBatchDAO transferBatchDAO;
    private BankTransferBatchDAO bankTransferBatchDAO;

    @Before
    public void init() {
        ApplicationContext context = ApplicationContextUtil.get();
        transferBatchDAO = (TransferBatchDAO) context
                .getBean("transferBatchDAO");
        bankTransferBatchDAO = (BankTransferBatchDAO) context
                .getBean("bankTransferBatchDAO");
    }

    @Test
    @Ignore
    public void testQueryWaitTrialTranData() {
        List<PojoTranData> tranDatas = transferBatchDAO
                .queryWaitTrialTranData(11);
        Assert.assertEquals(10, tranDatas.size());
    }

    @Test
    public void testGetByTranBatchAndOpenStatus() {
        bankTransferBatchDAO
                .getByTranBatchAndOpenStatus(447,
                        BankTransferBatchOpenStatusEnum.OPEN);

    }
}
