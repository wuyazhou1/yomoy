package com.nsc.AmoskiUser.service.impl;

import com.nsc.AmoskiUser.dao.MenuManageDao;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TMenuEntity;
import com.nsc.AmoskiUser.service.MenuManageService;
import com.nsc.Amoski.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service

@Transactional(transactionManager = "UserTransactionManager")
public class MenuManageServiceImpl  implements MenuManageService {


    @Autowired
    @Lazy
    public MenuManageDao menuManageDao;

    @Override
    public PagingBean MenuManageList(Map<String, Object> params) {
        List list = menuManageDao.MenuManageList(params);
        int count = menuManageDao.MenuManageCount(params);
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
    public void AddMenuManage(TMenuEntity tMenuEntity) {
        menuManageDao.AddMenuManage(tMenuEntity);
    }

    @Override
    public void UpdateMenuManage(TMenuEntity tMenuEntity) {
        menuManageDao.UpdateMenuManage(tMenuEntity);
    }

    @Override
    public void RemoveTMenu(String id) {
        menuManageDao.RemoveTMenu(id);
    }

    @Override
    public Object ztreeTMenu() {
        return menuManageDao.ztreeTMenu();
    }

    @Override
    public void checkedRoleIsExists(String code, String id) {
        menuManageDao.checkedRoleIsExists(code,id);
    }
}
