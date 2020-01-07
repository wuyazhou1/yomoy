package com.nsc.Amoski.service;

import java.util.List;
import java.util.Map;

/**
 * @author 李阳
 * @date 2019/12/25 17:39
 */
public interface AcitityDepartmentService {

    List<Map<String, Object>> findByOrgName(String orgName);

}
