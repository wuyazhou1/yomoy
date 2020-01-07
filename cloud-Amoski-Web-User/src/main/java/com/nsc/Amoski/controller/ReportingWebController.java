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
public class ReportingWebController   extends BaseController{
    @Autowired
    @Lazy
    public LoginNameCheckedService loginNameCheckedService;
    /**
     * 举报管理页面
     * @return
     */
    @RequestMapping("/AMOSKIACTIVITY/ReportingManager")
    public String ReportingManager(HttpServletRequest request){
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        return "ACTIVITY/ReportingManagement";
    }
}
