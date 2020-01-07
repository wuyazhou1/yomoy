package com.nsc.AmoskiUser.service;

import org.apache.shiro.authz.AuthorizationInfo;

public interface UserRealmService {
    AuthorizationInfo doGetAuthorizationInfo(String code);

    AuthorizationInfo putDoGetAuthorizationInfo(String code);

}
