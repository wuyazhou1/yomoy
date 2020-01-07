package com.nsc.AmoskiActivity.Dao;

import com.nsc.Amoski.entity.*;

import java.util.List;
import java.util.Map;

public interface ActivityCreateDao {
    List QueryActivityList(Map<String, Object> params);

    Integer QueryActivityCount(Map<String, Object> params);

    TbActivityBasicsEntity saveTbActivityBasicsEntity(TbActivityBasicsEntity tbActivityBasicsEntity);

    void saveEntityJdbc(Object obj);

    TbActivityScheduleEntity saveTbActivityScheduleEntity(TbActivityScheduleEntity o1);

    TbActivityRouteEntity saveTbActivityRouteEntity(TbActivityRouteEntity o);

    TbActivityHotelEntity saveTbActivityHotelEntity(TbActivityHotelEntity tbActivityHotelEntity);

    TbActivityHotelRoomEntity saveTbActivityHotelRoomEntity(TbActivityHotelRoomEntity tbActivityHotelRoomEntity);

    TbHotelRestaurantEntity saveTbHotelRestaurantEntity(TbHotelRestaurantEntity tbHotelRestaurantEntity);

    void deleteActivityList(String id);

    Map<String, Object> QueryActivity(String id);

    List QueryEntryMustCompleted(String table);

    void saveEntityObject(ActivityCalendarImagesEntity activityCalendarImagesEntity);

    void deleteActivity(String id);
}
