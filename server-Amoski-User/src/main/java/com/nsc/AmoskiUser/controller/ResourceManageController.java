package com.nsc.AmoskiUser.controller;


import com.nsc.Amoski.entity.EnumEntity;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TResourceEntity;
import com.nsc.AmoskiUser.service.ResourceManageService;
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
@RequestMapping(value="ResourceManage")
@Api(value="ResourceManage",description = "元素管理模块")
@Controller
public class ResourceManageController extends BaseController {
    Logger logger = LoggerFactory.getLogger(ResourceManageController.class);

    @Autowired
    @Lazy
    public ResourceManageService resourceManageService;


    @RequestMapping(value = "ResourceManageList",method = RequestMethod.POST)
    @ApiOperation(value="元素管理列表", notes = "元素管理列表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="menuCode",value="菜单代码",dataType="string", paramType = "insert"),
    })
    public PagingBean  ResourceManageList(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<String, Object>();
        params= pageInfo(request);
        params.put("menuCode",request.getParameter("menuCode"));
        PagingBean pagingBean = resourceManageService. ResourceManageList(params);
        long endTime = System.currentTimeMillis();
        logger.info("访问ResourceManageController==》ResourceManageList"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return pagingBean;
    }


    @RequestMapping(value = "AddResourceManage",method = RequestMethod.POST)
    @ApiOperation(value="添加元素", notes = "添加元素", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键（自动生成）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="code",value="元素代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="menuCode",value="菜单代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="resName",value="元素名称",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="resType",value="元素类型",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="remark",value="元素备注",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createName",value="创建人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createDate",value="创建时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateName",value="修改人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "insert"),
    })
    public Object  AddResourceManage(TResourceEntity tResourceEntity, HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        this.setCreateDatautil(tResourceEntity,request);
        try {
            resourceManageService.AddMenuManage(tResourceEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问ResourceManageController==》AddResourceManage"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }


    @RequestMapping(value = "updateResourceManage",method = RequestMethod.POST)
    @ApiOperation(value="修改元素", notes = "修改元素", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键（自动生成）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="code",value="元素代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="menuCode",value="菜单代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="resName",value="元素名称",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="resType",value="元素类型",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="remark",value="元素备注",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createName",value="创建人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createDate",value="创建时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateName",value="修改人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "insert"),
    })
    public Object  updateResourceManage(TResourceEntity tResourceEntity, HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        this.setUpdateDatautil(tResourceEntity,request);
        try {
            resourceManageService.updateMenuManage(tResourceEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问ResourceManageController==》updateResourceManage"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }

    @RequestMapping(value = "deleteResourceManage",method = RequestMethod.POST)
    @ApiOperation(value="删除元素", notes = "删除元素", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键（自动生成）",dataType="string", paramType = "insert"),
    })
    public Object  deleteResourceManage(TResourceEntity tResourceEntity, HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        try {
            resourceManageService.deleteResourceManage(request.getParameter("id"));
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问ResourceManageController==》deleteResourceManage"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }

    @RequestMapping(value = "checkedResourceIsExists",method = RequestMethod.POST)
    @ApiOperation(value="校验元素代码是否存在", notes = "校验元素码是否存在", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="code",value="角色代码",dataType="string", paramType = "insert"),
    })
    public Object checkedResourceIsExists(String code,String id, HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        try {
            resourceManageService.checkedResourceIsExists(code,id);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.getProjectUtil(((AmoskiException) e).getCode()));
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问ResourceManageController==》checkedResourceIsExists"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }

}
