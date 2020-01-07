package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.ActivitySupplementManageDao;
import com.nsc.Amoski.service.ActivitySupplementManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;


@Transactional
@Service
public class ActivitySupplementManageServiceImpl implements ActivitySupplementManageService {

    @Autowired
    private ActivitySupplementManageDao ActivitySupplementManageDao;

    @Override
    public Map<String, Object> queryTicketDetails(Map<String, String> parent) {
        return ActivitySupplementManageDao.queryTicketDetails(parent);
    }

    @Override
    public Object queryRefundRules(Map<String, String> parent) {
        return ActivitySupplementManageDao.queryRefundRules(parent);
    }
}
