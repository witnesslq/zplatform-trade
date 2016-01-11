/* 
 * RouteConfigServiceImpl.java  
 * 
 * version TODO
 *
 * 2015年9月2日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.trade.service.impl;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.commons.utils.StringUtil;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.dao.IRouteConfigDAO;
import com.zlebank.zplatform.trade.model.MemberBaseModel;
import com.zlebank.zplatform.trade.model.RouteConfigModel;
import com.zlebank.zplatform.trade.service.IMemberService;
import com.zlebank.zplatform.trade.service.IRouteConfigService;
import com.zlebank.zplatform.trade.service.base.BaseServiceImpl;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2015年9月2日 下午12:11:12
 * @since 
 */
@Service("routeConfigService")
public class RouteConfigServiceImpl extends BaseServiceImpl<RouteConfigModel, Long> implements IRouteConfigService{
    private static final Log log = LogFactory.getLog(RouteConfigServiceImpl.class);
    @Autowired
    private IRouteConfigDAO routeConfigDAO;
    @Autowired
    private IMemberService memberService;
    /**
     *
     * @return
     */
    @Override
    public Session getSession() {
        return routeConfigDAO.getSession();
    }
    
    
    
    public ResultBean getTransRout(String transTime,String transAmt,String memberId,String busiCode,String cardNo,String cashCode){
        try {
            MemberBaseModel merchant =  memberService.get(memberId);
            String merchRoutver= merchant.getRoutver();
            if (log.isDebugEnabled()) {
                log.debug("transTime：" + transTime);
                log.debug("transAmt：" + transAmt);
                log.debug("memberId：" + memberId);
                log.debug("busiCode：" + busiCode);
                log.debug("cardNo：" + cardNo);
                log.debug("cashCode：" + cashCode);
                log.debug("merchRoutver：" + merchRoutver);
            }
            String bankcode = null;
            String cardType = null;
            if(StringUtils.isNotEmpty(cardNo)){
                Map<String, Object> cardInfoMap = getCardInfo(cardNo);
                if(cardInfoMap==null){
                    return new ResultBean("RC23", "银行卡验证失败 ");
                }
                bankcode = cardInfoMap.get("BANKCODE").toString();
                cardType = cardInfoMap.get("TYPE").toString();
            }
            
            
            
            StringBuffer sqlBuffer = new StringBuffer();
            List<Object> paramList = new ArrayList<Object>();
            sqlBuffer = new StringBuffer();
            sqlBuffer.append("SELECT rt ");
            sqlBuffer.append("FROM (SELECT t.rid AS rid, t.ROUTVER AS rt ");
            sqlBuffer.append("FROM t_route_config t ");
            sqlBuffer.append("WHERE 1=1 ");
            if(StringUtil.isNotEmpty(cashCode)){
                sqlBuffer.append("AND t.CASHCODE = ? ");
                paramList.add(cashCode);
            }
            if(StringUtil.isNotEmpty(merchRoutver)){
                sqlBuffer.append("AND t.merchroutver = ? ");
                paramList.add(merchRoutver);
            }
            sqlBuffer.append("AND t.status = '00' ");
            sqlBuffer.append("AND t.stime <= ? ");
            paramList.add(transTime);
            sqlBuffer.append("AND t.etime > ? ");
            paramList.add(transTime);
            sqlBuffer.append("AND t.minamt <= ? ");
            paramList.add(transAmt);
            sqlBuffer.append("AND t.maxamt > ? ");
            paramList.add(transAmt);
            
            sqlBuffer.append("AND t.isdef = '1' ");
            if (StringUtils.isNotEmpty(bankcode)) {
                sqlBuffer.append("AND t.bankcode like ? ");
                paramList.add("%"+bankcode + ";%");
            }
            if (StringUtils.isNotEmpty(busiCode)) {
                sqlBuffer.append("AND t.busicode like ? ");
                
                paramList.add("%"+busiCode + ";%");
            }
            if (StringUtils.isNotEmpty(cardType)) {
                sqlBuffer.append("AND t.cardtype like ? ");
                paramList.add("%"+cardType + ";%");
            }
            sqlBuffer
                    .append(" ORDER BY t.ordertype, t.orders) t1 WHERE ROWNUM = 1");
            log.info("member rout sql:" + sqlBuffer);
            List<Map<String, Object>> memberRoutList = (List<Map<String, Object>>) super
                    .queryBySQL(sqlBuffer.toString(), paramList.toArray());
            if (memberRoutList.size() > 0) {
                log.info("member rout:" + memberRoutList.get(0));
                return new ResultBean(memberRoutList.get(0).get("RT"));
            } else {//查找当前路由版本下的默认路由
                sqlBuffer = new StringBuffer();
                sqlBuffer.append("SELECT t.ROUTVER as rout ");
                sqlBuffer.append("FROM t_route_config t ");
                sqlBuffer.append("WHERE t.merchroutver = ? ");
                sqlBuffer.append("AND t.status = '00' ");
                sqlBuffer.append("AND t.isdef = '0' ");
                log.info("member default rout sql:" + sqlBuffer);
                List<Map<String, Object>> defaultMemberRoutList = (List<Map<String, Object>>) super
                        .queryBySQL(sqlBuffer.toString(), new Object[]{merchRoutver});
                if (defaultMemberRoutList.size() > 0) {
                    log.info("member default rout:"
                            + defaultMemberRoutList.get(0));
                    return new ResultBean(defaultMemberRoutList.get(0).get("ROUT"));
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
           log.error(e);
        }
        log.info("member "+memberId+" no find member rout!!!");
        return new ResultBean("RC99", "交易路由异常");
    }
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public Map<String, Object> getCardInfo(String cardNo){
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
    public ResultBean getWapTransRout(String transTime,String transAmt,String memberId,String busiCode,String cardNo){
        try {
            MemberBaseModel merchant =  memberService.get(memberId);
            String merchRoutver= merchant.getRoutver();
            if (log.isDebugEnabled()) {
                log.debug("transTime：" + transTime);
                log.debug("transAmt：" + transAmt);
                log.debug("memberId：" + memberId);
                log.debug("busiCode：" + busiCode);
                log.debug("cardNo：" + cardNo);
                log.debug("merchRoutver：" + merchRoutver);
            }
            String bankcode = null;
            String cardType = null;
            if(StringUtils.isNotEmpty(cardNo)){
                Map<String, Object> cardInfoMap = getCardInfo(cardNo);
                if(cardInfoMap==null){
                    return new ResultBean("RC23", "银行卡验证失败");
                }
                bankcode = cardInfoMap.get("BANKCODE").toString();
                cardType = cardInfoMap.get("TYPE").toString();
            }
            
            
            StringBuffer sqlBuffer = new StringBuffer();
            List<Object> paramList = new ArrayList<Object>();
            sqlBuffer = new StringBuffer();
            sqlBuffer.append("SELECT rt ");
            sqlBuffer.append("FROM (SELECT t.rid AS rid, t.ROUTVER AS rt ");
            sqlBuffer.append("FROM t_route_config t ");
            sqlBuffer.append("WHERE 1=1 ");
            if(StringUtil.isNotEmpty(merchRoutver)){
                sqlBuffer.append("AND t.merchroutver = ? ");
                paramList.add(merchRoutver);
            }
            sqlBuffer.append("AND t.status = '00' ");
            sqlBuffer.append("AND t.stime <= ? ");
            paramList.add(transTime);
            sqlBuffer.append("AND t.etime > ? ");
            paramList.add(transTime);
            sqlBuffer.append("AND t.minamt <= ? ");
            paramList.add(transAmt);
            sqlBuffer.append("AND t.maxamt > ? ");
            paramList.add(transAmt);
            sqlBuffer.append("AND t.isdef = '1' ");
            if (StringUtils.isNotEmpty(bankcode)) {
                sqlBuffer.append("AND t.bankcode like ? ");
                paramList.add("%"+bankcode + ";%");
            }
            if (StringUtils.isNotEmpty(busiCode)) {
                sqlBuffer.append("AND t.busicode like ? ");
                
                paramList.add("%"+busiCode + ";%");
            }
            if (StringUtils.isNotEmpty(cardType)) {
                sqlBuffer.append("AND t.cardtype like ? ");
                paramList.add("%"+cardType + ";%");
            }
            sqlBuffer
                    .append(" ORDER BY t.ordertype, t.orders) t1 WHERE ROWNUM = 1");
            log.info("member rout sql:" + sqlBuffer);
            List<Map<String, Object>> memberRoutList = (List<Map<String, Object>>) super
                    .queryBySQL(sqlBuffer.toString(), paramList.toArray());
            if (memberRoutList.size() > 0) {
                log.info("member rout:" + memberRoutList.get(0));
                return new ResultBean(memberRoutList.get(0).get("RT"));
            } else {//查找当前路由版本下的默认路由
                sqlBuffer = new StringBuffer();
                sqlBuffer.append("SELECT t.ROUTVER as rout ");
                sqlBuffer.append("FROM t_route_config t ");
                sqlBuffer.append("WHERE 1=1 ");
                sqlBuffer.append("WHERE t.merchroutver = ? ");
                sqlBuffer.append("AND t.status = '00' ");
                sqlBuffer.append("AND t.isdef = '0' ");
                log.info("member default rout sql:" + sqlBuffer);
                List<Map<String, Object>> defaultMemberRoutList = (List<Map<String, Object>>) super
                        .queryBySQL(sqlBuffer.toString(), new Object[]{merchRoutver});
                if (defaultMemberRoutList.size() > 0) {
                    log.info("member default rout:"
                            + defaultMemberRoutList.get(0));
                    return new ResultBean(defaultMemberRoutList.get(0).get("ROUT"));
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
           log.error(e);
        }
        log.info("member "+memberId+" no find member rout!!!");
        return new ResultBean("RC99", "交易路由异常");
    }

}
