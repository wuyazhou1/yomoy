package com.nsc.AmoskiActivity.Dao.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.util.BeanCopyUtil;
import com.nsc.AmoskiActivity.Dao.ActivityCreateDao;
import com.nsc.AmoskiActivity.Util.CreateActivityAnalysisUtil;
import com.nsc.AmoskiActivity.Util.GenericRepositoryActivityImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class ActivityCreateDaoImpl extends GenericRepositoryActivityImpl implements ActivityCreateDao {
    @Override
    public List QueryActivityList(Map<String, Object> params) {
        ShiroUser suser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        params.put("orgCode",suser.getOrgCode());
        StringBuffer str = new StringBuffer("");
        if(params.get("title")!=null && !params.get("title").toString().trim().equals("")){
            str.append(" and title like '%'|| :title ||'%'");
        }
        if(params.get("state")!=null && !params.get("state").toString().trim().equals("")){
            str.append(" and state= :title ");
        }
        List list = null;
        try {
            list = this.jdbcTemplate.queryForList(pageSql("select * from TB_ACTIVITY_BASICS where org_code= :orgCode "
                    + str.toString() ,params), params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String selectColumn(){
        return "w.id," +//活动id
                "w.title \"title\"," +//活动名称
                "(select a.ACTIVITY_START from TB_ACTIVITY_ORDINATION a where a.BASICS_ID = w.id) \"start\"," +//开始时间
                "(select a.ACTIVITY_STOP from TB_ACTIVITY_ORDINATION a where a.BASICS_ID = w.id) \"end\"," +//结束时间
                "(select count(1) from TB_HOTEL_RESTAURANT a where a.basics_id=w.id and order_id='1')||'早餐'||(select count(1) from TB_HOTEL_RESTAURANT a where a.basics_id=w.id and order_id='2')||'中餐'||(select count(1) from TB_HOTEL_RESTAURANT a where a.basics_id=w.id and order_id='3')||'晚餐' \"eact\"," +//用餐
                "(select count(1) from TB_ACTIVITY_HOTEL a where a.basics_id=w.id)||'晚' \"shu\"," +//住宿
                "(select listagg(a.PATH_POINT_NAME, ',') WITHIN GROUP(ORDER BY a.BASICS_ID) from TB_ACTIVITY_ROUTE a where a.BASICS_ID=w.id  group by a.BASICS_ID) \"router\"," +//活动线路名称
                "w.nature \"nature\"," +//活动性质
                "w.state \"state\","+
                "(select count(1) from TB_ACTIVITY_SIGN_UP a where a.BASICS_ID=w.id)||(select DECODE('/'||a.NUMBER_LIMITATION,'/','','/'||a.NUMBER_LIMITATION) from TB_ACTIVITY_ORDINATION a where a.BASICS_ID=w.id ) \"amountMoney\"," +//报名人数
                "w.type \"type\" ";
    }

    @Override
    public Integer QueryActivityCount(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");
        if(params.get("title")!=null && !params.get("title").toString().trim().equals("")){
            str.append(" and title like '%'|| :title ||'%'");
        }
        if(params.get("state")!=null && !params.get("state").toString().trim().equals("")){
            str.append(" and state= :title ");
        }
        Integer count = this.jdbcTemplate.queryForObject("select count(1) from TB_ACTIVITY_BASICS where org_code= :orgCode ", params,Integer.class);
        return count;
    }

    @Override
    public TbActivityBasicsEntity saveTbActivityBasicsEntity(TbActivityBasicsEntity tbActivityBasicsEntity) {
        TbActivityBasicsEntity tbActivityBasicsEntity1 = this.saveEntity(tbActivityBasicsEntity);
        return tbActivityBasicsEntity1;
    }

    @Override
    public void saveEntityJdbc(Object obj) {
        this.save(obj);
    }

    @Override
    public TbActivityScheduleEntity saveTbActivityScheduleEntity(TbActivityScheduleEntity o1) {
        TbActivityScheduleEntity tbActivityScheduleEntity = this.saveEntity(o1);
        return o1;
    }

    @Override
    public TbActivityRouteEntity saveTbActivityRouteEntity(TbActivityRouteEntity o) {
        TbActivityRouteEntity tbActivityRouteEntity = this.saveEntity(o);
        return tbActivityRouteEntity;
    }

    @Override
    public TbActivityHotelEntity saveTbActivityHotelEntity(TbActivityHotelEntity tbActivityHotelEntity) {
        tbActivityHotelEntity = this.saveEntity(tbActivityHotelEntity);
        return tbActivityHotelEntity;
    }

    @Override
    public TbActivityHotelRoomEntity saveTbActivityHotelRoomEntity(TbActivityHotelRoomEntity tbActivityHotelRoomEntity) {
        tbActivityHotelRoomEntity = this.saveEntity(tbActivityHotelRoomEntity);
        return tbActivityHotelRoomEntity;
    }

    @Override
    public TbHotelRestaurantEntity saveTbHotelRestaurantEntity(TbHotelRestaurantEntity tbHotelRestaurantEntity) {
        tbHotelRestaurantEntity = this.saveEntity(tbHotelRestaurantEntity);
        return tbHotelRestaurantEntity;
    }

    @Override
    public void deleteActivityList(String id) {
        Map<String,Object> parentMap = new HashMap<>();
        parentMap.put("id",id);
        this.jdbcTemplate.update("update TB_ACTIVITY_BASICS set STATE='4' where id= :id",parentMap);
    }

    @Override
    public Map<String, Object> QueryActivity(String id) {
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> parentMap = new HashMap<>();
        parentMap.put("id",id);
        List<TbActivityBasicsEntity> tbActivityBasicsEntityList = jdbcTemplate.query("select * from TB_ACTIVITY_BASICS where id= :id" , parentMap , new BeanPropertyRowMapper(TbActivityBasicsEntity.class));
        for(TbActivityBasicsEntity TbActivityBasicsEntity:tbActivityBasicsEntityList){
            List<TbActivityBillImageEntity> tbActivityBillImageEntity=jdbcTemplate.query("select * from TB_ACTIVITY_BILL_IMAGE where BASICS_ID= :id" , parentMap , new BeanPropertyRowMapper(TbActivityBillImageEntity.class));
            List<TbCtivityInvoiceEntity> tbCtivityInvoiceEntity=jdbcTemplate.query("select * from TB_CTIVITY_INVOICE where BASICS_ID= :id" , parentMap , new BeanPropertyRowMapper(TbCtivityInvoiceEntity.class));

            List<TbActivityRefundSettingsEntity> tbActivityRefundSettings=jdbcTemplate.query("select * from TB_ACTIVITY_REFUND_SETTINGS where BASICS_ID= :id" , parentMap , new BeanPropertyRowMapper(TbActivityRefundSettingsEntity.class));
            TbActivityBasicsEntity.setTbActivityBillImageEntity(tbActivityBillImageEntity);
            TbActivityBasicsEntity.setTbCtivityInvoiceEntity(tbCtivityInvoiceEntity);
            TbActivityBasicsEntity.setTbActivityRefundSettings(tbActivityRefundSettings);
            resultMap.put("tbActivityBasicsEntity",TbActivityBasicsEntity);
        }
        List tbActivitySynopsisEntityList = jdbcTemplate.query("select * from TB_ACTIVITY_SYNOPSIS where BASICS_ID= :id", parentMap, new BeanPropertyRowMapper(TbActivitySynopsisEntity.class));
        resultMap.put("tbActivitySynopsisEntity",tbActivitySynopsisEntityList==null||tbActivitySynopsisEntityList.size()==0?new HashMap<>():tbActivitySynopsisEntityList.get(0));
        List tbActivityOrdinationEntityList = jdbcTemplate.query("select * from TB_ACTIVITY_ORDINATION where BASICS_ID= :id", parentMap, new BeanPropertyRowMapper(TbActivityOrdinationEntity.class));
        resultMap.put("tbActivityOrdinationEntity",tbActivityOrdinationEntityList==null||tbActivityOrdinationEntityList.size()==0?new HashMap<>():tbActivityOrdinationEntityList.get(0));
        List<TbActivityScheduleEntity> TbActivityScheduleEntityList = jdbcTemplate.query("select * from TB_ACTIVITY_SCHEDULE where BASICS_ID= :id", parentMap, new BeanPropertyRowMapper(TbActivityScheduleEntity.class));
        for(TbActivityScheduleEntity TbActivityScheduleEntity:TbActivityScheduleEntityList){
            parentMap.put("scheduleId",TbActivityScheduleEntity.getId());
            TbActivityScheduleEntity.setActivityCalendarImagesEntity(jdbcTemplate.query("select * from ACTIVITY_CALENDAR_IMAGES where BASICS_ID= :id  and SCHEDULE_ID= :scheduleId", parentMap, new BeanPropertyRowMapper(ActivityCalendarImagesEntity.class)));
            TbActivityScheduleEntity.setTbActivityTimeHistoryEntity(jdbcTemplate.query("select * from TB_ACTIVITY_TIME_HISTORY where BASICS_ID= :id  and SCHEDULE_ID= :scheduleId", parentMap, new BeanPropertyRowMapper(TbActivityTimeHistoryEntity.class)));
            List<TbActivityRouteEntity> TbActivityRouteEntityList = jdbcTemplate.query("select * from TB_ACTIVITY_ROUTE where BASICS_ID= :id  and SCHEDULE_ID= :scheduleId", parentMap, new BeanPropertyRowMapper(TbActivityRouteEntity.class));
            for(TbActivityRouteEntity TbActivityRouteEntity:TbActivityRouteEntityList){
                parentMap.put("routeId",TbActivityRouteEntity.getId());
                TbActivityRouteEntity.setTbActivityRouteImageEntity(jdbcTemplate.query("select * from TB_ACTIVITY_ROUTE_IMAGE where BASICS_ID= :id  and ROUTE_ID= :routeId", parentMap, new BeanPropertyRowMapper(TbActivityRouteImageEntity.class)));
            }
            TbActivityScheduleEntity.setTbActivityRouteEntity(TbActivityRouteEntityList);
            List tbActivityHotelEntityList = jdbcTemplate.query("select * from TB_ACTIVITY_HOTEL where BASICS_ID= :id  and SCHEDULE_ID= :scheduleId", parentMap, new BeanPropertyRowMapper(TbActivityHotelEntity.class));
            if(tbActivityHotelEntityList!=null && tbActivityHotelEntityList.size()>0){
                TbActivityHotelEntity tbActivityHotelEntity = (TbActivityHotelEntity) tbActivityHotelEntityList.get(0);
                parentMap.put("hotelId",tbActivityHotelEntity.getId());
                tbActivityHotelEntity.setTbHotelRestaurantImageEntity(jdbcTemplate.query("select * from TB_HOTEL_RESTAURANT_IMAGE where BASICS_ID= :id  and TYPE= '1' and RELATION_ID= :hotelId", parentMap, new BeanPropertyRowMapper(TbHotelRestaurantImageEntity.class)));
                List<TbActivityHotelRoomEntity> TbActivityHotelRoomEntityList = jdbcTemplate.query("select * from TB_ACTIVITY_HOTEL_ROOM where BASICS_ID= :id and HOTEL_ID= :hotelId", parentMap, new BeanPropertyRowMapper(TbActivityHotelRoomEntity.class));
                for(TbActivityHotelRoomEntity TbActivityHotelRoomEntity:TbActivityHotelRoomEntityList){
                    parentMap.put("roomId",TbActivityHotelRoomEntity.getId());
                    TbActivityHotelRoomEntity.setTbActivityRoomImageEntity(jdbcTemplate.query("select * from TB_ACTIVITY_ROOM_IMAGE where BASICS_ID= :id and ROOM_ID= :roomId and RELATION_ID= :hotelId", parentMap, new BeanPropertyRowMapper(TbActivityRoomImageEntity.class)));
                }
                tbActivityHotelEntity.setTbActivityHotelRoomEntity(TbActivityHotelRoomEntityList);
                TbActivityScheduleEntity.setTbActivityHotelEntity(tbActivityHotelEntity);

            }
            List<TbHotelRestaurantEntity> TbHotelRestaurantEntityList = jdbcTemplate.query("select * from TB_HOTEL_RESTAURANT where BASICS_ID= :id and SCHEDULE_ID= :scheduleId ", parentMap, new BeanPropertyRowMapper(TbHotelRestaurantEntity.class));
            for(TbHotelRestaurantEntity TbHotelRestaurantEntity:TbHotelRestaurantEntityList){
                parentMap.put("relationId",TbHotelRestaurantEntity.getId());
                TbHotelRestaurantEntity.setTbHotelRestaurantImageEntity(jdbcTemplate.query("select * from TB_HOTEL_RESTAURANT_IMAGE where BASICS_ID= :id and type= '2' and RELATION_ID= :relationId ", parentMap, new BeanPropertyRowMapper(TbHotelRestaurantImageEntity.class)));
            }
            TbActivityScheduleEntity.setTbHotelRestaurantEntity(TbHotelRestaurantEntityList);
        }
        resultMap.put("tbActivityScheduleEntity",TbActivityScheduleEntityList);
        //list.add(resultMap);
        return resultMap;
    }

    @Override
    public List QueryEntryMustCompleted(String table) {
        Map<String,Object> parentMap = new HashMap<>();
        parentMap.put("table",table);
        List list = jdbcTemplate.queryForList("SELECT  a.COLUMN_NAME,b.comments\n" +
                "  FROM ALL_TAB_COLUMNS a\n" +
                "  inner join user_col_comments b on a.table_name=b.table_name and a.COLUMN_NAME=b.COLUMN_NAME\n" +
                " WHERE a.TABLE_NAME = :table  and a.COLUMN_NAME not in ('ID','BASICS_ID','ORG_CODE','CREATE_NAME','CREATE_DATA','UPDATE_NAME','UPDATE_DATE')\n" +
                " ORDER BY a.COLUMN_ID  ", parentMap);
        return list;
    }

    @Override
    public void saveEntityObject(ActivityCalendarImagesEntity activityCalendarImagesEntity) {
        this.saveEntity(activityCalendarImagesEntity);
    }

    @Override
    public void deleteActivity(String id) {
        Map<String,Object> parentMap = new HashMap<>();
        parentMap.put("id",id);
        jdbcTemplate.update("delete  TB_ACTIVITY_BASICS   where id = :id",parentMap);//活动基础表
        jdbcTemplate.update("delete  TB_ACTIVITY_SYNOPSIS   where BASICS_ID = :id",parentMap);//活动简介表
        jdbcTemplate.update("delete  TB_ACTIVITY_ORDINATION   where BASICS_ID = :id",parentMap);//活动报名规则表
        jdbcTemplate.update("delete  TB_ACTIVITY_BILL_IMAGE   where BASICS_ID = :id",parentMap);//活动海报图片表
        jdbcTemplate.update("delete  TB_CTIVITY_INVOICE   where BASICS_ID = :id",parentMap);//活动发票表
        jdbcTemplate.update("delete  TB_ACTIVITY_SCHEDULE   where BASICS_ID = :id",parentMap);//活动日程安排表
        jdbcTemplate.update("delete  TB_ACTIVITY_TIME_HISTORY   where BASICS_ID = :id",parentMap);//活动时辰安排表
        jdbcTemplate.update("delete  TB_ACTIVITY_ROUTE   where BASICS_ID = :id",parentMap);//活动线路安排表
        jdbcTemplate.update("delete  TB_ACTIVITY_ROUTE_IMAGE   where BASICS_ID = :id",parentMap);//活动线路图片表
        jdbcTemplate.update("delete  TB_ACTIVITY_HOTEL   where BASICS_ID = :id",parentMap);//活动酒店表
        jdbcTemplate.update("delete  TB_ACTIVITY_HOTEL_ROOM   where BASICS_ID = :id",parentMap);//酒店房型表
        jdbcTemplate.update("delete  TB_ACTIVITY_ROOM_IMAGE   where BASICS_ID = :id",parentMap);//活动房型图片
        jdbcTemplate.update("delete  TB_HOTEL_RESTAURANT   where BASICS_ID = :id",parentMap);//活动酒店餐厅表
        jdbcTemplate.update("delete  ACTIVITY_CALENDAR_IMAGES   where BASICS_ID = :id",parentMap);//活动日程图片表
        jdbcTemplate.update("delete  TB_ACTIVITY_REFUND_SETTINGS   where BASICS_ID = :id",parentMap);//活动退款设置表
    }
}
