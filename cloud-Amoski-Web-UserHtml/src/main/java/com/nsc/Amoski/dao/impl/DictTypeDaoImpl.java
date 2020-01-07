package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.DictTypeDao;
import com.nsc.Amoski.entity.TDictTypeEntity;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.AmoskiException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DictTypeDaoImpl extends GenericRepositoryImpl implements DictTypeDao {

    @Override
    public List RoleManageList(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");
        if(params.get("dictName")!=null && !params.get("dictName").toString().trim().equals("")){
            str.append(" and dict_Name like '%'|| :dictName ||'%'");
        }
        List list = this.jdbcTemplate.queryForList(pageSql("SELECT (select DICT_NAME from t_dict_type where code=a.parent_code) PARENT_NAME,ID,CODE,PARENT_CODE,DICT_NAME,REMARK,CREATE_NAME,CREATE_DATE,UPDATE_NAME,UPDATE_DATE FROM t_dict_type a where (parent_Code= :parentCode or code= :parentCode ) " + str.toString() ,params), params);
        return list;
    }

    @Override
    public int UserManageCount(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");
        if(params.get("dictName")!=null && !params.get("dictName").toString().trim().equals("")){
            str.append(" and dict_Name like '%'|| :dictName ||'%'");
        }
        int count = this.jdbcTemplate.queryForObject("select count(1) from t_dict_type a  where (parent_Code= :parentCode or code= :parentCode )" + str.toString() , params , Integer.class);
        return count;
    }

    @Override
    public void AddDictType(TDictTypeEntity tDictTypeEntity) {
        this.save(tDictTypeEntity);
    }

    @Override
    public void UpdateDictType(TDictTypeEntity tDictTypeEntity) {
        this.update(tDictTypeEntity);
    }

    @Override
    public void DeleteDictType(String id) {
        int count = this.jdbcTemplate.queryForObject("select count(1) from t_dict_type a where a.code not in ("+id+") and a.parent_code in ("+id+")"
                , new HashMap() , Integer.class);
        if(count>0){
            throw new AmoskiException("请删除该类型的下级字典类型和下级字典");
        }
        this.jdbcTemplate.update("delete T_DICT_TYPE where code in ("+id+")",new HashMap());
    }

    @Override
    public void CheckedDictType(String id, String code) {
        Map<String,Object> parent = new HashMap<String, Object>();
        parent.put("code",code);
        parent.put("id",id);
        StringBuffer sql = new StringBuffer("select count(1) from T_DICT_TYPE where code = :code ");
        if(id != null && !id.trim().equals("")){
            sql.append(" and id != :id");
        }
        Integer count = this.jdbcTemplate.queryForObject(sql.toString(),parent,Integer.class);
        if(count>0){
            throw new AmoskiException("此角色代码已经存在");
        }
    }

    @Override
    public List<Map> getZtreeDatail(String parentCode) {
        HashMap<String, String> parent = new HashMap<String, String>();
        if(parentCode != null && !parentCode.trim().equals(""))
            parent.put("parentCode",parentCode);
        else
            parent.put("parentCode","-1");
        List list = this.jdbcTemplate.queryForList("select code \"id\",dict_name \"name\",parent_code \"pId\" from t_dict_type a start with nvl(parent_code,'-1')= :parentCode  connect by prior code=parent_code"  , parent);
        return list;
    }
}
