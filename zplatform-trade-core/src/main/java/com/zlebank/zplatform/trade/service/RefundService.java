/* 
 * RefundService.java  
 * 
 * version TODO
 *
 * 2016年5月17日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import com.zlebank.zplatform.trade.bean.ResultBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年5月17日 下午4:25:11
 * @since 
 */
public interface RefundService {

	public ResultBean execute(String refundOrderNo,String merchNo);
}
