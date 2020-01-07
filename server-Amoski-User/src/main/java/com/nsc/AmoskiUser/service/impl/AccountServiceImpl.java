package com.nsc.AmoskiUser.service.impl;


import java.util.List;
import java.util.Map;

import com.nsc.AmoskiUser.chlientTest.MapVo2;
import com.nsc.AmoskiUser.dao.UserDao;
import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TUserEntity;
import com.nsc.AmoskiUser.service.AccountService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Spring Bean�ı�ʶ.
@Service
@Transactional(transactionManager = "UserTransactionManager")
public class AccountServiceImpl implements AccountService {

	@Autowired
	private UserDao userDao;

	public TUserEntity get(Long id) throws Exception {
		TUserEntity user=null;
			user = userDao.findById(id);
		return user;
	}

	public List<TUserEntity> findUserByLoginName(String userName) {
		return userDao.findByLoginName(userName);
	}
	

	public ShiroUser getCurrentUser() {
		Subject currentUser = SecurityUtils.getSubject();
		ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipal();
		return shiroUser;
	}

	@Override
	public List<Map<String, Object>> findRoleByLonginName(String loginName) {
		return userDao.findRoleByLonginName(loginName);
	}

	@Override
	public List<Map<String, Object>> findMenuCodeByRoleIdStr(String string) {
		return userDao.findMenuCodeByRoleIdStr(string);
	}

	@Override
	public List<Map<String, Object>> findResourceCodeByRoleIdStr(String string) {
		return userDao.findResourceCodeByRoleIdStr(string);
	}

	@Override
	public List<Map<String, Object>> selectMenuCodeAll() {
		return userDao.selectMenuCodeAll();
	}

	@Override
	public List<Map<String, Object>> selectResCodeAll() {
		return userDao.selectResCodeAll();
	}

	@Override
	public List<TUserEntity> selectByNameAndIdCodeInUser(String name,String idCode) {
		return userDao.selectByNameAndIdCodeInUser(name,idCode);
	}

	@Override
	public List<MapVo2> selectByNameAndIdCodeInSysUser(Map<String,Object> maps) {
		return userDao.selectByNameAndIdCodeInSysUser(maps);
	}

	@Override
	public List<TUserEntity> findUserByIdCode(String idCode) {
		return userDao.findUserByIdCode(idCode);
	}
}
