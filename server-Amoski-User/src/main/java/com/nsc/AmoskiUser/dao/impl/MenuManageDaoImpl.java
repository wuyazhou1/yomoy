package com.nsc.AmoskiUser.dao.impl;

import com.nsc.AmoskiUser.dao.MenuManageDao;
import com.nsc.Amoski.entity.TMenuEntity;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.AmoskiException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MenuManageDaoImpl extends GenericRepositoryImpl implements MenuManageDao {
    @Override
    public List MenuManageList(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");
        if(params.get("menuName")!=null && !params.get("menuName").toString().trim().equals("")){
            str.append(" and MENU_NAME like '%'|| :menuName ||'%'");
        }
        if(params.get("parentCode")!=null && !params.get("parentCode").toString().trim().equals("")){
            str.append(" and PARENT_CODE = :parentCode ");
        }
        List list = this.jdbcTemplate.queryForList(pageSql("SELECT ID,CODE,(select MENU_NAME from T_MENU b where b.code=a.parent_code) PARENT_NAME,PARENT_CODE,MENU_NAME,ORDER_CODE,MENU_URL,CREATE_NAME,CREATE_DATE,UPDATE_NAME,UPDATE_DATE FROM T_MENU a where 1=1" + str.toString() +" order by ORDER_CODE",params), params);
        return list;
    }

    @Override
    public int MenuManageCount(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");
        if(params.get("menuName")!=null && !params.get("menuName").toString().trim().equals("")){
            str.append(" and MENU_NAME like '%'|| :menuName ||'%'");
        }
        if(params.get("parentCode")!=null && !params.get("parentCode").toString().trim().equals("")){
            str.append(" and PARENT_CODE = :parentCode ");
        }
        int count = this.jdbcTemplate.queryForObject("select count(1) from T_MENU a  where 1=1 " + str.toString() , params , Integer.class);
        return count;
    }

    @Override
    public void AddMenuManage(TMenuEntity tMenuEntity) {
        this.save(tMenuEntity);
    }

    @Override
    public void UpdateMenuManage(TMenuEntity tMenuEntity) {
        this.update(tMenuEntity);
    }

    @Override
    public void RemoveTMenu(String id) {
        this.jdbcTemplate.update("delete T_MENU where id in ("+id+")",new HashMap());
    }

    @Override
    public Object ztreeTMenu() {
        return this.jdbcTemplate.queryForList(" select code \"id\",menu_name \"name\",parent_code \"pId\",case when (select count(1) from t_menu b where b.parent_code=a.code)>0 then 'true' else 'false' end \"isParent\" from (select * from t_menu a start with nvl(parent_code,'-1')='-1' connect by prior code=parent_code) a",new HashMap());
    }

    @Override
    public void checkedRoleIsExists(String code, String id) {
        Map<String,Object> parent = new HashMap<String, Object>();
        parent.put("code",code);
        parent.put("id",id);
        StringBuffer sql = new StringBuffer("select count(1) from t_menu where code = :code ");
        if(id != null && !id.trim().equals("")){
            sql.append(" and id != :id");
        }
        Integer count = this.jdbcTemplate.queryForObject(sql.toString(),parent,Integer.class);
        if(count>0){
            throw new AmoskiException("此角色代码已经存在");
        }
    }


}
