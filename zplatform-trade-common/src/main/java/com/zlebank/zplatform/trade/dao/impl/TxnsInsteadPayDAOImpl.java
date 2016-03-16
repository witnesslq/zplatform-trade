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
}
