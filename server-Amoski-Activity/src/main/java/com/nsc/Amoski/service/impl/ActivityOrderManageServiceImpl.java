package com.nsc.Amoski.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.nsc.Amoski.AlipayPay.AlipayPayUtils;
import com.nsc.Amoski.WXPay.WXPayUtils;
import com.nsc.Amoski.dao.ActivityOrderManageDao;
import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.service.ActivityOrderManageService;
import com.nsc.Amoski.service.UserCenterManageService;
import com.nsc.Amoski.util.DateUtils;
import com.nsc.Amoski.util.RegUtil;
import com.nsc.Amoski.util.UniqId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
@Service
public class ActivityOrderManageServiceImpl implements ActivityOrderManageService {

    @Autowired
    ActivityOrderManageDao activityOrderManageDao;

    @Autowired
    UserCenterManageService userCenterManageService;

    Logger log= LoggerFactory.getLogger(ActivityOrderManageServiceImpl.class);

    /**
     * 查询所有活动列表
     * @param param 查询条件 查询所有活动列表
     * @return  查询所有活动列表
     */
    public PagingBean queryActivityList(ActivityBasicDto param){
        return activityOrderManageDao.queryActivityList(param);
    }

    /**
     * 查询用户是否已报名活动 (是否存在已支付订单)
     * @return  查询用户是否已报名活动
     */
    public ActivityOrderDto queryUserExistOrder(ActivityOrderDto info){
        return activityOrderManageDao.queryUserExistOrder(info);
    }

    /**
     * 创建订单
     * @return  创建订单
     */
    public TbActivityOrderEntity createOrder(TbActivityOrderEntity order,String arrStr){
        JSONArray arrObj= JSON.parseArray(arrStr);
        RegUtil regUtil=RegUtil.getSingleton();
        log.info(">>>>>>>>>>>arrStr:"+arrStr);
        Map<String,Object> map=new HashMap<>();
        map.put("basicsId",order.getUpdateName());
        String whereSql=" BASICS_ID=:basicsId ";
        TbActivityOrdinationEntity ordinationEntity=null;
        List<TbActivityOrdinationEntity> ordinationList = userCenterManageService.queryListEntity(new TbActivityOrdinationEntity(), whereSql, map);
        if(ordinationList!=null&&ordinationList.size()>0){
            ordinationEntity=ordinationList.get(0);
        }
        int day = (int)Math.ceil(((double) ordinationEntity.getActivityEnd().getTime() - (double) ordinationEntity.getActivityStart().getTime()) / 1000 / 3600 / 24)+1;
        log.info(">>>>>>>>>>>>>>>>>activityDay:"+day);
        order.setUpdateName("");
        userCenterManageService.addEntity(order,true);
        List<Object> addList = new ArrayList<Object>();
        List<Object> addList1 = new ArrayList<Object>();
        //List<Object> updList = new ArrayList<Object>();
        BigDecimal totalAmount=new BigDecimal(0);
        //票种Map
        Map<String,TbCtivityInvoiceEntity> inMap=new HashMap<>();
        for (int i=0;i<arrObj.size();i++) {
            JSONObject jsonObj = arrObj.getJSONObject(i);
            TbActivityOrderDetailsEntity orderDetail=new TbActivityOrderDetailsEntity();
            String invoiceId = jsonObj.getString("invoiceId");
            //查询票种信息  如果需减少连接数据次数 可一次查出活动所有票种放入map
            TbCtivityInvoiceEntity invoice = null;
            if(inMap.get(invoiceId)==null){
                invoice = userCenterManageService.queryEntity(new TbCtivityInvoiceEntity(), invoiceId);
            }else{
                invoice=inMap.get(invoiceId);
            }
            if(invoice==null){//票种数据错误
                continue;
            }
            //加入集合批量更新库存
            if(invoice.getNumberCount()<=0){
                continue;
            }
            invoice.setNumberCount(invoice.getNumberCount()-1);
            inMap.put(invoice.getId()+"",invoice);
            //updList.add(invoice);
            //报list名人员信息
            TbActivitySignUpEntity activitySignUp=new TbActivitySignUpEntity();
            activitySignUp.setBasicsId(order.getBasicsId());
            activitySignUp.setInvoiceId(invoice.getId()+"");
            activitySignUp.setInvoiceName(invoice.getNameInvoice());
            activitySignUp.setCreateData(DateUtils.getCurrentStamp());
            activitySignUp.setOrderCode(order.getCode());
            activitySignUp.setVerificationCode(UniqId.getRandomPwd(2)+
                    (order.getBasicsId().length()>=2?order.getBasicsId().substring(order.getBasicsId().length()-2):"0"+order.getBasicsId())+
                    (order.getMember_id().toString().length()>=2?order.getMember_id().toString().substring(order.getMember_id().toString().length()-2):"0"+order.getMember_id())+
                    UniqId.getRandomPwd(2));
            RegUtil.JSONObjToDto(jsonObj,activitySignUp);
            if(regUtil.isNull(order.getContactName())){
                order.setContactName(activitySignUp.getName());
                order.setContactTel(activitySignUp.getTel());
            }
            ;
            if(activitySignUp.getSex()!=null && activitySignUp.getSex().length()>1){
                activitySignUp.setSex("");
            }
            userCenterManageService.addEntity(activitySignUp,true);//添加报名人员

            totalAmount=totalAmount.add(invoice.getTicketPrice());
            orderDetail.setCode(order.getCode());
            orderDetail.setOrderId(order.getId()+"");
            orderDetail.setCreateDate(DateUtils.getCurrentStamp());
            String strn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderDetail.getCreateDate());
            log.info("创建订单时间==》"+strn);
            log.info("System.currentTimeMillis()==>"+System.currentTimeMillis());
            orderDetail.setInvoiceId(invoice.getId()+"");
            orderDetail.setInvoiceName(invoice.getNameInvoice());
            //orderDetail.setReceivingCode(activitySignUp.getVerificationCode());
            orderDetail.setSignUpId(activitySignUp.getId()+"");
            orderDetail.setCreateName(order.getCreateName());
            orderDetail.setPrimitiveMoney(invoice.getTicketPrice());
            orderDetail.setState(order.getState());
            addList.add(orderDetail);

            //住宿空数据
            for(int j=1;j<=day;j++){
                TbPeoplePutUpEntity peoplePutUp=new TbPeoplePutUpEntity();
                peoplePutUp.setDaysOfTravel(""+j);
                peoplePutUp.setPeopleId(activitySignUp.getId()+"");
                addList1.add(peoplePutUp);
            }
        }
        order.setSumCount(addList.size());
        order.setPrimitiveMoneySum(totalAmount);
        userCenterManageService.updateEntity(order);
        userCenterManageService.bachInsert(addList);
        userCenterManageService.bachInsert(addList1);
        //userCenterManageService.bachUpdate(updList);
        return order;
    }
    public static void main(String[] args){
        Timestamp date=new Timestamp(1574927743959L);
        String strn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        System.out.println(strn);
    }

    /**
     * 支付订单
     * @return  支付订单
     */
    public Result payOrder(TbActivityOrderEntity order, HttpServletRequest request){
        Result rs=new Result();
        Object o = null;
        //支付流水
        TbOrderPayment tb_order_payment=new TbOrderPayment();
        tb_order_payment.setAmount(order.getPrimitiveMoneySum());
        tb_order_payment.setOpenDate(DateUtils.getCurrentStamp());
        tb_order_payment.setPayType(order.getPayType());
        tb_order_payment.setRemark("活动报名支付");
        tb_order_payment.setReturnStatus("");
        tb_order_payment.setSerialNumber(order.getCode());
        tb_order_payment.setStatus(2);
        tb_order_payment.setMemberId(order.getMember_id());
        userCenterManageService.addEntity(tb_order_payment,true);
        //调用支付接口
        AlipayPayUtils AlipayPayUtils = new AlipayPayUtils();
        String hour="1h";
        try {
            if("2".equals(order.getPayType())){//支付宝支付
                o = AlipayPayUtils.AlipayPay1(tb_order_payment.getRemark(), tb_order_payment.getRemark(), order.getCode(),
                        hour,
                        order.getPrimitiveMoneySum().toString(),null);
            }else{//微信支付
                o= WXPayUtils.getWxpayWapPayStr(request,
                        order.getCode(),
                        tb_order_payment.getRemark(),
                        order.getPrimitiveMoneySum().toString(),
                        "");
                log.info("\n微信支付parent=>order.getCode():"+order.getCode()+
                        ",tb_order_payment.getRemark()："+tb_order_payment.getRemark()+
                        ",order.getPrimitiveMoneySum().toString()"+order.getPrimitiveMoneySum().toString()+
                        "\n\n微信支付返回"+o);
            }
            //dealPayBusiness(order,tb_order_payment);
        } catch (Exception e) {
            log.info("?>>>>>>>>>>>>>>>>>catch"+e.getMessage());
            e.printStackTrace();
        }
        rs.setMsg(ResultMsg.SUCCESS.getMessage());
        rs.setCode(ResultMsg.SUCCESS.getCode());
        rs.setData(o);
        log.info(">>>>>>payOrder data:"+rs);
        return rs;
    }

    /**
     * 支付完成后业务处理
     */
    public void dealPayBusiness(TbActivityOrderEntity order,TbOrderPayment payment){
        log.info("进入业务处理");
        //成功后业务操作
        Map<String,Object> map=new HashMap<>();
        map.put("code",order.getCode());
        String whereSql=" code=:code";
        order.setState("3");//已支付
        payment.setStatus(1);
        userCenterManageService.updateEntity(order);
        userCenterManageService.updateEntity(payment);
        //修改详情状态
        ActivityOrderDto dto=new ActivityOrderDto();
        dto.setId(order.getId()+"");
        dto.setState(order.getState());
        activityOrderManageDao.updateOrderState(dto);
        //减少库存
        List<TbActivityOrderDetailsEntity> detailList = userCenterManageService.queryListEntity(new TbActivityOrderDetailsEntity(), whereSql, map);
        List<Object> updList = new ArrayList<Object>();
        //票种Map
        Map<String,TbCtivityInvoiceEntity> inMap=new HashMap<>();
        for(TbActivityOrderDetailsEntity detail:detailList){
            String invoiceId = detail.getInvoiceId();
            //查询票种信息  如果需减少连接数据次数 可一次查出活动所有票种放入map
            TbCtivityInvoiceEntity invoice = null;
            if(inMap.get(invoiceId)==null){
                invoice = userCenterManageService.queryEntity(new TbCtivityInvoiceEntity(), invoiceId);
            }else{
                invoice=inMap.get(invoiceId);
            }
            if(invoice==null){//票种数据错误
                continue;
            }
            //加入集合批量更新库存
            if(invoice.getNumberCount()<=0){//库存不足
                //return ResultMsg.IS_NULL;
            }
            invoice.setNumberCount(invoice.getNumberCount()-1);
            inMap.put(invoice.getId()+"",invoice);
            updList.add(invoice);
        }
        userCenterManageService.bachUpdate(updList);
    }

    /**
     * 查询用户所有活动报名订单信息
     * @return  查询用户所有活动报名订单信息
     */
    public List<ActivityOrderDto1> queryOrder(ActivityOrderDto order){
        //查询更新所有失效订单
        return activityOrderManageDao.queryOrder(order);
    }
    /**
     * 更新失效订单
     * @return  更新失效订单
     */
    public void updateTimeoutOrder(ActivityOrderDto order){
        activityOrderManageDao.updateTimeoutOrder(order,0);
        activityOrderManageDao.updateTimeoutOrder(order,1);
    }

    /**
     * 查询用户订单详情信息
     * @return  查询用户订单详情信息
     */
    public List<ActivityOrderDto> queryOrderDetail(ActivityOrderDto order){
        return activityOrderManageDao.queryOrderDetail(order,0);
    }

    /**
     * 查询用户退票详情信息
     * @return  查询用户退票详情信息
     */
    public List<ActivityOrderDto> queryOrderRefundDetail(ActivityOrderDto order){
        return activityOrderManageDao.queryOrderDetail(order,1);
    }

    /**
     * 用户退票
     * @return  用户退票
     */
    public Result updateOrderState(TbActivityOrderDetailsEntity detailEntity,MemberView userInfo){
        Result rs=new Result();
        detailEntity.setState("4");
        TbActivitySignUpEntity tbSignUpEntity = userCenterManageService.queryEntity(new TbActivitySignUpEntity(), detailEntity.getSignUpId());
        detailEntity.setRefundTicketDate(DateUtils.getCurrentStamp());//申请退票时间
        TbRefundAuditEntity refundAuditEntity=new TbRefundAuditEntity();
        refundAuditEntity.setMemberId(userInfo.getMemberId()+"");
        refundAuditEntity.setAssociationType("1");
        refundAuditEntity.setOrderDetailsId(detailEntity.getId()+"");
        refundAuditEntity.setRefundMoney(detailEntity.getPrimitiveMoney().toString());
        refundAuditEntity.setCreateData(DateUtils.getCurrentStamp());
        refundAuditEntity.setCreateName(tbSignUpEntity.getName());
        refundAuditEntity.setState("1");
        userCenterManageService.addEntity(refundAuditEntity);

        userCenterManageService.updateEntity(detailEntity);
        boolean bl=true;
        Map<String,Object> map=new HashMap<>();
        map.put("code",detailEntity.getCode());
        String whereSql=" code=:code ";
        List<TbActivityOrderDetailsEntity> detailList = userCenterManageService.queryListEntity(new TbActivityOrderDetailsEntity(),whereSql,map);
        for(TbActivityOrderDetailsEntity entity:detailList){
            if(entity.getId().intValue()!=detailEntity.getId().intValue()&&!entity.getState().equals("4")&&!entity.getState().equals("5")){
                bl=false;
            }
        }
        if(bl){//如果全部退款中 改变总订单状态
            map.clear();
            map.put("code",detailEntity.getCode());
            map.put("memberId",userInfo.getMemberId());
            whereSql=" code=:code and member_id=:memberId ";
            List<TbActivityOrderEntity> orderList = userCenterManageService.queryListEntity(new TbActivityOrderEntity(),whereSql,map);
            TbActivityOrderEntity order=orderList.get(0);
            order.setState("4");
            userCenterManageService.updateEntity(order);
        }
        rs.setCode(ResultMsg.SUCCESS.getCode());
        rs.setMsg(ResultMsg.SUCCESS.getMessage());
        rs.setData(bl);
        return rs;
    }

    /**
     * 查询电子票列表
     * @return  查询电子票列表
     */
    public List<ActivityOrderDto1> queryElectronicTicket(ActivityOrderDto order){
        return activityOrderManageDao.queryElectronicTicket(order);
    }
    /**
     * 查询电子详情
     * @return  查询电子详情
     */
    public List<ElectronicTicketDto> queryElectronicTicketDetail(ElectronicTicketDto order){
        return activityOrderManageDao.queryElectronicTicketDetail(order);
    }

    /**
     * 查询活动报名订单提醒
     * @return  查询活动报名订单提醒
     */
    public List<ActivityOrderDto> queryActivityOrderWarning(){
        return activityOrderManageDao.queryActivityOrderWarning();
    }
}







































