package com.nsc.Amoski.controller;


import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Configuration
public class LoginUserPageController extends BaseController{
    @RequestMapping("/AMOSKI/loginNameUser")
    public String loginNameUser(HttpServletRequest request){
        return "loginNameUser";
    }

    @RequestMapping("/AMOSKI/math")
    public String math(HttpServletRequest request){
        return "math";
    }
/*
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        //getHeaderParent(request);
        String path=request.getHeader("referer").split("/")[2];
        String ipAddr = getIpAddr(request);
        return "redirect:http://"+path+"/AmoskiWebUser/AMOSKI/XiTongMenu?ipAddr="+ipAddr;//重定向
    }*/
}
