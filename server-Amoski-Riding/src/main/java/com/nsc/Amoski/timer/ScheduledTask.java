package com.nsc.Amoski.timer;

import com.nsc.Amoski.dto.RidingInfoDto;
import com.nsc.Amoski.entity.TbUsreMessageEntity;
import com.nsc.Amoski.service.RidingTeamManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Component
public class ScheduledTask {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    RidingTeamManageService ridingManageService;
    /**
     * 每隔5秒执行, 单位：ms。

    @Scheduled(fixedRate = 5000)
    public void testFixRate() {
        System.out.println("我每隔5秒冒泡一次：" + DateUtils.getNowDay());
    }*/

    @Scheduled(cron = "0 0 0 ? * MON")    //每周一凌晨触发
    public void testMyBatis() {
        System.out.println("每周一凌晨触发");
        //统计周报数据

        //批量操作

        RidingInfoDto dto=new RidingInfoDto();
        List<Object> list=new ArrayList<Object>();
        //插入一条周报消息
        TbUsreMessageEntity param=new TbUsreMessageEntity();
        param.setMsgContent("欢迎登陆爱摩老司机app!!!!");
        param.setMsg_title("注册成功");
        param.setMsgType("1");
        param.setMsgImg("");
        param.setCreateTime(new Timestamp(System.currentTimeMillis()));
        param.setCreateUser(dto.getCreateUser());
        param.setMemberId(dto.getMemberId());
        param.setStatus("1");
        list.add(param);
        ridingManageService.bachInsert(list);//批量操作
    }
}