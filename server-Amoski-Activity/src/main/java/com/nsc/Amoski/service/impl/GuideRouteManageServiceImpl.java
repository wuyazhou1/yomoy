package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.GuideRouteManageDao;
import com.nsc.Amoski.dto.ActivityBasicDto;
import com.nsc.Amoski.dto.ActivityScheduleDto;
import com.nsc.Amoski.dto.ActivitySignUpDto;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.service.GuideRouteManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class GuideRouteManageServiceImpl implements GuideRouteManageService {

    @Autowired
    GuideRouteManageDao guideRouteManageDao;

    /**
     * 查询所有活动列表
     *
     * @param param 查询条件 查询所有活动列表
     * @return 查询所有活动列表
     */
    public PagingBean queryActivityList(ActivityBasicDto param) {
        return guideRouteManageDao.queryActivityList(param);
    }

    /**
     * 小程序查询所有活动列表
     *
     * @param param 查询条件 小程序查询所有活动列表
     * @return 小程序查询所有活动列表
     */
    public PagingBean smallQueryActivityList(ActivityBasicDto param) {
        return guideRouteManageDao.smallQueryActivityList(param);
    }

    /**
     * 小程序查询当前正在进行的活动列表信息
     *
     * @param param 查询条件 小程序查询所有活动列表
     * @return 小程序查询所有活动列表
     */
    @Override
    public PagingBean smallQueryCurrentActivityList(ActivityBasicDto param) {
        return guideRouteManageDao.smallQueryCurrentActivityList(param);
    }

    /**
     * 查询单个活动详情信息
     *
     * @return 查询单个活动详情信息
     */
    @Override
    public ActivityBasicDto queryActivityDetailInfo(ActivityBasicDto info, boolean bl) {
        return guideRouteManageDao.queryActivityDetailInfo(info, bl);
    }

    /**
     * 查询活动路书详细信息
     *
     * @return 查询活动路书详细信息
     */
    @Override
    public List<ActivityScheduleDto> queryH5ActivityGuideInfo(ActivityScheduleDto info) {
        return guideRouteManageDao.queryH5ActivityGuideInfo(info);
    }

    /**
     * 查询报名人员和接送信息
     *
     * @return 查询报名人员和接送信息
     */
    @Override
    public List<ActivitySignUpDto> queryActivitySignUpPeople(ActivitySignUpDto info) {
        return guideRouteManageDao.queryActivitySignUpPeople(info);
    }

    @Override
    public String queryActityImages(String code) {
        return guideRouteManageDao.queryActityImages(code);
    }
}








































