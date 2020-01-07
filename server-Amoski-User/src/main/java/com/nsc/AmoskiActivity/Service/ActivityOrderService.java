package com.nsc.AmoskiActivity.Service;

import com.nsc.Amoski.entity.PagingBean;

import java.util.Map;

public interface ActivityOrderService {
    PagingBean activityOrderlist(Map<String, Object> params);


    PagingBean activityOrderDatail(Map<String, Object> params);

    void refundApplication(Map<String, Object> parentMap);

    void refundApplicationFalse(Map<String, Object> parentMap);
}
