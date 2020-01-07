package com.nsc.Amoski.service;

import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TDepartmentEntity;

import java.util.List;
import java.util.Map;

public interface DepartmentManageService {


    List<Map> getZtreeDatail(String id, String loginIdentification);

}
