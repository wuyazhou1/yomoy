package com.nsc.AmoskiUser.controller;


import com.nsc.Amoski.entity.EnumEntity;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TDictTypeEntity;
import com.nsc.AmoskiUser.service.DictTypeService;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="DictType")
@Api(value="DictType",description = "字典类型控制层")
@Controller
public class DictTypeController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DictTypeController.class);

    @Autowired
    @Lazy
    public DictTypeService dictTypeService;

    @RequestMapping(value = "DictTypeList",method = RequestMethod.POST)
    @ApiOperation(value="字典类型表单List数据接口", notes = "字典类型表单List数据接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="dictName",value="字典类型名称",dataType="string", paramType = "insert"),
    })
    public PagingBean DictTypeList(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<String, Object>();
        params= pageInfo(request);
        params.put("dictName",request.getParameter("dictName"));
        params.put("parentCode",request.getParameter("parentCode"));
        PagingBean pagingBean = dictTypeService.RoleManageList(params);
        long endTime = System.currentTimeMillis();
        log.info("访问DictTypeController==》DictTypeList"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return pagingBean;
    }

    @RequestMapping(value = "AddDictType",method = RequestMethod.POST)
    @ApiOperation(value="添加字典类型", notes = "添加字典类型", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键id",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="code",value="字典代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="parentCode",value="上级字典类型代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="dictName",value="字典类型名字",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="remark",value="备注",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createName",value="创建人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createDate",value="创建时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateName",value="修改人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "insert"),
    })
    public Object AddDictType(TDictTypeEntity tDictTypeEntity, HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        this.setCreateDatautil(tDictTypeEntity,request);
        try {
            dictTypeService.AddDictType(tDictTypeEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        long endTime = System.currentTimeMillis();
        log.info("访问DictTypeController==》AddDictType"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }

    @RequestMapping(value = "UpdateDictType",method = RequestMethod.POST)
    @ApiOperation(value="修改字典类型", notes = "修改字典类型", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键id",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="code",value="字典代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="parentCode",value="上级字典类型代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="dictName",value="字典类型名字",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="remark",value="备注",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createName",value="创建人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createDate",value="创建时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateName",value="修改人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "insert"),
    })
    public Object UpdateDictType(TDictTypeEntity tDictTypeEntity, HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        this.setUpdateDatautil(tDictTypeEntity,request);
        try {
            dictTypeService.UpdateDictType(tDictTypeEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        long endTime = System.currentTimeMillis();
        log.info("访问DictTypeController==》UpdateDictType"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }


    @RequestMapping(value = "DeleteDictType",method = RequestMethod.POST)
    @ApiOperation(value="删除字典类型", notes = "删除字典类型", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键id",dataType="string", paramType = "insert"),
    })
    public Object DeleteDictType(String id, HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        try {
            dictTypeService.DeleteDictType(id);
        } catch (Exception e) {
            e.printStackTrace();
            AmoskiException amoskiException = (AmoskiException)e;
            return this.resultData(EnumEntity.ProjectUtil.操作失败,amoskiException.getMessage());
        }
        long endTime = System.currentTimeMillis();
        log.info("访问DictTypeController==》DeleteDictType"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }


    @RequestMapping(value = "CheckedDictType",method = RequestMethod.POST)
    @ApiOperation(value="验证字典代码是否重复", notes = "验证字典代码是否重复", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键id",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="code",value="主键id",dataType="string", paramType = "insert"),
    })
    public Object CheckedDictType(String id,String code,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        try {
            dictTypeService.CheckedDictType(id,code);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        long endTime = System.currentTimeMillis();
        log.info("访问DictTypeController==》CheckedDictType"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }

    @RequestMapping(value = "getZtreeDatail",method = RequestMethod.POST)
    @ApiOperation(value="字典类型树查询", notes = "字典类型树查询", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="parentCode",value="上级字典类型代码",dataType="string", paramType = "query"),
    })
    public Object getZtreeDatail(String parentCode,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        List<Map> returnMap = null;
        try {
            returnMap = dictTypeService.getZtreeDatail(parentCode);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        long endTime = System.currentTimeMillis();
        log.info("访问DictTypeController==》getZtreeDatail"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return returnMap;
    }
}
