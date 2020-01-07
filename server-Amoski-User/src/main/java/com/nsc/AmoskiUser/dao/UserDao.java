package com.nsc.AmoskiUser.dao;

import java.util.List;
import java.util.Map;

import com.nsc.AmoskiUser.chlientTest.MapVo2;
import com.nsc.Amoski.entity.TUserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao  {
	

	List<TUserEntity> findByLoginName(String loginName);
	List<TUserEntity> findUserByIdCode(String idCode);
	TUserEntity findById(Long loginName);
    int changePwd(Map<String, Object> mapParam) ;
	List<Map<String, Object>> findRoleByLonginName(@Param("loginName") String loginName);
	List<Map<String, Object>> findMenuCodeByRoleIdStr(@Param("RoleId") String RoleId);
	List<Map<String, Object>> findResourceCodeByRoleIdStr(@Param("RoleId") String RoleId);
	List<Map<String, Object>> selectMenuCodeAll();
	List<Map<String, Object>> selectResCodeAll();
	
	List<TUserEntity> selectByNameAndIdCodeInUser(@Param("name") String name, @Param("idCode") String idCode);
	List<MapVo2> selectByNameAndIdCodeInSysUser(Map<String, Object> maps);
}
