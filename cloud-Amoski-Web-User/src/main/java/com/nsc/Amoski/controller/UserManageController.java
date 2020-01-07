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
public class UserManageController  extends BaseController{

    @Autowired
    @Lazy
    public LoginNameCheckedService loginNameCheckedService;

    @RequestMapping("/AMOSKI/UserManageList")
    public String returnIndex(HttpServletRequest request){
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        return "UserManageList";
    }

    @RequestMapping("/AMOSKI/AddUser")
    public String AddUser(HttpServletRequest request){
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        return "AddUser";
    }

    @RequestMapping("/AMOSKI/UserImppowerRole")
    public String UserImppowerRole(HttpServletRequest request){
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        return "UserImppowerRole";
    }
}
