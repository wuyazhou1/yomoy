package com.nsc.AmoskiUser.service.impl;

import com.nsc.AmoskiUser.dao.UserManageDao;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TUserEntity;
import com.nsc.AmoskiUser.service.UserManageService;
import com.nsc.Amoski.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service

@Transactional(transactionManager = "UserTransactionManager")
public class UserManageServiceImpl implements UserManageService {

    @Autowired
    @Lazy
    public UserManageDao userManageDao;


    @Override
    public PagingBean UserManageList(Map<String, Object> params) {
        List list = userManageDao.UserManageList(params);
        int count = userManageDao.UserManageCount(params);
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
    public void addTuser(TUserEntity tUserEntity) {
        userManageDao.addTuser(tUserEntity);
    }

    @Override
    public void updateTuser(TUserEntity tUserEntity) {
        userManageDao.updateTuser(tUserEntity);
    }

    @Override
    public void RemoveTuser(String id) {
        userManageDao.RemoveTuser(id);
    }

    @Override
    public Object UserImpowerRole(String userCode, String orgCode) {
        return userManageDao.UserImpowerRole(userCode,orgCode);
    }

    @Override
    public void setUserImpowerRole(String userCode, String roleCode) {
        userManageDao.setUserImpowerRole(userCode,roleCode);
    }

    @Override
    public void checkedUserCodeByCode(String code,String id) {
        userManageDao.checkedUserCodeByCode(code,id);
    }
}
