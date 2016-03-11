/* 
 * InsteadPayServiceImpl.java  
 * 
 * version TODO
 *
 * 2015年11月3日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.cmbc.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.trade.cmbc.bean.InsteadPayBean;
import com.zlebank.zplatform.trade.cmbc.bean.RealTimePayBean;
import com.zlebank.zplatform.trade.cmbc.bean.RealTimeQueryBean;
import com.zlebank.zplatform.trade.cmbc.bean.ReexchangeBean;
import com.zlebank.zplatform.trade.cmbc.bean.enmus.FileTypeEnmu;
import com.zlebank.zplatform.trade.cmbc.bean.gateway.InsteadPayMessageBean;
import com.zlebank.zplatform.trade.cmbc.exception.CMBCTradeException;
import com.zlebank.zplatform.trade.cmbc.net.BaseSocketLongClient;
import com.zlebank.zplatform.trade.cmbc.processor.CMBCInsteadPayReciveProcessor;
import com.zlebank.zplatform.trade.cmbc.service.IFTPCMBCService;
import com.zlebank.zplatform.trade.cmbc.service.IInsteadPayService;
import com.zlebank.zplatform.trade.dao.TransferBatchDAO;
import com.zlebank.zplatform.trade.dao.TransferDataDAO;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.utils.ConsUtil;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年11月3日 上午11:53:14
 * @since
 */
@Service("insteadPayService")
public class InsteadPayServiceImpl implements IInsteadPayService {
    private static final Log log = LogFactory.getLog(InsteadPayServiceImpl.class);
    @Autowired
    private IFTPCMBCService ftpcmbcService;
    @Autowired
    private TransferDataDAO transferDataDAO;
    @Autowired
    private TransferBatchDAO transferBatchDAO;
    @Autowired
    private ITxnsLogService txnsLogService;
    private static final String Key = ConsUtil.getInstance().cons.getCmbc_insteadpay_batch_md5(); //"1234567887654321";
    private static String SecretFilePath = ConsUtil.getInstance().cons.getCmbc_secretfilepath();
    private static final String ENCODE = "GBK";
    
    
    /**
     * 批量代付(跨行)
     * 
     * @param batchNo
     *            批次号
     * @throws IOException 
     * @throws TradeException 
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public void batchOuterPay(String batchNo) throws CMBCTradeException, IOException, TradeException {
        //List<InsteadPayBean> payList = initBatchData();
        /*PojoTransferBatch transferBatch = transferBatchDAO.getByBatchNo(batchNo);
        List<PojoTransferData> transferDataList =  transferDataDAO.findTransDataByBatchNo(batchNo);
        Long sumAmt = 0L;
        Long sumItem = 0L;
        StringBuffer bodyMsg = new StringBuffer();
        for (PojoTransferData bean : transferDataList) {
            //第三方流水号|帐号|户名|支付行号|开户行名称|金额|摘要|备注
            bodyMsg.append(bean.getTranid());
            bodyMsg.append("|");
            bodyMsg.append(bean.getAccno());
            bodyMsg.append("|");
            bodyMsg.append(bean.getAccname());
            bodyMsg.append("|");
            bodyMsg.append(StringUtil.isEmpty(bean.getBanktype())?"":bean.getBanktype());
            bodyMsg.append("|");
            bodyMsg.append(StringUtil.isEmpty(bean.getBankname())?"":bean.getBankname());
            bodyMsg.append("|");
            bodyMsg.append(bean.getTransamt());
            bodyMsg.append("|");
            bodyMsg.append(StringUtil.isEmpty(bean.getRemark())?"":bean.getRemark());
            bodyMsg.append("|");
            bodyMsg.append(StringUtil.isEmpty(bean.getResv())?"":bean.getResv());
            bodyMsg.append("\r\n");
            sumAmt += bean.getTransamt();
            sumItem++;
            TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(bean.getTxnseqno());
            //风控
            txnsLogService.tradeRiskControl(txnsLog.getTxnseqno(),txnsLog.getAccfirmerno(),txnsLog.getAccsecmerno(),txnsLog.getAccmemberid(),txnsLog.getBusicode(),txnsLog.getAmount()+"",bean.getAcctype(),bean.getAccno());
        }
        // PO|总笔数|总金额
        StringBuffer headMsg = new StringBuffer();
        headMsg.append("PO");
        headMsg.append("|");
        headMsg.append(sumItem);
        headMsg.append("|");
        headMsg.append(sumAmt);
        headMsg.append("\r\n");
        String msg = headMsg.append(bodyMsg).append("########").toString();
        log.info("cmbc outer send msg:\r\n"+msg);
        String offerBatchNo = OrderNumber.getInstance().generateCMBCBatchNo();
        String fileName = "req_outer_"
                + DateUtil.formatDateTime("yyyyMMdd", new Date()) + "_"
                + offerBatchNo + ".txt";
        String resFileName = "res_outer_"
                + DateUtil.formatDateTime("yyyyMMdd", new Date()) + "_"
                + offerBatchNo + ".txt";

        File batchPayFile = null;
       
        batchPayFile = writeFile(msg);
        
        // 加密后的文件
        File secretFile = new File(SecretFilePath);
        // 文件加密
        try {
            CMBCAESUtils.encodeAESFile(Key.getBytes(), batchPayFile, secretFile);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new CMBCTradeException("M004");
        }
        //ftpcmbcService = new FTPCMBCService();
        ftpcmbcService.uploadOuterCMBCFile(secretFile, fileName);
        // 修改批次状态为正在支付，修改每笔划拨的状态为正在支付
        transferDataDAO.updateTransDataStatusByBatchNo(batchNo, InsteadPayTypeEnum.Paying);
        //更新批次数据状态,上传文件
        transferBatch.setStatus("02");
        transferBatch.setRequestfilename(fileName);
        transferBatch.setResponsefilename(resFileName);
        transferBatch.setTransfertime(DateUtil.getCurrentDateTime());
        transferBatchDAO.updateBatchToTransfer(transferBatch);*/
    }

    private File writeFile(String msg) throws CMBCTradeException,
            IOException {
        File payFile = new File(SecretFilePath);
        if (payFile.exists()) {
            FileOutputStream out = new FileOutputStream(payFile, false);
            out.write(msg.getBytes(ENCODE));// 注意需要转换对应的字符集
            out.close();
            return payFile;
        } else {
            throw new CMBCTradeException("M003");
        }
    }

    /**
     * 单笔代付
     * 
     * @param insteadPayBean
     *            单笔代付类
     */
    @Override
    public void singlePay(InsteadPayBean insteadPayBean) {
        
    }
    
    public void realTimePay(InsteadPayMessageBean bean){
        RealTimePayBean realTimePayBean = new RealTimePayBean(bean);
        String sendMsg = realTimePayBean.toXML();
        BaseSocketLongClient client =  BaseSocketLongClient.getInstance(ConsUtil.getInstance().cons.getCmbc_withholding_ip(), ConsUtil.getInstance().cons.getCmbc_insteadpay_port(), 90000);     
         try {
            client.setReceiveProcessor(new CMBCInsteadPayReciveProcessor());
            client.sendMessage(sendMsg.getBytes(ENCODE));
        } catch (CMBCTradeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void realTimePayQuery(String oriTranDate, String oriTranId ){
        RealTimeQueryBean realTimePayBean = new RealTimeQueryBean(oriTranDate, oriTranId);
        String sendMsg = realTimePayBean.toXML();
        BaseSocketLongClient client =  BaseSocketLongClient.getInstance(ConsUtil.getInstance().cons.getCmbc_withholding_ip(), ConsUtil.getInstance().cons.getCmbc_insteadpay_port(), 90000);     
         try {
            client.setReceiveProcessor(new CMBCInsteadPayReciveProcessor());
            client.sendMessage(sendMsg.getBytes(ENCODE));
        } catch (CMBCTradeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    

    private List<InsteadPayBean> initBatchData(){
        List<InsteadPayBean> payList =  new ArrayList<InsteadPayBean>();
        for(int i=0;i<5;i++){
            InsteadPayBean bean = new InsteadPayBean(DateUtil.getCurrentDateTime()+i);
            payList.add(bean);
        }
        JSON.toJSONString(payList);
        return payList;
    }
    
    /**
     *
     * @param batchNo
     * @throws TradeException 
     */
    @Override
    @Transactional
    public void batchInnerPay(String batchNo) throws CMBCTradeException,IOException, TradeException {
        // TODO Auto-generated method stub
        /*PojoTransferBatch transferBatch = transferBatchDAO.getByBatchNo(batchNo);
        List<PojoTransferData> transferDataList =  transferDataDAO.findTransDataByBatchNo(batchNo);
        //List<InsteadPayBean> payList = initBatchData();
        Long sumAmt = 0L;
        Long sumItem = 0L;
        StringBuffer bodyMsg = new StringBuffer();
        for (PojoTransferData bean : transferDataList) {
            // 第三方流水号|帐号|户名|金额|摘要|备注
            bodyMsg.append(bean.getTranid());
            bodyMsg.append("|");
            bodyMsg.append(bean.getAccno());
            bodyMsg.append("|");
            bodyMsg.append(bean.getAccname());
            bodyMsg.append("|");
            bodyMsg.append(bean.getTransamt());
            bodyMsg.append("|");
            bodyMsg.append(StringUtil.isEmpty(bean.getRemark())?"":bean.getRemark());
            bodyMsg.append("|");
            bodyMsg.append(StringUtil.isEmpty(bean.getResv())?"":bean.getResv());
            bodyMsg.append("\r\n");
            sumAmt += bean.getTransamt();
            sumItem++;
            TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(bean.getTxnseqno());
            //风控
            txnsLogService.tradeRiskControl(txnsLog.getTxnseqno(),txnsLog.getAccfirmerno(),txnsLog.getAccsecmerno(),txnsLog.getAccmemberid(),txnsLog.getBusicode(),txnsLog.getAmount()+"",bean.getAcctype(),bean.getAccno());
        }
        // PO|总笔数|总金额
        StringBuffer headMsg = new StringBuffer();
        headMsg.append("P");
        headMsg.append("|");
        headMsg.append(sumItem);
        headMsg.append("|");
        headMsg.append(sumAmt);
        headMsg.append("\r\n");
        String msg = headMsg.append(bodyMsg).append("########").toString();
        log.info("cmbc Inner send msg:\r\n"+msg);
        String offerBatchNo = OrderNumber.getInstance().generateCMBCBatchNo();
        String fileName = "req_"
                + DateUtil.formatDateTime("yyyyMMdd", new Date()) + "_"
                + offerBatchNo + ".txt";
        String resFileName = "res_"
                + DateUtil.formatDateTime("yyyyMMdd", new Date()) + "_"
                + offerBatchNo + ".txt";
        File batchPayFile = null;
        batchPayFile = writeFile(msg);
        // 加密后的文件
        File secretFile = new File(SecretFilePath);
        // 文件加密
        try {
            CMBCAESUtils
                    .encodeAESFile(Key.getBytes(), batchPayFile, secretFile);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new CMBCTradeException("M004");
        }
        ftpcmbcService.uploadInnerCMBCFile(secretFile, fileName);
        //修改批次状态为正在支付，修改每笔划拨的状态为正在支付
        transferDataDAO.updateTransDataStatusByBatchNo(batchNo, InsteadPayTypeEnum.Paying);
        //更新批次数据状态,上传文件
        transferBatch.setStatus("02");
        transferBatch.setRequestfilename(fileName);
        transferBatch.setResponsefilename(resFileName);
        transferBatch.setTransfertime(DateUtil.getCurrentDateTime());
        transferBatchDAO.updateBatchToTransfer(transferBatch);*/
    }

    public void analyzeCMBCFile(File file,String fileName,FileTypeEnmu fileTypeEnmu) throws IOException {
        switch (fileTypeEnmu) {
            case RES ://同行回盘                                             第三方流水号|银行流水号|帐号|户名|金额|付款结果|失败返回码|失败原因|付款日期|付款时间
                analyzeResFile(file,fileName);
                break;
            case RESOUTER://跨行回盘                                第三方流水号|银行流水号|帐号|户名|金额|付款结果|失败返回码|失败原因|付款日期|付款时间
                analyzeResFile(file,fileName);
                break;
            case REEXCHANGE://退汇回盘                         报盘日期|报盘批次|第三方流水号|银行流水号|帐号|户名|金额|付款结果|失败返回码|失败原因|退汇日期
                analyzeReexchangeFile(file);
                break;
		default:
			break;
        }
    }
    
    private void analyzeResFile(File file,String fileName) throws NumberFormatException, IOException{
        /*if(file.isFile() && file.exists()) { 
            //判断文件是否存在
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), ENCODE);// 考虑到编码格式
            @SuppressWarnings("resource")
			BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            int i=0;
            long sumItems = 0;//总笔数
            long succItems = 0;//成功笔数
            long sumAmt = 0;//总金额
            long succAmt = 0;//成功金额
            List<PojoTransferData> resultList = new ArrayList<PojoTransferData>();
            while ((lineTxt = bufferedReader.readLine()) != null) {
                if(i==0){
                    //0    1        2       3           4
                    //P|总笔数|总金额|成功笔数|成功金额
                    if(StringUtil.isEmpty(lineTxt)){
                        return;
                    }
                    String[] head = lineTxt.split("\\|");
                    sumItems   =  Long.valueOf(head[1]);
                    sumAmt     =  Long.valueOf(head[2]);
                    succItems  =  Long.valueOf(head[3]);
                    succAmt    =  Long.valueOf(head[4]);
                    i++;
                    continue;
                }
                if("########".equals(lineTxt.trim())){
                    break;
                }
                //0                 1              2     3      4      5         6              7            8           9
                //第三方流水号|银行流水号|帐号|户名|金额|付款结果|失败返回码|失败原因|付款日期|付款时间
                String[] body = lineTxt.split("\\|");
                PojoTransferData resultBean = new PojoTransferData(body);
                resultList.add(resultBean);
                i++;
            }
            log.info("cmbc Inner Back to disk file context:/n/r"+JSON.toJSONString(resultList));
            read.close();
            long sumItems_self = 0;//总笔数
            long succItems_self = 0;//成功笔数
            long sumAmt_self = 0;//总金额
            long succAmt_self = 0;//成功金额
            long failItems = 0;
            long failAmt = 0;
            for(PojoTransferData bean : resultList){
                if("S".equalsIgnoreCase(bean.getResptype())){//成功
                    succItems_self++;
                    succAmt_self+=bean.getTransamt();
                }else if("F".equalsIgnoreCase(bean.getResptype())){
                    failItems++;
                    failAmt+=bean.getTransamt();
                }
                sumItems_self++;
                sumAmt_self += bean.getTransamt();
            }
            if(sumItems_self!=sumItems){//总笔数和实际笔数不一致
                log.info("总笔数和实际笔数不一致:sumItems_self:"+sumItems_self+" / sumItems:"+sumItems);
            }
            if(succItems_self!=succItems){//成功笔数和实际成功笔数不一致
                log.info("成功笔数和实际成功笔数不一致:succItems_self:"+succItems_self+" / succItems:"+succItems);
            }
            if(sumAmt_self!=sumAmt){//总金额和实际总金额不一致
                log.info("总金额和实际总金额不一致:sumAmt_self:"+sumAmt_self+" / sumAmt:"+sumAmt);
            }
            if(succAmt_self!=succAmt){//成功总金额和实际成功总金额不一致
                log.info("总金额和实际总金额不一致:succAmt_self:"+succAmt_self+" / succAmt:"+succAmt);
            }
            //数据没有问题,批量更新划拨明细数据
            //更新批次数据
            transferDataDAO.batchUpdateTransData(resultList);
            PojoTransferBatch transferBatch = transferBatchDAO.getByResponseFileName(fileName);
            if(transferBatch!=null){
                transferBatch.setSuccitem(succItems);
                transferBatch.setSuccamount(succAmt);
                transferBatch.setFailitem(failItems);
                transferBatch.setFailamount(failAmt);
                transferBatch.setStatus("00");
                transferBatch.setAccstatus("01");
                transferBatchDAO.updateTransferBatch(transferBatch);
            }
        }else{
            
        }*/
    }
    
    private void analyzeReexchangeFile(File file) throws NumberFormatException, IOException{
        if (file.isFile() && file.exists()) { // 判断文件是否存在
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), ENCODE);// 考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            int i=0;
            long sumItems = 0;//总笔数
            long succItems = 0;//成功笔数
            long sumAmt = 0;//总金额
            long succAmt = 0;//成功金额
            List<ReexchangeBean> resultList = new ArrayList<ReexchangeBean>();
            while ((lineTxt = bufferedReader.readLine()) != null) {
                if(i==0){
                    //0    1        2       3           4
                    //PO|总笔数|总金额|成功笔数|成功金额
                    String[] head = lineTxt.split("\\|");
                    sumItems   =  Long.valueOf(head[1]);
                    sumAmt     =  Long.valueOf(head[2]);
                    succItems  =  Long.valueOf(head[3]);
                    succAmt    =  Long.valueOf(head[4]);
                }
                if("########".equals(lineTxt.trim())){
                    break;
                }
                //0     1        2         3              4      5       6         7            8           9     10
                //报盘日期|报盘批次|第三方流水号|银行流水号|帐号|户名|金额|付款结果|失败返回码|失败原因|退汇日期
                String[] body = lineTxt.split("\\|");
                ReexchangeBean resultBean = new ReexchangeBean(body);
                resultList.add(resultBean);
                i++;
            }
            read.close();
            long sumItems_self = 0;//总笔数
            long succItems_self = 0;//成功笔数
            long sumAmt_self = 0;//总金额
            long succAmt_self = 0;//成功金额
            @SuppressWarnings("unused")
			long failItems = 0;
            @SuppressWarnings("unused")
			long failAmt = 0;
            for(ReexchangeBean bean : resultList){
                if("S".equals(bean.getRespType())){//成功
                    succItems_self++;
                    succAmt_self+=bean.getTransAmt();
                }else if("F".equals(bean.getRespType())){
                    failItems++;
                    failAmt+=bean.getTransAmt();
                }
                sumItems_self++;
                sumAmt_self += bean.getTransAmt();
            }
            if(sumItems_self!=sumItems){//总笔数和实际笔数不一致
                
            }
            if(succItems_self!=succItems){//成功笔数和实际成功笔数不一致
                
            }
            if(sumAmt_self!=sumAmt){//总金额和实际总金额不一致
                
            }
            if(succAmt_self!=succAmt){//成功总金额和实际成功总金额不一致
                
            }
            //数据没有问题
            
            
        } else {
            
        }
    }

    public static void main(String[] args) {
        InsteadPayServiceImpl impl =new InsteadPayServiceImpl();
        impl.initBatchData();
        
        //System.out.println("P|5|500|5|500".split("\\|")[1]);
        //impl.batchOuterPay("008");
    }
}
