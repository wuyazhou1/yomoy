package com.nsc.AmoskiUser.service.impl;

import com.nsc.AmoskiUser.dao.RoleManageDao;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TRoleEntity;
import com.nsc.AmoskiUser.service.RoleManageService;
import com.nsc.Amoski.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service

@Transactional(transactionManager = "UserTransactionManager")
public class RoleManageServiceImpl implements RoleManageService {
    @Autowired
    @Lazy
    public RoleManageDao roleManageDao;

    @Override
    public PagingBean RoleManageList(Map<String, Object> params) {
        List list = roleManageDao.RoleManageList(params);
        int count = roleManageDao.UserManageCount(params);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        if (StringUtils.isEmpty(params.get("draw")))
            pagelist.setDraw(0);
        else
            pagelist.setDraw(Integer.valueOf(params.get("draw").toString()));
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }

    @Override
    public void addRole(TRoleEntity tRole) {
        roleManageDao.addRole(tRole);
    }

    @Override
    public void updateRole(TRoleEntity tRole) {
        roleManageDao.updateRole(tRole);
    }

    @Override
    public void checkedRoleIsExists(String code,String id) {
        roleManageDao.checkedRoleIsExists(code,id);
    }

    @Override
    public void RemoveTRole(String id) {
        roleManageDao.RemoveTRole(id);
    }

    @Override
    public void setRoleMenuResource(String role, String menu, String resource) {
        roleManageDao.setRoleMenuResource(role,menu,resource);
    }

    @Override
    public Object getRoleMenuResource(String role) {
        return roleManageDao.getRoleMenuResource(role);
    }

    @Override
    public Object getRoleImpowerUser(String role, String orgCode) {
        return roleManageDao.getRoleImpowerUser(role,orgCode);
    }

    @Override
    public void setRoleImpowerUser(String roleCode, String userCode) {
        roleManageDao.setRoleImpowerUser(roleCode,userCode);
    }

}
