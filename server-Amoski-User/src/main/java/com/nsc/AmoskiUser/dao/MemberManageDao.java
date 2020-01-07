package com.nsc.AmoskiUser.dao;


import java.util.List;
import java.util.Map;

public interface MemberManageDao {

    List findDepartmentList(Map<String, Object> params);

    Integer findDepartmentCount(Map<String, Object> params);

    Object getMemberImpowerRole(String memberId, String orgCode);
}
