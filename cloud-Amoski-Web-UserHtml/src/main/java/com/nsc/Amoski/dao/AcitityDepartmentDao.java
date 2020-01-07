package com.nsc.Amoski.dao;

import java.util.List;
import java.util.Map;

/**
 * @author 李阳
 * @date 2019/12/25 17:34
 */
public interface AcitityDepartmentDao {

    List<Map<String, Object>> findByOrgName(String orgName);

}
