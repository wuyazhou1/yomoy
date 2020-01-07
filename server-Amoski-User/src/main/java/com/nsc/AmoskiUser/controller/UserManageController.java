package com.nsc.AmoskiUser.controller;

import com.nsc.Amoski.entity.EnumEntity;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TUserEntity;
import com.nsc.AmoskiUser.service.UserManageService;
import com.nsc.Amoski.util.AmoskiException;
import com.nsc.Amoski.util.BaseController;
import com.nsc.Amoski.util.PasswordUtil;
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
@RequestMapping(value="UserManage")
@Api(value="UserManage",description = "用户管理模块")
@Controller
public class UserManageController  extends BaseController {
    Logger logger = LoggerFactory.getLogger(UserManageController.class);

    @Autowired
    @Lazy
    public UserManageService userManageService;

    @RequestMapping(value = "UserManageList",method = RequestMethod.POST)
    @ApiOperation(value="用户管理表单List数据接口", notes = "用户管理表单List数据接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="orgCode",value="部门",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="name",value="用户名",dataType="string", paramType = "insert"),
    })
    public PagingBean UserManageList(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<String, Object>();
        params= pageInfo(request);
        params.put("orgCode",request.getParameter("orgCode"));
        params.put("name",request.getParameter("name"));
        PagingBean pagingBean = userManageService.UserManageList(params);
        long endTime = System.currentTimeMillis();
        logger.info("访问UserManageController==》UserManageList"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return pagingBean;
    }


    @RequestMapping(value = "addTuser",method = RequestMethod.POST)
    @ApiOperation(value="添加用户", notes = "添加用户", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="createName",value="创建用户",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createDate",value="创建时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateName",value="修改用户",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="id",value="主键（自动生成）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="code",value="用户代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="name",value="用户名称",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="loginname",value="登入用户名",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="password",value="登入密码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="salt",value="加密密钥",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="locked",value="是否可用",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="orgCode",value="部门代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="remark",value="备注",dataType="string", paramType = "insert"),
    })
    public Object addTuser(TUserEntity tUserEntity,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        this.setCreateDatautil(tUserEntity,request);
        try {
            PasswordUtil.entryptPassword(tUserEntity);
            userManageService.addTuser(tUserEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问UserManageController==》addTuser"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }

    @RequestMapping(value = "updateTuser",method = RequestMethod.POST)
    @ApiOperation(value="添加用户", notes = "添加用户", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="createName",value="创建用户",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createDate",value="创建时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateName",value="修改用户",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="id",value="主键（自动生成）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="code",value="用户代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="name",value="用户名称",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="loginname",value="登入用户名",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="password",value="登入密码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="salt",value="加密密钥",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="locked",value="是否可用",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="orgCode",value="部门代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="remark",value="备注",dataType="string", paramType = "insert"),
    })
    public Object updateTuser(TUserEntity tUserEntity,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        this.setUpdateDatautil(tUserEntity,request);
        try {
            PasswordUtil.entryptPassword(tUserEntity);
            userManageService.updateTuser(tUserEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问UserManageController==》updateTuser"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }

    @RequestMapping(value = "RemoveTuser",method = RequestMethod.POST)
    @ApiOperation(value="删除用户", notes = "删除用户", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="用户id以逗号隔开",dataType="string", paramType = "insert"),
    })
    public Object RemoveTuser(String id){
        long starTime = System.currentTimeMillis();
        try {
            userManageService.RemoveTuser(id);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问UserManageController==》RemoveTuser"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }


    @RequestMapping(value = "UserImpowerRole",method = RequestMethod.POST)
    @ApiOperation(value="删除用户", notes = "删除用户", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="用户id以逗号隔开",dataType="string", paramType = "insert"),
    })
    public Object UserImpowerRole(String userCode,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        TUserEntity tUserEntityByRequest = this.getTUserEntityByRequest(request);
        try {
            return userManageService.UserImpowerRole(userCode,tUserEntityByRequest.getOrgCode());
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问UserManageController==》UserImpowerRole"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
    }

    @RequestMapping(value = "setUserImpowerRole",method = RequestMethod.POST)
    @ApiOperation(value="删除用户", notes = "删除用户", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="用户id以逗号隔开",dataType="string", paramType = "insert"),
    })
    public Object setUserImpowerRole(String userCode,String roleCode,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        TUserEntity tUserEntityByRequest = this.getTUserEntityByRequest(request);
        try {
            userManageService.setUserImpowerRole(userCode,roleCode);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.getProjectUtil(((AmoskiException) e).getCode()));
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问UserManageController==》setUserImpowerRole"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }

    @RequestMapping(value = "checkedUserCodeByCode",method = RequestMethod.POST)
    @ApiOperation(value="校验用户code值唯一性", notes = "校验用户code值唯一性", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="code",value="code",dataType="string", paramType = "insert"),
    })
    public Object checkedUserCodeByCode(String code,String id,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        try {
            userManageService.checkedUserCodeByCode(code,id);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.getProjectUtil(((AmoskiException) e).getCode()));
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问UserManageController==》checkedUserCodeByCode"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }
}
