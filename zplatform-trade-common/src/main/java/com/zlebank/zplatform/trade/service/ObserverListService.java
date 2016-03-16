/* 
 * ObserverListService.java  
 * 
 * version TODO
 *
 * 2016年3月16日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Class Description
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月16日 下午4:42:35
 * @since 
 */
@Service
public class ObserverListService {
    private static ObserverListService instance = new ObserverListService();
    List<UpdateSubject> observerList; 
    
    private ObserverListService(){}
    public static ObserverListService getInstance() {
        return instance;
    }
    public void add(UpdateSubject subject) {
        if (observerList == null || observerList.size() == 0) 
            observerList = new ArrayList<UpdateSubject>();
        observerList.add(subject);
    }
    public List<UpdateSubject> getObserverList() {
        return observerList;
    }
    public void setObserverList(List<UpdateSubject> observerList) {
        this.observerList = observerList;
    }
}
