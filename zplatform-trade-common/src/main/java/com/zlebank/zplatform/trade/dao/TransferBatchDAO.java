package com.zlebank.zplatform.trade.dao;

import java.util.List;
import java.util.Map;

import com.zlebank.zplatform.commons.bean.TransferBatchQuery;
import com.zlebank.zplatform.commons.dao.BaseDAO;
import com.zlebank.zplatform.commons.dao.BasePagedQueryDAO;
import com.zlebank.zplatform.trade.bean.page.QueryTransferBean;
import com.zlebank.zplatform.trade.model.PojoTranBatch;
import com.zlebank.zplatform.trade.model.PojoTranData;

public interface TransferBatchDAO extends BaseDAO<PojoTranBatch>{

    
    
    /**
     * 查询划拨批次数据（分页）
     * @param queryTransferBean
     * @param page
     * @param pageSize
     * @return
     */
    public Map<String, Object> queryTransferBatchByPage(QueryTransferBean queryTransferBean,int page,int pageSize);
    
    /**
     * 更新划拨批次状态
     * @param transferBatch
     */
    public void updateBatchTransferFinish(PojoTranBatch transferBatch);
    
    /**
     * 划拨批次审核
     * @param batchNo
     * @param status
     */
    public void batchTrailTransfer(String batchNo,String status);
    
    /**
     * 获取划拨批次数据
     * @param batchno
     * @return
     */
    public PojoTranBatch getByBatchNo(String batchno);
    
    /**
     * 
     * @param batchNo
     * @param status
     * @return
     */
    public List<PojoTranData> queryBatchTransfer(String batchNo,String status);
}
