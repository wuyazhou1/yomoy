package com.nsc.Amoski.dao;

import com.nsc.Amoski.entity.MemberDto;
import com.nsc.Amoski.entity.MemberView;

import java.util.List;

public interface SameCityDao {
    void updateMemberYXAxis(MemberView memberView);

    List queryMemberByYXAxis(MemberView memberView);

    List<MemberDto> queryMemberByMemberName(MemberView memberView);

    Integer queryMemberByMemberNameCount(MemberView memberView);

    List queryMemberRankingList(MemberView dto);

    void saveBackgroundImages(MemberView dto);

}
