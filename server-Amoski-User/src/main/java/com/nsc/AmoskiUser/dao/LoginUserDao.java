package com.nsc.AmoskiUser.dao;

import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TUserEntity;

import java.util.List;
import java.util.Map;

public interface LoginUserDao {
    TUserEntity findUserByLoginName(String loginName);

    List<String> finRoleCodeByUserCode(String code);

    List<String> findMenuResourceByUserCode(String code);

    List<String> findMenuResourceAll();

    TUserEntity putIpAddrTUserEntity(String ipAddr, TUserEntity user);

    ShiroUser putIpAddrShiroUser(String ipAddr, ShiroUser shiroUser);

    TUserEntity putIpAddrTUserEntity(String ipAddr);

    ShiroUser putIpAddrShiroUser(String ipAddr);

    Object putLoginToke(String ipAddr, Object request);

    Object cacheaLoginToke(String ipAddr);

    List<Map<String, Object>> findMenuFrameListMap(String o);


}
