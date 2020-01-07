package com.nsc.Amoski.service.im;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jmessage.api.resource.DownloadResult;
import cn.jmessage.api.resource.UploadResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
public interface JGIMResourceService {

    DownloadResult downloadFile(String mediaId) throws APIConnectionException, APIRequestException;

    UploadResult uploadFileToServer(Integer uid, String type, MultipartFile multipartFile) throws IOException, APIConnectionException, APIRequestException;

}