package com.nsc.AmoskiUser.service;

import com.nsc.Amoski.entity.TUserEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface LoginUserService {
    TUserEntity findUserByLoginName(String loginName);

    HashMap<String,Object>  findRequestShiro(String ipAddr, TUserEntity user);

    HashMap<String,Object> putFindRequestShiro(String ipAddr, TUserEntity user);

    List<String> finRoleCodeByUserCode(String code);

    List<String> findMenuResourceByUserCode(String code);

    List<String> findMenuResourceAll();

    Object putLoginToke(String ipAddr, Object request);

    Object cacheaLoginToke(String ipAddr);

    Object getShiroRedisObject(Object key);


    Object getShiroRedisObject(Object key,Object value);

    Object delShiroRedisObject(Object id);

    List<Map<String,Object>> findMenuFrameListMap();

}
