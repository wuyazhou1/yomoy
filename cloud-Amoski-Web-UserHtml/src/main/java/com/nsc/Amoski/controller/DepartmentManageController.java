package com.nsc.Amoski.controller;

import com.nsc.Amoski.entity.EnumEntity;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TDepartmentEntity;
import com.nsc.Amoski.service.DepartmentManageService;
import com.nsc.Amoski.util.BaseController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
            //ShiroUser requestShiro = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            returnMap = departmentManageService.getZtreeDatail(id,loginIdentification);
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
