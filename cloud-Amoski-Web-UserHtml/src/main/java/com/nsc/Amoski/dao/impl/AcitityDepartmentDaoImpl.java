package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.AcitityDepartmentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 李阳
 * @date 2019/12/25 17:34
 */
@Repository
public class AcitityDepartmentDaoImpl implements AcitityDepartmentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据俱乐部名称模糊查询
     *
     * @param orgName
     * @return
     */
    @Override
    public List<Map<String, Object>> findByOrgName(String orgName) {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT ID FROM T_DEPARTMENT WHERE ORG_NAME LIKE ?", "%" + orgName + "%");
        return maps == null || maps.size() == 0 ? null : maps;
    }

}
