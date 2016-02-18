package com.zlebank.zplatform.trade.cmbc.service.impl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.enums.ChannelEnmu;
import com.zlebank.zplatform.trade.bean.enums.ChnlTypeEnum;
import com.zlebank.zplatform.trade.cmbc.bean.gateway.WithholdingMessageBean;
import com.zlebank.zplatform.trade.cmbc.exception.CMBCTradeException;
import com.zlebank.zplatform.trade.cmbc.service.ICMBCQuickPayService;
import com.zlebank.zplatform.trade.cmbc.service.ICMBCTransferService;
import com.zlebank.zplatform.trade.cmbc.service.IWithholdingService;
import com.zlebank.zplatform.trade.dao.RspmsgDAO;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.model.PojoRspmsg;
import com.zlebank.zplatform.trade.model.TxnsWithholdingModel;
import com.zlebank.zplatform.trade.service.ITxnsWithholdingService;

/**
 * 
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年12月16日 下午1:58:17
 * @since
 */
@Service("cmbcQuickPayService")
public class CMBCQuickPayServiceImpl implements ICMBCQuickPayService{

    private static final Log log = LogFactory.getLog(CMBCQuickPayServiceImpl.class);
    @Autowired
    private ICMBCTransferService cmbcTransferService;
    @Autowired
    private IWithholdingService withholdingService;
    @Autowired
    private ITxnsWithholdingService txnsWithholdingService;
    @Autowired
    private RspmsgDAO rspmsgDAO;
    /**
     * 跨行代扣
     * @param withholdingMsg
     * @return
     */
    
    public ResultBean crossLineWithhold(TradeBean trade){
        ResultBean resultBean = null;
        try {
            TxnsWithholdingModel withholding = new TxnsWithholdingModel(trade,ChannelEnmu.CMBCWITHHOLDING);
            withholding.setSerialno(generateSerialDateNumber("SEQ_CMBC_REALNAME_QUERY_NO"));
            txnsWithholdingService.saveWithholdingLog(withholding);
            WithholdingMessageBean withholdingMsg = new WithholdingMessageBean(withholding);
            withholdingMsg.setWithholding(withholding);
            withholdingService.realTimeWitholding(withholdingMsg);
            
            resultBean = queryResult(withholding.getSerialno());
            /*if(resultBean.getResultObj().toString().equals("R")){//启动查询线程
                //queryTradeResult(withholding.getTransdate(),withholding.getSerialno(),trade.getTxnseqno());
            }*/
            return resultBean;
        } catch (TradeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CMBCTradeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ResultBean("success");
    }
    
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    private String generateSerialDateNumber(String sequences){
        List<Map<String,Object>> resultList = (List<Map<String, Object>>) txnsWithholdingService.queryBySQL("select "+sequences+".NEXTVAL seq from dual", new Object[]{});
        DecimalFormat df = new DecimalFormat("00000000");
        String seqNo = df.format( resultList.get(0).get("SEQ"));
        return DateUtil.getCurrentDate()+seqNo;
    }
    
    public ResultBean queryResult(String serialno) {
        TxnsWithholdingModel withholding = null;
        ResultBean resultBean = null;
        int[] timeArray = new int[]{1000, 2000, 8000, 16000, 32000};
        try {
            for (int i = 0; i < 5; i++) {
                withholding = txnsWithholdingService.getWithholdingBySerialNo(serialno);
                if(!StringUtil.isEmpty(withholding.getExectype())){
                    if("S".equalsIgnoreCase(withholding.getExectype())){
                        resultBean = new ResultBean(withholding);
                        break;
                    }else if("E".equalsIgnoreCase(withholding.getExectype())){
                    	PojoRspmsg msg = rspmsgDAO.getRspmsgByChnlCode(ChnlTypeEnum.CMBCWITHHOLDING, withholding.getExeccode());
                        resultBean = new ResultBean(msg.getWebrspcode(),msg.getRspinfo());
                        break;
                    }else if("R".equalsIgnoreCase(withholding.getExectype())){
                        resultBean = new ResultBean("R","正在支付中");
                        break;
                    }
                }
                Thread.currentThread().sleep(timeArray[i]);
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return resultBean;
    }
    
    public void queryTradeResult(String oritransdate,String orireqserialno,String txnseqno){
        try {
            TxnsWithholdingModel withholding = new TxnsWithholdingModel(oritransdate, orireqserialno,txnseqno,ChannelEnmu.CMBCWITHHOLDING);
            txnsWithholdingService.saveWithholdingLog(withholding);
            withholdingService.realTimeWitholdinghQuery(withholding);
        } catch (CMBCTradeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TradeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * 民生银行本行代扣（确认支付）
     * @param trade
     * @return
     */
    public ResultBean innerLineWithhold(TradeBean trade){
        ResultBean resultBean = null;
        try {
            TxnsWithholdingModel withholding = new TxnsWithholdingModel(trade,ChannelEnmu.CMBCSELFWITHHOLDING);
            withholding.setSerialno(generateSerialDateNumber("SEQ_CMBC_REALNAME_QUERY_NO"));
            txnsWithholdingService.saveWithholdingLog(withholding);
            WithholdingMessageBean withholdingMsg = new WithholdingMessageBean(withholding);
            withholdingMsg.setWithholding(withholding);
            withholdingService.realTimeWitholdingSelf(withholdingMsg);
            
            resultBean = queryResult(withholding.getSerialno());
            /*if(resultBean.getResultObj().toString().equals("R")){//启动查询线程
                //queryTradeResult(withholding.getTransdate(),withholding.getSerialno(),trade.getTxnseqno());
            }*/
            return resultBean;
        } catch (TradeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CMBCTradeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ResultBean("success");
        
    }
}
