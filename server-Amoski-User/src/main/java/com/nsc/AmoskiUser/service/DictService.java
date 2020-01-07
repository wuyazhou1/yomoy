package com.nsc.AmoskiUser.service;

import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TDictEntity;

import java.util.Map;

public interface DictService {
    PagingBean DictList(Map<String, Object> params);

    void AddDict(TDictEntity tDictEntity);

    void UpdateDict(TDictEntity tDictEntity);

    void DeleteDict(String code);

    void CheckedDict(String id, String code);

    Object GetDictZtree(String parentCode);
}
