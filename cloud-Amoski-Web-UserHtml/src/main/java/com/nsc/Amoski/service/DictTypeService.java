package com.nsc.Amoski.service;

import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TDictTypeEntity;

import java.util.List;
import java.util.Map;

public interface DictTypeService {

    PagingBean RoleManageList(Map<String, Object> params);

    void AddDictType(TDictTypeEntity tDictTypeEntity);

    void UpdateDictType(TDictTypeEntity tDictTypeEntity);

    void DeleteDictType(String id);

    void CheckedDictType(String id, String code);

    List<Map> getZtreeDatail(String parentCode);
}
