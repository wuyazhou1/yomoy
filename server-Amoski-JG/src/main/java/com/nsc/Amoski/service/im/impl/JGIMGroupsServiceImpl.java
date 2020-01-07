package com.nsc.Amoski.service.im.impl;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.group.CreateGroupResult;
import cn.jmessage.api.group.GroupInfoResult;
import cn.jmessage.api.group.MemberListResult;
import com.nsc.Amoski.service.im.JGIMGroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 李阳
 * @date 2019/12/13 14:10
 */
@Service
public class JGIMGroupsServiceImpl implements JGIMGroupsService {

    @Autowired
    private JMessageClient jMessageClient;

    /**
     * 创建群组
     *
     * @param owner
     * @param gname
     * @param desc
     * @param avatar
     * @param flag
     * @param userlist
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public CreateGroupResult createGroup(String owner, String gname, String desc, String avatar, int flag, String... userlist) throws APIConnectionException, APIRequestException {
        CreateGroupResult createGroupResult = jMessageClient.createGroup(owner, gname, desc, avatar, flag, userlist);
        return createGroupResult;
    }

    /**
     * 获取群组详情
     *
     * @param gid
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public GroupInfoResult getGroupInfo(long gid) throws APIConnectionException, APIRequestException {
        GroupInfoResult groupInfoResult = jMessageClient.getGroupInfo(gid);
        return groupInfoResult;
    }

    /**
     * 删除群组
     *
     * @param gid
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public void deleteGroup(long gid) throws APIConnectionException, APIRequestException {
        jMessageClient.deleteGroup(gid);
    }

    /**
     * 更新群组信息
     *
     * @param gid
     * @param groupName
     * @param groupDesc
     * @param avatar
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public void updateGroupInfo(long gid, String groupName, String groupDesc, String avatar) throws APIConnectionException, APIRequestException {
        jMessageClient.updateGroupInfo(gid, groupName, groupDesc, avatar);
    }

    /**
     * 更新群组成员
     *
     * @param gid
     * @param addList
     * @param removeList
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public void addOrRemoveMembers(long gid, String[] addList, String[] removeList) throws APIConnectionException, APIRequestException {
        jMessageClient.addOrRemoveMembers(gid, addList, removeList);
    }

    /**
     * 获取群组成员列表
     *
     * @param git
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public MemberListResult getGroupMembers(long git) throws APIConnectionException, APIRequestException {
        MemberListResult memberListResult = jMessageClient.getGroupMembers(git);
        return memberListResult;
    }

}
