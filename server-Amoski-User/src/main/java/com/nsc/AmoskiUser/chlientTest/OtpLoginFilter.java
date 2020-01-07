package com.nsc.AmoskiUser.chlientTest;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





import org.apache.shiro.SecurityUtils;
//import net.sinodata.opt.common.vo.ShiroUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OtpLoginFilter extends FormAuthenticationFilter{
	
	Logger logger = LoggerFactory.getLogger(OtpLoginFilter.class);
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,
			Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {
		logger.info("进入OtpLoginFilter==》onLoginSuccess");
		logger.info("login success " + getUsername(request),LOGTYPE.LOGIN);
		
		String url = this.getSuccessUrl();
		HttpServletRequest hreq = (HttpServletRequest)request;
		HttpServletResponse hresp= (HttpServletResponse)response;
		hresp.sendRedirect(hreq.getContextPath()+url);
		return false;
	}
	
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		logger.info("进入OtpLoginFilter==》onLoginFailure");
		String loginError="登录失败，请核实用户和密码";
		Object caError= SecurityUtils.getSubject().getSession().getAttribute("LOGIN_CA_ERROR_MSG");
		if(caError!=null){
			loginError=caError.toString();
		}
		request.setAttribute("LOGIN_ERROR_MSG", loginError);
		logger.info("login fail:" + getUsername(request),LOGTYPE.LOGIN);
		return super.onLoginFailure(token, e, request, response);
	}
}
