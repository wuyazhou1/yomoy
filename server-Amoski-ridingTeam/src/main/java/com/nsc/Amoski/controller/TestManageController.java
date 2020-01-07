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
@RequestMapping(value="/")
@Controller
public class TestManageController {

    @RequestMapping(value="/startNetty",method = {RequestMethod.POST,RequestMethod.GET})
    public String startNetty(){
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>startNetty :");
        //String s = get(ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>startNetty:");
        return "success";
    }

    @ApiOperation(value="界面请求", notes = "界面请求", httpMethod = "GET" )
    @RequestMapping(value = "/indexjsp" ,method = RequestMethod.GET)
    public String returnIndex(){
        return "indexjsp";
    }



}
