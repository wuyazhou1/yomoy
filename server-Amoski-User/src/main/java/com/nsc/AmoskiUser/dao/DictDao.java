package com.nsc.AmoskiUser.dao;

import com.nsc.Amoski.entity.TDictEntity;

import java.util.List;
import java.util.Map;

public interface DictDao {

    List DictList(Map<String, Object> params);

    int DictCount(Map<String, Object> params);

    void AddDict(TDictEntity tDictEntity);

    void UpdateDict(TDictEntity tDictEntity);

    void DeleteDict(String code);

    void CheckedDict(String id, String code);

    Object GetDictZtree(String parentCode);
}
