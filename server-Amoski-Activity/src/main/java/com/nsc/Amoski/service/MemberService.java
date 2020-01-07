package com.nsc.Amoski.service;


import com.nsc.Amoski.dto.MemberDto;
import com.nsc.Amoski.dto.RegPhoneRandomNumDto;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TUserEntity;
import org.springframework.cache.annotation.Cacheable;

public interface MemberService {
    public RegPhoneRandomNumDto getTelChecked(String redisKey, RegPhoneRandomNumDto codeObj);

    public RegPhoneRandomNumDto saveMobileValidCode(String redisKey, RegPhoneRandomNumDto codeObj);

    public MemberView getRedisUserObj(String redisKey, MemberView codeObj);

    public MemberView setRedisUserObj(String redisKey, MemberView codeObj);

    public Object getSingValue(String redisKey, Object obj);

    public Object setSingValue(String redisKey, Object obj);

    public ShiroUser findRequestShiro(String ipAddr, MemberView user);

    public void clearRedisUserObj(String redisKey);

    /**
     * 后台登陆状态拦截
     * @param ipAddr
     * @return
     */
    public ShiroUser putIpAddrShiroUser(String ipAddr);
}