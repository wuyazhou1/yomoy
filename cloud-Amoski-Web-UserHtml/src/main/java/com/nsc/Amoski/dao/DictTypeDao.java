package com.nsc.Amoski.dao;

import com.nsc.Amoski.entity.TDictTypeEntity;

import java.util.List;
import java.util.Map;

public interface DictTypeDao {
    List RoleManageList(Map<String, Object> params);

    int UserManageCount(Map<String, Object> params);

    void AddDictType(TDictTypeEntity tDictTypeEntity);

    void UpdateDictType(TDictTypeEntity tDictTypeEntity);

    void DeleteDictType(String id);

    void CheckedDictType(String id, String code);

    List<Map> getZtreeDatail(String parentCode);
}
