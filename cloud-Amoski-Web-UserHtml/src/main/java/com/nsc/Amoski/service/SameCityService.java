package com.nsc.Amoski.service;

import com.nsc.Amoski.entity.MemberDto;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.PagingBean;

import java.util.List;
import java.util.Map;

public interface SameCityService {
    void updateMemberYXAxis(MemberView memberView);

    List queryMemberByYXAxis(MemberView memberView);

    PagingBean<MemberDto> queryMemberByMemberName(MemberView memberView);

    List queryMemberRanking(MemberView dto);

    void saveBackgroundImages(MemberView dto);
}
