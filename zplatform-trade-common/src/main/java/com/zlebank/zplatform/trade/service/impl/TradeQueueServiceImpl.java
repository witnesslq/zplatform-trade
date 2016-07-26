/* 
 * TradeQueueServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年7月19日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.trade.bean.TradeQueueBean;
import com.zlebank.zplatform.trade.bean.enums.TradeQueueEnum;
import com.zlebank.zplatform.trade.service.TradeQueueService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年7月19日 下午3:37:29
 * @since 
 */
@Service("tradeQueueService")
public class TradeQueueServiceImpl implements TradeQueueService{
	private final static Log log = LogFactory.getLog(TradeQueueServiceImpl.class);
	
	
	
	private RedisTemplate<String, String> redisTemplate; 
	
	public void addTradeQueue(TradeQueueBean tradeQueueBean){
		BoundListOperations<String, String> boundListOps = redisTemplate.boundListOps(TradeQueueEnum.TRADEQUEUE.getName());
		boundListOps.rightPush(JSON.toJSONString(tradeQueueBean));
	}
	
	public void addTimeOutQueue(TradeQueueBean tradeQueueBean){
		BoundListOperations<String, String> boundListOps = redisTemplate.boundListOps(TradeQueueEnum.TIMEOUTQUEUE.getName());
		boundListOps.rightPush(JSON.toJSONString(tradeQueueBean));
	}
	
	public TradeQueueBean tradeQueuePop(){
		return queuePop(TradeQueueEnum.TRADEQUEUE);
	}
	
	public TradeQueueBean timeOutQueuePop(){
		return queuePop(TradeQueueEnum.TIMEOUTQUEUE);
	}
	
	
	public TradeQueueBean queuePop(TradeQueueEnum tradeQueueEnum){
		ListOperations<String, String> opsForList = redisTemplate.opsForList();
		String leftPop = opsForList.leftPop(tradeQueueEnum.getName());
		log.info("Queue 【"+tradeQueueEnum.getName()+"】 POP Value:"+leftPop);
		if(StringUtil.isEmpty(leftPop)){
			return null;
		}
		TradeQueueBean tradeQueueBean = JSON.parseObject(leftPop, TradeQueueBean.class);
		return tradeQueueBean;
	}
	
	public long getQueueSize(TradeQueueEnum queueEnum){
		ListOperations<String, String> opsForList = redisTemplate.opsForList();
		return opsForList.size(queueEnum.getName());
	}
	
}
