package com.nsc.AmoskiUser.service;

import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TMenuEntity;

import java.util.Map;

public interface MenuManageService {
    PagingBean MenuManageList(Map<String, Object> params);

    void AddMenuManage(TMenuEntity tMenuEntity);

    void UpdateMenuManage(TMenuEntity tMenuEntity);

    void RemoveTMenu(String id);

    Object ztreeTMenu();

    void checkedRoleIsExists(String code, String id);
}
