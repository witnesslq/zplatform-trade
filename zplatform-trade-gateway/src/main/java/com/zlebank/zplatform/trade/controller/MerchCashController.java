/* 
 * MerchCashController.java  
 * 
 * version TODO
 *
 * 2016年3月31日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.acc.bean.TradeInfo;
import com.zlebank.zplatform.acc.service.AccEntryService;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.member.bean.CoopInsti;
import com.zlebank.zplatform.member.bean.EnterpriseBean;
import com.zlebank.zplatform.member.bean.QuickpayCustBean;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.pojo.PojoMerchDeta;
import com.zlebank.zplatform.member.service.CoopInstiService;
import com.zlebank.zplatform.member.service.EnterpriseService;
import com.zlebank.zplatform.member.service.MemberBankCardService;
import com.zlebank.zplatform.member.service.MerchService;
import com.zlebank.zplatform.trade.adapter.quickpay.IQuickPayTrade;
import com.zlebank.zplatform.trade.analyzer.GateWayTradeAnalyzer;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.bean.TradeBean;
import com.zlebank.zplatform.trade.bean.enums.ChannelEnmu;
import com.zlebank.zplatform.trade.bean.gateway.OrderBean;
import com.zlebank.zplatform.trade.bean.gateway.OrderRespBean;
import com.zlebank.zplatform.trade.bean.gateway.RiskRateInfoBean;
import com.zlebank.zplatform.trade.dao.RspmsgDAO;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.factory.TradeAdapterFactory;
import com.zlebank.zplatform.trade.model.PojoRspmsg;
import com.zlebank.zplatform.trade.model.TxnsLogModel;
import com.zlebank.zplatform.trade.model.TxnsOrderinfoModel;
import com.zlebank.zplatform.trade.model.TxnsWithdrawModel;
import com.zlebank.zplatform.trade.service.IGateWayService;
import com.zlebank.zplatform.trade.service.IProdCaseService;
import com.zlebank.zplatform.trade.service.IRouteConfigService;
import com.zlebank.zplatform.trade.service.ITxnsLogService;
import com.zlebank.zplatform.trade.service.ITxnsWithdrawService;
import com.zlebank.zplatform.trade.service.IWebGateWayService;
import com.zlebank.zplatform.trade.utils.AmountUtil;
import com.zlebank.zplatform.trade.utils.DateUtil;
import com.zlebank.zplatform.trade.utils.ObjectDynamic;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年3月31日 下午3:06:08
 * @since
 */
@Controller
@RequestMapping("merch")
public class MerchCashController {
	private static final Log log = LogFactory.getLog(MerchCashController.class);
	@Autowired
	private IGateWayService gateWayService;
	@Autowired
	private RspmsgDAO rspmsgDAO;
	@Autowired
	private IProdCaseService prodCaseService;
	@Autowired
	private MerchService merchService;
	@Autowired
    private ITxnsLogService txnsLogService;
	@Autowired
    private CoopInstiService coopInstiService;
	@Autowired
    private MemberBankCardService memberBankCardService;
	@Autowired
	private EnterpriseService enterpriseService;
	@Autowired
    private IWebGateWayService webGateWayService;
	@Autowired
	private IRouteConfigService routeConfigService;
	@Autowired
    private AccEntryService accEntryService;
	@Autowired
    private ITxnsWithdrawService txnsWithdrawService;
	@RequestMapping("/coporder.htm")
	public ModelAndView pay(OrderBean order, HttpSession httpSession,
			HttpServletRequest request) {
		log.info("receive web message(json):" + JSON.toJSONString(order));
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			if (!validateOrder(order, model)) {
				return new ModelAndView("/erro_gw", model);
			}
			// 记录付款人的IP
			if (StringUtil.isEmpty(order.getCustomerIp())) {
				order.setCustomerIp(getIpAddr(request));
			}
			// 订单记录和业务逻辑处理,取得商户信息，记录交易数据（核心）和订单详细信息，分析交易所属业务
			String txnseqno = gateWayService.dealWithMerchOrder(order);
			return new ModelAndView("redirect:/merch/cash.htm?txnseqno="+ txnseqno);
		} catch (Exception e) {
			e.printStackTrace();
			model.put("errMsg", "订单信息错误，请重新提交");
			model.put("errCode", "RC99");
		}
		return new ModelAndView("/erro_gw", model);
	}
	
	
	@RequestMapping(value = "/cash.htm", method = RequestMethod.GET)
    public ModelAndView cashIndex(@RequestParam("txnseqno") String txnseqno,HttpServletRequest request) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(txnseqno)) {
                model.put("errMsg", "交易失败，查无此交易");
                return new ModelAndView("/erro_gw", model);
            }
            //交易流水数据
            TxnsLogModel txnsLog = txnsLogService.getTxnsLogByTxnseqno(txnseqno);
            //合作机构
            CoopInsti coopInsti = coopInstiService.getInstiByInstiCode(txnsLog.getAccfirmerno());
            EnterpriseBean enterprise = enterpriseService.getEnterpriseByMemberId(txnsLog.getAccsecmerno());
            TxnsOrderinfoModel orderInfo = gateWayService.getOrderinfoByOrderNoAndMemberId(txnsLog.getAccordno(),txnsLog.getAccfirmerno());
            if ("00".equals(orderInfo.getStatus())) {// 支付成功的订单不可重复支付
                model.put("errMsg", "交易成功，请不要重复支付");
                model.put("errCode", "RC10");
                return new ModelAndView("/erro_gw", model);
            }
            model.put("merchName", coopInsti.getInstiName());//合作机构名称
            model.put("merchId", txnsLog.getAccfirmerno());
            model.put("goodsName", orderInfo.getGoodsname());
            model.put("txnseqno", txnseqno);
            model.put("memberId", txnsLog.getAccmemberid());
            model.put("busicode", txnsLog.getBusicode());
            model.put("busitype", txnsLog.getBusitype());
            model.put("orderId", txnsLog.getAccordno());
            model.put("subMerName",enterprise.getEnterpriseName());//商户名称
            model.put("txnTime",DateUtil.formatDateTime(DateUtil.parse(DateUtil.DEFAULT_DATE_FROMAT,orderInfo.getOrdercommitime())));
            model.put("txnAmt", txnsLog.getAmount());
            model.put("orderDesc", orderInfo.getOrderdesc());
            model.put("tn", orderInfo.getTn());
            model.put("amount_y", AmountUtil.numberFormat(txnsLog.getAmount()));
            if (StringUtil.isNotEmpty(orderInfo.getSecmemberno())) {
                // 获取会员已绑定的银行卡
                PagedResult<QuickpayCustBean> bindCardList =  memberBankCardService.queryMemberBankCard(orderInfo.getSecmemberno(), "0", 0, 10);
                model.put("bindCardList", bindCardList.getPagedResult());
                String cardType = "1";
                // 显示会员已绑定的银行卡
                PagedResult<QuickpayCustBean> cardPageList = memberBankCardService.queryMemberBankCard(orderInfo.getSecmemberno(), cardType, 0, 10);
                List<QuickpayCustBean> cardList = cardPageList.getPagedResult();
                model.put("cardList", cardList);
                //获取请求方IP
                model.put("memberIP", orderInfo.getPayerip());
            }

            if ("3000".equals(txnsLog.getBusitype())) {// 提现交易 借记卡
                return new ModelAndView("/withdraw_merch", model);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            model.put("errMsg", "交易失败，系统忙，请稍后再试！");
            model.put("errCode", "RC10");
            return new ModelAndView("/erro_gw", model);
        }
        return new ModelAndView("/index_merch", model);
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
		ResultBean signResultBean = gateWayService.verifyOrder(order);
		if (!signResultBean.isResultBool()) {
			// 订单信息验签未通过
			model.put("errMsg", signResultBean.getErrMsg());
			model.put("errCode", signResultBean.getErrCode());
			return false;
		}

		// 验证订单号是否重复
		try {
			gateWayService.verifyRepeatWebOrder(order.getOrderId(),
					order.getTxnTime(), order.getTxnAmt(),
					order.getCoopInstiId(), order.getMerId());
		} catch (TradeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			PojoRspmsg msg = rspmsgDAO.get(e.getCode());
			model.put("errMsg", msg.getRspinfo());
			model.put("errCode", msg.getWebrspcode());
			return false;
		}

		// 验证商户产品版本中是否有对应的业务
		ResultBean busiResultBean = prodCaseService.verifyMerchBusiness(order);
		if (!busiResultBean.isResultBool()) {
			PojoRspmsg msg = rspmsgDAO.get(busiResultBean.getErrCode());
			model.put("errMsg", msg.getRspinfo());
			model.put("errCode", msg.getWebrspcode());
			return false;
		}

		// 检验一级商户和二级商户有效性
		if (StringUtil.isNotEmpty(order.getMerId())) {
			PojoMerchDeta subMember = merchService.getMerchBymemberId(order
					.getMerId());
			if (subMember == null) {
				PojoRspmsg msg = rspmsgDAO.get("GW05");
				model.put("errMsg", msg.getRspinfo());
				model.put("errCode", msg.getWebrspcode());
				return false;
			}
			if (order.getMerId().startsWith("2")) {// 对于商户会员需要进行检查
				if (!order.getCoopInstiId().equals(subMember.getParent())) {
					PojoRspmsg msg = rspmsgDAO.get("GW07");
					model.put("errMsg", msg.getRspinfo());
					model.put("errCode", msg.getWebrspcode());
					return false;
				}
			}

		}

		/*try {
			gateWayService.checkBusiAcct(order.getMerId(),null);
		} catch (TradeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			PojoRspmsg msg = rspmsgDAO.get(e.getCode());
			model.put("errMsg", msg.getRspinfo());
			model.put("errCode", msg.getWebrspcode());
		}*/
		return true;
	}
	
	
	@RequestMapping("/toFastPay.htm")
    public ModelAndView toFastPay(TradeBean trade, @RequestParam("txnseqno_")String txnseqno_) {
        if (StringUtil.isEmpty(trade.getTxnseqno())) {
            return new ModelAndView("redirect:/merch/cash.htm?txnseqno="+ txnseqno_);
        }
        Map<String, Object> model = new HashMap<String, Object>();
        trade.setBindCardId("");
        model.put("trade", trade);
        return new ModelAndView("/fastpay/fastCardInfo_merch", model);
    }
	
 	@RequestMapping("/toPay.htm")
    public ModelAndView toPay(TradeBean trade, @RequestParam("txnseqno_")String txnseqno_) {
        if (StringUtil.isEmpty(trade.getTxnseqno())) {
            return new ModelAndView("redirect:/merch/cash.htm?txnseqno=" + txnseqno_);
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
        model.put("trade", trade);
        return new ModelAndView("/fastpay/pay_merch", model);
    }
 	@RequestMapping("/bindPay.htm")
	public ModelAndView toBindPay(TradeBean trade, @RequestParam("txnseqno_")String txnseqno_) {
	    if (StringUtil.isEmpty(trade.getTxnseqno())) {
	        return new ModelAndView("redirect:/merch/cash.htm?txnseqno="
	                + txnseqno_);
	    }
	    
	    Map<String, Object> model = new HashMap<String, Object>();
	    TradeBean tradeBean2 = trade.clone();
	    try {
	        webGateWayService.bindPay(trade);
	    } catch (TradeException e) {
	        // TODO Auto-generated catch block
	        model.put("errMsg", e.getMessage());
	        model.put("txnseqno", txnseqno_);
	        return new ModelAndView("/erro", model);
	    }
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
	    return new ModelAndView("/fastpay/pay_merch", model);
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
                trade.setPayinstiId(channel.getChnlcode());
                webGateWayService.submitPay(trade);
                model.put("trade", trade);
                switch (channel) {
                    case ZLPAY :
                        //zlpayChannel(trade, routId, model);
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
            //验证提现密码
            if(!gateWayService.validatePayPWD(orderinfo.getSecmemberno(), tradeBean.getPay_pwd(), MemberType.ENTERPRISE)){
            	model.put("errMsg", "支付密码错误");
                model.put("respCode", "ZL34");
                model.put("txnseqno", tradeBean.getTxnseqno());
                return new ModelAndView("/erro_merch_withdraw", model);
            }
            TxnsWithdrawModel withdraw = new TxnsWithdrawModel(tradeBean);
            //记录提现账务
            TradeInfo tradeInfo = new TradeInfo();
            tradeInfo.setPayMemberId(withdraw.getMemberid());
            tradeInfo.setPayToMemberId(withdraw.getMemberid());
            tradeInfo.setAmount(new BigDecimal(withdraw.getAmount()));
            tradeInfo.setCharge(new BigDecimal(0));
            tradeInfo.setTxnseqno(orderinfo.getRelatetradetxn());
            //记录分录流水
            accEntryService.accEntryProcess(tradeInfo);
            if (StringUtil.isNotEmpty(tradeBean.getBindCardId())) {
                /*QuickpayCustModel card = memberBankCardService.
                        .getCardByBindId(tradeBean.getBindCardId());
                withdraw.setAcctname(card.getAccname());
                withdraw.setAcctno(card.getCardno());*/
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
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
