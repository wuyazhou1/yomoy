package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.RidingTeamManageDao;
import com.nsc.Amoski.dto.CreateTeamInfoDto;
import com.nsc.Amoski.dto.TeamPersonnelInfoDto;
import com.nsc.Amoski.entity.TbCityForbidRidingInfoEntity;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.BeanCopyUtil;
import com.nsc.Amoski.util.RegUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RidingTeamManageDaoImpl extends GenericRepositoryImpl implements RidingTeamManageDao {



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
        logger.info(">>>>>>>>>>>>queryEntity  entity:"+whereSql+"=====map:"+map);
        List<T> list = this.query(entity, whereSql, map);
        logger.info(">>>>>>>>>>>>queryEntity result:"+list);
        return list;
    }

    /**
     * 公共修改数据
     * @param entity
     */
    public <T> void updateEntityByWhereSql(T entity,String whereSql,Map<String,Object> map){
        logger.info(">>>>>>>>>>>>updateEntityByWhereSql  entity:"+entity+"++======id:"+whereSql);
        Class<?> clazz = entity.getClass();
        String table = clazz.getAnnotation(Table.class).name();
        String sql="update "+ table + " set " + whereSql;
        logger.info(">>>>>>>>>>>>updateEntityByWhereSql sql:"+sql);
        this.jdbcTemplate.update(sql,map);
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
        logger.info(">>>>>>>>>>>>queryEntity id result:"+list);
        return list!=null&&list.size()>0?list.get(0):null;
    }

    /**
     * 公共根据id查询数据  查询其他库表
     * @param entity
     */
    public <T> T queryEntity(T entity,String id,String dataBase) {
        logger.info(">>>>>>>>>>>>queryEntity  entity:"+entity+"++======dataBase:"+dataBase);
        String whereSql=" id=:id ";
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("id",id);
        List<T> list = this.query(entity, whereSql, map,dataBase);
        logger.info(">>>>>>>>>>>>queryEntity dataBase result:"+list);
        return list!=null&&list.size()>0?list.get(0):null;
    }

    /**
     * 公共根据id删除数据
     * @param entity
     */
    public <T> void deleteEntity(T entity,String id) {
        logger.info(">>>>>>>>>>>>deleteEntity  entity:"+entity+"++======id:"+id);
        String whereSql=" id=:id ";
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("id",id);
        Class<?> clazz = entity.getClass();
        String table = clazz.getAnnotation(Table.class).name();
        String sql="delete from "+ table + " where " + whereSql;
        logger.info(">>>>>>>>>>>>deleteEntity sql:"+sql);
        this.jdbcTemplate.update(sql,map);
    }

    /**
     * 批量更新
     * @return
     */
    public void bachUpdate(List<Object> list){
        logger.info(">>>>bachUpdate>>>>>list"+list+">>>>>>>");
        this.updateAll(list);
    }

    /**
     * 批量插入
     * @return
     */
    public void bachInsert(List<Object> list){
        logger.info(">>>>bachInsert>>>>>list"+list+">>>>>>>");
        this.saveAll(list);
    }

    /**
     * 查询单个有效队伍信息及人员信息
     * @return
     */
    public List<Map<String,Object>> queryTeamInfoAndPersonInfo(TeamPersonnelInfoDto paramDto){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder sql=new StringBuilder("select ri.*,pi.*,pi.team_id id,pi.team_id teamId from TB_CREATE_TEAM_INFO ri left join TB_TEAM_PERSONNEL_INFO pi on ri.id=pi.team_id ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and ri.valid_end_time>sysdate and ri.status=1 ");
        Map<String,Object> map=new HashMap<String, Object>();
        if(!regUtil.isNull(paramDto.getTeamId())){
            whereSql.append(" and ri.id=:teamId ");
            map.put("teamId",paramDto.getTeamId());
        }
        if(!regUtil.isNull(paramDto.getTeamCode())){
            whereSql.append(" and ri.team_code=:teamCode ");
            map.put("teamCode",paramDto.getTeamCode());
        }
        if(!regUtil.isNull(paramDto.getMemberId())){
            whereSql.append(" and pi.member_id=:userId ");
            map.put("userId",paramDto.getMemberId());
        }
        //查询条件
        String querySql=sql.append(whereSql).toString();
        logger.info(">>>>queryTeamInfoAndPersonInfo>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<Map<String,Object>> list=this.jdbcTemplate.queryForList(querySql, map);
        logger.info(">>>>queryTeamInfoAndPersonInfo>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list;
    }

    /**
     * 公共根据ids删除多条记录
     * @param entity
     */
    public <T> void deleteDataByIds(T entity,String ids){
        RegUtil regUtil=RegUtil.getSingleton();
        Class<?> clazz = entity.getClass();
        String table = clazz.getAnnotation(Table.class).name();
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("ids",ids.endsWith(",")?ids:ids+",");
        String whereSql=" where 1!=1 ";
        if(!regUtil.isNull(ids)){
            whereSql+=" or exists (select * from (select regexp_substr(:ids,'[^,]+',1,rownum) did from dual"+
                    " connect by rownum<=length(regexp_replace(:ids,'[^,]+'))+1) b where id=b.did) ";
        }
        String excSql="delete from "+table+whereSql;
        logger.info(">>>>>>>>>>>>>>deleteDataByIds sql:"+excSql+"======map:"+map);
        this.jdbcTemplate.update(excSql,map);
    }

}
