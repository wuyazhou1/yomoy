package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.EnrolmentActivitiesDao;
import com.nsc.Amoski.service.EnrolmentActivitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Transactional
@Service
public class EnrolmentActivitiesServiceImpl implements EnrolmentActivitiesService {
    @Autowired
    private EnrolmentActivitiesDao EnrolmentActivitiesDao;

    @Override
    public List<String> queryActivityListParent(String id) {
        return EnrolmentActivitiesDao.queryActivityListParent(id);
    }
}
