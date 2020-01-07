package com.nsc.AmoskiActivity.Service.impl;

import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.util.StringUtils;
import com.nsc.AmoskiActivity.Dao.ReportingManageDao;
import com.nsc.AmoskiActivity.Service.ReportingManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional(transactionManager = "ActivityTransactionManager")
public class ReportingManageServiceImpl implements ReportingManageService {

    @Autowired
    @Lazy
    public ReportingManageDao reportingManageDao;


    @Override
    public PagingBean ReportingList(Map<String, Object> params) {
        List list = reportingManageDao.ReportingList(params);
        int count = reportingManageDao.ReportingCount(params);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        if (StringUtils.isEmpty(params.get("draw")))
            pagelist.setDraw(0);
        else
            pagelist.setDraw(Integer.valueOf(params.get("draw").toString()));
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }
}
