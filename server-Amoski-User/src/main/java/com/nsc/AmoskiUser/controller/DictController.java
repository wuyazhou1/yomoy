package com.nsc.AmoskiUser.controller;

import com.nsc.Amoski.entity.EnumEntity;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TDictEntity;
import com.nsc.AmoskiUser.service.DictService;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value="Dict")
@Api(value="Dict",description = "会员管理控制层")
@Controller
public class DictController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DictController.class);

    @Autowired
    @Lazy
    public DictService dctService;

    @RequestMapping(value = "DictList",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="字典表单List数据接口", notes = "字典表单List数据接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="dictTypeCode",value="字典类型代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="dictKey",value="字典主键",dataType="string", paramType = "insert"),
    })
    public PagingBean DictList(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<String, Object>();
        params= pageInfo(request);
        params.put("dictKey",request.getParameter("dictKey"));
        params.put("dictTypeCode",request.getParameter("dictTypeCode"));
        PagingBean pagingBean = dctService.DictList(params);
        long endTime = System.currentTimeMillis();
        log.info("访问DictController==》DictList"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return pagingBean;
    }

    @ResponseBody
    @RequestMapping(value = "AddDict",method = RequestMethod.POST)
    @ApiOperation(value="添加字典类型", notes = "添加字典类型", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键id",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="code",value="字典代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="dictTypeCode",value="字典类型代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="dictKey",value="字典名字",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="dictValue",value="字典值",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="remark",value="字典备注",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createName",value="创建人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createDate",value="创建时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateName",value="修改人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "insert"),
    })
    public Object AddDict(TDictEntity tDictEntity, HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        this.setCreateDatautil(tDictEntity,request);
        try {
            dctService.AddDict(tDictEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        long endTime = System.currentTimeMillis();
        log.info("访问DictController==》AddDict"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }


    @ResponseBody
    @RequestMapping(value = "UpdateDict",method = RequestMethod.POST)
    @ApiOperation(value="修改字典类型", notes = "修改字典类型", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键id",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="code",value="字典代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="dictTypeCode",value="字典类型代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="dictKey",value="字典名字",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="dictValue",value="字典值",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="remark",value="字典备注",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createName",value="创建人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="createDate",value="创建时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateName",value="修改人",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "insert"),
    })
    public Object UpdateDict(TDictEntity tDictEntity, HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        this.setUpdateDatautil(tDictEntity,request);
        try {
            dctService.UpdateDict(tDictEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        long endTime = System.currentTimeMillis();
        log.info("访问DictController==》UpdateDict"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }


    @ResponseBody
    @RequestMapping(value = "DeleteDict",method = RequestMethod.POST)
    @ApiOperation(value="删除字典类型", notes = "删除字典类型", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="code",value="主键id",dataType="string", paramType = "insert"),
    })
    public Object DeleteDictType(String code, HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        try {
            dctService.DeleteDict(code);
        } catch (Exception e) {
            e.printStackTrace();
            AmoskiException amoskiException = (AmoskiException)e;
            return this.resultData(EnumEntity.ProjectUtil.操作失败,amoskiException.getMessage());
        }
        long endTime = System.currentTimeMillis();
        log.info("访问DictController==》DeleteDictType"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }


    @RequestMapping(value = "CheckedDict",method = RequestMethod.POST)
    @ApiOperation(value="验证字典代码是否重复", notes = "验证字典代码是否重复", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键id",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="code",value="主键id",dataType="string", paramType = "insert"),
    })
    public Object CheckedDict(String id,String code,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        try {
            dctService.CheckedDict(id,code);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        long endTime = System.currentTimeMillis();
        log.info("访问DictController==》CheckedDict"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }

    @RequestMapping(value = "GetDictZtree",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="根据字典类型查询ztree树形菜单集合", notes = "根据字典类型查询ztree树形菜单集合", httpMethod = "POST" )
    public Object GetDictZtree(@RequestParam("parentCode") String parentCode){
        long starTime = System.currentTimeMillis();
        try {
            return dctService.GetDictZtree(parentCode);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {
            long endTime = System.currentTimeMillis();
            log.info("访问DictController==》GetDictZtree"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
    }

    @RequestMapping(value = "GetDictZtreeData",method = RequestMethod.POST)
    @ApiOperation(value="根据字典类型查询ztree树形菜单集合", notes = "根据字典类型查询ztree树形菜单集合", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="parentCode",value="上级字典类型",dataType="string", paramType = "insert")
    })
    public Object GetDictZtreeData(String parentCode,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        try {
            return dctService.GetDictZtree(parentCode);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }finally {
            long endTime = System.currentTimeMillis();
            log.info("访问DictController==》GetDictZtreeData"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
    }

}
