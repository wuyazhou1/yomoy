package com.nsc.AmoskiUser.dao.impl;

import com.nsc.AmoskiUser.dao.RoleManageDao;
import com.nsc.Amoski.entity.TRoleEntity;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.AmoskiException;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RoleManageDaoImpl extends GenericRepositoryImpl implements RoleManageDao {
    @Override
    public List RoleManageList(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");
        if(params.get("name")!=null && !params.get("name").toString().trim().equals("")){
            str.append(" and ROLE_NAME like '%'|| :name ||'%'");
        }
        if(params.get("lockflag")!=null && !params.get("lockflag").toString().trim().equals("")){
            str.append(" and lockflag like '%'|| :lockflag ||'%'");
        }
        if(params.get("ispublic")!=null && !params.get("ispublic").toString().trim().equals("")){
            str.append(" and ispublic like '%'|| :ispublic ||'%'");
        }
        if(params.get("orgCode")!=null  && !params.get("orgCode").toString().trim().equals("")){
            str.append(" and exists (select id from (select id from t_department a start with id = :orgCode connect by prior id=parent_id) b where a.org_code=id )");
        }
        List list = this.jdbcTemplate.queryForList(pageSql("SELECT ID,CODE,ROLE_NAME,ISPUBLIC,(SELECT ORG_NAME FROM T_DEPARTMENT where id = a.org_code) ORG_NAME,ORG_CODE,LOCKFLAG,REMARK,CREATE_NAME,CREATE_DATE,UPDATE_NAME,UPDATE_DATE FROM T_ROLE a where 1=1" + str.toString() +" order by org_code",params), params);
        return list;
    }

    @Override
    public int UserManageCount(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");
        if(params.get("name")!=null && !params.get("name").toString().trim().equals("")){
            str.append(" and ROLE_NAME like '%'|| :name ||'%'");
        }
        if(params.get("lockflag")!=null && !params.get("lockflag").toString().trim().equals("")){
            str.append(" and lockflag like '%'|| :lockflag ||'%'");
        }
        if(params.get("ispublic")!=null && !params.get("ispublic").toString().trim().equals("")){
            str.append(" and ispublic like '%'|| :ispublic ||'%'");
        }
        if(params.get("orgCode")!=null  && !params.get("orgCode").toString().trim().equals("")){
            str.append(" and exists (select id from (select id from t_department a start with id = :orgCode connect by prior id=parent_id) b where a.org_code=id )");
        }
        int count = this.jdbcTemplate.queryForObject("select count(1) from t_role a  where 1=1 " + str.toString() , params , Integer.class);
        return count;
    }

    @Override
    public void addRole(TRoleEntity tRole) {
        this.save(tRole);
    }

    @Override
    public void updateRole(TRoleEntity tRole) {
        this.update(tRole);
    }

    @Override
    public void checkedRoleIsExists(String code,String id) {
        Map<String,Object> parent = new HashMap<String, Object>();
        parent.put("code",code);
        parent.put("id",id);
        StringBuffer sql = new StringBuffer("select count(1) from t_role where code = :code ");
        if(id != null && !id.trim().equals("")){
            sql.append(" and id != :id");
        }
        Integer count = this.jdbcTemplate.queryForObject(sql.toString(),parent,Integer.class);
        if(count>0){
            throw new AmoskiException("此角色代码已经存在");
        }
    }

    @Override
    public void RemoveTRole(String id) {
        this.jdbcTemplate.update("delete T_ROLE where id in ("+id+")",new HashMap());
    }

    @Override
    public void setRoleMenuResource(String role, String menu, String resource) {
        Map<String,Object> parentMap = new HashMap<String, Object>();
        parentMap.put("role",role);
        parentMap.put("menu",Arrays.asList((menu==null||menu.equals("")? "-1" :menu).split(",")));
        parentMap.put("resource",Arrays.asList((resource==null||resource.equals("")? "-1" :resource).split(",")));
        this.jdbcTemplate.update("delete T_ROLE_MENU_RESOURCE_REL where ROLE_CODE = :role " , parentMap);
        this.jdbcTemplate.update("insert into T_ROLE_MENU_RESOURCE_REL " +
                        "select '"+role.trim()+"' role_code,a.code menu_code,b.code resource_code  " +
                        "from (select code from t_menu where code in ( :menu )) a  " +
                        "left join (select code,menu_code from t_resource " +
                        "where code in ( :resource )) b on a.code=b.menu_code "
                , parentMap);
    }

    @Override
    public Object getRoleMenuResource(String role) {
        return this.jdbcTemplate.queryForList("select menu_code,res_code from T_ROLE_MENU_RESOURCE_REL  where role_code='"+role.trim()+"'  "
                , new HashMap());
    }

    @Override
    public Object getRoleImpowerUser(String role, String orgCode) {
        Map<String,Object> parentMap = new HashMap<String, Object>();
        parentMap.put("role",role);
        parentMap.put("orgCode",orgCode);
        List<String> list = this.jdbcTemplate.queryForList("select id from t_department a start with id = :orgCode connect by prior id=parent_id",parentMap, String.class);
        parentMap.put("orgCode",list);
        List<Map<String, Object>> resultList = this.jdbcTemplate.queryForList("select a.code , a.name , 'true' selected from t_user a  " +
                "where a.org_code in ( :orgCode )  " +
                "and exists (select * from t_role_user_rel b where a.code=b.user_code and b.role_code= :role ) ", parentMap);
        resultList.addAll(
            this.jdbcTemplate.queryForList("select a.code , a.name , 'false' selected from t_user a  " +
                    "where a.org_code in ( :orgCode )  " +
                    "and not exists (select * from t_role_user_rel b where a.code=b.user_code and b.role_code= :role ) ", parentMap)
        );
        return resultList;
    }

    @Override
    public void setRoleImpowerUser(String roleCode, String userCode) {
        this.jdbcTemplate.update("delete T_Role_user_rel where ROLE_CODE='"+roleCode+"'",new HashMap());
        this.jdbcTemplate.update("insert into T_Role_user_rel(role_code,user_code)  " +
                "SELECT '"+roleCode+"',REGEXP_SUBSTR('"+userCode+"','[^,]+',1,level,'i') AS STR  " +
                "FROM DUAL CONNECT BY  REGEXP_SUBSTR('"+userCode+"','[^,]+',1,level,'i')  " +
                "IS NOT NULL",new HashMap());
    }
}
