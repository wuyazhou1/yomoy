package com.nsc.AmoskiActivity.Service.impl;

import com.alipay.api.AlipayApiException;
import com.nsc.Amoski.AlipayPay.AlipayPayUtils;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TbActivityOrderDetailsEntity;
import com.nsc.Amoski.util.StringUtils;
import com.nsc.AmoskiActivity.Dao.ActivityOrderDao;
import com.nsc.AmoskiActivity.Service.ActivityOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("ActivityOrderServiceImpl")
@Transactional(transactionManager = "ActivityTransactionManager")
public class ActivityOrderServiceImpl implements ActivityOrderService {

    @Autowired
    @Qualifier("ActivityOrderDaoImpl")
    private ActivityOrderDao activityOrderDao;


    @Override
    public PagingBean activityOrderlist(Map<String, Object> params) {
        List list = activityOrderDao.activityOrderlist(params);
        int count = activityOrderDao.activityOrderCount(params);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        if (StringUtils.isEmpty(params.get("draw")))
            pagelist.setDraw(0);
        else
            pagelist.setDraw(Integer.valueOf(params.get("draw").toString()));
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }


    @Override
    public PagingBean activityOrderDatail(Map<String, Object> params) {
        //查询订单列表
        List list = activityOrderDao.activityOrderDatailList(params);
        int count = activityOrderDao.activityOrderDatailCount(params);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        if (StringUtils.isEmpty(params.get("draw")))
            pagelist.setDraw(0);
        else
            pagelist.setDraw(Integer.valueOf(params.get("draw").toString()));
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        //查询订单详情
        Map<String,Object> orderDatail = activityOrderDao.queryOrderDatail(params);
        //查询活动信息
        Map<String,Object> activityDatail = activityOrderDao.queryActivityDatail(params);
        //查询订单历史明细
        List<Map<String,Object>> orderHistoryDatailList = activityOrderDao.queryOrderHistoryDatail(params);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("orderDatail",orderDatail);
        resultMap.put("activityDatail",activityDatail);
        resultMap.put("orderHistoryDatailList",orderHistoryDatailList);
        pagelist.setResultMap(resultMap);
        return pagelist;
    }

    @Override
    public void refundApplication(Map<String, Object> parentMap) {
        //查询订单详情金额
        TbActivityOrderDetailsEntity tbActivityOrderDetailsEntity = activityOrderDao.queryOrderDatailByid(parentMap);
        //判断订单详情金额和==》订单金额是否一致
        if(!tbActivityOrderDetailsEntity.getPrimitiveMoney().toString().equals(parentMap.get("PRIMITIVE_MONEY"))){
            throw new RuntimeException("订单金额错误");
        }
        //判断订单金额和==》退款金额服务费一致
        BigDecimal refundMoney = new BigDecimal(parentMap.get("REFUND_MONEY").toString());
        BigDecimal serviceMoney = new BigDecimal(parentMap.get("SERVICE_MONEY").toString());
        if(tbActivityOrderDetailsEntity.getPrimitiveMoney().compareTo(refundMoney.add(serviceMoney))!=0){
            throw new RuntimeException("退款金额不正确");
        }
        //修改订单状态
        activityOrderDao.updateOrderStateById(parentMap);
        //修改退款申请表退款状态，退款金额，服务费用，修改人，修改时间
        activityOrderDao.updateRefundMoneyById(parentMap);
        AlipayPayUtils alipayPayUtils = new AlipayPayUtils();
        try {
            //alipayPayUtils.AlipayRefund1(tbActivityOrderDetailsEntity.getCode(), parentMap.get("REFUND_MONEY").toString());
            alipayPayUtils.AlipayRefund1("8298aafe99b34fad831ace87b79fe6f0", "0.01");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //修改订单状态
        activityOrderDao.updateOrderByDatailsId(parentMap);
    }

    @Override
    public void refundApplicationFalse(Map<String, Object> parentMap) {
        //修改退款流程表
        activityOrderDao.updateRefundMoneyFalseById(parentMap);
        //修改活动订单详情表
        activityOrderDao.updateOrderDatailsFalseById(parentMap);
    }

}
