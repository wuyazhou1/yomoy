package com.nsc.Amoski.service.im;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jmessage.api.group.CreateGroupResult;
import cn.jmessage.api.group.GroupInfoResult;
import cn.jmessage.api.group.MemberListResult;

/**
 * @author 李阳
 * @date 2019/12/13 14:10
 */
public interface JGIMGroupsService {

    CreateGroupResult createGroup(String owner, String gname, String desc, String avatar, int flag, String... userlist) throws APIConnectionException, APIRequestException;

    GroupInfoResult getGroupInfo(long gid) throws APIConnectionException, APIRequestException;

    void deleteGroup(long gid) throws APIConnectionException, APIRequestException;

    void updateGroupInfo(long gid, String groupName, String groupDesc, String avatar) throws APIConnectionException, APIRequestException;

    void addOrRemoveMembers(long gid, String[] addList, String[] removeList) throws APIConnectionException, APIRequestException;

    MemberListResult getGroupMembers(long git) throws APIConnectionException, APIRequestException;

}