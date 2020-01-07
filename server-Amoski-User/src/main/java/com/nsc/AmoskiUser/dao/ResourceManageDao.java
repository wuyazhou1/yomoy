package com.nsc.AmoskiUser.dao;

import com.nsc.Amoski.entity.TResourceEntity;

import java.util.List;
import java.util.Map;

public interface ResourceManageDao {
    List ResourceManageList(Map<String, Object> params);

    int ResourceManageCount(Map<String, Object> params);

    void AddMenuManage(TResourceEntity tResourceEntity);

    void updateMenuManage(TResourceEntity tResourceEntity);

    void deleteResourceManage(String id);

    void checkedResourceIsExists(String code, String id);
}
