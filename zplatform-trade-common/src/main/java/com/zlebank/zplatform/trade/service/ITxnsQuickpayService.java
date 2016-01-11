/* 
 * ITxnsQuickpayService.java  
 * 
 * version TODO
 *
 * 2015年8月31日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service;

import java.util.List;

import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.reapal.BindBean;
import com.zlebank.zplatform.trade.bean.reapal.CreditBean;
import com.zlebank.zplatform.trade.bean.reapal.DebitBean;
import com.zlebank.zplatform.trade.bean.reapal.PayBean;
import com.zlebank.zplatform.trade.bean.reapal.QueryBean;
import com.zlebank.zplatform.trade.bean.reapal.SMSBean;
import com.zlebank.zplatform.trade.bean.zlpay.MarginRegisterBean;
import com.zlebank.zplatform.trade.bean.zlpay.MarginSmsBean;
import com.zlebank.zplatform.trade.bean.zlpay.OnlineDepositShortBean;
import com.zlebank.zplatform.trade.model.TxnsQuickpayModel;
import com.zlebank.zplatform.trade.model.TxnsWithholdingModel;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年8月31日 上午10:15:00
 * @since 
 */
public interface ITxnsQuickpayService extends IBaseService<TxnsQuickpayModel, Long>{
    void saveMobileCode(TradeBean trade,MarginSmsBean marginSmsBean);
    void updateMobileCode(ResultBean bean);
    void saveMarginRegister(TradeBean trade,MarginRegisterBean marginRegisterBean);
    void updateMarginRegister(ResultBean bean);
    void saveOnlineDepositShort(TradeBean trade,OnlineDepositShortBean onlineDepositShortBean);
    void updateOnlineDepositShort(ResultBean bean);
    TxnsQuickpayModel saveReaPayDebitSign(TradeBean trade,DebitBean creditBean);
    String saveReaPaySMS(TradeBean trade,SMSBean smsBean);
    String saveReaPayCreditSign(TradeBean trade,CreditBean creditBean);
    String saveReaPayBindSign(TradeBean trade,BindBean bindBean);
    String saveReaPayToPay(TradeBean trade,PayBean payBean);
    String saveReaPayQuery(TradeBean trade,QueryBean queryBean);
    void updateReaPaySMS(ResultBean bean,String payorderno);
    void updateReaPaySign(ResultBean bean,String payorderno);
    void updateReaPayQuery(ResultBean bean,String payorderno);
    List<TxnsQuickpayModel> queryTxnsByOrderNo(String orderNo);
    String getReapayOrderNo(String txnseqno);
    public String saveTestBindSign(TradeBean trade, BindBean bindBean);
    public void updateTestPaySign(ResultBean bean,String payorderno) ;
    public TxnsQuickpayModel saveTestDebitSign(TradeBean trade,DebitBean debitBean);
    public String saveTestCreditSign(TradeBean trade, CreditBean creditBean) ;
    public void updateTestSign(ResultBean bean,String payorderno);
    public String saveTestSMS(TradeBean trade,SMSBean smsBean) ;
    public String saveTestToPay(TradeBean trade, PayBean payBean);
    
    /**
     * 保存民生银行跨行快捷流水
     * @param trade
     * @return
     */
    public String saveCMBCOuterBankSign(TradeBean trade);
    
    /**
     * 保存民生银行跨行代扣流水
     * @param trade
     */
    public String saveCMBCOuterWithholding(TradeBean trade);
    
    /**
     * 更新民生银行快捷支付结果
     * @param withholding
     * @param payorderno
     */
    public void updateCMBCWithholdingResult(TxnsWithholdingModel withholding,String payorderno);
    
    /**
     * 更新民生银行代扣短信验证交易结果
     * @param payorder
     * @param retcode
     * @param retinfo
     */
    public void updateCMBCSMSResult(String payorder,String retcode,String retinfo);
    
    /**
     * 保存民生银行卡绑卡签约流水
     * @param realnameAuth
     * @return
     */
    public String saveCMBCOuterBankCardSign(TradeBean trade);
}
