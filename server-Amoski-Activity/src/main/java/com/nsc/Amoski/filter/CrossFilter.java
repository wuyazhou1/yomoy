/**
 * 
 */
package com.nsc.Amoski.filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jeason
 * @date:2015-8-13 下午03:03:13
 * @version : v1.0
 * 
 */
//@Component
public class CrossFilter implements Filter {
	Logger log= LoggerFactory.getLogger(CrossFilter.class);

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		log.info(">>>>>>>>>>>>>>>>>>>>>activity  filter=====request:"+request.getRequestURL());
		/*response.setHeader("Access-Control-Allow-Origin", "http://192.168.5.155:8444");//源网址
		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE, HEAD");
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "x-auth-token, x-requested-with, Content-Type");
		response.setHeader("Access-Control-Max-Age", "3600");*/
		/*if (!"OPTIONS".equals(request.getMethod())) {
			chain.doFilter(req, res);
		} else {
		}*/


		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}
}
