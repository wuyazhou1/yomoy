package com.nsc.AmoskiActivity.Controller;


import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Configuration
public class ActivityController {
    @RequestMapping("/AMOSKIACTIVITY/amoskiwebuserAccomManage")
    public String amoskiwebuserAccomManage(HttpServletRequest request){
        return "amoskiwebuser/accomManage/index.html";
    }

}
