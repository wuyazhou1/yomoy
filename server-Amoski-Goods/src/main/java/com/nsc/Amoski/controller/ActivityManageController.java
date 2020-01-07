package com.nsc.Amoski.controller;

import com.nsc.Amoski.service.UserManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="UserManage")
@Api(value="UserManage",description = "用户管理模块")
@Controller
public class ActivityManageController {
    @Autowired
    @Lazy
    private UserManageService userManageService;


    @RequestMapping(value = "Test",method = {RequestMethod.POST,RequestMethod.GET})
    //接口注解
    @ApiOperation(value="测试接口1", notes = "测试接口2", httpMethod = "POST" )
    //参数注解
    @ApiImplicitParams({
            @ApiImplicitParam(name="name",value="用户名",dataType="string", paramType = "query",example="xingguo"),
            @ApiImplicitParam(name="id",value="用户id",dataType="long", paramType = "query")
    })
    public Integer Test(HttpServletRequest request){
        request.getParameter("id");
        Integer name = userManageService.findData(request.getParameter("name"));
        System.out.println("访问层"+name);
        return name;
    }

    @RequestMapping(value="/getWechatUserInfo",method = {RequestMethod.POST,RequestMethod.GET})
    public String getWechatUserInfo(){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>request start:");
        //String s = get(ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>request end:");
        return "111";
    }

    @ApiOperation(value="界面请求", notes = "界面请求", httpMethod = "GET" )
    @RequestMapping(value = "/indexjsp" ,method = RequestMethod.GET)
    public String returnIndex(){
        return "indexjsp";
    }



}
