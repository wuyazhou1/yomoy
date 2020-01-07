package com.nsc.AmoskiUser.service.impl;

import com.nsc.AmoskiUser.dao.ResourceManageDao;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TResourceEntity;
import com.nsc.AmoskiUser.service.ResourceManageService;
import com.nsc.Amoski.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service

@Transactional(transactionManager = "UserTransactionManager")
public class ResourceManageServiceImpl implements ResourceManageService {

    @Autowired
    @Lazy
    public ResourceManageDao resourceManageDao;

    @Override
    public PagingBean ResourceManageList(Map<String, Object> params) {
        List list = resourceManageDao.ResourceManageList(params);
        int count = resourceManageDao.ResourceManageCount(params);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        if (StringUtils.isEmpty(params.get("draw")))
            pagelist.setDraw(0);
        else
            pagelist.setDraw(Integer.valueOf(params.get("draw").toString()));
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }

    @Override
    public void AddMenuManage(TResourceEntity tResourceEntity) {
        resourceManageDao.AddMenuManage(tResourceEntity);
    }

    @Override
    public void updateMenuManage(TResourceEntity tResourceEntity) {
        resourceManageDao.updateMenuManage(tResourceEntity);
    }

    @Override
    public void deleteResourceManage(String id) {
        resourceManageDao.deleteResourceManage(id);
    }

    @Override
    public void checkedResourceIsExists(String code, String id) {
        resourceManageDao.checkedResourceIsExists(code,id);
    }
}
