package com.nsc.Amoski.controller.im;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.sensitiveword.SensitiveWordListResult;
import cn.jmessage.api.sensitiveword.SensitiveWordStatusResult;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.jg.ReturnCode;
import com.nsc.Amoski.service.im.JGIMSensitivewordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
@RestController
@RequestMapping(path = "/imSensitiveword")
@Api(value = "JGIMSensitivewordController", tags = "极光IM敏感词接口")
public class JGIMSensitivewordController {

    @Autowired
    private JGIMSensitivewordService jgimSensitivewordService;

    /**
     * 添加敏感词
     *
     * @param words
     */
    @ApiOperation(value = "添加敏感词", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "words", value = "敏感词(可添加多个)", required = true, dataType = "stirng", paramType = "query")
    })
    @PostMapping(path = "/addSensitiveWords")
    public Result addSensitiveWords(@RequestParam String[] words) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jgimSensitivewordService.addSensitiveWords(words);
        return new Result(ReturnCode.OK, responseWrapper);
    }

    /**
     * 删除敏感词
     *
     * @param word
     */
    @ApiOperation(value = "删除敏感词", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "word", value = "敏感词", required = true, dataType = "string", paramType = "query")
    })
    @DeleteMapping(path = "/deleteSensitiveWord")
    public Result deleteSensitiveWord(@RequestParam String word) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jgimSensitivewordService.deleteSensitiveWord(word);
        return new Result(ReturnCode.OK, responseWrapper);
    }

    /**
     * 修改敏感词
     *
     * @param oldWord
     * @param newWord
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "修改敏感词", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldWord", value = "敏感词", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "newWord", value = "新敏感词", required = true, dataType = "string", paramType = "query")
    })
    @PutMapping(path = "/updateSensitiveWord")
    public Result updateSensitiveWord(@RequestParam String oldWord, @RequestParam String newWord) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jgimSensitivewordService.updateSensitiveWord(newWord, oldWord);
        return new Result(ReturnCode.OK, responseWrapper);
    }

    /**
     * 获取敏感词列表
     *
     * @param start
     * @param count
     * @return
     */
    @ApiOperation(value = "获取敏感词列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "起始行(从0开始)", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "count", value = "条数(从0开始)", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping(path = "/getSensitiveWordList/{start}/{count}")
    public Result getSensitiveWordList(@PathVariable int start, @PathVariable int count) throws APIConnectionException, APIRequestException {
        SensitiveWordListResult sensitiveWordListResult = jgimSensitivewordService.getSensitiveWordList(start < 0 ? 0 : start, count < 1 ? 0 : count);
        return new Result(ReturnCode.OK, sensitiveWordListResult);
    }

    /**
     * 更新敏感词状态
     *
     * @param status
     */
    @ApiOperation(value = "更新敏感词状态", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态(0关闭,1开启)", required = true, dataType = "int", paramType = "path")
    })
    @PutMapping(path = "/updateSensitiveWordStatus/{status}")
    public Result updateSensitiveWordStatus(@PathVariable int status) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jgimSensitivewordService.updateSensitiveWordStatus(status < 1 ? 0 : 1);
        return new Result(ReturnCode.OK, responseWrapper);
    }

    /**
     * 获取敏感词状态
     */
    @ApiOperation(value = "获取敏感词状态", httpMethod = "GET")
    @GetMapping(path = "/getSensitiveWordStatus")
    public Result getSensitiveWordStatus() throws APIConnectionException, APIRequestException {
        SensitiveWordStatusResult sensitiveWordStatusResult = jgimSensitivewordService.getSensitiveWordStatus();
        return new Result(ReturnCode.OK, sensitiveWordStatusResult);
    }

}