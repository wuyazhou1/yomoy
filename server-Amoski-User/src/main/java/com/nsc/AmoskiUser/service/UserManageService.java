package com.nsc.AmoskiUser.service;

import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TUserEntity;

import java.util.Map;

public interface UserManageService {

    PagingBean UserManageList(Map<String, Object> params);


    void addTuser(TUserEntity tUserEntity);


    void updateTuser(TUserEntity tUserEntity);

    void RemoveTuser(String id);

    Object UserImpowerRole(String userCode, String orgCode);

    void setUserImpowerRole(String userCode, String roleCode);

    void checkedUserCodeByCode(String code,String id);
}
