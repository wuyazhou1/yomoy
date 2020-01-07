/**
 * 
 */
package com.nsc.Amoski.interceptor;

import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.service.MemberService;
import com.nsc.Amoski.util.GolConstant;
import com.nsc.Amoski.util.GsonUtil;
import com.nsc.Amoski.util.RegUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jeason
 * @date:2015-8-13 下午03:03:13
 * @version : v1.0
 * 
 */
public class UserLoginFilter extends HandlerInterceptorAdapter implements GolConstant {
	Logger log= LoggerFactory.getLogger(UserLoginFilter.class);

	@Autowired
	MemberService memberService;

	@Override
	public boolean preHandle(HttpServletRequest request,	
			HttpServletResponse response, Object handler) throws Exception {
		String requestUrl=request.getRequestURL().toString();
		RegUtil regUtil=RegUtil.getSingleton();
		log.info(">>>>>>>>>>>>>>>>>>>>进入UserLogin拦截~~~~~~~~request User   "+requestUrl);
		String sessionId=request.getSession().getId();
		String appToken = request.getHeader("appToken");
		if(!regUtil.isNull(appToken)){
			sessionId=appToken;
		}else{
			appToken=request.getParameter("appToken");
			log.info(">>>>>>>>>>>getParameter  appToken:"+appToken+">>>>>>>>>>...");
			if(!regUtil.isNull(appToken)){
				sessionId=appToken;
			}
			/*if(requestUrl.indexOf("/getImg")>0){
				log.info(">>>>>>>>>>>getParameter  appToken:"+appToken+">>>>>>>>>>...");
				if(!regUtil.isNull(appToken)){
					sessionId=appToken;
				}
			}*/
		}
		log.info(">>>>>>>>>>....appToken"+appToken+">>>>>>>>>>>>>");
		String reidsKey=REDIS_USERINFO_KEY+"_"+sessionId;
		MemberView dto=memberService.getRedisUserObj(reidsKey,null);
		log.info("inteceptor==============userObj:"+dto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(dto==null) {//未登录
			Result rs=new Result();
			PrintWriter out = null;
			response.setContentType("text/html;charset=utf-8");
			rs.setCode(ResultMsg.LONGTIMEOUT.getCode());
			rs.setMsg(ResultMsg.LONGTIMEOUT.getMessage());
			String json= GsonUtil.dtoToJson(rs);
			log.info("Response data is "+ json);
			try {
				out = response.getWriter();
				out.write(json);
				return false;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.close();
					return false;
				}
			}
		}
		return true;
	}
}
