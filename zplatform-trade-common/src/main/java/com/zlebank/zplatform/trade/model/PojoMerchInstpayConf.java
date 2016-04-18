/* 
 * PojoMerchInstpayConf.java  
 * 
 * version TODO
 *
 * 2016年4月11日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 商户代付配置表
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年4月11日 下午4:17:52
 * @since 
 */
@Entity
@Table(name="T_MERCH_INSTPAY_CONF")
public class PojoMerchInstpayConf {
    /**标识**/
    private Long tid;
    /**会员Id**/
    private String memberId;
    /**是否检查实名认证 (0:不检查  1:检查)**/
    private String isCheckRealname;
    /**是否检查白名单 (0:不检查  1:检查)**/
    private String isCheckWhiteList;
    /**创建人**/
    private Long inUser;
    /**创建时间**/
    private Date inTime;
    /**修改人**/
    private Long upUser;
    /**修改时间**/
    private Date upTime;
    /**备注**/
    private String notes;
    /**备注**/
    private String remarks;
    @GenericGenerator(name = "id_gen", strategy = "enhanced-table", parameters = {
            @Parameter(name = "table_name", value = "T_C_PRIMAY_KEY"),
            @Parameter(name = "value_column_name", value = "NEXT_ID"),
            @Parameter(name = "segment_column_name", value = "KEY_NAME"),
            @Parameter(name = "segment_value", value = "MERCH_INSTPAY_CONF_ID"),
            @Parameter(name = "increment_size", value = "1"),
            @Parameter(name = "optimizer", value = "pooled-lo") })
    @Id
    @GeneratedValue(generator = "id_gen")
    @Column(name = "TID")
    public Long getTid() {
        return tid;
    }
    public void setTid(Long tid) {
        this.tid = tid;
    }
    @Column(name = "MEMBER_ID")
    public String getMemberId() {
        return memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    @Column(name = "IS_CHECK_REALNAME")
    public String getIsCheckRealname() {
        return isCheckRealname;
    }
    public void setIsCheckRealname(String isCheckRealname) {
        this.isCheckRealname = isCheckRealname;
    }
    @Column(name = "IS_CHECK_WHITE_LIST")
    public String getIsCheckWhiteList() {
        return isCheckWhiteList;
    }
    public void setIsCheckWhiteList(String isCheckWhiteList) {
        this.isCheckWhiteList = isCheckWhiteList;
    }
    @Column(name = "IN_USER")
    public Long getInUser() {
        return inUser;
    }
    public void setInUser(Long inUser) {
        this.inUser = inUser;
    }
    @Column(name = "IN_TIME")
    public Date getInTime() {
        return inTime;
    }
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }
    @Column(name = "UP_USER")
    public Long getUpUser() {
        return upUser;
    }
    public void setUpUser(Long upUser) {
        this.upUser = upUser;
    }
    @Column(name = "UP_TIME")
    public Date getUpTime() {
        return upTime;
    }
    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }
    @Column(name = "NOTES")
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    @Column(name = "REMARKS")
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
