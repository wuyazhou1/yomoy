package com.nsc.Amoski.service;


import com.nsc.Amoski.entity.TUserEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface LoginNameCheckedService {
    HashMap<String,Object> findRequestShiro(String ipAddr);

    TUserEntity findUserByLoginName(String username);

    List<String> findMenuResourceByUserCode(String code);

    List<String> findMenuResourceAll();

    Collection<? extends String> finRoleCodeByUserCode(String code);

    Object putLoginToke(String toString, Object toString1);

    List<Map<String, Object>> findMenuFrameListMap();

    List<Map<String, Object>> findMenuByMenuFrameListMap(List<Map<String, Object>> parentMap);

    public Object getShiroRedisObject(Object key);

}