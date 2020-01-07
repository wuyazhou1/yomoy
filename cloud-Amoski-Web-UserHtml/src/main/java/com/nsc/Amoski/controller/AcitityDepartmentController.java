package com.nsc.Amoski.controller;

import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.jg.ReturnCode;
import com.nsc.Amoski.service.AcitityDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 李阳
 * @date 2019/12/25 17:28
 */
@RestController
@RequestMapping(path = "/acitityDepartment")
@Api(value = "AcitityDepartmentController", description = "活动俱乐部接口")
public class AcitityDepartmentController {

    @Autowired
    private AcitityDepartmentService acitityDepartmentService;

    /**
     * 根据俱乐部名称模糊查询
     *
     * @param orgName
     * @return
     */
    @ApiOperation(value = "模糊查询俱乐部", notes = "通过orgName模糊查询俱乐部", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgName", value = "俱乐部关键字", required = true, dataType = "string", paramType = "query")
    })
    @RequestMapping(path = "/findByOrgName", method = RequestMethod.GET)
    public Result findByOrgName(@RequestParam(value = "orgName") String orgName) {
        List<Map<String, Object>> byOrgName = acitityDepartmentService.findByOrgName(orgName);
        return new Result(ReturnCode.OK, byOrgName);
    }

}
