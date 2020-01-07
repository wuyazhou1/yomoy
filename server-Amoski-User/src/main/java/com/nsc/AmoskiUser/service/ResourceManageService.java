package com.nsc.AmoskiUser.service;

import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TResourceEntity;

import java.util.Map;

public interface ResourceManageService {
    PagingBean ResourceManageList(Map<String, Object> params);

    void AddMenuManage(TResourceEntity tResourceEntity);

    void updateMenuManage(TResourceEntity tResourceEntity);

    void deleteResourceManage(String id);

    void checkedResourceIsExists(String code, String id);
}
