package com.zlebank.zplatform.trade;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.zlebank.zplatform.trade.bean.enums.SeqNoEnum;
import com.zlebank.zplatform.trade.dao.TranBatchDAO;
import com.zlebank.zplatform.trade.model.PojoTranBatch;
import com.zlebank.zplatform.trade.model.PojoTranData;
import com.zlebank.zplatform.trade.service.SeqNoService;
import com.zlebank.zplatform.trade.util.RandomArugment;
/**
 * 
 * Transfer pojos mapping test
 *
 * @author yangying
 * @version
 * @date 2016年3月13日 上午11:24:33
 * @since
 */
public class TrasferPojoTest {
    private SeqNoService seqNoServiceImpl;
    private TranBatchDAO tranBatchDAO;
    private SessionFactory sessionFactory;
    @Before
    public void init() {
        ApplicationContext context = ApplicationContextUtil.get();
        seqNoServiceImpl = (SeqNoService) context.getBean("seqNoServiceImpl");
        tranBatchDAO = (TranBatchDAO) context.getBean("tranBatchDAOImpl");
        sessionFactory = (SessionFactory) context.getBean("sessionFactory");
    }
    @Test
    public void testQueryTranBatch() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(PojoTranBatch.class);

            @SuppressWarnings("unchecked")
            List<PojoTranBatch> transBatchs = (List<PojoTranBatch>) criteria
                    .list();
            Assert.assertEquals(9, transBatchs.size());
            for (PojoTranBatch transBatch : transBatchs) {
                if (transBatch.getTid() == 258) {
                    List<PojoTranData> tranDatas = transBatch.getTranDatas();
                    Assert.assertEquals(10, tranDatas.size());
                }
            }
            /*criteria = session.createCriteria(PojoTranBatch.class);
            criteria = criteria.setFetchMode("tranDatas", FetchMode.JOIN).setFetchMode("bankTranData", FetchMode.SELECT);
            Assert.assertEquals(27, transBatchs.size());
            for (PojoTranBatch transBatch : transBatchs) {
                if (transBatch.getTid() == 258) {
                    List<PojoTranData> tranDatas = transBatch.getTranDatas();
                    Assert.assertEquals(10, tranDatas.size());
                }
            }*/
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            Assert.fail();
        }finally{
            
            session.close();
        }
       
    }
    @Test
    @Ignore
    public void addTranBatch() {
        List<PojoTranData> pojoTranDatas = new ArrayList<PojoTranData>();
        int transDatasNum = 10;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "YYYYMMDDHHmmss");
        long totalAmt = 0L;
        long totalCount = 0L;
        for (int i = transDatasNum; i > 0; i--) {
            PojoTranData tranData = new PojoTranData();
            String tranDataseqNo = seqNoServiceImpl
                    .getBatchNo(SeqNoEnum.TRAN_DATA_NO);
            tranData.setTranDataSeqNo(tranDataseqNo);
            tranData.setAccName("accName" + new Random().nextInt(transDatasNum));
            tranData.setAccNo(RandomArugment.randomNumber(22));
            tranData.setAccName(RandomArugment.randomAccName());
            tranData.setAccType(RandomArugment.randomBoolean() ? "0" : "1");
            tranData.setBankNo("313653020010");
            tranData.setBusiDataId("11111111");
            tranData.setBusiType("00");
            tranData.setMemberId("200000000000593");
            long tranAmt = Long.parseLong(RandomArugment.randomNumber(4));
            totalAmt += tranAmt;
            tranData.setTranAmt(tranAmt);
            tranData.setStatus("01");
            Date nowDate = new Date();
            tranData.setTranFee(new BigDecimal(tranAmt * 0.003).longValue());
            String txnSeqNo = simpleDateFormat.format(nowDate);
            txnSeqNo = txnSeqNo
                    + String.format("%1$02d",
                            Long.parseLong(RandomArugment.randomNumber(2)));
            tranData.setTxnseqno(txnSeqNo);
            tranData.setApplyTime(nowDate);
            pojoTranDatas.add(tranData);
            totalCount++;
        }

        PojoTranBatch tranBatch = new PojoTranBatch();
        tranBatch.setBusiBatchId("222");
        tranBatch.setBusiType("00");
        tranBatch.setTotalAmt(totalAmt);
        tranBatch.setTotalCount(totalCount);
        tranBatch.setStatus("01");
        tranBatch.setTranBatchNo(seqNoServiceImpl
                .getBatchNo(SeqNoEnum.TRAN_BATCH_NO));
        tranBatch.addTranDatas(pojoTranDatas);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(tranBatch);
        session.getTransaction().commit();
        session.close();
    }

    @Test
    @Ignore
    public void generateBankTranBatch() {

    }
}
