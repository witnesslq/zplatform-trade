/* 
 * UpdateSubject.java  
 * 
 * version TODO
 *
 * 2016年3月16日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import com.zlebank.zplatform.trade.bean.UpdateData;

/**
 * 更新接口
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月16日 上午11:09:01
 * @since 
 */
public interface UpdateSubject {
    /**
     * 根据更新数据里的txnSeqNo找到相应的记录，进行记账和状态更新
     * @param data
     */
    public void update(UpdateData data);
    /**
     * 得到业务代码（例：00：代付 01：提现 02：退款）
     */
    public String getBusiCode();
}
