package com.nsc.Amoski.dao;


import com.nsc.Amoski.dto.ActivityBasicDto;
import com.nsc.Amoski.dto.ActivityScheduleDto;
import com.nsc.Amoski.dto.ActivitySignUpDto;
import com.nsc.Amoski.entity.PagingBean;

import java.util.List;

public interface GuideRouteManageDao {

    /**
     * 查询所有活动列表
     * @param param 查询条件 查询所有活动列表
     * @return  查询所有活动列表
     */
    PagingBean queryActivityList(ActivityBasicDto param);

    /**
     * 小程序查询所有活动列表
     * @param param 查询条件 小程序查询所有活动列表
     * @return  小程序查询所有活动列表
     */
    PagingBean smallQueryActivityList(ActivityBasicDto param);

    /**
     * 小程序查询当前正在进行的活动列表信息
     *
     * @param param
     * @return
     */
    PagingBean smallQueryCurrentActivityList(ActivityBasicDto param);

    /**
     * 查询单个活动详情信息
     * @return  查询单个活动详情信息
     */
    ActivityBasicDto queryActivityDetailInfo(ActivityBasicDto info,boolean bl);

    /**
     * 查询活动路书详细信息
     * @return  查询活动路书详细信息
     */
    List<ActivityScheduleDto> queryH5ActivityGuideInfo(ActivityScheduleDto info) ;

    /**
     * 查询报名人员和接送信息
     * @return  查询报名人员和接送信息
     */
    List<ActivitySignUpDto> queryActivitySignUpPeople(ActivitySignUpDto info);

    String queryActityImages(String code);
}
