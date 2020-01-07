package com.nsc.AmoskiActivity.Dao.impl;

import com.nsc.AmoskiActivity.Dao.ReportingManageDao;
import com.nsc.AmoskiActivity.Util.GenericRepositoryActivityImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class ReportingManageDaoImpl extends GenericRepositoryActivityImpl implements ReportingManageDao {
    @Override
    public List ReportingList(Map<String, Object> params) {
        List list = this.jdbcTemplate.queryForList(pageSql("select * from (\n" +
                        "SELECT ID, REGEXP_SUBSTR(SUGGESTION_ID,'[^,]+',1,level,'i') SUGGESTION_ID, MEMBER_ID, REMAKE  " +
                        "FROM TB_USER_FEEDBACK CONNECT BY  REGEXP_SUBSTR(SUGGESTION_ID,'[^,]+',1,level,'i')  IS NOT NULL  " +
                        ") " +
                        "where SUGGESTION_ID = :suggestionId "
                 ,params), params);
        return list;
    }
    public String heavyLoadSql(String sql){
        sql = "select distinct ID,(select dict_key from amoski_user.t_dict where dict_type_code='100' and dict_value=a.SUGGESTION_ID) SUGGESTION_ID,\n" +
                "nvl((select name from amoski_user.t_member where id=a.MEMBER_ID),MEMBER_ID) MEMBER_ID,REMAKE \n" +
                "from ("+sql+") a";
        return sql;
    }

    @Override
    public int ReportingCount(Map<String, Object> params) {
        int count = this.jdbcTemplate.queryForObject("select count(1) from ( " +
                " SELECT ID, REGEXP_SUBSTR(SUGGESTION_ID,'[^,]+',1,level,'i') SUGGESTION_ID, MEMBER_ID, REMAKE " +
                " FROM TB_USER_FEEDBACK CONNECT BY  REGEXP_SUBSTR(SUGGESTION_ID,'[^,]+',1,level,'i')  IS NOT NULL " +
                " ) " +
                "where SUGGESTION_ID = :suggestionId " , params , Integer.class);
        return count;
    }
}
