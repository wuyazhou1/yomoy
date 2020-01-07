package com.nsc.Amoski.controller.im;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.user.UserInfoResult;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.jg.ReturnCode;
import com.nsc.Amoski.service.im.JGIMFriendsService;
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
@RequestMapping(path = "/imFriends")
@Api(value = "JGIMFriendsController", tags = "极光IM好友接口")
public class JGIMFriendsController {

    @Autowired
    private JGIMFriendsService jgimFriendsService;

    /**
     * 获取好友列表
     *
     * @param username
     * @return
     */
    @ApiOperation(value = "获取好友列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "极光IM账号", required = true, dataType = "string", paramType = "query")
    })
    @GetMapping(path = "/getFriendsInfo")
    public Result getFriendsInfo(@RequestParam String username) throws APIConnectionException, APIRequestException {
        UserInfoResult[] userInfoResults = jgimFriendsService.getFriendsInfo(username);
        return new Result(ReturnCode.OK, userInfoResults);
    }

    /**
     * 添加好友
     *
     * @param username
     * @param user
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "添加好友", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "极光IM账号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "user", value = "对方极光IM账号", required = true, dataType = "string", paramType = "query")
    })
    @PostMapping(path = "/addFriends")
    public Result addFriends(@RequestParam String username, @RequestParam String user) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jgimFriendsService.addFriends(username, user);
        return new Result(ReturnCode.OK, responseWrapper);
    }

    /**
     * 删除好友
     *
     * @param username
     * @param user
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "删除好友", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "极光IM账号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "user", value = "对方极光IM账号", required = true, dataType = "string", paramType = "query")
    })
    @DeleteMapping(path = "/deleteFriends")
    public Result deleteFriends(@RequestParam String username, @RequestParam String user) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jgimFriendsService.deleteFriends(username, user);
        return new Result(ReturnCode.OK, responseWrapper);
    }

}
