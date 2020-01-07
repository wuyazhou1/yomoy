package com.nsc.Amoski.controller.im;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jmessage.api.resource.DownloadResult;
import cn.jmessage.api.resource.UploadResult;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.jg.ReturnCode;
import com.nsc.Amoski.service.im.JGIMResourceService;
import com.nsc.Amoski.utlis.JGUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
@RestController
@RequestMapping(path = "/imResource")
@Api(value = "JGIMResourceController", tags = "极光IM媒体文件下载与上传接口")
public class JGIMResourceController {

    @Autowired
    private JGIMResourceService jgimResourceService;

    /**
     * 文件下载
     *
     * @param mediaId
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "文件下载", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mediaId", value = "媒体id", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping(path = "/downloadFile")
    public Result downloadFile(@RequestParam String mediaId) throws APIConnectionException, APIRequestException {
        DownloadResult downloadResult = jgimResourceService.downloadFile(mediaId);
        return new Result(ReturnCode.OK, downloadResult);
    }

    /**
     * 文件上传的服务器
     *
     * @param member_id
     * @param type
     * @param file
     * @return
     * @throws IOException
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "文件上传的服务器", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "member_id", value = "用户表主键", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "类型=image(图片),voice(语音),file(其它文件)", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "file", paramType = "query")
    })
    @PostMapping(path = "/uploadFileToServer/{member_id}/{type}")
    public Result uploadFileToServer(@PathVariable Integer member_id, @PathVariable String type, @RequestParam MultipartFile file) throws IOException, APIConnectionException, APIRequestException {
        if (!(JGUtil.IM_FILE_TYPE_IMAGE.equals(type))) return new Result(ReturnCode.IM_FILE_TYPE_NO);
        /*if (!(JGUtil.IM_FILE_TYPE_IMAGE.equals(type) || JGUtil.IM_FILE_TYPE_VOICE.equals(type) || JGUtil.IM_FILE_TYPE_FILE.equals(type)))
            return new Result(ReturnCode.IM_FILE_TYPE_NO);*/
        UploadResult uploadResult = jgimResourceService.uploadFileToServer(member_id, type, file);
        return new Result(ReturnCode.OK, uploadResult);
    }

}