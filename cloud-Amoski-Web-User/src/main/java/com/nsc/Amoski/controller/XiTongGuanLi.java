package com.nsc.Amoski.controller;


import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.service.LoginNameCheckedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static javax.swing.UIManager.get;

@Controller
@Configuration
public class XiTongGuanLi  extends BaseController{

    @Autowired
    @Lazy
    public LoginNameCheckedService loginNameCheckedService;

    @RequestMapping("/AMOSKI/XiTongMenu")
    public String XiTongMenu(HttpServletRequest request){
        String ipAddr = request.getParameter("ipAddr");
        Map<String,Object> requestShiro = loginNameCheckedService.findRequestShiro(ipAddr);
        ShiroUser shiroUser= (ShiroUser)requestShiro.get("ShiroUser");
        if(shiroUser==null){
            return "forward:http://"+ipAddr+"/AmoskiWebUser/AMOSKI/loginNameUse//r";//转发
        }
        request.getSession().setAttribute("shiro",requestShiro);
        //获取菜单
        List<Map<String, Object>> parentMap = loginNameCheckedService.findMenuFrameListMap();
        //获取菜单页面数据
        List<Map<String, Object>> resultListMap = loginNameCheckedService.findMenuByMenuFrameListMap(parentMap);
        /*for(Map<String, Object> map : resultListMap){
            System.out.println(map.get("start_menu"));
            if(map.get("next_menu")!=null)
                for(Map<String, Object> map1 : (List<Map<String, Object>>)map.get("next_menu")){
                    System.out.println("\t"+map1.get("start_menu"));
                    if(map1.get("next_menu")!=null)
                        for(Map<String, Object> map2 : (List<Map<String, Object>>)map1.get("next_menu")){
                            System.out.println("\t\t"+map2.get("start_menu"));
                            System.out.print("\t\t"+map2.get("end_menu"));
                        }
                    System.out.print("\t"+map1.get("end_menu"));
                }
            System.out.println(map.get("end_menu"));
        }*/


        /*UsernamePasswordTokenSerializable token = new UsernamePasswordTokenSerializable(ipAddr,shiroUser.getPassword());
        //UsernamePasswordTokenSerializable token = (UsernamePasswordTokenSerializable)loginNameCheckedService.getShiroRedisObject(ipAddr);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        subject.isPermitted("admin");*/
        request.setAttribute("MENULIST",resultListMap);
        return "XiTongGuanLi";
    }

}
