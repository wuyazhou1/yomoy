package com.nsc.Amoski.service;

import java.util.Map;

public interface ActivitySupplementManageService {
    Map<String, Object> queryTicketDetails(Map<String, String> parent);

    Object queryRefundRules(Map<String, String> parent);
}
