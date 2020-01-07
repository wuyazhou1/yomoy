package com.nsc.Amoski.service.im;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.user.UserInfoResult;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
public interface JGIMFriendsService {

    UserInfoResult[] getFriendsInfo(String username) throws APIConnectionException, APIRequestException;

    ResponseWrapper addFriends(String username, String user) throws APIConnectionException, APIRequestException;

    ResponseWrapper deleteFriends(String username, String user) throws APIConnectionException, APIRequestException;

}
