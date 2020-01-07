package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

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
     * 存放单个redis 值
     * @param redisKey
     * @param obj
     * @return  key = "T(String).valueOf(#principals).concat('-getShiroUserCodeByPrincipals')"
     */
    @Cacheable(value = "String" ,key = "T(String).valueOf('ridingTeam_').concat(#redisKey)")
    public Object getSingValue(String redisKey, Object obj){
        log.info("insert singValue redis >>>>>>>redisKey:"+redisKey+"======codeObj:"+obj);
        return obj;
    }

    /**
     * 存放单个redis 值
     * @param redisKey
     * @param obj
     * @return
     */
    @CachePut(value = "String", key = "T(String).valueOf('ridingTeam_').concat(#redisKey)")
    public Object setSingValue(String redisKey, Object obj){
        log.info("insert singValue redis >>>>>>>redisKey:"+redisKey+"======codeObj:"+obj);
        return obj;
    }
}








































