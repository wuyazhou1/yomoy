package com.nsc.Amoski.service.im.impl;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.user.UserListResult;
import com.nsc.Amoski.service.im.JGIMAdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
@Service
public class JGIMAdminUserServiceImpl implements JGIMAdminUserService {

    @Autowired
    private JMessageClient jMessageClient;

    /**
     * 注册管理员
     *
     * @param username
     * @param password
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public String registerAdmins(String username, String password) throws APIConnectionException, APIRequestException {
        String s = jMessageClient.registerAdmins(username, password);
        return s;
    }

    /**
     * 获取管理员列表
     *
     * @param start
     * @param count
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public UserListResult getAdminListByAppkey(int start, int count) throws APIConnectionException, APIRequestException {
        UserListResult userListResult = jMessageClient.getAdminListByAppkey(start, count);
        return userListResult;
    }

}
