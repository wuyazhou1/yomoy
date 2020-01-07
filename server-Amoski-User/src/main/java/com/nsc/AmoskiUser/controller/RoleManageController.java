package com.nsc.AmoskiUser.controller;

import com.nsc.Amoski.entity.EnumEntity;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TRoleEntity;
import com.nsc.Amoski.entity.TUserEntity;
import com.nsc.AmoskiUser.service.RoleManageService;
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
@RequestMapping(value="RoleManage")
@Api(value="RoleManage",description = "角色管理模块")
@Controller
public class RoleManageController extends BaseController {
    Logger logger = LoggerFactory.getLogger(RoleManageController.class);

    @Autowired
    @Lazy
    public RoleManageService roleManageService;

    @RequestMapping(value = "RoleManageList",method = RequestMethod.POST)
    @ApiOperation(value="角色管理表单List数据接口", notes = "角色管理表单List数据接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="orgCode",value="部门id",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="name",value="角色名称",dataType="string", paramType = "insert"),
    })
    public PagingBean RoleManageList(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<String, Object>();
        params= pageInfo(request);
        params.put("orgCode",request.getParameter("orgCode"));
        params.put("name",request.getParameter("name"));
        params.put("lockflag",request.getParameter("lockflag"));
        params.put("ispublic",request.getParameter("ispublic"));
        PagingBean pagingBean = roleManageService.RoleManageList(params);
        long endTime = System.currentTimeMillis();
        logger.info("访问RoleManageController==》setRoleImpowerUser"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return pagingBean;
    }

    @RequestMapping(value = "addRole",method = RequestMethod.POST)
    @ApiOperation(value="添加角色", notes = "添加角色", httpMethod = "POST" )
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
    public Object addRole(TRoleEntity tRole, HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        this.setCreateDatautil(tRole,request);
        try {
            roleManageService.addRole(tRole);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问RoleManageController==》setRoleImpowerUser"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }


    @RequestMapping(value = "updateRole",method = RequestMethod.POST)
    @ApiOperation(value="修改角色", notes = "修改角色", httpMethod = "POST" )
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
    public Object updateRole(TRoleEntity tRole, HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        this.setCreateDatautil(tRole,request);
        try {
            roleManageService.updateRole(tRole);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问RoleManageController==》setRoleImpowerUser"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }

    @RequestMapping(value = "RemoveTRole",method = RequestMethod.POST)
    @ApiOperation(value="删除用户", notes = "删除用户", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="用户id以逗号隔开",dataType="string", paramType = "insert"),
    })
    public Object RemoveTRole(String id){
        long starTime = System.currentTimeMillis();
        try {
            roleManageService.RemoveTRole(id);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问RoleManageController==》setRoleImpowerUser"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }

    @RequestMapping(value = "checkedRoleIsExists",method = RequestMethod.POST)
    @ApiOperation(value="校验角色代码是否存在", notes = "校验角色代码是否存在", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="code",value="角色代码",dataType="string", paramType = "insert"),
    })
    public Object checkedRoleIsExists(String code,String id, HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        try {
            roleManageService.checkedRoleIsExists(code,id);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.getProjectUtil(((AmoskiException) e).getCode()));
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问RoleManageController==》setRoleImpowerUser"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }

    @RequestMapping(value = "setRoleMenuResource",method = RequestMethod.POST)
    @ApiOperation(value="设置角色权限", notes = "设置角色权限", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="role",value="角色代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="menu",value="菜单代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="resource",value="元素代码",dataType="string", paramType = "insert"),
    })
    public Object setRoleMenuResource(String role,String menu, String resource){
        long starTime = System.currentTimeMillis();
        try {
            roleManageService.setRoleMenuResource(role,menu,resource);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.getProjectUtil(((AmoskiException) e).getCode()));
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问RoleManageController==》setRoleImpowerUser"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }


    @RequestMapping(value = "getRoleMenuResource",method = RequestMethod.POST)
    @ApiOperation(value="查看角色权限", notes = "查看角色权限", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="role",value="角色代码",dataType="string", paramType = "select"),
    })
    public Object getRoleMenuResource(String role){
        long starTime = System.currentTimeMillis();
        try {
            return roleManageService.getRoleMenuResource(role);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.getProjectUtil(((AmoskiException) e).getCode()));
        }finally {
            long endTime = System.currentTimeMillis();
            logger.info("访问RoleManageController==》setRoleImpowerUser"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
    }



    @RequestMapping(value = "getRoleImpowerUser",method = RequestMethod.POST)
    @ApiOperation(value="获取角色授权用户信息", notes = "查看角色权限", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="role",value="角色代码",dataType="string", paramType = "select"),
    })
    public Object getRoleImpowerUser(String role,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        TUserEntity tUserEntityByRequest = this.getTUserEntityByRequest(request);
        try {
            return roleManageService.getRoleImpowerUser(role,tUserEntityByRequest.getOrgCode());
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.getProjectUtil(((AmoskiException) e).getCode()));
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问RoleManageController==》setRoleImpowerUser"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
    }


    @RequestMapping(value = "setRoleImpowerUser",method = RequestMethod.POST)
    @ApiOperation(value="获取角色授权用户信息", notes = "查看角色权限", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="role",value="角色代码",dataType="string", paramType = "select"),
    })
    public Object setRoleImpowerUser(String roleCode,String userCode , HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        TUserEntity tUserEntityByRequest = this.getTUserEntityByRequest(request);
        try {
            roleManageService.setRoleImpowerUser(roleCode,userCode);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.getProjectUtil(((AmoskiException) e).getCode()));
        }finally {
            long endTime = System.currentTimeMillis();
            logger.info("访问RoleManageController==》setRoleImpowerUser"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }

}
