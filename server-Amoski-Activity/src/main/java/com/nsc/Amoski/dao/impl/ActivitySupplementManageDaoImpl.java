package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.ActivitySupplementManageDao;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class ActivitySupplementManageDaoImpl  extends GenericRepositoryImpl implements ActivitySupplementManageDao {
    @Override
    public Map<String, Object> queryTicketDetails(Map<String, String> parent) {
        List<Map<String, Object>> resultList = this.jdbcTemplate.queryForList("select * from TB_CTIVITY_INVOICE where basics_id = :basicsId  and id = :invoiceId", parent);
        if(resultList == null || resultList.size() < 1 ){
            return null;
        }else{
            return resultList.get(0);
        }
    }

    @Override
    public Object queryRefundRules(Map<String, String> parent) {
        List<Map<String, Object>> resultList = this.jdbcTemplate.queryForList("select * from TB_ACTIVITY_REFUND_SETTINGS where basics_id in (select id from TB_ACTIVITY_BASICS where code = :basicsId)", parent);
        if(resultList == null || resultList.size() < 1 ){
            return null;
        }else{
            return resultList;
        }
    }
}
