package com.nsc.AmoskiUser.dao.impl;


import com.nsc.AmoskiUser.dao.MemberManageDao;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class MemberManageDaoImpl  extends GenericRepositoryImpl implements MemberManageDao {

    @Override
    public List findDepartmentList(Map<String, Object> params) {
        StringBuffer memberWhere = new StringBuffer(" where 1=1 ");
        if(!StringUtils.isEmpty(params.get("orgCode")))
            memberWhere.append(" and a.ORG_CODE = :orgCode " );
        if(!StringUtils.isEmpty(params.get("name")))
            memberWhere.append(" and a.name like  '%'|| :name ||'%'" );
        if(!StringUtils.isEmpty(params.get("tel")))
            memberWhere.append(" and a.tel like '%'|| :tel ||'%'" );
        if(!StringUtils.isEmpty(params.get("address")))
            memberWhere.append(" and c.address like '%'|| :address ||'%'" );
        List list = this.jdbcTemplate.queryForList(pageSql("select " +
                "a.ID ,a.NAME ,a.LOGINNAME ,a.PASSWORD ,a.SALT ,a.TEL ,a.LOCKED," +
                "a.ORG_CODE ,a.REMARK ,a.BINDING_IDENTIFICATION ,a.LOGIN_IDENTIFICATION ,a.UPDATE_NAME," +
                "a.UPDATE_DATE,b.ID wx_Id,b.MEMBER_ID,b.OPEN_ID,b.SUBSCRIBE,b.SUBSCRIBE_TIME,b.NICKNAME," +
                "b.SEX,b.COUNTRY,b.PROVINCE,b.CITY,b.LANGUAGE,b.HEAD_IMG_URL,c.ID dail_Id,c.MEMBER_ID member_Dail_Id," +
                "c.IDENTITY_CARD ,c.SEX member_sex,c.HEAD_IMG_PROJECT,c.HEAD_IMG_FILE ,c.YEAR_OF_BIRTH ,c.ADDRESS ," +
                "c.ISATTESTATION,c.SYNOPSIS ,c.REAL_NAME " +
                " from  T_MEMBER  a " +
                "left join T_WEIXIN  b on a.id=b.member_id " +
                "left join T_MEMBER_DAIL c on a.id=c.member_id "
                + memberWhere ,params) , params);
        return list;
    }
    public String selectColumn(){
        return "ID ,NAME ,LOGINNAME ,PASSWORD ,SALT ,TEL ,LOCKED," +
                "(select ORG_NAME from t_department where id=w.ORG_CODE) ORG_CODE " +
                ",REMARK ,BINDING_IDENTIFICATION ,LOGIN_IDENTIFICATION ,UPDATE_NAME,UPDATE_DATE,wx_Id," +
                "MEMBER_ID,OPEN_ID,SUBSCRIBE,SUBSCRIBE_TIME,NICKNAME,SEX,COUNTRY,PROVINCE,CITY,LANGUAGE,HEAD_IMG_URL,dail_Id," +
                "member_Dail_Id,IDENTITY_CARD ,member_sex,HEAD_IMG_PROJECT,HEAD_IMG_FILE ,YEAR_OF_BIRTH ,ADDRESS ," +
                "ISATTESTATION,SYNOPSIS ,REAL_NAME" ;
    }

    @Override
    public Integer findDepartmentCount(Map<String, Object> params) {
        StringBuffer memberWhere = new StringBuffer(" where 1=1 ");
        if(!StringUtils.isEmpty(params.get("orgCode")))
            memberWhere.append(" and a.ORG_CODE = :orgCode " );
        if(!StringUtils.isEmpty(params.get("name")))
            memberWhere.append(" and a.name like  '%'|| :name ||'%'" );
        if(!StringUtils.isEmpty(params.get("tel")))
            memberWhere.append(" and a.tel like '%'|| :tel ||'%'" );
        if(!StringUtils.isEmpty(params.get("address")))
            memberWhere.append(" and c.address like '%'|| :address ||'%'" );
        Integer count = this.jdbcTemplate.queryForObject("select count(a.id) from T_MEMBER  a " +
                "left join T_WEIXIN  b on a.id=b.member_id " +
                "left join T_MEMBER_DAIL c on a.id=c.member_id "
                + memberWhere  , params , Integer.class);
        return count;
    }

    @Override
    public Object getMemberImpowerRole(String memberId, String orgCode) {
        Map<String,Object> parentMap = new HashMap<String, Object>();
        parentMap.put("memberId",memberId);
        parentMap.put("orgCode",orgCode);
        List<String> list = this.jdbcTemplate.queryForList("select id from t_department a start with id = :orgCode connect by prior id=parent_id",parentMap, String.class);
        parentMap.put("orgCode",list);
        List<Map<String, Object>> resultList = this.jdbcTemplate.queryForList("   select a.code , a.role_name name, 'true' selected from t_role a  \n" +
                "  where a.org_code in ( :orgCode ) \n" +
                "  and exists (select * from T_ROLE_MEMBER_REL b where a.code=b.role_code and b.member_code= :memberId  ) ", parentMap);
        resultList.addAll(
                this.jdbcTemplate.queryForList("   select a.code , a.role_name name, 'false' selected from t_role a  \n" +
                        "  where a.org_code in ( :orgCode ) \n" +
                        "  and not exists (select * from T_ROLE_MEMBER_REL b where a.code=b.role_code and b.member_code= :memberId  ) ", parentMap)
        );
        return resultList;
    }
}
