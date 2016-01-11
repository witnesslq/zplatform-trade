package com.zlebank.zplatform.trade.dao;

import java.util.List;

import com.zlebank.zplatform.commons.bean.TransferBatchQuery;
import com.zlebank.zplatform.commons.dao.BasePagedQueryDAO;
import com.zlebank.zplatform.commons.dao.pojo.AccStatusEnum;
import com.zlebank.zplatform.trade.model.PojoTransferBatch;

public interface TransferBatchDAO extends BasePagedQueryDAO<PojoTransferBatch,TransferBatchQuery>{

    /**
     * 通过批次号查找批次信息
     * @param batchno
     * @return
     */
    public PojoTransferBatch getByBatchNo(String batchno);
    
    /**
     * 更新批次信息
     * @param transferBatch
     */
    public void updateBatchToTransfer(PojoTransferBatch transferBatch);
    
    /**
     * 通过上传文件名称取得批次信息
     * @param fileName
     * @return
     */
    public PojoTransferBatch getByReqestFileName(String fileName);
    
    /**
     * 通过回盘文件名称取得批次信息
     * @param fileName
     * @return
     */
    public PojoTransferBatch getByResponseFileName(String fileName);
    /**
     * 更新批次数据
     * @param transferBatch
     */
    public void updateTransferBatch(PojoTransferBatch transferBatch);
    
    /**
     * 获取待账务处理的批次数据
     * @return
     */
    public List<PojoTransferBatch> findWaitAccountingTransferBatch();
    
    /**
     * 校验代付批次中的划拨批次是否都已完成
     * @param insteadpaybatchno
     * @return 0-划拨完成，其他划拨未完成
     */
    public int validateBatchResult(String insteadpaybatchno);
    /**
     * 更新批次账务状态
     * @param batchno 批次号
     * @param accStatus 账务状态
     */
    public void updateAccountingResult(String batchno,AccStatusEnum accStatus);
    
    /**
     * 通过代付批次号查找批次数据
     * @param insteadpaybatchno
     * @return
     */
    public List<PojoTransferBatch> findByInsteadpaybatchno(String insteadpaybatchno);
}
