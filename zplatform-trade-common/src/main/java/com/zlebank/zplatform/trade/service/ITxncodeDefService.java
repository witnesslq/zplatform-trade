/* 
 * ITxncodeDefService.java  
 * 
 * version TODO
 *
 * 2015年9月10日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import com.zlebank.zplatform.trade.model.TxncodeDefModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年9月10日 下午3:58:56
 * @since 
 */
public interface ITxncodeDefService extends IBaseService<TxncodeDefModel, Long>{
    public TxncodeDefModel getBusiCode(String txntype,String txnsubtype,String biztype);
}
