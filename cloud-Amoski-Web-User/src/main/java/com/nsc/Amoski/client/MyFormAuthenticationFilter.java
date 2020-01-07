package com.nsc.Amoski.client;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
//import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {
    private Logger logger = LoggerFactory.getLogger(MyFormAuthenticationFilter.class);
    public static List<String> list = new ArrayList(){{
        this.add("/images/loginNameImage.jpg");//用户登入界面背景图
        this.add("/AMOSKI/loginNameUser");//用户登入界面
        this.add("/AMOSKI/LoginUserCheck.html");//用户登入接口
        this.add("/MemberWeiXinManage/WeiXinRegister");//微信注册接口
        this.add("/MemberWeiXinManage/MemberLogin");//会员登入
        this.add("/MemberWeiXinManage/checkedLoginRepeat");//验证手机号码或账号是否重复
        this.add("/MemberWeiXinManage/TelRegister");//手机号注册
        this.add("/MemberWeiXinManage/updateMemberView");//修改会员信息
        this.add("/MemberWeiXinManage/findMemberView");//获取会员信息

    }};
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        logger.info("进入MyFormAuthenticationFilter==》onLoginSuccess");
        String successUrl = "/home";
        WebUtils.issueRedirect(request,response,successUrl);
        return false;
    }
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        logger.info("进入MyFormAuthenticationFilter==》onAccessDenied");
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String meth = req.getRequestURI();
        if(list.contains(meth)){
            return true;
        }
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                if (logger.isTraceEnabled()) {
                    logger.info("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(request, response);
            } else {
                if (logger.isTraceEnabled()) {
                    logger.info("Login page view.");
                }
                //allow them to see the login page ;)
                return true;
            }
        } else {
            if(req.getMethod().equals(RequestMethod.OPTIONS.name())) {
                resp.setStatus(HttpStatus.OK.value());
                return true;
            }

            if (logger.isTraceEnabled()) {
                logger.info("Attempting to access a path which requires authentication.  Forwarding to the " +
                        "Authentication url [" + getLoginUrl() + "]");
            }
            //前端Ajax请求时requestHeader里面带一些参数，用于判断是否是前端的请求
            String test= req.getHeader("test");
            /*if (test!= null || req.getHeader("wkcheck") != null) {
                //前端Ajax请求，则不会重定向
                resp.setHeader("Access-Control-Allow-Origin",  req.getHeader("Origin"));
                resp.setHeader("Access-Control-Allow-Credentials", "true");
                resp.setContentType("application/json; charset=utf-8");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter out = resp.getWriter();
                JSONObject result = new JSONObject();
                result.put("message", "登录失效");
                result.put("resultCode", 1000);
                out.println(result);
                out.flush();
                out.close();
            } else {
                saveRequestAndRedirectToLogin(request, response);
            }*/
            return false;
        }
    }

}
