package com.nsc.AmoskiUser.chlientTest;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;





import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OtpLogoutFilter extends LogoutFilter{
	Logger logger = LoggerFactory.getLogger(OtpLogoutFilter.class);
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response)
			throws Exception {
		logger.info("进入OtpLogoutFilter==》preHandle");
		Subject subject = getSubject(request, response);
		logger.info("logout:"+subject.getPrincipal(),LOGTYPE.LOGIN);
		subject.logout();
		return super.preHandle(request, response);
	}

}
