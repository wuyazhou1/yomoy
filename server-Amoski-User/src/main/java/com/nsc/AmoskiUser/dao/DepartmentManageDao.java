package com.nsc.AmoskiUser.dao;

import com.nsc.Amoski.entity.TDepartmentEntity;

import java.util.List;
import java.util.Map;

public interface DepartmentManageDao {

    List findDepartmentList(Map<String, Object> params);

    int findDepartmentCount(Map<String, Object> params);

    void AddDepartment(TDepartmentEntity epartment);

    void UpdateDepartment(TDepartmentEntity department);

    void removeDepartment(String id);

    List<Map> getUserZtreeDatail(String id, String loginIdentification);

    List<Map> getMemberZtreeDatail(String id, String loginIdentification);
}
