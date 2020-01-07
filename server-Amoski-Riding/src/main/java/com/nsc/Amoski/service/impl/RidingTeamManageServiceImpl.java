package com.nsc.Amoski.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.dao.RidingTeamManageDao;
import com.nsc.Amoski.dto.RidingInfoDto;
import com.nsc.Amoski.dto.TbRidingGuideInfoDto;
import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.service.RidingTeamManageService;
import com.nsc.Amoski.util.DateUtils;
import com.nsc.Amoski.util.RegUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class RidingTeamManageServiceImpl implements RidingTeamManageService {

    @Autowired
    RidingTeamManageDao ridingManageDao;


    public void addRidingInfo(TbRidingInfoEntity entity) {
        this.ridingManageDao.addRidingInfo(entity);
    }

    public void addRidingTravelPlan(TbRidingTravelPlanEntity entity) {
        this.ridingManageDao.addRidingTravelPlan(entity);
    }

    public void addInitRidingData(TbRidingInfoEntity entity, TbRidingInfoDetailEntity detailEntity) {
        this.ridingManageDao.addRidingInfo(entity);
        detailEntity.setRidingId(entity.getId());
        this.ridingManageDao.addRidingInfoDetail(detailEntity);
    }

    public void updateAllRidingData(TbRidingInfoEntity entity, TbRidingInfoDetailEntity detailEntity) {
        this.ridingManageDao.updateRidingInfo(entity);
        if (detailEntity.getId() != null) {
            this.ridingManageDao.updateRidingInfoDetail(detailEntity);
        } else {
            this.ridingManageDao.addEntity(detailEntity, false);
        }

    }

    public void addRidingInfoDetail(TbRidingInfoDetailEntity entity) {
        this.ridingManageDao.addRidingInfoDetail(entity);
    }

    public void updateRidingInfo(TbRidingInfoEntity entity) {
        this.ridingManageDao.updateRidingInfo(entity);
    }

    public void updateRidingInfoDetail(TbRidingInfoDetailEntity entity) {
        this.ridingManageDao.updateRidingInfoDetail(entity);
    }

    public RidingInfoDto queryRidingInfo(RidingInfoDto param) {
        return this.ridingManageDao.queryRidingInfo(param);
    }

    public RidingInfoDto queryUserRidingRank(RidingInfoDto param) {
        return this.ridingManageDao.queryUserRidingRank(param);
    }

    public List<RidingInfoDto> queryUserRidingCountByMonth(RidingInfoDto param) {
        return this.ridingManageDao.queryUserRidingCountByMonth(param);
    }

    public List<RidingInfoDto> queryAllRidingInfo(RidingInfoDto param) {
        return this.ridingManageDao.queryAllRidingInfo(param);
    }

    public List<RidingInfoDto> queryUserDisCountRidingInfo(RidingInfoDto param) {
        return this.ridingManageDao.queryUserDisCountRidingInfo(param);
    }

    /**
     * 查询根据最近距离排序数据
     * @return
     */
    public PagingBean queryDistanceGuide(TbRidingGuideInfoDto dto){
        return ridingManageDao.queryDistanceGuide(dto);
    }

    public void saveForbidErea(List<Object> insertList, List<Object> updateList, TbCityForbidRidingInfoEntity entity) {
        if (insertList.size() > 0) {
            this.ridingManageDao.bachInsert(insertList);
        }

        if (updateList.size() > 0) {
            this.ridingManageDao.bachUpdate(updateList);
        }

        if (entity != null && entity.getId() != null) {
            this.ridingManageDao.updateEntity(entity);
        }

    }

    public void cancelForbidArea(TbForbidDetailInfoEntity entity) {
        Map<String, Object> map = new HashMap();
        map.put("id", entity.getForbidId());
        map.put("cityCode", entity.getCityCode());
        TbCityForbidRidingInfoEntity rentity = new TbCityForbidRidingInfoEntity();
        List<TbCityForbidRidingInfoEntity> list = this.ridingManageDao.queryEntity(rentity, " id=:id and city_code=:cityCode", map);
        if (list != null && list.size() > 0) {
            rentity = (TbCityForbidRidingInfoEntity)list.get(0);
            if ("0".equals(entity.getStatus()) || "1".equals(entity.getStatus())) {
                rentity.setStatus(entity.getStatus());
                this.ridingManageDao.updateEntity(rentity);
                if ("0".equals(entity.getStatus())) {
                    this.ridingManageDao.deleteForbidEreaByForbidId(entity);
                }
            }
        }

    }

    public List<Map<String, Object>> queryAllCityForbid() {
        return this.ridingManageDao.queryAllCityForbid();
    }

    /**
     * 删除路书所有日程信息
     */
    public void deleteRoute(TbGuideRouteInfoDto dto){
        ridingManageDao.deleteRoute(dto);
    }

    /**
     * 批量更新
     * @return
     */
    public void bachUpdate(List<Object> list){
        ridingManageDao.bachUpdate(list);
    }

    /**
     * 批量插入
     * @return
     */
    public void bachInsert(List<Object> list){
        ridingManageDao.bachInsert(list);
    }

    /**
     * 公共插入实体数据
     * @param entity
     */
    public <T> void addEntity(T entity,boolean bl){
        ridingManageDao.addEntity(entity,bl);
    }

    /**
     * 公共修改实体数据
     * @param entity
     */
    public <T> void updateEntity(T entity){
        ridingManageDao.updateEntity(entity);
    }

    /**
     * 公共查询数据
     * @param entity
     */
    public <T> List<T> queryEntity(T entity,String whereSql,Map<String,Object> map){
        return ridingManageDao.queryEntity(entity,whereSql,map);
    }

    /**
     * 公共根据id查询数据
     * @param entity
     */
    public <T> T queryEntity(T entity,String id) {
        return ridingManageDao.queryEntity(entity,id);
    }

    /**
     * 公共根据id查询数据  查询其他库表
     * @param entity
     */
    public <T> T queryEntity(T entity,String id,String dataBase){
        return ridingManageDao.queryEntity(entity,id,dataBase);
    }



    public List<MapServerDataDto> queryMapData(MapServerDataDto param) {
        return this.ridingManageDao.queryMapData(param);
    }

    public List<TerminalDataDto> queryTerminalData(TerminalDataDto param) {
        return this.ridingManageDao.queryTerminalData(param);
    }

    public PagingBean queryAllCityForbidInfo(CityForbidRidingInfoDto dto) {
        return this.ridingManageDao.queryAllCityForbidInfo(dto);
    }
    public PagingBean queryAllTravelGuideInfo(TbRidingGuideInfoDto dto) {
        return this.ridingManageDao.queryAllTravelGuideInfo(dto);
    }

    public TbCityForbidRidingInfoEntity querySingCityForbidInfo(CityForbidRidingInfoDto dto) {
        return this.ridingManageDao.querySingCityForbidInfo(dto);
    }

    public List<TbForbidDetailInfoEntity> queryForbidEreaInfo(ForbidDetailInfoDto dto) {
        return this.ridingManageDao.queryForbidEreaInfo(dto);
    }

    public void deleteForbidEreaByForbidId(TbForbidDetailInfoEntity dto) {
        this.ridingManageDao.deleteForbidEreaByForbidId(dto);
    }

    /**
     * 查询路书下所有线路及途经点数据 根据路书id
     * @return
     */
    public List<TbGuideRouteInfoDto> queryGuideRouteInfo(TbGuideRouteInfoDto dto,int type){
        return ridingManageDao.queryGuideRouteInfo(dto,type);
    }

    /**
     * 查询用户所在骑行队伍信息
     * @return
     */
    public List<TbCreateTeamInfoEntity> queryRidingTeamInfo(TeamPersonnelInfoDto paramDto){
        return ridingManageDao.queryRidingTeamInfo(paramDto);
    }

    /**
     * 新增或修改路书线路及途经点
     * @return
     */
    public void addOrUpdGuideRouteData(String dto){
        RegUtil regUtil=RegUtil.getSingleton();
        //打印日志对象
        Logger log= LoggerFactory.getLogger(RidingTeamManageServiceImpl.class);
        JSONObject jsObj=JSON.parseObject(dto);
        JSONArray arrObj= JSON.parseArray(jsObj.getString("arrObj"));
        log.info(">>>>>>>>>>>dto:"+dto);
        //查询活动
        TbRidingGuideInfoEntity ridingGuide =queryEntity(new TbRidingGuideInfoEntity(), jsObj.getString("id"));
        if(ridingGuide==null){//活动不存在
            return;
        }
        int allAboutdis=0;
        for (int i=0;i<arrObj.size();i++){
            int routeAboutdis=0,routeTime=0;
            List<Object> updPointList=new ArrayList<Object>();
            List<Object> addPointList=new ArrayList<Object>();
            JSONObject jsonObj = arrObj.getJSONObject(i);
            if(i==0){
                //先删除
                TbGuideRouteInfoDto grdto=new TbGuideRouteInfoDto();
                grdto.setGuideId(jsonObj.getIntValue("guideId"));
                deleteRoute(grdto);
            }
            //区分添加和修改  批量操作
            TbGuideRouteInfoEntity entity = regUtil.mapToEntity(jsonObj.getInnerMap(), TbGuideRouteInfoEntity.class);
            entity.setId(null);
            entity.setCreateTime(DateUtils.getCurrentStamp());
            entity.setCreateUser("system");
            log.info(">>>>>>>>>>>entity:"+entity);
            addEntity(entity,true);
            JSONArray pointList= jsonObj.getJSONArray("pointList");
            for(int j=0;j<pointList.size();j++){//途经点
                JSONObject jsonObj1 = pointList.getJSONObject(j);
                TbRoutePointInfoEntity entity1 = regUtil.mapToEntity(jsonObj1.getInnerMap(), TbRoutePointInfoEntity.class);
                entity1.setId(null);
                entity1.setRouteId(entity.getId());
                entity1.setAboutdis(regUtil.isNull(entity1.getAboutdis())?"0":entity1.getAboutdis());
                entity1.setRidingtime(regUtil.isNull(entity1.getRidingtime())?"0":entity1.getRidingtime());
                addPointList.add(entity1);
                routeAboutdis+=Integer.parseInt(regUtil.isNull(entity1.getAboutdis())?"0":entity1.getAboutdis());
                routeTime+=Integer.parseInt(regUtil.isNull(entity1.getRidingtime())?"0":entity1.getRidingtime());
            }
            allAboutdis+=routeAboutdis;
            entity.setAboutdis(routeAboutdis+"");
            entity.setRidingtime(routeTime+"");
            log.info(">>>>>>>>>>>addPointList:"+addPointList+"=====addRouteEntity:"+entity);
            updateEntity(entity);
            bachInsert(addPointList);
        }
        ridingGuide.setAboutdis(allAboutdis+"");
        ridingGuide.setStatus(jsObj.getIntValue("status"));
        ridingGuide.setRidingtime(jsObj.getString("allDay"));
        updateEntity(ridingGuide);
    }

    /**
     * 查询我的路书
     * @return
     */
    public PagingBean queryMyRoadBook(TbRidingGuideInfoDto dto){
        return ridingManageDao.queryMyRoadBook(dto);
    }
}
