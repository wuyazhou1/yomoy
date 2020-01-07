package com.nsc.Amoski.service.im.impl;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.resource.DownloadResult;
import cn.jmessage.api.resource.UploadResult;
import com.nsc.Amoski.dao.im.JGIMResourceDao;
import com.nsc.Amoski.entity.IdWorker;
import com.nsc.Amoski.entity.TJGIMResourceEntity;
import com.nsc.Amoski.service.im.JGIMResourceService;
import com.nsc.Amoski.utlis.JGUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
@Service
public class JGIMResourceServiceImpl implements JGIMResourceService {

    @Autowired
    private JMessageClient jMessageClient;
    //@Value("${fileUploadDirectory}")
    private String fileUploadDirectory = "D:/uploadfile/jg/";
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private JGIMResourceDao jgimResourceDao;

    /**
     * 文件下载
     *
     * @param mediaId
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public DownloadResult downloadFile(String mediaId) throws APIConnectionException, APIRequestException {
        DownloadResult downloadResult = jMessageClient.downloadFile(mediaId);
        return downloadResult;
    }

    /**
     * 文件上传到服务器
     *
     * @param uid
     * @param type
     * @param multipartFile
     * @return
     * @throws IOException
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public UploadResult uploadFileToServer(Integer uid, String type, MultipartFile multipartFile) throws IOException, APIConnectionException, APIRequestException {
        String id = idWorker.nextId() + "";
        String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        String path = null;
        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR) + "";
        String mouth = calendar.get(Calendar.MONTH) < 10 ? "0" + calendar.get(Calendar.MONTH) : calendar.get(Calendar.MONTH) + "";
        String date = calendar.get(Calendar.DATE) < 10 ? "0" + calendar.get(Calendar.DATE) : calendar.get(Calendar.DATE) + "";
        String hour = calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : calendar.get(Calendar.HOUR_OF_DAY) + "";
        if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
            path = "D:/home/uploadFile/jg/" + type + "/" + year + "/" + mouth + "/" + date + "/" + hour + "/";
            File file = new File(path);
            if (!file.exists()) file.mkdirs();
        } else {
            path = "/home/uploadFile/jg/" + type + "/" + year + "/" + mouth + "/" + date + "/" + hour + "/";
            File file = new File(path);
            if (!file.exists()) file.mkdirs();
        }
        File saveFile = new File(path + id + suffix);
        multipartFile.transferTo(saveFile);
        UploadResult uploadResult = jMessageClient.uploadFile(saveFile.getAbsolutePath(), type);
        TJGIMResourceEntity tjgimResourceEntity = new TJGIMResourceEntity();
        tjgimResourceEntity.setId(id);
        tjgimResourceEntity.setMember_id(uid);
        tjgimResourceEntity.setData_type(type);
        if (type.equals(JGUtil.IM_FILE_TYPE_IMAGE)) {
            tjgimResourceEntity.setMedia_id(uploadResult.getMediaId());
            tjgimResourceEntity.setMedia_crc32(uploadResult.getMediaCrc32());
            tjgimResourceEntity.setWidth(uploadResult.getWidth());
            tjgimResourceEntity.setHeight(uploadResult.getHeight());
            tjgimResourceEntity.setFormat(uploadResult.getFormat());
            tjgimResourceEntity.setFsize(uploadResult.getFileSize());
            tjgimResourceEntity.setFilepath(path + id + suffix);
            tjgimResourceEntity.setFilename(id + suffix);
            jgimResourceDao.insertImage(tjgimResourceEntity);
        }
        return uploadResult;
    }

}
