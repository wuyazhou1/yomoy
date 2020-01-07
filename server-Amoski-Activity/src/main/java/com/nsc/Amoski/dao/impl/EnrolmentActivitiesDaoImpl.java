package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.EnrolmentActivitiesDao;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class EnrolmentActivitiesDaoImpl  extends GenericRepositoryImpl implements EnrolmentActivitiesDao {
    @Override
    public List<String> queryActivityListParent(String id) {
        Map<String,String> map = new HashMap<>();
        map.put("code",id);
        List<String> strings = this.jdbcTemplate.queryForList("select REGEXP_SUBSTR(MANDATORY_FIELD,'[^,]+',1,level,'i') from (\n" +
                "select MANDATORY_FIELD  from TB_ACTIVITY_ORDINATION \n" +
                "where basics_id in (select id from TB_ACTIVITY_BASICS where code = :code )\n" +
                ") CONNECT BY  REGEXP_SUBSTR(MANDATORY_FIELD,'[^,]+',1,level,'i')  IS NOT NULL",
                map, String.class);
        return strings;
    }
}
