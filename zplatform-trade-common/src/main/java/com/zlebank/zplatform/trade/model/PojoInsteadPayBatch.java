/* 
 * PojoInsteadPayBatch.java  
 * 
 * version TODO
 *
 * 2015年11月24日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 代付批次表
 *
 * @author Luxiaoshuai
 * @version
 * @date 2015年11月24日 下午12:07:25
 * @since 
 */
@Entity
@Table(name="T_INSTEAD_PAY_BATCH")
public class PojoInsteadPayBatch {

    /**标识**/
    private Long id;
    /**提交的日期**/
    private String submitDate;
    /**批次号**/
    private Long batchNo;
    /**商户号**/
    private String merId;
    /**订单发送时间(yyyyMMddhhmmss)**/
    private String txnTime;
    /**总笔数**/
    private Long totalQty;
    /**总金额**/
    private BigDecimal totalAmt;
    /**状态(00:已处理01:未处理)**/
    private String status;
    /**创建人**/
    private Long inuser;
    /**创建时间**/
    private Date intime;
    /**修改人**/
    private Long upuser;
    /**修改时间**/
    private Date uptime;
    /**备注**/
    private String notes;
    /**批次明细**/
    private List<PojoInsteadPayDetail> details = new ArrayList<PojoInsteadPayDetail>();

    @OneToMany(mappedBy="insteadPayBatch",fetch=FetchType.EAGER)
    public List<PojoInsteadPayDetail> getDetails() {
        return details;
    }
    public void setDetails(List<PojoInsteadPayDetail> details) {
        this.details = details;
    }
    @GenericGenerator(name = "id_gen", strategy = "enhanced-table", parameters = {
            @Parameter(name = "table_name", value = "T_C_PRIMAY_KEY"),
            @Parameter(name = "value_column_name", value = "NEXT_ID"),
            @Parameter(name = "segment_column_name", value = "KEY_NAME"),
            @Parameter(name = "segment_value", value = "INSTEAD_PAY_BATCH_ID"),
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
    @Column(name = "SUBMIT_DATE")
    public String getSubmitDate() {
        return submitDate;
    }
    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }
    @Column(name = "BATCH_NO")
    public Long getBatchNo() {
        return batchNo;
    }
    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }
    @Column(name = "MER_ID")
    public String getMerId() {
        return merId;
    }
    public void setMerId(String merId) {
        this.merId = merId;
    }
    @Column(name = "TXN_TIME")
    public String getTxnTime() {
        return txnTime;
    }
    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }
    @Column(name = "TOTAL_QTY")
    public Long getTotalQty() {
        return totalQty;
    }
    public void setTotalQty(Long totalQty) {
        this.totalQty = totalQty;
    }
    @Column(name = "TOTAL_AMT")
    public BigDecimal getTotalAmt() {
        return totalAmt;
    }
    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name = "INUSER")
    public Long getInuser() {
        return inuser;
    }
    public void setInuser(Long inuser) {
        this.inuser = inuser;
    }
    @Column(name = "INTIME")
    public Date getIntime() {
        return intime;
    }
    public void setIntime(Date intime) {
        this.intime = intime;
    }
    @Column(name = "UPUSER")
    public Long getUpuser() {
        return upuser;
    }
    public void setUpuser(Long upuser) {
        this.upuser = upuser;
    }
    @Column(name = "UPTIME")
    public Date getUptime() {
        return uptime;
    }
    public void setUptime(Date uptime) {
        this.uptime = uptime;
    }
    @Column(name = "NOTES")
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

}
