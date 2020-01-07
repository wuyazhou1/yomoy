package com.nsc.Amoski.controller;

import com.nsc.Amoski.dto.CreateTeamInfoDto;
import com.nsc.Amoski.dto.NettyJsonBodyObj;
import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.dto.TeamPersonnelInfoDto;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.util.DateUtils;
import com.nsc.Amoski.util.UniqId;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Controller
@ResponseBody
@RequestMapping("/ridingTeam")
public class RidingTeamApiController extends RidingTeamBaseController<RidingTeamApiController>{

    /**
     * 查询队伍信息及队伍人员信息
     * @return
     */
    @ApiOperation(value="查询队伍信息及队伍人员信息", notes = "查询队伍信息及队伍人员信息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="队伍id",dataType="String", paramType = "query"),
    })
    @RequestMapping(value="/queryTeamInfoAndPersonInfo",method = {RequestMethod.POST,RequestMethod.GET})
    public Result queryTeamInfoAndPersonInfo(HttpServletResponse response, HttpServletRequest request,@RequestBody TbCreateTeamInfoEntity entity) throws Exception{
        log.info(">>>>>>>>>>>>>>..queryTeamInfoAndPersonInfo !!! entity:"+entity);
        MemberView userInfo = getRedisUserInfo(request);
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("userId",userInfo.getMemberId());
        CreateTeamInfoDto newEntity=new CreateTeamInfoDto();
        //map.put("currDate",DateUtils.getNowDay(DateUtils.DATE_FORMAT_YS_TIME));
        String whereSql=" valid_end_time>sysdate and member_id=:userId ";
        if(!regUtil.isNull(entity.getId())){
            whereSql+=" and id=:id";
            map.put("id",entity.getId());
        }
        List<TbCreateTeamInfoEntity> list = ridingTeamManageService.queryEntity(entity,whereSql,map);
        if(list!=null&&list.size()>0){//如果存在
            BeanUtils.copyProperties(list.get(0),newEntity);
            TeamPersonnelInfoDto personDto=new TeamPersonnelInfoDto();
            personDto.setTeamId(newEntity.getId());
            List<TbTeamPersonnelInfoEntity> pList = queryTeamAllPersonInfo(personDto);
            newEntity.setList(pList);
        }
        return success();
    }

    /**
     * 创建队伍
     * @return
     */
    @ApiOperation(value="创建队伍", notes = "创建队伍", httpMethod = "POST" )
    @RequestMapping(value="/createTeam",method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name="joinAddr",value="加入队伍地址",dataType="String", paramType = "query"),
    })
    public Result createTeam(HttpServletResponse response, HttpServletRequest request,@RequestBody CreateTeamInfoDto entity) throws Exception{
        log.info(">>>>>>>>>>>>>>..createTeam !!! entity:"+entity);
        if(regUtil.isNull(entity.getJoinAddr())){//参数不为空
            return error(ResultMsg.IS_NULL);
        }
        MemberView userInfo = getRedisUserInfo(request);
        TeamPersonnelInfoDto paramDto =new TeamPersonnelInfoDto();
        paramDto.setMemberId(userInfo.getMemberId());
        //如果当前用户处在一个队伍中  则无法创建队伍
        List<Map<String, Object>> maps = ridingTeamManageService.queryTeamInfoAndPersonInfo(paramDto);
        TbCreateTeamInfoEntity newEntity=new TbCreateTeamInfoEntity();
        if(maps.size()==0){//没有在有效队伍中  创建队伍
            int day=1;
            newEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            newEntity.setValidDay(day);
            newEntity.setStatus("1");
            newEntity.setValidEndTime(new Timestamp(DateUtils.getNextDayNum(day)));
            newEntity.setCreateUser(userInfo.getLoginname());
            newEntity.setTeamer(userInfo.getId());
            newEntity.setTeamCode(UniqId.getRandomPwd(6));//如果有需求  如果code相同  则重新生成

            newEntity.setTeamName("我的队伍");
            ridingTeamManageService.addEntity(newEntity,true);//保存骑行队伍信息
            TbTeamPersonnelInfoEntity pEntity=new TbTeamPersonnelInfoEntity();
            pEntity.setMemberId(userInfo.getMemberId());
            pEntity.setMemberName(userInfo.getName());
            pEntity.setTeamId(newEntity.getId());
            String roleId="1";//骑士
            TbTeamRoleEntity roleEntity = ridingTeamManageService.queryEntity(new TbTeamRoleEntity(), roleId);
            pEntity.setTeamRoleId(Integer.parseInt(roleId));
            pEntity.setTeamRoleName(roleEntity.getRoleName());
            pEntity.setTeamRoleColor(roleEntity.getRoleColor());
            pEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            pEntity.setMemberHeaderUrl(userInfo.getHeadImgFile());
            pEntity.setJoinAddr(entity.getJoinAddr());
            ridingTeamManageService.addEntity(pEntity,true);//保存队伍人员信息
            //保存至redis

        }else{
            return error(ResultMsg.EXIST_RIDINGTEAM_ERROR);
        }
        return success(newEntity);
    }

    /**
     * 修改队伍信息
     * @return
     */
    @ApiOperation(value="修改队伍信息", notes = "修改队伍信息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="teamName",value="队伍名称",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="id",value="队伍id",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="status",value="队伍状态(是否解散 0:解散)",dataType="String", paramType = "query"),
    })
    @RequestMapping(value="/updateTeam",method = RequestMethod.POST)
    public Result updateTeam(HttpServletResponse response, HttpServletRequest request,@RequestBody TbCreateTeamInfoEntity entity){
        log.info(">>>>>>>>>>>>>>..updateTeam !!! entity:"+entity);
        if(regUtil.isNull(entity.getId())){//id不为空
            return error(ResultMsg.IS_NULL);
        }
        TbCreateTeamInfoEntity newEntity = ridingTeamManageService.queryEntity(entity, entity.getId() + "");
        if(newEntity!=null){
            if(!regUtil.isNull(entity.getTeamName())){
                newEntity.setTeamName(entity.getTeamName());
            }
            if("0".equals(entity.getStatus())){
                newEntity.setStatus(entity.getStatus());
            }
            ridingTeamManageService.updateEntity(newEntity);
        }
        return success(newEntity);
    }

    /**
     * 加入队伍
     * @return
     */
    @ApiOperation(value="加入队伍", notes = "加入队伍", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="teamCode",value="队伍口令",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="joinAddr",value="加入队伍地址",dataType="String", paramType = "query"),
    })
    @RequestMapping(value="/joinTeam",method = RequestMethod.POST)
    public Result joinTeam(HttpServletResponse response, HttpServletRequest request,@RequestBody TeamPersonnelInfoDto entity){
        log.info(">>>>>>>>>>>>>>..joinTeam !!! entity:"+entity);
        if(regUtil.isNull(entity.getTeamCode(),entity.getJoinAddr())){//参数错误
            return error(ResultMsg.IS_NULL);
        }
        MemberView userInfo = getRedisUserInfo(request);
        TeamPersonnelInfoDto paramDto =new TeamPersonnelInfoDto();
        paramDto.setMemberId(userInfo.getMemberId());
        //如果当前用户处在一个队伍中  则无法创建队伍
        List<Map<String, Object>> maps = ridingTeamManageService.queryTeamInfoAndPersonInfo(paramDto);
        TbTeamPersonnelInfoEntity pEntity=new TbTeamPersonnelInfoEntity();
        if(maps==null||maps.size()==0) {//没有在有效队伍中  加入队伍
            paramDto =new TeamPersonnelInfoDto();
            paramDto.setTeamCode(entity.getTeamCode());
            maps = ridingTeamManageService.queryTeamInfoAndPersonInfo(paramDto);
            if(maps!=null&&maps.size()>0){
                if(maps.size()>=TEAMMAXCOUNT){//队伍已满
                    return error(ResultMsg.RIDINGTEAM_FILL_ERROR);
                }
                log.info(">>>>>>>>>>..... maps.get(0):"+ maps.get(0)+"========"+maps.get(0).get("ID")+"========"+maps.get(0).get("TEAM_NAME"));
                String roleId="1";//骑士
                TbTeamRoleEntity roleEntity = ridingTeamManageService.queryEntity(new TbTeamRoleEntity(), roleId);
                pEntity.setTeamRoleId(Integer.parseInt(roleId));
                pEntity.setMemberId(userInfo.getMemberId());
                pEntity.setMemberName(userInfo.getName());
                pEntity.setTeamId(Integer.parseInt(maps.get(0).get("ID").toString()));
                pEntity.setTeamRoleName(roleEntity.getRoleName());
                pEntity.setTeamRoleColor(roleEntity.getRoleColor());
                pEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                pEntity.setMemberHeaderUrl(userInfo.getHeadImgFile());
                pEntity.setJoinAddr(entity.getJoinAddr());
                ridingTeamManageService.addEntity(pEntity,true);//保存队伍人员信息
            }else{//骑行队伍不存在
                return error(ResultMsg.NOTEXIST_RIDINGTEAM_ERROR);
            }
        }else{
            return error(ResultMsg.EXIST_RIDINGTEAM_ERROR);
        }
        pEntity.setId(pEntity.getTeamId());
        return success(pEntity);
    }

    /**
     * 离开队伍或踢出队伍
     * @return
     */
    @ApiOperation(value="离开队伍或踢出队伍", notes = "离开队伍或踢出队伍", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="队伍id",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="leaveAddr",value="离队地址",dataType="String", paramType = "query"),
    })
    @RequestMapping(value="/leaveTeam",method = RequestMethod.POST)
    public Result leaveTeam(@RequestBody CreateTeamInfoDto entity){
        log.info(">>>>>>>>>>>>>>..leaveTeam !!! entity:"+entity);
        entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return success();
    }

    /**
     * 查询所有队伍角色
     * @return
     */
    @ApiOperation(value="查询所有队伍角色", notes = "查询所有队伍角色", httpMethod = "POST" )
    @RequestMapping(value="/queryTeamRoleInfo",method = RequestMethod.POST)
    public Result queryTeamRoleInfo(){
        log.info(">>>>>>>>>>>>>>..queryTeamRoleInfo !!! ");
        List<TbTeamRoleEntity> roleList = ridingTeamManageService.queryEntity(new TbTeamRoleEntity(), "1=1", new HashMap<>());
        return success(roleList);
    }

    /**
     * 查询队伍失效
     * @return
     */
    @ApiOperation(value="查询队伍失效", notes = "查询队伍失效", httpMethod = "POST" )
    @RequestMapping(value="/queryTeamInvalid",method = RequestMethod.POST)
    public Result queryTeamInvalid(HttpServletRequest request,@RequestBody Map<String,Object> map){
        log.info(">>>>>>>>>>>>>>..queryTeamInvalid !!! param:"+map);
        Object teamCode=map.get("teamCode");
        TeamPersonnelInfoDto paramDto =new TeamPersonnelInfoDto();
        if(!regUtil.isNull(teamCode)){
            paramDto.setTeamCode(teamCode.toString());
        }
        MemberView userInfo = getRedisUserInfo(request);
        paramDto.setMemberId(userInfo.getMemberId());
        List<Map<String, Object>> maps = ridingTeamManageService.queryTeamInfoAndPersonInfo(paramDto);
        if(maps==null||maps.size()==0){
            return error(ResultMsg.NOTEXIST_RIDINGTEAM_ERROR);
        }
        return success(regUtil.mapKeyToLowerCase(maps.get(0)));
    }

}
