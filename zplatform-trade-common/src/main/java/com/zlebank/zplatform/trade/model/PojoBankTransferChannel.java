/* 
 * PojoBankTransferChannel.java  
 * 
 * version TODO
 *
 * 2016年3月7日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Class Description
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月7日 下午3:39:36
 * @since 
 */
@Entity
@Table(name="T_BANK_TRAN_CHANNEL")
public class PojoBankTransferChannel {
    /****/
    private Long id;
    /**渠道号**/
    private String channelCode;
    /**渠道名称**/
    private String channelName;
    /**批次最大笔数**/
    private Long detaCount;
    /**定时发送（分钟）**/
    private Long scheduleDeliver;
    /**每日最终发送时间（hhmmss格式）**/
    private String finalDeliverTime;
    /**银行渠道号（参照T_Channel）**/
    private String bankChannelCode;
    
    @GenericGenerator(name = "id_gen", strategy = "enhanced-table", parameters = {
            @Parameter(name = "table_name", value = "T_C_PRIMAY_KEY"),
            @Parameter(name = "value_column_name", value = "NEXT_ID"),
            @Parameter(name = "segment_column_name", value = "KEY_NAME"),
            @Parameter(name = "segment_value", value = "BANK_TRAN_CHANNEL_ID"),
            @Parameter(name = "increment_size", value = "1"),
            @Parameter(name = "optimizer", value = "pooled-lo") })
    @Id
    @GeneratedValue(generator = "id_gen")
    @Column(name = "ID")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Column(name = "CHANNEL_CODE")
    public String getChannelCode() {
        return channelCode;
    }
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }
    @Column(name = "CHANNEL_NAME")
    public String getChannelName() {
        return channelName;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    @Column(name = "DETA_COUNT")
    public Long getDetaCount() {
        return detaCount;
    }
    public void setDetaCount(Long detaCount) {
        this.detaCount = detaCount;
    }
    @Column(name = "SCHEDULE_DELIVER")
    public Long getScheduleDeliver() {
        return scheduleDeliver;
    }
    public void setScheduleDeliver(Long scheduleDeliver) {
        this.scheduleDeliver = scheduleDeliver;
    }
    @Column(name = "FINAL_DELIVER_TIME")
    public String getFinalDeliverTime() {
        return finalDeliverTime;
    }
    public void setFinalDeliverTime(String finalDeliverTime) {
        this.finalDeliverTime = finalDeliverTime;
    }
    @Column(name = "BANK_CHANNEL_CODE")
    public String getBankChannelCode() {
        return bankChannelCode;
    }
    public void setBankChannelCode(String bankChannelCode) {
        this.bankChannelCode = bankChannelCode;
    }
}
