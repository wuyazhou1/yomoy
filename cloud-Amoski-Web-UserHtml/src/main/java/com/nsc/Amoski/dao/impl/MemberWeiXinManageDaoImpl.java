package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.MemberWeiXinManageDao;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.TMemberDailEntity;
import com.nsc.Amoski.entity.TMemberEntity;
import com.nsc.Amoski.entity.TWeixinEntity;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.AmoskiException;
import com.nsc.Amoski.util.BeanCopyUtil;
import com.nsc.Amoski.util.PasswordUtil;
import com.nsc.Amoski.util.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberWeiXinManageDaoImpl extends GenericRepositoryImpl implements MemberWeiXinManageDao {

    @Override
    public TMemberEntity AddTMemberEntity(TMemberEntity tMemberEntity) {
        return this.saveEntity(tMemberEntity);
    }

    @Override
    public void addWeiXin(MemberView memberView) {
        String sqlAll = BeanCopyUtil.getSaveSqlAll(new TWeixinEntity());
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(memberView);
        System.out.println(sqlAll);
        jdbcTemplate.update(sqlAll,Parameter);
    }

    @Override
    public Boolean checkedMeneberLogin(String loginName, String password) {
        Map<String,String> parentMap = new HashMap<String,String>();
        parentMap.put("loginName",loginName);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from t_member where loginname = :loginName  or tel = :loginName ", parentMap);
        parentMap.put("password", PasswordUtil.setPassWordBySalt(password,maps.get(0).get("SALT").toString()));
        Integer count = jdbcTemplate.queryForObject("select count(1) from t_member where (loginname = :loginName  or tel = :loginName ) and password = :password ", parentMap, Integer.class);
        if(count!=0){
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkedWeiXin(String openId) {
        Map<String,String> parentMap = new HashMap<String, String>();
        parentMap.put("openId",openId);
        Integer count = jdbcTemplate.queryForObject("select count(1) from t_weixin where open_id = :openId ", parentMap, Integer.class);
        if(count!=0){
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkedLoginRepeat(String tel, String loginName) {
        StringBuffer str = new StringBuffer("");
        Map<String,String> parentMap = new HashMap<String, String>();
        if(tel!=null){
            str.append(" or tel = :tel ");
            parentMap.put("tel",tel);
        }else{
            str.append(" or loginName = :loginName ");
            parentMap.put(" or loginName",loginName);
        }
        Integer count = jdbcTemplate.queryForObject("select count(1) from t_weixin where 1=1 "+str.toString(), parentMap, Integer.class);
        if(count!=0){
            return true;
        }
        return false;
    }

    @Override
    public TMemberEntity TelRegister(MemberView memberView) {
        TMemberEntity tMemberEntity = new TMemberEntity(memberView);
        tMemberEntity.setCreateDate(new Date());
        tMemberEntity = this.saveEntity(tMemberEntity);
        memberView.setId(tMemberEntity.getId());
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(tMemberEntity);
        Integer count = this.getJdbcTemplate().queryForObject("select count(1) from T_MEMBER_DAIL where MEMBER_ID = :id", Parameter, Integer.class);
        if(count>0){
            throw new AmoskiException("此会员已经存在");//没找到帐号
        }
        TMemberDailEntity TMemberDailEntity = new TMemberDailEntity(memberView);
        //TMemberDailEntity.setMemberId(memberView.getId().toString());
        this.save(TMemberDailEntity);

        TWeixinEntity TWeixinEntity = new TWeixinEntity(memberView);
        this.save(TWeixinEntity);
        //updateMemberCreateDate(tMemberEntity);
        return tMemberEntity;

    }

    @Override
    public MemberView findMemberView(String tel, String loginName,String openId) {
        Map<String,String> parentMap = new HashMap<String, String>();
        StringBuffer sql = new StringBuffer("");
        if(StringUtils.isEmpty(tel)&&StringUtils.isEmpty(loginName) && StringUtils.isEmpty(openId))
            return null;
        if(!StringUtils.isEmpty(tel)){
            parentMap.put("tel",tel);
            sql.append(" or a.TEL = :tel ");
        }
        if(!StringUtils.isEmpty(loginName)){
            parentMap.put("loginName",loginName);
            sql.append(" or a.LOGINNAME = :loginName ");
        }
        if(!StringUtils.isEmpty(openId)) {
            parentMap.put("openId", openId);
            sql.append(" or b.OPEN_ID = :openId ");
        }
        List<MemberView> returnList = jdbcTemplate.query("select a.ID,a.NAME,a.LOGINNAME,a.PASSWORD,a.SALT,a.TEL,a.LOCKED,a.ORG_CODE,a.REMARK," +
                "a.BINDING_IDENTIFICATION,a.LOGIN_IDENTIFICATION,a.UPDATE_NAME,a.UPDATE_DATE,b.ID wxid,b.MEMBER_ID,b.OPEN_ID,b.SUBSCRIBE," +
                "b.SUBSCRIBE_TIME,b.NICKNAME,b.SEX,b.COUNTRY,b.PROVINCE,b.CITY,b.LANGUAGE,b.HEAD_IMG_URL," +
                "c.ID dail_Id,c.real_Name,c.MEMBER_ID member_Dail_Id,c.IDENTITY_CARD identity_Card,c.SEX member_Sex,c.HEAD_IMG_PROJECT,c.HEAD_IMG_FILE,c.YEAR_OF_BIRTH,c.ADDRESS," +
                "c.ISATTESTATION,c.SYNOPSIS " +
                "from t_member a " +
                "left join t_weixin b on a.id=b.member_id " +
                "left join T_MEMBER_DAIL c on a.id=c.member_id " +
                "where  1!=1" + sql.toString(), parentMap, new BeanPropertyRowMapper(MemberView.class));
        if(returnList!=null && returnList.size()>0)
            return returnList.get(0);
        else{
            return null;
        }
    }

    @Override
    public void updateTmemberEntity(TMemberEntity tMemberEntity) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(tMemberEntity);
        this.jdbcTemplate.update("UPDATE T_MEMBER SET NAME=:name,LOGINNAME=:loginname,PASSWORD=:password,SALT=:salt,TEL=:tel,LOCKED=:locked,ORG_CODE=:orgCode,REMARK=:remark,BINDING_IDENTIFICATION=:bindingIdentification,LOGIN_IDENTIFICATION=:loginIdentification,UPDATE_NAME=:updateName,UPDATE_DATE=:updateDate where ID=:id",
                Parameter
        );
        //this.update(tMemberEntity);
    }

    @Override
    public void updateTWeiXinEntity(TWeixinEntity tWeixinEntity) {
        this.update(tWeixinEntity);
    }

    @Override
    public void AddTMemberDailEntity(TMemberDailEntity tMemberDailEntity) {
        this.save(tMemberDailEntity);
    }

    @Override
    public void updateTMemberDailEntity(TMemberDailEntity tMemberDailEntity) {
        this.update(tMemberDailEntity);
    }

    @Override
    public void updateMemberCreateDate(TMemberEntity tMemberEntity) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(tMemberEntity);
        this.jdbcTemplate.update("update T_MEMBER set CREATE_DATE=sysdate where id = :id",Parameter);
    }

}
