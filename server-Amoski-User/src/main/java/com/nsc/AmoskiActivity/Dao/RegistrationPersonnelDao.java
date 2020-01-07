package com.nsc.AmoskiActivity.Dao;

import com.nsc.Amoski.entity.TbActivityBasicsEntity;

import java.util.List;
import java.util.Map;

public interface RegistrationPersonnelDao {
    List RegistrationPersonnelList(Map<String, Object> params);

    Integer RegistrationPersonnelCount(Map<String, Object> params);

    List<Map<String, Object>> RegistrationPersonnelDetails(String id,Map<String, Object>  resultMap);

    void updateUpdateList(List list);

    void updateUpdate(Object o);

    void deleteObject(Object o);

    void addObjectList(List list);

    List<TbActivityBasicsEntity> queryHotelNameRoomType(String id);
    Map<String,Object> queryHotelNameRoomType(List<TbActivityBasicsEntity> list);
}
