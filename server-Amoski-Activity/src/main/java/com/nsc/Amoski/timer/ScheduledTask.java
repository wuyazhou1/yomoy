package com.nsc.Amoski.timer;

import com.nsc.Amoski.dto.ActivityOrderDto;
import com.nsc.Amoski.entity.TbUsreMessageEntity;
import com.nsc.Amoski.service.ActivityOrderManageService;
import com.nsc.Amoski.service.UserCenterManageService;
import com.nsc.Amoski.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class ScheduledTask {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    UserCenterManageService userCenterManageService;

    @Autowired
    ActivityOrderManageService activityOrderManageService;

    /**
     * 每隔5秒执行, 单位：ms。
     */
   /* @Scheduled(fixedRate = 5000)
    public void testFixRate() {
        System.out.println("我每隔5秒冒泡一次：" + DateUtils.getNowDay());
    }*/

    @Scheduled(cron = "0 0 02 ? * *")    //每周一凌晨触发
    public void testMyBatis() throws ParseException {
        System.out.println("每天凌晨2点触发");
        List<Object> list=new ArrayList<Object>();
        List<ActivityOrderDto> activityOrderList = activityOrderManageService.queryActivityOrderWarning();//查询需要提醒的用户
        for(ActivityOrderDto dto:activityOrderList){
            //插入一条周报消息
            TbUsreMessageEntity param=new TbUsreMessageEntity();
            String date=DateUtils.string2TimeYs(new Date(dto.getActivityStart().getTime()),"yyyy月MM日");
            String msg="您好！欢迎参加"+dto.getTitle()+"活动，本次活动将于"+date+"在"+dto.getCollectionPlace()+"举行，我们将在9:30 - 18:00开始签到；期待您的光临！优摩游活动组委会敬上。";
            param.setMsgContent(msg);
            param.setMsg_title("活动开始提醒");
            param.setMsgType("1");
            param.setMsgImg("");
            param.setCreateTime(new Timestamp(System.currentTimeMillis()));
            param.setCreateUser("system");
            param.setMemberId(dto.getMemberId());
            param.setStatus("2");
            list.add(param);
        }
        logger.info(">>>>>>>>>>batch msg data!!  list:"+list);
        //批量操作
        userCenterManageService.bachInsert(list);
        //RidingInfoDto dto=new RidingInfoDto();
        //ridingManageService.bachInsert(list);//批量操作
    }
}