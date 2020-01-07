package com.nsc.AmoskiActivity.Dao.impl;


import com.nsc.Amoski.entity.ActivityCalendarImagesEntity;
import com.nsc.Amoski.entity.TbActivityBasicsEntity;
import com.nsc.AmoskiActivity.Dao.CalendarImagesDao;
import com.nsc.AmoskiActivity.Util.GenericRepositoryActivityImpl;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CalendarImagesDaoImpl extends GenericRepositoryActivityImpl implements CalendarImagesDao {
    @Override
    public List queryCalendarImagesList(Map<String, Object> params) {
        StringBuffer strbug = new StringBuffer("");
        if(params.get("uploadTime")!=null && !params.get("uploadTime").toString().trim().equals("")){
            strbug .append(" and to_char(upload_Time,'yyyy-MM-dd') = :uploadTime ");
        }
        if(params.get("scheduleId")!=null && !params.get("scheduleId").toString().trim().equals("")){
            strbug.append( " and schedule_Id = :scheduleId ");
        }
        List list = this.jdbcTemplate.query(pageSql("select * from ACTIVITY_CALENDAR_IMAGES where STATE!=3 " + strbug.toString() +"  order by id " ,params), params, new BeanPropertyRowMapper(ActivityCalendarImagesEntity.class));
        return list;
    }

    @Override
    public Integer queryCalendarImagesCount(Map<String, Object> params) {
        StringBuffer strbug = new StringBuffer("");
        if(params.get("uploadTime")!=null && !params.get("uploadTime").toString().trim().equals("")){
            strbug .append(" and to_char(upload_Time,'yyyy-MM-dd') = :uploadTime ");
        }
        if(params.get("scheduleId")!=null && !params.get("scheduleId").toString().trim().equals("")){
            strbug.append( " and schedule_Id = :scheduleId ");
        }
        Integer count = this.jdbcTemplate.queryForObject("select count(1) from ACTIVITY_CALENDAR_IMAGES  where STATE!=3  " + strbug.toString()  , params  , Integer.class);
        return count;
    }

    @Override
    public ActivityCalendarImagesEntity queryCalendarImages(String id) {
        Map<String,Object> parentMap = new HashMap<>();
        parentMap.put("id",id);
        List<ActivityCalendarImagesEntity> query = this.jdbcTemplate.query("select * from ACTIVITY_CALENDAR_IMAGES where id = :id and  STATE!=3 ", parentMap, new BeanPropertyRowMapper(ActivityCalendarImagesEntity.class));
        return query.get(0);
    }

    @Override
    public void deleteCalendarImages(String id) {
        Map<String,Object> parentMap = new HashMap<>();
        parentMap.put("id",id);
        this.jdbcTemplate.update("update ACTIVITY_CALENDAR_IMAGES set STATE=3 where id = :id",parentMap);
    }

    @Override
    public void updateCalendarImages(List<ActivityCalendarImagesEntity> list) {
        for(int i=0;i<list.size();i++){
            logger.info("id["+i+"]==>"+list.get(i).getId());
            logger.info("state["+i+"]==>"+list.get(i).getState());
            logger.info("showFileName["+i+"]==>"+list.get(i).getShowFileName());
        }
        SqlParameterSource Parame = new BeanPropertySqlParameterSource(list.get(0));
        this.jdbcTemplate.update("update ACTIVITY_CALENDAR_IMAGES set STATE = :state  ",Parame);
        SqlParameterSource[] Parameter = SqlParameterSourceUtils.createBatch(list.toArray());
        String sql = "update ACTIVITY_CALENDAR_IMAGES set STATE = :state ,SHOW_FILE_NAME = :showFileName  where id = :id";
        logger.info(sql);
        this.jdbcTemplate.batchUpdate(sql,Parameter);
    }
}
