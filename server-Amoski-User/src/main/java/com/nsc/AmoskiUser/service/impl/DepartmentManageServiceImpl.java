package com.nsc.AmoskiUser.service.impl;

import com.nsc.AmoskiUser.dao.DepartmentManageDao;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TDepartmentEntity;
import com.nsc.AmoskiUser.service.DepartmentManageService;
import com.nsc.Amoski.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(transactionManager = "UserTransactionManager")
public class DepartmentManageServiceImpl implements DepartmentManageService {

    @Autowired
    @Lazy
    private DepartmentManageDao departmentManageDao;

    @Override
    public PagingBean findDepartmentList(Map<String, Object> params) {
        List list = departmentManageDao.findDepartmentList(params);
        int count = departmentManageDao.findDepartmentCount(params);
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
    public void AddDepartment(TDepartmentEntity epartment) {
        departmentManageDao.AddDepartment(epartment);
    }

    @Override
    public void UpdateDepartment(TDepartmentEntity department) {
        departmentManageDao.UpdateDepartment(department);
    }

    @Override
    public void removeDepartment(String id) {
        departmentManageDao.removeDepartment(id);
    }

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
