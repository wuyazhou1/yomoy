package com.nsc.Amoski.controller.im;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jmessage.api.group.CreateGroupResult;
import cn.jmessage.api.group.GroupInfoResult;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.jg.JGIMGroups;
import com.nsc.Amoski.entity.jg.ReturnCode;
import com.nsc.Amoski.service.im.JGIMGroupsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author 李阳
 * @date 2019/12/13 14:10
 */
@RestController
@RequestMapping(path = "/imGroups")
@Api(value = "JGIMGroupsController", tags = "极光IM群组维护接口")
public class JGIMGroupsController {

    @Autowired
    private JGIMGroupsService jgimGroupsService;

    /**
     * 创建群组
     *
     * @param jgimGroups
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "创建群组", httpMethod = "POST")
    @PostMapping(path = "/createGroup")
    public Result createGroup(@RequestBody JGIMGroups jgimGroups) throws APIConnectionException, APIRequestException {
        if (StringUtils.isEmpty(jgimGroups.getOwner())) return new Result(ReturnCode.OWNER_NULL, null);
        if (StringUtils.isEmpty(jgimGroups.getGname())) return new Result(ReturnCode.GNAME_NULL, null);
        CreateGroupResult createGroupResult = jgimGroupsService.createGroup(jgimGroups.getOwner(), jgimGroups.getGname(), jgimGroups.getDesc(), jgimGroups.getAvatar(), jgimGroups.getFlag(), jgimGroups.getUserlist());
        return new Result(ReturnCode.OK, createGroupResult);
    }

    /**
     * 获取群组详情
     *
     * @param gid
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "获取群组详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gid", value = "群组id", required = true, dataType = "long", paramType = "path")
    })
    @GetMapping(path = "/getGroupInfo/{gid}")
    public Result getGroupInfo(@PathVariable long gid) throws APIConnectionException, APIRequestException {
        GroupInfoResult groupInfoResult = jgimGroupsService.getGroupInfo(gid);
        return new Result(ReturnCode.OK, groupInfoResult);
    }

    /**
     * 删除群组
     *
     * @param gid
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "删除群组", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gid", value = "群组id", required = true, dataType = "long", paramType = "path")
    })
    @DeleteMapping(path = "/deleteGroup/{gid}")
    public Result deleteGroup(@PathVariable long gid) throws APIConnectionException, APIRequestException {
        jgimGroupsService.deleteGroup(gid);
        return new Result(ReturnCode.OK);
    }

    /**
     * 更新群组信息
     *
     * @param gid
     * @param map
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "更新群组信息", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gid", value = "群组id", required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "map", value = "群组名:群组描述:群组头像", required = true, dataType = "map", paramType = "body")
    })
    @PutMapping(path = "/updateGroupInfo/{gid}")
    public Result updateGroupInfo(@PathVariable long gid, @RequestBody HashMap<String, Object> map) throws APIConnectionException, APIRequestException {
        if (StringUtils.isEmpty(map.get("groupName"))) return new Result(ReturnCode.GROUPNAME_NULL);
        if (StringUtils.isEmpty(map.get("groupDesc"))) return new Result(ReturnCode.GROUPDESC_NULL);
        if (StringUtils.isEmpty(map.get("avatar"))) return new Result(ReturnCode.AVATAR_NULL);
        jgimGroupsService.updateGroupInfo(gid, map.get("groupName").toString(), map.get("groupDesc").toString(), map.get("avatar").toString());
        return new Result(ReturnCode.OK);
    }

    /**
     * 更新群组成员
     *
     * @param gid
     * @param map
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "更新群组成员", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gid", value = "群组id", required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "map", value = "添加成员:删除成员", required = true, dataType = "map", paramType = "body")
    })
    @PutMapping(path = "/addOrRemoveMembers/{gid}")
    public Result addOrRemoveMembers(@PathVariable long gid, @RequestBody HashMap<String, Object> map) throws APIConnectionException, APIRequestException {
        if (StringUtils.isEmpty(map.get("addList")) && StringUtils.isEmpty(map.get("removeList")))
            return new Result(ReturnCode.ADDLIST_AND_REMOTELIST_NULL);
        if (null == map.get("addList")) {
            jgimGroupsService.addOrRemoveMembers(gid, null, (String[]) map.get("remoteList"));
            return new Result(ReturnCode.OK);
        }
        if (null == map.get("remoteList")) {
            jgimGroupsService.addOrRemoveMembers(gid, (String[]) map.get("addList"), null);
            return new Result(ReturnCode.OK);
        }
        jgimGroupsService.addOrRemoveMembers(gid, (String[]) map.get("addList"), (String[]) map.get("remoteList"));
        return new Result(ReturnCode.OK);
    }

}