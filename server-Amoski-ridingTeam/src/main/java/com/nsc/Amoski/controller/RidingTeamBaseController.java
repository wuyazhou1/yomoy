package com.nsc.Amoski.controller;

import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.TbTeamPersonnelInfoEntity;
import com.nsc.Amoski.repository.BaseController;
import com.nsc.Amoski.service.MemberService;
import com.nsc.Amoski.service.RidingTeamManageService;
import com.nsc.Amoski.uti.ResultUtil;
import com.nsc.Amoski.util.DateUtils;
import com.nsc.Amoski.util.GsonUtil;
import com.nsc.Amoski.util.RegUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RidingTeamBaseController<T> extends BaseController {
    //打印日志对象
    Logger log;

    public RegUtil regUtil= RegUtil.getSingleton();

    @Autowired
    MemberService memberService;

    @Autowired
    RidingTeamManageService ridingTeamManageService;

    public static  final  int TEAMMAXCOUNT=20;


    @SuppressWarnings("unchecked")
    public RidingTeamBaseController() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        log= LoggerFactory.getLogger((Class<T>) type.getActualTypeArguments()[0]);
        //System.out.println("Dao实现类是：" + entityClass.getName());
    }

    public Result success(){
        Result result = ResultUtil.success();
        log.info("===============responseData:"+GsonUtil.dtoToJson(result));
        return result;
    }
    public <T> Result success(T t){
        Result result = ResultUtil.success(t);
        log.info("===============responseData:"+ GsonUtil.dtoToJson(result));
        return result;
    }
    public Result error(String code,String msg){
        Result result = ResultUtil.error(code,msg);
        log.info("===============responseData:"+GsonUtil.dtoToJson(result));
        return result;
    }

    public Result error(ResultMsg msg){
        Result result = ResultUtil.error(msg);
        log.info("===============responseData:"+ GsonUtil.dtoToJson(result));
        return result;
    }
    public <T> Result error(ResultMsg msg,T t){
        Result result = ResultUtil.error(msg,t);
        log.info("===============responseData:"+GsonUtil.dtoToJson(result));
        return result;
    }
    /**
     * 获取用户redis信息
     * sessionId 请求sessionid
     * bl 是否更新用户数据
     */
    public MemberView getRedisUserInfo(HttpServletRequest request){
        String sessionId=request.getSession().getId();
        String appToken = request.getHeader("appToken");
        if(!regUtil.isNull(request.getParameter("appToken"))){
            sessionId=request.getParameter("appToken");
        }
        if(!RegUtil.getSingleton().isNull(appToken)){
            sessionId=appToken;
        }
        String reidsKey=REDIS_USERINFO_KEY+"_"+sessionId;
        MemberView dto=memberService.getRedisUserObj(reidsKey,null);
        log.info(">>>>>>>>>>>>redis user info !!!!!sessionId:"+sessionId+">>>>>... dto:"+dto);
        return dto;
    }

    /**
     * 获取队伍人员信息
     * @param teamDto
     * @return
     */
    public List<TbTeamPersonnelInfoEntity> queryTeamAllPersonInfo(TeamPersonnelInfoDto teamDto){
        log.info(">>>>>>>>>>>>>>>>queryTeamAllPersonInfo=======teamDto:"+teamDto);
        Map<String,Object> map=new HashMap<String,Object>();
        String whereSql=" team_id=:teamId ";
        map.put("teamId",teamDto.getTeamId());
        if(!regUtil.isNull(teamDto.getMemberId())){
            whereSql+=" and member_id=:memberId ";
            map.put("memberId",teamDto.getMemberId());
        }
        List<TbTeamPersonnelInfoEntity> pList = ridingTeamManageService.queryEntity(new TbTeamPersonnelInfoEntity(), whereSql, map);
        return pList;
    }

    /**
     * 获取redis队伍信息数据
     * @param teamCode
     * @return
     */
    public CreateTeamInfoDto getRedisTeamInfo(String teamCode){
        CreateTeamInfoDto teamInfo = (CreateTeamInfoDto)memberService.getSingValue(REDIS_RIDING_TEAM_INFO + "_" + teamCode, null);//队伍信息放入redis
        log.info(">>>>>>>>>>>>>>>>>>>>>getRedisTeamInfo====teamCode:"+teamCode+"++++++++++++++teamInfo:"+teamInfo);
        return teamInfo;
    }

    /**
     * 设置redis队伍信息数据
     * @param teamCode
     * @return
     */
    public void setRedisTeamInfo(String teamCode, CreateTeamInfoDto redisData){
        log.info(">>>>>>>>>>>>>>>>>>>>>setRedisTeamInfo====teamCode:"+teamCode+"++++++++++++++teamInfo:"+redisData);
        memberService.setSingValue(REDIS_RIDING_TEAM_INFO+"_"+teamCode,redisData);//队伍信息放入redis
    }


    /**
     * 封装socket返回数据
     * @return
     */
    public String pageSocketReturnData(NettyJsonObj requestData,ResultMsg respMsg,NettyJsonBodyObj jsonBodyObj){
        if(requestData==null){
            requestData=new NettyJsonObj();
        }
        requestData.setSendTime(DateUtils.getCurrentStamp().getTime());
        requestData.setCode(respMsg.getCode());
        if(ResultMsg.LONGTIMEOUT.getCode().equals(respMsg.getCode())){//长连接登陆剔除
            requestData.setMsg("长连接登陆失效!!请重新加入或创建队伍");
        }else{
            requestData.setMsg(respMsg.getMessage());
        }

        requestData.setBody(jsonBodyObj);
        return JSONObject.toJSONString(requestData);
    }

    /**
     * 验证socket是否登录
     * @return
     */
    public String checkSocketLoginIn(ChannelHandlerContext ctx){
        String redisKey=ctx.channel().id().toString();
        Object value = memberService.getSingValue(redisKey, null);
        log.info(">>>>>>>>>.checkSocketLoginIn!!!!!! value:"+value);
        if(value!=null&&!regUtil.isNull(value)){//登录成功
            return value.toString();
        }
        return "";
    }
    /**
     * 封装socket返回数据
     * @return
     */
    public String pageSocketReturnData(NettyJsonObj requestData){
        if(requestData==null){
            requestData=new NettyJsonObj();
        }
        NettyJsonBodyObj jsonBodyObj=new NettyJsonBodyObj();
        requestData.setSendTime(DateUtils.getCurrentStamp().getTime());
        requestData.setCode(ResultMsg.IS_NULL.getCode());
        requestData.setMsg(ResultMsg.IS_NULL.getMessage());
        requestData.setBody(jsonBodyObj);
        return JSONObject.toJSONString(requestData);
    }
    /**
     * 封装socket返回数据
     * @return
     */
    public String pageSocketReturnSuccessData(NettyJsonObj requestData,NettyJsonBodyObj jsonBodyObj){
        if(requestData==null){
            requestData=new NettyJsonObj();
        }
        requestData.setSendTime(DateUtils.getCurrentStamp().getTime());
        requestData.setCode(ResultMsg.SUCCESS.getCode());
        requestData.setMsg(ResultMsg.SUCCESS.getMessage());
        requestData.setBody(jsonBodyObj);
        return JSONObject.toJSONString(requestData);
    }
}
