package com.nsc.Amoski.entity;

import lombok.Data;

import java.util.List;

@Data
public class ActivityCreateQueryEntity {
    private String id;
    private String img;
    private String txt;
    private String type;
    //活动基础表
    private TbActivityBasicsEntity tbActivityBasicsEntity;
    //活动简介表
    private TbActivitySynopsisEntity tbActivitySynopsisEntity;
    //活动报名规则表
    private TbActivityOrdinationEntity tbActivityOrdinationEntity;
    //活动日程安排表
    private List<TbActivityScheduleEntity> tbActivityScheduleEntity;
}
