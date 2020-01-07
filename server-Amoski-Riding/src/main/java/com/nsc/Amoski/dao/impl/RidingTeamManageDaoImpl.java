package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.RidingTeamManageDao;
import com.nsc.Amoski.dto.RidingInfoDto;
import com.nsc.Amoski.dto.TbRidingGuideInfoDto;
import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.RegUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class RidingTeamManageDaoImpl extends GenericRepositoryImpl implements RidingTeamManageDao {



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
        StringBuilder whereSql=new StringBuilder(" where 1=1 and i.member_id=:userId and i.riding_file_url is not null ");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("userId",param.getMemberId());
        if(!regUtil.isNull(param.getRidingVehicleId())){
            map.put("ridingVehicleId",param.getRidingVehicleId());
            whereSql.append(" and i.RIDNG_vehicle_id=:ridingVehicleId");
        }
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
     * 查询用户骑行次数统计
     * @return
     */
    public List<RidingInfoDto> queryUserRidingCountByMonth(RidingInfoDto param){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder sql=new StringBuilder("select to_char(i.create_time,'yyyy-mm') queryDate,sum(i.total_distance) allTotalDis from TB_RIDING_INFO i ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and i.member_id=:userId and i.riding_file_url is not null ");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("userId",param.getMemberId());
        if(!regUtil.isNull(param.getQueryType())){
            whereSql.append(" and to_char(add_months(sysdate,-12),'yyyy-mm')<to_char(i.create_time,'yyyy-mm') and to_char(i.create_time,'yyyy-mm')<=to_char(sysdate,'yyyy-mm')");
        }else{
            whereSql.append(" and to_char(add_months(sysdate,-6),'yyyy-mm')<to_char(i.create_time,'yyyy-mm') and to_char(i.create_time,'yyyy-mm')<=to_char(sysdate,'yyyy-mm')");
        }
        //查询条件
        String querySql=sql.append(whereSql).toString();
        querySql=querySql+" group by to_char(create_time,'yyyy-mm') order  by to_char(create_time,'yyyy-mm')";
        logger.info(">>>>queryUserRidingCountByMonth>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<RidingInfoDto> list=this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<RidingInfoDto>(RidingInfoDto.class));
        logger.info(">>>>queryUserRidingCountByMonth>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list;
    }

    /**
     * 查询用户骑行排名
     * @return
     */
    @Override
    public RidingInfoDto queryUserRidingRank(RidingInfoDto param) {
        RegUtil regUtil=RegUtil.getSingleton();
        String querySql="select rn from (select t.member_id,rownum rn from (select i.member_id,sum(i.TOTAL_DISTANCE) from amoski_user.T_MEMBER_DAIL d inner join TB_RIDING_INFO i on i.member_id=d.member_id where 1=1 replaceStr  group by i.member_id order by sum(i.TOTAL_DISTANCE) desc) t) where member_id=:userId";
        RidingInfoDto dto=new RidingInfoDto();
        StringBuilder sql=new StringBuilder(querySql.replace("replaceStr",""));
        sql.append(" union all "+querySql.replace("replaceStr"," and d.address=:address"));
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("userId",param.getMemberId());
        map.put("address",param.getStartPosition());
        //查询条件
        logger.info(">>>>queryRidingInfo>>>>>query sql:"+sql.toString()+">>>>>>>paramMap:"+map.toString());
        List<Map<String, Object>> maps = this.jdbcTemplate.queryForList(sql.toString(), map);
        logger.info(">>>>queryRidingInfo>>>>>>>>>>>>result:"+maps);
        if(maps.size()>0){
            if(maps.size()==2){
                dto.setCountryRank(maps.get(0).get("rn").toString());
                dto.setAreaRank(maps.get(1).get("rn").toString());
            }else{
                dto.setCountryRank(maps.get(0).get("rn").toString());
                dto.setAreaRank(maps.get(0).get("rn").toString());
            }
        }
        return dto;
    }

    /**
     * 查询单次骑行所有数据
     * @param param
     * @return
     */
    public List<RidingInfoDto> queryAllRidingInfo(RidingInfoDto param){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder sql=new StringBuilder("select i.*,d.* from TB_RIDING_INFO i inner join TB_RIDING_INFO_DETAIL d on i.id=d.RIDING_ID ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and i.member_id=:userId and i.riding_file_url is not null ");
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
        querySql=querySql+" order by i.create_time desc";
        logger.info(">>>>queryAllRidingInfo>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<RidingInfoDto> list=this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<RidingInfoDto>(RidingInfoDto.class));
        logger.info(">>>>queryAllRidingInfo>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list;
    }

    /**
     * 查询最远距离最多次数本人骑行数据统计
     * @param param
     * @return
     */
    public List<RidingInfoDto> queryUserDisCountRidingInfo(RidingInfoDto param){
        RegUtil regUtil=RegUtil.getSingleton();
       /* StringBuilder sql=new StringBuilder("select * from (select t.member_id,0 allTotalDis,sum(t.total_distance) totalDistance,0 allRidingCount from tb_riding_info t " +
                "where t.total_distance is not null and to_char(t.create_time, 'yyyy-mm') = to_char(sysdate, 'yyyy-mm') group by t.member_id order by sum(t.total_distance) desc) rt " +
                " where rownum = 1  union all " +
                " select * from (select t.member_id,0 allTotalDis, 0 totalDistance,count(1) allRidingCount from tb_riding_info t where t.total_distance is not null " +
                "and to_char(t.create_time, 'yyyy-mm') = to_char(sysdate, 'yyyy-mm') group by t.member_id order by count(1) desc) rt where rownum = 1 union all " +
                " select t.member_id,sum(t.total_distance) allTotalDis, sum(decode(to_char(t.create_time, 'yyyy-mm'),to_char(sysdate, 'yyyy-mm'),t.total_distance,0)) totalDistance," +
                "sum(decode(to_char(t.create_time, 'yyyy-mm'),to_char(sysdate, 'yyyy-mm'),1,0)) allRidingCount from tb_riding_info t where t.total_distance is not null and t.member_id = :userId group by t.member_id");
*/
        StringBuilder sql=new StringBuilder("select NVL(sum(t.total_distance),0) allTotalDis,NVL(sum(t.total_time),0) allTotalTime,NVL((select max(tt.aa) from (select to_char(t.create_time,'yyyy-mm') dt,sum(t.total_distance) aa,1 cc " +
                "from TB_RIDING_INFO t where t.member_id=:memberId group by to_char(t.create_time,'yyyy-mm')) tt group by tt.cc),0) maxDis from TB_RIDING_INFO t where t.member_id=:memberId group by t.member_id");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("memberId",param.getMemberId());
        //查询条件
        String querySql=sql.toString();
        logger.info(">>>>queryUserDisCountRidingInfo>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<RidingInfoDto> list=this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<RidingInfoDto>(RidingInfoDto.class));
        if(list==null||list.size()==0){
            list=new ArrayList<RidingInfoDto>();
            RidingInfoDto rid=new RidingInfoDto();
            rid.setAllTotalDis(0.0);
            rid.setAllTotalTime(0);
            rid.setMaxDis(0.0);
            list.add(rid);
        }
        logger.info(">>>>queryUserDisCountRidingInfo>>>>>>>>>>>>result:"+list);
        sql=new StringBuilder("select tt.* from (select to_char(t.create_time,'yyyy-mm') ridingMonth,sum(t.total_distance) allTotalDis,sum(t.total_time) allTotalTime " +
                "from TB_RIDING_INFO t where t.member_id=:memberId group by to_char(t.create_time,'yyyy-mm')) tt " +
                "where tt.ridingMonth>to_char(ADD_MONTHS(sysdate,-4),'yyyy-mm') order by tt.ridingMonth desc");
        List<RidingInfoDto> list1=this.jdbcTemplate.query(sql.toString(), map, new BeanPropertyRowMapper<RidingInfoDto>(RidingInfoDto.class));
        for(int i=0;i<4;i++){
            boolean bl=false;
            Date dNow = new Date();   //当前时间
            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(dNow);//把当前时间赋给日历
            calendar.add(Calendar.MONTH, 0-i);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM"); //设置时间格式
            String returnStr = sdf.format(calendar.getTime()); //格式化当前时间
            RidingInfoDto rid=new RidingInfoDto();
            if(list1==null||list1.size()==0){
                rid.setAllTotalDis(0.0);
                rid.setAllTotalTime(0);
                rid.setRidingMonth(returnStr);
            }else{
                for(RidingInfoDto ldto:list1){
                    if(returnStr.equals(ldto.getRidingMonth())){
                        rid=ldto;
                        bl=true;
                        break;
                    }
                }
                if(!bl){
                    rid.setAllTotalDis(0.0);
                    rid.setAllTotalTime(0);
                    rid.setRidingMonth(returnStr);
                }
                list.add(rid);
            }
        }
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
     * 查询根据最近距离排序数据
     * @return
     */
    public PagingBean queryDistanceGuide(TbRidingGuideInfoDto dto){
        StringBuilder sql=new StringBuilder("");
        StringBuilder countSql=new StringBuilder("select count(*) from TB_RIDING_GUIDE_INFO ri");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("pageSize",dto.getPageSize());
        map.put("start",dto.getStart());
        String clum="a.id, a.title, a.bill, a.feature, a.startaddr, a.routepoint, a.strengthgrade, a.scenerygrad, a.experience, a.season, a.status, a.lookcount, a.ridingtime, a.vehicletype, a.routepointcount, a.aboutdis, a.create_time, a.create_user, a.endaddr, a.keys, a.guide_type, a.base_url, a.lng, a.lat, a.recommend, a.recommendlevel, a.simp_introduction ";
        String orderByStr=" order by lookcount desc,create_time desc ";
        if("2".equals(dto.getOrderByType())){
            sql.append("select * from (select "+clum+",sqrt((" +
                    "     ((:lng-lng)*ACOS(-1)*12656*cos(((:lng+lng)/2)*ACOS(-1)/180)/180) * "+
                    "     ((:lng-lng)*ACOS(-1)*12656*cos (((:lng+lng)/2)*ACOS(-1)/180)/180)) +" +
                    "     (((:lat-lat)*ACOS(-1)*12656/180) *" +
                    "     ((:lat-lat)*ACOS(-1)*12656/180))) distance from TB_RIDING_GUIDE_INFO a ");
            orderByStr=") i order by i.distance, i.lookcount desc";
            map.put("lng",dto.getLng());
            map.put("lat",dto.getLat());
        }else{
            sql.append("select "+clum+" from TB_RIDING_GUIDE_INFO a ");
        }
        StringBuilder whereSql=new StringBuilder(" where status=2 and guide_type=1 ");
        if(!RegUtil.getSingleton().isNull(dto.getSearchVal())){//搜索字段
            whereSql.append(" and (title like '%' || :searchVal || '%' or feature like '%' || :searchVal || '%' or routepoint like '%' || :searchVal || '%' " +
                    "or season like '%' || :searchVal || '%' or vehicletype like '%' || :searchVal || '%' or introduction like '%' || :searchVal || '%') ");
            map.put("searchVal",dto.getSearchVal());
        }
        //查询条件
        String querySql=sql.append(whereSql).append(orderByStr).toString(),queryCountSql=countSql.append(whereSql).toString();
        int count= this.jdbcTemplate.queryForObject(queryCountSql,map,Integer.class);
        logger.info(">>>>queryMapData>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<TbRidingGuideInfoEntity> list=this.jdbcTemplate.query(pageSql(querySql,map), map, new BeanPropertyRowMapper<TbRidingGuideInfoEntity>(TbRidingGuideInfoEntity.class));
        PagingBean bean=new PagingBean();
        bean.setData(list);
        bean.setRecordsTotal(count);
        bean.setRecordsFiltered(count);
        logger.info(">>>>queryMapData>>>>>where"+whereSql+">>>>>>>result:"+list);
        return bean;
    }

    /**
     * 公共根据id查询数据
     * @param entity
     */
    public <T> T queryEntity(T entity,String id) {
        if(entity.getClass().getName().indexOf("TbRidingGuideInfoEntity")<0){
            logger.info(">>>>>>>>>>>>queryEntity  entity:"+entity);
        }
        String whereSql=" id=:id ";
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("id",id);
        List<T> list = this.query(entity, whereSql, map);
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
     * 查询所有路书信息
     * @return
     */
    public PagingBean queryAllTravelGuideInfo(TbRidingGuideInfoDto dto){
        StringBuilder sql=new StringBuilder("select * from TB_RIDING_GUIDE_INFO ri ");
        StringBuilder countSql=new StringBuilder("select count(*) from TB_RIDING_GUIDE_INFO ri");
        StringBuilder whereSql=new StringBuilder(" where guide_type=1 ");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("pageSize",dto.getPageSize());
        map.put("start",dto.getStart());
        RegUtil regUtil=RegUtil.getSingleton();
        if(!regUtil.isNull(dto.getStrengthgrade())){//强度等级
            whereSql.append(" and strengthgrade=:strengthgrade");
            map.put("strengthgrade",dto.getStrengthgrade());
        }
        if(!regUtil.isNull(dto.getScenerygrad())){//风景等级
            whereSql.append(" and scenerygrad=:scenerygrad");
            map.put("scenerygrad",dto.getScenerygrad());
        }
        if(!regUtil.isNull(dto.getTitle())){//路书标题
            whereSql.append(" and title like '%' || :title || '%'");
            map.put("title",dto.getTitle());
        }
        if(!regUtil.isNull(dto.getStatus())){//状态(1.草稿 2.已发布)
            whereSql.append(" and status=:status");
            map.put("status",dto.getStatus());
        }
        if(!regUtil.isNull(dto.getId())){//状态(1.草稿 2.已发布)
            whereSql.append(" and id=:id");
            map.put("id",dto.getId());
        }
        //查询条件
        String querySql=sql.append(whereSql).toString(),queryCountSql=countSql.append(whereSql).toString();
        querySql=querySql+" order by create_time desc";
        logger.info(">>>>queryAllTravelGuideInfo>>>>>queryCount sql:"+queryCountSql+">>>>>>>paramMap:"+map.toString());
        logger.info(">>>>queryAllTravelGuideInfo>>>>>query sql:"+querySql+">>>>>>>");
        int count= this.jdbcTemplate.queryForObject(queryCountSql,map,Integer.class);
        List<TbRidingGuideInfoEntity> list=this.jdbcTemplate.query(pageSql(querySql,map), map, new BeanPropertyRowMapper<TbRidingGuideInfoEntity>(TbRidingGuideInfoEntity.class));
        PagingBean bean=new PagingBean();
        bean.setData(list);
        bean.setRecordsTotal(count);
        bean.setRecordsFiltered(count);
        logger.info(">>>>queryAllTravelGuideInfo>>>>>where"+whereSql+">>>>>>>result:"+bean.toString());
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
        logger.info(">>>>>>>bachUpdate ====list:"+list);
        if(list!=null&&list.size()>0){
            this.updateAll(list);
        }
    }

    /**
     * 删除路书所有日程信息
     */
    public void deleteRoute(TbGuideRouteInfoDto dto){
        logger.info(">>>>>>>>>>>deleteRoute=== param:"+dto);


        String sql="delete from (select 1 from TB_ROUTE_POINT_INFO a,TB_GUIDE_ROUTE_INFO b where a.route_id = b.id and b.guide_id=:guideId )";
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("guideId",dto.getGuideId());
        logger.info(">>>>>>>>>>>deletePoint=== sql:"+sql);
        this.jdbcTemplate.update(sql,map);

        sql="delete from TB_GUIDE_ROUTE_INFO where 1=1 and guide_id=:guideId";
        if(!RegUtil.getSingleton().isNull(dto.getId())){
            sql+=" and id=:id";
            map.put("id",dto.getId());
        }
        logger.info(">>>>>>>>>>>deleteRoute=== sql:"+sql);
        this.jdbcTemplate.update(sql,map);
    }

    /**
     * 批量插入
     * @return
     */
    public void bachInsert(List<Object> list){
        if(list!=null&&list.size()>0) {
            this.saveAll(list);
        }
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

    /**
     * 查询路书下所有线路及途经点数据 根据路书id
     * @return
     */
    public List<TbGuideRouteInfoDto> queryGuideRouteInfo(TbGuideRouteInfoDto dto,int type){
        String clum="ri.id, ri.startaddr, ri.endaddr, ri.day, ri.all_routepoint, ri.aboutdis, ri.ridingtime, ri.create_time, ri.create_user, ri.guide_id, ri.startlng, ri.startlat, ri.endlng, ri.endlat, ri.simp_introduction "+(type==1?",ri.introduction ":" ");
        StringBuilder sql=new StringBuilder("select "+clum+",pi.id pId,pi.address,pi.route_name,pi.ridingtime pRidingtime,pi.aboutdis pAboutdis,pi.route_icon,pi.remake,pi.lng,pi.lat,pi.type,pi.img_url,pi.base_url,pi.img_url1,pi.img_url2,pi.img_url3,pi.line_introduction " +
                " from TB_GUIDE_ROUTE_INFO ri left join TB_ROUTE_POINT_INFO pi on ri.id=pi.route_id ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and ri.guide_id=:guideId");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("guideId",dto.getGuideId());
        //查询条件
        String querySql=sql.append(whereSql).toString()+" order by ri.day";
        logger.info(">>>>queryGuideRouteInfo>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<TbGuideRouteInfoDto> list=this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<TbGuideRouteInfoDto>(TbGuideRouteInfoDto.class));
        logger.info(">>>>queryGuideRouteInfo>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list;
    }

    /**
     * 查询用户所在骑行队伍信息
     * @return
     */
    public List<TbCreateTeamInfoEntity> queryRidingTeamInfo(TeamPersonnelInfoDto paramDto){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder sql=new StringBuilder("select ri.* from TB_CREATE_TEAM_INFO ri left join TB_TEAM_PERSONNEL_INFO pi on ri.id=pi.team_id ");
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
        logger.info(">>>>queryRidingTeamInfo>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<TbCreateTeamInfoEntity> list=this.jdbcTemplate.query(querySql, map,new BeanPropertyRowMapper<TbCreateTeamInfoEntity>(TbCreateTeamInfoEntity.class));
        logger.info(">>>>queryRidingTeamInfo>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list;
    }

    /**
     * 查询我的路书
     * @return
     */
    public PagingBean queryMyRoadBook(TbRidingGuideInfoDto dto){
        StringBuilder sql=new StringBuilder("select gi.* from TB_RIDING_GUIDE_INFO gi inner join TB_USER_DOWN_GUIDE ud on gi.id=ud.guide_id ");
        StringBuilder countSql=new StringBuilder("select count(*) from TB_USER_DOWN_GUIDE ud ");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("pageSize",dto.getPageSize());
        map.put("userId",dto.getUserId());
        map.put("start",dto.getStart());
        String orderByStr=" order by ud.create_time desc ";
        StringBuilder whereSql=new StringBuilder(" where ud.member_id=:userId");
        //查询条件
        String querySql=sql.append(whereSql).append(orderByStr).toString(),queryCountSql=countSql.append(whereSql).toString();
        int count= this.jdbcTemplate.queryForObject(queryCountSql,map,Integer.class);
        logger.info(">>>>queryMyRoadBook>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<TbRidingGuideInfoEntity> list=this.jdbcTemplate.query(pageSql(querySql,map), map, new BeanPropertyRowMapper<TbRidingGuideInfoEntity>(TbRidingGuideInfoEntity.class));
        PagingBean bean=new PagingBean();
        bean.setData(list);
        bean.setRecordsTotal(count);
        bean.setRecordsFiltered(count);
        logger.info(">>>>queryMyRoadBook>>>>>where"+whereSql+">>>>>>>result:"+list);
        return bean;
    }

}
