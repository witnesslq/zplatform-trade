/* 
 * IRouteProcessService.java  
 * 
 * version TODO
 *
 * 2015年9月6日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import com.zlebank.zplatform.trade.bean.RoutBean;
import com.zlebank.zplatform.trade.model.RouteProcessModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年9月6日 上午10:35:07
 * @since 
 */
public interface IRouteProcessService extends IBaseService<RouteProcessModel, Long>{
	@Deprecated
    public RoutBean getNextRoutStep(String routId,String currentStep,String busicode);
	@Deprecated
    public RoutBean getFirstRoutStep(String routId,String busicode);
	@Deprecated
    public RoutBean getCurrentStep(String routId,String beforeStep,String busicode);
    
}
