package com.zlebank.zplatform.trade.dao;

import java.util.List;
import java.util.Map;

import com.zlebank.zplatform.acc.exception.AbstractBusiAcctException;
import com.zlebank.zplatform.acc.exception.AccBussinessException;
import com.zlebank.zplatform.commons.dao.BaseDAO;
import com.zlebank.zplatform.trade.bean.enums.BusinessEnum;
import com.zlebank.zplatform.trade.bean.page.QueryTransferBean;
import com.zlebank.zplatform.trade.model.PojoTranBatch;
import com.zlebank.zplatform.trade.model.PojoTranData;
import com.zlebank.zplatform.trade.model.TTranDataModel;

    
public interface TransferDataDAO extends BaseDAO<PojoTranData>{
    /**
     * 通过批次号和账务状态获取批次明细数据
     * @param batchNo
     * @return
     */
    public List<PojoTranData> findTransDataByBatchNoAndAccstatus(String batchNo);
    
    
    /**
     * 划拨明细查询（分页）
     * @param queryTransferBean
     * @param page
     * @param pageSize
     * @return
     */
    public Map<String, Object> queryTranfersDetaByPage(QueryTransferBean queryTransferBean,int page,int pageSize);
    
    /**
     * 审核驳回业务退款
     * @param transferData
     * @param businessEnum
     * @throws AccBussinessException
     * @throws AbstractBusiAcctException
     * @throws NumberFormatException
     */
    public void businessRefund(PojoTranData transferData,BusinessEnum businessEnum) throws AccBussinessException, AbstractBusiAcctException,NumberFormatException;

    /**
     * 
     * @param tid
     * @param status
     * @throws NumberFormatException 
     * @throws AbstractBusiAcctException 
     * @throws AccBussinessException 
     */
    public void singleTrailTransfer(Long tid,String status) throws AccBussinessException, AbstractBusiAcctException, NumberFormatException;
    
    /**
     * 
     * @param tid
     * @return
     */
    public PojoTranData queryTransferData(Long tid);
    
    /**
     * 
     * @param transferBatch
     */
    public void updateBatchTransferSingle(PojoTranBatch transferBatch);
    
    
    /**
     * 更新划拨数据状态
     * @param tid
     * @param status
     */
    public void updateTransferDataStatus(Long tid, String status);
    
    /**
     * 获取等待转账结果的划拨数据数量
     * @param tranBatchId
     * @return
     */
    public Long queryWaritTransferCount(Long tranBatchId);
}
