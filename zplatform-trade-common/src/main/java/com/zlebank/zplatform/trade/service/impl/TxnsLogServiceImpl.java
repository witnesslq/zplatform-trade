/* 
 * TxnsLogServiceImpl.java  
 * 
 * version TODO
 *
 * 2015年8月29日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.trade.bean.AccountTradeBean;
import com.zlebank.zplatform.trade.bean.AppPartyBean;
import com.zlebank.zplatform.trade.bean.PayPartyBean;
import com.zlebank.zplatform.trade.bean.ReaPayResultBean;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.enums.ChannelEnmu;
import com.zlebank.zplatform.trade.bean.enums.ChnlTypeEnum;
import com.zlebank.zplatform.trade.bean.enums.RiskLevelEnum;
import com.zlebank.zplatform.trade.bean.gateway.QueryBean;
import com.zlebank.zplatform.trade.dao.ITxnsLogDAO;
import com.zlebank.zplatform.trade.dao.RspmsgDAO;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.MemberBaseModel;
import com.zlebank.zplatform.trade.model.PojoBankTransferData;
import com.zlebank.zplatform.trade.model.PojoRspmsg;
import com.zlebank.zplatform.trade.model.RiskTradeLogModel;
import com.zlebank.zplatform.trade.model.TxncodeDefModel;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsWithholdingModel;
import com.zlebank.zplatform.trade.service.IMemberService;
import com.zlebank.zplatform.trade.service.IRiskTradeLogService;
import com.zlebank.zplatform.trade.service.ITxncodeDefService;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.base.BaseServiceImpl;
import com.zlebank.zplatform.trade.utils.ConsUtil;
import com.zlebank.zplatform.trade.utils.DateUtil;
import com.zlebank.zplatform.trade.utils.OrderNumber;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年8月29日 下午3:25:00
 * @since 
 */
@Service("txnsLogService")
public class TxnsLogServiceImpl extends BaseServiceImpl<TxnsLogModel, String> implements ITxnsLogService{
    private static final Log log = LogFactory.getLog(TxnsLogServiceImpl.class);
    @Autowired
    private ITxnsLogDAO txnsLogDAO;
    @Autowired 
    private ITxncodeDefService txncodeDefService;
    @Autowired 
    private RspmsgDAO rspmsgDAO;
    @Autowired
    private IMemberService memberService;
    @Autowired
    private IRiskTradeLogService riskTradeLogService;
    
    /**
     *
     * @return
     */
    @Override
    public Session getSession() {
        // TODO Auto-generated method stub
        return txnsLogDAO.getSession();
    }
    
    
    public ResultBean verifyRepeatOrder(String orderId){
        ResultBean resultBean = null;
        String queryString = "from TxnsLogModel txn where accordno = ?";
        TxnsLogModel txnsLog = super.getUniqueByHQL(queryString, new Object[]{orderId});
        if(txnsLog!=null){
            resultBean = new ResultBean("RC37", "订单号重复");
        }else{
            resultBean = new ResultBean("success");
        }
        return resultBean;
    }
    
    
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public ResultBean updatePayInfo_Fast(PayPartyBean payPartyBean){
        TxnsLogModel txnsLog = getTxnsLogByTxnseqno(payPartyBean.getTxnseqno());
            /*TxnsLogModel txnsLog = getTxnsLogByTxnseqno(payPartyBean.getTxnseqno());
            txnsLog.setPaytype("01"); //支付类型（01：快捷，02：网银，03：账户）
            txnsLog.setPayordno(payPartyBean.getPayordno());//支付定单号
            txnsLog.setPayinst(payPartyBean.getPayinst());//支付所属机构
            txnsLog.setPayfirmerno(payPartyBean.getPayfirmerno());//支付一级商户号
            //txnsLog.setPaysecmerno(payPartyBean.getPaysecmerno());//支付二级商户号
            txnsLog.setPayordcomtime(payPartyBean.getPayordcomtime());//支付定单提交时间
            
            //卡信息
            txnsLog.setPan(payPartyBean.getCardNo());
            
            txnsLog.setCardtype(cardMap.get("TYPE").toString());
            txnsLog.setCardinstino(cardMap.get("BANKCODE").toString());
            txnsLog.setTxnfee(getTxnFee(txnsLog));
            updateTxnsLog(txnsLog);*/
        Map<String, Object> cardMap = getCardInfo(payPartyBean.getCardNo());
        String hql = "update TxnsLogModel set paytype=?,payordno=?,payinst=?,payfirmerno=?,payordcomtime=?,pan=?,cardtype=?,cardinstino=?,txnfee=? where txnseqno=?";
        super.updateByHQL(hql, new Object[]{"01",payPartyBean.getPayordno(),payPartyBean.getPayinst(),payPartyBean.getPayfirmerno(),payPartyBean.getPayordcomtime(),
                    payPartyBean.getCardNo(),cardMap.get("TYPE").toString(),cardMap.get("BANKCODE").toString(),getTxnFee(txnsLog),payPartyBean.getTxnseqno()});
        return null;
    }
    
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void updatePayInfo_Fast_result(String txnseqno,String retcode,String retinfo){
        TxnsLogModel txnsLog = super.get(txnseqno);
        txnsLog.setPayretcode(retcode);
        txnsLog.setPayretinfo(retinfo);
        super.update(txnsLog);
    }
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void updateCoreRetResult(String txnseqno,String retcode,String retinfo){
        TxnsLogModel txnsLog = super.get(txnseqno);
        txnsLog.setRetcode(retcode);
        txnsLog.setRetinfo(retinfo);
        super.update(txnsLog);
    }
    
    
    @Transactional
    public ResultBean updatePayInfo_ecitic(PayPartyBean payPartyBean){
        ResultBean resultBean =null;
          try {
            TxnsLogModel txnsLog = super.get(payPartyBean.getTxnseqno());
            txnsLog.setPaytype(payPartyBean.getPaytype()); //支付类型（01：快捷，02：网银，03：账户）
            txnsLog.setPayordno(payPartyBean.getPayordno());//支付定单号
            txnsLog.setPayinst("97000001");//支付所属机构
            txnsLog.setPayfirmerno(payPartyBean.getPayfirmerno());//支付一级商户号
            //txnsLog.setPaysecmerno(payPartyBean.getPaysecmerno());//支付二级商户号
            txnsLog.setPayordcomtime(payPartyBean.getPayordcomtime());//支付定单提交时间
            
            //更新路由信息
            txnsLog.setRoutver(payPartyBean.getRout());
            txnsLog.setTxncode(payPartyBean.getRoutlvl());
            
            //更新交易流水表交易位
            txnsLog.setRelate("01000000");
            txnsLog.setTradetxnflag("01000000");
            txnsLog.setCashcode(payPartyBean.getCashCode());
            //支付定单完成时间
            super.update(txnsLog);
            resultBean = new ResultBean("success");
        } catch (Exception e) {
            resultBean = new ResultBean("RC33","业务处理失败");
            log.error(e, e);
        }
        return resultBean;
    }
    @Transactional
    public ResultBean updateRoutInfo(String txnseqno,String routId,String currentStep,String cashCode){
        ResultBean resultBean =null;
        try {
          TxnsLogModel txnsLog = super.get(txnseqno);
          txnsLog.setRoutver(routId);
          txnsLog.setTxncode(currentStep);
          txnsLog.setCashcode(cashCode);
          super.update(txnsLog);
          resultBean = new ResultBean("success");
      } catch (Exception e) {
          resultBean = new ResultBean("RC33","业务处理失败");
      }
      return resultBean;
    }
    
    @Transactional
    public void updateWapRoutInfo(String txnseqno,String routId) throws TradeException{
        try {
          TxnsLogModel txnsLog = super.get(txnseqno);
          txnsLog.setRoutver(routId);
          //txnsLog.setTxncode(currentStep);
          //txnsLog.setCashcode(cashCode);
          super.update(txnsLog);
      } catch (Exception e) {
          throw new TradeException("T007");
      }
      
    }
    
    private Map<String, Object> getCardInfo(String cardNo){
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT type,bankcode,bankname ");
        sqlBuffer.append("FROM (SELECT t.TYPE,t.BANKCODE,b.bankname ");
        sqlBuffer.append("FROM t_card_bin t, t_bank_insti b ");
        sqlBuffer.append("WHERE t.bankcode = b.bankcode ");
        sqlBuffer.append("AND ? LIKE t.cardbin || '%' ");
        sqlBuffer.append("AND t.cardlen = ? ");
        sqlBuffer.append("ORDER BY t.cardbin DESC) ");
        sqlBuffer.append("WHERE ROWNUM = 1 ");
        List<Map<String, Object>> routList =  (List<Map<String, Object>>) super.queryBySQL(sqlBuffer.toString(), new Object[]{cardNo,cardNo.trim().length()});
       
        if(routList.size()>0){
            return routList.get(0);
        }
        return null;
    }
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public ResultBean updateAppInfo(AppPartyBean appParty){
        ResultBean resultBean =null;
        try {
            String hql = "update TxnsLogModel set appinst=?,appordcommitime=?,appordno=?,appordfintime=?,accordfintime=? where txnseqno = ?";
            int row = super.updateByHQL(hql, new Object[]{appParty.getAppinst(),appParty.getAppordcommitime(),appParty.getAppordno(),appParty.getAppordfintime(),DateUtil.getCurrentDateTime(),appParty.getTxnseqno()});
            log.info("effect rows:"+row);
            resultBean = new ResultBean("success");
        } catch (Exception e) {
            resultBean = new ResultBean("RC33","业务处理失败");
        }
        return resultBean;
    }


    /**
     *
     * @param txnseqno
     * @return
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public TxnsLogModel getTxnsLogByTxnseqno(String txnseqno) {
        return super.get(txnseqno);
    }


    /**
     *
     * @param txnsLog
     * @return
     */
    @Override
    public Long getTxnFee(TxnsLogModel txnsLog){
        //交易序列号，扣率版本，业务类型，交易金额，会员号，原交易序列号，卡类型 
        List<Map<String, Object>> feeList = (List<Map<String, Object>>) super.queryBySQL("select FNC_GETFEES(?,?,?,?,?,?,?) as fee from dual", 
                new Object[]{txnsLog.getTxnseqno(),txnsLog.getFeever(),txnsLog.getBusicode(),txnsLog.getAmount(),txnsLog.getAccfirmerno(),txnsLog.getTxnseqnoOg(),txnsLog.getCardtype()});
        if(feeList.size()>0){
            if(StringUtil.isNull(feeList.get(0).get("FEE"))){
                return 0L;
            }else{
                return Long.valueOf(feeList.get(0).get("FEE")+"");
            }
            
        }
        return 0L;
       
    }


    /**
     *
     * @param txnseqno
     * @param payResult
     */
    @Override
    @Transactional
    public void updateReaPayRetInfo(String txnseqno, ReaPayResultBean payResult) {
        TxnsLogModel txnsLog = super.get(txnseqno);
        txnsLog.setPayretinfo(payResult.getResult_msg());
        txnsLog.setPayretcode(payResult.getResult_code());
        try {
            PojoRspmsg msg =   rspmsgDAO.getRspmsgByChnlCode(ChnlTypeEnum.REAPAY,payResult.getResult_code());
            txnsLog.setRetinfo(msg.getRspinfo());
            txnsLog.setRetcode(msg.getWebrspcode());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.update(txnsLog);
    }
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void updateCMBCWithholdingRetInfo(String txnseqno, TxnsWithholdingModel withholdin) {
        TxnsLogModel txnsLog = super.get(txnseqno);
        txnsLog.setPayretinfo(withholdin.getExecmsg());
        txnsLog.setPayretcode(withholdin.getExeccode());
        try {
            PojoRspmsg msg =   rspmsgDAO.getRspmsgByChnlCode(ChnlTypeEnum.CMBCWITHHOLDING,withholdin.getExeccode().trim());
            txnsLog.setRetinfo(msg.getRspinfo());
            txnsLog.setRetcode(msg.getWebrspcode());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        txnsLog.setRetinfo(withholdin.getExecmsg());
        txnsLog.setRetcode(withholdin.getExeccode());
        super.update(txnsLog);
    }

    /**
     *
     * @param queryBean
     * @return
     */
    @Override
    public TxnsLogModel queryTrade(QueryBean queryBean) {
        TxncodeDefModel busiModel = txncodeDefService.getBusiCode(queryBean.getTxnType(), queryBean.getTxnSubType(), queryBean.getBizType());
        List<Object> paramList = new ArrayList<Object>();
        StringBuffer queryBuffer = new StringBuffer("from TxnsLogModel where busicode=? and accfirmerno = ? ");
        paramList.add(busiModel.getBusicode());
        paramList.add(queryBean.getMerId());
        if(StringUtil.isNotEmpty(queryBean.getOrderId())){//受理订单号
            queryBuffer.append(" and accordno = ?");
            paramList.add(queryBean.getOrderId());
        }
        if(StringUtil.isNotEmpty(queryBean.getQueryId())){//交易查询码
            queryBuffer.append(" and tradeseltxn = ?");
            paramList.add(queryBean.getQueryId());
        }
        List<TxnsLogModel> resultList = (List<TxnsLogModel>) super.queryByHQL(queryBuffer.toString(), paramList.toArray());
        if(resultList.size()>0){
            return resultList.get(0);
        }
        return null;
    }
    
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void saveAccountTrade(AccountTradeBean accountTrade) throws TradeException{
        
        try {
          TxnsLogModel txnsLog = super.get(accountTrade.getTxnseqno());
          txnsLog.setPaytype("03"); //支付类型（01：快捷，02：网银，03：账户）
          txnsLog.setPayordno(OrderNumber.getInstance().generateAppOrderNo());//支付定单号
          txnsLog.setPayinst("99999999");//支付所属机构
          txnsLog.setPayfirmerno(accountTrade.getMemberId());//支付一级商户号-个人会员
          //txnsLog.setPaysecmerno(payPartyBean.getPaysecmerno());//支付二级商户号
          txnsLog.setPayordcomtime(DateUtil.getCurrentDateTime());//支付定单提交时间
          
          //更新交易流水表交易位
          txnsLog.setRelate("01000000");
          txnsLog.setTradetxnflag("01000000");
          txnsLog.setCashcode("");
          //支付定单完成时间
          super.update(txnsLog);
      } catch (Exception e) {
         throw new TradeException("AC03");
      }
      
    }
    
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void updateAccountTrade(AccountTradeBean accountTrade,ResultBean resultBean) throws TradeException{
        try {
            TxnsLogModel txnsLog = super.get(accountTrade.getTxnseqno());
            txnsLog.setPayretinfo(resultBean.getErrMsg());
            txnsLog.setPayretcode(resultBean.getErrCode());
            if(resultBean.isResultBool()){
                txnsLog.setPayordfintime(DateUtil.getCurrentDateTime());
                txnsLog.setRetcode("0000");
                txnsLog.setRetinfo("支付成功");
            }else{
                txnsLog.setRetcode(resultBean.getErrCode());
                txnsLog.setRetinfo(resultBean.getErrMsg());
            }
            super.update(txnsLog);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new TradeException("AC06");
        }
    }
    
    public TxnsLogModel queryLogByTradeseltxn(String queryId){
        return super.getUniqueByHQL(" from TxnsLogModel where  tradeseltxn = ? ", new Object[]{queryId});
    }
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void updateAppStatus(String txnseqno,String appOrderStatus,String appOrderinfo){
        String hql = "update TxnsLogModel set appordfintime = ?,apporderstatus = ?,apporderinfo = ? where txnseqno = ?";
        super.updateByHQL(hql,new Object[]{DateUtil.getCurrentDateTime(),appOrderStatus,appOrderinfo,txnseqno});
    }
    @Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor=Throwable.class) 
    public void tradeRiskControl(String txnseqno,String merchId,String subMerchId,String memberId,String busiCode,String txnAmt,String cardType,String cardNo) throws TradeException{
        log.info("trade risk control start");
        int riskLevel = 0;
        int riskOrder = 0;
        RiskLevelEnum riskLevelEnum = null;
        String riskInfo = "";
        log.info("risk paramaters:"+merchId+"|"+subMerchId+"|"+memberId+"|"+busiCode+"|"+txnAmt+"|"+cardType+"|"+cardNo);
        List<Map<String, Object>> riskList = (List<Map<String, Object>>) super.queryBySQL("SELECT FNC_GETRISK(?,?,?,?,?,?,?) AS RISK FROM DUAL", 
                new Object[]{merchId,subMerchId,memberId,busiCode,txnAmt,cardType,cardNo});
        log.info("trade risk result:"+JSON.toJSONString(riskList));
        if(riskList.size()>0){
            riskInfo = riskList.get(0).get("RISK")+"";
            String[] riskInfos =riskInfo.split(",");
            riskOrder = Integer.valueOf(riskInfos[0]);
            riskLevel = Integer.valueOf(riskInfos[1]);
            riskLevelEnum = RiskLevelEnum.fromValue(riskLevel);
            log.info("riskOrder:"+riskOrder);
            log.info("riskLevel:"+riskLevel);
        }else{
            throw new TradeException("T034");
        }
        if(RiskLevelEnum.PASS==riskLevelEnum){//交易通过
            return;
        }
        //记录风控日志
        List<Map<String,String>> riskStrategyList =  getRiskStrategy(riskOrder);
        log.info("riskStrategyList:"+JSON.toJSONString(riskStrategyList));
        if(riskStrategyList.size()>0){
            TxnsLogModel txnsLog = getTxnsLogByTxnseqno(txnseqno);
            Map<String,String> riskMap = riskStrategyList.get(0);
            RiskTradeLogModel tradeLog = new RiskTradeLogModel();
            tradeLog.setRiskTradeType(riskOrder);
            tradeLog.setTxnseqno(txnseqno);
            tradeLog.setOrderno(txnsLog.getAccordno());
            tradeLog.setMerchno(txnsLog.getAccfirmerno());
            //tradeLog.setMerchname(txnsLog.getaccm);
            tradeLog.setSubmerchno(txnsLog.getAccsecmerno());
            // tradeLog.setSummerchname("");
            tradeLog.setMemberid(txnsLog.getAccmemberid());
            tradeLog.setAmount(txnsLog.getAmount());
            tradeLog.setPan(cardNo); 
            //tradeLog.inpan ;
            tradeLog.setTradeRiskLevel(riskLevel);  ;
            Map<String, Object> cardMap = getCardInfo(cardNo);
            tradeLog.setCardIssuerCode(cardMap.get("BANKCODE")+""); ;
            tradeLog.setCardIssuerName(cardMap.get("BANKNAME")+""); ;
            tradeLog.setAcqInistCode(txnsLog.getAccordinst());
            tradeLog.setAcqdatetime(txnsLog.getAccordcommitime()); ;
            tradeLog.setBusicode(busiCode);
            tradeLog.setBusitype(txnsLog.getBusitype());
            tradeLog.setStrategy(riskMap.get("STRATEGY"));
            riskTradeLogService.saveTradeLog(tradeLog);
        }
        if(RiskLevelEnum.REFUSE==riskLevelEnum){//交易拒绝
            throw new TradeException("T035");
        }else{
            
        }
        log.info(" trade risk control end");
    }
    
    
    public List<Map<String,String>> getRiskStrategy(int orders){
        String sql = "select * from T_RISK_LIST where ORDERS = ?";
        return (List<Map<String, String>>) super.queryBySQL(sql, new Object[]{orders});
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void updateAccBusiCode(String txnseqno, String busicode) {
        // TODO Auto-generated method stub
        String hql = "update TxnsLogModel set accbusicode = ? where txnseqno = ? ";
        super.updateByHQL(hql, new Object[]{busicode,txnseqno});
    }


    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void initretMsg(String txnseqno) throws TradeException {
        // TODO Auto-generated method stub
        try {
            String hql = "update TxnsLogModel set payretcode = '',payretinfo='',retcode='',retinfo='' where txnseqno = ? ";
            super.updateByHQL(hql, new Object[]{txnseqno});
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new TradeException("AC06");
        }
    }


    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void updateTxnsLog(TxnsLogModel txnsLog) {
        // TODO Auto-generated method stub
        super.update(txnsLog);
    }


    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void saveTransferLogs(List<PojoBankTransferData> transferDataList) {
        List<TxnsLogModel> logList = new ArrayList<TxnsLogModel>();
        for(PojoBankTransferData data : transferDataList){
        	/* TxnsLogModel txnsLog = getTxnsLogByTxnseqno(data.getTxnseqno());
            if(txnsLog!=null){
                continue;
            }
            txnsLog = new TxnsLogModel();
            MemberBaseModel member = memberService.get(data.getMemberid());
            txnsLog.setTxnseqno(data.getTxnseqno());
            txnsLog.setTxndate(DateUtil.getCurrentDate());
            txnsLog.setTxntime(DateUtil.getCurrentTime());
            txnsLog.setBusicode(data.getBusicode());
            txnsLog.setBusitype(data.getBusitype());
            txnsLog.setAmount(data.getTransamt());
            txnsLog.setRiskver(member.getRiskver());
            txnsLog.setSplitver(member.getSpiltver());
            txnsLog.setFeever(member.getFeever());
            txnsLog.setPrdtver(member.getPrdtver());
            txnsLog.setCheckstandver(member.getCashver());
            txnsLog.setRoutver(member.getRoutver());
            txnsLog.setAccordno(data.getRelatedorderno());
            txnsLog.setAccordinst(member.getMerchinsti());
            txnsLog.setAccfirmerno(data.getMemberid());
            txnsLog.setAccordcommitime(DateUtil.getCurrentDateTime());
            txnsLog.setTradestatflag("00000000");//交易初始状态
            txnsLog.setAccsettledate(DateUtil.getSettleDate(Integer.valueOf(member.getSetlcycle().toString())));
            txnsLog.setPaytype("04"); //支付类型（01：快捷，02：网银，03：账户）
            txnsLog.setPayordno(data.getTranid());//支付定单号
            txnsLog.setPayinst(ChannelEnmu.CMBCINSTEADPAY.getChnlcode());//支付所属机构
            txnsLog.setPayfirmerno(ConsUtil.getInstance().cons.getCmbc_insteadpay_merid());//支付一级商户号
            txnsLog.setPayordcomtime(DateUtil.getCurrentDateTime());//支付定单提交时间
            //卡信息
            txnsLog.setPan(data.getAccno());
            Map<String, Object> cardMap = getCardInfo(data.getAccno());
            txnsLog.setCardtype(cardMap.get("TYPE").toString());
            txnsLog.setCardinstino(cardMap.get("BANKCODE").toString());
            txnsLog.setTxnfee(data.getTxnfee().longValue());*/
            //super.save(txnsLog);
        }
        
    }


    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void saveTxnsLog(TxnsLogModel txnsLogModel) throws TradeException {
        try {
            super.save(txnsLogModel);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new TradeException("T016");
        }
        
    }

}
