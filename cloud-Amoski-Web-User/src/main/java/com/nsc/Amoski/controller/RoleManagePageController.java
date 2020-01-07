package com.nsc.Amoski.controller;

import com.nsc.Amoski.service.LoginNameCheckedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Configuration
public class RoleManagePageController  extends BaseController{

    @Autowired
    @Lazy
    public LoginNameCheckedService loginNameCheckedService;

    @RequestMapping("/AMOSKI/RoleManageList")
    public String RoleManageList(HttpServletRequest request){
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        return "RoleManageList";
    }

    @RequestMapping("/AMOSKI/AddRole")
    public String AddUser(HttpServletRequest request){
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        return "AddRole";
    }

    @RequestMapping("/AMOSKI/RoleAuthorization")
    public String RoleAuthorization(HttpServletRequest request){
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        return "RoleAuthorization";
    }

    @RequestMapping("/AMOSKI/AuthorizationResource")
    public String AuthorizationResource(HttpServletRequest request){
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        return "AuthorizationResource";
    }

    @RequestMapping("/AMOSKI/MemberImpowerRole")
    public String RoleImpowerMember(HttpServletRequest request){
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        return "MemberImpowerRole";
    }

}
