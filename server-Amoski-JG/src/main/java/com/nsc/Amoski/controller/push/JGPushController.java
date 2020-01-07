package com.nsc.Amoski.controller.push;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.device.TagListResult;
import cn.jpush.api.push.CIDResult;
import cn.jpush.api.push.PushResult;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.jg.ReturnCode;
import com.nsc.Amoski.service.push.JGPushService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 李阳
 * @date 2019/12/12 11:34
 */
@RestController
@RequestMapping(path = "/push")
@Api(value = "JGPushController", tags = "极光PUSH接口")
public class JGPushController {

    @Autowired
    private JGPushService jgPushService;

    /**
     * 根据类型获取唯一标识符列表
     *
     * @param count
     * @param type
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "根据类型获取唯一标识符列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "count", value = "数量", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "类型(push或schedule)", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(path = "/getCidList/{count}/{type}")
    public Result getCidList(@PathVariable int count, @PathVariable String type) throws APIConnectionException, APIRequestException {
        CIDResult cidResult = jgPushService.getCidList(count < 1 ? 1 : count, type);
        return new Result(ReturnCode.OK, cidResult);
    }

    /**
     * 获取标签列表
     *
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "获取标签列表", httpMethod = "GET")
    @GetMapping(path = "/getTagList")
    public Result getTagList() throws APIConnectionException, APIRequestException {
        TagListResult tagListResult = jgPushService.getTagList();
        return new Result(ReturnCode.OK, tagListResult);
    }

    /**
     * 推送通知到所有平台
     *
     * @param text
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "推送通知到所有平台", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "text", value = "内容", required = true, dataType = "string", paramType = "query")
    })
    @PostMapping(path = "/sendPushToAll")
    public Result sendPushToAll(@RequestParam String text) throws APIConnectionException, APIRequestException {
        PushResult pushResult = jgPushService.sendPushToAll(text);
        return new Result(ReturnCode.OK, pushResult);
    }

}
