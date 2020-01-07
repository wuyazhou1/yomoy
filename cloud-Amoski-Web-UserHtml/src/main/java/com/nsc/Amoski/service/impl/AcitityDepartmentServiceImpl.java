package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.AcitityDepartmentDao;
import com.nsc.Amoski.service.AcitityDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 李阳
 * @date 2019/12/25 17:39
 */
@Service
public class AcitityDepartmentServiceImpl implements AcitityDepartmentService {

    @Autowired
    private AcitityDepartmentDao acitityDepartmentDao;

    /**
     * 根据俱乐部名称模糊查询
     *
     * @param orgName
     * @return
     */
    @Override
    public List<Map<String, Object>> findByOrgName(String orgName) {
        List<Map<String, Object>> byOrgName = acitityDepartmentDao.findByOrgName(orgName);
        return byOrgName == null || byOrgName.size() == 0 ? null : byOrgName;
    }

}
