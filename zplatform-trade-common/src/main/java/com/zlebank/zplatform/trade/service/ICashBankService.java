/* 
 * ICashBankService.java  
 * 
 * version TODO
 *
 * 2015年10月14日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import java.util.List;

import com.zlebank.zplatform.trade.model.CashBankModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年10月14日 下午3:05:59
 * @since 
 */
public interface ICashBankService extends IBaseService<CashBankModel, Long>{
    public List<CashBankModel> findBankByCashCode(String cashCode);
    public List<CashBankModel> findBankByPaytype(String payType);
    public List<CashBankModel> findBankPage(int page,int pageSize);
    public long findBankCount();
}
