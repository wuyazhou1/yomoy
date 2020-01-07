package com.nsc.AmoskiActivity.Dao;

import com.nsc.Amoski.entity.TbActivityOrderDetailsEntity;

import java.util.List;
import java.util.Map;

public interface ActivityOrderDao {
    List activityOrderlist(Map<String, Object> params);

    int activityOrderCount(Map<String, Object> params);







    List activityOrderDatailList(Map<String, Object> params);

    int activityOrderDatailCount(Map<String, Object> params);

    Map<String, Object> queryOrderDatail(Map<String, Object> params);

    Map<String, Object> queryActivityDatail(Map<String, Object> params);

    List<Map<String, Object>> queryOrderHistoryDatail(Map<String, Object> params);

    TbActivityOrderDetailsEntity queryOrderDatailByid(Map<String, Object> parentMap);

    void updateRefundMoneyById(Map<String, Object> parentMap);

    void updateOrderStateById(Map<String, Object> parentMap);

    void updateRefundMoneyFalseById(Map<String, Object> parentMap);

    void updateOrderDatailsFalseById(Map<String, Object> parentMap);

    void updateOrderByDatailsId(Map<String, Object> parentMap);
}
