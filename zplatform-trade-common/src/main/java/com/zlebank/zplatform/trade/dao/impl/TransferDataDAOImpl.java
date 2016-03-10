package com.zlebank.zplatform.trade.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.acc.bean.TradeInfo;
import com.zlebank.zplatform.acc.exception.AbstractBusiAcctException;
import com.zlebank.zplatform.acc.exception.AccBussinessException;
import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.commons.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.trade.bean.enums.BusinessEnum;
import com.zlebank.zplatform.trade.bean.enums.InsteadPayTypeEnum;
import com.zlebank.zplatform.trade.bean.page.QueryTransferBean;
import com.zlebank.zplatform.trade.dao.TransferBatchDAO;
import com.zlebank.zplatform.trade.dao.TransferDataDAO;
import com.zlebank.zplatform.trade.model.PojoInsteadPayDetail;
import com.zlebank.zplatform.trade.model.PojoTranBatch;
import com.zlebank.zplatform.trade.model.PojoTranData;
@Repository("transferDataDAO")
public class TransferDataDAOImpl  extends HibernateBaseDAOImpl<PojoTranData> implements TransferDataDAO {
    private static final Log log = LogFactory.getLog(TransferDataDAOImpl.class);

    @Autowired
    private TransferBatchDAO transferBatchDAO;
    @Autowired
    private AccEntryService accEntryService;
    
    public Map<String, Object> queryTranfersDetaByPage(QueryTransferBean queryTransferBean,int page,int pageSize){
    	StringBuffer sqlBuffer = new StringBuffer("from PojoTranData where 1=1 ");
		StringBuffer sqlCountBuffer = new StringBuffer("select count(*) from PojoTranData where 1=1 ");
		List<String> parameterList = new ArrayList<String>();
		if(queryTransferBean!=null){
			if(StringUtil.isNotEmpty(queryTransferBean.getBatchNo())){
				sqlBuffer.append(" and tranBatchId = ? ");
				sqlCountBuffer.append(" and tranBatchId = ? ");
				parameterList.add(queryTransferBean.getBatchNo());
			}
			if(StringUtil.isNotEmpty(queryTransferBean.getEndDate())){
				sqlBuffer.append(" and status = ? ");
				sqlCountBuffer.append(" and status = ? ");
				parameterList.add(queryTransferBean.getStatus());
			}
		}
		Query query = getSession().createQuery(sqlBuffer.toString());
		Query countQuery = getSession().createQuery(sqlCountBuffer.toString());
		int i = 0;
		for(String parameter : parameterList){
			query.setParameter(i, parameter);
			countQuery.setParameter(i, parameter);
			i++;
		}
		query.setFirstResult((pageSize)*((page==0?1:page)-1));
    	query.setMaxResults(pageSize);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", ((Long) countQuery.uniqueResult()).longValue());
		resultMap.put("rows", query.list());
		return resultMap;
    }
    
    @Transactional(readOnly=true)
    public PojoTranData queryTransferData(String tranDataSeqNo){
    	StringBuffer sqlBuffer = new StringBuffer("from PojoTranData where 1=1 and tranDataSeqNo = ? and status = ?");
    	Query query = getSession().createQuery(sqlBuffer.toString());
    	query.setParameter(0, tranDataSeqNo);
    	query.setParameter(1, "01");
    	return (PojoTranData) query.uniqueResult();
    }
    
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public void singleTrailTransfer(String tranDataSeqNo,String status) throws AccBussinessException, AbstractBusiAcctException, NumberFormatException{
    	
    	PojoTranData transferData = queryTransferData(tranDataSeqNo);
    	//统计审核通过和不同过的数据，笔数和金额
		long approveCount = 0L;
		long approveAmount = 0L;
		long unApproveCount = 0L;
		long unApproveAmount = 0L;
		//判断划拨明细数据状态
    	PojoTranBatch transferBatch = transferBatchDAO.getByBatchNo(transferData.getTranBatchId());
    	if("00".equals(status)){//审核通过的执行分批算法
    		PojoTranData[] pojoTransferDatas = new PojoTranData[]{transferData};
    		//
			if("00".equals(transferData.getStatus())){
				approveCount++;
				approveAmount+=transferData.getTranAmt().longValue();
			}else{
				unApproveCount++;
				unApproveAmount+=transferData.getTranAmt().longValue();
			}
    		transferBatch.setApproveAmt(transferBatch.getApproveAmt()+approveAmount);
    		transferBatch.setApproveCount(approveCount+transferBatch.getApproveCount());
    		transferBatch.setUnapproveAmt(transferBatch.getUnapproveAmt()+unApproveAmount);
    		transferBatch.setUnapproveCount(unApproveCount+transferBatch.getUnapproveCount());
    		
    	}else{
			unApproveCount++;
			unApproveAmount+=transferData.getTranAmt().longValue();
			transferData.setStatus(status);
			this.update(transferData);
    		transferBatch.setUnapproveAmt(transferBatch.getUnapproveAmt()+unApproveAmount);
    		transferBatch.setUnapproveCount(unApproveCount+transferBatch.getUnapproveCount());
    		BusinessEnum businessEnum = null;
    		//00：代付01：提现02：退款
    		if("00".equals(transferBatch.getBusitype())){
    			businessEnum = BusinessEnum.INSTEADPAY_REFUND;
    		}else if("01".equals(transferBatch.getBusitype())){
    			businessEnum = BusinessEnum.WITHDRAWALS_REFUND;
    		}else if("02".equals(transferBatch.getBusitype())){
    			businessEnum = BusinessEnum.REFUND_REFUND;
    		}
    		businessRefund(transferData,businessEnum);
    	}
    	updateBatchTransferSingle(transferBatch);
    }
    
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public void businessRefund(PojoTranData transferData,BusinessEnum businessEnum) throws AccBussinessException, AbstractBusiAcctException,NumberFormatException {
        TradeInfo tradeInfo = new TradeInfo();
        tradeInfo.setAmount(transferData.getTranAmt());
        tradeInfo.setBusiCode(businessEnum.getBusiCode());
        tradeInfo.setPayMemberId(transferData.getMemberId());
        tradeInfo.setTxnseqno(transferData.getTxnseqno());
        //tradeInfo.setTxnseqno(pojoinstead.getOrderId());
        tradeInfo.setCommission(new BigDecimal(0));
        tradeInfo.setCharge(transferData.getTranFee());
        accEntryService.accEntryProcess(tradeInfo);
    }
    
    
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public void updateBatchTransferSingle(PojoTranBatch transferBatch){
    	StringBuffer sqlBuffer = new StringBuffer("select count(*) from PojoTranData where 1=1 and tranBatchId = ? and status = ?");
    	Query query = getSession().createQuery(sqlBuffer.toString());
    	query.setParameter(0, transferBatch.getBusiBatchNo());
    	query.setParameter(1, "01");
    	Long count = ((Long) query.uniqueResult()).longValue();
    	if(count==0){
    		transferBatch.setStatus("03");
    		transferBatch.setApproveFinishTime(new Date());
    	}else{
    		transferBatch.setStatus("02");
    	}
    	transferBatchDAO.updateBatchTransferFinish(transferBatch);
	}

	@Override
	public List<PojoTranData> findTransDataByBatchNoAndAccstatus(String batchNo) {
		// TODO Auto-generated method stub
		return null;
	}
}
