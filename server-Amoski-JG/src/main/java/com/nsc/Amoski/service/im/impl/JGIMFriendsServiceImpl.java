package com.nsc.Amoski.service.im.impl;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.user.UserInfoResult;
import com.nsc.Amoski.service.im.JGIMFriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
@Service
public class JGIMFriendsServiceImpl implements JGIMFriendsService {

    @Autowired
    private JMessageClient jMessageClient;

    /**
     * 获取好友列表
     *
     * @param username
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public UserInfoResult[] getFriendsInfo(String username) throws APIConnectionException, APIRequestException {
        UserInfoResult[] userInfoResults = jMessageClient.getFriendsInfo(username);
        return userInfoResults;
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
    @Override
    public ResponseWrapper addFriends(String username, String user) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jMessageClient.addFriends(user, user);
        return responseWrapper;
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
    @Override
    public ResponseWrapper deleteFriends(String username, String user) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jMessageClient.deleteFriends(username, user);
        return responseWrapper;
    }

}
