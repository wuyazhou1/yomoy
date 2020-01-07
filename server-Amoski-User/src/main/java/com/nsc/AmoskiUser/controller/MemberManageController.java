package com.nsc.AmoskiUser.controller;


import com.nsc.Amoski.entity.EnumEntity;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TUserEntity;
import com.nsc.AmoskiUser.service.MemberManageService;
import com.nsc.Amoski.util.AmoskiException;
import com.nsc.Amoski.util.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="MemberManage")
@Api(value="MemberManage",description = "会员管理控制层")
@Controller
public class MemberManageController  extends BaseController {
    Logger logger = LoggerFactory.getLogger(MemberManageController.class);

    @Autowired
    @Lazy
    public MemberManageService memberManageService;

    @RequestMapping(value = "MemberList",method = RequestMethod.POST)
    @ApiOperation(value="获取会员列表", notes = "获取会员列表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="orgCode",value="部门id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="loginName",value="会员登入名称",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="nickname",value="微信昵称",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="name",value="会员名称",dataType="string", paramType = "query")
    })
    public PagingBean getMemberList(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<String, Object>();
        params= pageInfo(request);
        params.put("orgCode",request.getParameter("orgCode"));
        params.put("name",request.getParameter("name"));
        params.put("tel",request.getParameter("tel"));
        params.put("address",request.getParameter("address"));
        PagingBean pagingBean = memberManageService.getMemberList(params);
        long endTime = System.currentTimeMillis();
        logger.info("访问MemberManageController==》getMemberList"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return pagingBean;
    }

    @RequestMapping(value = "getMemberImpowerRole",method = RequestMethod.POST)
    @ApiOperation(value="获取会员角色权限配置", notes = "获取会员角色权限配置", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="会员id",value="会员id",dataType="string", paramType = "query"),
    })
    public Object getMemberImpowerRole(String memberId,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        TUserEntity tUserEntityByRequest = this.getTUserEntityByRequest(request);
        try {
            return memberManageService.getMemberImpowerRole(memberId,tUserEntityByRequest.getOrgCode());
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.getProjectUtil(((AmoskiException) e).getCode()));
        }finally {
            long endTime = System.currentTimeMillis();
            logger.info("访问MemberManageController==》getMemberImpowerRole"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
    }
}
