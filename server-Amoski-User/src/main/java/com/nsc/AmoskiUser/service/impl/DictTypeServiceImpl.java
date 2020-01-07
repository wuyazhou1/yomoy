package com.nsc.AmoskiUser.service.impl;


import com.nsc.AmoskiUser.dao.DictTypeDao;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TDictTypeEntity;
import com.nsc.AmoskiUser.service.DictTypeService;
import com.nsc.Amoski.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service

@Transactional(transactionManager = "UserTransactionManager")
public class DictTypeServiceImpl implements DictTypeService {

    @Autowired
    @Lazy
    public DictTypeDao dictTypeDao;

    @Override
    public PagingBean RoleManageList(Map<String, Object> params) {
        List list = dictTypeDao.RoleManageList(params);
        int count = dictTypeDao.UserManageCount(params);
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
    public void AddDictType(TDictTypeEntity tDictTypeEntity) {
        dictTypeDao.AddDictType(tDictTypeEntity);
    }

    @Override
    public void UpdateDictType(TDictTypeEntity tDictTypeEntity) {
        dictTypeDao.UpdateDictType(tDictTypeEntity);
    }

    @Override
    public void DeleteDictType(String id) {
        dictTypeDao.DeleteDictType(id);
    }

    @Override
    public void CheckedDictType(String id, String code) {
        dictTypeDao.CheckedDictType(id,code);
    }

    @Override
    public List<Map> getZtreeDatail(String parentCode) {
        return dictTypeDao.getZtreeDatail(parentCode);
    }
}
