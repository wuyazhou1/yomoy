package com.nsc.Amoski.controller;


import com.nsc.Amoski.service.LoginNameCheckedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Configuration
public class DictPageController  extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DepartmentManagementController.class);
    @Autowired
    @Lazy
    public LoginNameCheckedService loginNameCheckedService;

    @RequestMapping("/AMOSKI/DictList")
    public String DictList(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        long endTime = System.currentTimeMillis();
        log.info("访问DictPageController==》DictList"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return "DictList";
    }

    @RequestMapping("/AMOSKI/AddDict")
    public String AddDict(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        long endTime = System.currentTimeMillis();
        log.info("访问DictPageController==》AddDict"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return "AddDict";
    }
}
