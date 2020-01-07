package com.nsc.AmoskiActivity.Dao;

import com.nsc.Amoski.entity.ActivityCalendarImagesEntity;

import java.util.List;
import java.util.Map;

public interface CalendarImagesDao {
    List queryCalendarImagesList(Map<String, Object> params);

    Integer queryCalendarImagesCount(Map<String, Object> params);

    ActivityCalendarImagesEntity queryCalendarImages(String id);

    void deleteCalendarImages(String id);

    void updateCalendarImages(List<ActivityCalendarImagesEntity> list);
}
