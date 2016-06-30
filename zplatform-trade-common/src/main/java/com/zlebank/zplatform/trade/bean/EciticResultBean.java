/* 
 * EciticResultBean.java  
 * 
 * version TODO
 *
 * 2015年9月9日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.bean;

import java.io.Serializable;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年9月9日 上午9:42:36
 * @since 
 */
public class EciticResultBean implements Serializable{
    private String retcode;
    private String merid;
    private String tranamt;
    private String currency;
    private String orderno;
    private String merjnlno;
    private String tranjnlno;
    private String stt;
    private String trantype;
    private String trandate;
    private String trantime;
    private String accno;
    private String cardtype;
    private String paybankname;
    private String mernote;
    private String reqreserved;
    /**
     * @return the retcode
     */
    public String getRetcode() {
        return retcode;
    }
    /**
     * @param retcode the retcode to set
     */
    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }
    /**
     * @return the merid
     */
    public String getMerid() {
        return merid;
    }
    /**
     * @param merid the merid to set
     */
    public void setMerid(String merid) {
        this.merid = merid;
    }
    /**
     * @return the tranamt
     */
    public String getTranamt() {
        return tranamt;
    }
    /**
     * @param tranamt the tranamt to set
     */
    public void setTranamt(String tranamt) {
        this.tranamt = tranamt;
    }
    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }
    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    /**
     * @return the orderno
     */
    public String getOrderno() {
        return orderno;
    }
    /**
     * @param orderno the orderno to set
     */
    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }
    /**
     * @return the merjnlno
     */
    public String getMerjnlno() {
        return merjnlno;
    }
    /**
     * @param merjnlno the merjnlno to set
     */
    public void setMerjnlno(String merjnlno) {
        this.merjnlno = merjnlno;
    }
    /**
     * @return the tranjnlno
     */
    public String getTranjnlno() {
        return tranjnlno;
    }
    /**
     * @param tranjnlno the tranjnlno to set
     */
    public void setTranjnlno(String tranjnlno) {
        this.tranjnlno = tranjnlno;
    }
    /**
     * @return the stt
     */
    public String getStt() {
        return stt;
    }
    /**
     * @param stt the stt to set
     */
    public void setStt(String stt) {
        this.stt = stt;
    }
    /**
     * @return the trantype
     */
    public String getTrantype() {
        return trantype;
    }
    /**
     * @param trantype the trantype to set
     */
    public void setTrantype(String trantype) {
        this.trantype = trantype;
    }
    /**
     * @return the trandate
     */
    public String getTrandate() {
        return trandate;
    }
    /**
     * @param trandate the trandate to set
     */
    public void setTrandate(String trandate) {
        this.trandate = trandate;
    }
    /**
     * @return the trantime
     */
    public String getTrantime() {
        return trantime;
    }
    /**
     * @param trantime the trantime to set
     */
    public void setTrantime(String trantime) {
        this.trantime = trantime;
    }
    /**
     * @return the accno
     */
    public String getAccno() {
        return accno;
    }
    /**
     * @param accno the accno to set
     */
    public void setAccno(String accno) {
        this.accno = accno;
    }
    /**
     * @return the cardtype
     */
    public String getCardtype() {
        return cardtype;
    }
    /**
     * @param cardtype the cardtype to set
     */
    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }
    /**
     * @return the paybankname
     */
    public String getPaybankname() {
        return paybankname;
    }
    /**
     * @param paybankname the paybankname to set
     */
    public void setPaybankname(String paybankname) {
        this.paybankname = paybankname;
    }
    /**
     * @return the mernote
     */
    public String getMernote() {
        return mernote;
    }
    /**
     * @param mernote the mernote to set
     */
    public void setMernote(String mernote) {
        this.mernote = mernote;
    }
    /**
     * @return the reqreserved
     */
    public String getReqreserved() {
        return reqreserved;
    }
    /**
     * @param reqreserved the reqreserved to set
     */
    public void setReqreserved(String reqreserved) {
        this.reqreserved = reqreserved;
    }
    /**
     * @param retcode
     * @param merid
     * @param tranamt
     * @param currency
     * @param orderno
     * @param merjnlno
     * @param tranjnlno
     * @param stt
     * @param trantype
     * @param trandate
     * @param trantime
     * @param accno
     * @param cardtype
     * @param paybankname
     * @param mernote
     * @param reqreserved
     */
    public EciticResultBean(String retcode, String merid, String tranamt,
            String currency, String orderno, String merjnlno, String tranjnlno,
            String stt, String trantype, String trandate, String trantime,
            String accno, String cardtype, String paybankname, String mernote,
            String reqreserved) {
        super();
        this.retcode = retcode;
        this.merid = merid;
        this.tranamt = tranamt;
        this.currency = currency;
        this.orderno = orderno;
        this.merjnlno = merjnlno;
        this.tranjnlno = tranjnlno;
        this.stt = stt;
        this.trantype = trantype;
        this.trandate = trandate;
        this.trantime = trantime;
        this.accno = accno;
        this.cardtype = cardtype;
        this.paybankname = paybankname;
        this.mernote = mernote;
        this.reqreserved = reqreserved;
    }
    
    

}
