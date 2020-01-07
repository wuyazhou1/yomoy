package com.nsc.AmoskiUser.dao;

import com.nsc.Amoski.entity.TMenuEntity;

import java.util.List;
import java.util.Map;

public interface MenuManageDao {
    List MenuManageList(Map<String, Object> params);

    int MenuManageCount(Map<String, Object> params);

    void AddMenuManage(TMenuEntity tMenuEntity);

    void UpdateMenuManage(TMenuEntity tMenuEntity);

    void RemoveTMenu(String id);

    Object ztreeTMenu();

    void checkedRoleIsExists(String code, String id);
}
