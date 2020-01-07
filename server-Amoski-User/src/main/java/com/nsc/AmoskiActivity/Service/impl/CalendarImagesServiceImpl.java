package com.nsc.AmoskiActivity.Service.impl;

import com.nsc.Amoski.entity.ActivityCalendarImagesEntity;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.util.StringUtils;
import com.nsc.AmoskiActivity.Controller.ActivityCreateController;
import com.nsc.AmoskiActivity.Dao.CalendarImagesDao;
import com.nsc.AmoskiActivity.Service.CalendarImagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
@Transactional(transactionManager = "ActivityTransactionManager")
public class CalendarImagesServiceImpl implements CalendarImagesService {
    private static final Logger log = LoggerFactory.getLogger(CalendarImagesServiceImpl.class);
    @Autowired
    private CalendarImagesDao calendarImagesDao;
    @Override
    public PagingBean queryCalendarImagesList(Map<String, Object> params) {
        List list = calendarImagesDao.queryCalendarImagesList(params);
        Integer count = calendarImagesDao.queryCalendarImagesCount(params);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        if (StringUtils.isEmpty(params.get("draw")))
            pagelist.setDraw(0);
        else
            pagelist.setDraw(Integer.valueOf(params.get("draw").toString()));
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }

    @Override
    public void deleteCalendarImages(String id) {
        ActivityCalendarImagesEntity ActivityCalendarImagesEntity = calendarImagesDao.queryCalendarImages(id);
        //deleteTempPathFile(ActivityCalendarImagesEntity.getFilePath()+"/"+ActivityCalendarImagesEntity.getFileNameUrl());
        //deleteTempPathFile(ActivityCalendarImagesEntity.getFilePath()+"/"+ActivityCalendarImagesEntity.getImgCompress());
        calendarImagesDao.deleteCalendarImages(id);
    }

    @Override
    public void updateCalendarImages(List<ActivityCalendarImagesEntity> list) {
        calendarImagesDao.updateCalendarImages(list);
    }

    private boolean deleteTempPathFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                log.info("删除" + fileName + "成功！");
                return true;
            } else {
                log.info("删除" + fileName + "失败！");
                return false;
            }
        } else {
            log.info("删除：" + fileName + "不存在！");
            return false;
        }
    }

}
