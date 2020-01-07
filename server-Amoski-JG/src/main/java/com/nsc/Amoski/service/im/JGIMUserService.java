package com.nsc.Amoski.service.im;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.user.UserInfoResult;
import cn.jmessage.api.user.UserStateResult;
import com.nsc.Amoski.entity.jg.JGIMUser;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
public interface JGIMUserService {

    boolean registerUsers(Integer member_id, String username, String password) throws APIConnectionException, APIRequestException;

    UserInfoResult getUserInfo(String username) throws APIConnectionException, APIRequestException;

    UserStateResult getUserStatus(String username) throws APIConnectionException, APIRequestException;

    boolean deleteUser(String username) throws APIConnectionException, APIRequestException;

    void updateUserInfo(JGIMUser jgimUser) throws APIConnectionException, APIRequestException;

    boolean updateUserPassword(String username, String password) throws APIConnectionException, APIRequestException;

    ResponseWrapper forbidUser(String username, boolean disable) throws APIConnectionException, APIRequestException;

}
