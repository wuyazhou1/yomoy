package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.SameCityDao;
import com.nsc.Amoski.entity.MemberDto;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.StringUtils;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class SameCityDaoImpl extends GenericRepositoryImpl implements SameCityDao {
    @Override
    public void updateMemberYXAxis(MemberView memberView) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(memberView);
        this.jdbcTemplate.update("update t_member set x_Axis = :xAxis,y_Axis = :yAxis  where id= :id ",Parameter);
    }

    @Override
    public List queryMemberByYXAxis(MemberView memberView) {
        memberView.InfoQueryParent();
        StringBuffer whereSql = new StringBuffer("");
        if(!StringUtils.isEmpty(memberView.getDistance())){
            whereSql.append(" where  a.sqrtValue <= :distance ");
        }
        logger.info("查询附近全国人员信息queryMemberByYXAxis==》memberView==》"+memberView);
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(memberView);
        return this.jdbcTemplate.query("SELECT  *  FROM (SELECT q.*, ROWNUM AS rn FROM (" +
                " select a.id,a.name,b.sex,b.HEAD_IMG_FILE member_image,a.distance_sum,a.sqrtValue " +
                " from ( select a.ID,a.NAME,a.LOGINNAME,a.REMARK,a.BINDING_IDENTIFICATION,a.LOGIN_IDENTIFICATION,a.UPDATE_NAME,a.UPDATE_DATE,a.CREATE_DATE,a.Y_AXIS,a.X_AXIS," +
                " nvl((select sum(distance_sum) from T_member_Riding_ranking x  where x.member_id=a.id and to_char(sysdate - 1 ,'yyyyMMdd')=to_char(x.create_date,'yyyyMMdd')),'0') distance_sum," +
                " SQRT( + ((( :yAxis - Y_AXIS) * ACOS(-1) * 12656 *  " +
                "   COS((( :yAxis + Y_AXIS) / 2) * ACOS(-1) / 180) / 180) *  " +
                "   (( :yAxis - Y_AXIS) * ACOS(-1) * 12656 *  " +
                "   COS((( :yAxis + Y_AXIS) / 2) * ACOS(-1) / 180) / 180)) +   " +
                "   ((( :xAxis - X_AXIS) * ACOS(-1) * 12656 / 180) *  " +
                "   (( :xAxis - X_AXIS) * ACOS(-1) * 12656 / 180))) sqrtValue " +
                "   from t_member a ) a" +
                " left join T_MEMBER_DAIL b on a.id = b.MEMBER_ID  "+whereSql.toString()+"  " +
                " order by a.distance_sum desc" +
                " ) q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength",Parameter,new BeanPropertyRowMapper(MemberDto.class));
    }

    @Override
    public List<MemberDto> queryMemberByMemberName(MemberView memberView) {
        StringBuffer where = new StringBuffer(" where 1=1 ");
        memberView.InfoQueryParent();
        if(!StringUtils.isEmpty(memberView.getName())){
            where.append("and a.name like '%' || :name || '%'  ");
        }
        if(!StringUtils.isEmpty(memberView.getId())){
            where.append("and a.id = :id  ");
        }
        if(!StringUtils.isEmpty(memberView.getDistance())){
            where.append("and a.sqrtValue <= :distance  ");
        }
        if(StringUtils.isEmpty(memberView.getCycle())){
            memberView.setCycle("1");
        }

        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(memberView);
        return this.jdbcTemplate.query("SELECT  *  FROM (SELECT q.*, ROWNUM AS rn FROM ( " +
                " select a.id,a.name,b.sex,b.HEAD_IMG_FILE member_image,a.distance_sum,a.sqrtValue " +
                " from ( select a.ID,a.NAME,a.LOGINNAME,a.REMARK,a.BINDING_IDENTIFICATION,a.LOGIN_IDENTIFICATION,a.UPDATE_NAME,a.UPDATE_DATE,a.CREATE_DATE,a.Y_AXIS,a.X_AXIS," +
                " nvl((select sum(distance_sum) from T_member_Riding_ranking x  where x.member_id=a.id and to_char(sysdate - :cycle ,'yyyyMMdd')=to_char(x.create_date,'yyyyMMdd') ),'0') distance_sum, " +
                " SQRT( + ((( :yAxis - Y_AXIS) * ACOS(-1) * 12656 *  " +
                "   COS((( :yAxis + Y_AXIS) / 2) * ACOS(-1) / 180) / 180) *  " +
                "   (( :yAxis - Y_AXIS) * ACOS(-1) * 12656 *  " +
                "   COS((( :yAxis + Y_AXIS) / 2) * ACOS(-1) / 180) / 180)) +   " +
                "   ((( :xAxis - X_AXIS) * ACOS(-1) * 12656 / 180) *  " +
                "   (( :xAxis - X_AXIS) * ACOS(-1) * 12656 / 180))) sqrtValue " +
                "   from t_member a ) a" +
                " left join T_MEMBER_DAIL b on a.id = b.MEMBER_ID  " + where.toString() +
                " order by a.distance_sum desc " +
                " ) q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength ",
                Parameter,new BeanPropertyRowMapper(MemberDto.class));
    }

    @Override
    public Integer queryMemberByMemberNameCount(MemberView memberView) {
        StringBuffer where = new StringBuffer(" where 1=1 ");
        memberView.InfoQueryParent();
        if(!StringUtils.isEmpty(memberView.getName())){
            where.append("and a.name like '%' || :name || '%'  ");
        }
        if(!StringUtils.isEmpty(memberView.getId())){
            where.append("and a.id = :id  ");
        }
        if(!StringUtils.isEmpty(memberView.getDistance())){
            where.append("and a.sqrtValue <= :distance  ");
        }
        if(StringUtils.isEmpty(memberView.getCycle())){
            memberView.setCycle("1");
        }
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(memberView);
        Integer count= this.jdbcTemplate.queryForObject("" +
                        " select count(1) " +
                        " from ( select a.ID,a.NAME," +
                        " SQRT( + ((( :yAxis - Y_AXIS) * ACOS(-1) * 12656 *  " +
                        "   COS((( :yAxis + Y_AXIS) / 2) * ACOS(-1) / 180) / 180) *  " +
                        "   (( :yAxis - Y_AXIS) * ACOS(-1) * 12656 *  " +
                        "   COS((( :yAxis + Y_AXIS) / 2) * ACOS(-1) / 180) / 180)) +   " +
                        "   ((( :xAxis - X_AXIS) * ACOS(-1) * 12656 / 180) *  " +
                        "   (( :xAxis - X_AXIS) * ACOS(-1) * 12656 / 180))) sqrtValue " +
                        "   from t_member a ) a" + where.toString() ,
                Parameter,Integer.class);
        return count;
    }


    @Override
    public List<MemberDto> queryMemberRankingList(MemberView memberView) {
        StringBuffer where = new StringBuffer(" where 1=1 ");
        memberView.InfoQueryParent();
        /*if(!StringUtils.isEmpty(memberView.getName())){
            where.append("and a.neme like '%' || :name || '%'  ");
        }*/
        if(!StringUtils.isEmpty(memberView.getDistance())){
            where.append("and a.sqrtValue <= :distance  ");
        }
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(memberView);
        return this.jdbcTemplate.query("SELECT  *  FROM (SELECT q.*, ROWNUM AS rn FROM ( " +
                        " select a.id,a.name,b.sex,b.HEAD_IMG_FILE member_image,a.distance_sum,a.sqrtValue " +
                        " from ( select a.ID,a.NAME,a.LOGINNAME,a.REMARK,a.BINDING_IDENTIFICATION,a.LOGIN_IDENTIFICATION,a.UPDATE_NAME,a.UPDATE_DATE,a.CREATE_DATE,a.Y_AXIS,a.X_AXIS," +
                        " nvl((select sum(distance_sum) from T_member_Riding_ranking x  " +
                        "       where x.member_id=a.id and to_char(sysdate- :cycle ,'yyyyMMdd')=to_char(x.create_date,'yyyyMMdd')),'0') distance_sum, " +
                        " SQRT( + ((( :yAxis - Y_AXIS) * ACOS(-1) * 12656 *  " +
                        "   COS((( :yAxis + Y_AXIS) / 2) * ACOS(-1) / 180) / 180) *  " +
                        "   (( :yAxis - Y_AXIS) * ACOS(-1) * 12656 *  " +
                        "   COS((( :yAxis + Y_AXIS) / 2) * ACOS(-1) / 180) / 180)) +   " +
                        "   ((( :xAxis - X_AXIS) * ACOS(-1) * 12656 / 180) *  " +
                        "   (( :xAxis - X_AXIS) * ACOS(-1) * 12656 / 180))) sqrtValue " +
                        "   from t_member a ) a" +
                        " left join T_MEMBER_DAIL b on a.id = b.MEMBER_ID  " + where.toString() +
                        " order by a.distance_sum desc " +
                        " ) q  WHERE ROWNUM <= :endLength ) w  WHERE w.rn > :startLength ",
                Parameter,new BeanPropertyRowMapper(MemberDto.class));
    }

    @Override
    public void saveBackgroundImages(MemberView dto) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(dto);
        this.jdbcTemplate.update("update T_MEMBER_DAIL " +
                "set background_images = :backgroundImages " +
                "where member_id = :id",Parameter);
    }
}
