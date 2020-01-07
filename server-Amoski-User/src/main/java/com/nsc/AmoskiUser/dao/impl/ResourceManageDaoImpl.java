package com.nsc.AmoskiUser.dao.impl;

import com.nsc.AmoskiUser.dao.ResourceManageDao;
import com.nsc.Amoski.entity.TResourceEntity;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.AmoskiException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ResourceManageDaoImpl extends GenericRepositoryImpl implements ResourceManageDao {
    @Override
    public List ResourceManageList(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");
        if(params.get("menuCode")!=null && !params.get("menuCode").toString().trim().equals("")){
            str.append(" or a.menu_code = :menuCode ");
        }
        List list = this.jdbcTemplate.queryForList(pageSql("select (select MENU_NAME from t_menu b where b.code=a.menu_code) MENU_NAME,a.menu_code,a.ID,a.CODE,a.RES_NAME,a.RES_TYPE,a.REMARK,a.CREATE_NAME,a.CREATE_DATE,a.UPDATE_NAME,a.UPDATE_DATE from t_resource a  where 1!=1 " + str.toString() ,params), params);
        return list;
    }

    @Override
    public int ResourceManageCount(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");
        if(params.get("menuCode")!=null && !params.get("menuCode").toString().trim().equals("")){
            str.append(" or a.menu_code = :menuCode ");
        }
        int count = this.jdbcTemplate.queryForObject("select count(1) from t_resource a   where 1!=1 " + str.toString() , params , Integer.class);
        return count;
    }

    @Override
    public void AddMenuManage(TResourceEntity tResourceEntity) {
        this.save(tResourceEntity);
    }

    @Override
    public void updateMenuManage(TResourceEntity tResourceEntity) {
        this.update(tResourceEntity);
    }

    @Override
    public void deleteResourceManage(String id) {
        this.jdbcTemplate.update("delete T_RESOURCE where id in("+id+")",new HashMap());
    }

    @Override
    public void checkedResourceIsExists(String code, String id) {
        Map<String,Object> parent = new HashMap<String, Object>();
        parent.put("code",code);
        parent.put("id",id);
        StringBuffer sql = new StringBuffer("select count(1) from t_resource where code = :code ");
        if(id != null && !id.trim().equals("")){
            sql.append(" and id != :id");
        }
        Integer count = this.jdbcTemplate.queryForObject(sql.toString(),parent,Integer.class);
        if(count>0){
            throw new AmoskiException("此元素代码已经存在");
        }
    }


}
