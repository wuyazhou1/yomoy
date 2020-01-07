package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dto.MemberDto;
import com.nsc.Amoski.dto.RegPhoneRandomNumDto;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TUserEntity;
import com.nsc.Amoski.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);
    /**
     * 手机验证码存在就写入 否则返回
     * @param redisKey
     * @param codeObj
     * @return
     */
    @Cacheable(value="mobileValidCode" ,key = "'key_'+#redisKey")
    public RegPhoneRandomNumDto getTelChecked(String redisKey, RegPhoneRandomNumDto codeObj){
        log.info("getTelChecked mobileValidCode insert redis >>>>>>>redisKey:"+redisKey+"======codeObj:"+codeObj);
        return codeObj;
    }


    /**
     * 手机验证码对象写入
     * @param redisKey
     * @param codeObj
     * @return
     */
    @CachePut(value="mobileValidCode", key = "'key_'+#redisKey")
    public RegPhoneRandomNumDto saveMobileValidCode(String redisKey, RegPhoneRandomNumDto codeObj){
        log.info("saveMobileValidCode mobileValidCode  insert redis >>>>>>>redisKey:"+redisKey+"======codeObj:"+codeObj);
        return codeObj;
    }

    /**
     * 用户对象存在就写入 否则返回
     * @param redisKey
     * @param codeObj
     * @return
     */
    @Cacheable(value="user" ,key = "'key_'+#redisKey")
    public MemberView getRedisUserObj(String redisKey, MemberView codeObj){
        log.info("getRedisUserObj >>>>>>>redisKey:"+redisKey+"======codeObj:"+codeObj);
        return codeObj;
    }

    /**
     * 用户对象写入
     * @param redisKey
     * @param codeObj
     * @return
     */
    @CachePut(value="user", key = "'key_'+#redisKey")
    public MemberView setRedisUserObj(String redisKey, MemberView codeObj){
        log.info("setRedisUserObj >>>>>>>redisKey:"+redisKey+"======codeObj:"+codeObj);
        return codeObj;
    }

    /**
     * 用户清除缓存
     * @param redisKey
     * @return allEntries = true
     */
    @CacheEvict(value="user", key = "'key_'+#redisKey")
    public void clearRedisUserObj(String redisKey){
        log.info("clearRedisUserObj >>>>>>>redisKey:"+redisKey+"======");
    }


    /**
     * 存放单个redis 值
     * @param redisKey
     * @param obj
     * @return
     */
    @Cacheable(value="singValue#7200" ,key = "'key_'+#redisKey")
    public Object getSingValue(String redisKey, Object obj){
        log.info("insert singValue redis >>>>>>>redisKey:"+redisKey+"======codeObj:"+obj);
        return obj;
    }

    /**
     * 用户对象写入
     * @param redisKey
     * @param obj
     * @return
     */
    @CachePut(value="singValue", key = "'key_'+#redisKey")
    public Object setSingValue(String redisKey, Object obj){
        log.info("insert singValue redis >>>>>>>redisKey:"+redisKey+"======codeObj:"+obj);
        return obj;
    }

    @Cacheable(value = "String",key = "T(String).valueOf(#ipAddr)")
    public ShiroUser findRequestShiro(String ipAddr, MemberView user) {
        if(user==null){
            return null;
        }
        return new ShiroUser(user);
    }


    /**
     * 后台登陆状态拦截
     * @param ipAddr
     * @return
     */
    @Cacheable(value = "String",key = "T(String).valueOf(#ipAddr).concat('-ShiroUser')")
    public ShiroUser putIpAddrShiroUser(String ipAddr) {
        return null;
    }
}








































