package com.nsc.AmoskiActivity.Service;

import com.alibaba.fastjson.JSONArray;
import com.nsc.Amoski.entity.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface ActivityCreateService {
    PagingBean QueryActivityList(Map<String, Object> params);

    void saveActivity(JSONArray activity);

    void deleteActivityList(String id);

    Map<String, Object> QueryActivity(String id);

    List QueryEntryMustCompleted(String table);

    void saveObject(ActivityCalendarImagesEntity activityCalendarImagesEntity);

    void saveEntityObject(ActivityCalendarImagesEntity activityCalendarImagesEntity);

    void deleteActivity(String id);
}
