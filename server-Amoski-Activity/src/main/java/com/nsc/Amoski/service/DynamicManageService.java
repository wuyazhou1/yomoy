package com.nsc.Amoski.service;

import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.parent.*;

public interface DynamicManageService {
    void releaseDynamic(ReleaseDynamicParent releaseDynamicParent);

    PagingBean queryDynamicList(ReleaseDynamicParent ReleaseDynamicParent, MemberView dto);

    DynamicCommentEntity dynamicComment(DynamicCommentParent dynamicCommentParent);

    void dynamicFabulous(DynamicSpotFabulousParent dynamicSpotFabulousParent);

    Integer queryObjectByEntityById(String s, Object dynamicSpotFabulousParent);

    void dynamicCollection(DynamicCollectionParent dynamicCollectionParent);

    void dynamicFansAttention(FansAttentionParent fansAttentionParent);

    PagingBean dynamicFollowList(FansAttentionParent FansAttentionParent);

    Integer updateObjectByEntityById(String s, Object obj);

    PagingBean dynamicFabulousList(DynamicSpotFabulousParent dynamicSpotFabulousParent);

    PagingBean dynamicCommentList(DynamicCommentParent dynamicCommentParent);

    void commentFabulous(DynamicSpotFabulousParent DynamicSpotFabulousParent);

    Object queryObjectByEntityByIdResult(String s, Object o);

    void deleteDynamic(ReleaseDynamicParent releaseDynamicParent);

    void dynamicShieldMember(DynamicShieldEntity dynamicShieldEntity);
}
