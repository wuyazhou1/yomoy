package com.nsc.AmoskiUser.dao.impl;

import com.nsc.AmoskiUser.dao.DictDao;
import com.nsc.Amoski.entity.TDictEntity;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.AmoskiException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DictDaoImpl extends GenericRepositoryImpl implements DictDao {

    @Override
    public List DictList(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");
        if(params.get("dictKey")!=null && !params.get("dictKey").toString().trim().equals("")){
            str.append(" and dict_Key like '%'|| :dictKey ||'%'");
        }
        List list = this.jdbcTemplate.queryForList(pageSql("select * from t_dict where dict_Type_Code = :dictTypeCode  "
                + str.toString() ,params), params);
        return list;
    }

    @Override
    public int DictCount(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");
        if(params.get("dictKey")!=null && !params.get("dictKey").toString().trim().equals("")){
            str.append(" and dict_Key like '%'|| :dictKey ||'%'");
        }
        int count = this.jdbcTemplate.queryForObject("select count(1) from t_dict where dict_Type_Code = :dictTypeCode  " + str.toString() , params , Integer.class);
        return count;
    }

    @Override
    public void AddDict(TDictEntity tDictEntity) {
        this.save(tDictEntity);
    }

    @Override
    public void UpdateDict(TDictEntity tDictEntity) {
        this.update(tDictEntity);
    }

    @Override
    public void DeleteDict(String code) {
        this.jdbcTemplate.update("delete T_DICT where code in ("+code+")",new HashMap());
    }

    @Override
    public void CheckedDict(String id, String code) {
        Map<String,Object> parent = new HashMap<String, Object>();
        parent.put("code",code);
        parent.put("id",id);
        StringBuffer sql = new StringBuffer("select count(1) from T_DICT where code = :code ");
        if(id != null && !id.trim().equals("")){
            sql.append(" and id != :id");
        }
        Integer count = this.jdbcTemplate.queryForObject(sql.toString(),parent,Integer.class);
        if(count>0){
            throw new AmoskiException("此角色代码已经存在");
        }
    }

    @Override
    public Object GetDictZtree(String parentCode) {
        HashMap<String, String> parent = new HashMap<String, String>();
        if(parentCode != null && !parentCode.trim().equals(""))
            parent.put("parentCode",parentCode);
        else
            parent.put("parentCode","-1");
        List list = this.jdbcTemplate.queryForList("select code \"id\",dict_name \"name\",parent_code \"pId\" from t_dict_type a start with nvl(parent_code,'-1')= :parentCode  connect by prior code=parent_code"  , parent);
        list.addAll(
            this.jdbcTemplate.queryForList(
                "select dict_value \"id\",dict_key \"name\",dict_type_code \"pId\" from t_dict a where exists ( " +
                        "select * from (select code from t_dict_type a start with code= :parentCode  connect by prior code=parent_code) b " +
                        "where b.code=a.dict_type_code)"  , parent)
        );
        return list;
    }
}
