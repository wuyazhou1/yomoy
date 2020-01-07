package com.nsc.Amoski.service;

import com.nsc.Amoski.dto.TeamPersonnelInfoDto;

import java.util.List;
import java.util.Map;

public interface RidingTeamManageService {

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
     * 公共插入实体数据
     * @param entity
     */
    <T> void addEntity(T entity, boolean bl);

    /**
     * 公共修改实体数据
     * @param entity
     */
    <T> void updateEntity(T entity);

    /**
     * 公共查询数据
     * @param entity
     */
    <T> List<T> queryEntity(T entity, String whereSql, Map<String, Object> map);

    /**
     * 公共修改数据
     * @param entity
     */
    <T> void updateEntityByWhereSql(T entity,String whereSql,Map<String,Object> map);

    /**
     * 公共根据id查询数据
     * @param entity
     */
    <T> T queryEntity(T entity, String id);


    /**
     * 公共根据id查询数据  查询其他库表
     * @param entity
     */
    <T> T queryEntity(T entity, String id, String dataBase);

    /**
     * 公共根据id查删除数据
     * @param entity
     */
    <T> void deleteEntity(T entity,String id);

    /**
     * 查询单个队伍信息及人员信息
     * @return
     */
    List<Map<String,Object>> queryTeamInfoAndPersonInfo(TeamPersonnelInfoDto paramDto);

    /**
     * 公共根据ids删除多条记录
     * @param entity
     */
    <T> void deleteDataByIds(T entity,String ids);

}