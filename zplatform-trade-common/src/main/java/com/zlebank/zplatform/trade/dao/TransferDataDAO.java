package com.zlebank.zplatform.trade.dao;

import java.util.List;

import com.zlebank.zplatform.commons.bean.TransferDataQuery;
import com.zlebank.zplatform.commons.dao.BasePagedQueryDAO;
import com.zlebank.zplatform.trade.bean.enums.InsteadPayTypeEnum;
import com.zlebank.zplatform.trade.model.PojoTransferData;

public interface TransferDataDAO extends BasePagedQueryDAO<PojoTransferData, TransferDataQuery>{
    /**
     * 通过批次号查找划拨数据
     * @param batchNo
     * @return
     */
    public List<PojoTransferData> findTransDataByBatchNo(String batchNo);
    /**
     * 通过批次号更新划拨数据
     * @param batchNo
     * @param payType
     */
    public void updateTransDataStatusByBatchNo(String batchNo,InsteadPayTypeEnum payType);
    
    /**
     * 批量更新划拨数据
     * @param transferDataList
     */
    public void batchUpdateTransData(List<PojoTransferData> transferDataList);
    /**
     * 批量更新划拨数据-账务处理信息
     * @param transferDataList
     */
    public void batchUpdateTransDataAccStatus(List<PojoTransferData> transferDataList);
    /**
     * 通过Id得到划拨信息
     * @param id
     * @return
     */
    public PojoTransferData  getTransferDataByTranId(String tranid,String status);
    
    
    /**
     * 通过批次号和账务状态获取批次明细数据
     * @param batchNo
     * @return
     */
    public List<PojoTransferData> findTransDataByBatchNoAndAccstatus(String batchNo);
    
}
