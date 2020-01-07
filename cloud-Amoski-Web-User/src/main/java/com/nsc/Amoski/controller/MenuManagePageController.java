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
public class MenuManagePageController  extends BaseController{

    @Autowired
    @Lazy
    public LoginNameCheckedService loginNameCheckedService;

    /**
     * 菜单列表
     * @return
     */
    @RequestMapping("/AMOSKI/MenuManageList")
    public String RoleManageList(HttpServletRequest request){
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        return "MenuManageList";
    }

    /**
     * 添加菜单，修改菜单
     * @return
     */
    @RequestMapping("/AMOSKI/AddMenuManage")
    public String AddMenuManage(HttpServletRequest request){
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        return "AddMenuManage";
    }


    /**
     * 元素列表
     * @return
     */
    @RequestMapping("/AMOSKI/ResourceManageList")
    public String ResourceManageList(HttpServletRequest request){
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        return "ResourceManageList";
    }

    /**
     * 添加元素，修改元素
     * @return
     */
    @RequestMapping("/AMOSKI/AddResourceManage")
    public String AddResourceManage(HttpServletRequest request){
        Object requestShiro = loginNameCheckedService.findRequestShiro(getIpAddr(request));
        request.getSession().setAttribute("shiro",requestShiro);
        return "AddResourceManage";
    }


}
