package com.nsc.AmoskiActivity.Dao.impl;


import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.util.BeanCopyUtil;
import com.nsc.AmoskiActivity.Dao.RegistrationPersonnelDao;
import com.nsc.AmoskiActivity.Util.GenericRepositoryActivityImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RegistrationPersonnelDaoImpl extends GenericRepositoryActivityImpl implements RegistrationPersonnelDao {
    @Override
    public List RegistrationPersonnelList(Map<String, Object> params) {
        ShiroUser suser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        params.put("orgCode",suser.getOrgCode());
        StringBuffer str = new StringBuffer("");
        if(params.get("addres")!=null && !params.get("addres").toString().trim().equals("")){
            str.append(" and REGION like '%'|| :addres ||'%'");
        }
        if(params.get("nameTel")!=null && !params.get("nameTel").toString().trim().equals("")){
            str.append(" and (NAME = :nameTel  or  TEL = :nameTel )");
        }
        List list = this.jdbcTemplate.query(pageSql("select * from TB_ACTIVITY_SIGN_UP where BASICS_ID = :basicsId  " + str.toString() ,params),
                params , new BeanPropertyRowMapper(TbActivitySignUpEntity.class));
        return list;
    }

    public String selectColumn(){
        return "w.*," +
                "(select decode(state,'1','未提交','2','未付款','3','已付款','4','审核退款','5','以退款','未知状态') from TB_ACTIVITY_ORDER_DETAILS where SIGN_UP_id=w.id) state";
    }

    @Override
    public Integer RegistrationPersonnelCount(Map<String, Object> params) {
        ShiroUser suser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        params.put("orgCode",suser.getOrgCode());
        StringBuffer str = new StringBuffer("");
        if(params.get("addres")!=null && !params.get("addres").toString().trim().equals("")){
            str.append(" and REGION like '%'|| :addres ||'%'");
        }
        if(params.get("nameTel")!=null && !params.get("nameTel").toString().trim().equals("")){
            str.append(" and (NAME = :nameTel  or  TEL = :nameTel )");
        }
        Integer count = this.jdbcTemplate.queryForObject("select count(1) from TB_ACTIVITY_SIGN_UP where BASICS_ID = :basicsId  ", params,Integer.class);
        return count;
    }

    @Override
    public List<TbActivityBasicsEntity> queryHotelNameRoomType(String id) {
        Map<String,Object> parmeterMap = new HashMap<>();
        parmeterMap.put("id",id);
        List<TbActivityBasicsEntity> list = this.jdbcTemplate.query(
                "select * from TB_ACTIVITY_BASICS where code in (select BASICS_ID from TB_ACTIVITY_SIGN_UP where id = :id)",
                parmeterMap, new BeanPropertyRowMapper(TbActivityBasicsEntity.class));
        return list;
    }

    @Override
    public Map<String,Object> queryHotelNameRoomType(List<TbActivityBasicsEntity> list) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(list.get(0));
        List<Map<String, Object>> HotelNameRoomType = this.jdbcTemplate.queryForList("select id,Hotel_name \"value\",Hotel_name \"label\" " +
                "from TB_ACTIVITY_HOTEL where basics_id = :id", Parameter);
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String,Object> Map = new HashMap<>();
        Map<String,Object> resultMap = new HashMap<>();
        for(Map<String, Object> map:HotelNameRoomType){
            Set<String> strings = map.keySet();
            Map<String,Object> mapHotel = new HashMap<>();
            for(String str:strings){
                mapHotel.put(str,map.get(str));
            }
            mapList.add(mapHotel);
            resultMap.put(map.get("value")==null?"":map.get("value").toString() , this.jdbcTemplate.queryForList(
                    "select id,HOTEL_TYPE \"value\",HOTEL_TYPE \"label\" " +
                            "from TB_ACTIVITY_HOTEL_ROOM where  HOTEL_ID= :ID  and nvl(HOTEL_TYPE,'-1')!='-1'" , map ) );
            Map.put("hotelNameMap",mapList);
            Map.put("Option",resultMap);
        }
        return Map;
    }

    @Override
    public List<Map<String, Object>> RegistrationPersonnelDetails(String id,Map<String, Object>  HotelNameRoomType) {
        Map<String,Object> parmeterMap = new HashMap<>();
        parmeterMap.put("id",id);
        List<TbActivityOrderDetailsEntity> list = this.jdbcTemplate.query("select * from TB_ACTIVITY_ORDER_DETAILS where Order_id in (select Order_id from TB_ACTIVITY_ORDER_DETAILS where SIGN_UP_id = :id)",parmeterMap, new BeanPropertyRowMapper(TbActivityOrderDetailsEntity.class));
        List<Map<String,Object>> resultList = new ArrayList<>();
        for(TbActivityOrderDetailsEntity TbActivityOrderDetailsEntity:list){
            Map<String,Object> resultMap = new HashMap<>();
            SqlParameterSource Parameter = new BeanPropertySqlParameterSource(TbActivityOrderDetailsEntity);
            //报名人员
            List<TbActivitySignUpEntity> tbActivitySignUpEntityList = this.jdbcTemplate.query("select * from TB_ACTIVITY_SIGN_UP where id = :signUpId", Parameter, new BeanPropertyRowMapper(TbActivitySignUpEntity.class));
            TbActivitySignUpEntity tbActivitySignUpEntity = tbActivitySignUpEntityList.get(0);
            resultMap.put("tbActivitySignUpEntity",tbActivitySignUpEntity);
            //接送机
            SqlParameterSource TbActivitySignUpEntityParameter = new BeanPropertySqlParameterSource(tbActivitySignUpEntity);
            List<TbPeopleReceiveSendEntity> tbPeopleReceiveSendEntity = this.jdbcTemplate.query("select * from TB_PEOPLE_RECEIVE_SEND where PEOPLE_ID = :id",TbActivitySignUpEntityParameter, new BeanPropertyRowMapper(TbPeopleReceiveSendEntity.class));
            resultMap.put("tbPeopleReceiveSendEntity",tbPeopleReceiveSendEntity);
            //住宿
            List<TbPeoplePutUpEntity> tbPeoplePutUpEntity = this.jdbcTemplate.query("select * from TB_PEOPLE_PUT_UP where PEOPLE_ID = :id",TbActivitySignUpEntityParameter, new BeanPropertyRowMapper(TbPeoplePutUpEntity.class));
            //设置住宿中的酒店和房型
            for(TbPeoplePutUpEntity TbPeoplePutUpEntity:tbPeoplePutUpEntity){
                List<Map<String, Object>> aa = (List<Map<String, Object>>)HotelNameRoomType.get("hotelNameMap");
                TbPeoplePutUpEntity.setHotelNameMap(aa);
                Map<String,Object> option = (Map<String,Object>) HotelNameRoomType.get("Option");
                TbPeoplePutUpEntity.setOption(option);
            }
            resultMap.put("tbPeoplePutUpEntity",tbPeoplePutUpEntity);
            //订单
            resultMap.put("tbActivityOrderDetailsEntity",TbActivityOrderDetailsEntity);
            resultList.add(resultMap);
        }
        return resultList;
    }

    @Override
    public void updateUpdateList(List list) {
        updateAll(list);
    }

    @Override
    public void updateUpdate(Object o) {
        update(o);
    }

    @Override
    public void deleteObject(Object o) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(o);
        this.jdbcTemplate.update("delete TB_PEOPLE_RECEIVE_SEND where PEOPLE_ID = :id",Parameter);
    }

    @Override
    public void addObjectList(List list) {
        String sqlAll = BeanCopyUtil.getSaveSqlAll(list.get(0));
        SqlParameterSource[] Parameter = SqlParameterSourceUtils.createBatch(list.toArray());
        System.out.println(sqlAll);
        jdbcTemplate.batchUpdate(sqlAll,Parameter);
    }

}
