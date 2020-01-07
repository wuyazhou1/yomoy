package com.nsc.AmoskiUser.dao;

import com.nsc.Amoski.entity.TUserEntity;

import java.util.List;
import java.util.Map;

public interface UserManageDao {
    List UserManageList(Map<String, Object> params);

    int UserManageCount(Map<String, Object> params);

    void addTuser(TUserEntity tUserEntity);

    void updateTuser(TUserEntity tUserEntity);

    void RemoveTuser(String id);

    Object UserImpowerRole(String userCode, String orgCode);

    void setUserImpowerRole(String userCode, String roleCode);

    void checkedUserCodeByCode(String code,String id);
}
