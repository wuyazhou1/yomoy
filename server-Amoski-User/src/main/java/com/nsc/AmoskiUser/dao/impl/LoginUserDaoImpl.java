package com.nsc.AmoskiUser.dao.impl;

import com.nsc.AmoskiUser.client.RedisObjectEntity;
import com.nsc.AmoskiUser.dao.LoginUserDao;
import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TUserEntity;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LoginUserDaoImpl extends GenericRepositoryImpl implements LoginUserDao {
    @Override
    public TUserEntity findUserByLoginName(String loginName) {
        Map<String,String> parentMap = new HashMap<String, String>();
        parentMap.put("loginName",loginName);
        List<TUserEntity> query = this.jdbcTemplate.query("select * from t_user where LOGINNAME = :loginName  and LOCKED=1", parentMap, new BeanPropertyRowMapper(TUserEntity.class));
        if(query!=null && query.size() > 0 ){
            return query.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<String> finRoleCodeByUserCode(String code) {
        Map<String,String> parentMap = new HashMap<String, String>();
        parentMap.put("code",code);
        return this.jdbcTemplate.queryForList("SELECT ROLE_CODE FROM T_ROLE_USER_REL where USER_CODE = :code ", parentMap, String.class);
    }

    @Override
    public List<String> findMenuResourceByUserCode(String code) {
        Map<String,String> parentMap = new HashMap<String, String>();
        parentMap.put("code",code);
        return this.jdbcTemplate.queryForList("select * from ( " +
                "SELECT 'menu'||MENU_CODE menu_resource_code FROM T_ROLE_MENU_RESOURCE_REL  " +
                "where ROLE_CODE in (SELECT ROLE_CODE FROM T_ROLE_USER_REL where USER_CODE = :code ) " +
                "union all  " +
                "SELECT 'res'||RES_CODE menu_resource_code FROM T_ROLE_MENU_RESOURCE_REL  " +
                "where ROLE_CODE in (SELECT ROLE_CODE FROM T_ROLE_USER_REL where USER_CODE = :code  and  nvl(RES_CODE,'-999')!='-999') " +
                ") ", parentMap, String.class);
    }

    @Override
    public List<String> findMenuResourceAll() {
        return this.jdbcTemplate.queryForList("select 'menu'||code from t_menu " +
                "union all " +
                "select 'res'||code from t_resource", new HashMap<>(), String.class);
    }

    @Override
    @CachePut(value = "String",key = "T(String).valueOf(#ipAddr).concat('-TUserEntity')")
    public TUserEntity putIpAddrTUserEntity(String ipAddr, TUserEntity user) {
        return user;
    }

    @Override
    @CachePut(value = "String",key = "T(String).valueOf(#ipAddr).concat('-ShiroUser')")
    public ShiroUser putIpAddrShiroUser(String ipAddr, ShiroUser shiroUser) {
        return shiroUser;
    }

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf(#ipAddr).concat('-TUserEntity')")
    public TUserEntity putIpAddrTUserEntity(String ipAddr) {
        return null;
    }

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf(#ipAddr).concat('-ShiroUser')")
    public ShiroUser putIpAddrShiroUser(String ipAddr) {
        return null;
    }

    @Override
    @CachePut(value = "String",key = "T(String).valueOf(#ipAddr)")
    public Object putLoginToke(String ipAddr, Object request) {
        RedisObjectEntity redis = new RedisObjectEntity();
        ShiroUser shiroUser = (ShiroUser) request;
        String md5Pwd = new Md5Hash(shiroUser.getPassword(),shiroUser.getLoginName()).toHex();
        redis.setShiroUser(shiroUser);
        redis.setMd5Pwd(md5Pwd);
        redis.setLoginName(shiroUser.getLoginName());
        return redis;
    }

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf(#ipAddr)")
    public Object cacheaLoginToke(String ipAddr) {
        return null;
    }

    @Override
    public List<Map<String, Object>> findMenuFrameListMap(String o) {
        Map<String,String> parentMap = new HashMap<>();
        parentMap.put("CODE",o);
        List<Map<String, Object>> resultListMap = this.jdbcTemplate.queryForList("select * from T_MENU where nvl(PARENT_CODE , '-1') = nvl( :CODE ,'-1')", parentMap);
        for(Map<String,Object> resultMap:resultListMap){
            List<Map<String, Object>> menuFrameListMap = findMenuFrameListMap(resultMap);
            if(menuFrameListMap!=null){
                resultMap.put("NEXT_MENU",menuFrameListMap);
            }
        }
        return resultListMap;
    }

    public List<Map<String, Object>> findMenuFrameListMap(Map<String,Object> parentMap) {
        List<Map<String, Object>> resultListMap = this.jdbcTemplate.queryForList("select * from T_MENU where nvl(PARENT_CODE , '-1') = nvl( :CODE ,'-1')", parentMap);
        if(resultListMap!=null){
            for(Map<String,Object> resultMap:resultListMap){
                List<Map<String, Object>> menuFrameListMap = findMenuFrameListMap(resultMap);
                if(menuFrameListMap!=null){
                    resultMap.put("NEXT_MENU",menuFrameListMap);
                }
            }
            return resultListMap;
        }else{
            return null;
        }
    }


}
