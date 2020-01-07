package com.nsc.Amoski.controller;

/*import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;*/
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="UserManage")
/*@Api(value="UserManage",description = "用户管理模块")*/
@Controller
public class UserManageController {

    @RequestMapping(value = "Test",method = RequestMethod.POST)
    //接口注解
    /*@ApiOperation(value="测试接口1", notes = "测试接口2", httpMethod = "POST" )
    //参数注解
    @ApiImplicitParams({
            @ApiImplicitParam(name="name",value="用户名",dataType="string", paramType = "query",example="xingguo"),
            @ApiImplicitParam(name="id",value="用户id",dataType="long", paramType = "query")
    })*/
    public String Test(){
        return "下班啦";
    }

    //@ApiOperation(value="界面请求", notes = "界面请求", httpMethod = "GET" )
    @RequestMapping(value = "/indexjsp" ,method = RequestMethod.GET)
    public String returnIndex(){
        return "indexjsp";
    }
}
