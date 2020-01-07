package com.nsc.AmoskiActivity.Service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.util.StringUtils;
import com.nsc.AmoskiActivity.Dao.ActivityCreateDao;
import com.nsc.AmoskiActivity.Service.ActivityCreateService;
import com.nsc.AmoskiActivity.Util.CreateActivityAnalysisUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional(transactionManager = "ActivityTransactionManager")
public class ActivityCreateServiceImpl implements ActivityCreateService {

    @Autowired
    private ActivityCreateDao activityCreateDao;

    @Override
    public PagingBean QueryActivityList(Map<String, Object> params) {
        List list = activityCreateDao.QueryActivityList(params);
        Integer count = activityCreateDao.QueryActivityCount(params);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        if (StringUtils.isEmpty(params.get("draw")))
            pagelist.setDraw(0);
        else
            pagelist.setDraw(Integer.valueOf(params.get("draw").toString()));
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }


    @Override
    public void saveActivity(JSONArray activity) {
        ShiroUser suser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        JSONObject tbActivityBasicsEntityJson = activity.getJSONObject(0).getJSONObject("tbActivityBasicsEntity");
        TbActivityBasicsEntity tbActivityBasicsEntity = (TbActivityBasicsEntity)
                CreateActivityAnalysisUtil.ActivityAnalysisUtil(tbActivityBasicsEntityJson,TbActivityBasicsEntity.class);//活动基础表
        if(tbActivityBasicsEntity.getId()!=null){
            //activityCreateDao.deleteActivityList(tbActivityBasicsEntity.getId()+"");//逻辑删除
            activityCreateDao.deleteActivity(tbActivityBasicsEntity.getId()+"");

        }
        tbActivityBasicsEntity.setOrgCode(suser.getOrgCode());
        tbActivityBasicsEntity = activityCreateDao.saveTbActivityBasicsEntity(tbActivityBasicsEntity);
        JSONArray tbActivityRefundSettings = tbActivityBasicsEntityJson.getJSONArray("tbActivityRefundSettings");//活动退款设置
        for(int i=0;i<tbActivityRefundSettings.size();i++){
            TbActivityRefundSettingsEntity TbActivityRefundSettingsEntity = (TbActivityRefundSettingsEntity)
                    CreateActivityAnalysisUtil.ActivityAnalysisUtil(tbActivityRefundSettings.getJSONObject(i),TbActivityRefundSettingsEntity.class);//活动基础表
            TbActivityRefundSettingsEntity.setBasicsId(tbActivityBasicsEntity.getId().toString());
            activityCreateDao.saveEntityJdbc(TbActivityRefundSettingsEntity);
        }
        JSONArray tbActivityBillImageEntityJson = tbActivityBasicsEntityJson.getJSONArray("tbActivityBillImageEntity");
        for (int i=0;i<tbActivityBillImageEntityJson.size();i++){
            TbActivityBillImageEntity o = (TbActivityBillImageEntity)
                    CreateActivityAnalysisUtil.ActivityAnalysisUtil(tbActivityBillImageEntityJson.getJSONObject(i), TbActivityBillImageEntity.class);//活动海报图片表
            o.setBasicsId(tbActivityBasicsEntity.getId()+"");
            activityCreateDao.saveEntityJdbc(o);
        }
        JSONArray tbCtivityInvoiceEntity = tbActivityBasicsEntityJson.getJSONArray("tbCtivityInvoiceEntity");
        for (int i=0;i<tbCtivityInvoiceEntity.size();i++){
            TbCtivityInvoiceEntity o = (TbCtivityInvoiceEntity)
                    CreateActivityAnalysisUtil.ActivityAnalysisUtil(tbCtivityInvoiceEntity.getJSONObject(i), TbCtivityInvoiceEntity.class);//票种表
            o.setBasicsId(tbActivityBasicsEntity.getId()+"");
            activityCreateDao.saveEntityJdbc(o);
        }
        TbActivitySynopsisEntity TbActivitySynopsisEntity = (TbActivitySynopsisEntity)CreateActivityAnalysisUtil.ActivityAnalysisUtil(activity.getJSONObject(0).getJSONObject("tbActivitySynopsisEntity"),TbActivitySynopsisEntity.class);//活动简介表
        TbActivitySynopsisEntity.setBasicsId(tbActivityBasicsEntity.getId()+"");
        activityCreateDao.saveEntityJdbc(TbActivitySynopsisEntity);
        TbActivityOrdinationEntity TbActivityOrdinationEntity = (TbActivityOrdinationEntity)CreateActivityAnalysisUtil.ActivityAnalysisUtil(activity.getJSONObject(0).getJSONObject("tbActivityOrdinationEntity"),TbActivityOrdinationEntity.class);//活动报名规则表
        TbActivityOrdinationEntity.setBasicsId(tbActivityBasicsEntity.getId()+"");
        activityCreateDao.saveEntityJdbc(TbActivityOrdinationEntity);

        JSONArray jsonArray = activity.getJSONObject(0).getJSONArray("tbActivityScheduleEntity");//活动日程安排表
        for(Integer i=0;i<jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            TbActivityScheduleEntity o1 = (TbActivityScheduleEntity)CreateActivityAnalysisUtil.ActivityAnalysisUtil(jsonObject, TbActivityScheduleEntity.class);
            o1.setDaysStatistics(i.toString());
            o1.setBasicsId(tbActivityBasicsEntity.getId()+"");
            o1=activityCreateDao.saveTbActivityScheduleEntity(o1);
            JSONArray tbActivityTimeHistoryEntity = jsonObject.getJSONArray("tbActivityTimeHistoryEntity");//活动时辰安排表
            for(int j=0;j<tbActivityTimeHistoryEntity.size();j++){
                TbActivityTimeHistoryEntity o = (TbActivityTimeHistoryEntity)CreateActivityAnalysisUtil.ActivityAnalysisUtil(tbActivityTimeHistoryEntity.getJSONObject(j), TbActivityTimeHistoryEntity.class);
                o.setBasicsId(tbActivityBasicsEntity.getId()+"");
                o.setScheduleId(o1.getId()+"");
                activityCreateDao.saveEntityJdbc(o);
            }
            JSONArray tbActivityRouteEntity = jsonObject.getJSONArray("tbActivityRouteEntity");//活动线路安排表
            if(tbActivityRouteEntity==null){
                tbActivityRouteEntity = new JSONArray();
            }
            for(int j=0;j<tbActivityRouteEntity.size();j++){
                JSONObject jsonObject1 = tbActivityRouteEntity.getJSONObject(j);//活动线路安排表
                TbActivityRouteEntity o = (TbActivityRouteEntity)CreateActivityAnalysisUtil.ActivityAnalysisUtil(jsonObject1, TbActivityRouteEntity.class);
                o.setBasicsId(tbActivityBasicsEntity.getId()+"");
                o.setScheduleId(o1.getId()+"");
                o = activityCreateDao.saveTbActivityRouteEntity(o);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("tbActivityRouteImageEntity");
                for (int k=0;k<jsonArray1.size();k++){
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(k);//活动线路图片表
                    TbActivityRouteImageEntity d = (TbActivityRouteImageEntity)CreateActivityAnalysisUtil.ActivityAnalysisUtil(jsonObject2, TbActivityRouteImageEntity.class);
                    d.setBasicsId(tbActivityBasicsEntity.getId()+"");
                    d.setRouteId(o.getId()+"");
                    activityCreateDao.saveEntityJdbc(d);
                }
            }
            JSONObject tbActivityHotelEntity = jsonObject.getJSONObject("tbActivityHotelEntity");//活动酒店表
            if(tbActivityHotelEntity!=null){
                TbActivityHotelEntity TbActivityHotelEntity = (TbActivityHotelEntity) CreateActivityAnalysisUtil.ActivityAnalysisUtil(tbActivityHotelEntity, TbActivityHotelEntity.class);
                TbActivityHotelEntity.setBasicsId(tbActivityBasicsEntity.getId()+"");
                TbActivityHotelEntity.setScheduleId(o1.getId().toString());
                TbActivityHotelEntity = activityCreateDao.saveTbActivityHotelEntity(TbActivityHotelEntity);
                JSONArray tbHotelRestaurantImageEntity = null;
                JSONArray tbActivityHotelRoomEntity = null;
                if(tbActivityHotelEntity==null){
                    tbHotelRestaurantImageEntity = new JSONArray();
                    tbActivityHotelRoomEntity = new JSONArray();
                }else{
                    tbHotelRestaurantImageEntity = tbActivityHotelEntity.getJSONArray("tbHotelRestaurantImageEntity");
                    tbActivityHotelRoomEntity = tbActivityHotelEntity.getJSONArray("tbActivityHotelRoomEntity");//活动房型表
                }
                for(int j=0;j<tbHotelRestaurantImageEntity.size();j++){
                    JSONObject jsonObject1 = tbHotelRestaurantImageEntity.getJSONObject(j);//活动酒店图片
                    TbHotelRestaurantImageEntity TbHotelRestaurantImageEntity = (TbHotelRestaurantImageEntity) CreateActivityAnalysisUtil.ActivityAnalysisUtil(jsonObject1, TbHotelRestaurantImageEntity.class);
                    TbHotelRestaurantImageEntity.setBasicsId(tbActivityBasicsEntity.getId()+"");
                    TbHotelRestaurantImageEntity.setRelationId(TbActivityHotelEntity.getId()+"");
                    TbHotelRestaurantImageEntity.setType("1");
                    activityCreateDao.saveEntityJdbc(TbHotelRestaurantImageEntity);
                }

                for(int j=0;j<tbActivityHotelRoomEntity.size();j++){
                    JSONObject jsonObject1 = tbActivityHotelRoomEntity.getJSONObject(j);
                    TbActivityHotelRoomEntity TbActivityHotelRoomEntity = (TbActivityHotelRoomEntity) CreateActivityAnalysisUtil.ActivityAnalysisUtil(jsonObject1, TbActivityHotelRoomEntity.class);
                    TbActivityHotelRoomEntity.setBasicsId(tbActivityBasicsEntity.getId()+"");
                    TbActivityHotelRoomEntity.setHotelId(TbActivityHotelEntity.getId()+"");
                    TbActivityHotelRoomEntity=activityCreateDao.saveTbActivityHotelRoomEntity(TbActivityHotelRoomEntity);
                    JSONArray tbActivityRoomImageEntity = jsonObject1.getJSONArray("tbActivityRoomImageEntity");//活动房型图片表
                    for (int k=0;k<tbActivityRoomImageEntity.size();k++){
                        JSONObject jsonObject2 = tbActivityRoomImageEntity.getJSONObject(k);
                        TbActivityRoomImageEntity TbActivityRoomImageEntity = (TbActivityRoomImageEntity) CreateActivityAnalysisUtil.ActivityAnalysisUtil(jsonObject1, TbActivityRoomImageEntity.class);
                        TbActivityRoomImageEntity.setBasicsId(tbActivityBasicsEntity.getId()+"");
                        TbActivityRoomImageEntity.setRoomId(TbActivityHotelRoomEntity.getId()+"");
                        TbActivityRoomImageEntity.setRoomId(TbActivityHotelRoomEntity.getHotelId());
                        activityCreateDao.saveEntityJdbc(TbActivityRoomImageEntity);
                    }
                }
            }
            JSONArray tbHotelRestaurantEntity = jsonObject.getJSONArray("tbHotelRestaurantEntity");//活动酒店餐厅表
            for (int j=0;j<tbHotelRestaurantEntity.size();j++){
                JSONObject jsonObject1 = tbHotelRestaurantEntity.getJSONObject(j);
                TbHotelRestaurantEntity TbHotelRestaurantEntity = (TbHotelRestaurantEntity) CreateActivityAnalysisUtil.ActivityAnalysisUtil(jsonObject1, TbHotelRestaurantEntity.class);
                TbHotelRestaurantEntity.setBasicsId(tbActivityBasicsEntity.getId()+"");
                TbHotelRestaurantEntity.setScheduleId(o1.getId()+"");
                TbHotelRestaurantEntity = activityCreateDao.saveTbHotelRestaurantEntity(TbHotelRestaurantEntity);
                JSONArray tbHotelRestaurantImageEntity1 = jsonObject1.getJSONArray("tbHotelRestaurantImageEntity");
                for(int k=0;k<tbHotelRestaurantImageEntity1.size();k++){
                    JSONObject jsonObject2 = tbHotelRestaurantImageEntity1.getJSONObject(k);
                    TbHotelRestaurantImageEntity TbHotelRestaurantImageEntity = (TbHotelRestaurantImageEntity) CreateActivityAnalysisUtil.ActivityAnalysisUtil(jsonObject2, TbHotelRestaurantImageEntity.class);
                    TbHotelRestaurantImageEntity.setBasicsId(tbActivityBasicsEntity.getId()+"");
                    TbHotelRestaurantImageEntity.setRelationId(TbHotelRestaurantEntity.getId()+"");
                    TbHotelRestaurantImageEntity.setType("2");
                    activityCreateDao.saveEntityJdbc(TbHotelRestaurantImageEntity);
                }
            }
        }
    }

    @Override
    public void deleteActivityList(String id) {
        activityCreateDao.deleteActivityList(id);
    }

    @Override
    public Map<String, Object> QueryActivity(String id) {
        return activityCreateDao.QueryActivity(id);
    }

    @Override
    public List QueryEntryMustCompleted(String table) {
        return activityCreateDao.QueryEntryMustCompleted(table);
    }

    @Override
    public void saveObject(ActivityCalendarImagesEntity activityCalendarImagesEntity) {
        activityCreateDao.saveEntityJdbc(activityCalendarImagesEntity);
    }

    @Override
    public void saveEntityObject(ActivityCalendarImagesEntity activityCalendarImagesEntity) {
        activityCreateDao.saveEntityObject(activityCalendarImagesEntity);
    }

    @Override
    public void deleteActivity(String id) {
        //activityCreateDao.deleteActivityList(id);//逻辑删除
        activityCreateDao.deleteActivity(id);
    }
}
