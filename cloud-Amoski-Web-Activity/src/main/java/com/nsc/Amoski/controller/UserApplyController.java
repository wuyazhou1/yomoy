package com.nsc.Amoski.controller;

import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.dto.ActivityApplyDto;
import com.nsc.Amoski.dto.Result;
import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.uti.HttpSMSMessage;
import com.nsc.Amoski.uti.RegUtil;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value="userApply")
@Controller
public class UserApplyController{

    Logger log= LoggerFactory.getLogger(UserApplyController.class);
    /**
     * 用户活动报名
     */
    @RequestMapping(value="/userActivityApply",method = {RequestMethod.POST,RequestMethod.GET})
    public Result userActivityApply(HttpServletRequest request, ActivityApplyDto dto) throws Exception{
        Result rs=new Result();
        RegUtil regUtil =RegUtil.getSingleton();
        rs.setCode(ResultMsg.SUCCESS.getCode());
        rs.setMsg(ResultMsg.SUCCESS.getMessage());
        log.info(">>>>>>>>>>>>>>>>>>userActivityApply  .request param  dto:"+dto);
        //校验参数是否有效
        if(regUtil.isNull(dto.getName(),dto.getMobile(),dto.getCount())){//参数有为空
            rs.setCode(ResultMsg.IS_NULL.getCode());
            rs.setMsg(ResultMsg.IS_NULL.getMessage());
            return rs;
        }
        if(!regUtil.isMobile(dto.getMobile())){//手机号格式错误
            rs.setCode(ResultMsg.MOBILE_FORMAT_ERROR.getCode());
            rs.setMsg(ResultMsg.MOBILE_FORMAT_ERROR.getMessage());
            return rs;
        }
        /*//查询手机号是否已报名
        List<ActivityApplyDto> apply = userCenterManageService.queryMobileApply(dto);
        if(apply!=null&&apply.size()>0){
            return error(ResultMsg.MOBILE_APPLY_ERROR);
        }*/
        dto.setCreateTime(new Timestamp(System.currentTimeMillis()));
        dto.setCreateUser("system");
        /*userCenterManageService.addEntity(entity);*/
        String wcode=writeEmailLog(dto);
        if(ResultMsg.MOBILE_APPLY_ERROR.getCode().equals(wcode)){//手机号存在
            rs.setCode(ResultMsg.MOBILE_APPLY_ERROR.getCode());
            rs.setMsg(ResultMsg.MOBILE_APPLY_ERROR.getMessage());
            return rs;
        }
        //发送短信
        String sendMsgStr = HttpSMSMessage.sendApplyMsg(dto);
        JSONObject objects = JSONObject.parseObject(sendMsgStr);
        String code=objects.get("code").toString();
        if(!"000000".equals(code)){//发送成功
            String msg=objects.get("msg").toString();
            rs.setCode(code);
            rs.setMsg(msg);
            return rs;//发送错误
        }
        return rs;
    }

    public final static String applyFileName="applyPeopleMenu.txt";
    /**
     * 写发送邮件日志
     *
     */
    public synchronized static String writeEmailLog(ActivityApplyDto dto) throws Exception{
        String path = File.separator+"home"+File.separator+"file"+File.separator;
        File logFile=new File(path);
        if(!logFile.exists()){
            logFile.mkdirs();
        }
        logFile=new File(path+applyFileName);
        if(!logFile.exists()){
            logFile.createNewFile();
        }
        BufferedWriter bw=null;
        BufferedReader br=null;
        try {
            DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            br=new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
            bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile,true)));
            String currLine=br.readLine();boolean isExist=false;
            while(currLine!=null&&!"".equals(currLine)){
                System.out.println(">>>>>>>>>>>>>>currLine:"+currLine);
                if(currLine.indexOf(dto.getMobile())>0){//如果存在 则返回
                    isExist=true;
                    break;
                }
                currLine=br.readLine();
            }
            if(isExist){
                return ResultMsg.MOBILE_APPLY_ERROR.getCode();
            }
            String userInfo = br.readLine();
            String logStr=dto.getName()+","+dto.getMobile()+","+dto.getCount()+","+ simpleDateFormat.format(dto.getCreateTime());//写日志
        	 /*SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
         	 String strDt = sdf.format(new Date());

             if(RegUtil.getSingleton().isNull(dto.getRespMessge())){//如果发送成功则把发送信息写入日志 否则把错误信息写入日志
            	 logStr=strDt+"    sendMsg log >>>>>> send success!! "+"sendUserName："+dto.getUserName()+"-----sendEmailName："+dto.getEmailName()+"-----sendTitle："+dto.getTitle()+"-----sendContent："+dto.getContent();
             }else{
            	 logStr=strDt+"    sendMsg log >>>>>> send fail!! "+dto.getRespMessge();
             }*/
            System.out.println("+++++sendMsgLog:"+logStr);
            bw.write(logStr);
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(br!=null){
                br.close();
            }
            if(bw!=null){
                bw.close();
            }
        }
        return ResultMsg.SUCCESS.getCode();
    }
}
