package com.nsc.Amoski.service;


import com.nsc.Amoski.entity.MemberView;

public interface MemberService {
    public MemberView getRedisUserObj(String redisKey, MemberView codeObj);

    public MemberView setRedisUserObj(String redisKey, MemberView codeObj);

    public Object getSingValue(String redisKey, Object obj);

    public Object setSingValue(String redisKey, Object obj);
}