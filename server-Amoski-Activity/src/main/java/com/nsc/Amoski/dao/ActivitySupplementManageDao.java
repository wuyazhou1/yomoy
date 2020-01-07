package com.nsc.Amoski.dao;

import java.util.Map;

public interface ActivitySupplementManageDao {
    Map<String, Object> queryTicketDetails(Map<String, String> parent);

    Object queryRefundRules(Map<String, String> parent);
}
