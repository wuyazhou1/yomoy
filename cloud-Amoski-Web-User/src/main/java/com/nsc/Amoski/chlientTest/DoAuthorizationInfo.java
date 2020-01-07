package com.nsc.Amoski.chlientTest;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TUserEntity;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoAuthorizationInfo {

	Logger logger = LoggerFactory.getLogger(DoAuthorizationInfo.class);

	
	protected AuthorizationInfo shiro(String loginname) {
		//创建菜单元素权限集合
		Set<String> menuRouse=new HashSet<String>();
		//创建角色编码权限集合
		Set<String> role = new HashSet<String>();
		//创建查询条件字符串
		StringBuffer str = new StringBuffer();
		//设置默认不是admin用户
		boolean roleid=false;
		//查询当前用户拥有的角色
		//List<Map<String, Object>> list = accountService.findRoleByLonginName(loginname);
		List<Map<String, Object>> list =null;
		if(list.size()!=0){//判断结果集是否有数据
			for(Map<String, Object> map : list){//循环遍历角色
				//设置角色权限
				role.add(map.get("CODE").toString());
				//拼接角色in对象sql语句
				str.append("'"+map.get("CODE").toString()+"',");
				if(map.get("CODE").toString().equals("admin")){//判断是否是admin角色
					roleid = true;
					break;
				}
			}
			if(roleid){//是admin角色
				//查询所有的菜单
				//List<Map<String, Object>> menu = accountService.selectMenuCodeAll();
				List<Map<String, Object>> menu = null;
				for (Map<String, Object> map : menu) {//把菜单添加到权限集合中去
					menuRouse.add(map.get("ID").toString());
				}
				//查询所有的元素
				//List<Map<String, Object>> res = accountService.selectResCodeAll();
				List<Map<String, Object>> res = null;
				for (Map<String, Object> map : res) {//把元素添加到权限集合中去
					menuRouse.add(map.get("ID").toString());
				}
			}else{//不是admin角色
				//按条件查询菜单
				//List<Map<String, Object>> listMneu = accountService.findMenuCodeByRoleIdStr(str.toString().substring(0,str.toString().length()-1));
				List<Map<String, Object>> listMneu = null;
				for(Map<String, Object> map : listMneu){//把菜单添加到权限集合中去
					menuRouse.add(map.get("ID").toString());
				}
				//按条件查询元素
				//List<Map<String, Object>> listResource = accountService.findResourceCodeByRoleIdStr(str.toString().substring(0,str.toString().length()-1));
				List<Map<String, Object>> listResource = null;
				for(Map<String, Object> map : listResource){//把元素添加到权限集合中去
					menuRouse.add(map.get("ID").toString());
				}
			}
		}
		//创建权限对象
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		//用权限对象设置权限
		info.addStringPermissions(menuRouse);
		//设置角色权限
		info.addRoles(role);
		return info;
	}
	//数据库登录用户验证
	protected SimpleAuthenticationInfo shiroDBCheck(TUserEntity user, String name){
		byte[] salts = PasswordUtil.decodeHex(user.getSalt());
		String password = user.getPassword();
		ShiroUser shiroUser = new ShiroUser(user);
		return new SimpleAuthenticationInfo(shiroUser,password, ByteSource.Util.bytes(salts),name);
	}
	//证书登录用户验证
	protected SimpleAuthenticationInfo shiroCACheck(TUserEntity user,String name,String idCode){
		ShiroUser shiroUser = new ShiroUser(user);
		return new SimpleAuthenticationInfo(shiroUser,idCode,name);
	}
}
