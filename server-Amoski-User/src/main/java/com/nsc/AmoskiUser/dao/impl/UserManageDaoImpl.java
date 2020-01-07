package com.nsc.AmoskiUser.dao.impl;

import com.nsc.AmoskiUser.dao.UserManageDao;
import com.nsc.Amoski.entity.TUserEntity;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.AmoskiException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserManageDaoImpl  extends GenericRepositoryImpl implements UserManageDao {
    @Override
    public List UserManageList(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");
        if(params.get("name")!=null && !params.get("name").toString().trim().equals("")){
            str.append(" and NAME like '%'|| :name ||'%'");
        }
        if(params.get("orgCode")!=null  && !params.get("orgCode").toString().trim().equals("")){
            str.append(" and exists (select id from (select id from t_department a start with id = :orgCode connect by prior id=parent_id) b where a.org_code=id )");
        }
        List list = this.jdbcTemplate.queryForList(pageSql("select ID,CODE,NAME,LOGINNAME,PASSWORD,SALT,LOCKED,ORG_CODE,(SELECT ORG_NAME FROM T_DEPARTMENT where id = a.org_code) ORG_NAME,REMARK,CREATE_NAME,CREATE_DATE,UPDATE_NAME,UPDATE_DATE from T_USER a  where 1=1 " + str.toString() +" order by org_code",params), params);
        return list;
    }

    @Override
    public int UserManageCount(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");
        if(params.get("name")!=null && !params.get("name").toString().trim().equals("")){
            str.append(" and NAME like '%'|| :name ||'%'");
        }
        if(params.get("orgCode")!=null  && !params.get("orgCode").toString().trim().equals("")){
            str.append(" and exists (select id from (select id from t_department a start with id = :orgCode connect by prior id=parent_id) b where a.org_code=id )");
        }
        int count = this.jdbcTemplate.queryForObject("select count(1) from T_USER a  where 1=1 " + str.toString() , params , Integer.class);
        return count;
    }

    @Override
    public void addTuser(TUserEntity tUserEntity) {
        this.save(tUserEntity);
    }

    @Override
    public void updateTuser(TUserEntity tUserEntity) {
        this.update(tUserEntity);
    }

    @Override
    public void RemoveTuser(String id) {
        this.jdbcTemplate.update("delete T_USER where id in ("+id+")", new HashMap());
    }



    @Override
    public Object UserImpowerRole(String userCode, String orgCode) {
        Map<String,Object> parentMap = new HashMap<String, Object>();
        parentMap.put("userCode",userCode);
        parentMap.put("orgCode",orgCode);
        List<String> list = this.jdbcTemplate.queryForList("select id from t_department a start with id = :orgCode connect by prior id=parent_id",parentMap, String.class);
        parentMap.put("orgCode",list);
        List<Map<String, Object>> resultList = this.jdbcTemplate.queryForList("select a.code , a.ROLE_NAME name , 'true' selected from T_ROLE a  " +
                "where a.org_code in ( :orgCode )  " +
                "and exists (select * from t_role_user_rel b where a.code=b.ROLE_CODE and b.USER_CODE= :userCode ) ", parentMap);
        resultList.addAll(
                this.jdbcTemplate.queryForList("select a.code , a.ROLE_NAME name , 'false' selected from T_ROLE a  " +
                        "where a.org_code in ( :orgCode )  " +
                        "and not exists (select * from t_role_user_rel b where a.code=b.ROLE_CODE and b.USER_CODE= :userCode ) ", parentMap)
        );
        return resultList;
    }

    @Override
    public void setUserImpowerRole( String userCode,String roleCode) {
        this.jdbcTemplate.update("delete T_Role_user_rel where USER_CODE='"+userCode+"'",new HashMap());
        this.jdbcTemplate.update("insert into T_Role_user_rel(role_code,user_code)  " +
                "SELECT REGEXP_SUBSTR('"+roleCode+"','[^,]+',1,level,'i') AS STR,'"+userCode+"' userCode  " +
                "FROM DUAL CONNECT BY  REGEXP_SUBSTR('"+roleCode+"','[^,]+',1,level,'i')  " +
                "IS NOT NULL",new HashMap());
    }

    @Override
    public void checkedUserCodeByCode(String code,String id) {
        Map<String , String > parentMap = new HashMap<String, String>();
        parentMap.put("code",code);
        parentMap.put("id",id);
        List<Map<String, Object>> resultList = this.jdbcTemplate.queryForList("select * from T_User where code = :code and nvl( id , '-999')!=nvl( :id ,'-999')", parentMap);
        if(resultList.size()>0){
            throw new AmoskiException("此用户代码已经存在");
        }
    }


}
