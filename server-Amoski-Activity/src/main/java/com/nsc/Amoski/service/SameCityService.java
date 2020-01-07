package com.nsc.Amoski.service;

import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.PagingBean;

import java.util.Map;

public interface SameCityService {
    Map<String,Object> sameCityData(MemberView dto);

    Map<String, Object> knightHomePage(MemberView memberView);

    PagingBean getFabulousList(MemberView memberView);

    Integer queryFabulousByMemberView(MemberView dto, MemberView memberView);
}
