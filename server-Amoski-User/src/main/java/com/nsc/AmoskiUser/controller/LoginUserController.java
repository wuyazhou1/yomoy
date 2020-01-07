package com.nsc.AmoskiUser.controller;


import com.nsc.AmoskiUser.client.UsernamePasswordTokenSerializable;
import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TUserEntity;
import com.nsc.AmoskiUser.service.LoginUserService;
import com.nsc.AmoskiUser.service.UserRealmService;
import com.nsc.Amoski.util.BaseController;
import com.nsc.Amoski.util.PasswordUtil;
import com.nsc.Amoski.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Controller
@RequestMapping(value = "AMOSKI/LoginUserCheck.html")
public class LoginUserController extends BaseController {
    Logger logger = LoggerFactory.getLogger(LoginUserController.class);
    @Autowired
    @Lazy
    private LoginUserService loginUserService;
    @Autowired
    @Lazy
    private UserRealmService userRealmService;




    @RequestMapping
    @ResponseBody
    public Object LoginUserCheck(String loginName , String password  , HttpServletRequest request, Model model){
        long starTime = System.currentTimeMillis();
        TUserEntity user = loginUserService.findUserByLoginName(loginName);
        password = PasswordUtil.setPassWordBySalt(password, user.getSalt());
        String path=request.getHeader("x-forwarded-host");
        String ipAddr = getIpAddr(request);
        StringUtils.servletContext = request.getServletContext();
        if(password.equals(user.getPassword())){
            Map<String, Object> resultMap = loginUserService.putFindRequestShiro(ipAddr, user);
            Subject subject = SecurityUtils.getSubject();
            ShiroUser shiroUser = (ShiroUser) resultMap.get("ShiroUser");
            request.getSession().setAttribute("loginName",loginName);
            request.getSession().setAttribute("password",shiroUser.getPassword());
            List<Map<String, Object>> menuFrameListMap = loginUserService.findMenuFrameListMap();
            String code = shiroUser.getCode();
            AuthorizationInfo authorizationInfo = userRealmService.putDoGetAuthorizationInfo(code);


            UsernamePasswordTokenSerializable token = new UsernamePasswordTokenSerializable(ipAddr,shiroUser.getPassword());
            loginUserService.getShiroRedisObject(ipAddr,token);
            subject.login(token);
            logger.info("认证结果"+subject.isAuthenticated());
            ShiroUser shiroUser1 = (ShiroUser)subject.getPrincipal();
            subject.isPermitted("admin");
            long endTime = System.currentTimeMillis();
            logger.info("访问LoginUserController==》LoginUserCheck"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
            return "/AmoskiWebUser/AMOSKI/XiTongMenu?ipAddr="+ipAddr;
        }else{
            long endTime = System.currentTimeMillis();
            logger.info("访问LoginUserController==》LoginUserCheck"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
            return "/AmoskiWebUser/AMOSKI/loginNameUser";
        }
    }
}
