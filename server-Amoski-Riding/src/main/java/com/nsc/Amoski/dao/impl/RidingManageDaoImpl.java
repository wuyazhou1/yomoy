package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.RidingManageDao;
import com.nsc.Amoski.dto.RidingInfoDto;
import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.RegUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RidingManageDaoImpl extends GenericRepositoryImpl implements RidingManageDao {



    /**
     * 插入单次总骑行数据
     * @param entity
     */
    public void addRidingInfo(TbRidingInfoEntity entity) {
        logger.info(">>>>>>>>>>>> addRidingInfo  insert into before list  entity:"+entity);
        this.saveEntity(entity);
    }
    /**
     * 插入单次总骑行详细数据
     * @param entity
     */
    public void addRidingInfoDetail(TbRidingInfoDetailEntity entity) {
        logger.info(">>>>>>>>>>>>addRidingInfoDetail insert into before list  entity:"+entity);
        this.save(entity);
    }

    /**
     * 插入路书数据
     * @param entity  TB_RIDING_TRAVEL_PLAN
     */
    public void addRidingTravelPlan(TbRidingTravelPlanEntity entity) {
        logger.info(">>>>>>>>>>>>addRidingTravelPlan insert into before list  entity:"+entity);
        this.save(entity);
    }

    /**
     * 更新单次骑行数据
     * @param entity
     */
    public void updateRidingInfo(TbRidingInfoEntity entity) {
        logger.info(">>>>>>>>>>>>updateRidingInfo insert into before list  entity:"+entity);
        this.update(entity);
    }
    /**
     * 更新单次骑行详细数据
     * @param entity
     */
    public void updateRidingInfoDetail(TbRidingInfoDetailEntity entity) {
        logger.info(">>>>>>>>>>>>updateRidingInfoDetail insert into before list  entity:"+entity);
        this.update(entity);
    }





    /**
     * 查询用户所有骑行汇总信息
     * @return
     */
    public RidingInfoDto queryRidingInfo(RidingInfoDto param){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder sql=new StringBuilder("select count(1) allRidingCount,sum(i.TOTAL_DISTANCE) allTotalDis,sum(i.TOTAL_TIME) allTotalTime,max(i.TRACK_IMG_URL) trackImgUrl from TB_RIDING_INFO i ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and i.member_id=:userId ");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("userId",param.getMemberId());
        if(!regUtil.isNull(param.getQueryDate())){
            map.put("queryDate",param.getQueryDate());
            whereSql.append(" and to_char(i.create_time,'yyyy-mm')=:queryDate");
        }
        //查询条件
        String querySql=sql.append(whereSql).toString();
        querySql=querySql+" group by i.member_id";
        logger.info(">>>>queryRidingInfo>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<RidingInfoDto> list=this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<RidingInfoDto>(RidingInfoDto.class));
        logger.info(">>>>queryRidingInfo>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list.size()>0?list.get(0):null;
    }

    /**
     * 查询单次骑行所有数据
     * @param param
     * @return
     */
    public List<RidingInfoDto> queryAllRidingInfo(RidingInfoDto param){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder sql=new StringBuilder("select i.*,d.* from TB_RIDING_INFO i inner join TB_RIDING_INFO_DETAIL d on i.id=d.RIDING_ID ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and i.member_id=:userId ");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("userId",param.getMemberId());
        if(!regUtil.isNull(param.getId())){
            map.put("id",param.getId());
            whereSql.append(" and i.id=:id");
        }
        if(!regUtil.isNull(param.getQueryDate())){
            map.put("queryDate",param.getQueryDate());
            whereSql.append(" and to_char(i.create_time,'yyyy-mm')=:queryDate");
        }

        //查询条件
        String querySql=sql.append(whereSql).toString();
        logger.info(">>>>queryAllRidingInfo>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<RidingInfoDto> list=this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<RidingInfoDto>(RidingInfoDto.class));
        logger.info(">>>>queryAllRidingInfo>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list;
    }

    /**
     * 插入高德地图创建猎鹰服务数据
     * @param entity
     */
    public void addMapServerData(TbMapServerDataEntity entity) {
        logger.info(">>>>>>>>>>>>addMapServerData insert into before list  entity:"+entity);
        this.save(entity);
    }

    /**
     * 插入猎鹰服务创建终端数据
     * @param entity
     */
    public void addTerminalData(TbTerminalDataEntity entity) {
        logger.info(">>>>>>>>>>>>addTerminalData insert into before list  entity:"+entity);
        this.save(entity);
    }
    /**
     * 公共插入实体数据
     * @param entity
     */
    public <T> void addEntity(T entity,boolean bl) {
        logger.info(">>>>>>>>>>>>addEntity insert into before list  entity:"+entity);
        if(bl){
            this.saveEntity(entity);
        }else{
            this.save(entity);
        }
    }
    /**
     * 公共修改实体数据
     * @param entity
     */
    public <T> void updateEntity(T entity) {
        logger.info(">>>>>>>>>>>>updateEntity insert into before list  entity:"+entity);
        this.update(entity);
    }

    /**
     * 公共查询数据
     * @param entity
     */
    public <T> List<T> queryEntity(T entity,String whereSql,Map<String,Object> map) {
        logger.info(">>>>>>>>>>>>queryEntity  entity:"+entity);
        return this.query(entity,whereSql,map);
    }

    /**
     * 公共根据id查询数据
     * @param entity
     */
    public <T> T queryEntity(T entity,String id) {
        logger.info(">>>>>>>>>>>>queryEntity  entity:"+entity);
        String whereSql=" id=:id ";
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("id",id);
        List<T> list = this.query(entity, whereSql, map);
        return list!=null&&list.size()>0?list.get(0):null;
    }

    /**
     * 查询猎鹰服务 根据高德key查询
     * @return
     */
    public List<MapServerDataDto> queryMapData(MapServerDataDto param){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder sql=new StringBuilder("select * from TB_MAP_SERVER_DATA i ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 ");
        Map<String,Object> map=new HashMap<String, Object>();
        if(!regUtil.isNull(param.getMapKey())){
            map.put("mapKey",param.getMapKey());
            whereSql.append(" and i.map_key=:mapKey");
        }
        //查询条件
        String querySql=sql.append(whereSql).toString();
        querySql=querySql+" order by i.create_time";
        logger.info(">>>>queryMapData>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<MapServerDataDto> list=this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<MapServerDataDto>(MapServerDataDto.class));
        logger.info(">>>>queryMapData>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list;
    }
    /**
     * 查询猎鹰终端 根据服务id和终端id查询
     * @return
     */
    public List<TerminalDataDto> queryTerminalData(TerminalDataDto param){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder sql=new StringBuilder("select * from TB_TERMINAL_DATA i ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and i.server_id=:serverId and i.member_id=:memberId ");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("serverId",param.getServerId());
        map.put("memberId",param.getMemberId());
        if(!regUtil.isNull(param.getTerminalId())){
            map.put("terminalId",param.getTerminalId());
            whereSql.append(" and i.terminal_id=:terminalId");
        }
        //查询条件
        String querySql=sql.append(whereSql).toString();
        querySql=querySql+" order by i.create_time";
        logger.info(">>>>queryTerminalData>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<TerminalDataDto> list=this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<TerminalDataDto>(TerminalDataDto.class));
        logger.info(">>>>queryTerminalData>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list;
    }

    /**
     * 查询所有城市的禁摩信息
     * @return
     */
    public PagingBean queryAllCityForbidInfo(CityForbidRidingInfoDto dto){
        StringBuilder sql=new StringBuilder("select * from TB_CITY_FORBID_RIDING_INFO ri ");
        StringBuilder countSql=new StringBuilder("select count(*) from TB_CITY_FORBID_RIDING_INFO ri");
        StringBuilder whereSql=new StringBuilder(" where 1=1 ");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("pageSize",dto.getPageSize());
        map.put("start",dto.getStart());
        RegUtil regUtil=RegUtil.getSingleton();
        if(!regUtil.isNull(dto.getCityName())){//城市
            whereSql.append(" and ri.city_name=:cityName");
            map.put("cityName",dto.getCityName());
        }
        if(!regUtil.isNull(dto.getStatus())){//状态(0:消禁 1:禁止)
            whereSql.append(" and ri.status=:status");
            map.put("status",dto.getStatus());
        }
        //查询条件
        String querySql=sql.append(whereSql).toString(),queryCountSql=countSql.append(whereSql).toString();
        querySql=querySql+" order by ri.status desc,ri.create_time desc";
        logger.info(">>>>queryAllCityForbidInfo>>>>>queryCount sql:"+queryCountSql+">>>>>>>paramMap:"+map.toString());
        logger.info(">>>>queryAllCityForbidInfo>>>>>query sql:"+querySql+">>>>>>>");
        int count= this.jdbcTemplate.queryForObject(queryCountSql,map,Integer.class);
        List<TbCityForbidRidingInfoEntity> list=this.jdbcTemplate.query(pageSql(querySql,map), map, new BeanPropertyRowMapper<TbCityForbidRidingInfoEntity>(TbCityForbidRidingInfoEntity.class));
        PagingBean bean=new PagingBean();
        bean.setData(list);
        bean.setRecordsTotal(count);
        bean.setRecordsFiltered(count);
        logger.info(">>>>queryAllCityForbidInfo>>>>>where"+whereSql+">>>>>>>result:"+bean.toString());

        return bean;
    }

    /**
     * 查询所有禁摩城市
     * @return
     */
    public List<Map<String, Object>> queryAllCityForbid(){
        String sql="select ri.city_code cityCode,ri.city_name cityName from TB_CITY_FORBID_RIDING_INFO ri group by ri.city_code,ri.city_name";
        List<Map<String, Object>> lists = this.jdbcTemplate.queryForList(sql, new HashMap<String, String>());
        return lists;
    }

    /**
     * 批量更新
     * @return
     */
    public void bachUpdate(List<Object> list){
        this.updateAll(list);
    }

    /**
     * 批量插入
     * @return
     */
    public void bachInsert(List<Object> list){
        this.saveAll(list);
    }

    /**
     * 查询单个城市的禁摩信息根据城市code
     * @return
     */
    public TbCityForbidRidingInfoEntity querySingCityForbidInfo(CityForbidRidingInfoDto dto){
        StringBuilder sql=new StringBuilder("select * from TB_CITY_FORBID_RIDING_INFO ri ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and ri.city_code=:cityCode");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("cityCode",dto.getCityCode());
        //查询条件
        String querySql=sql.append(whereSql).toString();
        logger.info(">>>>querySingCityForbidInfo>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<TbCityForbidRidingInfoEntity> list=this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<TbCityForbidRidingInfoEntity>(TbCityForbidRidingInfoEntity.class));
        logger.info(">>>>querySingCityForbidInfo>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list!=null&&list.size()>0?list.get(0):null;
    }

    /**
     * 查询城市禁摩区域详情
     * @return
     */
    public List<TbForbidDetailInfoEntity> queryForbidEreaInfo(ForbidDetailInfoDto dto){
        StringBuilder sql=new StringBuilder("select * from TB_FORBID_DETAIL_INFO di ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and di.city_code=:cityCode");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("cityCode",dto.getCityCode());
        //查询条件
        String querySql=sql.append(whereSql).toString();
        logger.info(">>>>queryForbidEreaInfo>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<TbForbidDetailInfoEntity> list=this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<TbForbidDetailInfoEntity>(TbForbidDetailInfoEntity.class));
        logger.info(">>>>queryForbidEreaInfo>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list;
    }
    /**
     * 删除禁摩区域通过城市code
     * @return
     */
    public void deleteForbidEreaByForbidId(TbForbidDetailInfoEntity dto){
        StringBuilder sql=new StringBuilder("update TB_FORBID_DETAIL_INFO set status=:status ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and city_code=:cityCode");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("status",dto.getStatus());
        map.put("cityCode",dto.getCityCode());
        //查询条件
        String deleteSql=sql.append(whereSql).toString();
        logger.info(">>>>deleteForbidEreaByForbidId>>>>>query sql:"+deleteSql+">>>>>>>paramMap:"+map.toString());
        this.jdbcTemplate.update(deleteSql,map);
    }

}
