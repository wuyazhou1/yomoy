package com.nsc.Amoski.dao;

import com.nsc.Amoski.entity.TDepartmentEntity;

import java.util.List;
import java.util.Map;

public interface DepartmentManageDao {

    List<Map> getUserZtreeDatail(String id, String loginIdentification);

    List<Map> getMemberZtreeDatail(String id, String loginIdentification);
}
