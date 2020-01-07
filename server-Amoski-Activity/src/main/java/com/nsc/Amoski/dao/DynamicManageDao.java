package com.nsc.Amoski.dao;

import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.parent.*;

import java.util.List;

public interface DynamicManageDao {
    ReleaseDynamicEntity saveEntityByReleaseDynamicEntity(ReleaseDynamicEntity releaseDynamicEntity);

    void saveObject(DynamicCallParent dynamicCallMeEntity);

    List<ReleaseDynamicParent> queryDynamicList(ReleaseDynamicParent ReleaseDynamicParent, MemberView dto);

    int queryDynamicCount(ReleaseDynamicParent ReleaseDynamicParent, MemberView dto);

    void insertSqlUpdate(String sql, Object releaseDynamicParent);

    void saveDynamicImageList(List<DynamicImageEntity> dynamicImageList,String dynamicId);

    void savePhotoPic(String s, List<DynamicImageEntity> dynamicImageList);

    void dynamicComment(DynamicCommentEntity dynamicCommentEntity);

    void bonusPointsDynamic(String dynamicId, String s);

    void dynamicFabulous(Object DynamicSpotFabulousEntity);

    Integer queryObjectByEntityById(String s, Object dynamicSpotFabulousParent);

    void dynamicCollection(DynamicCollectionEntity dynamicCollectionEntity);

    void dynamicFansAttention(FansAttentionEntity fansAttentionEntity);

    List dynamicFollowList(FansAttentionParent FansAttentionParent);

    int dynamicFollowCount(FansAttentionParent FansAttentionParent);

    Integer updateObjectByEntityById(String sql, Object obj);

    List dynamicFabulousList(DynamicSpotFabulousParent dynamicSpotFabulousParent);

    int dynamicFabulousCount(DynamicSpotFabulousParent dynamicSpotFabulousParent);

    List dynamicCommentList(DynamicCommentParent dynamicCommentParent);

    int dynamicCommentCount(DynamicCommentParent dynamicCommentParent);

    void saveObjectEntity(Object obj);

    Object queryObjectByEntityByIdResult(String s, Object o);

    void deleteDynamic(ReleaseDynamicParent releaseDynamicParent);

    void dynamicShieldMember(DynamicShieldEntity dynamicShieldEntity);

}
