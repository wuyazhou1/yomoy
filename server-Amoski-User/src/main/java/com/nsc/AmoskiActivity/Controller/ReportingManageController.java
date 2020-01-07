package com.nsc.AmoskiActivity.Controller;


import com.nsc.Amoski.entity.EnumEntity;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.util.BaseController;
import com.nsc.AmoskiActivity.Service.ReportingManageService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 举报管理
 */
@RequestMapping(value="ReportingManage")
@Controller
public class ReportingManageController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(ReportingManageController.class);

    @Autowired
    @Lazy
    public ReportingManageService reportingManageService;


    @ResponseBody
    @RequestMapping(value = "ReportingList",method = RequestMethod.POST)
    @ApiOperation(value="举报管理List数据接口", notes = "举报管理List数据接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="suggestionId",value="举报管理类型",dataType="string", paramType = "insert"),
    })
    public PagingBean ReportingList(HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<String, Object>();
        params= pageInfo(request);
        params.put("suggestionId",request.getParameter("suggestionId"));
        PagingBean pagingBean = reportingManageService.ReportingList(params);
        long endTime = System.currentTimeMillis();
        log.info("访问DictController==》DictList"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return pagingBean;
    }

}
