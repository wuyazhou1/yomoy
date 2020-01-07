package com.nsc.AmoskiUser.chlientTest;


import java.util.List;

import javax.annotation.PostConstruct;

import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TUserEntity;
import com.nsc.AmoskiUser.service.AccountService;
import com.nsc.Amoski.util.PasswordUtil;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroDbRealm extends AuthorizingRealm{
	
	Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);
	
	@Autowired(required=false)
	protected AccountService accountService;
	@Autowired
	protected DoAuthorizationInfo doAuthorizationInfo;

	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		logger.info("进入ShiroDbRealm==》doGetAuthenticationInfo");
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		TUserEntity  user = null;
		List<TUserEntity> userlist =accountService.findUserByLoginName(token.getUsername().toUpperCase());
		if(null!=userlist&&userlist.size()==1){
			user = userlist.get(0);
		}
		if (user != null) {
			return doAuthorizationInfo.shiroDBCheck(user,getName());
		} else {
			return null;
		}
	}
	@PostConstruct
	public void initCredentialsMatcher() {
		logger.info("进入ShiroDbRealm==》initCredentialsMatcher");
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(PasswordUtil.HASH_ALGORITHM);
		matcher.setHashIterations(PasswordUtil.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}


	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		logger.info("进入ShiroDbRealm==》doGetAuthorizationInfo");
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		return doAuthorizationInfo.shiro(shiroUser.getLoginName());
	}
	
}
