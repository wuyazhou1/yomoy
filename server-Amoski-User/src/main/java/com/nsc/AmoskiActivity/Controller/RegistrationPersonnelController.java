package com.nsc.AmoskiActivity.Controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.entity.ActivityCalendarImagesEntity;
import com.nsc.Amoski.entity.EnumEntity;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.util.BaseController;
import com.nsc.AmoskiActivity.Service.RegistrationPersonnelService;
import com.nsc.AmoskiActivity.Util.CreateActivityAnalysisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value="RegistrationPersonnel")
@Api(value="RegistrationPersonnel",description = "报名人员管理")
@Controller
public class RegistrationPersonnelController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationPersonnelController.class);


    @Autowired
    private RegistrationPersonnelService registrationPersonnelService;

    @ResponseBody
    @RequestMapping(value = "RegistrationPersonnelList",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询报名人员列表信息", notes = "查询报名人员列表信息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="addres",value="地区",dataType="string", paramType = "string"),
            @ApiImplicitParam(name="nameTel",value="报名人/手机",dataType="string", paramType = "string"),
    })
    public Object RegistrationPersonnelList(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        //getHeaderParent(request);
        params= pageInfoVue(request);
        params.put("addres",request.getParameter("addres"));
        params.put("nameTel",request.getParameter("nameTel"));
        params.put("basicsId",request.getParameter("basicsId"));
        PagingBean pagingBean = registrationPersonnelService.RegistrationPersonnelList(params);
        return pagingBean;
    }



    @ResponseBody
    @RequestMapping(value = "RegistrationPersonneldetails",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询报名人员详情信息", notes = "查询报名人员详情信息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="人员id",dataType="string", paramType = "string"),
    })
    public Object RegistrationPersonneldetails(HttpServletRequest request){
        List<Map<String, Object>> result = registrationPersonnelService.RegistrationPersonnelDetails(request.getParameter("id"));
        return result;

    }

    /*//临时文件目录
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = System.getProperty("user.dir") + "/data/tmp";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }*/





    @ResponseBody
    @RequestMapping(value = "SaveActivitySignUp",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="保存人员信息", notes = "保存人员信息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="SignUp",value="人员信息json数据",dataType="string", paramType = "update"),
    })
    public Object SaveActivitySignUp(HttpServletRequest request, HttpServletResponse response){
        try {
            String SignUp = request.getParameter("SignUp");
            log.info(SignUp);
            JSONArray SignUpJson = JSON.parseArray(SignUp);
            registrationPersonnelService.SaveActivitySignUp(SignUpJson);
            //calendarImagesService.updateCalendarImages(list);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }




    public static void main(String[] args){
        System.out.println(System.getProperty("user.dir"));
    }
}
