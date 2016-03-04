package com.zlebank.zplatform.trade.adapter.accounting.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.acc.bean.TradeInfo;
import com.zlebank.zplatform.acc.exception.AbstractBusiAcctException;
import com.zlebank.zplatform.acc.exception.AccBussinessException;
import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.commons.dao.pojo.AccStatusEnum;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.trade.adapter.accounting.IAccounting;
import com.zlebank.zplatform.trade.bean.AppPartyBean;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.dao.TransferBatchDAO;
import com.zlebank.zplatform.trade.dao.TransferDataDAO;
import com.zlebank.zplatform.trade.model.PojoTransferBatch;
import com.zlebank.zplatform.trade.model.PojoTransferData;
import com.zlebank.zplatform.trade.model.TxnsRefundModel;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.ITxnsRefundService;
import com.zlebank.zplatform.trade.utils.DateUtil;
import com.zlebank.zplatform.trade.utils.SpringContext;
/**
 * 
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年12月8日 下午4:00:46
 * @since
 */
public class RefundAccounting implements IAccounting{
    
    private TransferBatchDAO transferBatchDAO;
    private TransferDataDAO transferDataDAO;
    private AccEntryService accEntryService;
    private ITxnsRefundService txnsRefundService;
    private ITxnsLogService txnsLogService;
    public RefundAccounting(){
        transferBatchDAO = (TransferBatchDAO) SpringContext.getContext().getBean("transferBatchDAO");
        transferDataDAO = (TransferDataDAO) SpringContext.getContext().getBean("transferDataDAO");
        accEntryService = (AccEntryService) SpringContext.getContext().getBean("accEntryService");
        txnsRefundService = (ITxnsRefundService) SpringContext.getContext().getBean("txnsRefundService");
        txnsLogService = (ITxnsLogService) SpringContext.getContext().getBean("txnsLogService");
    }
    
    @Override
    public ResultBean accountedForInsteadPay(String batchno) {
        List<PojoTransferBatch> transferBatchList = transferBatchDAO.findByInsteadpaybatchno(batchno);
        for(PojoTransferBatch transferBatch : transferBatchList){
            String commiteTime = DateUtil.getCurrentDateTime();
            String transferBatchNo = transferBatch.getBatchno();
            List<PojoTransferData> transferDataList = transferDataDAO.findTransDataByBatchNoAndAccstatus(transferBatchNo);
            for(PojoTransferData transferData : transferDataList){
                TradeInfo tradeInfo = initAccountingData(transferData, transferBatch);
                String errorMsg = "";
                try {
                    accEntryService.accEntryProcess(tradeInfo);
                } catch (AccBussinessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    errorMsg=e.getMessage();
                } catch (AbstractBusiAcctException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    errorMsg=e.getMessage();
                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block cwk552 tbb533
                    e.printStackTrace();
                    errorMsg=e.getMessage();
                }
                //应用方信息
                AppPartyBean appParty = new AppPartyBean("123",
                        "99999999", commiteTime,
                        DateUtil.getCurrentDateTime(), transferData.getTxnseqno(), "");
                txnsLogService.updateAppInfo(appParty);
                if(StringUtil.isEmpty(errorMsg)){//错误信息为空，入账成功
                    transferData.setAccstatus(AccStatusEnum.Finish.getCode());
                    transferData.setAccinfo("账务处理成功");
                    txnsLogService.updateAppStatus(transferData.getTxnseqno(), AccStatusEnum.Finish.getCode(), "退款账务成功");
                    //更新原始订单信息
                    updateRefundOrder(transferData, batchno, transferBatch);
                }else{
                    transferData.setAccstatus(AccStatusEnum.AccountingFail.getCode());
                    transferData.setAccinfo(errorMsg);
                    txnsLogService.updateAppStatus(transferData.getTxnseqno(), AccStatusEnum.AccountingFail.getCode(), errorMsg);
                }  

            }
            transferDataDAO.batchUpdateTransDataAccStatus(transferDataList);
           
        }
        return null;
    }
    
    @Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor=Throwable.class)
    public void updateRefundOrder(PojoTransferData transferData,String batchno,PojoTransferBatch transferBatch){
        TxnsRefundModel refundModel = new TxnsRefundModel();
        refundModel.setStatus(transferData.getStatus());
        refundModel.setRefundinstid(transferBatch.getChnlcode());
        refundModel.setRetcode(transferData.getRespcode());
        refundModel.setRetinfo(transferData.getRespmsg());
        refundModel.setMemberid(transferBatch.getMerchid());
        refundModel.setRefundorderno(transferData.getRelatedorderno());
        txnsRefundService.updateRefundResult(refundModel);
    }

    @Override
    public ResultBean accountedFor(String txnseqno) {
        // TODO Auto-generated method stub
        return null;
    }
    private TradeInfo initAccountingData(PojoTransferData transferData,PojoTransferBatch transferBatch){
        TxnsRefundModel refund = txnsRefundService.getRefundByRefundorderNo(transferData.getRelatedorderno(),transferData.getMemberid());
        String txnseqno = transferData.getTxnseqno();
        /**支付订单号**/
        String payordno = transferData.getRelatedorderno();
        /**交易类型 - 根据交易结果进行判断，**/
        String busiCode = "";
        /**付款方会员ID**/
        String payMemberId = transferData.getMemberid();
        /**收款方会员ID**/
        String payToMemberId = refund.getMemberid();
        if(transferData.getBusicode().equals("40000001")){
            if("S".equalsIgnoreCase(transferData.getResptype())){//交易成功
                busiCode = "40000002";
            }else if("F".equalsIgnoreCase(transferData.getResptype())){//交易失败
                busiCode = "40000003";
                payMemberId = transferBatch.getMerchid();
                payToMemberId = transferBatch.getMerchid();
            }
        }
        
        
        /**收款方父级会员ID**/
        String payToParentMemberId="";
        /**渠道**/
        String channelId = transferBatch.getChnlcode();//支付机构代码
        
        /**产品id**/
        String productId = "";
        /**交易金额**/
        BigDecimal amount = new BigDecimal(transferData.getTransamt());
        /**佣金**/
        BigDecimal commission = new BigDecimal(0);
        /**手续费**/
        long txnfee = 0;
        BigDecimal charge = new BigDecimal(txnfee);
        /**金额D**/
        BigDecimal amountD = new BigDecimal(0);
        /**金额E**/
        BigDecimal amountE = new BigDecimal(0);
        TradeInfo tradeInfo = new TradeInfo(txnseqno, payordno, busiCode, payMemberId, payToMemberId, payToParentMemberId, channelId, productId, amount, commission, charge, amountD, amountE, false);
        return tradeInfo;
    }
}
