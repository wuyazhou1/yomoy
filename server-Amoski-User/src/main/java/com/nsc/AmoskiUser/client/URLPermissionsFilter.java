package com.nsc.AmoskiUser.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class URLPermissionsFilter extends PermissionsAuthorizationFilter{
	private static Logger logger = LoggerFactory.getLogger(URLPermissionsFilter.class);



	@Override
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
		logger.info("URLPermissionsFilter==>isAccessAllowed");
		String curUrl = getRequestUrl(request);
		Subject subject = SecurityUtils.getSubject();
		if(subject.getPrincipal() == null 
				|| StringUtils.endsWithAny(curUrl, ".js",".css",".html",".jsp")
				|| StringUtils.endsWithAny(curUrl, ".jpg",".png",".gif", ".jpeg")
				|| StringUtils.equals(curUrl, "/unauthor")) {
			return true;
		}
		logger.info(subject.getPrincipal().toString());
		return true;
		//List<String> urls = userService.findPermissionUrl(subject.getPrincipal().toString());
		
		//return urls.contains(curUrl);
	}
	


	private String getRequestUrl(ServletRequest request) {
		logger.info("URLPermissionsFilter==>getRequestUrl");
		HttpServletRequest req = (HttpServletRequest)request;
		String queryString = req.getQueryString();

		queryString = StringUtils.isBlank(queryString)?"": "?"+queryString;
		return req.getRequestURI()+queryString;
	}
}
