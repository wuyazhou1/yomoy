package com.nsc.Amoski.dao;

import com.nsc.Amoski.entity.TDictEntity;

import java.util.List;
import java.util.Map;

public interface DictDao {

    List DictList(Map<String, Object> params);

    int DictCount(Map<String, Object> params);

    void CheckedDict(String id, String code);

    Object GetDictZtree(String parentCode);
}
