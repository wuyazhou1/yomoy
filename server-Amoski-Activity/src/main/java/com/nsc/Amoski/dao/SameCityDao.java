package com.nsc.Amoski.dao;

import com.nsc.Amoski.entity.MemberView;

import java.util.List;

public interface SameCityDao {
    List getFabulousList(MemberView memberView);

    int getFabulousCount(MemberView memberView);

    Integer queryFabulousByMemberView(MemberView dto, MemberView memberView);
}
