package com.nsc.AmoskiUser.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Controller
@Configuration
public class IndexJspController {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
    /**
     * 跳转到订餐页面
     * @param request
     * @return
     */
    @RequestMapping("/indexjsp")
    public String toList(HttpServletRequest request){
        return "indexjsp";
    }
    @RequestMapping(value = "/index" ,method = RequestMethod.GET)
    public String returnIndex(){
        return "indexjsp";
    }

    @GetMapping("/order")
    @ResponseBody
    public String router() {
        RestTemplate tpl = getRestTemplate();
        String result = tpl.getForObject("indexjsp", String.class);
        return result;
    }


}
