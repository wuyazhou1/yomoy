package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.DepartmentManageDao;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TDepartmentEntity;
import com.nsc.Amoski.service.DepartmentManageService;
import com.nsc.Amoski.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DepartmentManageServiceImpl implements DepartmentManageService {

    @Autowired
    @Lazy
    private DepartmentManageDao departmentManageDao;


    @Override
    public List<Map> getZtreeDatail(String id,String loginIdentification) {
        List<Map> returnMap = null;
        if ("0".equals(loginIdentification)) {//用户员工
            returnMap = departmentManageDao.getUserZtreeDatail(id, loginIdentification);
        } else if ("1".equals(loginIdentification)) {//会员用户
            returnMap = departmentManageDao.getMemberZtreeDatail(id, loginIdentification);
        }
        return returnMap;
    }
}
