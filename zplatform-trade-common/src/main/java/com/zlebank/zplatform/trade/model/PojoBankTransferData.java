/* 
 * PojoBankTransferData.java  
 * 
 * version TODO
 *
 * 2016年3月7日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.model;


import java.io.Serializable;
import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 转账流水
 *
 * @author Luxiaoshuai
 * @version
 * @date 2016年3月7日 上午11:40:30
 * @since 1.3.0
 */
@Entity
@Table(name="T_BANK_TRAN_DATA")
public class PojoBankTransferData implements Serializable{
    /**"表标识"**/
    private Long tid;
    /**"银行转账流水序列号"**/
    private String bankTranDataSeqNo; 
    /**"银行转账批次号"**/
    private long bankTranBatchId;
    /**"划拨流水号"**/
    private String tranDataId;
    /**"转账金额"**/
    private Long tranAmt;
    /**"账户号"**/
    private String accNo;
    /**"账户名"**/
    private String accName;
    /**"银行代码"**/
    private String accBankNo;
    /**"银行名称"**/
    private String accBankName;
    /**"""状态（01：未审核02：等待转账03：正在转账04:转账完成)**/
    private String status;
    /**"响应码"**/
    private String resCode;
    /**响应信息**/
    private String resInfo;
    /**"申请时间"**/
    private Date applyTime;
    /**"账户类型(0:对私账户1：对公账户)"**/
    private String accType;
    /**划拨类型，01-行内02-跨行**/
    private String transferType;
    /**付款结果**/
    private String resType;
    
    @GenericGenerator(name = "id_gen", strategy = "enhanced-table", parameters = {
            @Parameter(name = "table_name", value = "T_C_PRIMAY_KEY"),
            @Parameter(name = "value_column_name", value = "NEXT_ID"),
            @Parameter(name = "segment_column_name", value = "KEY_NAME"),
            @Parameter(name = "segment_value", value = "BANK_TRAN_DATA_ID"),
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
    @Column(name = "BANK_TRAN_DATA_SEQ_NO")
    public String getBankTranDataSeqNo() {
        return bankTranDataSeqNo;
    }
    public void setBankTranDataSeqNo(String bankTranDataSeqNo) {
        this.bankTranDataSeqNo = bankTranDataSeqNo;
    } 

    @Column(name = "TRAN_AMT")
    public Long getTranAmt() {
        return tranAmt;
    }
    public void setTranAmt(Long tranAmt) {
        this.tranAmt = tranAmt;
    }
    @Column(name = "ACC_NO")
    public String getAccNo() {
        return accNo;
    }
    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }
    @Column(name = "ACC_NAME")
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    @Column(name = "ACC_BANK_NO")
    public String getAccBankNo() {
        return accBankNo;
    }
    public void setAccBankNo(String accBankNo) {
        this.accBankNo = accBankNo;
    }
    @Column(name = "ACC_BANK_NAME")
    public String getAccBankName() {
        return accBankName;
    }
    public void setAccBankName(String accBankName) {
        this.accBankName = accBankName;
    }
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name = "RES_CODE")
    public String getResCode() {
        return resCode;
    }
    public void setResCode(String resCode) {
        this.resCode = resCode;
    }
    @Column(name = "RES_INFO")
    public String getResInfo() {
        return resInfo;
    }
    public void setResInfo(String resInfo) {
        this.resInfo = resInfo;
    }
    @Column(name = "APPLY_TIME")
    public Date getApplyTime() {
        return applyTime;
    }
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }
    @Column(name = "ACC_TYPE")
    public String getAccType() {
        return accType;
    }
    public void setAccType(String accType) {
        this.accType = accType;
    }
    @Column(name = "TRANSFER_TYPE")
    public String getTransferType() {
        return transferType;
    }
    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }
    @Column(name = "RES_TYPE")
    public String getResType() {
        return resType;
    }
    public void setResType(String resType) {
        this.resType = resType;
    }
    @Column(name = "BANK_TRAN_BATCH_ID")
	public long getBankTranBatchId() {
		return bankTranBatchId;
	}
	public void setBankTranBatchId(long bankTranBatchId) {
		this.bankTranBatchId = bankTranBatchId;
	}
	@Column(name = "TRAN_DATA_ID")
	public String getTranDataId() {
		return tranDataId;
	}
	public void setTranDataId(String tranDataId) {
		this.tranDataId = tranDataId;
	}
	/*@ManyToOne
    @JoinColumn(name="BANK_TRAN_BATCH_ID")
    public PojoBankTransferBatch getBankTranBatch() {
        return bankTranBatch;
    }
    public void setBankTranBatch(PojoBankTransferBatch bankTranBatch) {
        this.bankTranBatch = bankTranBatch;
    }*/
    
}
