package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.UserCenterManageDao;
import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.BeanCopyUtil;
import com.nsc.Amoski.util.GsonUtil;
import com.nsc.Amoski.util.RegUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserCenterManageDaoImpl extends GenericRepositoryImpl implements UserCenterManageDao {

    /**
     * 查询用户消息
     * @param param 查询条件 查询所有消息
     * @return  用户所有消息
     */
    public PagingBean queryUserMessage(UsreMessageDto param){
             RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder sql=new StringBuilder("select * from TB_USRE_MESSAGE m");
        StringBuilder countSql=new StringBuilder("select count(*) from TB_USRE_MESSAGE m");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and m.member_id=:userId");

        Map<String,Object> map=new HashMap<String, Object>();
        map.put("pageSize",param.getPageSize());
        map.put("start",param.getStart());
        map.put("userId",param.getMemberId());
        //查询条件
        String querySql=sql.append(whereSql).toString(),queryCountSql=countSql.append(whereSql).toString();
        querySql=querySql+" order by m.create_time desc";
        logger.info(">>>>queryUserMessage>>>>>queryCount sql:"+queryCountSql+">>>>>>>paramMap:"+map.toString());
        logger.info(">>>>queryUserMessage>>>>>query sql:"+querySql+">>>>>>>");
        int count= this.jdbcTemplate.queryForObject(queryCountSql,map,Integer.class);
        List<UsreMessageDto> list=this.jdbcTemplate.query(pageSql(querySql,map), map, new BeanPropertyRowMapper<UsreMessageDto>(UsreMessageDto.class));
        PagingBean bean=new PagingBean();
        bean.setData(list);
        bean.setRecordsTotal(count);
        bean.setRecordsFiltered(count);
        logger.info(">>>>queryUserMessage>>>>>where"+whereSql+">>>>>>>result:"+bean.toString());
        return bean;
    }

    /**
     * 新增用户消息
     * @return
     */
    public void addUserMessage(TbUsreMessageEntity param){
        logger.info(">>>>>>>>>>>>>>>>>> addUserMessage!!!  save param:"+param);
        this.save(param);
    }

    /**
     * 查询用户相册图片
     * @param param 查询条件 查询所有消息
     * @return  用户所有图片
     */
    public PagingBean queryUserPhotoImg(PhotoPicDto param){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder sql=new StringBuilder("select * from TB_PHOTO_PIC p");
        StringBuilder countSql=new StringBuilder("select count(*) from TB_PHOTO_PIC p");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and status=1 and p.member_id=:userId");

        Map<String,Object> map=new HashMap<String, Object>();
        map.put("userId",param.getMemberId());
        map.put("pageSize",param.getPageSize());
        map.put("start",param.getStart());
        //查询条件
        String querySql=sql.append(whereSql).toString(),queryCountSql=countSql.append(whereSql).toString();
        querySql=querySql+" order by p.create_time desc";
        logger.info(">>>>queryUserPhotoImg>>>>>queryCount sql:"+queryCountSql+">>>>>>>paramMap:"+map.toString());
        logger.info(">>>>queryUserPhotoImg>>>>>query sql:"+querySql+">>>>>>>");
        int count= this.jdbcTemplate.queryForObject(queryCountSql,map,Integer.class);
        List<TbPhotoPic> list=this.jdbcTemplate.query(pageSql(querySql,map), map, new BeanPropertyRowMapper<TbPhotoPic>(TbPhotoPic.class));
        PagingBean bean=new PagingBean();
        bean.setData(list);
        bean.setRecordsTotal(count);
        bean.setRecordsFiltered(count);
        logger.info(">>>>queryUserPhotoImg>>>>>where"+whereSql+">>>>>>>result:"+bean.toString());
        return bean;
    }
    /**
     * 删除图片
     * @param param
     */
    public void deleteUserPic(PhotoPicDto param){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("userId",param.getMemberId());
        map.put("ids",param.getIds());
                this.jdbcTemplate.update("update TB_PHOTO_PIC set status=0 where member_id=:userId " +
                "and exists (select * from (select regexp_substr(:ids,'[^,]+',1,rownum) did from dual" +
                " connect by rownum<=length(regexp_replace(:ids,'[^,]+'))+1) b where id=b.did)",map);
    }

    /**
     * 查询所有绑定车辆信息
     * @return
     */
    public List<VehicleInfoDto> queryAllBindVehicleInfo(VehicleInfoDto param){
        StringBuilder sql=new StringBuilder("select i.*,t.series_name brandTypeName,b.brand_name brandName from TB_VEHICLE_INFO i " +
                "left join TB_BRAND_SERIES t on i.car_brand_id=t.id left join TB_VEHICLE_BRAND b on t.brand_id=b.id ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and i.status=1 and i.member_id=:userId");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("userId",param.getMemberId());
        //查询条件
        String querySql=sql.append(whereSql).toString();
        querySql=querySql+" order by i.is_default desc,i.create_time desc";
        logger.info(">>>>queryAllBindVehicleInfo>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<VehicleInfoDto> list=this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<VehicleInfoDto>(VehicleInfoDto.class));
        logger.info(">>>>queryAllBindVehicleInfo>>>>>where"+whereSql+">>>>>>>result:"+list);

        return list;
    }

    /**
     * 查询某辆绑定车辆信息
     * @return
     */
    public TbVehicleInfoEntity querySingleBindVehicleInfo(String id,String userId){
        StringBuilder sql=new StringBuilder("select * from TB_VEHICLE_INFO i ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and i.member_id=:userId and i.id=:id");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("userId",userId);
        map.put("id",id);
        //查询条件
        String querySql=sql.append(whereSql).toString();
        querySql=querySql+" order by i.create_time desc";
        logger.info(">>>>querySingleBindVehicleInfo>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<TbVehicleInfoEntity> list=this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<TbVehicleInfoEntity>(TbVehicleInfoEntity.class));
        logger.info(">>>>querySingleBindVehicleInfo>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list.size()>0?list.get(0):null;
    }

    /**
     * 查询用户某张图片
     * @return
     */
    public TbPhotoPic querySingleUserImg(Integer uid,Integer imgId){
        StringBuilder sql=new StringBuilder("select * from TB_PHOTO_PIC p ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and p.member_id=:userId and i.id=:imgId");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("userId",uid);
        map.put("imgId",imgId);
        //查询条件
        String querySql=sql.append(whereSql).toString();
        logger.info(">>>>querySingleUserImg>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<TbPhotoPic> list=this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<TbPhotoPic>(TbPhotoPic.class));
        logger.info(">>>>querySingleUserImg>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list.size()>0?list.get(0):null;
    }

    /**
     * 查询一张或多张图片根据图片ids
     * @return
     */
    public List<TbPhotoPic> queryImgByImgId(String imgIds){
        StringBuilder sql=new StringBuilder("select * from TB_PHOTO_PIC p ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and exists (select * from (select regexp_substr(:imgIds,'[^,]+',1,rownum) did from dual" +
                " connect by rownum<=length(regexp_replace(:imgIds,'[^,]+'))) b where id=b.did)");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("imgIds",imgIds);
        //查询条件
        String querySql=sql.append(whereSql).toString();
        logger.info(">>>>queryImgByImgId>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<TbPhotoPic> list=this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<TbPhotoPic>(TbPhotoPic.class));
        logger.info(">>>>queryImgByImgId>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list;
    }

    /**
     * 批量插入天气code
     * @param list
     */
    public void addWeatherBean(List<WeatherDto> list) {
        String sql="insert into TB_WEATHER_AREA_CODE(code,province,city,area,all_addr,create_user,create_time)" +
                " values(?,?,?,?,?,?,?)";
        logger.info(">>>>>>>>>>>>insert into before list  length:"+list.size());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(list.toArray());
        int[] updateCounts = this.jdbcTemplate.batchUpdate(sql, batch);
        logger.info(">>>>>>>>>>>>insert into after data:"+updateCounts.length);
    }

    /**
     * 车辆绑定
     * @param param
     */
    public void bindSelfVehicle(TbVehicleInfoEntity param){
        logger.info(">>>>>>>>>>>>>>save bindSelfVehicle param:"+param);
        this.save(param);
    }
    /**
     * 用户车辆信息修改
     * @param param
     */
    public void updateSelfVehicle(TbVehicleInfoEntity param){
        logger.info(">>>>>>>>>>>>>>update updateSelfVehicle param:"+param);
        this.update(param);
    }
    /**
     * 用户默认车辆信息修改
     * @param userId 用户id
     */
    public void updateDefaultVehicle(String userId){
        logger.info(">>>>>>>>>>>>>>update updateDefaultVehicle userId:"+userId);
        Map<String,String> map=new HashMap();
        map.put("userId",userId);
        this.jdbcTemplate.update("update TB_VEHICLE_INFO set is_default=0 where member_id=:userId ",map);
    }

    /**
     * 查询车辆所有品牌
     * @return
     */
    public List<VehicleBrandDto> queryAllBindVehicleBrandInfo(){
        StringBuilder sql=new StringBuilder("select * from TB_VEHICLE_BRAND b");
        StringBuilder whereSql=new StringBuilder(" where 1=1 ");
        //查询条件
        String querySql=sql.append(whereSql).toString();
        querySql=querySql+" order by b.create_time desc";
        logger.info(">>>>queryAllBindVehicleInfo>>>>>query sql:"+querySql+">>>>>>>");
        List<VehicleBrandDto> list=this.jdbcTemplate.query(querySql, new BeanPropertyRowMapper<VehicleBrandDto>(VehicleBrandDto.class));
        logger.info(">>>>queryAllBindVehicleInfo>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list;
    }
    /**
     * 查询车辆所有品牌下的车型
     * @return
     */
    public List<VehicleBrandTypeDto> queryVehicleBrandByBrandId(VehicleBrandTypeDto param){
        StringBuilder sql=new StringBuilder("select b.*,b.series_name name from TB_BRAND_SERIES b ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 and b.brand_id=:brandId ");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("brandId",param.getBrandId());
        //查询条件
        String querySql=sql.append(whereSql).toString();
        querySql=querySql+" order by b.create_time desc";
        logger.info(">>>>queryVehicleBrandByBrandId>>>>>query sql:"+querySql+">>>>>>>");
        List<VehicleBrandTypeDto> list=this.jdbcTemplate.query(querySql,map, new BeanPropertyRowMapper<VehicleBrandTypeDto>(VehicleBrandTypeDto.class));
        logger.info(">>>>queryVehicleBrandByBrandId>>>>>where"+whereSql+">>>>>>>result:"+list);
        return list;
    }
    /**
     * 查询车辆所有车型
     * @return
     */
    public List<VehicleBrandTypeDto> queryVehicleBrandType(){
        StringBuilder sql=new StringBuilder("select b.*,b.series_name name from TB_BRAND_SERIES b");
        //查询条件
        String querySql=sql.toString();
        querySql=querySql+" order by b.create_time desc";
        logger.info(">>>>queryVehicleBrandType>>>>>query sql:"+querySql+">>>>>>>");
        List<VehicleBrandTypeDto> list=this.jdbcTemplate.query(querySql, new BeanPropertyRowMapper<VehicleBrandTypeDto>(VehicleBrandTypeDto.class));
        return list;
    }

    /**
     * 删除车辆信息
     * @param dto 用户id  车辆信息id
     */
    public void deleteUserVehicleInfo(VehicleInfoDto dto){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("userId",dto.getMemberId());
        map.put("id",dto.getId());
        logger.info(">>>>>>>>>>>>>deleteUserVehicleInfo>>> param:"+map);
        this.jdbcTemplate.update("update TB_VEHICLE_INFO set status=0 where member_id=:userId and id=:id ",map);
    }

    /**
     * 新增相册图片
     */
    public void addPhotoPic(List<TbPhotoPic> list){

        logger.info(">>>>>>>>>>>>>addPhotoPic>>> pic:"+list.size()+">>>>>>>>>list"+ GsonUtil.dtoToJson(list));
        this.saveAll(list);
        /*String sql="insert into TB_PHOTO_PIC(IMG_URL,CREATE_TIME,CREATE_USER,SMALL_URL,STATUS,BASE_URL,PHOTO_ID,MEMBER_ID)" +
                " values(?,?,?,?,?,?,?,?)";
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(list.toArray());
        int[] updateCounts = this.jdbcTemplate.batchUpdate(sql, batch);*/
        logger.info(">>>>>>>>>>>>insert into after data:"+list.size());
    }

    /**
     * 新增用户意见反馈
     * @return
     */
    public void addUserFeedback(TbUserFeedbackEntity param){
        logger.info(">>>>>>>>>>>>>>>>>> addUserFeedback!!!  save param:"+param);
        this.save(param);
    }

    /**
     * 公共插入实体数据
     * @param entity
     */
    public <T> void addEntity(T entity) {
        logger.info(">>>>>>>>>>>>addEntity insert into before list  entity:"+entity);
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
     * 批量插入
     * @return
     */
    public void bachInsert(List<Object> list){
        logger.info(">>>>>>>>>bachInsert  list:"+list);
        if(list!=null&&list.size()>0){
            this.saveAll(list);
        }
    }

    /**
     * 批量更新
     * @return
     */
    public void bachUpdate(List<Object> list){
        logger.info(">>>>>>>>>bachUpdate  list:"+list);
        if(list!=null&&list.size()>0) {
            this.updateAll(list);
        }
    }

    /**
     * 公共查询数据
     * @param entity
     */
    public <T> List<T> queryListEntity(T entity,String whereSql,Map<String,Object> map) {
        logger.info(">>>>>>>>>>>>queryListEntity  entity:"+entity+"=====map:"+map);
        return this.query(entity,whereSql,map);
    }

    /**
     * 公共根据id查询数据
     * @param entity
     */
    public <T> T queryEntity(T entity,String id) {
        logger.info(">>>>>>>>>>>>queryEntity  entity:"+entity);
        String whereSql=" id=:id ";
        Map<String,Object> map=new HashMap<>();
        map.put("id",id);
        List<T> list = this.query(entity, whereSql, map);
        return list!=null&&list.size()>0?list.get(0):null;
    }

    /**
     * 查询品牌系列数据
     * @return
     */
    public List<TbBrandSeries> queryBrandSeries(BrandSeriesDto dto){
        StringBuilder sql=new StringBuilder("select * from TB_BRAND_SERIES b where 1=1 ");
        RegUtil regUtil=RegUtil.getSingleton();
        Map<String,String> map=new HashMap<String, String>();
        if(regUtil.isNull(dto.getBrandId())){
            sql.append(" and brand_id=:brandId");
            map.put("brandId",dto.getBrandId());
        }
        //查询条件
        String querySql=sql.toString();
        logger.info(">>>>queryBrandSeries>>>>>query sql:"+querySql+">>>>>>>");
        List<TbBrandSeries> list=this.jdbcTemplate.query(querySql,map, new BeanPropertyRowMapper<TbBrandSeries>(TbBrandSeries.class));
        return list;
    }

    @Override
    public void updateBasics(TbActivityBasics1Entity basicsEntity) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(basicsEntity);
        String updateSql = "update TB_ACTIVITY_BASICS set TOTAL_VISITS = TOTAL_VISITS+1  where id = :id ";
        System.out.println(updateSql+"\n parent:id="+basicsEntity.getId());
        jdbcTemplate.update(updateSql,Parameter);
    }

    @Override
    public List<TbActivitySignUpEntity> querySignUpEntity(TbActivitySignUpEntity tbActivitySignUpEntity, Map<String, Object> map) {
        String querySql = "select a.* from TB_ACTIVITY_SIGN_UP a " +
                "left join TB_ACTIVITY_ORDER_DETAILS b on b.SIGN_UP_ID=a.id " +
                "where a.basics_id=:basicsId and b.state=3";
        logger.info(">>>>queryMobileApply>>>>>query sql:"+querySql+">>>>>>>parent>>>>basicsId:"+map.get("basicsId"));
        return jdbcTemplate.query(querySql,
                map,
                new BeanPropertyRowMapper(TbActivitySignUpEntity.class));
    }

    /*@Override
    public void update(Object entity) {

    }*/

    /**
     * 查询手机号是否已报名
     * @return
     */
    public List<ActivityApplyDto> queryMobileApply(ActivityApplyDto parma){
        StringBuilder sql=new StringBuilder("select * from TB_ACTIVITY_APPLY b where mobile=:mobile");
        //查询条件
        String querySql=sql.toString();
        Map<String,String> map=new HashMap<String, String>();
        map.put("mobile",parma.getMobile());
        logger.info(">>>>queryMobileApply>>>>>query sql:"+querySql+">>>>>>>");
        List<ActivityApplyDto> list=this.jdbcTemplate.query(querySql,map, new BeanPropertyRowMapper<ActivityApplyDto>(ActivityApplyDto.class));
        return list;
    }
}
