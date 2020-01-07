package com.nsc.AmoskiUser.controller;


import com.nsc.Amoski.entity.EnumEntity;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TMenuEntity;
import com.nsc.AmoskiUser.service.MenuManageService;
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
@RequestMapping(value="MenuManage")
@Api(value="MenuManage",description = "菜单管理模块")
@Controller
public class MenuManageController extends BaseController {
    Logger logger = LoggerFactory.getLogger(MenuManageController.class);

    @Autowired
    @Lazy
    public MenuManageService menuManageService;

    @RequestMapping(value = "MenuManageList",method = RequestMethod.POST)
    @ApiOperation(value="菜单管理列表", notes = "菜单管理列表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="menu",value="部门",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="name",value="用户名",dataType="string", paramType = "insert"),
    })
    public PagingBean MenuManageList(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<String, Object>();
        params= pageInfo(request);
        params.put("parentCode",request.getParameter("parentCode"));
        params.put("menuName",request.getParameter("menuName"));
        PagingBean pagingBean = menuManageService.MenuManageList(params);
        long endTime = System.currentTimeMillis();
        logger.info("访问MenuManageController==》MenuManageList"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return pagingBean;
    }


    @RequestMapping(value = "AddMenuManage",method = RequestMethod.POST)
    @ApiOperation(value="添加菜单", notes = "添加菜单", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键（自动生成）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="code",value="代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="parentCode",value="上级菜单",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="menuName",value="菜单名称",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="orderCode",value="排序编号",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="menuUrl",value="菜单连接",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createName",value="创建人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createDate",value="创建时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateName",value="修改人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "insert"),
    })
    public Object AddMenuManage(TMenuEntity tMenuEntity,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        this.setCreateDatautil(tMenuEntity,request);
        try {
            menuManageService.AddMenuManage(tMenuEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问MenuManageController==》AddMenuManage"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }



    @RequestMapping(value = "UpdateMenuManage",method = RequestMethod.POST)
    @ApiOperation(value="修改菜单", notes = "修改菜单", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键（自动生成）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="code",value="代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="parentCode",value="上级菜单",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="menuName",value="菜单名称",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="orderCode",value="排序编号",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="menuUrl",value="菜单连接",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createName",value="创建人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createDate",value="创建时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateName",value="修改人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "insert"),
    })
    public Object UpdateMenuManage(TMenuEntity tMenuEntity,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        this.setCreateDatautil(tMenuEntity,request);
        try {
            menuManageService.UpdateMenuManage(tMenuEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问MenuManageController==》UpdateMenuManage"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }


    @RequestMapping(value = "RemoveTMenu",method = RequestMethod.POST)
    @ApiOperation(value="删除菜单", notes = "删除菜单", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="用户id以逗号隔开",dataType="string", paramType = "insert"),
    })
    public Object RemoveTMenu(String id){
        long starTime = System.currentTimeMillis();
        try {
            menuManageService.RemoveTMenu(id);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问MenuManageController==》RemoveTMenu"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }


    @RequestMapping(value = "ztreeTMenu",method = RequestMethod.POST)
    @ApiOperation(value="菜单树", notes = "菜单树", httpMethod = "POST" )
    public Object ztreeTMenu(String id){
        long starTime = System.currentTimeMillis();
        try {
            return menuManageService.ztreeTMenu();
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问MenuManageController==》ztreeTMenu"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
    }


    @RequestMapping(value = "checkedMenuIsExists",method = RequestMethod.POST)
    @ApiOperation(value="校验菜单代码是否存在", notes = "校验菜单代码是否存在", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="code",value="菜单代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="id",value="菜单id",dataType="string", paramType = "insert"),
    })
    public Object checkedRoleIsExists(String code,String id, HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        try {
            menuManageService.checkedRoleIsExists(code,id);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.getProjectUtil(((AmoskiException) e).getCode()));
        }finally {
            long endTime = System.currentTimeMillis();
            logger.info("访问MenuManageController==》checkedRoleIsExists"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }
}
