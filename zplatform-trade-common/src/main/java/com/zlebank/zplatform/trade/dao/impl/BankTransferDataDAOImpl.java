package com.zlebank.zplatform.trade.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.commons.bean.TransferDataQuery;
import com.zlebank.zplatform.commons.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.trade.bean.enums.InsteadPayTypeEnum;
import com.zlebank.zplatform.trade.bean.page.QueryTransferBean;
import com.zlebank.zplatform.trade.dao.BankTransferDataDAO;
import com.zlebank.zplatform.trade.model.PojoBankTransferData;
@Repository("bankTransferDataDAO")
public class BankTransferDataDAOImpl  extends
HibernateBaseDAOImpl<PojoBankTransferData>
        implements
        BankTransferDataDAO {
    private static final Log log = LogFactory.getLog(BankTransferDataDAOImpl.class);

    /**
     * 通过批次号查找划拨数据
     * 
     * @param batchNo
     * @return
     */
    @SuppressWarnings("unchecked")
	@Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public List<PojoBankTransferData> findTransDataByBatchNo(String batchNo) {
        List<PojoBankTransferData> result = null;
        String queryString = " from PojoBankTransferData where bankTranBatchId = ? and status = ?";
        try {
            log.info("queryString:" + queryString);
            Query query = getSession().createQuery(queryString);
            query.setLong(0, Long.valueOf(batchNo));
            query.setString(1, "03");
            result = query.list();
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过批次号更新划拨数据
     * 
     * @param batchNo
     * @param payType
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public void updateTransDataStatusByBatchNo(String batchNo,
            InsteadPayTypeEnum payType) {
        try {
            String hql = "update PojoBankTransferData set status = ? where batchno = ?";
            Session session = getSession();
            Query query = session.createQuery(hql);
            query.setParameter(0, payType.getCode());
            query.setParameter(1, batchNo);
            query.executeUpdate();
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public List<PojoBankTransferData> findTransDataByBatchNoAndAccstatus(String batchNo) {
        List<PojoBankTransferData> result = null;
        String queryString = "from PojoBankTransferData where batchno = ? and accstatus = ?";
        try {
            log.info("queryString:" + queryString);
            Query query = getSession().createQuery(queryString);
            query.setString(0, batchNo);
            query.setString(1, "01");
            result = query.list();
        } catch (HibernateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void batchUpdateTransData(List<PojoBankTransferData> transferDataList) {
        /*StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("update PojoBankTransferData set banktranid = ?, ");
        hqlBuffer.append("resptype = ?,");
        hqlBuffer.append("respcode = ?,");
        hqlBuffer.append("respmsg = ?,");
        hqlBuffer.append("trandate = ?,");
        hqlBuffer.append("trantime = ?, ");
        hqlBuffer.append("status = ?, ");
        hqlBuffer.append("accstatus = ? ");
        hqlBuffer.append("where tranid = ? ");
        Session session = getSession();
        for (PojoBankTransferData data : transferDataList) {
            PojoBankTransferData data_old = getTransferDataByTranId(data.getTranid());
            Query query = session.createQuery(hqlBuffer.toString());
            query.setParameter(0, data.getBanktranid());
            query.setParameter(1, data.getResptype());
            query.setParameter(2, data.getRespcode());
            query.setParameter(3, data.getRespmsg());
            query.setParameter(4, data.getTrandate());
            query.setParameter(5, data.getTrantime());
            query.setParameter(6, "S".equalsIgnoreCase(data.getResptype())? "00": "03");
            query.setParameter(7, "01");
            query.setParameter(8, data.getTranid());
            query.executeUpdate();
            
            
            query = session.createQuery("update TxnsLogModel set payretcode=?,payretinfo=?,payordfintime=?,payrettsnseqno=?,retcode=?,retinfo=?, tradeseltxn=? where txnseqno=?");
            if("S".equalsIgnoreCase(data.getResptype())){
                query.setParameter(0, "000000");
                query.setParameter(1, "交易成功");
                query.setParameter(4, "0000");
                query.setParameter(5, "交易成功");
            }else{
                query.setParameter(0, data.getRespcode());
                query.setParameter(1, data.getRespmsg());
                query.setParameter(4, data.getRespcode());
                query.setParameter(5, data.getRespmsg());
            }
            query.setParameter(2, DateUtil.getCurrentDateTime());
            query.setParameter(3, data.getBanktranid());
            query.setParameter(6, UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
            query.setParameter(7, data_old.getTxnseqno());
            query.executeUpdate();
        }
*/
    }

    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public void batchUpdateTransDataAccStatus(List<PojoBankTransferData> transferDataList) {
        // TODO Auto-generated method stub
        /*StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("update PojoBankTransferData set ");
        hqlBuffer.append("accstatus = ?,");
        hqlBuffer.append("accinfo = ?");
        hqlBuffer.append("where tranid = ? ");
        Session session = getSession();
        for(PojoBankTransferData data : transferDataList) {
            Query query = session.createQuery(hqlBuffer.toString());
            query.setParameter(0, data.getAccstatus());
            query.setParameter(1, data.getAccinfo());
            query.setParameter(2, data.getTranid());
            query.executeUpdate();
        }*/
    }

    /**
    *
    * @param e
    * @return
    */
   
   protected Criteria buildCriteria(TransferDataQuery e) {
       Criteria crite = this.getSession().createCriteria(
               PojoBankTransferData.class);

       if (e != null) {
           if(StringUtil.isNotEmpty(e.getBusicode())){
           crite.add(Restrictions.eq("busicode", e.getBusicode()));
           
           };
           if(StringUtil.isNotEmpty(e.getbatchno())){
           crite.add(Restrictions.eq("batchno", e.getbatchno()));}
           if(StringUtil.isNotEmpty(e.getTranId())){
               crite.add(Restrictions.eq("tranid", e.getTranId()));
           }
           if(StringUtil.isNotEmpty(e.getAcctType())){
               crite.add(Restrictions.eq("acctype", e.getAcctType()));
               
           }
         if(StringUtil.isNotEmpty( e.getStatus())){
             crite.add(Restrictions.eq("status", e.getStatus()));
             
         }
         
         if(StringUtil.isNotEmpty(e.getRelatedorderno())){
             crite.add(Restrictions.eq("relatedorderno",e.getRelatedorderno()));
             
         }
         
         if(e.getStartTime()!=null){
             crite.add(Restrictions.ge("createtime",e.getStartTime()));
             
         }
         
         if(e.getEndTime()!=null){
             crite.add(Restrictions.le("createtime",e.getEndTime()));
             
         }
         
         if(StringUtil.isNotEmpty(e.getBatch())){
             crite.add(Restrictions.le("associatedBatch",e.getBatch()));
             
         }


       }
       crite.addOrder(Order.asc("createtime"));
       return crite;

   }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public PojoBankTransferData getTransferDataByTranId(String tranid,String status) {
        Criteria crite = this.getSession().createCriteria(
                PojoBankTransferData.class);
        crite.add(Restrictions.eq("tranid", tranid));
        crite.add(Restrictions.eq("status", status));
       return  (PojoBankTransferData) crite.uniqueResult();
    }

    public PojoBankTransferData getTransferDataByTranId(String tranid) {
        Criteria crite = this.getSession().createCriteria(
                PojoBankTransferData.class);
        crite.add(Restrictions.eq("tranid", tranid));
       return  (PojoBankTransferData) crite.uniqueResult();
    }
    
    public static void main(String[] args) {
        System.out.println(UUID.randomUUID());
    }
    
    /**
     * 根据划拨流水ID取出转账ID
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PojoBankTransferData> getByTranDataId(Long id) {
        Criteria crite= this.getSession().createCriteria(PojoBankTransferData.class);
        crite.add(Restrictions.eq("tranData.id", id));
        return crite.list();
    }

	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> queryBankTransferDataByPage(
			QueryTransferBean queryTransferBean, int page, int pageSize) {
		StringBuffer sqlBuffer = new StringBuffer("from PojoBankTransferData where 1=1 ");
		StringBuffer sqlCountBuffer = new StringBuffer("select count(*) from PojoBankTransferData where 1=1 ");
		List<Object> parameterList = new ArrayList<Object>();
		if(queryTransferBean!=null){
			if(StringUtil.isNotEmpty(queryTransferBean.getBatchNo())){
				sqlBuffer.append(" and bankTranBatchId = ? ");
				sqlCountBuffer.append(" and bankTranBatchId = ? ");
				parameterList.add(Long.valueOf(queryTransferBean.getBatchNo()));
			}
			if(StringUtil.isNotEmpty(queryTransferBean.getStatus())){
				sqlBuffer.append(" and status = ? ");
				sqlCountBuffer.append(" and status = ? ");
				parameterList.add(queryTransferBean.getStatus());
			}
		}
		Query query = getSession().createQuery(sqlBuffer.toString());
		Query countQuery = getSession().createQuery(sqlCountBuffer.toString());
		int i = 0;
		for(Object parameter : parameterList){
			query.setParameter(i, parameter);
			countQuery.setParameter(i, parameter);
			i++;
		}
		query.setFirstResult((pageSize)*((page==0?1:page)-1));
    	query.setMaxResults(pageSize);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//System.out.println(JSON.toJSONString(countQuery.list()));
		resultMap.put("total", countQuery.uniqueResult());
		resultMap.put("rows", query.list());
		return resultMap;
	}

	@Override
	public List<PojoBankTransferData> findBankTransferDataByBankTranBatchId(
			Long bankTranBatchId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void updateWaitBankTransferStatus(String bankTranBatchId,
			String status) {
		
		 try {
	            String hql = "update PojoBankTransferData set status = ? where bankTranBatchId = ? and status = ?";
	            Session session = getSession();
	            Query query = session.createQuery(hql);
	            query.setParameter(0, status);
	            query.setParameter(1, Long.valueOf(bankTranBatchId));
	            query.setParameter(2, "01");
	            query.executeUpdate();
	        } catch (HibernateException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	}
	
	

	
}
