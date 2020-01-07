package com.nsc.Amoski.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.nsc.Amoski.AlipayPay.AlipayPayUtils;
import com.nsc.Amoski.WXPay.WXPay;
import com.nsc.Amoski.WXPay.WXPayConfigImpl;
import com.nsc.Amoski.WXPay.WXPayUtil;
import com.nsc.Amoski.WXPay.WXPayUtils;
import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.util.DateUtils;
import com.nsc.Amoski.util.UniqId;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.*;

@RestController
@RequestMapping("/activityOrderManage")
public class ActivityOrderManageController extends ActivityServerBaseController<ActivityOrderManageController> {

    /**
     * 查询用户是否已报名活动 (是否存在已支付订单)
     * @return  查询活动列表信息
     */

    @RequestMapping(value="/queryUserExistOrder",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询用户是否已报名活动", notes = "查询用户是否已报名活动", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="basicsId",value="活动id",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="identityNumber",value="报名人员身份证",dataType="String", paramType = "query"),
    })
    public Result queryUserExistOrder(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityOrderDto dto) throws Exception{
        log.info(">>>>>>>>>>>>> queryUserExistOrder .requestParam param!!!  dto:"+dto);
        if(regUtil.isNull(dto.getIdentityNumber(),dto.getBasicsId())){
            return error(ResultMsg.IS_NULL);
        }
        ActivityOrderDto activityOrderDto = activityOrderManageService.queryUserExistOrder(dto);
        if(activityOrderDto!=null){//存在 不能再报名
            return error(ResultMsg.ACTIVITY_ORDEREXIST_ERROR);
        }
        return success();
    }

    /**
     * 生成订单
     * @return  生成订单
     */

    @RequestMapping(value="/createOrder",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="生成订单", notes = "生成订单", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="basicsId",value="活动id",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="identityNumber",value="报名人员身份证",dataType="String", paramType = "query"),
    })
    public Result createOrder(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityOrderDto dto) throws Exception{
        log.info(">>>>>>>>>>>>> createOrder .requestParam param!!!  dto:"+dto);
        if(regUtil.isNull(dto.getCreateOrderJsonStr())){
            return error(ResultMsg.IS_NULL);
        }
        JSONObject jsObj= JSON.parseObject(dto.getCreateOrderJsonStr());
        String jsonArrStr=jsObj.getString("arrStr");
        if(regUtil.isNull(jsonArrStr)){
            return error(ResultMsg.IS_NULL);
        }
        /*Map<String,Object> map=new HashMap<>();
        String whereSql=" code=:basicsId";
        map.put("basicsId",jsObj.getString("basicsId"));
        List<TbActivityBasicsEntity> activity = userCenterManageService.queryListEntity(new TbActivityBasicsEntity(), whereSql, map);*/
        ActivityBasicDto info=new ActivityBasicDto();
        info.setId(jsObj.getIntValue("basicsId"));
        ActivityBasicDto activityBasicDto = guideRouteManageService.queryActivityDetailInfo(info,false);
        if(activityBasicDto==null){
            return error(ResultMsg.ACTIVITY_NOTEXIST_ERROR);
        }
        if(DateUtils.getCurrentStamp().getTime()-activityBasicDto.getActivityEnd().getTime()>0){//报名已截止
            return error(ResultMsg.ACTIVITY_FAILURE_ERROR);
        }
        MemberView userInfo = getRedisUserInfo(request, false);
        String orderCode="11"+DateUtils.string2TimeYs(new Date(),null).substring(4)+(DateUtils.getCurrentStamp().getTime()+"").substring((DateUtils.getCurrentStamp().getTime()+"").length()-8)+
                (userInfo.getMemberId().toString().length()>=4?userInfo.getMemberId().toString().substring(userInfo.getMemberId().toString().length()-4):(userInfo.getMemberId()>100?"0"+userInfo.getMemberId():(userInfo.getMemberId()>10?("00"+userInfo.getMemberId()):("000"+userInfo.getMemberId()))));
        TbActivityOrderEntity order=new TbActivityOrderEntity();
        order.setCode(orderCode);
        order.setCreateData(DateUtils.getCurrentStamp());
        order.setCreateName(userInfo.getLoginname());
        //order.setPrimitiveMoneySum(jsObj.getBigDecimal("primitiveMoneySum"));
        order.setBasicsId(jsObj.getString("basicsId"));
        order.setMember_id(userInfo.getMemberId());
        order.setUpdateName(activityBasicDto.getId()+"");
        order.setState("2");
        TbActivityOrderEntity orderEntity = activityOrderManageService.createOrder(order,jsonArrStr);
        return success(orderEntity);
    }

    /**
     * 订单支付
     * @return  订单支付
     */

    @RequestMapping(value="/payOrder",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="订单支付", notes = "生成订单", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="code",value="活动id",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="payType",value="支付方式(1.微信 2.支付宝)",dataType="String", paramType = "query"),
    })
    public Result payOrder(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityOrderDto dto) throws Exception{
        log.info(">>>>>>>>>>>>> payOrder .requestParam param!!!  dto:"+dto);
        if(regUtil.isNull(dto.getCode(),dto.getPayType())||Integer.parseInt(dto.getPayType())<=0||Integer.parseInt(dto.getPayType())>2){
            return error(ResultMsg.IS_NULL);
        }
        Map<String,Object> map=new HashMap<>();
        map.put("code",dto.getCode());
        String whereSql=" code=:code";
        List<TbActivityOrderEntity> list=userCenterManageService.queryListEntity(new TbActivityOrderEntity(),whereSql,map);
        if(list==null||list.size()==0){
            return error(ResultMsg.ORDER_NOTEXIST_ERROR);
        }

        TbActivityOrderEntity order=list.get(0);
        //判断活动是否存在 活动是否已报名截止
        ActivityBasicDto info=new ActivityBasicDto();
        info.setId(Integer.parseInt(order.getBasicsId()));
        ActivityBasicDto activityBasicDto = guideRouteManageService.queryActivityDetailInfo(info,false);
        if(activityBasicDto==null){
            return error(ResultMsg.ACTIVITY_NOTEXIST_ERROR);
        }
        if(DateUtils.getCurrentStamp().getTime()-activityBasicDto.getActivityEnd().getTime()>0){//报名已截止
            return error(ResultMsg.ACTIVITY_FAILURE_ERROR);
        }
        order.setPayType(dto.getPayType());
        order.setIsRemind("0");
        Result rs = activityOrderManageService.payOrder(order,request);
        return rs;
    }


    @RequestMapping(value="/isPayOrderByPay",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="订单支付", notes = "生成订单", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="code",value="活动id",dataType="String", paramType = "query"),
    })
    public Object isPayOrderByPay(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityOrderDto dto) throws Exception{
        log.info("dto.getCode:"+dto.getCode());
        Map<String,Object> map=new HashMap<>();
        map.put("code",dto.getCode());
        String whereSql=" code=:code";
        List<TbActivityOrderEntity> list=userCenterManageService.queryListEntity(new TbActivityOrderEntity(),whereSql,map);
        return list.get(0);
    }


    /**
     * 查询用户订单
     * @return  查询用户订单
     */

    @RequestMapping(value="/queryOrder",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询用户订单", notes = "查询用户订单", httpMethod = "POST" )
    /*@ApiImplicitParams({
            @ApiImplicitParam(name="code",value="订单id",dataType="String", paramType = "query"),
    })*/
    public Result queryOrder(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityOrderDto dto) throws Exception{
        log.info(">>>>>>>>>>>>> queryOrder .requestParam param!!!  dto:"+dto);
        MemberView userInfo=getRedisUserInfo(request,false);
        dto.setMemberId(userInfo.getMemberId());
        //更新失效订单
        activityOrderManageService.updateTimeoutOrder(dto);
        List<ActivityOrderDto1> activityOrderList = activityOrderManageService.queryOrder(dto);
        return success(activityOrderList);
    }

    /**
     * 查询用户订单详情
     * @return  查询用户订单详情
     */

    @RequestMapping(value="/queryOrderDetail",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询用户订单详情", notes = "查询用户订单详情", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="订单id",dataType="String", paramType = "query"),
    })
    public Result queryOrderDetail(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityOrderDto dto) throws Exception{
        log.info(">>>>>>>>>>>>> queryOrderDetail .requestParam param!!!  dto:"+dto);
        if(regUtil.isNull(dto.getId())){
            return error(ResultMsg.IS_NULL);
        }
        MemberView userInfo=getRedisUserInfo(request,false);
        dto.setMemberId(userInfo.getMemberId());
        List<ActivityOrderDto1> activityOrderList = activityOrderManageService.queryOrder(dto);
        if(activityOrderList==null||activityOrderList.size()==0){
            return error(ResultMsg.ORDER_NOTEXIST_ERROR);
        }
        ActivityOrderDto1 activityOrder=activityOrderList.get(0);
        List<ActivityOrderDto> signUplList = activityOrderManageService.queryOrderDetail(dto);
        activityOrder.setList(signUplList);
        return success(activityOrder);
    }

    /**
     * 修改订单信息
     * @return  修改订单信息
     */

    @RequestMapping(value="/updOrderInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="修改订单信息", notes = "修改订单信息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="订单id",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="contactName",value="联系人姓名",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="contactTel",value="联系人手机号",dataType="String", paramType = "query"),
    })
    public Result updOrderInfo(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityOrderDto dto) throws Exception{
        log.info(">>>>>>>>>>>>> updOrderInfo .requestParam param!!!  dto:"+dto);
        if(regUtil.isNull(dto.getContactName(),dto.getContactTel(),dto.getId())){
            return error(ResultMsg.IS_NULL);
        }
        MemberView userInfo=getRedisUserInfo(request,false);
        Map<String,Object> map=new HashMap<>();
        map.put("id",dto.getId());
        map.put("memberId",userInfo.getMemberId());
        String whereSql=" id=:id and member_id=:memberId ";
        List<TbActivityOrderEntity> orderList = userCenterManageService.queryListEntity(new TbActivityOrderEntity(),whereSql,map);
        if(orderList==null||orderList.size()==0){
            return error(ResultMsg.ORDER_NOTEXIST_ERROR);
        }
        TbActivityOrderEntity order=orderList.get(0);
        order.setContactTel(dto.getContactTel());
        order.setContactName(dto.getContactName());
        userCenterManageService.updateEntity(order);
        return success();
    }
    /**
     * 开始提醒
     * @return  开始提醒  orderRemind
     */

    @RequestMapping(value="/orderRemind",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="开始提醒", notes = "开始提醒", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="订单id",dataType="String", paramType = "query"),
    })
    public Result orderRemind(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityOrderDto dto) throws Exception{
        log.info(">>>>>>>>>>>>> orderRemind .requestParam param!!!  dto:"+dto);
        if(regUtil.isNull(dto.getId())){
            return error(ResultMsg.IS_NULL);
        }
        TbActivityOrderEntity orderEntity = userCenterManageService.queryEntity(new TbActivityOrderEntity(), dto.getId());
        orderEntity.setIsRemind("1");
        userCenterManageService.updateEntity(orderEntity);
        return success();
    }


    /**
     * 申请退款
     * @return  申请退款  applicationForRefund
     */

    @RequestMapping(value="/applicationForRefund",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="申请退款", notes = "申请退款", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="订单id",dataType="String", paramType = "query"),
    })
    public Result applicationForRefund(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityOrderDto dto) throws Exception{
        log.info(">>>>>>>>>>>>> applicationForRefund .requestParam param!!!  dto:"+dto);
        if(regUtil.isNull(dto.getId())){
            return error(ResultMsg.IS_NULL);
        }
        MemberView userInfo=getRedisUserInfo(request,false);
        TbActivityOrderDetailsEntity detailEntity = userCenterManageService.queryEntity(new TbActivityOrderDetailsEntity(), dto.getId());
        if(detailEntity==null){
            return error(ResultMsg.ORDER_NOTEXIST_ERROR);
        }
        Result result ;
        if("3".equals(detailEntity.getState())){
            result=activityOrderManageService.updateOrderState(detailEntity, userInfo);
        }else{
            result=error(ResultMsg.ORDER_RETUNRN_ERROR);
        }

        return result;
    }

    /**
     * 查询退款详情
     * @return  查询退款详情
     */

    @RequestMapping(value="/queryRefundMoneyInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询退款详情", notes = "查询退款详情", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="订单详情id",dataType="String", paramType = "query"),
    })
    public Result queryRefundMoneyInfo(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityOrderDto dto) throws Exception{
        log.info(">>>>>>>>>>>>> queryRefundMoneyInfo .requestParam param!!!  dto:"+dto);
        if(regUtil.isNull(dto.getId())){
            return error(ResultMsg.IS_NULL);
        }
        MemberView userInfo=getRedisUserInfo(request,false);
        dto.setMemberId(userInfo.getMemberId());
        List<ActivityOrderDto1> activityOrderList = activityOrderManageService.queryOrder(dto);
        if(activityOrderList==null||activityOrderList.size()==0){
            return error(ResultMsg.ORDER_NOTEXIST_ERROR);
        }
        ActivityOrderDto1 activityOrder=activityOrderList.get(0);
        List<ActivityOrderDto> signUplList = activityOrderManageService.queryOrderRefundDetail(dto);
        activityOrder.setList(signUplList);
        return success(activityOrder);
    }

    /**
     * 查询退款费率
     * @return  查询退款费率
     */
    @RequestMapping(value="/queryRefundFee",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询退款费率", notes = "查询退款费率", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="basicsId",value="活动id",dataType="String", paramType = "query"),
    })
    public Result queryRefundFee(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityOrderDto dto) throws Exception{
        log.info(">>>>>>>>>>>>> queryRefundFee .requestParam param!!!  dto:"+dto);
        if(regUtil.isNull(dto.getBasicsId())){
            return error(ResultMsg.IS_NULL);
        }
        Map<String,Object> map=new HashMap<>();
        map.put("basicsId",dto.getBasicsId());
        String whereSql=" basics_id=:basicsId ";
        List<TbRefundFeeEntity> feeList = userCenterManageService.queryListEntity(new TbRefundFeeEntity(),whereSql,map);
        return success(feeList);
    }
    /**
     * 查询电子票列表
     */
    @RequestMapping(value="/queryElectronicTicket",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询电子票列表", notes = "查询电子票列表", httpMethod = "POST" )
    public Result queryElectronicTicket(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityOrderDto dto) throws Exception{
        log.info(">>>>>>>>>>>>> queryElectronicTicket .requestParam param!!!  dto:"+dto);
        MemberView userInfo = getRedisUserInfo(request, false);
        dto.setMemberId(userInfo.getMemberId());
        List<ActivityOrderDto1> feeList = activityOrderManageService.queryElectronicTicket(dto);
        return success(feeList);
    }

    /**
     * 查询电子票详情
     */
    @RequestMapping(value="/queryElectronicTicketDetail",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询电子票详情", notes = "查询电子票详情", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="订单详情id",dataType="String", paramType = "query"),
    })
    public Result queryElectronicTicketDetail(HttpServletResponse response, HttpServletRequest request, @RequestBody ElectronicTicketDto dto) throws Exception{
        log.info(">>>>>>>>>>>>> queryElectronicTicketDetail .requestParam param!!!  dto:"+dto);
        if(regUtil.isNull(dto.getId())&&regUtil.isNull(dto.getOrderId())){
            return error(ResultMsg.IS_NULL);
        }
        MemberView userInfo = getRedisUserInfo(request, false);
        dto.setMemberId(userInfo.getMemberId()+"");
        List<ElectronicTicketDto> newList =new ArrayList<>();
        Map<String,ElectronicTicketDto> map=new HashMap<String, ElectronicTicketDto>();//用来判断是否存在
        List<ElectronicTicketDto> feeList = activityOrderManageService.queryElectronicTicketDetail(dto);
        //处理数据
        for(ElectronicTicketDto lDto:feeList){
            Map<String,Object> mapData=new HashMap<>();
            mapData.put("startTime",lDto.getStartTime());
            mapData.put("stopTime",lDto.getStopTime());
            mapData.put("introduceType",lDto.getIntroduceType());
            mapData.put("introduce",lDto.getIntroduce());
            mapData.put("vastate",lDto.getVastate());
            if(!map.containsKey(lDto.getId().toString())){
                map.put(lDto.getId().toString(),lDto);
                newList.add(lDto);
            }
            map.get(lDto.getId().toString()).getList().add(mapData);
        }
        return success(newList);
    }


    /**
     * 支付宝支付异步回调
     * @return  支付宝支付异步回调
     */

    @RequestMapping(value="/excludeRequest/aplipayCallBack",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="支付宝支付异步回调", notes = "支付宝支付异步回调", httpMethod = "POST" )
    public void aplipayCallBack(HttpServletResponse response, HttpServletRequest request) throws Exception{
        log.info(">>>>>>>>>>>>> aplipayCallBack .requestParam param!!! ");
        Map<String,String> map=getCallBackParamter(request);
        if(map.size()==0){
            log.info(">>>>>>>>>>param empty!!!!!!");
            return ;
        }
        log.info(">>>>>>>>>>>>>>request param map:"+map);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().print("success");//响应支付宝 防止再次回调
        if("TRADE_SUCCESS".equals(map.get("trade_status"))){
            String amount=map.get("total_amount");
            String orderCode=map.get("out_trade_no");
            String tranNo=map.get("trade_no");
            String whereSql=" serial_number=:orderCode ";
            Map<String,Object> mapParam=new HashMap<>();
            mapParam.put("orderCode",orderCode);
            List<TbOrderPayment> payList = userCenterManageService.queryListEntity(new TbOrderPayment(), whereSql, mapParam);
            if(payList==null||payList.size()==0){
                log.info(">>>>>>>>>>>>>>>>>payment not exist!!!! ");
                return;
            }
            TbOrderPayment payEntity= payList.get(0);
            if(payEntity.getAmount().doubleValue()!=Double.parseDouble(amount)){
                log.info(">>>>>>>>>>>>>>>>>pay order amount different!!!! ");
                return;
            }
            whereSql=" code=:orderCode";
            List<TbActivityOrderEntity> orderList=userCenterManageService.queryListEntity(new TbActivityOrderEntity(),whereSql,mapParam);
            if(orderList==null||orderList.size()==0){
                log.info(">>>>>>>>>>>>>>>>>order not exist!!!! ");
                return;
            }
            TbActivityOrderEntity order=orderList.get(0);
            payEntity.setBankFlow(tranNo);
            payEntity.setReturnStatus(map.get("trade_status"));
            activityOrderManageService.dealPayBusiness(order,payEntity);
        }else{
            log.info(">>>>>>>>>>>>>>pay callBack fail!!  code:"+map.toString());
        }

        /*gmt_create==>2019-09-10 11:24:55
        charset==>UTF-8
        seller_email==>2740255023@qq.com
                subject==>测试订单信息
        sign==>HEkPfcHfPfxAdKNkfGBsifAbNLeWRoFrZhzZpiyFbAmCwpI6XpKz5UhU3whIkcUmbnIN8Y0g+xgNDJTFTdCvbpWiylPBeD5KSfqJeb5SDPyj+IrUCwfZanVbzdy7Kgz
        body==>测试伍思遥支付
        buyer_id==>2088712570101499
        invoice_amount==>0.01
        notify_id==>2019091000222112456001490561416419
        fund_bill_list==>[{"amount":"0.01","fundChannel":"PCREDIT"}]
        notify_type==>trade_status_sync
        trade_status==>TRADE_SUCCESS
        receipt_amount==>0.01
        buyer_pay_amount==>0.01
        app_id==>2019073066097147
        sign_type==>RSA2
        seller_id==>2088531890033206
        gmt_payment==>2019-09-10 11:24:56
        notify_time==>2019-09-10 11:24:57
        version==>1.0
        out_trade_no==>484739adf51b4f6bab98cf4b133abd7c
        total_amount==>0.01
        trade_no==>2019091022001401490552665589
        auth_app_id==>2019073066097147
        buyer_logon_id==>734***@qq.com
                point_amount==>0.00*/




        //activityOrderManageService.dealPayBusiness(order);
    }

    /**
     * 微信支付异步回调
     * @return  微信支付异步回调
     */

    @RequestMapping(value="/excludeRequest/wechatCallBack",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="微信支付异步回调", notes = "微信支付异步回调", httpMethod = "POST" )
    public String wechatCallBack(HttpServletResponse response, HttpServletRequest request) throws Exception{
        Map<String,String> return_data = new HashMap<String,String>();
        try {
            // 获取微信POST过来反馈信息
            String notityXml = "";
            try {
                notityXml = getNotifyXml(request);
            }catch (Exception e) {
                e.printStackTrace();
                return_data.put("return_code", "FAIL");
                return_data.put("return_msg", "获取回调xml异常");
                if (e instanceof ServerException) {
                    return_data.put("return_msg", e.getMessage());
                }
                return  WXPayUtil.mapToXml(return_data);
            }
            WXPayConfigImpl config = WXPayConfigImpl.getInstance();
            WXPay wxpay = new WXPay(config, null, true, true);
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notityXml);  // 转换成map
            System.out.println("转换成map"+notifyMap);
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                try {
                    handle(notifyMap);
                }catch (Exception e) {
                    e.printStackTrace();
                    return_data.put("return_code", "FAIL");
                    return_data.put("return_msg", "处理失败");
                    if (e instanceof ServerException) {
                        return_data.put("return_msg", e.getMessage());
                    }
                    return  WXPayUtil.mapToXml(return_data);
                }
            }else {
                System.out.println("签名错误：" + notityXml);
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                return_data.put("return_code", "FAIL");
                return_data.put("return_msg", "签名错误");
                return  WXPayUtil.mapToXml(return_data);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return_data.put("return_code", "FAIL");
            return_data.put("return_msg", "签名错误");
            return  WXPayUtil.mapToXml(return_data);
        }
        return_data.put("return_code", "SUCCESS");
        return  WXPayUtils.mapToXml(return_data);
    }
    public void handle(Map<String, String> notifyMap) throws Exception {
        // 签名正确
        // 进行处理。
        // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
        if(!notifyMap.get("return_code").toString().equals("SUCCESS")){
            throw new ServerException("return_code不正确");
        }else{
            if(!notifyMap.get("result_code").toString().equals("SUCCESS")){
                throw new ServerException("result_code不正确");
            }
            Set<String> strings = notifyMap.keySet();
            log.info("微信支付回调");
            for(String key:strings){
                log.info("key:"+key);
                log.info("value:"+notifyMap.get(key));
            }
            log.info("微信支付回调Controller业务处理");
            BigDecimal amount=new BigDecimal(notifyMap.get("total_fee").toString()).divide(new BigDecimal(100));
            log.info("amount:"+amount);
            String orderCode=notifyMap.get("out_trade_no");
            String whereSql=" serial_number=:orderCode ";
            String tranNo= notifyMap.get("transaction_id").toString();
            Map<String,Object> mapParam=new HashMap<>();
            mapParam.put("orderCode",orderCode);
            List<TbOrderPayment> payList = userCenterManageService.queryListEntity(new TbOrderPayment(), whereSql, mapParam);
            log.info("查询payList返回值"+payList.size());
            if(payList==null||payList.size()==0){
                log.info(">>>>>>>>>>>>>>>>>payment not exist!!!! ");
                return;
            }
            TbOrderPayment payEntity= payList.get(0);
            if(payEntity.getAmount().compareTo(amount)!=0) {
                System.out.println("订单支付金额不正确"+orderCode);
                log.info(orderCode,"订单支付金额不正确");
                throw new ServerException("订单支付金额不正确");
            }
            whereSql=" code=:orderCode";
            List<TbActivityOrderEntity> orderList=userCenterManageService.queryListEntity(new TbActivityOrderEntity(),whereSql,mapParam);
            log.info("查询orderList返回值"+orderList.size());
            if(orderList==null||orderList.size()==0){
                log.info(">>>>>>>>>>>>>>>>>order not exist!!!! ");
                return;
            }
            TbActivityOrderEntity order=orderList.get(0);
            payEntity.setBankFlow(tranNo);
            payEntity.setReturnStatus(notifyMap.get("trade_status"));
            activityOrderManageService.dealPayBusiness(order,payEntity);

            /*
            PayInfo payInfo = new PayInfo();
            payInfo.setPayOrderNo(out_trade_no);
            payInfo = appPayService.getPayInfoObject(payInfo);
            System.out.println("订单信息"+payInfo);
            if(payInfo==null) {
                System.out.println("未查询到订单支付信息"+out_trade_no);
                throw new ServerException("未查询到订单支付信息");
            }
            //判断金额是否一致
            if(payInfo.getPayAmount().compareTo(total_amount)!=0) {
                System.out.println("订单支付金额不正确"+out_trade_no);
                logFile.logResult(out_trade_no,"订单支付金额不正确");
                throw new ServerException("订单支付金额不正确");
            }
            try {
                payInfo.setPayTradeNo(trade_no);
                appPayService.doNotify(payInfo);
            }catch (Exception e) {
                e.printStackTrace();
                logFile.logResult(out_trade_no,"订单处理异常"+e.getMessage());
                if (e instanceof ServerException) {
                    throw new ServerException(e.getMessage());
                }
                throw new ServerException("订单处理异常");
            }*/
        }
    }
    public String getNotifyXml(HttpServletRequest request) throws Exception {
        System.out.print("微信支付回调获取数据开始");
        log.debug("微信支付回调获取数据开始");
        String inputLine;
        String notityXml = "";
        try {
            while ((inputLine = request.getReader().readLine()) != null) {
                notityXml += inputLine;
            }
            request.getReader().close();
        } catch (Exception e) {
            log.error("xml获取失败：" + e);
            throw new ServerException("xml获取失败");
        }
        System.out.println("接收到的报文：" + notityXml);
        log.debug("收到微信异步回调："+notityXml);
        log.info("收到微信异步回调："+notityXml);

//	        notityXml ="<xml><appid><![CDATA[wx78662f865a5bf7b5]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1456817402]]></mch_id><nonce_str><![CDATA[bf82352570424e43b55051e8589f9ed4]]></nonce_str><openid><![CDATA[o1ZnfwTmxYR9Fyebbh3QDMigx23k]]></openid><out_trade_no><![CDATA[FXZC1516239816441]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[269A6089FF8F9B725C21D902E751924466AB4906BF22077F1C060E66C53BC137]]></sign><time_end><![CDATA[20180118094358]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[MWEB]]></trade_type><transaction_id><![CDATA[4200000068201801186133627976]]></transaction_id></xml>";
        if(StringUtils.isEmpty(notityXml)){
            log.info("xml为空");
            throw new ServerException("xml为空");
        }
        return notityXml;
    }

    public static void main(String [] arg){
        String str="7".length()>=4?"7".substring("7".length()-4):(7>100?'0'+"7":(7>10?("00"+"7"):("000"+"7")));
        System.out.println(">>>str:"+(new BigDecimal("0.10").doubleValue()==Double.parseDouble("0.1")));
    }
}
