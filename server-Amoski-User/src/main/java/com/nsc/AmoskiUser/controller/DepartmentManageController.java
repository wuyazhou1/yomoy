package com.nsc.AmoskiUser.controller;

import com.nsc.Amoski.entity.EnumEntity;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TDepartmentEntity;
import com.nsc.AmoskiUser.service.DepartmentManageService;
import com.nsc.AmoskiUser.service.LoginUserService;
import com.nsc.Amoski.util.BaseController;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@RestController
@RequestMapping(value="DepartmentManage")
//@Api(value="DepartmentManage",description = "部门管理控制层")
@Controller
public class DepartmentManageController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DepartmentManageController.class);
    @Autowired
    @Lazy
    public DepartmentManageService departmentManageService;

    @Autowired
    @Lazy
    public LoginUserService loginUserService;


    @RequiresPermissions("menu4")
    @ResponseBody
    @RequestMapping(value = "DepartmentList",method = RequestMethod.POST)
    @ApiOperation(value="部门", notes = "部门列表接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="name",value="部门名称",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="parentId",value="上级部门",dataType="string", paramType = "query")
    })
    public PagingBean findDepartmentList(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        //log.info("DepartmentManageController==>findDepartmentList");
        Map<String, Object> params = new HashMap<String, Object>();
        params= pageInfo(request);
        params.put("name",request.getParameter("name"));
        params.put("parentId",request.getParameter("parentId"));
        PagingBean pagingBean = departmentManageService.findDepartmentList(params);
        long endTime = System.currentTimeMillis();
        log.info("访问DepartmentManageController==》findDepartmentList"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return pagingBean;
    }



    @RequiresPermissions("menu4")
    @ResponseBody
    @RequestMapping(value = "AddDepartment",method = RequestMethod.POST)
    @ApiOperation(value="部门", notes = "添加部门", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="部门id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="orgName",value="部门名称",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="parentId",value="上级部门id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="remark",value="备注",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="createName",value="创建人",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="createDate",value="创建时间",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="updateName",value="修改人",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "query")
    })
    public Object AddDepartment(TDepartmentEntity department,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        this.setCreateDatautil(department,request);
        try {
            departmentManageService.AddDepartment(department);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        long endTime = System.currentTimeMillis();
        log.info("访问DepartmentManageController==》AddDepartment"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }


    @RequiresPermissions("menu4")
    @ResponseBody
    @RequestMapping(value = "UpdateDepartment",method = RequestMethod.POST)
    @ApiOperation(value="部门", notes = "添加部门", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="部门id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="orgName",value="部门名称",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="parentId",value="上级部门id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="remark",value="备注",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="createName",value="创建人",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="createDate",value="创建时间",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="updateName",value="修改人",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "query")
    })
    public Object UpdateDepartment(TDepartmentEntity department,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        this.setUpdateDatautil(department,request);
        try {
            departmentManageService.UpdateDepartment(department);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        long endTime = System.currentTimeMillis();
        log.info("访问DepartmentManageController==》UpdateDepartment"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }



    @RequiresPermissions("menu4")
    @ResponseBody
    @RequestMapping(value = "removeDepartment",method = RequestMethod.POST)
    @ApiOperation(value="部门", notes = "删除部门", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="部门id以逗号分隔开来",dataType="string", paramType = "query")
    })
    public Object removeDepartment(String id,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        //log.info("DepartmentManageController==>removeDepartment");
        try {
            departmentManageService.removeDepartment(id);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        long endTime = System.currentTimeMillis();
        log.info("访问DepartmentManageController==》removeDepartment"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }


    @ResponseBody
    @RequestMapping(value = "getZtreeDatail",method = RequestMethod.POST)
    @ApiOperation(value="部门", notes = "部门树查询", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="用户或者会员id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="loginIdentification",value="登入表示0用户员工，1会员客户",dataType="string", paramType = "query")
    })
    public Object getZtreeDatail(String id,String loginIdentification,HttpServletRequest request){
        //log.info("DepartmentManageController==>getZtreeDatail");
        long starTime = System.currentTimeMillis();
        List<Map> returnMap = null;
        try {
            //Map<String,Object>  parentMap = loginUserService.findRequestShiro(getIpAddr(request), null);
            //ShiroUser requestShiro = (ShiroUser) parentMap.get("ShiroUser");
            ShiroUser requestShiro = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            returnMap = departmentManageService.getZtreeDatail(requestShiro.getOrgCode(),"0");
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        long endTime = System.currentTimeMillis();
        log.info("访问DepartmentManageController==》getZtreeDatail"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return returnMap;
    }


    /*@ApiOperation(value = "文件上传",notes = "文件上传")
    @RequestMapping(value = "sssssssssssssssssss",method = RequestMethod.POST)
    public Object uplode(@ApiParam(value = "上传的文件", required = true) MultipartFile files1, @ApiParam(value = "上传的文件", required = true) MultipartFile files2) {
        return null;
    }*/
}
