package com.nsc.AmoskiUser.service;

import java.util.List;
import java.util.Map;

import com.nsc.AmoskiUser.chlientTest.MapVo2;
import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TUserEntity;

public interface AccountService {
	public TUserEntity get(Long id) throws Exception ;
	
	public List<TUserEntity> findUserByLoginName(String userName) ;
	List<TUserEntity> findUserByIdCode(String idCode);
	
	public ShiroUser getCurrentUser();

	public List<Map<String, Object>> findRoleByLonginName(String loginName);

	public List<Map<String, Object>> findMenuCodeByRoleIdStr(String string);

	public List<Map<String, Object>> findResourceCodeByRoleIdStr(String string);

	public List<Map<String, Object>> selectMenuCodeAll();

	public List<Map<String, Object>> selectResCodeAll();
	
//	public List<Role> findRolesByLoginName(String loginName);
	List<TUserEntity> selectByNameAndIdCodeInUser(String name, String idCode);
	List<MapVo2> selectByNameAndIdCodeInSysUser(Map<String, Object> maps);
}
