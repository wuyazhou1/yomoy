package com.nsc.AmoskiUser.service.impl;

import com.nsc.AmoskiUser.client.ByteSourceUtils;
import com.nsc.AmoskiUser.client.RedisObjectEntity;
import com.nsc.AmoskiUser.dao.LoginUserDao;
import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TUserEntity;
import com.nsc.AmoskiUser.service.LoginUserService;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service

@Transactional(transactionManager = "UserTransactionManager")
public class LoginUserServiceImpl implements LoginUserService {

    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    private String algorithmName = "md5";
    private int hashIterations = 2;

    @Autowired
    @Lazy
    private LoginUserDao loginUserDao;

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf('findUserByLoginName-').concat(#loginName)")
    public TUserEntity findUserByLoginName(String loginName) {
        return loginUserDao.findUserByLoginName(loginName);
    }

    @Override
    public  HashMap<String,Object> findRequestShiro(String ipAddr, TUserEntity user) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("ShiroUser",loginUserDao.putIpAddrShiroUser(ipAddr));
        resultMap.put("TUserEntity",loginUserDao.putIpAddrTUserEntity(ipAddr));
        return (HashMap)resultMap;
    }

    @Override
    public HashMap<String,Object> putFindRequestShiro(String ipAddr, TUserEntity user) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        if(user==null){
            return null;
        }
        ShiroUser shiroUser = new ShiroUser(user);
        shiroUser.setSalt(randomNumberGenerator.nextBytes().toHex());
        String newPassword = new SimpleHash(
                algorithmName,//加密算法
                ipAddr,//密码
                ByteSourceUtils.bytes(shiroUser.getCredentialsSalt()),//盐值 username+salt
                hashIterations//hash次数
        ).toHex();
        shiroUser.setPassword(newPassword);
        resultMap.put("TUserEntity",loginUserDao.putIpAddrTUserEntity(ipAddr,user));
        resultMap.put("ShiroUser",loginUserDao.putIpAddrShiroUser(ipAddr,shiroUser));
        return (HashMap)resultMap;
    }

    @Override
    @CachePut(value = "String",key = "T(String).valueOf('finRoleCodeByUserCode-').concat(#code)")
    public List<String> finRoleCodeByUserCode(String code) {
        return loginUserDao.finRoleCodeByUserCode(code);
    }

    @Override
    @CachePut(value = "String",key = "T(String).valueOf('findMenuResourceByUserCode-').concat(#code)")
    public List<String> findMenuResourceByUserCode(String code) {
        return loginUserDao.findMenuResourceByUserCode(code);
    }

    @Override
    @CachePut(value = "String",key = "T(String).valueOf('findMenuResourceAll')")
    public List<String> findMenuResourceAll() {
        return loginUserDao.findMenuResourceAll();
    }

    @Override
    public Object putLoginToke(String ipAddr, Object request) {
        ShiroUser shiroUser = loginUserDao.putIpAddrShiroUser(ipAddr);
        RedisObjectEntity redis = (RedisObjectEntity)loginUserDao.putLoginToke( ipAddr,  shiroUser);
        //SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(redis.getShiroUser(), redis.getMd5Pwd() , ByteSource.Util.bytes(redis.getLoginName()), redis.getName());
        return null;
    }

    @Override
    public Object cacheaLoginToke(String ipAddr) {
        RedisObjectEntity redis = (RedisObjectEntity)loginUserDao.cacheaLoginToke( ipAddr);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(redis.getShiroUser(), redis.getMd5Pwd() , ByteSource.Util.bytes(redis.getLoginName()), redis.getName());
        return info;
    }

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf(#key).concat('getShiroRedisObject')")
    public Object getShiroRedisObject(Object key) {
        return null;
    }

    @Override
    @CachePut(value = "String",key = "T(String).valueOf(#key).concat('getShiroRedisObject')")
    public Object getShiroRedisObject(Object key, Object value) {
        return value;
    }

    @Override
    @CacheEvict(value = "String",key = "T(String).valueOf(#key).concat('getShiroRedisObject')")
    public Object delShiroRedisObject(Object key) {
        return null;
    }

    @Override
    @CachePut(value = "String",key = "T(String).valueOf('findMenuFrameListMap')")
    public List<Map<String, Object>> findMenuFrameListMap() {
        List<Map<String,Object>> listMap = loginUserDao.findMenuFrameListMap("");
        return listMap;
    }

}
