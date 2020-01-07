package com.nsc.Amoski.controller.im;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.user.UserInfoResult;
import cn.jmessage.api.user.UserStateResult;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.jg.JGIMUser;
import com.nsc.Amoski.entity.jg.ReturnCode;
import com.nsc.Amoski.service.im.JGIMUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
@RestController
@RequestMapping(path = "/imUser")
@Api(value = "JGIMUserController", tags = "极光IM用户接口")
public class JGIMUserController {

    @Autowired
    private JGIMUserService jgimUserService;

    /**
     * 注册用户
     *
     * @param member_id
     * @param username
     * @param password
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "注册用户", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "member_id", value = "用户表主键", required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "username", value = "极光IM账号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "极光IM密码", required = true, dataType = "string", paramType = "query"),
    })
    @PostMapping(path = "/userRegister/{member_id}")
    public Result register(@PathVariable Integer member_id, @RequestParam String username, @RequestParam String password) throws APIConnectionException, APIRequestException {
        boolean b = jgimUserService.registerUsers(member_id, username, password);
        if (!b) return new Result(ReturnCode.IM_REGISTER_USERNAME_NO_NULL);
        return new Result(ReturnCode.OK);
    }

    /**
     * 查询用户信息
     *
     * @param username
     * @return
     */
    @ApiOperation(value = "查询用户信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping(path = "/getUserInfo")
    public Result getUserInfo(@RequestParam String username) throws APIConnectionException, APIRequestException {
        UserInfoResult userInfoResult = jgimUserService.getUserInfo(username);
        return new Result(ReturnCode.OK, userInfoResult);
    }

    /**
     * 查询用户在线状态
     *
     * @param username
     * @return
     */
    @ApiOperation(value = "查询用户在线状态", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping(path = "/getUserStatus")
    public Result getUserStatus(@RequestParam String username) throws APIConnectionException, APIRequestException {
        UserStateResult userStateResult = jgimUserService.getUserStatus(username);
        return new Result(ReturnCode.OK, userStateResult);
    }

    /**
     * 删除用户
     *
     * @param username
     * @return
     */
    @ApiOperation(value = "删除用户", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号", required = true, dataType = "string", paramType = "query")
    })
    @DeleteMapping(path = "/deleteUser")
    public Result deleteUser(@RequestParam String username) throws APIConnectionException, APIRequestException {
        jgimUserService.deleteUser(username);
        return new Result(ReturnCode.OK);
    }

    /**
     * 更新用户信息
     *
     * @param jgimUser
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "更新用户信息", httpMethod = "PUT")
    @PutMapping(path = "/updateUserInfo/")
    public Result updateUserInfo(@RequestBody JGIMUser jgimUser) throws APIConnectionException, APIRequestException {
        if (StringUtils.isEmpty(jgimUser.getUsername())) return new Result(ReturnCode.IM_USERNAME_NULL);
        jgimUserService.updateUserInfo(jgimUser);
        return new Result(ReturnCode.OK);
    }

    /**
     * 修改密码
     *
     * @param username
     * @param newPassword
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "修改密码", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, dataType = "string", paramType = "query")
    })
    @PutMapping(path = "/updateUserPassword")
    public Result updateUserPassword(@RequestParam String username, @RequestParam String newPassword) throws APIConnectionException, APIRequestException {
        jgimUserService.updateUserPassword(username, newPassword);
        return new Result(ReturnCode.OK);
    }

    /**
     * 禁用用户
     *
     * @param username
     * @param disable
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "禁用用户", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "disable", value = "禁用:1(禁用),0(不禁用)", required = true, dataType = "int", paramType = "path")
    })
    @PutMapping(path = "/forbidUser/{disable}")
    public Result forbidUser(@RequestParam String username, @PathVariable int disable) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jgimUserService.forbidUser(username, disable < 1 ? false : true);
        return new Result(ReturnCode.OK, responseWrapper);
    }

}
