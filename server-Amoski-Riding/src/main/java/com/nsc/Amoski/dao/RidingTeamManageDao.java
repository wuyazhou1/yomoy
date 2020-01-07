package com.nsc.Amoski.dao;


import com.nsc.Amoski.dto.RidingInfoDto;
import com.nsc.Amoski.dto.TbRidingGuideInfoDto;
import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.*;

import java.util.List;
import java.util.Map;

public interface RidingTeamManageDao {

    /**
     * 插入单次总骑行数据
     * @param entity
     */
    void addRidingInfo(TbRidingInfoEntity entity);

    /**
     * 插入单次总骑行详细数据
     * @param entity
     */
    void addRidingInfoDetail(TbRidingInfoDetailEntity entity);

    /**
     * 插入路书数据
     * @param entity  TB_RIDING_TRAVEL_PLAN
     */
    void addRidingTravelPlan(TbRidingTravelPlanEntity entity);

    /**
     * 更新单次骑行数据
     * @param entity
     */
    void updateRidingInfo(TbRidingInfoEntity entity);
    /**
     * 更新单次骑行详细数据
     * @param entity
     */
    void updateRidingInfoDetail(TbRidingInfoDetailEntity entity);

    /**
     * 查询所有骑行总信息
     * @return
     */
    RidingInfoDto queryRidingInfo(RidingInfoDto param);

    /**
     * 查询用户骑行排名
     * @return
     */
    RidingInfoDto queryUserRidingRank(RidingInfoDto param);

    /**
     * 查询用户骑行次数统计
     * @return
     */
    List<RidingInfoDto> queryUserRidingCountByMonth(RidingInfoDto param);

    /**
     * 查询最远距离最多次数本人骑行数据统计
     * @param param
     * @return
     */
    List<RidingInfoDto> queryUserDisCountRidingInfo(RidingInfoDto param);

    /**
     * 查询单次骑行所有数据
     * @param param
     * @return
     */
    List<RidingInfoDto> queryAllRidingInfo(RidingInfoDto param);

    /**
     * 查询所有禁摩城市
     * @return
     */
    List<Map<String, Object>> queryAllCityForbid();

    /**
     * 批量更新
     * @return
     */
    void bachUpdate(List<Object> list);

    /**
     * 批量插入
     * @return
     */
    void bachInsert(List<Object> list);

    /**
     * 删除路书所有日程信息
     */
    void deleteRoute(TbGuideRouteInfoDto dto);

    /**
     * 公共插入实体数据
     * @param entity
     */
    <T> void addEntity(T entity,boolean bl);

    /**
     * 公共修改实体数据
     * @param entity
     */
    <T> void updateEntity(T entity);

    /**
     * 公共查询数据
     * @param entity
     */
    <T> List<T> queryEntity(T entity, String whereSql, Map<String,Object> map);

    /**
     * 查询根据最近距离排序数据
     * @return
     */
    PagingBean queryDistanceGuide(TbRidingGuideInfoDto dto);

    /**
     * 公共根据id查询数据
     * @param entity
     */
    <T> T queryEntity(T entity,String id);

    /**
     * 公共根据id查询数据  查询其他库表
     * @param entity
     */
    <T> T queryEntity(T entity,String id,String dataBase);

    /**
     * 查询猎鹰服务 根据高德key查询
     * @return
     */
    List<MapServerDataDto> queryMapData(MapServerDataDto param);

    /**
     * 查询猎鹰终端 根据服务id和终端id查询
     * @return
     */
    List<TerminalDataDto> queryTerminalData(TerminalDataDto param);

    /**
     * 查询所有城市的禁摩信息
     * @return
     */
    PagingBean queryAllCityForbidInfo(CityForbidRidingInfoDto dto);

    /**
     * 查询所有路书信息
     * @return
     */
    PagingBean queryAllTravelGuideInfo(TbRidingGuideInfoDto dto);

    /**
     * 查询单个城市的禁摩信息根据城市code
     * @return
     */
    TbCityForbidRidingInfoEntity querySingCityForbidInfo(CityForbidRidingInfoDto dto);

    /**
     * 查询城市禁摩区域详情
     * @return
     */
    List<TbForbidDetailInfoEntity> queryForbidEreaInfo(ForbidDetailInfoDto dto);
    /**
     * 删除禁摩区域通过城市code
     * @return
     */
    void deleteForbidEreaByForbidId(TbForbidDetailInfoEntity dto);

    /**
     * 查询路书下所有线路及途经点数据 根据路书id
     * @return
     */
    List<TbGuideRouteInfoDto> queryGuideRouteInfo(TbGuideRouteInfoDto dto,int type);

    /**
     * 查询用户所在骑行队伍信息
     * @return
     */
    List<TbCreateTeamInfoEntity> queryRidingTeamInfo(TeamPersonnelInfoDto paramDto);

    /**
     * 查询我的路书
     * @return
     */
    PagingBean queryMyRoadBook(TbRidingGuideInfoDto dto);

}
