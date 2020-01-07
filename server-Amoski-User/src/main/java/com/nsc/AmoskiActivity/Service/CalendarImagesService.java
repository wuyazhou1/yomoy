package com.nsc.AmoskiActivity.Service;

import com.nsc.Amoski.entity.ActivityCalendarImagesEntity;
import com.nsc.Amoski.entity.PagingBean;

import java.util.List;
import java.util.Map;

public interface CalendarImagesService {

    PagingBean queryCalendarImagesList(Map<String, Object> params);

    void deleteCalendarImages(String id);

    void updateCalendarImages(List<ActivityCalendarImagesEntity> list);
}
