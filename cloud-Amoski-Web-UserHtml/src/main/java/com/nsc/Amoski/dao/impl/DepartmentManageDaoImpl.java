package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.DepartmentManageDao;
import com.nsc.Amoski.entity.TDepartmentEntity;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class DepartmentManageDaoImpl extends GenericRepositoryImpl implements DepartmentManageDao {



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
