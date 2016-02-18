/* 
 * InsteadPayServiceImpl.java  
 * 
 * version v1.0
 *
 * 2015年11月25日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.commons.service.impl.AbstractBasePageService;
import com.zlebank.zplatform.commons.utils.BeanCopyUtil;
import com.zlebank.zplatform.trade.bean.InsteadPayBatchBean;
import com.zlebank.zplatform.trade.bean.InsteadPayBatchQuery;
import com.zlebank.zplatform.trade.dao.InsteadPayBatchDAO;
import com.zlebank.zplatform.trade.model.PojoInsteadPayBatch;
import com.zlebank.zplatform.trade.service.InsteadBatchService;

/**
 * 代付业务
 *
 * @author Luxiaoshuai
 * @version
 * @date 2015年11月25日 上午10:48:24
 * @since 
 */
@Service
public class InsteadBatchServiceImpl extends AbstractBasePageService<InsteadPayBatchQuery, InsteadPayBatchBean> implements InsteadBatchService{
    
    private static final Log log = LogFactory.getLog(InsteadBatchServiceImpl.class);

  
    @Autowired
    private InsteadPayBatchDAO insteadPayBatchDAO;


    /**
     *
     * @param example
     * @return
     */
    @Override
    protected long getTotal(InsteadPayBatchQuery example) {
    return     insteadPayBatchDAO.count(example);
    }


    /**
     *
     * @param offset
     * @param pageSize
     * @param example
     * @return
     */
    @Override
    protected List<InsteadPayBatchBean> getItem(int offset,
            int pageSize,
            InsteadPayBatchQuery example) {
      List<PojoInsteadPayBatch> insteadBatch=insteadPayBatchDAO.getListByQuery(offset, pageSize, example);
      List<InsteadPayBatchBean> li=new ArrayList<InsteadPayBatchBean>();
      for(PojoInsteadPayBatch pojoinstead:insteadBatch){
          InsteadPayBatchBean insteadBean= BeanCopyUtil.copyBean(InsteadPayBatchBean.class, pojoinstead);
          
          li.add(insteadBean);
      }
          return li;
    } 


}
