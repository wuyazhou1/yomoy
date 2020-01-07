package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.RidingManageDao;
import com.nsc.Amoski.dto.RidingInfoDto;
import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.service.RidingManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class RidingManageServiceImpl implements RidingManageService {

    @Autowired
    RidingManageDao ridingManageDao;

    /**
     * 插入单次总骑行数据
     * @param entity
     */
    @Override
    public void addRidingInfo(TbRidingInfoEntity entity) {
        ridingManageDao.addRidingInfo(entity);
    }

    /**
     * 插入路书数据
     * @param entity  TB_RIDING_TRAVEL_PLAN
     */
    public void addRidingTravelPlan(TbRidingTravelPlanEntity entity) {
        ridingManageDao.addRidingTravelPlan(entity);
    }

    /**
     * 记录骑行开始数据
     * @param entity 骑行总汇数据
     * @param detailEntity 骑行详情数据
     */
    public void addInitRidingData(TbRidingInfoEntity entity,TbRidingInfoDetailEntity detailEntity){
        ridingManageDao.addRidingInfo(entity);
        detailEntity.setRidingId(entity.getId());
        ridingManageDao.addRidingInfoDetail(detailEntity);
    }

    /**
     * 更新所有骑行数据
     * @param entity 骑行总汇数据
     * @param detailEntity 骑行详情数据
     */
    public void updateAllRidingData(TbRidingInfoEntity entity,TbRidingInfoDetailEntity detailEntity){
        ridingManageDao.updateRidingInfo(entity);
        if(detailEntity.getId()!=null){
            ridingManageDao.updateRidingInfoDetail(detailEntity);
        }else{
            ridingManageDao.addEntity(detailEntity,false);
        }
    }

    /**
     * 插入单次总骑行详细数据
     * @param entity
     */
    @Override
    public void addRidingInfoDetail(TbRidingInfoDetailEntity entity) {
        ridingManageDao.addRidingInfoDetail(entity);
    }

    /**
     * 更新单次骑行数据
     * @param entity
     */
    @Override
    public void updateRidingInfo(TbRidingInfoEntity entity) {
        ridingManageDao.updateRidingInfo(entity);
    }

    /**
     * 更新单次骑行详细数据
     * @param entity
     */
    @Override
    public void updateRidingInfoDetail(TbRidingInfoDetailEntity entity) {
        ridingManageDao.updateRidingInfoDetail(entity);
    }

    /**
     * 查询所有骑行总信息
     * @return
     */
    @Override
    public RidingInfoDto queryRidingInfo(RidingInfoDto param) {
        return ridingManageDao.queryRidingInfo(param);
    }

    /**
     * 查询单次骑行所有数据
     * @param param
     * @return
     */
    @Override
    public List<RidingInfoDto> queryAllRidingInfo(RidingInfoDto param) {
        return ridingManageDao.queryAllRidingInfo(param);
    }

    /**
     * 增加禁摩区域
     * @param insertList 插入数据
     * @param updateList  更新数据
     */
    public void saveForbidErea(List<Object> insertList,List<Object> updateList,TbCityForbidRidingInfoEntity entity){
        if(insertList.size()>0){
            ridingManageDao.bachInsert(insertList);
        }
        if(updateList.size()>0){
            ridingManageDao.bachUpdate(updateList);
        }
        if(entity!=null&&entity.getId()!=null){
            ridingManageDao.updateEntity(entity);
        }
        /*TbCityForbidRidingInfoEntity rentity=new TbCityForbidRidingInfoEntity();
        RegUtil regUtil=RegUtil.getSingleton();
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("id",entity.getForbidId());
        map.put("cityCode",entity.getCityCode());
        List<TbCityForbidRidingInfoEntity> list = ridingManageDao.queryEntity(rentity, " id=:id and city_code=:cityCode", map);
        if(list!=null&&list.size()>0){
            rentity=list.get(0);
            rentity.setForbidAreaCount(regUtil.isNull(rentity.getForbidAreaCount())?"1":Integer.parseInt(rentity.getForbidAreaCount())+1+"");
            entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            entity.setCreateUser("system");
            ridingManageDao.addEntity(entity);
            ridingManageDao.updateEntity(rentity);
        }*/
    }

    /**
     * 消禁
     * @param entity
     */
    public void cancelForbidArea(TbForbidDetailInfoEntity entity){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("id",entity.getForbidId());
        map.put("cityCode",entity.getCityCode());
        TbCityForbidRidingInfoEntity rentity = new TbCityForbidRidingInfoEntity();
        List<TbCityForbidRidingInfoEntity> list = ridingManageDao.queryEntity(rentity, " id=:id and city_code=:cityCode", map);
        if(list!=null&&list.size()>0){
            rentity=list.get(0);
            if("0".equals(entity.getStatus())||"1".equals(entity.getStatus())){
                rentity.setStatus(entity.getStatus());
                ridingManageDao.updateEntity(rentity);
                if("0".equals(entity.getStatus())){//消禁
                    ridingManageDao.deleteForbidEreaByForbidId(entity);
                }
            }
        }

    }

    /**
     * 查询所有禁摩城市
     * @return
     */
    public List<Map<String, Object>> queryAllCityForbid(){
        return ridingManageDao.queryAllCityForbid();
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
     * 查询猎鹰服务 根据高德key查询
     * @return
     */
    public List<MapServerDataDto> queryMapData(MapServerDataDto param){
        return ridingManageDao.queryMapData(param);
    }

    /**
     * 查询猎鹰终端 根据服务id和终端id查询
     * @return
     */
    public List<TerminalDataDto> queryTerminalData(TerminalDataDto param){
        return ridingManageDao.queryTerminalData(param);
    }

    /**
     * 查询所有城市的禁摩信息
     * @return
     */
    public PagingBean queryAllCityForbidInfo(CityForbidRidingInfoDto dto){
        return ridingManageDao.queryAllCityForbidInfo(dto);
    }

    /**
     * 查询单个城市的禁摩信息根据城市code
     * @return
     */
    public TbCityForbidRidingInfoEntity querySingCityForbidInfo(CityForbidRidingInfoDto dto){
        return ridingManageDao.querySingCityForbidInfo(dto);
    }

    /**
     * 查询城市禁摩区域详情
     * @return
     */
    public List<TbForbidDetailInfoEntity> queryForbidEreaInfo(ForbidDetailInfoDto dto){
        return ridingManageDao.queryForbidEreaInfo(dto);
    }
    /**
     * 删除禁摩区域通过城市code
     * @return
     */
    public void deleteForbidEreaByForbidId(TbForbidDetailInfoEntity dto){
        ridingManageDao.deleteForbidEreaByForbidId(dto);
    }
}
