package com.zlebank.zplatform.trade.dao.impl;

import java.awt.color.CMMException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.commons.dao.impl.HibernateBaseDAOImpl;
import com.zlebank.zplatform.trade.dao.ITxnsInsteadPayDAO;
import com.zlebank.zplatform.trade.model.PojoBankTransferBatch;
import com.zlebank.zplatform.trade.model.PojoTxnsInsteadPay;

@Repository("txnsInsteadPayDAO")
public class TxnsInsteadPayDAOImpl extends HibernateBaseDAOImpl<PojoTxnsInsteadPay> implements ITxnsInsteadPayDAO{
	private static final Log log = LogFactory.getLog(TxnsInsteadPayDAOImpl.class);
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	@Override
	public PojoTxnsInsteadPay getByResponseFileName(String fileName) {
		String queryString = "from PojoTxnsInsteadPay where resFile = ? ";
		try {
			log.info("queryString:" + queryString);
			Query query = getSession().createQuery(queryString);
			query.setParameter(0, fileName);
			return (PojoTxnsInsteadPay) query.uniqueResult();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CMMException("M001");
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public boolean isUpload(String batchNo){
		String queryString = "from PojoTxnsInsteadPay where insteadPayNo = ? ";
		try {
			log.info("queryString:" + queryString);
			Query query = getSession().createQuery(queryString);
			query.setParameter(0, batchNo);
			List<PojoTxnsInsteadPay> txnsInsteadPayList = query.list();
			if(txnsInsteadPayList.size()>0){
				PojoTxnsInsteadPay insteadPay = txnsInsteadPayList.get(0);
				if(insteadPay.getStatus().equals("01")){
					return false;
				}else{
					return true;
				}
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CMMException("M001");
		}
		return false;
	}
	/**
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PojoTxnsInsteadPay> queryBossPayNoResult() {
		String queryString = "from PojoTxnsInsteadPay where channelCode = ? and status = ?";
		try {
			log.info("queryString:" + queryString);
			Query query = getSession().createQuery(queryString);
			query.setParameter(0, "92000002");
			query.setParameter(1, "01");
			return query.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CMMException("M001");
		}
	}
}
