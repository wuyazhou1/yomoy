package com.nsc.AmoskiUser.controller;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="ResourceManage")
@Api(value="ResourceManage",description = "元素管理模块")
@Controller
public class ReportingController {

    @Autowired
    @Lazy
    private com.nsc.AmoskiUser.service.ReportingService ReportingService;


}
