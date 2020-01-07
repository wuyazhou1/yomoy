package com.nsc.Amoski.service.im;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jmessage.api.user.UserListResult;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
public interface JGIMAdminUserService {

    String registerAdmins(String username, String password) throws APIConnectionException, APIRequestException;

    UserListResult getAdminListByAppkey(int start, int count) throws APIConnectionException, APIRequestException;

}
