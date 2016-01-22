/* 
 * GateWayController.java  
 * 
 * version TODO
 *
 * 2015年8月21日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.controller;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.acc.bean.BusiAcct;
import com.zlebank.zplatform.acc.bean.BusiAcctQuery;
import com.zlebank.zplatform.acc.bean.enums.Usage;
import com.zlebank.zplatform.acc.pojo.Money;
import com.zlebank.zplatform.acc.service.AccountQueryService;
import com.zlebank.zplatform.commons.utils.Base64Utils;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.member.dao.PersonDAO;
import com.zlebank.zplatform.member.pojo.PojoPersonDeta;
import com.zlebank.zplatform.member.service.MemberService;
import com.zlebank.zplatform.trade.adapter.accounting.IAccounting;
import com.zlebank.zplatform.trade.adapter.quickpay.IQuickPayTrade;
import com.zlebank.zplatform.trade.analyzer.GateWayTradeAnalyzer;
import com.zlebank.zplatform.trade.analyzer.ReaPayTradeAnalyzer;
import com.zlebank.zplatform.trade.bean.AppPartyBean;
import com.zlebank.zplatform.trade.bean.PayPartyBean;
import com.zlebank.zplatform.trade.bean.ReaPayResultBean;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.RoutBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.ZLPayResultBean;
import com.zlebank.zplatform.trade.bean.enums.ChannelEnmu;
import com.zlebank.zplatform.trade.bean.gateway.OrderAsynRespBean;
import com.zlebank.zplatform.trade.bean.gateway.OrderBean;
import com.zlebank.zplatform.trade.bean.gateway.OrderRespBean;
import com.zlebank.zplatform.trade.bean.gateway.QueryBean;
import com.zlebank.zplatform.trade.bean.gateway.QueryResultBean;
import com.zlebank.zplatform.trade.bean.gateway.RiskRateInfoBean;
import com.zlebank.zplatform.trade.cmbc.bean.gateway.CardMessageBean;
import com.zlebank.zplatform.trade.cmbc.bean.gateway.InsteadPayMessageBean;
import com.zlebank.zplatform.trade.cmbc.bean.gateway.WhiteListMessageBean;
import com.zlebank.zplatform.trade.cmbc.bean.gateway.WithholdingMessageBean;
import com.zlebank.zplatform.trade.cmbc.exception.CMBCTradeException;
import com.zlebank.zplatform.trade.cmbc.service.ICMBCTransferService;
import com.zlebank.zplatform.trade.cmbc.service.IWithholdingService;
import com.zlebank.zplatform.trade.cmbc.service.impl.InsteadPayServiceImpl;
import com.zlebank.zplatform.trade.cmbc.service.impl.WithholdingServiceImpl;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.factory.AccountingAdapterFactory;
import com.zlebank.zplatform.trade.factory.TradeAdapterFactory;
import com.zlebank.zplatform.trade.model.MemberBaseModel;
import com.zlebank.zplatform.trade.model.PojoRealnameAuth;
import com.zlebank.zplatform.trade.model.QuickpayCustModel;
import com.zlebank.zplatform.trade.model.TxncodeDefModel;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsNotifyTaskModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;
import com.zlebank.zplatform.trade.model.TxnsQuickpayModel;
import com.zlebank.zplatform.trade.model.TxnsWithdrawModel;
import com.zlebank.zplatform.trade.model.TxnsWithholdingModel;
import com.zlebank.zplatform.trade.security.reapay.AES;
import com.zlebank.zplatform.trade.security.reapay.RSA;
import com.zlebank.zplatform.trade.service.IAccountPayService;
import com.zlebank.zplatform.trade.service.IGateWayService;
import com.zlebank.zplatform.trade.service.IMemberService;
import com.zlebank.zplatform.trade.service.IProdCaseService;
import com.zlebank.zplatform.trade.service.IQuickpayCustService;
import com.zlebank.zplatform.trade.service.IRouteConfigService;
import com.zlebank.zplatform.trade.service.IRouteProcessService;
import com.zlebank.zplatform.trade.service.ITxncodeDefService;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.ITxnsNotifyTaskService;
import com.zlebank.zplatform.trade.service.ITxnsQuickpayService;
import com.zlebank.zplatform.trade.service.ITxnsWithdrawService;
import com.zlebank.zplatform.trade.service.ITxnsWithholdingService;
import com.zlebank.zplatform.trade.service.IWebGateWayService;
import com.zlebank.zplatform.trade.service.IZlTradeService;
import com.zlebank.zplatform.trade.service.impl.GateWayServiceImpl;
import com.zlebank.zplatform.trade.utils.AmountUtil;
import com.zlebank.zplatform.trade.utils.BankCardUtil;
import com.zlebank.zplatform.trade.utils.ConsUtil;
import com.zlebank.zplatform.trade.utils.DateUtil;
import com.zlebank.zplatform.trade.utils.ObjectDynamic;
import com.zlebank.zplatform.trade.utils.OrderNumber;
import com.zlebank.zplatform.trade.utils.SynHttpRequestThread;

/**
 * 
 * 网关支付控制层
 * 
 * @author guojia
 * @version
 * @date 2015年8月21日 下午4:26:42
 * @since
 */
@Controller
@RequestMapping("gateway")
public class GateWayController {
    private static final Log log = LogFactory.getLog(GateWayController.class);

    @Autowired
    private IGateWayService gateWayService;
    @Autowired
    private IWebGateWayService webGateWayService;
    private IZlTradeService zlTradeService;
    @Autowired
    private ITxnsQuickpayService txnsQuickpayService;
    @Autowired
    private ITxnsLogService txnsLogService;
    @Autowired
    private IMemberService memberService;

    @Autowired
    private IRouteConfigService routeConfigService;
    @Autowired
    private IRouteProcessService routeProcessService;
    @Autowired
    private ITxncodeDefService txncodeDefService;
    @Autowired
    private IProdCaseService prodCaseService;
    @Autowired
    @Qualifier("memberServiceImpl")
    private MemberService memberService2;
    @Autowired
    private AccountQueryService accountQueryService;
    @Autowired
    private IQuickpayCustService quickpayCustService;
    @Autowired
    private IAccountPayService accountPayService;
    @Autowired
    private ITxnsWithdrawService txnsWithdrawService;
    @Autowired
    private ITxnsNotifyTaskService txnsNotifyTaskService;
    @Autowired
    private PersonDAO personDAO;
    @Autowired
    private IWithholdingService withholdingService;
    @Autowired
    private ICMBCTransferService cmbcTransferService;
    @Autowired
    private ITxnsWithholdingService txnsWithholdingService;
    @RequestMapping("/coporder.htm")
    public ModelAndView pay(OrderBean order,HttpSession httpSession,HttpServletRequest request) {
        log.info("receive web message(json):" + JSON.toJSONString(order));
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            if (!validateOrder(order, model)) {
                return new ModelAndView("/erro_gw", model);
            }
            RiskRateInfoBean riskRateInfo = (RiskRateInfoBean) GateWayTradeAnalyzer
                    .generateRiskBean(order.getRiskRateInfo()).getResultObj();
            if(StringUtil.isEmpty(order.getCustomerIp())){
                order.setCustomerIp(getIpAddr(request));
            }
           
            // 订单记录和业务逻辑处理,取得商户信息，记录交易数据（核心）和订单详细信息，分析交易所属业务
            String txnseqno = gateWayService.dealWithOrder(order, riskRateInfo);
            
            // 将会员号保存在session
            httpSession.setAttribute("memberId", riskRateInfo.getMerUserId());
            return new ModelAndView("redirect:/gateway/cash.htm?txnseqno="
                    + txnseqno);
        } catch (Exception e) {
            e.printStackTrace();
            model.put("errMsg", "订单信息错误，请重新提交");
            model.put("errCode", "RC99");
        }
        return new ModelAndView("/erro_gw", model);
    }

    private boolean validateOrder(OrderBean order, Map<String, Object> model) {
        // 验证订单数据有效性，用hibernate validator处理
        ResultBean resultBean = GateWayTradeAnalyzer.validateOrder(order);
        if (!resultBean.isResultBool()) {
            // 订单信息长度和非空验证未通过
            model.put("errMsg", resultBean.getErrMsg());
            model.put("errCode", resultBean.getErrCode());
            return false;
        }
     // 检验风控信息是否符合要求
        ResultBean riskResultBean = GateWayTradeAnalyzer.generateRiskBean(order
                .getRiskRateInfo());
        if (!riskResultBean.isResultBool()) {// 验证风控信息的正确性
            model.put("errMsg", riskResultBean.getErrMsg());
            model.put("errCode", riskResultBean.getErrCode());
            return false;
        }
        ResultBean signResultBean = gateWayService.verifyOrder(order);
        if (!signResultBean.isResultBool()) {
            // 订单信息验签未通过
            model.put("errMsg", signResultBean.getErrMsg());
            model.put("errCode", signResultBean.getErrCode());
            return false;
        }

        // 验证订单号是否重复
        try {
            String memberId = ((RiskRateInfoBean)riskResultBean.getResultObj()).getMerUserId();
            gateWayService.verifyRepeatWebOrder(order.getOrderId(),
                    order.getTxnTime(), order.getTxnAmt(), order.getCoopInstiId(),memberId);
        } catch (TradeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            model.put("errMsg", e.getMessage());
            model.put("errCode", e.getCode());
            return false;
        }

        // 验证商户产品版本中是否有对应的业务
        ResultBean busiResultBean = prodCaseService.verifyBusiness(order);
        if (!busiResultBean.isResultBool()) {
            model.put("errMsg", busiResultBean.getErrMsg());
            model.put("errCode", busiResultBean.getErrCode());
            return false;
        }

        // 检验一级商户和二级商户有效性
        if (StringUtil.isNotEmpty(order.getMerId())) {
            MemberBaseModel subMember = memberService.getMemberByMemberId(order.getMerId());
            if (subMember == null) {
                model.put("errMsg", "商户信息不存");
                model.put("errCode", "RC10");
                return false;
            }
            if (order.getMerId().startsWith("2")) {// 对于商户会员需要进行检查
                ResultBean memberResultBean = memberService.verifySubMerch(
                        order.getCoopInstiId(), order.getMerId());
                if (!memberResultBean.isResultBool()) {
                    model.put("errMsg", memberResultBean.getErrMsg());
                    model.put("errCode", memberResultBean.getErrCode());
                    return false;
                }
            }

        }
        

        // 业务验证
        // 充值业务，如果memberId为空，或者为999999999999999时为非法订单
        ResultBean memberBusiResultBean = gateWayService
                .validateMemberBusiness(order,
                        (RiskRateInfoBean) riskResultBean.getResultObj());
        if (!memberBusiResultBean.isResultBool()) {// 验证风控信息的正确性
            model.put("errMsg", memberBusiResultBean.getErrMsg());
            model.put("errCode", memberBusiResultBean.getErrCode());
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/cash.htm", method = RequestMethod.GET)
    public ModelAndView cashIndex(@RequestParam("txnseqno") String txnseqno,HttpServletRequest request) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(txnseqno)) {
                model.put("errMsg", "订单信息错误，请重新提交");
                return new ModelAndView("/erro_gw", model);
            }
            TxnsLogModel txnsLog = txnsLogService
                    .getTxnsLogByTxnseqno(txnseqno);
            MemberBaseModel member = memberService
                    .getMemberByMemberId(txnsLog.getAccfirmerno());
            MemberBaseModel subMember = null;
            if (StringUtil.isNotEmpty(txnsLog.getAccsecmerno())) {
                subMember = memberService.get(txnsLog.getAccsecmerno());
            }
            TxnsOrderinfoModel orderInfo = gateWayService
                    .getOrderinfoByOrderNoAndMemberId(txnsLog.getAccordno(),
                            txnsLog.getAccfirmerno());
            if ("00".equals(orderInfo.getStatus())) {// 支付成功的订单不可重复支付
                model.put("errMsg", "订单已经支付成功，请不要重复支付");
                model.put("errCode", "RC10");
                return new ModelAndView("/erro_gw", model);
            }
            model.put("cashier", "");
            model.put("merchName", member.getMerchname());
            model.put("merchId", txnsLog.getAccfirmerno());
            model.put("goodsName", orderInfo.getGoodsname());
            model.put("txnseqno", txnseqno);
            model.put("memberId", txnsLog.getAccmemberid());
            model.put("busicode", txnsLog.getBusicode());
            model.put("busitype", txnsLog.getBusitype());
            model.put("orderId", txnsLog.getAccordno());
            model.put("subMerName",
                    subMember != null ? subMember.getMerchname() : "");
            model.put("txnTime",
                    DateUtil.formatDateTime(DateUtil.parse(
                            DateUtil.DEFAULT_DATE_FROMAT,
                            orderInfo.getOrdercommitime())));
            model.put("txnAmt", txnsLog.getAmount());
            model.put("orderDesc", orderInfo.getOrderdesc());
            model.put("tn", orderInfo.getTn());
            model.put("cashver", member.getCashver());
            model.put("amount_y", AmountUtil.numberFormat(txnsLog.getAmount()));

            if (!"999999999999999".equals(txnsLog.getAccmemberid())) {
                // 获取会员已绑定的银行卡
                List<QuickpayCustModel> bindCardList = quickpayCustService
                        .querBankCardByMemberId(txnsLog.getAccmemberid(), "");
                model.put("bindCardList", bindCardList);
                QuickpayCustModel quickpayCust = null;
                // 处理银行卡验证信息及身份信息，取得卡信息
                try {
                    if (StringUtil.isNotEmpty(orderInfo.getCustomerInfo())) {
                        /*quickpayCust = quickpayCustService.orderBindCard(
                                new String(Base64Utils.decode(orderInfo
                                        .getCustomerInfo()), "UTF-8"), txnsLog
                                        .getAccfirmerno(), txnsLog
                                        .getAccmemberid());*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    model.put("errMsg", "银行卡验证信息错误，请重新提交订单！");
                    model.put("errCode", "RC10");
                    return new ModelAndView("/erro_gw", model);
                }
                // PojoMember member2 =
                // memberService2.getMbmberByMemberId(txnsLog.getAccmemberid(),
                // MemberType.Individual);
                // log.info(member2.getMembername());
                String cardType = "";
                if ("1000".equals(txnsLog.getBusitype())) {// 消费交易

                } else if ("2000".equals(txnsLog.getBusitype())) {// 充值交易只能是借记
                    cardType = "1";
                } else if ("2000".equals(txnsLog.getBusitype())) {// 保障金交易 借记卡
                    cardType = "1";
                } else if ("3000".equals(txnsLog.getBusitype())) {// 提现交易 借记卡
                    cardType = "1";
                }
                // 显示会员已绑定的银行卡
                List<QuickpayCustModel> cardList = quickpayCustService
                        .querBankCardByMemberId(
                                txnsLog.getAccmemberid(), cardType);
                if (quickpayCust != null) {
                    for (QuickpayCustModel cust : cardList) {
                        // 判断依据卡号相同，账户名称相同，身份证号相同
                        if (cust.getAccname().equals(quickpayCust.getAccname())
                                && cust.getCardno().equals(
                                        quickpayCust.getCardno())
                                && cust.getIdnum().equals(
                                        quickpayCust.getIdnum())) {
                            // 此时卡已绑定，移除出原有集合，赋值到默认第一支付卡中
                            cardList.remove(cust);
                            quickpayCust = cust;
                            model.put("bindFlag", "1");
                            break;
                        }
                    }
                }

                model.put("cardList", cardList);
                model.put("memberCard", quickpayCust);
                
                
                //获取请求方IP
                model.put("memberIP", orderInfo.getPayerip());
            }

            if ("3000".equals(txnsLog.getBusitype())) {// 提现交易 借记卡
                return new ModelAndView("/withdraw", model);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            model.put("errMsg", "订单信息错误，请重新提交");
            model.put("errCode", "RC10");
            return new ModelAndView("/erro_gw", model);
        }
        return new ModelAndView("/index", model);
    }

    @RequestMapping("/showAccount")
    @ResponseBody
    public Object showMemberAccount(String memberId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<BusiAcct> busiAccList = accountQueryService
                .getBusiACCByMid(memberId);
        BusiAcct fundAcct = null;
        // 取得资金账户
        for (BusiAcct busiAcct : busiAccList) {
            if (Usage.BASICPAY == busiAcct.getUsage()) {
                fundAcct = busiAcct;
            }
        }
        BusiAcctQuery memberAcct = accountQueryService
                .getMemberQueryByID(fundAcct.getBusiAcctCode());
        
        PojoPersonDeta person =  personDAO.getPersonByMemberId(memberId);
        
        if(StringUtil.isEmpty(person.getPayPwd())){
            resultMap.put("paypwd", "none");
        }else{
            resultMap.put("paypwd", "ok");
        }
        resultMap.put("money", memberAcct.getBalance().toYuan());
        // 会员资金账户余额
        
        log.info(JSON.toJSONString(resultMap));
        return resultMap;
    }
    @RequestMapping(value = "/initPassWord")
    @ResponseBody
    public Object initPayPWD(String memberId,String pwd,String pwd_rep){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if(!pwd.equals(pwd_rep)){
            resultMap.put("retinfo","支付密码和确认密码不一致");
            resultMap.put("retcode", "01");
        }else{
            try {
                webGateWayService.initMemeberPwd(memberId, pwd);
                resultMap.put("retinfo","成功");
                resultMap.put("retcode", "00");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                resultMap.put("retinfo","初始化秘钥失败");
                resultMap.put("retcode", "02");
            }
           
        }
       
        return resultMap;
   }
    

    private int validateBalance(String memberId, Long amt) {
        List<BusiAcct> busiAccList = accountQueryService
                .getBusiACCByMid(memberId);
        BusiAcct fundAcct = null;
        // 取得资金账户
        for (BusiAcct busiAcct : busiAccList) {
            if (Usage.BASICPAY == busiAcct.getUsage()) {
                fundAcct = busiAcct;
            }
        }
        BusiAcctQuery memberAcct = accountQueryService
                .getMemberQueryByID(fundAcct.getBusiAcctCode());
        // 会员资金账户余额
        return memberAcct.getBalance().compareTo(
                Money.valueOf(new BigDecimal(amt)));
    }

    @RequestMapping("/accountPay.htm")
    public ModelAndView memberAcctPay(TradeBean tradeBean) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            String url =  webGateWayService.accountPay(tradeBean);
            model.put( "suburl",url);
            model.put("errMsg", "交易成功");
            return new ModelAndView("/fastpay/success", model);
        } catch (TradeException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            model.put("errMsg", e1.getMessage());
            model.put("txnseqno", tradeBean.getTxnseqno());
            return new ModelAndView("/erro", model);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            model.put("errMsg", "异步通知发生异常");
            model.put("txnseqno", tradeBean.getTxnseqno());
            return new ModelAndView("/erro", model);
        }
        
        
   /*     
        
        try {
            if (validateBalance(tradeBean.getMerUserId(),
                    Long.valueOf(tradeBean.getAmount())) < 0) {
                throw new TradeException("T025");
            }

            AccountTradeBean accountTrade = new AccountTradeBean(
                    tradeBean.getMerUserId(), tradeBean.getMerchId(),
                    tradeBean.getPay_pwd(), tradeBean.getAmount(),
                    tradeBean.getTxnseqno());
            accountTrade.setMerchId("888888888888888");
            String pwd = accountPayService.encryptPWD(
                    accountTrade.getMerchId(), tradeBean.getPay_pwd());
            accountTrade.setPay_pwd(pwd);
            TxnsOrderinfoModel orderinfo = gateWayService
                    .getOrderinfoByOrderNoAndMemberId(tradeBean.getOrderId(),
                            tradeBean.getMerchId());
            if("00".equals(orderinfo.getStatus())){
                throw new TradeException("T004");
            }    
            if("02".equals(orderinfo.getStatus())){
                throw new TradeException("T009");
            }
            if(!orderinfo.getOrderamt().toString().equals(tradeBean.getAmount())){
                throw new TradeException("T033");
            }
            accountPayService.accountPay(accountTrade);
            resultBean = new ResultBean("success");
            resultBean.setErrCode("0000");
            resultBean.setErrMsg("交易成功");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            model.put("errMsg", e.getMessage());
            model.put("txnseqno", tradeBean.getTxnseqno());
            resultBean = new ResultBean("9999", e.getMessage());
            return new ModelAndView("/erro", model);
        }
        gateWayService.saveAcctTrade(tradeBean.getTxnseqno(),
                tradeBean.getOrderId(), resultBean);
        if (resultBean.isResultBool()) {
            ResultBean orderResp = gateWayService.generateRespMessage(
                    tradeBean.getOrderId(), tradeBean.getMerchId());
            if (orderResp.isResultBool()) {
                TxnsOrderinfoModel gatewayOrderBean = gateWayService
                        .getOrderinfoByOrderNoAndMemberId(
                                tradeBean.getOrderId(), tradeBean.getMerchId());
                OrderRespBean respBean = (OrderRespBean) orderResp
                        .getResultObj();
                try {
                    model.put(
                            "suburl",
                            gatewayOrderBean.getFronturl()
                                    + "?"
                                    + ObjectDynamic.generateReturnParamer(
                                            respBean, false, null));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // 异步消息通知
                new SynHttpRequestThread(tradeBean.getMerchId(),
                        tradeBean.getTxnseqno(), gatewayOrderBean.getBackurl(),
                        respBean.getNotifyParam()).start();
            }
            model.put("errMsg", "交易成功");
            return new ModelAndView("/fastpay/success", model);
        } else {
            model.put("txnseqno", tradeBean.getTxnseqno());
            return new ModelAndView("/erro", model);
        }*/

    }

    @RequestMapping("/toFastPay.htm")
    public ModelAndView toFastPay(TradeBean trade, @RequestParam("txnseqno_")String txnseqno_) {
        if (StringUtil.isEmpty(trade.getTxnseqno())) {
            return new ModelAndView("redirect:/gateway/cash.htm?txnseqno="
                    + txnseqno_);
        }
        Map<String, Object> model = new HashMap<String, Object>();
        trade.setBindCardId("");
        model.put("trade", trade);
        return new ModelAndView("/fastpay/fastCardInfo", model);
    }

    @RequestMapping("/toPay.htm")
    public ModelAndView toPay(TradeBean trade, @RequestParam("txnseqno_")String txnseqno_) {
        if (StringUtil.isEmpty(trade.getTxnseqno())) {
            return new ModelAndView("redirect:/gateway/cash.htm?txnseqno="
                    + txnseqno_);
        }
        Map<String, Object> model = new HashMap<String, Object>();
        
        try {
            webGateWayService.bankCardSign(trade);
        } catch (TradeException e1) {
            // TODO Auto-generated catch block
            model.put("errMsg", e1.getMessage());
            model.put("errCode", e1.getCode());
            model.put("txnseqno", trade.getTxnseqno());
            return new ModelAndView("/erro", model);
        }
        
        /*ResultBean bean = null;
        ResultBean routResultBean = routeConfigService.getTransRout(
                DateUtil.getCurrentDateTime(), trade.getAmount(),
                trade.getMerchId(), trade.getBusicode(), trade.getCardNo(),
                trade.getCashCode());
        if (routResultBean.isResultBool()) {
            String routId = routResultBean.getResultObj().toString();
            if (ConsUtil.getInstance().cons.getReapay_chnl_code()
                    .equals(routId)) {
                trade.setReaPayOrderNo(OrderNumber.getInstance()
                        .generateReaPayOrderId());
                // RoutBean routBean =
                // routeProcessService.getFirstRoutStep(routId,trade.getBusicode());
                IQuickPayTrade quickPayTrade = null;
                try {
                    quickPayTrade = TradeAdapterFactory
                            .getInstance().getQuickPayTrade(routId);
                } catch (TradeException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // trade.setCurrentSetp(routBean.getTxncode_current());
                bean = quickPayTrade.marginRegister(trade);
                // 更新核心交易表路由信息
                txnsLogService.updateRoutInfo(trade.getTxnseqno(), routId, "",
                        trade.getCashCode());
                if (bean.isResultBool()) {
                    ReaPayResultBean resultBean = (ReaPayResultBean) bean
                            .getResultObj();
                    if (!"0000".equals(resultBean.getResult_code())) {
                        model.put("errMsg", resultBean.getResult_msg());
                        model.put("errCode", resultBean.getResult_code());
                        model.put("txnseqno", trade.getTxnseqno());
                        return new ModelAndView("/erro", model);
                    }
                }
            } else if ("95000001".equals(routId)) {
                trade.setReaPayOrderNo(OrderNumber.getInstance()
                        .generateReaPayOrderId());

                IQuickPayTrade quickPayTrade = null;
                try {
                    quickPayTrade = TradeAdapterFactory
                            .getInstance().getQuickPayTrade(routId);
                } catch (TradeException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                bean = quickPayTrade.marginRegister(trade);
                if (bean.isResultBool()) {
                    ReaPayResultBean resultBean = (ReaPayResultBean) bean
                            .getResultObj();
                    if (!"0000".equals(resultBean.getResult_code())) {
                        model.put("errMsg", resultBean.getResult_msg());
                        model.put("errCode", resultBean.getResult_code());
                        model.put("txnseqno", txnseqno_);
                        return new ModelAndView("/erro", model);
                    }
                }
            }
        } else {
            model.put("errMsg", routResultBean.getErrMsg());
            model.put("errCode", routResultBean.getErrCode());
            model.put("txnseqno", txnseqno_);
            return new ModelAndView("/erro", model);
        }*/
        model.put("trade", trade);
        return new ModelAndView("/fastpay/pay", model);
    }
    @RequestMapping("/bindPay.htm")
    public ModelAndView toBindPay(TradeBean trade, @RequestParam("txnseqno_")String txnseqno_) {
        if (StringUtil.isEmpty(trade.getTxnseqno())) {
            return new ModelAndView("redirect:/gateway/cash.htm?txnseqno="
                    + txnseqno_);
        }
        
        Map<String, Object> model = new HashMap<String, Object>();
        TradeBean tradeBean2 = trade.clone();
        ResultBean bean = null;
        try {
            webGateWayService.bindPay(trade);
        } catch (TradeException e) {
            // TODO Auto-generated catch block
            model.put("errMsg", e.getMessage());
            model.put("txnseqno", txnseqno_);
            return new ModelAndView("/erro", model);
        }
       
        /*try {
            String bindCardId = trade.getBindCardId();
            // 直接获取短信验证码
            QuickpayCustModel custCard = quickpayCustService.get(Long
                    .valueOf(trade.getBindCardId()));
            String routId = custCard.getInstitution();
            trade.setReaPayOrderNo(OrderNumber.getInstance()
                    .generateReaPayOrderId());
            IQuickPayTrade quickPayTrade = TradeAdapterFactory.getInstance()
                    .getQuickPayTrade(routId);
            if ("00".equals(custCard.getStatus())) {
                trade.setBindCardId(custCard.getBindcardid());

            } else {
                trade.setBindCardId("");
            }
            trade.setCardNo(custCard.getCardno());
            trade.setMobile(custCard.getPhone());
            trade.setAcctName(custCard.getAccname());
            trade.setCertId(custCard.getIdnum());
            trade.setValidthru(custCard.getValidtime());// web收银台使用
            trade.setCvv2(custCard.getCvv2());
            bean = quickPayTrade.marginRegister(trade);
            trade.setBindCardId(bindCardId);
            if (bean.isResultBool()) {
                ReaPayResultBean resultBean = (ReaPayResultBean) bean
                        .getResultObj();
                if (!"0000".equals(resultBean.getResult_code())) {
                    model.put("errMsg", resultBean.getResult_msg());
                    model.put("errCode", resultBean.getResult_code());
                    model.put("txnseqno", txnseqno_);

                    return new ModelAndView("/erro", model);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            model.put("errMsg", "订单信息异常，请返回收银台重新支付");
            model.put("errCode", "RC98");
            model.put("trade", trade);
            model.put("txnseqno", txnseqno_);
            return new ModelAndView("/erro", model);
        }*/
        tradeBean2.setCardNo(trade.getCardNo());
        tradeBean2.setReaPayOrderNo(trade.getReaPayOrderNo());
        tradeBean2.setCardNo(trade.getCardNo());
        tradeBean2.setMobile(trade.getMobile());
        tradeBean2.setAcctName(trade.getAcctName());
        tradeBean2.setCertId(trade.getCertId());
        tradeBean2.setValidthru(trade.getValidthru());// web收银台使用
        tradeBean2.setCvv2(trade.getCvv2());
        tradeBean2.setCardId(Long.valueOf(tradeBean2.getBindCardId()));
        model.put("trade", tradeBean2);
        return new ModelAndView("/fastpay/pay", model);
    }

    @RequestMapping("/getCodeForPay")
    @ResponseBody
    public Object mobileVerifCode(TradeBean trade) {
        ResultBean bean = null;
        try {
            // 路由控制,选择正确的交易渠道进行手机验证码 业务获取短信验证码（绑卡）
            ResultBean routResultBean = routeConfigService.getTransRout(
                    DateUtil.getCurrentDateTime(), trade.getAmount(),
                    StringUtil.isNotEmpty(trade.getMerchId())?trade.getMerchId():trade.getSubMerchId(), trade.getBusicode(), trade.getCardNo(),
                    trade.getCashCode());
            if (routResultBean.isResultBool()) {
                String routId = routResultBean.getResultObj().toString();
                IQuickPayTrade quickPayTrade = TradeAdapterFactory
                        .getInstance().getQuickPayTrade(routId);
                bean = quickPayTrade.sendMarginSms(trade);
            } else {
                return routResultBean;
            }
        } catch (Exception e) {
            e.printStackTrace();
            bean = new ResultBean("RC99", "短信发送失败!");
        }
        return bean;
    }

    /**
     * 证联支付基金支付渠道
     * 
     * @param trade
     * @param routId
     * @param model
     * @return
     * @throws Exception
     */
    private ResultBean zlpayChannel(TradeBean trade,
            String routId,
            Map<String, Object> model) throws Exception {
        ResultBean returnBean = null;
        String gateWayOrderId = trade.getOrderId();
        RoutBean routBean = routeProcessService.getCurrentStep(routId,
                trade.getCurrentSetp(), trade.getBusicode());
        IQuickPayTrade quickPayTrade = TradeAdapterFactory.getInstance()
                .getQuickPayTrade(routBean.getChnlcode());
        trade.setCurrentSetp(routBean.getTxncode_current());
        ResultBean resultBean = quickPayTrade.marginRegister(trade);
        // 更新核心交易表路由信息
        txnsLogService.updateRoutInfo(trade.getTxnseqno(), routId,
                routBean.getTxncode_current(), trade.getCashCode());
        trade.setCurrentSetp(routBean.getTxncode_current());
        if (resultBean.isResultBool()) {// 同步开户成功
            ZLPayResultBean zlPayResult = (ZLPayResultBean) resultBean
                    .getResultObj();
            ResultBean routResultBean = routeConfigService.getTransRout(
                    DateUtil.getCurrentDateTime(), trade.getAmount(),
                    StringUtil.isNotEmpty(trade.getMerchId())?trade.getMerchId():trade.getSubMerchId(), trade.getBusicode(), trade.getCardNo(),
                    trade.getCashCode());

            if (routResultBean.isResultBool()) {
                routBean = routeProcessService.getCurrentStep(routId,
                        trade.getCurrentSetp(), trade.getBusicode());
                quickPayTrade = TradeAdapterFactory.getInstance()
                        .getQuickPayTrade(routBean.getChnlcode());
                trade.setCurrentSetp(routBean.getTxncode_current());
                routId = routResultBean.getResultObj().toString();
                trade.setUserId(zlPayResult.getUserId());
                // 支付机构号暂时未定义
                PayPartyBean payPartyBean = new PayPartyBean(
                        trade.getTxnseqno(), "01", trade.getOrderId(), "",
                        ConsUtil.getInstance().cons.getInstuId(), "",
                        DateUtil.getCurrentDateTime(), "", trade.getCardNo(),
                        routId, routBean.getTxncode_current());
                txnsLogService.updatePayInfo_Fast(payPartyBean);
                // 更新核心交易表路由信息
                txnsLogService.updateRoutInfo(trade.getTxnseqno(), routId,
                        routBean.getTxncode_current(), trade.getCashCode());
                resultBean = quickPayTrade.onlineDepositShort(trade);
                if (resultBean.isResultBool()) {// 快捷支付成功
                    zlPayResult = (ZLPayResultBean) resultBean.getResultObj();
                    // 完成核心交易的记录和网关交易记录的更新
                    gateWayService.saveSuccessTrade(trade.getTxnseqno(),
                            gateWayOrderId, zlPayResult);
                    // 获取下一步路由步骤，进行账务处理
                    routBean = routeProcessService.getNextRoutStep(routId,
                            trade.getCurrentSetp(), trade.getBusicode());
                    log.info("nextStep:" + routBean.getTxncode_next());
                    String commiteTime = DateUtil.getCurrentDateTime();
                    IAccounting accounting = AccountingAdapterFactory
                            .getInstance().getAccounting(trade.getBusitype());
                    ResultBean accountingResultBean = accounting
                            .accountedFor(trade.getTxnseqno());
                    if (accountingResultBean.isResultBool()) {// 账务处理成功
                        // 处理同步通知和异步通知
                        // 根据原始订单拼接应答报文，异步通知商户
                        // gateWayOrderId
                        TxnsOrderinfoModel gatewayOrderBean = gateWayService
                                .getOrderinfoByOrderNoAndMemberId(
                                        gateWayOrderId, trade.getMerchId());
                        AppPartyBean appParty = new AppPartyBean(OrderNumber
                                .getInstance().generateAppOrderNo(),
                                "000000000000", commiteTime,
                                DateUtil.getCurrentDateTime(),
                                trade.getTxnseqno(),
                                routBean.getTxncode_current());
                        txnsLogService.updateAppInfo(appParty);
                        ResultBean orderResp = gateWayService
                                .generateRespMessage(gateWayOrderId,
                                        trade.getMerchId());
                        if (orderResp.isResultBool()) {
                            OrderRespBean respBean = (OrderRespBean) orderResp
                                    .getResultObj();
                            model.put(
                                    "suburl",
                                    gatewayOrderBean.getFronturl()
                                            + "?"
                                            + ObjectDynamic.generateParamer(
                                                    respBean, false, null));
                            // 异步消息通知
                            new SynHttpRequestThread(trade.getMerchId(),
                                    trade.getTxnseqno(),
                                    gatewayOrderBean.getBackurl(),
                                    respBean.getNotifyParam()).start();
                            returnBean = new ResultBean("success");
                            model.put("txnseqno", trade.getTxnseqno());
                            model.put("errMsg", "交易处理成功");
                            model.put("respCode", "0000");
                            return returnBean;
                        } else {
                            model.put("txnseqno", trade.getTxnseqno());
                            model.put("errMsg", orderResp.getErrMsg());
                            model.put("respCode", orderResp.getErrCode());
                        }

                    } else {
                        model.put("txnseqno", trade.getTxnseqno());
                        model.put("errMsg", accountingResultBean.getErrMsg());
                        model.put("respCode", accountingResultBean.getErrCode());
                    }

                } else {
                    zlPayResult = (ZLPayResultBean) resultBean.getResultObj();
                    // 完成核心交易的记录和网关交易记录的更新
                    gateWayService.saveFailTrade(trade.getTxnseqno(),
                            gateWayOrderId, zlPayResult);
                }
            }

        }
        model.put("errMsg", resultBean.getErrMsg());
        model.put("respCode", resultBean.getErrCode());
        model.put("txnseqno", trade.getTxnseqno());
        return null;
    }

    /**
     * 融宝支付渠道
     * 
     * @param trade
     *            交易信息实体类
     * @param routId
     *            路由版本
     * @param model
     *            页面传参参数
     * @return
     * @throws Exception
     */
    private ResultBean reaPayChannel(TradeBean trade,
            String routId,
            Map<String, Object> model) throws Exception {
        
        ResultBean returnBean = new ResultBean("success");
        
        
        /*model.put("txnseqno", trade.getTxnseqno());
        gateWayService.updateOrderToStartPay(trade.getOrderId());
        IQuickPayTrade quickPayTrade = TradeAdapterFactory.getInstance()
                .getQuickPayTrade(routId);
        // trade.setCurrentSetp(routBean.getTxncode_current());
        // 融宝渠道--确认支付
        ResultBean resultBean = quickPayTrade.submitPay(trade);
        // 更新核心交易表路由信息
        txnsLogService.updateRoutInfo(trade.getTxnseqno(), routId, "",
                trade.getCashCode());
        // trade.setCurrentSetp(routBean.getTxncode_current());

        if (resultBean.isResultBool()) {// 确认支付完成，此时只是没有异常出现
            ReaPayResultBean payResult = (ReaPayResultBean) resultBean
                    .getResultObj();

            ResultBean routResultBean = routeConfigService.getTransRout(
                    DateUtil.getCurrentDateTime(), trade.getAmount(),
                    trade.getMerchId(), trade.getBusicode(), trade.getCardNo(),
                    trade.getCashCode());
            if (routResultBean.isResultBool()) {
                // routBean = routeProcessService.getCurrentStep(routId,
                // trade.getCurrentSetp(),trade.getBusicode());
                quickPayTrade = TradeAdapterFactory.getInstance()
                        .getQuickPayTrade(routId);
                // trade.setCurrentSetp(routBean.getTxncode_current());
                routId = routResultBean.getResultObj().toString();
                PayPartyBean payPartyBean = new PayPartyBean(
                        trade.getTxnseqno(), "01", trade.getOrderId(), "",
                        ConsUtil.getInstance().cons
                                .getReapay_quickpay_merchant_id(), "",
                        DateUtil.getCurrentDateTime(), "", trade.getCardNo(),
                        routId, "");
                txnsLogService.updatePayInfo_Fast(payPartyBean);
                // 更新核心交易表路由信息
                // txnsLogService.updateRoutInfo(trade.getTxnseqno(), routId,
                // "",trade.getCashCode());
                model.put("errMsg", payResult.getResult_msg());
                model.put("respCode", payResult.getResult_code());

                txnsLogService.updateReaPayRetInfo(trade.getTxnseqno(),
                        payResult);
            }

        }*/
        return returnBean;
    }

    /**
     * 测试渠道
     * 
     * @param trade
     * @param routId
     * @param model
     * @return
     * @throws Exception
     */
    private ResultBean testChannel(TradeBean trade,String routId, Map<String, Object> model) throws Exception {
        model.put("txnseqno", trade.getTxnseqno());
        ResultBean returnBean = new ResultBean("success");
       
       /* IQuickPayTrade quickPayTrade = TradeAdapterFactory.getInstance().getQuickPayTrade(routId);
        // 测试渠道--确认支付
        ResultBean resultBean = quickPayTrade.submitPay(trade);
        if (resultBean.isResultBool()) {// 确认支付完成，此时只是没有异常出现
            ReaPayResultBean payResult = (ReaPayResultBean) resultBean
                    .getResultObj();
            PayPartyBean payPartyBean = new PayPartyBean(trade.getTxnseqno(),
                    "01", trade.getOrderId(), "",
                    ConsUtil.getInstance().cons
                            .getReapay_quickpay_merchant_id(), "",
                    DateUtil.getCurrentDateTime(), "", trade.getCardNo(),
                    routId, "");
            txnsLogService.updatePayInfo_Fast(payPartyBean);// 更新交易渠道信息卡信息，订单状态改为支付中

            model.put("errMsg", payResult.getResult_msg());
            model.put("respCode", payResult.getResult_code());
            txnsLogService.updateReaPayRetInfo(trade.getTxnseqno(), payResult);
            if ("0000".equals(model.get("respCode"))) {

                // 更新绑卡信息
                quickpayCustService.updateCardStatus(trade.getMerUserId(),
                        trade.getCardNo());

                String txnseqno = trade.getTxnseqno();
                TxnsLogModel txnsLog = txnsLogService.get(txnseqno);
                // 完成核心交易的记录和网关交易记录的更新
                gateWayService.saveSuccessReaPayTrade(txnseqno,
                        txnsLog.getAccordno(), payResult);
                String commiteTime = DateUtil.getCurrentDateTime();
                IAccounting accounting = AccountingAdapterFactory.getInstance()
                        .getAccounting(txnsLog.getBusitype());
                try {
                    accounting.accountedFor(txnseqno);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String reaPayOrderNo = txnsLog.getAccordno();
                // 处理同步通知和异步通知
                // 根据原始订单拼接应答报文，异步通知商户
                TxnsOrderinfoModel gatewayOrderBean = gateWayService
                        .getOrderinfoByOrderNoAndMemberId(reaPayOrderNo,
                                trade.getMerchId());
                // 应用方信息
                AppPartyBean appParty = new AppPartyBean(OrderNumber
                        .getInstance().generateAppOrderNo(), "000000000000",
                        commiteTime, DateUtil.getCurrentDateTime(), txnseqno,
                        "");
                txnsLogService.updateAppInfo(appParty);
                ResultBean orderResp = gateWayService.generateRespMessage(
                        reaPayOrderNo, trade.getMerchId());
                OrderRespBean respBean = (OrderRespBean) orderResp
                        .getResultObj();
                model.put(
                        "suburl",
                        gatewayOrderBean.getFronturl()
                                + "?"
                                + ObjectDynamic.generateReturnParamer(respBean,
                                        false, null));
                model.put("errMsg", "交易成功");
                model.put("respCode", "0000");
                // 记录同步发送的日志
                TxnsNotifyTaskModel task = new TxnsNotifyTaskModel(
                        gatewayOrderBean.getFirmemberno(),
                        gatewayOrderBean.getRelatetradetxn(), 1, 1,
                        ObjectDynamic.generateReturnParamer(respBean, false,
                                null), "00", "200",
                        gatewayOrderBean.getFronturl(), "2");
                txnsNotifyTaskService.saveTask(task);
                // 异步消息通知
                OrderAsynRespBean asynRespBean = (OrderAsynRespBean) gateWayService
                        .generateAsyncRespMessage(reaPayOrderNo,
                                trade.getMerchId()).getResultObj();
                Thread httpThread = new SynHttpRequestThread(
                        trade.getMerchId(), trade.getTxnseqno(),
                        gatewayOrderBean.getBackurl(),
                        asynRespBean.getNotifyParam());
                httpThread.start();
            }
        }*/
        return returnBean;
    }

    @RequestMapping("/toBankPay.htm")
    public ModelAndView toSubmitPay(TradeBean trade, @RequestParam("txnseqno_")String txnseqno_) {
        // t_txns_orderinfo的订单号

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("txnseqno", trade.getTxnseqno());
        try {
            String gateWayOrderId = trade.getOrderId();
            TxnsOrderinfoModel orderinfo = gateWayService
                    .getOrderinfoByOrderNoAndMemberId(gateWayOrderId,
                            trade.getMerchId());
            if (orderinfo == null) {
                model.put("errMsg", "无效订单信息");
                model.put("respCode", "RC99");
                model.put("trade", trade);
                model.put("txnseqno", txnseqno_);

                return new ModelAndView("/erro", model);
            }
            if ("00".equals(orderinfo.getStatus())) {
                model.put("errMsg", "订单已经支付成功，请不要重复支付");
                model.put("respCode", "RC99");
                model.put("trade", trade);
                model.put("txnseqno", txnseqno_);
                return new ModelAndView("/erro", model);
            }

            //获取路由信息
            ResultBean routResultBean = routeConfigService.getTransRout(
                    DateUtil.getCurrentDateTime(), trade.getAmount(),
                    StringUtil.isNotEmpty(trade.getMerchId())?trade.getMerchId():trade.getSubMerchId(), trade.getBusicode(), trade.getCardNo(),
                    trade.getCashCode());
            if (routResultBean.isResultBool()) {
                String routId = routResultBean.getResultObj().toString();
                ChannelEnmu channel = ChannelEnmu.fromValue(routId);
                if (StringUtil.isNotEmpty(trade.getBindCardId())) {
                    QuickpayCustModel card = quickpayCustService.getCardByBindId(trade.getBindCardId());
                }
                trade.setPayinstiId(channel.getChnlcode());
                webGateWayService.submitPay(trade);
                model.put("trade", trade);
                switch (channel) {
                    case ZLPAY :
                        zlpayChannel(trade, routId, model);
                        break;
                    case REAPAY :
                        return new ModelAndView("redirect:/gateway/waiting.htm?txnseqno="+ trade.getTxnseqno()+"&reapayOrderNo="+trade.getReaPayOrderNo()+"&orderNo="+trade.getOrderId());
                    case TEST:
                        TxnsOrderinfoModel gatewayOrderBean = gateWayService.getOrderinfoByOrderNoAndMemberId(gateWayOrderId,trade.getMerchId());
                        ResultBean orderResp = gateWayService.generateRespMessage(
                                gatewayOrderBean.getOrderno(), trade.getMerchId());
                        OrderRespBean respBean = (OrderRespBean) orderResp.getResultObj();
                        model.put("suburl",gatewayOrderBean.getFronturl()+ "?"+ ObjectDynamic.generateReturnParamer(respBean, false, null));
                        model.put("errMsg", "交易成功");
                        model.put("respCode", "0000");
                        return new ModelAndView("/fastpay/success", model);
                    case CMBCWITHHOLDING:
                        return new ModelAndView("redirect:/gateway/payment.htm?txnseqno="+ trade.getTxnseqno()+"&reapayOrderNo="+trade.getReaPayOrderNo()+"&orderNo="+trade.getOrderId());
                    case CMBCSELFWITHHOLDING:
                        return new ModelAndView("redirect:/gateway/payment.htm?txnseqno="+ trade.getTxnseqno()+"&reapayOrderNo="+trade.getReaPayOrderNo()+"&orderNo="+trade.getOrderId());
				default:
					break;
                    
                }
                model.put("trade", trade);
            }
        }catch (TradeException e) {
            e.printStackTrace();
            model.put("trade", trade);
            model.put("errMsg", e.getMessage());
            model.put("respCode", "RC99");
            model.put("txnseqno", txnseqno_);
            return new ModelAndView("/erro", model);
        }catch (Exception e) {
            e.printStackTrace();
            model.put("trade", trade);
            model.put("errMsg", "订单信息错误，请重新提交");
            model.put("respCode", "RC99");
            model.put("txnseqno", txnseqno_);
            return new ModelAndView("/erro", model);
        }
        model.put("txnseqno", trade.getTxnseqno());
        return new ModelAndView("/fastpay/result", model);
    }
    @RequestMapping("/waiting.htm")
    public ModelAndView payJump(String orderNo, String txnseqno,String reapayOrderNo ){
        Map<String, Object> model = new HashMap<String, Object>();
        TradeBean tradeBean = new TradeBean();
        tradeBean.setTxnseqno(txnseqno);
        tradeBean.setOrderId(orderNo);
        tradeBean.setReaPayOrderNo(reapayOrderNo);
        model.put("trade", tradeBean);
         return new ModelAndView("/fastpay/pay_jump", model);
    }
    @RequestMapping("/payment.htm")
    public ModelAndView payJumpCMBC(String orderNo, String txnseqno,String reapayOrderNo ){
        Map<String, Object> model = new HashMap<String, Object>();
        TradeBean tradeBean = new TradeBean();
        tradeBean.setTxnseqno(txnseqno);
        tradeBean.setOrderId(orderNo);
        tradeBean.setReaPayOrderNo(reapayOrderNo);
        model.put("trade", tradeBean);
        return new ModelAndView("/fastpay/pay_jump_cmbc", model);
    }
    
    
    @RequestMapping("/queryReaPayTrade")
    @ResponseBody
    public Object queryReaPayTrade(String orderNo) {
        IQuickPayTrade quickPayTrade = null;
        try {
            quickPayTrade = TradeAdapterFactory.getInstance()
                    .getQuickPayTrade(
                            ConsUtil.getInstance().cons.getReapay_chnl_code());
        } catch (TradeException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        TradeBean trade = new TradeBean();
        trade.setReaPayOrderNo(orderNo);
        ResultBean queryResultBean = null;
        ReaPayResultBean payResult = null;
        int[] timeArray = new int[]{1000, 2000, 8000, 16000, 32000};
        try {
            for (int i = 0; i < 5; i++) {
                Thread.currentThread().sleep(timeArray[i]);
                queryResultBean = quickPayTrade.queryTrade(trade);
                payResult = (ReaPayResultBean) queryResultBean.getResultObj();
                if ("completed".equalsIgnoreCase(payResult.getStatus())) {
                    break;
                }
                if ("failed".equalsIgnoreCase(payResult.getStatus())) {
                    break;
                }
                if ("wait".equalsIgnoreCase(payResult.getStatus())) {
                    break;
                }
                if ("processing".equalsIgnoreCase(payResult.getStatus())) {
                    log.info("processing");
                }
                Thread.currentThread().sleep(timeArray[i]);
            }
            return payResult.getStatus();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return "overtime";
    }
    
    @RequestMapping("/queryCMBCTrade")
    @ResponseBody
    public Object queryCMBCTrade(String orderNo,String txnseqno) {
        Map<String, Object> model = new HashMap<String, Object>();
        TxnsLogModel txnsLog = null;
        int[] timeArray = new int[]{1000, 2000, 8000, 16000, 32000,64000};
        try {
            /*for (int i = 0; i < 5; i++) {
                Thread.currentThread().sleep(timeArray[i]);
                queryResultBean = quickPayTrade.queryTrade(trade);
                payResult = (ReaPayResultBean) queryResultBean.getResultObj();
                if ("completed".equalsIgnoreCase(payResult.getStatus())) {
                    break;
                }
                if ("failed".equalsIgnoreCase(payResult.getStatus())) {
                    break;
                }
                if ("wait".equalsIgnoreCase(payResult.getStatus())) {
                    break;
                }
                if ("processing".equalsIgnoreCase(payResult.getStatus())) {
                    log.info("processing");
                }
                Thread.currentThread().sleep(timeArray[i]);
            }
            return payResult.getStatus();*/
            
            for (int i = 0; i < 6; i++) {
                txnsLog=txnsLogService.getTxnsLogByTxnseqno(txnseqno);
                if(StringUtil.isNotEmpty(txnsLog.getPayretcode())){
                    ChannelEnmu channel = ChannelEnmu.fromValue(txnsLog.getPayinst());
                    switch (channel) {
                        case CMBCWITHHOLDING :
                            String serialno = txnsLog.getPayordno();
                            if(!txnsLog.getPayretcode().equals("000000")){
                                model.put("errMsg", txnsLog.getRetinfo());
                                model.put("respCode", txnsLog.getRetcode());
                                TradeBean tradeBean = new TradeBean();
                                tradeBean.setTxnseqno(txnseqno);
                                model.put("trade", tradeBean);
                                model.put("txnseqno", txnseqno);
                                return "failed";
                            }
                            TxnsWithholdingModel withholding = txnsWithholdingService.getWithholdingBySerialNo(serialno);
                            if(!withholding.getExectype().equalsIgnoreCase("R")){
                                model.put("errMsg", txnsLog.getRetinfo());
                                model.put("respCode", txnsLog.getRetcode());
                                TradeBean tradeBean = new TradeBean();
                                tradeBean.setTxnseqno(txnseqno);
                                model.put("trade", tradeBean);
                                model.put("txnseqno", txnseqno);
                                if("S".equalsIgnoreCase(withholding.getExectype())){
                                    return "completed";
                                }else{
                                    return "failed";
                                }
                            }
                            break;
                        case CMBCSELFWITHHOLDING :
                            String tranId = txnsLog.getPayordno();
                            if(!txnsLog.getPayretcode().equals("000000")){
                                model.put("errMsg", txnsLog.getRetinfo());
                                model.put("respCode", txnsLog.getRetcode());
                                TradeBean tradeBean = new TradeBean();
                                tradeBean.setTxnseqno(txnseqno);
                                model.put("trade", tradeBean);
                                model.put("txnseqno", txnseqno);
                                return "failed";
                            }
                            TxnsWithholdingModel withholdingSelf = txnsWithholdingService.getWithholdingBySerialNo(tranId);
                            if(!withholdingSelf.getExectype().equalsIgnoreCase("R")){
                                model.put("errMsg", txnsLog.getRetinfo());
                                model.put("respCode", txnsLog.getRetcode());
                                TradeBean tradeBean = new TradeBean();
                                tradeBean.setTxnseqno(txnseqno);
                                model.put("trade", tradeBean);
                                model.put("txnseqno", txnseqno);
                                if("S".equalsIgnoreCase(withholdingSelf.getExectype())){
                                    return "completed";
                                }else{
                                    return "failed";
                                }
                            }
                            break;
					default:
						break;
                    }
                    
                   
                }
                Thread.currentThread().sleep(timeArray[i]);
            }

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return "overtime";
    }
    

    @RequestMapping("/toNetBank.htm")
    public ModelAndView toBank(TradeBean trade) {
        /*Map<String, Object> model = new HashMap<String, Object>();
        // 网关交易路由
        ResultBean routResultBean = routeConfigService.getTransRout(
                DateUtil.getCurrentDateTime(), trade.getAmount(),
                trade.getMerchId(), trade.getBusicode(), trade.getCardNo(),
                trade.getCashCode());
        if (routResultBean.isResultBool()) {
            String routId = routResultBean.getResultObj().toString();
            RoutBean routBean = routeProcessService.getFirstRoutStep(routId,
                    trade.getBusicode());
            IBankGateWay gateWay = BankGateWayFactory.getInstance()
                    .getBankGateWay(routBean.getChnlcode());
            ResultBean resultBean = gateWay.generateMessage(trade);
            if (resultBean.isResultBool()) {
                Map<String, String> valueMap = (Map<String, String>) resultBean
                        .getResultObj();
                model.put("signedMsg", valueMap.get("signMsg"));
                // 更交易流水支付方信息
                PayPartyBean payPartyBean = new PayPartyBean(
                        trade.getTxnseqno(), "02", valueMap.get("payOrderNo"),
                        "", valueMap.get("payMerchNo"), "",
                        DateUtil.getCurrentDateTime(), "", "", routId,
                        routBean.getTxncode_current());
                payPartyBean.setCashCode(trade.getCashCode());
                txnsLogService.updatePayInfo_ecitic(payPartyBean);
            }
        }*/

        // 由bankcode来判断要走的银行网关

        return new ModelAndView("/bank/order", null);
    }

    @RequestMapping("/ecitic/reciveNotify")
    public ModelAndView reciveNotifyEcitic(String SIGNREQMSG, String reqID) {
       /* IBankGateWay gateWay = BankGateWayFactory.getInstance().getBankGateWay(
                "0302");
        ResultBean resultBean = gateWay.finishTrade(SIGNREQMSG);
        if (resultBean.isResultBool()) {

        }*/
        return null;
    }

    @RequestMapping("/ecitic/reciveSync")
    public ModelAndView reciveSyncEcitic(String SIGNREQMSG) {
        /*Map<String, Object> model = new HashMap<String, Object>();
        
         * IBankGateWay gateWay =
         * BankGateWayFactory.getInstance().getBankGateWay("0302"); ResultBean
         * resultBean = gateWay.parseMessage(SIGNRESMSG);
         * if(resultBean.isResultBool()){ model.put("ecitic",
         * resultBean.getResultObj()); }
         
        log.info("SIGNRESMSG:" + SIGNREQMSG);
        IBankGateWay gateWay = BankGateWayFactory.getInstance().getBankGateWay(
                "ZX000001");
        ResultBean resultBean = gateWay.finishTrade(SIGNREQMSG);
        if (resultBean.isResultBool()) {
            TxnsLogModel txnsLog = txnsLogService
                    .getTxnsLogByTxnseqno(resultBean.getResultObj().toString());
            RoutBean routBean = routeProcessService.getNextRoutStep(
                    txnsLog.getRoutver(), txnsLog.getTxncode(),
                    txnsLog.getBusicode());
            log.info("nextStep:" + routBean.getTxncode_next());
            IAccounting accounting = AccountingAdapterFactory.getInstance()
                    .getAccounting(routBean.getTxncode_next());
            ResultBean accountingResultBean = accounting
                    .accountedFor(resultBean.getResultObj().toString());
            model.put("errMsg", "交易成功");
        } else {
            model.put("errMsg", "交易失败");
        }
        return new ModelAndView("/bank/result", model);*/
        return null;
    }

    @RequestMapping("/test")
    public ModelAndView test(String orderNo) throws Exception {
        TxnsOrderinfoModel gatewayOrderBean = gateWayService
                .getOrderinfoByOrderNo(orderNo);
        /*
         * ResultBean orderResp = gateWayService.generateRespMessage(orderNo);
         * OrderRespBean respBean = (OrderRespBean) orderResp.getResultObj();
         * new SynHttpRequestThread(gatewayOrderBean.getBackurl(),
         * respBean.getNotifyParam()).start();
         */

        ResultBean orderResp = gateWayService.generateRespMessage(orderNo, "");
        OrderRespBean respBean = (OrderRespBean) orderResp.getResultObj();
        log.info(gatewayOrderBean.getFronturl() + "?"
                + ObjectDynamic.generateReturnParamer(respBean, false, null));;
        return null;
    }

    @RequestMapping("/getCardType")
    @ResponseBody
    public Object getCardType(String cardNo) {
        log.info("bankCardNo" + cardNo);
        try {
            boolean checkResult = BankCardUtil.checkBankCard(cardNo);
            log.info("Bank Card No Check Result :" + checkResult);
        } catch (TradeException e) {
            // TODO Auto-generated catch block
            return "carderror";
        }
        Map<String, Object> cardMap = routeConfigService.getCardInfo(cardNo);
        if (cardMap == null) {
            return "error";
        }
        return cardMap;
    }
    @RequestMapping("/reciveSyncReaPay")
    @ResponseBody
    public String reciveSyncReaPay(String encryptkey,
            String data,
            String merchant_id,
            HttpServletResponse response) {
        try {
            log.info("reapay data :" + data);
            log.info("encryptkey  :" + encryptkey);
            PrivateKey pvkformPfx = RSA
                    .getPvkformPfx(ConsUtil.getInstance().cons
                            .getReapay_quickpay_prikey(), ConsUtil
                            .getInstance().cons.getReapay_quickpay_prikey_pwd());
            String decryptData = RSA.decrypt(encryptkey, pvkformPfx);
            String respMsg = AES.decryptFromBase64(data, decryptData);
            log.info("reapay ansyc message :" + respMsg);
            ReaPayResultBean reaPayResult = ReaPayTradeAnalyzer
                    .generateAsyncResultBean(respMsg);
            // 查询快捷交易数据获取交易序列号
            List<TxnsQuickpayModel> quickPayList = txnsQuickpayService
                    .queryTxnsByOrderNo(reaPayResult.getOrder_no());
            if ("TRADE_FINISHED".equalsIgnoreCase(reaPayResult.getStatus()
                    .trim())) {// 交易成功
                if (quickPayList.size() > 0) {
                    String txnseqno = quickPayList.get(0)
                            .getRelatetradetxnseqno();
                    TxnsLogModel txnsLog = txnsLogService.get(txnseqno);
                    if ("00".equals(txnsLog.getApporderstatus())) {
                        log.info("账务处理成功");
                        response.setContentType("text/html");
                        response.setCharacterEncoding("utf-8");
                        response.getWriter().print("success");
                        response.getWriter().flush();
                        response.getWriter().close();
                        return "";
                    }
                    // 完成核心交易的记录和网关交易记录的更新
                    gateWayService.saveSuccessReaPayTrade(txnseqno,
                            txnsLog.getAccordno(), reaPayResult);
                    String commiteTime = DateUtil.getCurrentDateTime();
                    IAccounting accounting = AccountingAdapterFactory
                            .getInstance().getAccounting(txnsLog.getBusitype());
                    String reaPayOrderNo = txnsLog.getAccordno();
                    // 处理同步通知和异步通知
                    // 根据原始订单拼接应答报文，异步通知商户
                    TxnsOrderinfoModel gatewayOrderBean = gateWayService
                            .getOrderinfoByOrderNoAndMemberId(reaPayOrderNo,
                                    txnsLog.getAccfirmerno());
                    // 应用方信息
                    AppPartyBean appParty = new AppPartyBean(OrderNumber
                            .getInstance().generateAppOrderNo(),
                            "000000000000", commiteTime,
                            DateUtil.getCurrentDateTime(), txnseqno, "AC000000");
                    txnsLogService.updateAppInfo(appParty);
                    ResultBean orderResp = gateWayService
                            .generateAsyncRespMessage(reaPayOrderNo,
                                    txnsLog.getAccfirmerno());
                    if (orderResp.isResultBool()) {
                        OrderAsynRespBean respBean = (OrderAsynRespBean) orderResp
                                .getResultObj();
                        // 异步消息通知
                        new SynHttpRequestThread(
                                gatewayOrderBean.getFirmemberno(),
                                gatewayOrderBean.getRelatetradetxn(),
                                gatewayOrderBean.getBackurl(),
                                respBean.getNotifyParam()).start();
                    }
                    accounting.accountedFor(txnseqno);

                    /*
                     * TxnsOrderinfoModel gatewayOrderBean =
                     * gateWayService.getOrderinfoByOrderNo(reaPayOrderNo);
                     * ResultBean orderResp =
                     * gateWayService.generateRespMessage(reaPayOrderNo);
                     * OrderRespBean respBean = (OrderRespBean)
                     * orderResp.getResultObj(); new
                     * SynHttpRequestThread(gatewayOrderBean.getBackurl(),
                     * respBean.getNotifyParam()).run();
                     */

                }
            } else if ("TRADE_FAILURE".equalsIgnoreCase(reaPayResult
                    .getStatus().trim())) {// 交易失败
                String txnseqno = quickPayList.get(0).getRelatetradetxnseqno();
                TxnsLogModel txnsLog = txnsLogService.get(txnseqno);
                // 完成核心交易的记录和网关交易记录的更新
                gateWayService.saveFailReaPayTrade(txnseqno,
                        txnsLog.getAccordno(), reaPayResult);
                String reaPayOrderNo = txnsLog.getAccordno();
                TxnsOrderinfoModel gatewayOrderBean = gateWayService
                        .getOrderinfoByOrderNoAndMemberId(reaPayOrderNo,
                                txnsLog.getAccfirmerno());
                ResultBean orderResp = gateWayService.generateAsyncRespMessage(
                        reaPayOrderNo, txnsLog.getAccfirmerno());
                if (orderResp.isResultBool()) {
                    OrderAsynRespBean respBean = (OrderAsynRespBean) orderResp
                            .getResultObj();
                    // 异步消息通知
                    new SynHttpRequestThread(gatewayOrderBean.getFirmemberno(),
                            gatewayOrderBean.getRelatetradetxn(),
                            gatewayOrderBean.getBackurl(),
                            respBean.getNotifyParam()).start();
                }
            }
            response.setContentType("text/html");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print("success");
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping("/success.htm")
    public ModelAndView toSuccessPage(String orderNo, String txnseqno)
            throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(txnseqno);
        TxnsOrderinfoModel gatewayOrderBean = gateWayService
                .getOrderinfoByOrderNoAndMemberId(txnsLog.getAccordno(),txnsLog.getAccfirmerno());
        ResultBean orderResp = gateWayService.generateRespMessage(orderNo,txnsLog.getAccfirmerno());
        OrderRespBean respBean = (OrderRespBean) orderResp.getResultObj();
        model.put("suburl", gatewayOrderBean.getFronturl() + "?"+ ObjectDynamic.generateReturnParamer(respBean, false, null));
        model.put("errMsg", "交易成功");
        model.put("respCode", "0000");
        model.put("txnseqno", txnseqno);
        // 记录同步发送的日志
        TxnsNotifyTaskModel task = new TxnsNotifyTaskModel(
                gatewayOrderBean.getFirmemberno(),
                gatewayOrderBean.getRelatetradetxn(), 1, 1,
                ObjectDynamic.generateReturnParamer(respBean, false, null),
                "00", "200", gatewayOrderBean.getFronturl(), "2");
        txnsNotifyTaskService.saveTask(task);
        return new ModelAndView("/fastpay/success", model);
    }
    @RequestMapping("/fail.htm")
    public ModelAndView toFailPage(String orderNo, String txnseqno) {
        Map<String, Object> model = new HashMap<String, Object>();
        // TxnsOrderinfoModel gatewayOrderBean =
        // gateWayService.getOrderinfoByOrderNo(orderNo);
        gateWayService.updateOrderToFail(orderNo);
        TxnsLogModel txnsLog = txnsLogService.get(txnseqno);
        model.put("errMsg", txnsLog.getRetinfo());
        model.put("respCode", txnsLog.getRetcode());
        model.put("txnseqno", txnseqno);;
        return new ModelAndView("/erro", model);
    }

    @RequestMapping("/processing.htm")
    public ModelAndView toProcessingPage(String orderNo, String txnseqno) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("errMsg", "交易正在处理中，请稍后查询");
        model.put("respCode", "ZL34");
        model.put("txnseqno", txnseqno);
        return new ModelAndView("/fastpay/result", model);
    }

    @RequestMapping("/wait.htm")
    public ModelAndView toWaitPage(String orderNo, String txnseqno,String reapayOrderNo) {
        Map<String, Object> model = new HashMap<String, Object>();
        TxnsLogModel txnsLog = null;
        int[] timeArray = new int[]{1000, 2000, 8000, 16000, 32000};
        try {
            for (int i = 0; i < 5; i++) {
                txnsLog=txnsLogService.getTxnsLogByTxnseqno(txnseqno);
                if(StringUtil.isNotEmpty(txnsLog.getPayretcode())){
                    ChannelEnmu channel = ChannelEnmu.fromValue(txnsLog.getPayinst());
                    switch (channel) {
                        case REAPAY :
                            if ("0000".equals(txnsLog.getPayretcode())
                                    || "3006".equals(txnsLog.getPayretcode())
                                    || "3053".equals(txnsLog.getPayretcode())
                                    || "3054".equals(txnsLog.getPayretcode())
                                    || "3056".equals(txnsLog.getPayretcode())
                                    || "3083".equals(txnsLog.getPayretcode())
                                    || "3081".equals(txnsLog.getPayretcode())) {
                                //return new ModelAndView("redirect:/gateway/cash.htm?txnseqno=" + txnseqno);
                                TradeBean tradeBean = new TradeBean();
                                tradeBean.setTxnseqno(txnseqno);
                                tradeBean.setOrderId(orderNo);
                                tradeBean.setReaPayOrderNo(reapayOrderNo);
                                model.put("trade", tradeBean);
                                return new ModelAndView("/fastpay/pay_jump", model);
                            }else{
                                model.put("errMsg", txnsLog.getPayretinfo());
                                model.put("respCode", txnsLog.getPayretcode());
                                TradeBean tradeBean = new TradeBean();
                                tradeBean.setTxnseqno(txnseqno);
                                model.put("trade", tradeBean);
                                model.put("txnseqno", txnseqno);
                                return new ModelAndView("/erro", model);
                            }

                        case CMBCWITHHOLDING :
                            String serialno = txnsLog.getPayordno();
                            if(!txnsLog.getPayretcode().equals("000000")){
                                model.put("errMsg", txnsLog.getRetinfo());
                                model.put("respCode", txnsLog.getRetcode());
                                TradeBean tradeBean = new TradeBean();
                                tradeBean.setTxnseqno(txnseqno);
                                model.put("trade", tradeBean);
                                model.put("txnseqno", txnseqno);
                                return new ModelAndView("redirect:/gateway/fail.htm?txnseqno="+ txnseqno+"&orderNo="+txnsLog.getAccordno());
                            }
                            TxnsWithholdingModel withholding = txnsWithholdingService.getWithholdingBySerialNo(serialno);
                            if(!withholding.getExectype().equalsIgnoreCase("R")){
                                model.put("errMsg", txnsLog.getPayretinfo());
                                model.put("respCode", txnsLog.getPayretcode());
                                TradeBean tradeBean = new TradeBean();
                                tradeBean.setTxnseqno(txnseqno);
                                model.put("trade", tradeBean);
                                model.put("txnseqno", txnseqno);
                                if("S".equalsIgnoreCase(withholding.getExectype())){
                                    return new ModelAndView("redirect:/gateway/success.htm?txnseqno="+ txnseqno+"&orderNo="+txnsLog.getAccordno());
                                }else{
                                    return new ModelAndView("redirect:/gateway/fail.htm?txnseqno="+ txnseqno+"&orderNo="+txnsLog.getAccordno());
                                }
                            }
                            break;
                        case CMBCSELFWITHHOLDING :
                            String tranId = txnsLog.getPayordno();
                            if(!txnsLog.getPayretcode().equals("000000")){
                                model.put("errMsg", txnsLog.getRetinfo());
                                model.put("respCode", txnsLog.getRetcode());
                                TradeBean tradeBean = new TradeBean();
                                tradeBean.setTxnseqno(txnseqno);
                                model.put("trade", tradeBean);
                                model.put("txnseqno", txnseqno);
                                return new ModelAndView("redirect:/gateway/fail.htm?txnseqno="+ txnseqno+"&orderNo="+txnsLog.getAccordno());
                            }
                            TxnsWithholdingModel withholdingSelf = txnsWithholdingService.getWithholdingBySerialNo(tranId);
                            if(!withholdingSelf.getExectype().equalsIgnoreCase("R")){
                                model.put("errMsg", txnsLog.getPayretinfo());
                                model.put("respCode", txnsLog.getPayretcode());
                                TradeBean tradeBean = new TradeBean();
                                tradeBean.setTxnseqno(txnseqno);
                                model.put("trade", tradeBean);
                                model.put("txnseqno", txnseqno);
                                if("S".equalsIgnoreCase(withholdingSelf.getExectype())){
                                    return new ModelAndView("redirect:/gateway/success.htm?txnseqno="+ txnseqno+"&orderNo="+txnsLog.getAccordno());
                                }else{
                                    return new ModelAndView("redirect:/gateway/fail.htm?txnseqno="+ txnseqno+"&orderNo="+txnsLog.getAccordno());
                                }
                            }
                            break;
                    }
                    
                   
                }
                Thread.currentThread().sleep(timeArray[i]);
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        model.put("errMsg", txnsLog.getPayretinfo());
        model.put("respCode", txnsLog.getPayretcode());
        TradeBean tradeBean = new TradeBean();
        tradeBean.setTxnseqno(txnseqno);
        model.put("trade", tradeBean);
        model.put("txnseqno", txnseqno);
        return new ModelAndView("/fastpay/result", model);
    }
    @RequestMapping("/error.htm")
    public ModelAndView toExceptionPage(String orderNo, String txnseqno) {
        Map<String, Object> model = new HashMap<String, Object>();
        gateWayService.updateOrderToFail(orderNo);
        model.put("errMsg", "系统异常,请联系证联金融客服");
        model.put("respCode", "ZL34");
        model.put("txnseqno", txnseqno);
        return new ModelAndView("/fastpay/result", model);
    }

    @RequestMapping("/testJump")
    @ResponseBody
    public String testJump( HttpServletResponse response) throws Exception {
        // Map<String, Object> model = new HashMap<String, Object>();
        // ResultBean orderResp =
        // gateWayService.generateAsyncRespMessage("2015102616287790",,txnsLog.getAccfirmerno());
        // OrderAsynRespBean respBean = (OrderAsynRespBean)
        // orderResp.getResultObj();
        // model.put("suburl",
        // "http://192.168.101.209:8081/demo/ReciveNotifyServlet"+"?"+ObjectDynamic.generateParamer(orderResp,
        // false, null));
        // new
        // SynHttpRequestThread("http://192.168.101.209:8081/demo/ReciveNotifyServlet",
        // respBean.getNotifyParam()).start();
        
        
        return "success";
    }

    @RequestMapping("/query.htm")
    @ResponseBody
    public Object queryTrade(QueryBean queryBean) {
        QueryResultBean queryResultBean = new QueryResultBean(queryBean);
        // 验证订单数据有效性，用hibernate validator处理
        ResultBean resultBean = GateWayTradeAnalyzer
                .validateQueryOrder(queryBean);
        if (!resultBean.isResultBool()) {
            // 订单信息长度和非空验证未通过
            queryResultBean.setStatus("error");
            queryResultBean.setRetinfo(resultBean.getErrMsg());
        }
        ResultBean signResultBean = gateWayService.verifyQueryOrder(queryBean);
        if (!signResultBean.isResultBool()) {
            //订单信息验签未通过
            queryResultBean.setStatus("error");
            queryResultBean.setRetinfo(signResultBean.getErrMsg());
        }
        TxncodeDefModel busiModel = txncodeDefService.getBusiCode(
                queryBean.getTxnType(), queryBean.getTxnSubType(),
                queryBean.getBizType());
        if (busiModel == null) {
            queryResultBean.setStatus("error");
            queryResultBean.setRetinfo("业务类型错误");
        }
        TxnsLogModel txnsLog = txnsLogService.queryTrade(queryBean);
        if (txnsLog == null) {
            queryResultBean.setStatus("error");
            queryResultBean.setRetinfo("无此交易");
        } else {
            if (StringUtil.isNotEmpty(txnsLog.getRetcode())) {
                if ("00".equals(txnsLog.getRetcode().substring(2))) {
                    queryResultBean.setStatus("success");
                    queryResultBean.setRetinfo("无此交易");
                } else {
                    queryResultBean.setStatus("failed");
                    queryResultBean.setRetinfo("交易失败");
                }
            } else {
                queryResultBean.setStatus("processing");
                queryResultBean.setRetinfo("交易正在处理中");
            }
        }
        return gateWayService.generateQueryResultBean(queryResultBean)
                .getResultObj();
    }

    @RequestMapping("/withdraw.htm")
    public ModelAndView withdraw(TradeBean tradeBean) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            TxnsOrderinfoModel orderinfo = gateWayService
                    .getOrderinfoByOrderNoAndMemberId(tradeBean.getOrderId(),
                            tradeBean.getMerchId());
            if ("02".equals(orderinfo.getStatus())) {
                model.put("errMsg", "提现正在审核中，请不要重复提交");
                model.put("txnseqno", tradeBean.getTxnseqno());
                return new ModelAndView("/erro_gw", model);
            }
            TxnsWithdrawModel withdraw = new TxnsWithdrawModel(tradeBean);
            if (StringUtil.isNotEmpty(tradeBean.getBindCardId())) {
                QuickpayCustModel card = quickpayCustService
                        .getCardByBindId(tradeBean.getBindCardId());
                withdraw.setAcctname(card.getAccname());
                withdraw.setAcctno(card.getCardno());
            }
            txnsWithdrawService.saveWithdraw(withdraw);
            gateWayService.updateOrderToStartPay(tradeBean.getOrderId());
            model.put(
                    "suburl",
                    orderinfo.getFronturl()
                            + "?"
                            + ObjectDynamic.generateReturnParamer(
                                    gateWayService
                                            .generateWithdrawRespMessage(tradeBean
                                                    .getOrderId()), false, null));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            model.put("errMsg", "提现申请失败");
            model.put("respCode", "ZL34");
            model.put("txnseqno", tradeBean.getTxnseqno());
            return new ModelAndView("/fastpay/erro", model);
        }
        model.put("errMsg", "提现申请成功");
        return new ModelAndView("/fastpay/success", model);
    }
    
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
