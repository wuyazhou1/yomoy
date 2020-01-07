package com.nsc.Amoski.controller.im;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jmessage.api.user.UserListResult;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.jg.ReturnCode;
import com.nsc.Amoski.service.im.JGIMAdminUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
@RestController
@RequestMapping(path = "/imAdminUser")
@Api(value = "JGIMAdminUserController", tags = "极光IM管理员接口")
public class JGIMAdminUserController {

    @Autowired
    private JGIMAdminUserService jgimAdminUserService;

    /**
     * 注册管理员
     *
     * @param username
     * @param password
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "注册管理员", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "极光IM账号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "极光IM账号", required = true, dataType = "string", paramType = "query")

    })
    @PostMapping(path = "/registerAdmins")
    public Result registerAdmins(@RequestParam String username, @RequestParam String password) throws APIConnectionException, APIRequestException {
        String s = jgimAdminUserService.registerAdmins(username, password);
        return new Result(ReturnCode.OK, s);
    }

    /**
     * 获取管理员列表
     *
     * @param start
     * @param count
     * @return
     */
    @ApiOperation(value = "获取管理员列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始行", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "count", value = "条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping(path = "/getAdminListByAppkey/{start}/{count}")
    public Result getAdminListByAppkey(@PathVariable int start, @PathVariable int count) throws APIConnectionException, APIRequestException {
        UserListResult userListResult = jgimAdminUserService.getAdminListByAppkey(start < 0 ? 0 : start, count < 0 ? 0 : count);
        return new Result(ReturnCode.OK, userListResult);
    }

}