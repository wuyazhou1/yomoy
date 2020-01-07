package com.nsc.Amoski.service.im.impl;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.common.model.RegisterInfo;
import cn.jmessage.api.common.model.UserPayload;
import cn.jmessage.api.user.UserInfoResult;
import cn.jmessage.api.user.UserStateResult;
import com.nsc.Amoski.dao.im.JGIMUserDao;
import com.nsc.Amoski.entity.IdWorker;
import com.nsc.Amoski.entity.TJGIMMemberEntity;
import com.nsc.Amoski.entity.jg.JGIMUser;
import com.nsc.Amoski.service.im.JGIMUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
@Service
public class JGIMUserServiceImpl implements JGIMUserService {

    @Autowired
    private JMessageClient jMessageClient;
    @Autowired
    private JGIMUserDao jgimUserDao;
    @Autowired
    private IdWorker idWorker;

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
    public boolean registerUsers(Integer member_id, String username, String password) throws APIConnectionException, APIRequestException {
        TJGIMMemberEntity tjgimMemberEntity = jgimUserDao.findByUsername(new TJGIMMemberEntity(null, null, username, null, null));
        if (!StringUtils.isEmpty(tjgimMemberEntity)) return false;
        ArrayList<RegisterInfo> registerInfos = new ArrayList<>();
        RegisterInfo.Builder builder = RegisterInfo.newBuilder();
        builder.setUsername(username);
        builder.setPassword(password);
        registerInfos.add(builder.build());
        jMessageClient.registerUsers(registerInfos.toArray(new RegisterInfo[]{}));
        jgimUserDao.insert(new TJGIMMemberEntity(idWorker.nextId() + "", member_id, username, password, null));
        return true;
    }

    /**
     * 查询用户信息
     *
     * @param username
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public UserInfoResult getUserInfo(String username) throws APIConnectionException, APIRequestException {
        UserInfoResult userInfoResult = jMessageClient.getUserInfo(username);
        return userInfoResult;
    }

    /**
     * 查询用户在线状态
     *
     * @param username
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    public UserStateResult getUserStatus(String username) throws APIConnectionException, APIRequestException {
        UserStateResult userStateResult = jMessageClient.getUserState(username);
        return userStateResult;
    }

    /**
     * 删除用户
     *
     * @param username
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public boolean deleteUser(String username) throws APIConnectionException, APIRequestException {
        TJGIMMemberEntity tjgimMemberEntity = jgimUserDao.findByUsername(new TJGIMMemberEntity(null, null, username, null, null));
        if (StringUtils.isEmpty(tjgimMemberEntity)) {
            return false;
        }
        jMessageClient.deleteUser(tjgimMemberEntity.getUsername());
        jgimUserDao.deleteById(new TJGIMMemberEntity(tjgimMemberEntity.getId(), null, null, null, null));
        return true;
    }

    /**
     * 更新用户信息
     *
     * @param jgimUser
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public void updateUserInfo(JGIMUser jgimUser) throws APIConnectionException, APIRequestException {
        UserPayload.Builder builder = UserPayload.newBuilder();
        BeanUtils.copyProperties(jgimUser, builder);
        UserPayload userPayload = builder.build();
        jMessageClient.updateUserInfo(jgimUser.getUsername(), userPayload);
    }

    /**
     * 修改密码
     *
     * @param username
     * @param password
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public boolean updateUserPassword(String username, String password) throws APIConnectionException, APIRequestException {
        TJGIMMemberEntity tjgimMemberEntity = jgimUserDao.findByUsername(new TJGIMMemberEntity(null, null, username, null, null));
        if (StringUtils.isEmpty(tjgimMemberEntity)) {
            return false;
        }
        jMessageClient.updateUserPassword(tjgimMemberEntity.getUsername(), password);
        tjgimMemberEntity.setPassword(password);
        jgimUserDao.byIdUpdatePassword(tjgimMemberEntity);
        return true;
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
    @Override
    public ResponseWrapper forbidUser(String username, boolean disable) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jMessageClient.forbidUser(username, disable);
        return responseWrapper;
    }

}
