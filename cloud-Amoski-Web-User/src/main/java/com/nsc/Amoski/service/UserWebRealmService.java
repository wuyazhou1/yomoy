package com.nsc.Amoski.service;

import org.apache.shiro.authz.AuthorizationInfo;

public interface UserWebRealmService {
    AuthorizationInfo doGetAuthorizationInfo(String code);

    String getShiroUserCodeByPrincipals(Object principals);
}
