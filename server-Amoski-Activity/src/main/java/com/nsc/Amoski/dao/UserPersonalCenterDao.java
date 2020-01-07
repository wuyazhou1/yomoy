package com.nsc.Amoski.dao;

import com.nsc.Amoski.parent.DynamicCollectionParent;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;

public interface UserPersonalCenterDao {
    Integer queryDynamicCount(SqlParameterSource dto);

    Integer queryFollowCount(SqlParameterSource dto);

    Integer queryFansCount(SqlParameterSource dto);

    Integer queryFabulousCount(SqlParameterSource dto);

    List queryDynamicList(SqlParameterSource dto);


    List queryFollowList(SqlParameterSource parameter);

    List queryFansList(SqlParameterSource parameter);

    List queryFabulousList(SqlParameterSource parameter);

    List<DynamicCollectionParent> dynamicCollectionList(SqlParameterSource parameter);

    Integer dynamicCollectionCount(SqlParameterSource parameter);

    List queryFabulousListTo(SqlParameterSource parameter);

    Object queryBePraisedCount(SqlParameterSource parameter);
}
