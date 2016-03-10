/* 
 * TransferDataServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年3月9日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.zlebank.zplatform.trade.bean.enums.SeqNoEnum;
import com.zlebank.zplatform.trade.bean.enums.TransferBatchStatusEnum;
import com.zlebank.zplatform.trade.bean.enums.TransferBusiTypeEnum;
import com.zlebank.zplatform.trade.bean.enums.TransferDataStatusEnum;
import com.zlebank.zplatform.trade.dao.TranBatchDAO;
import com.zlebank.zplatform.trade.dao.TranDataDAO;
import com.zlebank.zplatform.trade.exception.RecordsAlreadyExistsException;
import com.zlebank.zplatform.trade.model.PojoTranBatch;
import com.zlebank.zplatform.trade.model.PojoTranData;
import com.zlebank.zplatform.trade.service.SeqNoService;
import com.zlebank.zplatform.trade.service.TransferDataService;

/**
 * 划拨数据服务类
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月9日 下午7:57:14
 * @since 
 */
public class TransferDataServiceImpl implements TransferDataService{

    @Autowired
    private TranDataDAO tranDataDAO;
    @Autowired
    private TranBatchDAO tranBatchDAO;
    @Autowired
    SeqNoService seqNoService;
    
    /**
     * 保存划拨流水
     * @param datas 划拨流水Pojo
     * @return long 划拨批次号
     * @throws RecordsAlreadyExistsException 
     */
    @Override
    public long saveTransferData(TransferBusiTypeEnum type, String busiBatchNo, PojoTranData[] datas) throws RecordsAlreadyExistsException {
        
        if (datas == null || datas.length ==0) return 0;
        
        PojoTranBatch batch = new PojoTranBatch();
        // 保存划拨流水信息
        batch.setApplyTime(new Date());
        batch.setBusitype(type.getCode());
        batch.setBusiBatchNo(busiBatchNo);
        batch.setStatus(TransferBatchStatusEnum.INIT.getCode());
        batch.setTranBatchSeqNo(seqNoService.getBatchNo(SeqNoEnum.TRAN_BATCH_NO));
        batch = tranBatchDAO.merge(batch);
        // 循环数据
        for (PojoTranData data : datas) {
            if (data == null) continue;
            // 有效性检查
            checkDetails(data);
            // 保存划拨流水
            data.setTranDataSeqNo(seqNoService.getBatchNo(SeqNoEnum.TRAN_DATA_NO));
            data.setApplyTime(new Date());
            data.setStatus(TransferDataStatusEnum.INIT.getCode());
            data = tranDataDAO.merge(data);
            // 保存划拨批次统计数据
            batch.setTotalCount(addOne(batch.getTotalCount()));
            batch.setTotalAmt(addAmount(batch.getTotalAmt(), data.getTranAmt()));
            batch.setUnapproveCount(addOne(batch.getUnapproveCount()));
            batch.setUnapproveAmt(addAmount(batch.getUnapproveAmt(), data.getTranAmt()));
        }
        // 保存划拨批次
        batch = tranBatchDAO.merge(batch);
        
        return batch.getTid().longValue();
    }
    
    /**
     * 添加指定金额
     * @param totalAmt 总金额
     * @param tranAmt 被添加的金额
     * @return
     */
    private BigDecimal addAmount(BigDecimal totalAmt, BigDecimal tranAmt) {
        return totalAmt == null ? BigDecimal.ONE : totalAmt.add(tranAmt);
    }

    /**
     * 将指定的数据加1
     * @param data
     * @return
     */
    private long addOne(Long data) {
        return data == null ? 1 : data.longValue() + 1;
    }
    
    /**
     * 有效性检查
     * @param data
     * @throws RecordsAlreadyExistsException 
     */
    private void checkDetails(PojoTranData data) throws RecordsAlreadyExistsException {
        // 重复检查
        int count = tranDataDAO.getCountByInsteadDataId(data.getInsteadDataId());
        if (count > 0)
            throw new RecordsAlreadyExistsException();
    }
    
}
