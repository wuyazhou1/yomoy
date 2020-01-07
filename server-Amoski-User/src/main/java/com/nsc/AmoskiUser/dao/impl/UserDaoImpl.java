package com.nsc.AmoskiUser.dao.impl;

import com.nsc.AmoskiUser.chlientTest.MapVo2;
import com.nsc.AmoskiUser.dao.UserDao;
import com.nsc.Amoski.entity.TUserEntity;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class UserDaoImpl extends GenericRepositoryImpl implements UserDao {
    @Override
    public List<TUserEntity> findByLoginName(String loginName) {
        Map<String,String> parentMap = new HashMap<>();
        parentMap.put("loginName" ,loginName);
        List query = this.jdbcTemplate.query("select * from T_USER where LOGINNAME = :loginName", parentMap, new BeanPropertyRowMapper(TUserEntity.class));
        return query;
    }

    @Override
    public List<TUserEntity> findUserByIdCode(String idCode) {
        return null;
    }

    @Override
    public TUserEntity findById(Long loginName) {
        return null;
    }

    @Override
    public int changePwd(Map<String, Object> mapParam) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> findRoleByLonginName(String loginName) {
        return null;
    }

    @Override
    public List<Map<String, Object>> findMenuCodeByRoleIdStr(String RoleId) {
        return null;
    }

    @Override
    public List<Map<String, Object>> findResourceCodeByRoleIdStr(String RoleId) {
        return null;
    }

    @Override
    public List<Map<String, Object>> selectMenuCodeAll() {
        return null;
    }

    @Override
    public List<Map<String, Object>> selectResCodeAll() {
        return null;
    }

    @Override
    public List<TUserEntity> selectByNameAndIdCodeInUser(String name, String idCode) {
        return null;
    }

    @Override
    public List<MapVo2> selectByNameAndIdCodeInSysUser(Map<String, Object> maps) {
        return null;
    }
}
