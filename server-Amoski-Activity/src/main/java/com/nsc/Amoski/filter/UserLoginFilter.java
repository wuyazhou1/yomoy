/**
 * 
 */
package com.nsc.Amoski.filter;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.service.MemberService;
import com.nsc.Amoski.uti.WeChatUtil;
import com.nsc.Amoski.util.GolConstant;
import com.nsc.Amoski.util.GsonUtil;
import com.nsc.Amoski.util.RegUtil;
import com.nsc.Amoski.util.StringUtils;
import lombok.experimental.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

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
			if(requestUrl.indexOf("/getImg")>0||requestUrl.indexOf("/activityImgDown")>0){
				appToken=request.getParameter("appToken");
				log.info(">>>>>>>>>>>getParameter  appToken:"+appToken+">>>>>>>>>>...");
				if(!regUtil.isNull(appToken)){
					if("activeImg".equals(appToken)){
						return true;
					}else{
						sessionId=appToken;
					}
				}
			}
		}
		log.info(">>>>>>>>>>....appToken"+appToken+">>>>>>>>>>>>>");
		String reidsKey=REDIS_USERINFO_KEY+"_"+sessionId;
		/*Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()){//请求头部打印
			String hName=headerNames.nextElement();
			String hValue=request.getHeader(hName);
			log.info(">>>>>>>>>>>>>>>>.headValue >>>>>>> headName:"+hName+"=====hValue:"+hValue);
		}*/
		MemberView dto=memberService.getRedisUserObj(reidsKey,null);
		log.info("inteceptor==============userObj:"+dto);
		/*String redisSingValue=REDIS_LOGIN_TYPE+"_"+sessionId;
		Object obj =memberService.getSingValue(redisSingValue,null);*/
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String ua=request.getHeader("User-Agent").toLowerCase();//判断浏览器类型
		/*if(RegUtil.isWechat(ua)&&requestUrl.indexOf("/memberUser")==-1&&dto==null){//微信浏览器  则调微信接口
			String type=request.getParameter("type");
			String responseUrl=GolConstant.BASE_URL+"AmoskiActivity/memberUser/getUserAuthInfo";
			if("2".equals(type)){//微信信息绑定  根据一个参数区分
				responseUrl=GolConstant.BASE_URL+"AmoskiActivity/userCenterManage/userWechatBind";
			}
			String returnStr= WeChatUtil.authorWebPage(responseUrl);
			response.sendRedirect(returnStr);
			//request.getRequestDispatcher("/AmoskiActivity/memberUser/loginOAuth?type=1").forward(request,response);
			return false;
		}else{*/
			if(dto==null && !StringUtils.isTextMember ) {//未登录
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
//			}
		}
		return true;
	}
}
