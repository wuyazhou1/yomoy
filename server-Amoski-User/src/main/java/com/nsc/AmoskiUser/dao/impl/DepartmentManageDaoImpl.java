package com.nsc.AmoskiUser.dao.impl;

import com.nsc.AmoskiUser.dao.DepartmentManageDao;
import com.nsc.Amoski.entity.TDepartmentEntity;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class DepartmentManageDaoImpl extends GenericRepositoryImpl implements DepartmentManageDao {



    @Override
    public List findDepartmentList(Map<String, Object> params) {


        StringBuffer str = new StringBuffer("");
        if(params.get("name")!=null){
            str.append(" where ORG_NAME like '%"+params.get("name")+"%'");
        }
        if(params.get("parentId")!=null){
            if(str.toString().trim().equals("")){
                str.append(" where (PARENT_ID = '"+params.get("parentId")+"' or  id = '"+params.get("parentId")+"')");
            }else{
                str.append(" and (PARENT_ID = '"+params.get("parentId")+"' or  id = '"+params.get("parentId")+"')");
            }
        }
        List list = this.jdbcTemplate.queryForList(pageSql("select * from (select * from t_department a start with id='0' connect by prior id=parent_id) " + str.toString() ,params), params);
        return list;
    }

    public String selectColumn(){
        return "ID,ORG_NAME," +
                "(select ORG_NAME from t_department where id=w.PARENT_ID) PARENT_ID" +
                ",REMARK,CREATE_NAME,CREATE_DATE,UPDATE_NAME,UPDATE_DATE";
    }

    @Override
    public int findDepartmentCount(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");
        if(params.get("name")!=null){
            str.append(" where ORG_NAME like '%"+params.get("name")+"%'");
        }
        if(params.get("parentId")!=null){
            if(str.toString().trim().equals("")){
                str.append(" where (PARENT_ID = '"+params.get("parentId")+"' or  id = '"+params.get("parentId")+"')");
            }else{
                str.append(" and (PARENT_ID = '"+params.get("parentId")+"' or  id = '"+params.get("parentId")+"')");
            }
        }
        return this.jdbcTemplate.queryForObject("select count(1) from (select * from t_department a start with id='0' connect by prior id=parent_id) " + str.toString() , params,Integer.class);
    }

    @Override
    public void AddDepartment(TDepartmentEntity epartment) {
        this.save(epartment);
    }

    @Override
    public void UpdateDepartment(TDepartmentEntity department) {
        this.update(department);
    }

    @Override
    public void removeDepartment(String id) {
        this.jdbcTemplate.update("delete T_DEPARTMENT where id in ("+id+")", new HashMap());
    }

    @Override
    public List<Map> getUserZtreeDatail(String id, String loginIdentification) {
        HashMap<String, String> parent = new HashMap<String, String>();
        parent.put("id",id);
        parent.put("loginIdentification",loginIdentification);
        System.out.println("select id \"id\",org_name \"name\",parent_id \"pId\" from (select * from t_department a start with id in (select org_code from t_user where id= '"+id+"' ) connect by prior id=parent_id) ");
        List list = this.jdbcTemplate.queryForList("select id \"id\",org_name \"name\",parent_id \"pId\" " +
                "from (select * from t_department a start with  nvl(parent_id,'-999')=nvl( :id ,'-999') connect by prior id=parent_id) " +
                "union all " +
                "select id \"id\",org_name \"name\",parent_id \"pId\"  from t_department where id= :id"  , parent);
        return list;
    }

    @Override
    public List<Map> getMemberZtreeDatail(String id, String loginIdentification) {
        HashMap<String, String> parent = new HashMap<String, String>();
        parent.put("id",id);
        parent.put("loginIdentification",loginIdentification);
        List list = this.jdbcTemplate.queryForList("select id \"id\",org_name \"name\",parent_id \"pId\",case when (select count(1) from t_department b where b.parent_id=a.id)>0 then 'true' else 'false' end \"isParent\"   " +
                "from (select * from t_department a start with " +
                "(nvl(id,'-999')=nvl( :id ,'-999') or nvl(parent_id,'-999')=nvl( :id ,'-999')) connect by prior id=parent_id)"  , parent);
        return list;
    }
}
