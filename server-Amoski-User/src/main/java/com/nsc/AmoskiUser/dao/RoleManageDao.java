package com.nsc.AmoskiUser.dao;

import com.nsc.Amoski.entity.TRoleEntity;

import java.util.List;
import java.util.Map;

public interface RoleManageDao {
    List RoleManageList(Map<String, Object> params);

    int UserManageCount(Map<String, Object> params);

    void addRole(TRoleEntity tRole);

    void updateRole(TRoleEntity tRole);

    void checkedRoleIsExists(String code,String id);

    void RemoveTRole(String id);

    void setRoleMenuResource(String role, String menu, String resource);

    Object getRoleMenuResource(String role);

    Object getRoleImpowerUser(String role, String orgCode);

    void setRoleImpowerUser(String roleCode, String userCode);
    
}
