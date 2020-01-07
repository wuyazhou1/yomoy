package com.nsc.AmoskiUser.client;


import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TUserEntity;
import com.nsc.AmoskiUser.service.LoginUserService;
import com.nsc.AmoskiUser.service.UserRealmService;
import com.nsc.Amoski.util.AmoskiException;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
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
public class UserRealm extends AuthorizingRealm {
    private static final Log logger = LogFactory.getLog(UserRealm.class);

    @Autowired
    @Lazy
    private LoginUserService loginUserService;

    @Autowired
    @Lazy
    private UserRealmService userRealmService;



    @Override
    public String getName() {
        return "UserRealm";
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
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        AuthorizationInfo authorizationInfo = userRealmService.doGetAuthorizationInfo(shiroUser.getCode());

        long endTime = System.currentTimeMillis();
        logger.info("权限配置-->MyShiroRealm.doGetAuthorizationInfo()执行"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
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
        Map<String, Object> resultMap = loginUserService.findRequestShiro(token.getUsername(), user);
        logger.info("resultMap==>"+resultMap);
        if(resultMap == null ) {
            user = loginUserService.findUserByLoginName(token.getUsername());
            if(user!=null){
                resultMap = loginUserService.putFindRequestShiro(token.getHost(), user);
            }
        }

        logger.info("TUserEntity==>"+resultMap.get("TUserEntity"));
        if(null!=resultMap&&resultMap.size()>0){
            user = (TUserEntity) resultMap.get("TUserEntity");
        }

        if(user==null){
            throw new AmoskiException("此用户不存在");//没找到帐号
        }

        if(Boolean.TRUE.equals(user.getLocked())) {
            throw new AmoskiException("此用户以锁定");//没找到帐号
        }

        if (user != null) {
            logger.info("ShiroUser==>"+resultMap.get("ShiroUser"));
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