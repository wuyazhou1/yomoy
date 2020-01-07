package com.nsc.AmoskiUser.service;


import com.nsc.Amoski.entity.PagingBean;

import java.util.Map;

public interface MemberManageService{
    PagingBean getMemberList(Map<String, Object> params);

    Object getMemberImpowerRole(String memberId, String orgCode);
}