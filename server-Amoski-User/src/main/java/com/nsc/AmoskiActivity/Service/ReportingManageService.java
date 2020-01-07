package com.nsc.AmoskiActivity.Service;

import com.nsc.Amoski.entity.PagingBean;

import java.util.Map;

public interface ReportingManageService {
    PagingBean ReportingList(Map<String, Object> params);
}
