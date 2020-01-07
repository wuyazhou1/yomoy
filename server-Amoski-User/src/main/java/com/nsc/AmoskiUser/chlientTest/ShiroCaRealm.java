package com.nsc.AmoskiUser.chlientTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TUserEntity;

import com.nsc.AmoskiUser.service.AccountService;
import com.nsc.AmoskiUser.service.UserManageService;
import com.nsc.Amoski.util.PasswordUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroCaRealm extends AuthorizingRealm{
	Logger logger = LoggerFactory.getLogger(ShiroCaRealm.class);

	@Autowired
	protected DoAuthorizationInfo doAuthorizationInfo;
	@Autowired(required=false)
	protected AccountService accountService;
	@Autowired(required=false)
	private UserManageService userGovern;
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		logger.info("ShiroCaRealm==>doGetAuthorizationInfo");
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		return doAuthorizationInfo.shiro(shiroUser.getLoginName());
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		logger.info("ShiroCaRealm==>doGetAuthenticationInfo");
		String LOGIN_CA_ERROR_MSG="";//错误提示
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String username=token.getUsername().toUpperCase();//姓名
		String idCode=new String(token.getPassword());//身份证号
		try {
			String [] arr=idCode.split("_");
			token.setPassword(arr[0].toCharArray());
			idCode=arr[0];
			String idCaStr=arr[1];//是否为ca登录
			if(!idCaStr.equals("0")){//如果不是证书登录
				return null;
			}
		} catch (Exception e) {//此时为非证书登录
			return null;
		}
		//首先根据身份证号查询t_user中是否存在该用户
		List<TUserEntity> users =accountService.findUserByIdCode(idCode);
		TUserEntity user=null;
		if(users!=null&&users.size()>0){
			user=users.get(0);
			if(!user.getLocked().equals("1")){//没有锁定，直接登录  1为true   0为false  0是未锁定
				if(StringUtil.isNullOrEmpty(user.getOrgCode())){//是否有机构，有机构直接登录，没有机构进行提示
					return doAuthorizationInfo.shiroCACheck(user,getName(),idCode);
				}else{
					LOGIN_CA_ERROR_MSG="当前登录用户没有所属机构，请联系管理员";
				}
			}else{//用户已锁定，给出提示
				LOGIN_CA_ERROR_MSG="当前登录用户已锁定，如需解锁请联系管理员";
			}
		}else{//用户在t_user中没有找到，到sys_t_user中查找  k:loginname(plcno)  v1:name   v2:idCode  v3:deptno
			Map<String,Object> maps=new HashMap<String,Object>();
			maps.put("name", username);
			maps.put("idCode", idCode);
			List<MapVo2> mvs=accountService.selectByNameAndIdCodeInSysUser(maps);
			MapVo2 mv=null;
			if(mvs!=null&&mvs.size()>0){//找到信息，初始化到t_user中
				mv=mvs.get(0);
				TUserEntity newUser=new TUserEntity();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				newUser.setCreateDate(new Date(System.currentTimeMillis()));
				newUser.setLoginname(mv.getK());
				newUser.setLocked("1");//锁定
				newUser.setName(mv.getV1());
				PasswordUtil.entryptPassword(newUser);
				//userGovern.addInsertTUserSave(newUser);
				logger.info(newUser.getLoginname()+",user add success");
				LOGIN_CA_ERROR_MSG="当前登录用户没有所属机构，请联系管理员";
			}else{//sys_t_user中没有找到信息
				logger.info("sys_t_user not exist");
				LOGIN_CA_ERROR_MSG="找不到当前用户信息";
			}
		}
		/*
		 * 1.查找t_user where 身份证与用户姓名 
		 * 	 判断可用标志——》不可用：用户为冻结状态 并 退出
		 * 	ture：正常登录
		 * 2、未找到查找SYS_T_USER where 身份证，姓名，
		 *  判断可用标志--》用户为冻结状态 退出 
		 *  			 未冻结 将数据初始化到 t_user 表中 姓名，警号，身份证号，机构（判断是否有机构 有机构初始化，没有则提示还没有机构并）
		 *  ，登录名（警号），冻结状态（冻结）
		 *  初始化后提示 当前登录信息不是系统用户请联系管理员分配权限  
		 *
		 * 3、管理员维护：查看冻结人员信息找到需要付权限的人员，1)、修改人员登录名（默认为自动插入的警号），2）、分配机构、3）、符权限
		 */
		logger.info(LOGIN_CA_ERROR_MSG);
		SecurityUtils.getSubject().getSession().setAttribute("LOGIN_CA_ERROR_MSG",LOGIN_CA_ERROR_MSG);
		return null;
	}
	
//	@PostConstruct
//	public void initCredentialsMatcher() {
//		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(PasswordUtil.HASH_ALGORITHM);
//		matcher.setHashIterations(PasswordUtil.HASH_INTERATIONS);
//		setCredentialsMatcher(matcher);
//	}
	

}
