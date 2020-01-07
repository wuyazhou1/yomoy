package com.nsc.AmoskiUser.service.impl;

import com.nsc.AmoskiUser.service.LoginUserService;
import com.nsc.AmoskiUser.service.UserRealmService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@Transactional(transactionManager = "UserTransactionManager")
public class UserRealmServiceImpl implements UserRealmService {

    @Autowired
    @Lazy
    private LoginUserService loginUserService;

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf(#code).concat('-doGetAuthorizationInfo')")
    public AuthorizationInfo doGetAuthorizationInfo(String code) {
        Set<String> role = new HashSet<String>();
        Set<String> permission = new HashSet<String>();
        role.addAll(loginUserService.finRoleCodeByUserCode(code));
        if(role.contains("admin")){
            permission.addAll(loginUserService.findMenuResourceAll());
        }else{
            permission.addAll(loginUserService.findMenuResourceByUserCode(code));
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(role);
        authorizationInfo.setStringPermissions(permission);
        return authorizationInfo;
    }

    @Override
    @CachePut(value = "String",key = "T(String).valueOf(#code).concat('-doGetAuthorizationInfo')")
    public AuthorizationInfo putDoGetAuthorizationInfo(String code) {

        Set<String> role = new HashSet<String>();
        Set<String> permission = new HashSet<String>();
        //查询菜单框架
        List<Map<String, Object>> menuFrameListMap = loginUserService.findMenuFrameListMap();
        role.addAll(loginUserService.finRoleCodeByUserCode(code));
        if(role.contains("admin")){
            permission.addAll(loginUserService.findMenuResourceAll());
        }else{
            permission.addAll(loginUserService.findMenuResourceByUserCode(code));
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(role);
        authorizationInfo.setStringPermissions(permission);
        return authorizationInfo;
    }
}
