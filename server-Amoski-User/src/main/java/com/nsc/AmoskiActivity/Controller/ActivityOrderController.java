package com.nsc.AmoskiActivity.Controller;


import com.nsc.Amoski.entity.EnumEntity;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.util.BaseController;
import com.nsc.AmoskiActivity.Service.ActivityOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 活动订单
 *
 */
@RequestMapping(value="ActivityOrder")
@Api(value="ActivityOrder",description = "活动订单管理")
@Controller
public class ActivityOrderController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(ActivityOrderController.class);

    @Autowired
    @Qualifier("ActivityOrderServiceImpl")
    private ActivityOrderService activityOrderService;

    @ResponseBody
    @RequestMapping(value = "activityOrderlist",method = RequestMethod.POST)
    @ApiOperation(value="活动订单列表", notes = "活动订单列表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="orderId",value="订单编号",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="basicsTile",value="活动名称",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="signUpName",value="报名人",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="state",value="订单状态",dataType="string", paramType = "query")
    })
    public PagingBean activityOrderlist(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        log.info("ActivityOrderController==>activityOrderlist");
        Map<String, Object> params = new HashMap<String, Object>();
        params= pageInfo(request);
        params.put("orderId",request.getParameter("orderId"));
        params.put("basicsTile",request.getParameter("basicsTile"));
        params.put("memberId",request.getParameter("memberId"));
        params.put("state",request.getParameter("state"));
        PagingBean pagingBean = activityOrderService.activityOrderlist(params);
        long endTime = System.currentTimeMillis();
        log.info("访问DepartmentManageController==》findDepartmentList"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return pagingBean;
    }

    @ResponseBody
    @RequestMapping(value = "activityOrderDatail",method = RequestMethod.POST)
    @ApiOperation(value="活动订单详情", notes = "活动订单详情", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="订单代码",dataType="string", paramType = "query"),
    })
    public PagingBean activityOrderDatail(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        log.info("ActivityOrderController==>activityOrderDatail");
        Map<String, Object> params = new HashMap<String, Object>();
        params= pageInfo(request);
        params.put("id",request.getParameter("id"));
        PagingBean pagingBean = activityOrderService.activityOrderDatail(params);
        long endTime = System.currentTimeMillis();
        log.info("访问DepartmentManageController==》findDepartmentList"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return pagingBean;
    }


    @ResponseBody
    @RequestMapping(value = "refundApplication",method = RequestMethod.POST)
    @ApiOperation(value="退款申请接口", notes = "退款申请接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="订单代码",dataType="string", paramType = "query"),
    })
    public Object refundApplication(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        Map<String,Object> parentMap = new HashMap<>();
        ShiroUser suser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        parentMap.put("DETAILS_ID",request.getParameter("DETAILS_ID"));//订单详情id
        parentMap.put("SERVICE_MONEY",request.getParameter("SERVICE_MONEY"));//退款收取服务费
        parentMap.put("REFUND_MONEY",request.getParameter("REFUND_MONEY"));//退款金额
        parentMap.put("PRIMITIVE_MONEY",request.getParameter("PRIMITIVE_MONEY"));//订单金额
        parentMap.put("REFUND_AUDIT_ID",request.getParameter("REFUND_AUDIT_ID"));//退款申请id
        parentMap.put("REMARK",request.getParameter("REMARK"));//备注
        parentMap.put("USER_NAME",suser.loginName);//备注
        if(Boolean.parseBoolean(request.getParameter("BOOL"))){
            activityOrderService.refundApplicationFalse(parentMap);
        }else{
            activityOrderService.refundApplication(parentMap);
        }
        long endTime = System.currentTimeMillis();
        log.info("访问DictTypeController==》UpdateDictType"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }
}
