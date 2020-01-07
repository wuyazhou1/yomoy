package com.nsc.Amoski.controller;

import com.nsc.Amoski.entity.EnumEntity;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TDictEntity;
import com.nsc.Amoski.service.DictService;
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

    @RequestMapping(value = "GetDictZtree",method = {RequestMethod.POST,RequestMethod.GET})
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
