package com.nsc.Amoski.service;

import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.parent.DynamicSpotFabulousParent;
import com.nsc.Amoski.parent.FansAttentionParent;
import com.nsc.Amoski.entity.PagingBeanQuery;

import java.util.Map;

public interface UserPersonalCenterService {
    Map<String, Object> PersonalCenterDatil(MemberView dto);

    Object queryDynamicList(PagingBeanQuery pagingBeanQuery);

    Object queryFollowList(FansAttentionParent fansAttentionParent);

    Object queryFansList(FansAttentionParent fansAttentionParent);

    Object queryFabulousList(DynamicSpotFabulousParent dynamicSpotFabulousParent);

    Object queryFabulousListTo(DynamicSpotFabulousParent dynamicSpotFabulousParent);

    Object dynamicCollectionList(MemberView dto);

}
