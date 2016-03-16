/* 
 * InsteadPayServiceImpl.java  
 * 
 * version v1.0
 *
 * 2015年11月25日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.acc.bean.BusiAcctQuery;
import com.zlebank.zplatform.acc.bean.QueryAccount;
import com.zlebank.zplatform.acc.bean.TradeInfo;
import com.zlebank.zplatform.acc.bean.enums.Usage;
import com.zlebank.zplatform.acc.exception.AbstractBusiAcctException;
import com.zlebank.zplatform.acc.exception.AccBussinessException;
import com.zlebank.zplatform.acc.pojo.Money;
import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.acc.service.BusiAcctService;
import com.zlebank.zplatform.acc.service.FreezeAmountService;
import com.zlebank.zplatform.commons.bean.CardBin;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.commons.dao.CardBinDao;
import com.zlebank.zplatform.commons.enums.BusinessCodeEnum;
import com.zlebank.zplatform.commons.service.impl.AbstractBasePageService;
import com.zlebank.zplatform.commons.utils.BeanCopyUtil;
import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.member.pojo.PojoMerchDeta;
import com.zlebank.zplatform.member.service.MemberService;
import com.zlebank.zplatform.member.service.MerchService;
import com.zlebank.zplatform.trade.bean.InsteadPayDetailBean;
import com.zlebank.zplatform.trade.bean.InsteadPayDetailQuery;
import com.zlebank.zplatform.trade.bean.InsteadPayInterfaceParamBean;
import com.zlebank.zplatform.trade.bean.enums.BusinessEnum;
import com.zlebank.zplatform.trade.bean.enums.InsteadPayImportTypeEnum;
import com.zlebank.zplatform.trade.bean.enums.SeqNoEnum;
import com.zlebank.zplatform.trade.dao.ConfigInfoDAO;
import com.zlebank.zplatform.trade.dao.InsteadPayBatchDAO;
import com.zlebank.zplatform.trade.dao.InsteadPayDetailDAO;
import com.zlebank.zplatform.trade.dao.TransferDataDAO;
import com.zlebank.zplatform.trade.exception.BalanceNotEnoughException;
import com.zlebank.zplatform.trade.exception.DuplicateOrderIdException;
import com.zlebank.zplatform.trade.exception.FailToGetAccountInfoException;
import com.zlebank.zplatform.trade.exception.FailToInsertAccEntryException;
import com.zlebank.zplatform.trade.exception.FailToInsertFeeException;
import com.zlebank.zplatform.trade.exception.InvalidCardException;
import com.zlebank.zplatform.trade.exception.MerchWhiteListCheckFailException;
import com.zlebank.zplatform.trade.exception.NotInsteadPayWorkTimeException;
import com.zlebank.zplatform.trade.insteadPay.message.InsteadPayFile;
import com.zlebank.zplatform.trade.insteadPay.message.InsteadPayQueryFile;
import com.zlebank.zplatform.trade.insteadPay.message.InsteadPayQuery_Request;
import com.zlebank.zplatform.trade.insteadPay.message.InsteadPayQuery_Response;
import com.zlebank.zplatform.trade.insteadPay.message.InsteadPay_Request;
import com.zlebank.zplatform.trade.model.ConfigInfoModel;
import com.zlebank.zplatform.trade.model.PojoInsteadPayBatch;
import com.zlebank.zplatform.trade.model.PojoInsteadPayDetail;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.service.IGateWayService;
import com.zlebank.zplatform.trade.service.InsteadPayService;
import com.zlebank.zplatform.trade.service.MerchWhiteListService;
import com.zlebank.zplatform.trade.service.SeqNoService;
import com.zlebank.zplatform.trade.utils.OrderNumber;


/**
 * 代付业务
 *
 * @author Luxiaoshuai
 * @version
 * @date 2015年11月25日 上午10:48:24
 * @since
 */
@Service
public class InsteadPayServiceImpl
        extends
            AbstractBasePageService<InsteadPayDetailQuery, InsteadPayDetailBean>
        implements
            InsteadPayService {

    private static final Log log = LogFactory
            .getLog(InsteadPayServiceImpl.class);

    @Autowired
    private ConfigInfoDAO configInfoDAO;
    @Autowired
    private TransferDataDAO transdata;
    @Autowired
    private MemberService memberService;
    @Autowired
    private InsteadPayBatchDAO insteadPayBatchDAO;
    @Autowired
    private InsteadPayDetailDAO insteadPayDetailDAO;
    @Autowired
    private FreezeAmountService freezeAmountService;
    @Autowired
    private BusiAcctService busiAcctService;

    @Autowired
    private AccEntryService accEntryService;
    
    @Autowired
    private IGateWayService gateWayService;
    
    @Autowired
    private MerchService merch;
    
    @Autowired
    private CardBinDao cardBinDao;
    
    @Autowired
    private MerchWhiteListService merchWhiteListService;
    
    @Autowired
    private SeqNoService seqNoService;

    /**
     * 代付处理
     * 
     * @param request
     * @return 错误代码，如果没有错误则返回NULL
     * @throws NotInsteadPayWorkTimeException
     * @throws FailToGetAccountInfoException
     * @throws BalanceNotEnoughException
     * @throws DuplicateOrderIdException
     * @throws InvalidCardException 
     * @throws FailToInsertAccEntryException 
     * @throws MerchWhiteListCheckFailException 
     * @throws FailToInsertFeeException 
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor=Throwable.class)
    public void insteadPay(InsteadPay_Request request, InsteadPayImportTypeEnum type,InsteadPayInterfaceParamBean param) throws NotInsteadPayWorkTimeException, FailToGetAccountInfoException, BalanceNotEnoughException, DuplicateOrderIdException, InvalidCardException, FailToInsertAccEntryException, MerchWhiteListCheckFailException, FailToInsertFeeException {


        if (log.isDebugEnabled()) {
            log.debug("代付处理开始");
            log.debug(JSONObject.fromObject(request));
        }
        // 是否是批处理时间范围内
        Date currentTime;
        try {
            ConfigInfoModel startTime = configInfoDAO
                    .getConfigByParaName("INSTEAD_PAY_START_TIME");
            ConfigInfoModel endTime = configInfoDAO
                    .getConfigByParaName("INSTEAD_PAY_END_TIME");
            currentTime = DateUtil.convertToDate(DateUtil.getCurrentTime(),
                    "HHmmss");
            Date insteadStartTime = DateUtil.convertToDate(startTime.getPara(),
                    "HHmmss");
            Date insteadEndTime = DateUtil.convertToDate(endTime.getPara(),
                    "HHmmss");
            if (currentTime.before(insteadEndTime)
                    && currentTime.after(insteadStartTime)) {
            } else {
                throw new NotInsteadPayWorkTimeException();
            }
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            throw new NotInsteadPayWorkTimeException();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NotInsteadPayWorkTimeException();
        }
        if (log.isDebugEnabled()) {
            log.debug("批处理时间范围检查【通过】");
        }
        
        // 代付明细
        List<InsteadPayFile> fileContent = request.getFileContent();
        
        // 是否要判断白名单
        StringBuilder whiteListError = new StringBuilder();
        if (isCheckWhiteList(request.getMerId())) {
            for (InsteadPayFile file : fileContent) {
                String error = merchWhiteListService.checkMerchWhiteList(file.getMerId(), file.getAccName(), file.getAccNo());
                if (StringUtil.isNotEmpty(error)) {
                    whiteListError.append(error);
                }
            }
        }
        if (whiteListError.length() != 0) {
            throw new MerchWhiteListCheckFailException(new Object[]{whiteListError.toString()+"不在白名单内"});
        }
        
        // 商户信息
        PojoMerchDeta merchPojo = merch.getMerchBymemberId(request.getMerId());

        // 商户余额是否足够
        BigDecimal payBalance = new BigDecimal(request.getTotalAmt());
        BigDecimal merBalance = null;
        QueryAccount queryAccount = new QueryAccount();
        queryAccount.setMemberId(request.getMerId());
        queryAccount.setUsage(Usage.BASICPAY.getCode());
        PagedResult<BusiAcctQuery> busiAccount = memberService.getBusiAccount(
                queryAccount, 1, 10);
        try {
            if (busiAccount != null) {
                List<BusiAcctQuery> pagedResult = busiAccount.getPagedResult();
                if (pagedResult != null && pagedResult.size() == 1) {
                    BusiAcctQuery result = pagedResult.get(0);
                    merBalance = result.getBalance().getAmount();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FailToGetAccountInfoException();
        }
        if (merBalance == null) {
            throw new FailToGetAccountInfoException();
        }
        if (merBalance.compareTo(payBalance) < 0) {
            throw new BalanceNotEnoughException();
        }
        if (log.isDebugEnabled()) {
            log.debug("商户本金余额是否足够检查【通过】");
        }

        /**  手续费计算 */
        BigDecimal feeAmt= BigDecimal.ZERO; 

        // 插入批次表
        PojoInsteadPayBatch batch = convertToPojoInsteadPayBatch(request, param);
        // 设定代付接入类型
        batch.setType(type.getCode()); 
        if (type == InsteadPayImportTypeEnum.FILE) {
            batch.setFilePath(param.getFtpFileName());
            batch.setOriginalFileName(param.getOriginalFileName());    
        }
        batch = insteadPayBatchDAO.merge(batch);

        // 插入明细表
        List<PojoInsteadPayDetail> pojoInsteadPayDetails = convertToPojoInsteadPayDetail(
                request, batch.getId());
        for (PojoInsteadPayDetail detail : pojoInsteadPayDetails) {
            // 插入交易流水号
            detail.setTxnseqno(OrderNumber.getInstance().generateTxnseqno(BusinessCodeEnum.INSTEADPAY_APPLY.getBusiCode()));
            // 取手续费
            Long txnfee = getTxnFee(detail, merchPojo, request.getMerId());
            feeAmt = feeAmt.add(new BigDecimal(txnfee));
            detail.setTxnfee(txnfee);
            detail = insteadPayDetailDAO.merge(detail);
            // 将资金划拨到中间账户
            TradeInfo tradeInfo = new TradeInfo();
            tradeInfo.setTxnseqno(detail.getTxnseqno());
            tradeInfo.setAmount(new BigDecimal(detail.getAmt()));
            tradeInfo.setBusiCode(BusinessCodeEnum.INSTEADPAY_APPLY.getBusiCode());
            tradeInfo.setCharge(new BigDecimal(detail.getTxnfee()));
            tradeInfo.setCommission(BigDecimal.ZERO);
            tradeInfo.setPayMemberId(detail.getMerId());
            tradeInfo.setPayToMemberId(detail.getMerId());
            tradeInfo.setPayToParentMemberId(detail.getMerId());
            try {
                accEntryService.accEntryProcess(tradeInfo);
            } catch (AccBussinessException e) {
                log.error(e.getMessage(),e);
                if ("E000019".equals(e.getCode()))
                    throw new BalanceNotEnoughException();
                else 
                    throw new  FailToInsertAccEntryException();
            } catch (AbstractBusiAcctException e) {
                log.error(e.getMessage(),e);
                throw new  FailToInsertAccEntryException();
            } catch (NumberFormatException e) {
                log.error(e.getMessage(),e);
                throw new  FailToInsertAccEntryException();
            }
        }
        // 商户余额是否能够支付手续费
        if (merBalance.compareTo(payBalance.add(feeAmt))<0) {
            throw new BalanceNotEnoughException();
        }
        if (log.isDebugEnabled()) {
            log.debug("代付处理结束");
        }
    }
    /**
     * 是否要判断白名单【默认不判断】
     * @param merId 一级商户号
     * @return 是否要判断白名单
     */
    private boolean isCheckWhiteList(String merId) {
        ConfigInfoModel  isCheckWhiteList = configInfoDAO.getConfigByParaName("IS_CHECK_WHITE_LIST_INSTEAD_PAY");
        if (isCheckWhiteList != null && "1".equals( isCheckWhiteList.getPara())){
            return true;
        } else {
            return false;
        }
    }
    /**
     * 得到手续费
     * @param detail 
     * @param merchPojo 
     * @param request 
     * @return
     * @throws InvalidCardException 
     * @throws FailToInsertAccEntryException 
     * @throws FailToInsertFeeException 
     */
    private Long getTxnFee(PojoInsteadPayDetail detail, PojoMerchDeta merchPojo, String memberId) throws InvalidCardException, FailToInsertAccEntryException, FailToInsertFeeException {
        TxnsLogModel txnsLog = new TxnsLogModel();
        if(detail==null){
            throw new  FailToInsertAccEntryException();
        }
        if(merchPojo==null){
            throw new FailToInsertFeeException();
            
        }
        txnsLog.setTxnseqno(detail.getTxnseqno());
        // 交易序列号，扣率版本，业务类型，交易金额，会员号，原交易序列号，卡类型
        txnsLog.setFeever(merchPojo.getFeeVer() == null
                ? ""
                : merchPojo.getFeeVer());
        txnsLog.setBusicode(BusinessEnum.INSTEADPAY.getBusiCode());
        txnsLog.setAccfirmerno(merchPojo.getParent() == null ? memberId  : merchPojo.getParent());
        CardBin card = cardBinDao.getCard(merchPojo.getAccNum());
        if (card != null) {
            txnsLog.setCardtype(card.getType());
        }
        txnsLog.setAmount(detail.getAmt());
        Long fee = gateWayService.getTxnFee(txnsLog );
        return fee;
    }
    /**
     * 转换为代付批次表POJO
     * 
     * @param request
     * @return
     */
    private PojoInsteadPayBatch convertToPojoInsteadPayBatch(InsteadPay_Request request,InsteadPayInterfaceParamBean param) {
        PojoInsteadPayBatch batch = new PojoInsteadPayBatch();
        batch.setInsteadPayBatchSeqNo(seqNoService.getBatchNo(SeqNoEnum.INSTEAD_PAY_BATCH_NO));
        batch.setBatchNo(Long.parseLong(request.getBatchNo()));// 批次号
        batch.setMerId(request.getMerId());// 商户号
        batch.setTxnTime(request.getTxnTime());// 订单发送时间(yyyyMMddhhmmss)
        batch.setTotalQty(Long.parseLong(request.getTotalQty()));// 总笔数
        batch.setTotalAmt(Long.parseLong(request.getTotalAmt()));// 总金额
        batch.setStatus("01");// 状态(00:已处理01:未处理)
       if(param==null){
        batch.setInuser(0L);// 创建人
       }else{
           batch.setInuser(param.getUserId());// 创建人 
       }
        batch.setIntime(new Date());// 创建时间
        batch.setUpuser(0L);// 修改人
        batch.setUptime(new Date());// 修改时间
        batch.setNotes("");// 备注
        batch.setType("00");// 接入类型（01：文件导入00：接口）
        batch.setUnapproveCount(batch.getTotalQty());// 未审核笔数
        batch.setUnapproveAmt(batch.getTotalAmt());// 未审核金额
        batch.setApplyTime(new Date());// 申请时间
        return batch;
    }
    /**
     * 转换为代付批次明细表POJO
     * 
     * @param request
     * @return
     */
    private List<PojoInsteadPayDetail> convertToPojoInsteadPayDetail(InsteadPay_Request request,
            Long batch) {
        List<PojoInsteadPayDetail> result = new ArrayList<PojoInsteadPayDetail>();
        List<InsteadPayFile> fileContent = request.getFileContent();
        for (InsteadPayFile file : fileContent) {
            PojoInsteadPayDetail detail = new PojoInsteadPayDetail();
            detail.setInsteadPayDataSeqNo(seqNoService.getBatchNo(SeqNoEnum.INSTEAD_PAY_DATA_NO));
            detail.setBatchId(batch);
            detail.setMerId(file.getMerId());
            detail.setOrderId(file.getOrderId());
            detail.setCurrencyCode(file.getCurrencyCode());
            detail.setAmt(Long.parseLong(file.getAmt()));
            detail.setBizType(file.getBizType());
            detail.setAccType(file.getAccType());
            detail.setAccNo(file.getAccNo());
            detail.setAccName(file.getAccName());
            detail.setBankCode(file.getBankCode());
            detail.setIssInsProvince(file.getIssInsProvince());
            detail.setIssInsCity(file.getIssInsCity());
            detail.setIssInsName(file.getIssInsName());
            detail.setCertifTp(file.getCertifTp());
            detail.setCertifId(file.getCertifId());
            detail.setPhoneNo(Long.parseLong(file.getPhoneNo()));
            detail.setBillType(file.getBillType());
            detail.setNotes(file.getNotes());
            detail.setRespCode("11");
            detail.setRespMsg("未处理");
            detail.setStatus("01");
            detail.setInuser(0L);
            detail.setIntime(new Date());
            result.add(detail);
        }
        return result;
    }
    /**
     * 代付状态查询处理
     * 
     * @param requestBean
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
    public void insteadPayQuery(InsteadPayQuery_Request requestBean,
            InsteadPayQuery_Response responseBean) {
        if (log.isDebugEnabled()) {
            log.debug("代付状态查询开始");
            log.debug(JSONObject.fromObject(requestBean));
            log.debug(JSONObject.fromObject(responseBean));
        }
        // 查询该批次
        PojoInsteadPayBatch batch = insteadPayBatchDAO.getByBatchNo(
                requestBean.getBatchNo(), requestBean.getTxnTime());
        if (batch == null) {
            responseBean.setRespCode("00");
            responseBean.setRespMsg("该批次尚未处理");
            return;
        }
        // 成功笔数
        int successQty = 0;
        // 成功金额
        Long successAmt = 0L;
        // 失败笔数
        int failQty = 0;
        // 失败金额
        Long failAmt = 0L;
        List<InsteadPayQueryFile> queryFiles = new ArrayList<InsteadPayQueryFile>();
        List<PojoInsteadPayDetail> detailList = batch.getDetails();
        for (PojoInsteadPayDetail detail : detailList) {
            if (detail.getStatus().equals("00")) {
                successQty++;
                successAmt = successAmt+ detail.getAmt();
            } else {
                failQty++;
                failAmt = failAmt+ detail.getAmt();
            }
            InsteadPayQueryFile queryFile = new InsteadPayQueryFile();
            queryFile.setMerId(detail.getMerId());
            queryFile.setOrderId(detail.getOrderId());
            queryFile.setCurrencyCode(detail.getCurrencyCode());
            queryFile.setAmt(String.valueOf(detail.getAmt()));
            queryFile.setBizType(detail.getBizType());
            queryFile.setAccType(detail.getAccType());
            queryFile.setAccNo(detail.getAccNo());
            queryFile.setAccName(detail.getAccName());
            queryFile.setBankCode(detail.getBankCode());
            queryFile.setIssInsProvince(detail.getIssInsProvince());
            queryFile.setIssInsCity(detail.getIssInsCity());
            queryFile.setIssInsName(detail.getIssInsName());
            queryFile.setCertifTp(detail.getCertifTp());
            queryFile.setCertifId(detail.getCertifId());
            queryFile.setPhoneNo(String.valueOf(detail.getPhoneNo()));
            queryFile.setBillType(detail.getBillType());
            queryFile.setNotes(detail.getNotes());
            queryFile.setRespCode(detail.getRespCode());
            queryFile.setRespMsg(detail.getRespMsg());
            //01:待初审，09初审未过，11：待复审，19：复审未过，21：等待批处理，29：批处理失败，00：代付成功,39：自行终止
            if ("01".equals(detail.getStatus())) {
                queryFile.setRespCode(detail.getStatus());
                queryFile.setRespMsg("待初审");
            } else if ("09".equals(detail.getStatus())) {
                queryFile.setRespCode(detail.getStatus());
                queryFile.setRespMsg("初审未过");
            }else if ("11".equals(detail.getStatus())) {
                queryFile.setRespCode(detail.getStatus());
                queryFile.setRespMsg("待复审");
            }else if ("19".equals(detail.getStatus())) {
                queryFile.setRespCode(detail.getStatus());
                queryFile.setRespMsg("复审未过");
            }else if ("21".equals(detail.getStatus())) {
                queryFile.setRespCode(detail.getStatus());
                queryFile.setRespMsg("等待批处理");
            }else if ("29".equals(detail.getStatus())) {
                queryFile.setRespCode(detail.getStatus());
                queryFile.setRespMsg("批处理失败");
            }
            queryFiles.add(queryFile);
        }

        // 应答报文
        responseBean.setFileContent(queryFiles);
        responseBean.setSuccTotalQty(String.valueOf(successQty));
        responseBean.setSuccTotalAmt(String.valueOf(successAmt));
        responseBean.setFailTotalQty(String.valueOf(failQty));
        responseBean.setFailTotalAmt(String.valueOf(failAmt));
        responseBean.setRespCode("00");
        responseBean.setRespMsg("成功！");
        if (log.isDebugEnabled()) {
            log.debug("代付状态查询结束");
        }
    }
    /**
     *
     * @param example
     * @return
     */
    @Override
    protected long getTotal(InsteadPayDetailQuery example) {
        return insteadPayDetailDAO.count(example);
    }
    /**
     *
     * @param offset
     * @param pageSize
     * @param example
     * @return
     */
    @Override
    protected List<InsteadPayDetailBean> getItem(int offset,
            int pageSize,
            InsteadPayDetailQuery example) {
        List<InsteadPayDetailBean> insteadBeanList = new ArrayList<InsteadPayDetailBean>();
        List<PojoInsteadPayDetail> li = insteadPayDetailDAO.getListByQuery(
                offset, pageSize, example);
        for (PojoInsteadPayDetail instead : li) {
            InsteadPayDetailBean insteadBean = BeanCopyUtil.copyBean(
                    InsteadPayDetailBean.class, instead);
            
            insteadBean.setAmt(Money.valueOf(instead.getAmt()).toYuan());
            
            insteadBeanList.add(insteadBean);
        }
        return insteadBeanList;
    }

}
