/* 
 * BankTransferDetaDAO.java  
 * 
 * version TODO
 *
 * 2016年3月7日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.dao;

import java.util.List;

import com.zlebank.zplatform.commons.dao.BaseDAO;
import com.zlebank.zplatform.trade.model.PojoBankTransferData;

/**
 * 银行转账流水DAO
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月7日 下午2:21:42
 * @since 
 */
public interface BankTransferDataDAO  extends BaseDAO<PojoBankTransferData>{
    /**
     * 根据划拨流水ID取出转账ID
     * @param id
     * @return
     */
    public List<PojoBankTransferData> getByTranDataId(Long id);
}
