/* 
 * ChannelGenerator.java  
 * 
 * version v1.3
 *
 * 2016年3月7日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.batch.spliter;

/**
 * 渠道生成器
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月7日 上午10:10:08
 * @since 
 */
public class ChannelGenerator {
    private static  ChannelGenerator instance = new ChannelGenerator();
    
    public static ChannelGenerator getInstance() {
        return instance;
    }

    private  ChannelGenerator(){}
    
    /**
     * 得到相应的渠道
     * @param channel 生成渠道所需要的条件
     * @return 渠道代码
     */
    public String getChannel(ChannelCondition channel) {
        return defaultChannel();
    }

    /**
     * 默认渠道
     * @return 默认渠道
     */
    private String defaultChannel() {
        return "10000001";
    }
}
