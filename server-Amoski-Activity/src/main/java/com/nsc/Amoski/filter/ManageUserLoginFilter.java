/**
 * 
 */
package com.nsc.Amoski.filter;

import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.service.MemberService;
import com.nsc.Amoski.uti.WeChatUtil;
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
public class ManageUserLoginFilter extends HandlerInterceptorAdapter implements GolConstant {
	Logger log= LoggerFactory.getLogger(ManageUserLoginFilter.class);

	@Autowired
	MemberService memberService;

	@Override
	public boolean preHandle(HttpServletRequest request,	
			HttpServletResponse response, Object handler) throws Exception {
		String requestUrl=request.getRequestURL().toString();
		RegUtil regUtil=RegUtil.getSingleton();
		log.info(">>>>>>>>>>>>>>>>>>>>进入ManageUserLogin拦截~~~~~~~~request User   "+requestUrl);
		String sessionId=request.getSession().getId();
		String reidsKey=REDIS_USERINFO_KEY+"_"+sessionId;
		ShiroUser dto=memberService.putIpAddrShiroUser(regUtil.getIpAddr(request));
		log.info("inteceptor==============userObj:"+dto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		/*if(dto==null) {//未登录
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
			}*/
		return true;
	}
}
