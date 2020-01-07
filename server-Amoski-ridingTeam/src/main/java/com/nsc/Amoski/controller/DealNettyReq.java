package com.nsc.Amoski.controller;

import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.nettyServer.GlobalUserUtil;
import com.nsc.Amoski.util.GsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DealNettyReq extends RidingTeamBaseController<DealNettyReq>{

    /**
     * 所有队伍信息
     */
    private Map<String,CreateTeamInfoDto> golTeamInfoMap=new HashMap<String,CreateTeamInfoDto>();

    private NettyJsonBodyObj DEFAULTBODY=new NettyJsonBodyObj();

    /**
     * 中心分发处理
     * @return
     */
    public String centerDealReq(ChannelHandlerContext ctx,String jsonStr){
        String respStr="";
        NettyJsonObj requestData=null;
        try{
            String dealStr=jsonStr.trim();
            log.info(">>>>>>>>>....deal:"+jsonStr.indexOf("\r\n"));
            if(jsonStr.indexOf("\\r\\n")>0){
                //处理\r\n
                //dealStr=jsonStr.substring(0,jsonStr.length()-4);
                dealStr=jsonStr.replace("\\r\\n","");
            }
            log.info(">>>>>>>>>>>>>dealStr:"+dealStr);
            if(!dealStr.startsWith("{")||!dealStr.endsWith("}")){
                respStr=pageSocketReturnData(requestData);
                writeAndFlushData(ctx.channel(),respStr);
                return "";
            }
            if(dealStr.indexOf("1003")>0){//心跳
                ByteBuf wbuf = ctx.alloc().buffer();
                wbuf.writeBytes((dealStr+"\r\n").getBytes());
                ctx.writeAndFlush(wbuf);
                return "";
            }
            requestData = (NettyJsonObj) GsonUtil.jsonToDto(dealStr, NettyJsonObj.class);
            log.info(">>>>>>>>>>>>>>>>>>>>requestDataDto:"+requestData);
            String type = requestData.getType();
            if(type!=null&&type.startsWith("10")&&!type.equals(SocketDealType.JOINTEAM.getCode())){//统一拦截登陆状态
                if(regUtil.isNull(checkSocketLoginIn(ctx))){
                    log.info(">>>>>>>>>>>>> user not login !!  =============requestData:"+requestData);
                    respStr=pageSocketReturnData(requestData, ResultMsg.LONGTIMEOUT,DEFAULTBODY);
                    log.info(">>>>>>>>>>>>>writeBytes respStr:"+respStr);
                    writeAndFlushData(ctx.channel(),respStr);
                    return "";
                }
            }
            //判断队伍是否失效
            TbCreateTeamInfoEntity entity=queryTeamInfo(requestData.getTeamCode());
            if(entity==null) {//如果存在
                CreateTeamInfoDto redisData = getRedisTeamInfo(requestData.getTeamCode());
                if(redisData!=null){
                    log.info(">>>>>>>>>>>>>team not exist!!  =============");
                    respStr=pageSocketReturnData(requestData, ResultMsg.NOTEXIST_RIDINGTEAM_ERROR,DEFAULTBODY);
                    log.info(">>>>>>>>>>>>>writeBytes respStr:"+respStr);
                    requestData.setType(SocketDealType.DISMISSTEAM.getCode());
                    sendTeamOtherPerson(requestData,redisData.getPersonMap(),respStr);
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("teamCode",requestData.getTeamCode());
                    String whereSql=" status=0 where valid_end_time<=sysdate and team_code=:teamCode and status=1 ";
                    ridingTeamManageService.updateEntityByWhereSql(new TbCreateTeamInfoEntity(),whereSql,map);
                }
                return "";
            }
            switch(type){
                case "1000"://创建或加入队伍
                    log.info("create or join connect!!  begin riding team!!!!");
                    respStr=loginAndCreateAndJoinSuccess(ctx,requestData);
                    break;
                case "1001"://发送队友点数据
                    log.info("send teammate point info!!!! ");
                    respStr=sendTeamerPointInfo(ctx,requestData);
                    break;
                /*case "1002"://队友离线
                    log.info("teammate offline!!!!!!");
                    respStr=teammateOffline(ctx,requestData);
                    break;*/
                case "1003"://心跳
                    log.info("connect heartbeat!!!");
                    respStr=heartbeat(ctx,requestData);
                    break;
                case "1004"://队友离开队伍
                    log.info("connect leave team!!!");
                    respStr=teammateOffline(ctx,requestData);
                    break;
                case "1005"://队友被踢出队伍
                    log.info("connect reject team!!!");
                    respStr=rejectOffline(ctx,requestData);
                    break;
                case "1006"://查询队伍信息
                    log.info("connect query teamInfo!!!");
                    respStr=queryTeamInfo(ctx,requestData);
                    break;
                case "1007"://修改队伍信息
                    log.info("connect update teamInfo!!!");
                    respStr=updateTeamInfo(ctx,requestData);
                    break;
                case "1008"://解散队伍
                    log.info("connect dismissTeam!!!");
                    respStr=dismissTeam(ctx,requestData);
                    break;
                /*case "1009"://获取所有角色信息
                    log.info("connect get all team role!!!");
                    respStr=queryTeamRoleInfo(ctx,requestData);
                    break;*/
                case "1009"://修改角色信息
                    log.info("connect update team perpson role info!!!");
                    respStr=updateTeamRoleInfo(ctx,requestData);
                    break;
                case "1010"://转移队伍权限
                    log.info("connect move team author!!!");
                    respStr=moveTeamAuthor(ctx,requestData);
                    break;
                case "1011"://队长导航
                    log.info("connect teamer drive navi!!!");
                    respStr=teamerDriveNavi(ctx,requestData);
                    break;
                case "1012"://取消导航
                    log.info("connect teamer cancel drive navi!!!");
                    respStr=teamerCancelDriveNavi(ctx,requestData);
                    break;
                default:
                    log.info("nothing");
                    respStr=jsonStr;
                    break;
            }
            // 返回客户端消息
        }catch (Exception e){
            e.printStackTrace();
            // 返回客户端消息
            respStr=pageSocketReturnData(requestData, ResultMsg.UNKNOWN_EXCEPTION,DEFAULTBODY);
            //buf.writeBytes(e.getMessage().getBytes());
        }
        log.info(">>>>>>>>>>>>>writeBytes respStr:"+respStr);
        writeAndFlushData(ctx.channel(),respStr);
        return "";
    }

    /**
     * 加入或创建队伍
     * @param ctx
     * @param requestData
     * @return
     */
    public String loginAndCreateAndJoinSuccess(ChannelHandlerContext ctx,NettyJsonObj requestData) throws Exception{
        //JSONObject bodyObj=JSONObject.parseObject(requestData.getBody());
        NettyJsonBodyObj bodyObj=requestData.getBody();
        Map<String,Object> map=new HashMap<String,Object>();
        String token=bodyObj.getToken();
        String reidsKey=REDIS_USERINFO_KEY+"_"+token;
        MemberView dto=memberService.getRedisUserObj(reidsKey,null);
        ChannelId channelId = ctx.channel().id();
        String respStr=pageSocketReturnSuccessData(requestData,DEFAULTBODY);
        if(dto==null){//未登录
            log.info(">>>>>>>>>>>>>loginAndCreateAndJoinSuccess user not login !!  =============");
            return pageSocketReturnData(requestData, ResultMsg.LONGTIMEOUT,DEFAULTBODY);
        }else{
            /*map.put("teamId",requestData.getTeamId());
            map.put("teamCode",requestData.getTeamCode());
            String whereSql=" valid_end_time>sysdate and id=:teamId and team_code=:teamCode and status=1 ";
            List<TbCreateTeamInfoEntity> list = ridingTeamManageService.queryEntity(new TbCreateTeamInfoEntity(),whereSql,map);*/
            TbCreateTeamInfoEntity entity=queryTeamInfo(requestData.getTeamCode());
            if(entity!=null) {//如果存在
                boolean isSend=false;
                CreateTeamInfoDto redisData = getRedisTeamInfo(requestData.getTeamCode());
                int index=checkTeamExistPerson(requestData,redisData);//检测加入队员是否已存在  存在先删除数据
                log.info(">>>>>>>>>>>>create or join riding team!!!  teamInfo redisData :"+redisData+">>>>>");
                TeamPersonnelInfoDto personDto=new TeamPersonnelInfoDto();
                personDto.setTeamId(Integer.parseInt(requestData.getTeamId()));
                personDto.setMemberId(requestData.getUserId());
                List<TbTeamPersonnelInfoEntity> pList = queryTeamAllPersonInfo(personDto);
                if(pList!=null&&pList.size()>0){
                    personDto=new TeamPersonnelInfoDto();
                    BeanUtils.copyProperties(pList.get(0),personDto);
                    personDto.setChannelId(channelId);
                    personDto.setTeamCode(requestData.getTeamCode());
                    personDto.setStatus("1");
                    //redisData.setDtoList();//更新队伍人员信息
                    log.info(">>>>>>>>>.update TeamPersonnelInfoDto success!!!!!!!personDto:"+personDto);
                }else{
                    return pageSocketReturnData(requestData, ResultMsg.NOTEXIST_TEAMPERSON_ERROR,DEFAULTBODY);
                }
                if(redisData==null){
                    redisData=new CreateTeamInfoDto();
                    BeanUtils.copyProperties(entity,redisData);
                    log.info(">>>>>>>>>>>>>BeanUtils.copyProperties after!!! redisData:"+redisData);
                    if(redisData.getTeamer().equals(requestData.getUserId())){//队长  说明创建队伍
                        golTeamInfoMap.put(redisData.getTeamCode(),redisData);
                    }
                }else{//加入队伍  推送给队伍其他人
                    isSend=true;
                }
                memberService.getSingValue("usesId_"+dto.getMemberId(),null);//
                redisData.getDtoList().add(index,personDto);//更新队伍人员信息 加入以便于app使用  默认插入到
                Map<String,TeamPersonnelInfoDto> dtoMap = redisData.getPersonMap();
                dtoMap.put(channelId.toString(),personDto);
                log.info(">>>>>>>>>>>>>>>>>>>>update redisData after data:"+redisData);
                memberService.setSingValue(channelId.toString(),redisData.getTeamCode());//标明此链接已登录
                memberService.setSingValue("usesId_"+dto.getMemberId(),channelId.toString());//用户和连接的关系
                redisData.setTeamMaxCount(TEAMMAXCOUNT);
                setRedisTeamInfo(requestData.getTeamCode(),redisData);
                if(isSend){
                    NettyJsonBodyObj respBodyObj=new NettyJsonBodyObj();
                    respBodyObj.setPesonInfo(personDto);
                    log.info(">>>>>>>>>>>>>>>sendUserinfo ============"+respBodyObj);
                    String respStr1=pageSocketReturnSuccessData(requestData,respBodyObj);
                    sendTeamOtherPerson(requestData,redisData.getPersonMap(),respStr1);//推送加入队伍骑手信息给其他人
                }
            }else{//队伍不存在或已失效
                log.info(">>>>>>>>>>>>>team not exist!!  =============");
                return pageSocketReturnData(requestData, ResultMsg.NOTEXIST_RIDINGTEAM_ERROR,DEFAULTBODY);
            }
        }
        return respStr;
    }

    /**
     * 推送骑友当前点数据
     * @param ctx
     * @param requestData
     * @return
     */
    public String sendTeamerPointInfo(ChannelHandlerContext ctx,NettyJsonObj requestData) throws Exception{
        CreateTeamInfoDto redisData = getRedisTeamInfo(requestData.getTeamCode());
        String key=ctx.channel().id().toString();
        Map<String, TeamPersonnelInfoDto> personMap = redisData.getPersonMap();
        TeamPersonnelInfoDto personnelInfoDto = personMap.get(ctx.channel().id().toString());
        NettyJsonBodyObj jsonObj = requestData.getBody();
        String token=jsonObj.getToken();
        String reidsKey=REDIS_USERINFO_KEY+"_"+token;
        MemberView userInfo=memberService.getRedisUserObj(reidsKey,null);
        log.info(">>>>>>>>sendTeamerPointInfo token:"+token+"userInfo:"+userInfo);

        //JSONObject jsonObj = JSONObject.parseObject(requestData.getBody());
        String point=jsonObj.getLatitude()+","+jsonObj.getLongitude();
        personnelInfoDto.setLastPoint(point);
        List<TeamPersonnelInfoDto> dtoList = redisData.getDtoList();
        for(TeamPersonnelInfoDto dto:dtoList){
            if(requestData.getUserId().intValue()==dto.getMemberId().intValue()){
                log.info(">>>>>>> sendTeamerPointInfo >>>>>>>>lastPoint:"+point);
                dto.setLastPoint(point);
                boolean bl=false;
                if(!regUtil.isNull(userInfo.getName())){
                    if(!userInfo.getName().equals(dto.getMemberName())){
                        bl=true;
                        dto.setMemberName(userInfo.getName());
                        log.info(">>>>>>>>>>>>>>>sendTeamerPointInfo updMemberName oldName:"+dto.getMemberName()+"======memberName:"+userInfo.getName());
                    }
                }
                if(!regUtil.isNull(userInfo.getHeadImgFile())){
                    if(!userInfo.getHeadImgFile().equals(dto.getMemberHeaderUrl())){
                        bl=true;
                        dto.setMemberHeaderUrl(userInfo.getHeadImgFile());
                        log.info(">>>>>>>>>>>>>>>sendTeamerPointInfo updHeadUrl =====oldHeadUrl:"+dto.getMemberHeaderUrl()+"====memberUrl:"+userInfo.getHeadImgFile());

                    }
                }
                if(bl){//修改了个人信息  推送给所有队伍里面的人员
                    //写1006type给客户端
                    NettyJsonBodyObj respBodyObj=new NettyJsonBodyObj();
                    respBodyObj.setRedisData(redisData);
                    requestData.setType(SocketDealType.GETTEAMINFO.getCode());
                    String respStr= pageSocketReturnSuccessData(requestData,redisData==null?DEFAULTBODY:respBodyObj);
                    sendTeamOtherPerson(requestData,personMap,respStr);
                    writeAndFlushData(ctx.channel(),respStr);
                }
                break;
            }
        }
        jsonObj.setToken(null);
        setRedisTeamInfo(redisData.getTeamCode(),redisData);//更新redis
        sendTeamOtherPerson(requestData,personMap,null);
        Object obj = memberService.getSingValue(key,null);//登陆状态
        if(obj!=null){
            String teamCode=obj.toString();//标明此链接已登录
            memberService.setSingValue(key,teamCode);//标明此链接已登录
        }
        requestData.setType(SocketDealType.SENDPOINT.getCode());
        return pageSocketReturnSuccessData(requestData,DEFAULTBODY);
    }
    /**
     * 队友离开队伍或离线
     * @param ctx
     * @param requestData
     * @return
     */
    public String teammateOffline(ChannelHandlerContext ctx,NettyJsonObj requestData)throws Exception{
        /*JSONObject jsonObj=JSONObject.parseObject(requestData.getBody());
        Map<String,Object> map=new HashMap<>();*/
        CreateTeamInfoDto redisData=getTeamInfo(ctx,requestData);//获取队伍信息
        if(redisData!=null){
            Map<String, TeamPersonnelInfoDto> personMap = redisData.getPersonMap();
            Map<String, TeamPersonnelInfoDto> newPersonMap = new HashMap<String, TeamPersonnelInfoDto>();
            newPersonMap.putAll(personMap);//复制一个map 用于推送数据
            log.info(">>>>>>>>>>>>>>>teammateOffline  personMap:"+personMap);
            TeamPersonnelInfoDto personnelInfoDto = personMap.get(ctx.channel().id().toString());
            if(personnelInfoDto!=null){
                requestData.setUserId(personnelInfoDto.getMemberId());
                requestData.setTeamCode(personnelInfoDto.getTeamCode());
                requestData.setTeamId(personnelInfoDto.getTeamId().toString());
                if(SocketDealType.OFFLINE.getCode().equals(requestData.getType())){//离线修改状态
                    personnelInfoDto.setStatus("0");
                }else{//主动离开  删除信息 移除redis数据
                    String ids=clearListTeamerData(requestData.getUserId().toString(),redisData.getDtoList(),personMap);
                    ridingTeamManageService.deleteDataByIds(new TbTeamPersonnelInfoEntity(),ids);//删除信息
                }
                memberService.setSingValue(ctx.channel().id().toString(), null);//清空数据
                setRedisTeamInfo(redisData.getTeamCode(),redisData);//更新redis
                //String respStr=pageSocketReturnSuccessData(requestData,requestData.getBody());
                sendTeamOtherPerson(requestData,newPersonMap,null);
            }
            GlobalUserUtil.channels.remove(ctx.channel());
            //ctx.channel().close().sync();
        }
        return pageSocketReturnSuccessData(requestData,DEFAULTBODY);
    }

    /**
     * 队友被踢
     * @param ctx
     * @param requestData
     * @return
     */
    public String rejectOffline(ChannelHandlerContext ctx,NettyJsonObj requestData) throws Exception{
        /*JSONObject jsonObj=JSONObject.parseObject(requestData.getBody());
        Map<String,Object> map=new HashMap<>();*/
        CreateTeamInfoDto redisData = getRedisTeamInfo(requestData.getTeamCode());
        NettyJsonBodyObj respBodyObj=requestData.getBody();
        String respStr=pageSocketReturnSuccessData(requestData,redisData==null?DEFAULTBODY:respBodyObj);
        Map<String, TeamPersonnelInfoDto> personMap = redisData.getPersonMap();
        Map<String, TeamPersonnelInfoDto> newPersonMap = new HashMap<String, TeamPersonnelInfoDto>();
        newPersonMap.putAll(personMap);//复制一个map 用于推送数据
        //移除list数据
        String ids=clearListTeamerData(respBodyObj.getUserIds(),redisData.getDtoList(),personMap);

        ridingTeamManageService.deleteDataByIds(new TbTeamPersonnelInfoEntity(),ids);//删除
        setRedisTeamInfo(requestData.getTeamCode(),redisData);//更新redis
        //推送数据
        sendTeamOtherPerson(requestData,newPersonMap,respStr);
        log.info(">>>>>>>>>>>>>send data after!!!:"+redisData.getPersonMap());

        return respStr;
    }
    /**
     * 查询队伍所有信息
     * @param ctx
     * @param requestData
     * @return
     */
    public String queryTeamInfo(ChannelHandlerContext ctx,NettyJsonObj requestData){
        /*JSONObject jsonObj=JSONObject.parseObject(requestData.getBody());
        Map<String,Object> map=new HashMap<>();*/
        CreateTeamInfoDto redisData = getRedisTeamInfo(requestData.getTeamCode());
        NettyJsonBodyObj respBodyObj=new NettyJsonBodyObj();
        respBodyObj.setRedisData(redisData);
        return pageSocketReturnSuccessData(requestData,redisData==null?DEFAULTBODY:respBodyObj);
    }

    /**
     * 查询队伍所有角色信息
     * @param ctx
     * @param requestData
     * @return
     */
    public String queryTeamRoleInfo(ChannelHandlerContext ctx,NettyJsonObj requestData){
        List<TbTeamRoleEntity> roleList = ridingTeamManageService.queryEntity(new TbTeamRoleEntity(), "1=1", new HashMap<>());
        NettyJsonBodyObj respBodyObj=new NettyJsonBodyObj();
        respBodyObj.setRoleList(roleList);
        return pageSocketReturnSuccessData(requestData,respBodyObj);
    }
    /**
     * 转移队长权限
     * @param ctx
     * @param requestData
     * @return
     */
    public String moveTeamAuthor(ChannelHandlerContext ctx,NettyJsonObj requestData) throws Exception{
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("teamId",requestData.getTeamId());
        map.put("teamCode",requestData.getTeamCode());
        String moveUserId=requestData.getBody().getUserIds();
        if(regUtil.isNull(moveUserId)){
            return pageSocketReturnData(requestData);
        }
        String whereSql=" valid_end_time>sysdate and id=:teamId and team_code=:teamCode and status=1 ";
        List<TbCreateTeamInfoEntity> list = ridingTeamManageService.queryEntity(new TbCreateTeamInfoEntity(),whereSql,map);
        String respStr=pageSocketReturnSuccessData(requestData,DEFAULTBODY);
        if(list!=null&&list.size()>0) {//如果存在
            CreateTeamInfoDto redisData = getRedisTeamInfo(requestData.getTeamCode());
            redisData.setTeamer(Integer.parseInt(moveUserId));
            TbCreateTeamInfoEntity entity=list.get(0);
            entity.setTeamer(Integer.parseInt(moveUserId));
            ridingTeamManageService.updateEntity(entity);
            List<TeamPersonnelInfoDto> dtoList = redisData.getDtoList();
            TeamPersonnelInfoDto newDto=null;
            int idx=0;
            for(int i=0;i<dtoList.size();i++){
                TeamPersonnelInfoDto dto=dtoList.get(i);
                if(dto.getMemberId().intValue()==Integer.parseInt(moveUserId)){
                    newDto=dto;
                    idx=i;
                }
            }
            if(newDto!=null){//交换集合位置
                dtoList.set(idx,dtoList.get(0));
                dtoList.set(0,newDto);
            }
            setRedisTeamInfo(requestData.getTeamCode(),redisData);//更新redis
            //推送数据
            sendTeamOtherPerson(requestData,redisData.getPersonMap(),respStr);
        }
        return respStr;
    }


    /**
     * 修改队伍角色信息
     * @param ctx
     * @param requestData
     * @return
     */
    /*public String updateTeamRoleInfo(ChannelHandlerContext ctx,NettyJsonObj requestData) throws Exception{
        CreateTeamInfoDto redisData = getRedisTeamInfo(requestData.getTeamCode());
        NettyJsonBodyObj jsonObj = requestData.getBody();
        if(!regUtil.isNull(jsonObj.getRoleId())){//修改用户角色
            TbTeamRoleEntity tbRoleEntity = ridingTeamManageService.queryEntity(new TbTeamRoleEntity(), jsonObj.getRoleId());
            if(tbRoleEntity!=null){
                TeamPersonnelInfoDto personDto=new TeamPersonnelInfoDto();
                personDto.setTeamId(Integer.parseInt(requestData.getTeamId()));
                personDto.setMemberId(requestData.getUserId());
                List<TbTeamPersonnelInfoEntity> pList = queryTeamAllPersonInfo(personDto);
                if(pList!=null&&pList.size()>0){
                    TbTeamPersonnelInfoEntity pdto=pList.get(0);
                    pdto.setTeamRoleColor(tbRoleEntity.getRoleColor());
                    pdto.setTeamRoleName(tbRoleEntity.getRoleName());
                    pdto.setTeamRoleId(tbRoleEntity.getId());
                    ridingTeamManageService.updateEntity(pdto);//更新数据库信息
                    //更新redis数据
                    Map<String, TeamPersonnelInfoDto> personMap = redisData.getPersonMap();
                    //更新map
                    TeamPersonnelInfoDto mapPersonDto = personMap.get(ctx.channel().id().toString());
                    mapPersonDto.setTeamRoleId(pdto.getTeamRoleId());
                    mapPersonDto.setTeamRoleName(pdto.getTeamRoleName());
                    mapPersonDto.setTeamRoleColor(pdto.getTeamRoleColor());
                    List<TeamPersonnelInfoDto> dtoList = redisData.getDtoList();
                    for(TeamPersonnelInfoDto dto:dtoList){//更新集合信息
                        if(dto.getMemberId().intValue()==requestData.getUserId()){
                            dto.setTeamRoleId(pdto.getTeamRoleId());
                            dto.setTeamRoleName(pdto.getTeamRoleName());
                            dto.setTeamRoleColor(pdto.getTeamRoleColor());
                        }
                    }
                    NettyJsonBodyObj respBodyObj=new NettyJsonBodyObj();
                    respBodyObj.setPesonInfo(mapPersonDto);
                    log.info(">>>>>>>>>>>>>>>sendUserinfo ============"+mapPersonDto+"========");
                    String respStr1=pageSocketReturnSuccessData(requestData,respBodyObj);
                    sendTeamOtherPerson(requestData,personMap,respStr1);//推送加入队伍骑手信息给其他人
                }
            }

        }
        return pageSocketReturnSuccessData(requestData,DEFAULTBODY);
    }*/


    /**
     * 修改队伍角色信息
     * @param ctx
     * @param requestData
     * @return
     */
    public String updateTeamRoleInfo(ChannelHandlerContext ctx,NettyJsonObj requestData) throws Exception{
        CreateTeamInfoDto redisData = getRedisTeamInfo(requestData.getTeamCode());
        Map<String,TeamPersonnelInfoDto> personMap=redisData.getPersonMap();
        NettyJsonBodyObj jsonObj = requestData.getBody();
        List<TeamPersonnelInfoDto1> newDtoList=jsonObj.getDtoList();
        List<Object> updList=new ArrayList<Object>();
        String respStr="";
        if(newDtoList!=null&&newDtoList.size()>0){//修改用户角色
            List<TeamPersonnelInfoDto> dtoList = redisData.getDtoList();
            for(int i=0;i<newDtoList.size();i++){
                TeamPersonnelInfoDto dto=dtoList.get(i);
                TeamPersonnelInfoDto1 newDto=newDtoList.get(i);
                if(dto.getMemberId().intValue()==newDto.getMemberId().intValue()){
                    dto.setTeamRoleId(newDto.getTeamRoleId());
                    dto.setTeamRoleName(newDto.getTeamRoleName());
                    dto.setTeamRoleColor(newDto.getTeamRoleColor());
                    //更新map
                    String channelId = dto.getChannelId().toString();
                    TeamPersonnelInfoDto mapPerson = personMap.get(channelId);
                    mapPerson.setTeamRoleId(newDto.getTeamRoleId());
                    mapPerson.setTeamRoleName(newDto.getTeamRoleName());
                    mapPerson.setTeamRoleColor(newDto.getTeamRoleColor());
                    TbTeamPersonnelInfoEntity entity=new TbTeamPersonnelInfoEntity();
                    BeanUtils.copyProperties(dto,entity);
                    updList.add(entity);
                }
            }
            ridingTeamManageService.bachUpdate(updList);//批量更新
            setRedisTeamInfo(requestData.getTeamCode(),redisData);//更新redis
            NettyJsonBodyObj respBodyObj=new NettyJsonBodyObj();
            respBodyObj.setRedisData(redisData);
            respStr=pageSocketReturnSuccessData(requestData,respBodyObj);
            //推送数据
            sendTeamOtherPerson(requestData,redisData.getPersonMap(),respStr);
        }
        return regUtil.isNull(respStr)?pageSocketReturnSuccessData(requestData,DEFAULTBODY):respStr;
    }

    /**
     * 解散队伍
     * @param ctx
     * @param requestData
     * @return
     */
    public String dismissTeam(ChannelHandlerContext ctx,NettyJsonObj requestData) throws Exception{
        CreateTeamInfoDto redisData = getRedisTeamInfo(requestData.getTeamCode());
        TbCreateTeamInfoEntity newEntity = ridingTeamManageService.queryEntity(new TbCreateTeamInfoEntity(), redisData.getId() + "");
        if(newEntity!=null){
            newEntity.setStatus("0");
            ridingTeamManageService.updateEntity(newEntity);//先更新  再清除redis数据
            setRedisTeamInfo(requestData.getTeamCode(),null);//清空队伍信息
            golTeamInfoMap.remove(requestData.getTeamCode());
            //推送数据给所有人  队伍解散
            String respStr1=pageSocketReturnSuccessData(requestData,DEFAULTBODY);
            sendTeamOtherPerson(requestData,redisData.getPersonMap(),respStr1);//推送加入队伍骑手信息给其他人 并清除数据
        }
        return pageSocketReturnSuccessData(requestData,DEFAULTBODY);
    }

    /**
     * 队伍导航
     * @param ctx
     * @param requestData
     * @return
     */
    public String teamerDriveNavi(ChannelHandlerContext ctx,NettyJsonObj requestData) throws Exception{
        CreateTeamInfoDto redisData = getRedisTeamInfo(requestData.getTeamCode());
        redisData.setNavigationPoint(requestData.getBody().getNavigationPoint());
        sendTeamOtherPerson(requestData,redisData.getPersonMap(),null);
        log.info(">>>>>>>>>>>>teamerDriveNavi===end  person:"+redisData.getPersonMap());
        setRedisTeamInfo(requestData.getTeamCode(),redisData);//更新redis
        return pageSocketReturnSuccessData(requestData,requestData.getBody());
    }

    /**
     * 队伍取消导航
     * @param ctx
     * @param requestData
     * @return
     */
    public String teamerCancelDriveNavi(ChannelHandlerContext ctx,NettyJsonObj requestData) throws Exception{
        CreateTeamInfoDto redisData = getRedisTeamInfo(requestData.getTeamCode());
        redisData.setNavigationPoint(null);
        sendTeamOtherPerson(requestData,redisData.getPersonMap(),null);
        log.info(">>>>>>>>>>>>teamerCancelDriveNavi===end person:"+redisData.getPersonMap());
        setRedisTeamInfo(requestData.getTeamCode(),redisData);//更新redis
        return pageSocketReturnSuccessData(requestData,requestData.getBody());
    }

    /**
     * 更新队伍信息
     * @param ctx
     * @param requestData
     * @return
     */
    public String updateTeamInfo(ChannelHandlerContext ctx,NettyJsonObj requestData) throws Exception{
        /*JSONObject jsonObj=JSONObject.parseObject(requestData.getBody());
        Map<String,Object> map=new HashMap<>();*/
        CreateTeamInfoDto redisData = getRedisTeamInfo(requestData.getTeamCode());
        NettyJsonBodyObj jsonObj = requestData.getBody();
        //JSONObject jsonObj = JSONObject.parseObject(body);
        String teamName = jsonObj.getTeamName();
        String status = jsonObj.getStatus();
        String respStr = "";
        TbCreateTeamInfoEntity newEntity = ridingTeamManageService.queryEntity(new TbCreateTeamInfoEntity(), redisData.getId() + "");
        if(newEntity!=null){
            if("0".equals(status)){//解散队伍
                newEntity.setStatus(status);
                ridingTeamManageService.updateEntity(newEntity);//先更新  再清除redis数据
                setRedisTeamInfo(requestData.getTeamCode(),null);//清空队伍信息
                golTeamInfoMap.remove(requestData.getTeamCode());
                //推送数据给所有人  队伍解散
                requestData.setType(SocketDealType.DISMISSTEAM.getCode());
                /*String respStr1=pageSocketReturnSuccessData(requestData,DEFAULTBODY);
                sendTeamOtherPerson(requestData,redisData.getPersonMap(),respStr1);*///推送加入队伍骑手信息给其他人 并清除数据
            }else{
                log.info(">>>>>>>>>>>update team name!!!: teamName:"+teamName);
                if(!regUtil.isNull(teamName)){
                    newEntity.setTeamName(teamName);
                    redisData.setTeamName(teamName);
                }
                ridingTeamManageService.updateEntity(newEntity);
                setRedisTeamInfo(requestData.getTeamCode(),redisData);//更新redis
            }
            respStr=pageSocketReturnSuccessData(requestData,DEFAULTBODY);
            sendTeamOtherPerson(requestData,redisData.getPersonMap(),respStr);//推送加入队伍骑手信息给其他人 并清除数据
        }else{
            respStr=pageSocketReturnData(requestData,ResultMsg.NOTEXIST_RIDINGTEAM_ERROR,requestData.getBody());
        }
        return respStr;
    }

    /**
     * 清除dtoList队员数据
     * @return
     */
    public String clearListTeamerData(String ids,List<TeamPersonnelInfoDto> dtoList,Map<String, TeamPersonnelInfoDto> personMap){
        log.info(">>>>>>>>>clearListTeamerData end ids:"+ids+"=======dtoList:"+dtoList+"====remove before map:"+personMap);
        String [] idsArr =ids.split(",");
        List<TeamPersonnelInfoDto> list=new ArrayList<TeamPersonnelInfoDto>();
        for(TeamPersonnelInfoDto dto:dtoList){
            for(int i=0;i<idsArr.length;i++){
                if(dto.getMemberId().intValue()==Integer.parseInt(idsArr[i])){
                    list.add(dto);
                    personMap.remove(dto.getChannelId().toString());
                    memberService.setSingValue(dto.getChannelId().toString(),null);//剔除登陆状态
                }
            }
        }
        log.info(">>>>>>>>>>>>>>>>reject teamer begin removeList:"+list);
        ids="";
        for(TeamPersonnelInfoDto dto:list){//移除集合
            ids+=dto.getId()+",";
            dtoList.remove(dto);
        }
        log.info(">>>>>>>>>clearListTeamerData end ids:"+ids+"=======dtoList:"+dtoList+"====remove after map:"+personMap);
        return ids;
    }

    /**
     * 用户连接断开操作
     * @return
     */
    public void connectBreak(){

    }

    /**
     * 心跳
     * @param ctx
     * @param requestData
     * @return
     */
    public String heartbeat(ChannelHandlerContext ctx,NettyJsonObj requestData){
        return pageSocketReturnSuccessData(requestData,DEFAULTBODY);
    }

    /**
     *检查队伍中是否已存在改加入队员信息  如果存在则删除
     */
    public int checkTeamExistPerson(NettyJsonObj requestData,CreateTeamInfoDto redisData){
        int result=0;
        if(redisData!=null){
            result=redisData.getDtoList().size();
            TeamPersonnelInfoDto existPerson=null;
            List<TeamPersonnelInfoDto> dtoList = redisData.getDtoList();
            for(int i=0;i<dtoList.size();i++){
                TeamPersonnelInfoDto dto=dtoList.get(i);
                if(dto.getMemberId().intValue()==requestData.getUserId().intValue()){//如果存在则先移除
                    existPerson=dto;
                    result=i;
                    break;
                }
            }
            if(existPerson!=null){
                dtoList.remove(existPerson);//移除集合里面数据
                redisData.getPersonMap().remove(existPerson.getChannelId().toString());//移除map里面的数据
                memberService.setSingValue(existPerson.getChannelId().toString(),null);//剔除登陆状态
            }
        }
        log.info(">>>>>>>>>checkTeamExistPerson end!! result:"+result);
        return result;
    }

    /**
     * 统一获取队伍信息
     */
    public CreateTeamInfoDto getTeamInfo(ChannelHandlerContext ctx,NettyJsonObj requestData){
        String teamCode = checkSocketLoginIn(ctx);
        if(regUtil.isNull(requestData.getTeamCode())){
            requestData.setTeamCode(teamCode);
        }
        CreateTeamInfoDto redisData = getRedisTeamInfo(requestData.getTeamCode());
        return redisData;
    }


    /**
     * 给队伍其他人发送数据
     */
    public void sendTeamOtherPerson(NettyJsonObj requestData,Map<String, TeamPersonnelInfoDto> personMap,String respStr) throws Exception{
        log.info(">>>>>>>>>>>>sendTeamOtherPerson requestData:"+requestData+"========personMap:"+personMap+"============respStr:"+respStr);
        Set<String> keys = personMap.keySet();
        if(regUtil.isNull(respStr)){
            respStr=pageSocketReturnSuccessData(requestData,requestData.getBody());
        }
        for(String key:keys){
            TeamPersonnelInfoDto value=personMap.get(key);
            Channel channel = GlobalUserUtil.channels.find(value.getChannelId());
            if(channel==null){//连接已断开
                continue;
            }
            if(value.getMemberId().intValue()!=requestData.getUserId().intValue()){
                //requestData.setUserId(value.getMemberId());
                log.info(">>>>>>>>>>>>>>>>>>>sendTeamerPointInfo foreach!!!.respStr:"+respStr+"==========channelId:"+channel+"========valueChannel:"+value.getChannelId());
                writeAndFlushData(channel,respStr);
                if(SocketDealType.DISMISSTEAM.getCode().equals(requestData.getType())){//解散队伍 清除数据 关闭连接
                    memberService.setSingValue(key,null);//剔除登陆状态
                    GlobalUserUtil.channels.remove(channel);
                    //channel.close().sync();
                }
                //channel.writeAndFlush(respStr);
            }else{
                if(SocketDealType.LEAVETEAM.equals(requestData.getType())||SocketDealType.OFFLINE.equals(requestData.getType())||SocketDealType.DISMISSTEAM.equals(requestData.getType())){
                    writeAndFlushData(channel,respStr);
                    GlobalUserUtil.channels.remove(channel);
                    //channel.close().sync();
                }
            }
            if(SocketDealType.REJECTTEAM.getCode().equals(requestData.getType())){//踢出队伍 关闭连接
                String [] idsArr =requestData.getBody().getUserIds().split(",");
                for(int i=0;i<idsArr.length;i++) {
                    if (value.getMemberId().intValue() == Integer.parseInt(idsArr[i])) {
                        GlobalUserUtil.channels.remove(channel);
                        //channel.close().sync();
                    }
                }
            }
        }
    }


    /**
     * 公共发送数据
     */
    public void writeAndFlushData(Channel ctx,String respStr){
        log.info(">>>>>>>=====>>>>>writeAndFlushData response data ...ctx:"+ctx.id()+"=====respStr:"+respStr);
        ByteBuf wbuf = ctx.alloc().buffer();
        wbuf.writeBytes((respStrPakageDeal(respStr)).getBytes());
        ctx.writeAndFlush(wbuf);
    }

    /**
     * 公共封装数据
     * @param respStr
     * @return
     */
    public String respStrPakageDeal(String respStr){
        respStr=respStr+"\r\n";
        log.info(">>>>>>>====respStrPakageDeal=>>>>====respStr:"+respStr);
        return respStr;
    }

    /**
     *查询有效队伍
     */
    public TbCreateTeamInfoEntity queryTeamInfo(String teamCode){
        log.info(">>>>>>>>>>>queryTeamInfo!!!! teamCode:"+teamCode);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("teamCode",teamCode);
        String whereSql=" valid_end_time>sysdate and team_code=:teamCode and status=1 ";
        List<TbCreateTeamInfoEntity> list = ridingTeamManageService.queryEntity(new TbCreateTeamInfoEntity(),whereSql,map);
        return list!=null&&list.size()>0?list.get(0):null;
    }

    /*public static void main(String [] arg){
        String dealStr="{\"teamId\":80,\"userId\":\"79\",\"user_mid\":0,\"isLoading\":false,\"sendTime\":1561177280394,\"version\":0,\"type\":1000,\"reqId\":\"156117728039432224221\",\"body\":\"{\\\"token\\\":\\\"bf0ea8ce-0dac-430d-a5c5-34a60c00b1d4\\\"}\",\"teamCode\":\"690171\"}";
        System.out.println(">>>>>>>>dealStr:"+dealStr.startsWith("{")+"=========="+dealStr.endsWith("}"));
        NettyJsonObj dto=(NettyJsonObj)GsonUtil.jsonToDto(dealStr, NettyJsonObj.class);
        System.out.println(">>>>>>>>dto:"+dto);
    }*/
}
