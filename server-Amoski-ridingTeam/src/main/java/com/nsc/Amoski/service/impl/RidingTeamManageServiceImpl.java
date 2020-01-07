package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.RidingTeamManageDao;
import com.nsc.Amoski.dto.TeamPersonnelInfoDto;
import com.nsc.Amoski.service.RidingTeamManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class RidingTeamManageServiceImpl implements RidingTeamManageService {

    @Autowired
    RidingTeamManageDao ridingManageDao;

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
     * 公共修改数据
     * @param entity
     */
    public <T> void updateEntityByWhereSql(T entity,String whereSql,Map<String,Object> map){
        ridingManageDao.updateEntityByWhereSql(entity,whereSql,map);
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

    /**
     * 公共根据id查删除数据
     * @param entity
     */
    public <T> void deleteEntity(T entity,String id){
        ridingManageDao.deleteEntity(entity,id);
    }

    /**
     * 查询单个队伍信息及人员信息
     * @return
     */
    public List<Map<String,Object>> queryTeamInfoAndPersonInfo(TeamPersonnelInfoDto paramDto){
        return ridingManageDao.queryTeamInfoAndPersonInfo(paramDto);
    }

    /**
     * 公共根据ids删除多条记录
     * @param entity
     */
    public <T> void deleteDataByIds(T entity,String ids){
        ridingManageDao.deleteDataByIds(entity,ids);
    }

}
