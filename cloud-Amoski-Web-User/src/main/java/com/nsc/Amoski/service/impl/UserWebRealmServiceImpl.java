package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.service.LoginNameCheckedService;
import com.nsc.Amoski.service.UserWebRealmService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;


@Service
public class UserWebRealmServiceImpl implements UserWebRealmService {
    private static final Logger log = LoggerFactory.getLogger(UserWebRealmServiceImpl.class);

    @Autowired
    @Lazy
    private LoginNameCheckedService loginNameCheckedService;


    @Override
    @Cacheable(value = "String",key = "T(String).valueOf(#code).concat('-doGetAuthorizationInfo')")
    public AuthorizationInfo doGetAuthorizationInfo(String code) {
        log.info("进入redis获取shiro实体类方法");
        Set<String> role = new HashSet<String>();
        Set<String> permission = new HashSet<String>();
        role.addAll(loginNameCheckedService.finRoleCodeByUserCode(code));
        if(role.contains("admin")){
            permission.addAll(loginNameCheckedService.findMenuResourceAll());
        }else{
            permission.addAll(loginNameCheckedService.findMenuResourceByUserCode(code));
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(role);
        authorizationInfo.setStringPermissions(permission);

        return authorizationInfo;
    }

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf(#principals).concat('-getShiroUserCodeByPrincipals')")
    public String getShiroUserCodeByPrincipals(Object principals) {
        log.info("进入redis获取授权方法方法");
        System.out.println("");
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut;
        try {
            objOut = new ObjectOutputStream(byteOut);
            objOut.writeObject(principals);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClassLoader classLoader = this.getClass().getClassLoader();
        Converter<byte[], Object> deserializer = new DeserializingConverter(classLoader);
        ShiroUser shiroUser = (ShiroUser) deserializer.convert(byteOut.toByteArray());
        //ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        return shiroUser.getCode();
    }
}
