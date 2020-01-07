package com.nsc.AmoskiUser.service;

import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TDepartmentEntity;

import java.util.List;
import java.util.Map;

public interface DepartmentManageService {

    PagingBean findDepartmentList(Map<String, Object> params);

    void AddDepartment(TDepartmentEntity epartment);

    void UpdateDepartment(TDepartmentEntity department);

    void removeDepartment(String id);

    List<Map> getZtreeDatail(String id, String loginIdentification);

}
