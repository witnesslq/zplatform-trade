/* 
 * TransferDataService.java  
 * 
 * version TODO
 *
 * 2016年3月9日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import java.util.List;

import com.zlebank.zplatform.trade.bean.enums.TransferBusiTypeEnum;
import com.zlebank.zplatform.trade.exception.RecordsAlreadyExistsException;
import com.zlebank.zplatform.trade.model.PojoTranData;

/**
 * 划拨数据服务类
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月9日 下午7:56:42
 * @since 
 */
public interface TransferDataService {
    /**
     * 保存划拨流水
     * @param datas 划拨流水Pojo
     * @return long 划拨批次号
     * @throws RecordsAlreadyExistsException 
     */
    public long saveTransferData(TransferBusiTypeEnum type, Long busiBatchId, List<PojoTranData> datas) throws RecordsAlreadyExistsException;
}