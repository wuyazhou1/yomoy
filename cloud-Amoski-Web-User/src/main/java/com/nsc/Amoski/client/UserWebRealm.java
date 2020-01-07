package com.nsc.Amoski.client;


import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TUserEntity;
import com.nsc.Amoski.service.LoginNameCheckedService;
import com.nsc.Amoski.service.UserWebRealmService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Auther: cpb
 * @Date: 2018/8/10 10:36
 * @Description:
 */
@Component("UserRealm")
public class UserWebRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(UserWebRealm.class);

    @Autowired
    @Lazy
    private LoginNameCheckedService loginNameCheckedService;



    @Autowired
    @Lazy
    private UserWebRealmService userWebRealmService;

    @Override
    public String getName() {
        return "UserWebRealm";
    }

    /**
     * 仅支持StatelessToken 类型的Token，
     * 那么如果在StatelessAuthcFilter类中返回的是UsernamePasswordToken，那么将会报如下错误信息：
     * Please ensure that the appropriate Realm implementation is configured correctly or
     * that the realm accepts AuthenticationTokens of this type.StatelessAuthcFilter.isAccessAllowed()
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordTokenSerializable;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        long starTime = System.currentTimeMillis();
        /*if(1==1)
            throw new UnknownError("测试调用地址");*/
        String codes = userWebRealmService.getShiroUserCodeByPrincipals(principals.getPrimaryPrincipal());

        AuthorizationInfo authorizationInfo = userWebRealmService.doGetAuthorizationInfo(codes);
        long endTime = System.currentTimeMillis();
        log.info("权限配置-->MyShiroRealm.doGetAuthorizationInfo()执行"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return authorizationInfo;
    }

    @Override//存入对象缓存数据
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {

        System.out.println("身份验证-->MyShiroRealm.doGetAuthorizationInfo()");
        UsernamePasswordTokenSerializable token = (UsernamePasswordTokenSerializable) authcToken;
        String username = token.getUsername();
        String password = new String((char[])token.getCredentials());
        TUserEntity  user = null;
        ShiroUser shiroUser = null;
        Map<String, Object> resultMap = loginNameCheckedService.findRequestShiro(token.getUsername());

        if(null!=resultMap&&resultMap.size()>0){
            user = (TUserEntity) resultMap.get("TUserEntity");
        }

        if(user==null){
            throw new UnknownAccountException("此用户不存在");//没找到帐号
        }

        if(Boolean.TRUE.equals(user.getLocked())) {
            throw new UnknownAccountException("此用户以锁定");//没找到帐号
        }

        if (user != null) {
            shiroUser = (ShiroUser) resultMap.get("ShiroUser");
            String md5Pwd = new Md5Hash(password,username).toHex();
            AuthenticationInfo info = new SimpleAuthenticationInfo(shiroUser, md5Pwd , ByteSourceUtils.bytes(username), getName());
            return info;
        } else {
            return null;
        }
    }


    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}