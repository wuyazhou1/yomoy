package com.nsc.Amoski.controller;


import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.service.ActivitySupplementManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.digester.Rules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="ActivitySupplementManage")
@Api(value="ActivitySupplementManage",description = "用户管理模块")
@Controller
public class ActivitySupplementManageController  extends ActivityServerBaseController<ActivitySupplementManageController> {
    @Autowired
    private ActivitySupplementManageService ActivitySupplementManageService;

    /**
     * 生成订单
     * @return  生成订单
     */

    @RequestMapping(value="/queryTicketDetails",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询票种详情", notes = "查询票种详情", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="basicsId",value="活动id",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="invoiceId",value="票种id",dataType="String", paramType = "query"),
    })
    public Result queryTicketDetails(HttpServletRequest request){
        Map<String,String> parent = new HashMap<>();
        parent.put("basicsId",request.getParameter("basicsId"));
        parent.put("invoiceId",request.getParameter("invoiceId"));
        if(parent.get("basicsId")==null || parent.get("basicsId").equals("")){
            return error(ResultMsg.IS_NULL);
        }
        if(parent.get("invoiceId")==null || parent.get("basicsId").equals("")){
            return error(ResultMsg.IS_NULL);
        }
        Map<String,Object> result = ActivitySupplementManageService.queryTicketDetails(parent);
        return success(result);
    }


    @RequestMapping(value="/queryRefundRules",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询退款规则表", notes = "查询退款规则表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="basicsId",value="活动id",dataType="String", paramType = "query"),
    })
    public Object queryRefundRules(HttpServletRequest request){
        Map<String,String> parent = new HashMap<>();
        parent.put("basicsId",request.getParameter("basicsId"));
        if(parent.get("basicsId")==null || parent.get("basicsId").equals("")){
            return error(ResultMsg.IS_NULL);
        }
        Object result = ActivitySupplementManageService.queryRefundRules(parent);
        return success(result);
    }

}
