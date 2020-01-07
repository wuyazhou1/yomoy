package com.nsc.AmoskiUser.service;

import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TRoleEntity;

import java.util.Map;

public interface RoleManageService {
    PagingBean RoleManageList(Map<String, Object> params);

    void addRole(TRoleEntity tRole);

    void updateRole(TRoleEntity tRole);

    void checkedRoleIsExists(String code,String id);

    void RemoveTRole(String id);

    void setRoleMenuResource(String role, String menu, String resource);

    Object getRoleMenuResource(String role);

    Object getRoleImpowerUser(String role, String orgCode);

    void setRoleImpowerUser(String roleCode, String userCode);

}
