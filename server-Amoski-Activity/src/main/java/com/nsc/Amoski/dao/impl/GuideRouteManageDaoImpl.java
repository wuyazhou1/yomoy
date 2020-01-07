package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.GuideRouteManageDao;
import com.nsc.Amoski.dto.ActivityBasicDto;
import com.nsc.Amoski.dto.ActivityBasicDto1;
import com.nsc.Amoski.dto.ActivityScheduleDto;
import com.nsc.Amoski.dto.ActivitySignUpDto;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.RegUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GuideRouteManageDaoImpl extends GenericRepositoryImpl implements GuideRouteManageDao {

    /**
     * 查询所有活动列表
     *
     * @param param 查询条件 查询所有活动列表
     * @return 查询所有活动列表
     */
    public PagingBean queryActivityList(ActivityBasicDto param) {
        Map<String, Object> map = new HashMap<String, Object>();
        RegUtil regUtil = RegUtil.getSingleton();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.* from (select bi.file_name_url,b.id,b.code,b.org_code,b.current_traffic,b.daily_visits,b.total_visits,b.type,b.title,b.nature,b.state,/*b.describe,b.notice,*/b.create_name,b.create_data,b.update_name,b.update_date " +
                ",s.type playType,s.destination,s.path_point,s.activity_intensity,s.equipment_requirements/*,s.details_activities*/ " +
                ",o.mandatory_field,o.scope_registration,o.collection_place,o.collection_time,to_char(o.activity_start,'yyyy-MM-dd hh:mm:ss') activity_start,to_char(o.activity_stop,'yyyy-MM-dd hh:mm:ss') activity_stop,o.activity_end,o.number_limitation,o.show_number " +
                ",case when o.activity_start>sysdate then 1 else 2 end orderDes," +
                "(select count(1) from TB_ACTIVITY_SIGN_UP q where q.BASICS_ID=b.code) SIGN_count" +
                " from TB_ACTIVITY_BASICS b left join tb_activity_synopsis s on b.id=s.basics_id left join tb_activity_ordination o on b.id=o.basics_id " +
                " left join (select basics_id,max(FILE_NAME_URL) file_name_url from TB_ACTIVITY_BILL_IMAGE where type='1' group by basics_id) bi on bi.basics_id=b.id  " +
                "where b.state=2) t ");
        if ("2".equals(param.getType())) {
            sql.append("order by t.update_date desc");
        } else {
            sql.append("order by t.Total_Visits desc");
        }
        StringBuilder countSql = new StringBuilder();
        countSql.append("select count(*) from TB_ACTIVITY_BASICS b where b.state=2 ");
        map.put("pageSize", param.getPageSize());
        map.put("start", param.getStart());
        //查询条件
        String querySql = sql.toString(), queryCountSql = countSql.toString();
        logger.info(">>>>queryActivityList>>>>>queryCount sql:" + queryCountSql + ">>>>>>>paramMap:" + map.toString());
        logger.info(">>>>queryActivityList>>>>>query sql:" + querySql + ">>>>>>>");
        int count = this.jdbcTemplate.queryForObject(queryCountSql, map, Integer.class);
        List<ActivityBasicDto1> list = this.jdbcTemplate.query(pageSql(querySql, map), map, new BeanPropertyRowMapper<ActivityBasicDto1>(ActivityBasicDto1.class));
        PagingBean bean = new PagingBean();
        bean.setData(list);
        bean.setRecordsTotal(count);
        bean.setRecordsFiltered(count);
        logger.info(">>>>queryActivityList end>>>>>>>>>>>>result:" + bean.toString());
        return bean;
    }

    /**
     * 小程序查询所有活动列表
     *
     * @param param 查询条件 小程序查询所有活动列表
     * @return 小程序查询所有活动列表
     */
    public PagingBean smallQueryActivityList(ActivityBasicDto param) {
        Map<String, Object> map = new HashMap<String, Object>();
        RegUtil regUtil = RegUtil.getSingleton();
        StringBuilder sql = new StringBuilder();
        map.put("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        if (StringUtils.isEmpty(param.getX()) && StringUtils.isEmpty(param.getY())) {
            sql.append("select t.* from (select bi.file_name_url,b.id,b.code,b.org_code,b.current_traffic,b.daily_visits,b.total_visits,b.type,b.title,b.nature,b.state,/*b.describe,b.notice,*/b.create_name,b.create_data,b.update_name,b.update_date " +
                    ",s.type playType,s.destination,s.path_point,s.activity_intensity,s.equipment_requirements/*,s.details_activities*/ " +
                    ",o.mandatory_field,o.scope_registration,o.collection_place,o.collection_time,to_char(o.activity_start,'yyyy-MM-dd hh:mm:ss') activity_start,to_char(o.activity_stop,'yyyy-MM-dd hh:mm:ss') activity_stop,o.activity_end,o.number_limitation,o.show_number " +
                    ",case when o.activity_start>sysdate then 1 else 2 end orderDes," +
                    "(select count(1) from TB_ACTIVITY_SIGN_UP q where q.BASICS_ID=b.code) SIGN_count" +
                    " from TB_ACTIVITY_BASICS b left join tb_activity_synopsis s on b.id=s.basics_id left join tb_activity_ordination o on b.id=o.basics_id " +
                    " left join (select basics_id,max(FILE_NAME_URL) file_name_url from TB_ACTIVITY_BILL_IMAGE where type='1' group by basics_id) bi on bi.basics_id=b.id  " +
                    "where b.state=2 ");
        } else {
            sql.append("select t.* from (select " +
                    "SQRT(+(((:yaxis - c.Y_AXIS) * ACOS(-1) * 12656 * COS(((:yaxis + c.Y_AXIS) / 2) * ACOS(-1) / 180) / 180) * ((:yaxis - c.Y_AXIS) * ACOS(-1) * 12656 * COS(((:yaxis + c.Y_AXIS ) / 2) * ACOS(-1) / 180) / 180)) + (((:xaxis - c.X_AXIS) * ACOS(-1) * 12656 / 180) * ((:xaxis - c.X_AXIS) * ACOS(-1) * 12656 / 180))) sqrtValue, c.X_AXIS, c.Y_AXIS, c.PATH_POINT_NAME," +
                    " bi.file_name_url,b.id,b.code,b.org_code,b.current_traffic,b.daily_visits,b.total_visits,b.type,b.title,b.nature,b.state,/*b.describe,b.notice,*/b.create_name,b.create_data,b.update_name,b.update_date " +
                    ",s.type playType,s.destination,s.path_point,s.activity_intensity,s.equipment_requirements/*,s.details_activities*/ " +
                    ",o.mandatory_field,o.scope_registration,o.collection_place,o.collection_time,to_char(o.activity_start,'yyyy-MM-dd hh:mm:ss') activity_start,to_char(o.activity_stop,'yyyy-MM-dd hh:mm:ss') activity_stop,o.activity_end,o.number_limitation,o.show_number " +
                    ",case when o.activity_start>sysdate then 1 else 2 end orderDes," +
                    "(select count(1) from TB_ACTIVITY_SIGN_UP q where q.BASICS_ID=b.code) SIGN_count" +
                    " from TB_ACTIVITY_BASICS b left join tb_activity_synopsis s on b.id=s.basics_id left join tb_activity_ordination o on b.id=o.basics_id " +
                    " left join (select basics_id,max(FILE_NAME_URL) file_name_url from TB_ACTIVITY_BILL_IMAGE where type='1' group by basics_id) bi on bi.basics_id=b.id  " +
                    " left join tb_activity_schedule tas ON b.ID=tas.BASICS_ID AND tas.DAYS_STATISTICS = 0" +
                    " left join tb_activity_route c ON tas.ID=c.SCHEDULE_ID AND c.order_id = 0" +
                    " where b.state=2 "
            );
            map.put("yaxis", param.getY());
            map.put("xaxis", param.getX());
        }
        if (StringUtils.isEmpty(param.getDps())) {
            sql.append(") t ");
        } else {
            sql.append(" and b.org_code in (:dps)) t ");
        }
        if (param.getSqrtValue() != null && param.getSqrtValue() > 0) {
            sql.append("where t.sqrtValue < :sqrtValue ");
            map.put("sqrtValue", param.getSqrtValue());
        }
        if ("2".equals(param.getType())) {
            sql.append("order by t.update_date desc");
        } else {
            sql.append("order by t.Total_Visits desc");
        }
        StringBuilder countSql = new StringBuilder();
        if (StringUtils.isEmpty(param.getX()) && StringUtils.isEmpty(param.getY())) {
            countSql.append("SELECT count(*) from TB_ACTIVITY_BASICS b WHERE b.STATE = 2 ");
        } else {
            if (param.getSqrtValue() != null && param.getSqrtValue() > 0) {
                countSql.append("SELECT COUNT(*) FROM (SELECT SQRT(+(((:yaxis - c.Y_AXIS) * ACOS(-1) * 12656 * COS(((:yaxis + c.Y_AXIS) / 2) * ACOS(-1) / 180) / 180) * ((:yaxis - c.Y_AXIS) * ACOS(-1) * 12656 * COS(((:yaxis + c.Y_AXIS ) / 2) * ACOS(-1) / 180) / 180)) + (((:xaxis - c.X_AXIS) * ACOS(-1) * 12656 / 180) * ((:xaxis - c.X_AXIS) * ACOS(-1) * 12656 / 180))) sqrtValue FROM TB_ACTIVITY_BASICS b " +
                        "LEFT JOIN TB_ACTIVITY_SCHEDULE tas ON b.ID = tas.BASICS_ID AND tas.DAYS_STATISTICS = 0 " +
                        "LEFT JOIN TB_ACTIVITY_ROUTE C ON tas.ID = C.SCHEDULE_ID AND C.order_id = 0 " +
                        "WHERE b.state = 2) t where t.sqrtValue < :sqrtValue ");
            } else {
                countSql.append("select count(*) from TB_ACTIVITY_BASICS b " +
                        "LEFT JOIN TB_ACTIVITY_SCHEDULE tas ON b.ID = tas.BASICS_ID AND tas.DAYS_STATISTICS = 0 " +
                        "LEFT JOIN TB_ACTIVITY_ROUTE C ON tas.ID = C.SCHEDULE_ID AND C.order_id = 0 " +
                        "WHERE b.STATE=2 "
                );
            }
        }
        if (!StringUtils.isEmpty(param.getDps())) {
            countSql.append(" and b.ORG_CODE in(:dps) ");
            map.put("dps", param.getDps());
        }
        map.put("pageSize", param.getPageSize());
        map.put("start", param.getStart());
        //查询条件
        String querySql = sql.toString(), queryCountSql = countSql.toString();
        logger.info(">>>>queryActivityList>>>>>queryCount sql:" + queryCountSql + ">>>>>>>paramMap:" + map.toString());
        logger.info(">>>>queryActivityList>>>>>query sql:" + querySql + ">>>>>>>");
        int count = this.jdbcTemplate.queryForObject(queryCountSql, map, Integer.class);
        List<ActivityBasicDto1> list = this.jdbcTemplate.query(pageSql(querySql, map), map, new BeanPropertyRowMapper<ActivityBasicDto1>(ActivityBasicDto1.class));
        PagingBean bean = new PagingBean();
        bean.setData(list);
        bean.setRecordsTotal(count);
        bean.setRecordsFiltered(count);
        logger.info(">>>>queryActivityList end>>>>>>>>>>>>result:" + bean.toString());
        return bean;
    }

    /**
     * 小程序查询当前正在进行的活动列表信息
     *
     * @param param
     * @return
     */
    @Override
    public PagingBean smallQueryCurrentActivityList(ActivityBasicDto param) {
        Map<String, Object> map = new HashMap<String, Object>();
        RegUtil regUtil = RegUtil.getSingleton();
        StringBuilder sql = new StringBuilder();
        map.put("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        if (StringUtils.isEmpty(param.getX()) && StringUtils.isEmpty(param.getY())) {
            sql.append("select t.* from (select bi.file_name_url,b.id,b.code,b.org_code,b.current_traffic,b.daily_visits,b.total_visits,b.type,b.title,b.nature,b.state,/*b.describe,b.notice,*/b.create_name,b.create_data,b.update_name,b.update_date " +
                    ",s.type playType,s.destination,s.path_point,s.activity_intensity,s.equipment_requirements/*,s.details_activities*/ " +
                    ",o.mandatory_field,o.scope_registration,o.collection_place,o.collection_time,to_char(o.activity_start,'yyyy-MM-dd hh:mm:ss') activity_start,to_char(o.activity_stop,'yyyy-MM-dd hh:mm:ss') activity_stop,o.activity_end,o.number_limitation,o.show_number " +
                    ",case when o.activity_start>sysdate then 1 else 2 end orderDes," +
                    "(select count(1) from TB_ACTIVITY_SIGN_UP q where q.BASICS_ID=b.code) SIGN_count" +
                    " from TB_ACTIVITY_BASICS b left join tb_activity_synopsis s on b.id=s.basics_id left join tb_activity_ordination o on b.id=o.basics_id " +
                    " left join (select basics_id,max(FILE_NAME_URL) file_name_url from TB_ACTIVITY_BILL_IMAGE where type='1' group by basics_id) bi on bi.basics_id=b.id  " +
                    "where b.state=2 AND o.ACTIVITY_START >= TO_DATE(:time, 'yyyy-MM-dd')");
        } else {
            sql.append("select t.* from (select " +
                    "SQRT(+(((:yaxis - c.Y_AXIS) * ACOS(-1) * 12656 * COS(((:yaxis + c.Y_AXIS) / 2) * ACOS(-1) / 180) / 180) * ((:yaxis - c.Y_AXIS) * ACOS(-1) * 12656 * COS(((:yaxis + c.Y_AXIS ) / 2) * ACOS(-1) / 180) / 180)) + (((:xaxis - c.X_AXIS) * ACOS(-1) * 12656 / 180) * ((:xaxis - c.X_AXIS) * ACOS(-1) * 12656 / 180))) sqrtValue, c.X_AXIS, c.Y_AXIS, c.PATH_POINT_NAME," +
                    " bi.file_name_url,b.id,b.code,b.org_code,b.current_traffic,b.daily_visits,b.total_visits,b.type,b.title,b.nature,b.state,/*b.describe,b.notice,*/b.create_name,b.create_data,b.update_name,b.update_date " +
                    ",s.type playType,s.destination,s.path_point,s.activity_intensity,s.equipment_requirements/*,s.details_activities*/ " +
                    ",o.mandatory_field,o.scope_registration,o.collection_place,o.collection_time,to_char(o.activity_start,'yyyy-MM-dd hh:mm:ss') activity_start,to_char(o.activity_stop,'yyyy-MM-dd hh:mm:ss') activity_stop,o.activity_end,o.number_limitation,o.show_number " +
                    ",case when o.activity_start>sysdate then 1 else 2 end orderDes," +
                    "(select count(1) from TB_ACTIVITY_SIGN_UP q where q.BASICS_ID=b.code) SIGN_count" +
                    " from TB_ACTIVITY_BASICS b left join tb_activity_synopsis s on b.id=s.basics_id left join tb_activity_ordination o on b.id=o.basics_id " +
                    " left join (select basics_id,max(FILE_NAME_URL) file_name_url from TB_ACTIVITY_BILL_IMAGE where type='1' group by basics_id) bi on bi.basics_id=b.id  " +
                    " left join tb_activity_schedule tas ON b.ID=tas.BASICS_ID AND tas.DAYS_STATISTICS = 0" +
                    " left join tb_activity_route c ON tas.ID=c.SCHEDULE_ID AND c.order_id = 0" +
                    " where b.state=2 AND o.ACTIVITY_START >= TO_DATE(:time, 'yyyy-MM-dd')"
            );
            map.put("yaxis", param.getY());
            map.put("xaxis", param.getX());
        }
        if (StringUtils.isEmpty(param.getDps())) {
            sql.append(") t ");
        } else {
            sql.append(" and b.org_code in (:dps)) t ");
        }
        if (param.getSqrtValue() != null && param.getSqrtValue() > 0) {
            sql.append("where t.sqrtValue < :sqrtValue ");
            map.put("sqrtValue", param.getSqrtValue());
        }
        if ("2".equals(param.getType())) {
            sql.append("order by t.update_date desc");
        } else {
            sql.append("order by t.Total_Visits desc");
        }
        StringBuilder countSql = new StringBuilder();
        if (StringUtils.isEmpty(param.getX()) && StringUtils.isEmpty(param.getY())) {
            countSql.append("SELECT count(*) from TB_ACTIVITY_BASICS b " +
                    "LEFT JOIN TB_ACTIVITY_ORDINATION o ON b.ID = o.BASICS_ID " +
                    "WHERE b.STATE = 2 AND o.ACTIVITY_START >= TO_DATE(:time, 'yyyy-MM-dd') ");
        } else {
            if (param.getSqrtValue() != null && param.getSqrtValue() > 0) {
                countSql.append("SELECT COUNT(*) FROM (SELECT SQRT(+(((:yaxis - c.Y_AXIS) * ACOS(-1) * 12656 * COS(((:yaxis + c.Y_AXIS) / 2) * ACOS(-1) / 180) / 180) * ((:yaxis - c.Y_AXIS) * ACOS(-1) * 12656 * COS(((:yaxis + c.Y_AXIS ) / 2) * ACOS(-1) / 180) / 180)) + (((:xaxis - c.X_AXIS) * ACOS(-1) * 12656 / 180) * ((:xaxis - c.X_AXIS) * ACOS(-1) * 12656 / 180))) sqrtValue FROM TB_ACTIVITY_BASICS b " +
                        "LEFT JOIN TB_ACTIVITY_SCHEDULE tas ON b.ID = tas.BASICS_ID AND tas.DAYS_STATISTICS = 0 " +
                        "LEFT JOIN TB_ACTIVITY_ROUTE C ON tas.ID = C.SCHEDULE_ID AND C.order_id = 0 " +
                        "LEFT JOIN TB_ACTIVITY_ORDINATION o ON b.ID = o.BASICS_ID " +
                        "WHERE b.state = 2 AND o.ACTIVITY_START >= TO_DATE(:time, 'yyyy-MM-dd')) t where t.sqrtValue < :sqrtValue ");
            } else {
                countSql.append("select count(*) from TB_ACTIVITY_BASICS b " +
                        "LEFT JOIN TB_ACTIVITY_SCHEDULE tas ON b.ID = tas.BASICS_ID AND tas.DAYS_STATISTICS = 0 " +
                        "LEFT JOIN TB_ACTIVITY_ROUTE C ON tas.ID = C.SCHEDULE_ID AND C.order_id = 0 " +
                        "LEFT JOIN TB_ACTIVITY_ORDINATION o ON b.ID = o.BASICS_ID " +
                        "WHERE b.STATE=2 AND o.ACTIVITY_START >= TO_DATE(:time, 'yyyy-MM-dd') "
                );
            }
        }
        if (!StringUtils.isEmpty(param.getDps())) {
            countSql.append(" and b.ORG_CODE in(:dps) ");
            map.put("dps", param.getDps());
        }
        map.put("pageSize", param.getPageSize());
        map.put("start", param.getStart());
        //查询条件
        String querySql = sql.toString(), queryCountSql = countSql.toString();
        logger.info(">>>>queryActivityList>>>>>queryCount sql:" + queryCountSql + ">>>>>>>paramMap:" + map.toString());
        logger.info(">>>>queryActivityList>>>>>query sql:" + querySql + ">>>>>>>");
        int count = this.jdbcTemplate.queryForObject(queryCountSql, map, Integer.class);
        List<ActivityBasicDto1> list = this.jdbcTemplate.query(pageSql(querySql, map), map, new BeanPropertyRowMapper<ActivityBasicDto1>(ActivityBasicDto1.class));
        PagingBean bean = new PagingBean();
        bean.setData(list);
        bean.setRecordsTotal(count);
        bean.setRecordsFiltered(count);
        logger.info(">>>>queryActivityList end>>>>>>>>>>>>result:" + bean.toString());
        return bean;
    }

    /**
     * 查询单个活动详情信息
     *
     * @return 查询单个活动详情信息
     */
    @Override
    public ActivityBasicDto queryActivityDetailInfo(ActivityBasicDto info, boolean bl) {
        String querySql = "select bi.file_name_url,b.id,b.code,b.org_code,b.current_traffic,b.daily_visits,b.total_visits,b.type,b.title,b.nature,b.state,b.describe,b.notice,b.create_name,b.create_data,b.update_name,b.update_date " +
                ",s.type playType,s.destination,s.path_point,s.activity_intensity,s.ACTIVITY_NOTICE,s.equipment_requirements " + (bl ? ",s.details_activities" : "") +
                ",o.mandatory_field,o.scope_registration,o.collection_place,o.collection_time,o.activity_start,o.activity_stop,to_char(o.activity_end,'yyyy-MM-dd hh24:mi:ss') activity_end,o.number_limitation,o.show_number " +
                ",case when o.activity_start>sysdate then 1 else 2 end orderDes from TB_ACTIVITY_BASICS b left join tb_activity_synopsis s on b.id=s.basics_id left join tb_activity_ordination o on b.id=o.basics_id " +
                " left join (select basics_id,max(FILE_NAME_URL) file_name_url from TB_ACTIVITY_BILL_IMAGE where type='1' group by basics_id) bi on bi.basics_id=b.id  " +
                "where b.state=2 and b.code=:id";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", info.getId());
        logger.info(">>>>queryActivityDetailInfo>>>>>query sql:" + querySql + ">>>>>>>paramMap:" + map.toString());
        List<ActivityBasicDto> list = this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<ActivityBasicDto>(ActivityBasicDto.class));
        logger.info(">>>>end  queryActivityDetailInfo>>>>>>>>>>>>result:"/*+list*/);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 查询活动路书详细信息
     *
     * @return 查询活动路书详细信息
     */
    @Override
    public List<ActivityScheduleDto> queryH5ActivityGuideInfo(ActivityScheduleDto info) {
        String querySql = "select h.*,hr.eatAdvice,r.*,r.id rid,r1.roleName,r1.allDistance,s.* from tb_activity_schedule s left join tb_activity_hotel h " +
                "on s.basics_id=h.basics_id and s.id=h.schedule_id left join (select a.schedule_id,a.basics_id," +
                "listagg(decode(a.order_id,1,'早餐',2,'中餐',3,'晚餐'), ' ') WITHIN GROUP(ORDER BY a.id) eatAdvice " +
                "from tb_hotel_restaurant a where  a.basics_id=:basicsId group by a.schedule_id,a.basics_id ) hr " +
                "on s.basics_id=hr.basics_id and s.id=hr.schedule_id left join TB_ACTIVITY_ROUTE  r on s.basics_id=r.basics_id and s.id=r.schedule_id " +
                "left join (select a.basics_id,a.schedule_id,sum(replace(nvl(a.distance,'0'),'km',''))||'km' allDistance,listagg(a.path_point_name, '-') WITHIN GROUP(ORDER BY a.id) roleName " +
                "from TB_ACTIVITY_ROUTE a group by schedule_id,a.basics_id) r1 on s.basics_id=r1.basics_id and s.id=r1.schedule_id where s.basics_id=:basicsId  ";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("basicsId", info.getBasicsId());
        querySql = querySql + " order by h.id asc";
        logger.info(">>>>queryH5ActivityGuideInfo>>>>>query sql:" + querySql + ">>>>>>>paramMap:" + map.toString());
        List<ActivityScheduleDto> list = this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<ActivityScheduleDto>(ActivityScheduleDto.class));
        logger.info(">>>>queryH5ActivityGuideInfo>>>>>>>>>>>>result:"/*+list*/);
        return list;
    }

    /**
     * 查询报名人员和接送信息
     *
     * @return 查询报名人员和接送信息
     */
    @Override
    public List<ActivitySignUpDto> queryActivitySignUpPeople(ActivitySignUpDto info) {
        StringBuilder sql = new StringBuilder("select s.*,r.*,r.id rid from TB_ACTIVITY_SIGN_UP s " +
                "left join TB_PEOPLE_RECEIVE_SEND r on s.id=r.PEOPLE_ID ");
        StringBuilder whereSql = new StringBuilder(" where 1=1 and s.basics_id=:basicsId and s.tel=:tel and r.type=1");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("basicsId", info.getBasicsId());
        map.put("tel", info.getTel());
        //查询条件
        String querySql = sql.append(whereSql).toString();
        logger.info(">>>>queryActivitySignUpPeople>>>>>query sql:" + querySql + ">>>>>>>paramMap:" + map.toString());
        List<ActivitySignUpDto> list = this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<ActivitySignUpDto>(ActivitySignUpDto.class));
        logger.info(">>>>queryActivitySignUpPeople>>>>>where" + whereSql + ">>>>>>>result:" + list);
        return list;
    }

    @Override
    public String queryActityImages(String code) {
        Map<String, String> parentMap = new HashMap<>();
        parentMap.put("code", code);
        return this.jdbcTemplate.queryForObject("SELECT b.FILE_NAME_URL " +
                " FROM " +
                " TB_ACTIVITY_BASICS a " +
                " left join TB_ACTIVITY_BILL_IMAGE b on a.id = b.basics_id " +
                " where a.code = :code and b.type = 1", parentMap, String.class);
    }

}
