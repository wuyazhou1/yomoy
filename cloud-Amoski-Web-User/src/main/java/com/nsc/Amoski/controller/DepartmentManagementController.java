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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 部门管理页面控制层
 * wusiyao 2019-04-02
 */
@Controller
@Configuration
public class DepartmentManagementController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DepartmentManagementController.class);

    @Autowired
    @Lazy
    public LoginNameCheckedService loginNameCheckedService;

    public void setRequest(HttpServletRequest request){
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String name =enums.nextElement();
            String value = request.getParameter(name);
            if(value!=null || !value.equals(""))
                request.setAttribute(name,value);
        }
    }

    /**
     * 部门管理页面接口
     * @param request
     * @return
     */
    @RequestMapping("/Department/DepartmentManagement")
    public String toList(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        long endTime = System.currentTimeMillis();
        log.info("访问DepartmentManagementController==》toList"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return "DepartmentManagement";
    }
    /**
     * 添加部门
     * @param request
     * @return
     */
    @RequestMapping("/Department/addDepartment")
    public String addDepartment(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        request.getSession().setAttribute("shiro",loginNameCheckedService.findRequestShiro(getIpAddr(request)));
        long endTime = System.currentTimeMillis();
        log.info("访问DepartmentManagementController==》addDepartment"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return "AddDepartment";
    }
}
