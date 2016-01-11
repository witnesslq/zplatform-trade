/* 
 * RouteProcessServiceImpl.java  
 * 
 * version TODO
 *
 * 2015年9月6日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.trade.bean.RoutBean;
import com.zlebank.zplatform.trade.dao.IRouteProcessDAO;
import com.zlebank.zplatform.trade.model.RouteProcessModel;
import com.zlebank.zplatform.trade.service.IRouteProcessService;
import com.zlebank.zplatform.trade.service.base.BaseServiceImpl;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年9月6日 上午10:35:45
 * @since 
 */
@Service("routeProcessService")
public class RouteProcessServiceImpl extends BaseServiceImpl<RouteProcessModel, Long> implements IRouteProcessService{

    @Autowired
    private IRouteProcessDAO routeProcessDAO;
    /**
     *
     * @return
     */
    @Override
    public Session getSession() {
        // TODO Auto-generated method stub
        return routeProcessDAO.getSession();
    }
    
    public RoutBean getNextRoutStep(String routId,String currentStep,String busicode){
        String queryString = " from RouteProcessModel rp where rp.routver=? and rp.nowstep=? ";
        RouteProcessModel  routeProcess = super.getUniqueByHQL(queryString, new Object[]{routId,currentStep});
        return new RoutBean(routeProcess);
        //return super.getUniqueByHQL(queryString, new Object[]{routId,currentStep}).getNextstep();
    }
    
    public RoutBean getFirstRoutStep(String routId,String busicode){
        String queryString = " from RouteProcessModel rp where rp.routver=? and rp.laststep = ? ";
        RouteProcessModel  routeProcess = super.getUniqueByHQL(queryString, new Object[]{routId,"00000000"});
        return new RoutBean(routeProcess);
        //return super.getUniqueByHQL(queryString, new Object[]{routId,"00000000"}).getNowstep();
    }
    
    public RoutBean getCurrentStep(String routId,String beforeStep,String busicode){
        String queryString = " from RouteProcessModel rp where rp.routver=? and rp.laststep=? ";
        RouteProcessModel  routeProcess = super.getUniqueByHQL(queryString, new Object[]{routId,"00000000"});
        return new RoutBean(routeProcess);
        //return super.getUniqueByHQL(queryString, new Object[]{routId,beforeStep}).getNowstep();
    }
    

}
