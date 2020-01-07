package com.nsc.AmoskiUser.chlientTest;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class OtpAccessFilter extends UserFilter {
	Logger logger = LoggerFactory.getLogger(OtpAccessFilter.class);

	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		if (isLoginRequest(request, response)) {
			return true;
		} else {
			Subject subject = getSubject(request, response);
			// If principal is not null, then the user is known and should be allowed access.
			return subject.getPrincipal() != null;
		}
	}


	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		logger.info("进入MyFormAuthenticationFilter==》onAccessDenied");
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String meth = req.getRequestURI();
		boolean b = super.onAccessDenied(request, WebUtils.toHttp(response));
		if(!b){
			String ipAddr = getIpAddr(req);
			resp.setHeader("shiro",  "/AmoskiWebUser/AMOSKI/loginNameUser");
			//resp.sendRedirect("http://"+ipAddr+"/AmoskiWebUser/AMOSKI/loginNameUser");
			return b;
		}
		logger.info("进入OtpAccessFilter==》onAccessDenied");
		return b;
	}

	/*@Override
	public void doFilterInternal(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws ServletException, IOException {
		try {
			//logger.info(arg0.getServerName());
			//logger.info(arg0.getServerPort()+"");
			//logger.info(arg0.getServletContext().getContextPath()+"");
			String contextPath=arg0.getServletContext().getContextPath();
			//首先判断当前登录的用户是否为证书登录
			ShiroUser suser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			*//*if (suser != null && suser.getIdCode() != null) {//存在身份证号，则为证书登录
				HttpServletResponse response=(HttpServletResponse) arg1;
				Subject subject = SecurityUtils.getSubject();
				//读取证书信息，如果证书信息读取不了，则证书已经拔出，此时退出登录
				X509Certificate[] certs = (X509Certificate[]) arg0.getAttribute("javax.servlet.request.X509Certificate");
				if (certs != null) {//证书存在，判断当前证书与已登录证书是否一致，如果不一致，则退出登录
					X509Certificate gaX509Cert = null;
					gaX509Cert = certs[0];
					byte[] b = gaX509Cert.getEncoded();
					CertificateFactory cf = CertificateFactory.getInstance("x509");
					ByteArrayInputStream bin = new ByteArrayInputStream(b);
					gaX509Cert = (X509Certificate) cf.generateCertificate(bin);
					GACertParser gcp = new GACertParser(gaX509Cert);
					String sdn = gcp.getParseredDN();
					String adn[] = sdn.split(",");
					String idCode = adn[1];
					if (!suser.getIdCode().equals(idCode)) {
						//证书存在，但不一致，退出登录
						subject.logout();
						response.sendRedirect("http://10.6.121.91:8080"+contextPath);
					}
				} else {//证书不存在，退出登录
					subject.logout();
					response.sendRedirect("http://10.6.121.91:8080"+contextPath);
				}
			}*//*
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.doFilterInternal(arg0, arg1, arg2);
	}*/


	/**
	   * 获取访问者IP地址
	   */
	public String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
}
