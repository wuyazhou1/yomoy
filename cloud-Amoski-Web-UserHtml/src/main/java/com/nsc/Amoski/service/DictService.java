package com.nsc.Amoski.service;

import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TDictEntity;

import java.util.Map;

public interface DictService {
    PagingBean DictList(Map<String, Object> params);

    void CheckedDict(String id, String code);

    Object GetDictZtree(String parentCode);
}
