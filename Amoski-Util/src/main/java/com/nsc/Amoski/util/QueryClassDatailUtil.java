package com.nsc.Amoski.util;

import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class QueryClassDatailUtil extends GenericRepositoryImpl implements Callable<ActivityCreateQueryEntity> {

    public TbActivityBasicsEntity tbActivityBasicsEntity;
    public ShiroUser shiroUser;
    public QueryClassDatailUtil(NamedParameterJdbcTemplate jdbcTemplate, TbActivityBasicsEntity tbActivityBasicsEntity, ShiroUser shiroUser){
        this.jdbcTemplate=jdbcTemplate;
        this.tbActivityBasicsEntity=tbActivityBasicsEntity;
        this.shiroUser=shiroUser;
    }

    @Override
    public ActivityCreateQueryEntity call() throws Exception {
        ActivityCreateQueryEntity activityCreateQueryEntity = new ActivityCreateQueryEntity();
        activityCreateQueryEntity.setId(tbActivityBasicsEntity.getId().toString());
        activityCreateQueryEntity.setTxt(tbActivityBasicsEntity.getTitle());
        activityCreateQueryEntity.setType(tbActivityBasicsEntity.getTitle());
        //activityCreateQueryEntity.setImg();
        activityCreateQueryEntity.setTbActivityBasicsEntity(tbActivityBasicsEntity);
        Map<String,Object> parentMap = new HashMap<>();
        parentMap.put("basicsId",tbActivityBasicsEntity.getId());
        parentMap.put("orgCode",tbActivityBasicsEntity.getId());

        //活动简介表
        String TbActivitySynopsisSql = "select * from TB_ACTIVITY_SYNOPSIS  where basics_Id = :basicsId  and org_code = :orgCode";
        List<TbActivitySynopsisEntity> query = jdbcTemplate.query(TbActivitySynopsisSql, parentMap, new BeanPropertyRowMapper(TbActivitySynopsisEntity.class));
        if(query==null || query.size()==0){
            activityCreateQueryEntity.setTbActivitySynopsisEntity(new TbActivitySynopsisEntity());
        }else{
            activityCreateQueryEntity.setTbActivitySynopsisEntity(query.get(0));
        }
        //活动报名规则表
        String tbActivityOrdinationSql = "select * from TB_ACTIVITY_ORDINATION  where basics_Id = :basicsId  and org_code = :orgCode";
        List<TbActivityOrdinationEntity> tbActivityOrdinationEntity = jdbcTemplate.query(tbActivityOrdinationSql, parentMap, new BeanPropertyRowMapper(TbActivityOrdinationEntity.class));
        if(tbActivityOrdinationEntity==null || tbActivityOrdinationEntity.size()==0){
            activityCreateQueryEntity.setTbActivityOrdinationEntity(new TbActivityOrdinationEntity());
        }else{
            activityCreateQueryEntity.setTbActivityOrdinationEntity(tbActivityOrdinationEntity.get(0));
        }
        //活动日程安排表
        String tbActivityScheduleSql = "select * from TB_ACTIVITY_SCHEDULE  where basics_Id = :basicsId  and org_code = :orgCode";
        List<TbActivityScheduleEntity> tbActivityScheduleEntity = jdbcTemplate.query(tbActivityScheduleSql, parentMap, new BeanPropertyRowMapper(TbActivityScheduleEntity.class));
        if(tbActivityScheduleEntity==null || tbActivityScheduleEntity.size()==0){
            activityCreateQueryEntity.setTbActivityScheduleEntity(new ArrayList<TbActivityScheduleEntity>(){{this.add(new TbActivityScheduleEntity());}});
        }else{
            activityCreateQueryEntity.setTbActivityScheduleEntity(tbActivityScheduleEntity);
        }
        queryTbActivityTimeHistoryEntity(tbActivityScheduleEntity);
        queryTbActivityRouteEntity(tbActivityScheduleEntity);
        queryTbActivityHotelEntity(tbActivityScheduleEntity);
        queryTbHotelRestaurantEntity(tbActivityScheduleEntity);
        return null;
    }
    //活动时辰安排表
    public void queryTbActivityTimeHistoryEntity(List<TbActivityScheduleEntity> tbActivityScheduleEntity){
        for(TbActivityScheduleEntity entity:tbActivityScheduleEntity){
            Map<String,Object> parentMap = new HashMap<>();
            parentMap.put("scheduleId",entity.getId());
            String tbActivityTimeHistorySql = "select * from TB_ACTIVITY_TIME_HISTORY  where basics_Id = :basicsId  and org_code = :orgCode";
            List<TbActivityTimeHistoryEntity> tbActivityTimeHistoryEntity = jdbcTemplate.query(tbActivityTimeHistorySql, parentMap, new BeanPropertyRowMapper(TbActivityTimeHistoryEntity.class));
            if(tbActivityTimeHistoryEntity==null || tbActivityTimeHistoryEntity.size()==0){
                entity.setTbActivityTimeHistoryEntity(new ArrayList<TbActivityTimeHistoryEntity>(){{this.add(new TbActivityTimeHistoryEntity());}});
            }else{
                entity.setTbActivityTimeHistoryEntity(tbActivityTimeHistoryEntity);
            }
        }
    }
    //活动线路安排表
    public void queryTbActivityRouteEntity(List<TbActivityScheduleEntity> tbActivityScheduleEntity){
        for (TbActivityScheduleEntity entity:tbActivityScheduleEntity){
            Map<String,Object> parentMap = new HashMap<>();
            parentMap.put("scheduleId",entity.getId());
            String tbActivityRouteSql = "select * from TB_ACTIVITY_ROUTE  where basics_Id = :basicsId  and org_code = :orgCode";
            List<TbActivityRouteEntity> tbActivityRouteEntity = jdbcTemplate.query(tbActivityRouteSql, parentMap, new BeanPropertyRowMapper(TbActivityRouteEntity.class));
            //添加线路相册
            for(TbActivityRouteEntity tbActivityRoute:tbActivityRouteEntity){
                Map<String,Object> parent = new HashMap<>();
                parent.put("routeId",tbActivityRoute.getId());
                String tbActivityRouteImageSql = "select * from TB_ACTIVITY_ROUTE_IMAGE  where basics_Id = :basicsId  and org_code = :orgCode";
                List<TbActivityRouteImageEntity> tbActivityRouteImageEntity = jdbcTemplate.query(tbActivityRouteImageSql, parent, new BeanPropertyRowMapper(TbActivityRouteImageEntity.class));
                if(tbActivityRouteImageEntity==null || tbActivityRouteImageEntity.size()==0){
                    tbActivityRoute.setTbActivityRouteImageEntity(new ArrayList<TbActivityRouteImageEntity>(){{this.add(new TbActivityRouteImageEntity());}});
                }else{
                    tbActivityRoute.setTbActivityRouteImageEntity(tbActivityRouteImageEntity);
                }
            }
            if(tbActivityRouteEntity==null || tbActivityRouteEntity.size()==0){
                entity.setTbActivityRouteEntity(new ArrayList<TbActivityRouteEntity>(){{this.add(new TbActivityRouteEntity());}});
            }else{
                entity.setTbActivityRouteEntity(tbActivityRouteEntity);
            }
        }
    }
    //活动酒店表
    public void queryTbActivityHotelEntity(List<TbActivityScheduleEntity> tbActivityScheduleEntity){
        for (TbActivityScheduleEntity entity:tbActivityScheduleEntity){
            Map<String,Object> parentMap = new HashMap<>();
            parentMap.put("scheduleId",entity.getId());
            String tbActivityRouteSql = "select * from TB_ACTIVITY_HOTEL  where basics_Id = :basicsId  and org_code = :orgCode";
            List<TbActivityHotelEntity> tbActivityHotelEntity = jdbcTemplate.query(tbActivityRouteSql, parentMap, new BeanPropertyRowMapper(TbActivityHotelEntity.class));
            //添加酒店图片表
            for(TbActivityHotelEntity tbActivityRoute:tbActivityHotelEntity){
                Map<String,Object> parent = new HashMap<>();
                parent.put("routeId",tbActivityRoute.getId());
                String tbHotelRestaurantImageSql = "select * from TB_HOTEL_RESTAURANT_IMAGE  where RELATION_ID = :routeId   and TYPE=1";
                List<TbHotelRestaurantImageEntity> tbHotelRestaurantImageEntity = jdbcTemplate.query(tbHotelRestaurantImageSql, parent, new BeanPropertyRowMapper(TbHotelRestaurantImageEntity.class));
                if(tbHotelRestaurantImageEntity==null || tbHotelRestaurantImageEntity.size()==0){
                    tbActivityRoute.setTbHotelRestaurantImageEntity(new ArrayList<TbHotelRestaurantImageEntity>(){{this.add(new TbHotelRestaurantImageEntity());}});
                }else{
                    tbActivityRoute.setTbHotelRestaurantImageEntity(tbHotelRestaurantImageEntity);
                }
                //添加酒店房型表
                String tbActivityHotelRoomSql = "select * from TB_ACTIVITY_HOTEL_ROOM  where HOTEL_ID = :routeId  and org_code = :orgCode";
                List<TbActivityHotelRoomEntity> tbActivityHotelRoomEntity = jdbcTemplate.query(tbActivityHotelRoomSql, parent, new BeanPropertyRowMapper(TbActivityHotelRoomEntity.class));
                //添加房性图片表
                for(TbActivityHotelRoomEntity TbActivityHotelNext:tbActivityHotelRoomEntity){
                    Map<String,Object> map = new HashMap<>();
                    parent.put("roomId",TbActivityHotelNext.getId());
                    String tbActivityRoomImageSql = "select * from TB_ACTIVITY_ROOM_IMAGE  where room_Id = :routeId  ";
                    List<TbActivityRoomImageEntity> tbActivityRoomImageEntity = jdbcTemplate.query(tbActivityRoomImageSql, parent, new BeanPropertyRowMapper(TbActivityRoomImageEntity.class));
                    if(tbHotelRestaurantImageEntity==null || tbHotelRestaurantImageEntity.size()==0){
                        TbActivityHotelNext.setTbActivityRoomImageEntity(new ArrayList<TbActivityRoomImageEntity>(){{this.add(new TbActivityRoomImageEntity());}});
                    }else{
                        TbActivityHotelNext.setTbActivityRoomImageEntity(tbActivityRoomImageEntity);
                    }
                }
                if(tbActivityHotelRoomEntity==null || tbActivityHotelRoomEntity.size()==0){
                    tbActivityRoute.setTbActivityHotelRoomEntity(new ArrayList<TbActivityHotelRoomEntity>(){{this.add(new TbActivityHotelRoomEntity());}});
                }else{
                    tbActivityRoute.setTbActivityHotelRoomEntity(tbActivityHotelRoomEntity);
                }
            }
            if(tbActivityHotelEntity==null || tbActivityHotelEntity.size()==0){
                entity.setTbActivityHotelEntity(new TbActivityHotelEntity());
            }else{
                entity.setTbActivityHotelEntity(tbActivityHotelEntity.get(0));
            }
        }
    }
    //活动酒店餐厅表
    public void queryTbHotelRestaurantEntity(List<TbActivityScheduleEntity> tbActivityScheduleEntity){
        for(TbActivityScheduleEntity entity:tbActivityScheduleEntity){
            Map<String,Object> parentMap = new HashMap<>();
            parentMap.put("scheduleId",entity.getId());
            String tbHotelRestaurantsql = "select * from TB_HOTEL_RESTAURANT  where schedule_Id = :scheduleId  and org_code = :orgCode";
            List<TbHotelRestaurantEntity> tbHotelRestaurantEntity = jdbcTemplate.query(tbHotelRestaurantsql, parentMap, new BeanPropertyRowMapper(TbHotelRestaurantEntity.class));
            if(tbHotelRestaurantEntity==null || tbHotelRestaurantEntity.size()==0){
                entity.setTbHotelRestaurantEntity(new ArrayList<TbHotelRestaurantEntity>(){{this.add(new TbHotelRestaurantEntity());}});
            }else{
                entity.setTbHotelRestaurantEntity(tbHotelRestaurantEntity);
            }
        }
    }


}
