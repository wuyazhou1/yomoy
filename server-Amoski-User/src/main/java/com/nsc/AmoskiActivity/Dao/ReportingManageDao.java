package com.nsc.AmoskiActivity.Dao;

import java.util.List;
import java.util.Map;

public interface ReportingManageDao {
    List ReportingList(Map<String, Object> params);

    int ReportingCount(Map<String, Object> params);

}
