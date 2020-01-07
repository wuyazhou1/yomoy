package com.nsc.Amoski.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.dto.RidingInfoDto;
import com.nsc.Amoski.dto.TbRidingGuideInfoDto;
import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.util.StringUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ridingManage")
public class RidingManageController extends RidingServerBaseController<RidingManageController> {

    /**
     * 查询用户骑行总计
     * @return  骑行总计数据
     */

    @RequestMapping(value="/queryUserRidingInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询用户骑行总计", notes = "查询用户骑行总计", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="queryType",value="查询方式(不传:半年,1:1年)",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="ridingVehicleId",value="查询车辆id",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="areaRank",value="是否查询排名(1:查询 否则不查询)",dataType="String", paramType = "query"),
    })
    public Result queryUserRidingInfo(HttpServletResponse response, HttpServletRequest request,@RequestBody RidingInfoDto rdto){
        log.info(">>>>>>>>>>>>> queryUserRidingInfo .requestParam param!!! dto:"+rdto);
        MemberView dto = getRedisUserInfo(request);
        rdto.setMemberId(dto.getMemberId());
        rdto.setStartPosition(dto.getAddress());
        RidingInfoDto result = ridingManageService.queryRidingInfo(rdto);
        if("1".equals(rdto.getAreaRank())&&result!=null){
            RidingInfoDto newDto = ridingManageService.queryUserRidingRank(rdto);
            result.setAreaRank(newDto.getAreaRank());
            result.setCountryRank(newDto.getCountryRank());
            List<RidingInfoDto> list = ridingManageService.queryUserRidingCountByMonth(rdto);
            result.setList(list);
        }
        //查询轨迹图地址
        //querySingleUserImg(result);
        return success(result);
    }

    /**
     * 查询用户骑行次数统计
     * @return  查询用户骑行次数统计
     */

    @RequestMapping(value="/queryUserRidingCountByMonth",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询用户骑行次数统计", notes = "查询用户骑行次数统计", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="queryType",value="查询方式(不传:半年,1:1年)",dataType="String", paramType = "query"),
    })
    public Result queryUserRidingCountByMonth(HttpServletResponse response, HttpServletRequest request,@RequestBody RidingInfoDto rdto){
        log.info(">>>>>>>>>>>>> queryUserRidingCountByMonth .requestParam param!!!  rdto:"+rdto);
        MemberView dto = getRedisUserInfo(request);
        rdto.setMemberId(dto.getMemberId());
        List<RidingInfoDto> list = ridingManageService.queryUserRidingCountByMonth(rdto);
        //查询轨迹图地址
        //querySingleUserImg(result);
        return success(list);
    }


    /**
     * 查询最远距离最多次数本人骑行数据统计
     * @return  查询最远距离最多次数本人骑行数据统计
     */

    @RequestMapping(value="/queryUserDisCountRidingInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询最远距离最多次数本人骑行数据统计", notes = "查询最远距离最多次数本人骑行数据统计", httpMethod = "POST" )
    public Result queryUserDisCountRidingInfo(HttpServletResponse response, HttpServletRequest request,@RequestBody RidingInfoDto rdto){
        log.info(">>>>>>>>>>>>> queryUserDisCountRidingInfo .requestParam param!!! dto:"+rdto);
        MemberView dto = getRedisUserInfo(request);
        rdto.setMemberId(dto.getMemberId());
        List<RidingInfoDto> list = ridingManageService.queryUserDisCountRidingInfo(rdto);
        Map<String,Object> map=new HashMap<String, Object>();
        //查询骑行组队信息
        log.info(">>>>>>>>>>>>>>..queryTeamInfoAndPersonInfo !!!");
        CreateTeamInfoDto newEntity=null;
        TeamPersonnelInfoDto pEntity=new TeamPersonnelInfoDto();
        pEntity.setMemberId(dto.getMemberId());
        List<TbCreateTeamInfoEntity> teamlist = ridingManageService.queryRidingTeamInfo(pEntity);
        if(teamlist!=null&&teamlist.size()>0){//如果存在
            newEntity=new CreateTeamInfoDto();
            BeanUtils.copyProperties(teamlist.get(0),newEntity);
            String whereSql=" team_id=:teamId ";
            map.clear();
            map.put("teamId",newEntity.getId());
            List<TbTeamPersonnelInfoEntity> pList = ridingManageService.queryEntity(new TbTeamPersonnelInfoEntity(), whereSql, map);
            newEntity.setList(pList);
        }
        if(newEntity!=null){
            map.put("ridingTeam",newEntity);//骑行队伍信息
        }
        //查询骑行路书数据
        TbRidingGuideInfoDto guideInfoDto=new TbRidingGuideInfoDto();
        guideInfoDto.setOrderByType("1");
        guideInfoDto.setLimit(2);
        guideInfoDto.setPage(1);
        PagingBean pagingBean = ridingManageService.queryDistanceGuide(guideInfoDto);
        TbRidingGuideInfoDto activityEntity=new TbRidingGuideInfoDto();
        activityEntity.setPage(1);
        activityEntity.setLimit(5);
        //activityEntity.set
        Result result = activityServerApi.queryActivityList(activityEntity);
        JSONObject jsonObj=JSONObject.parseObject(JSONObject.toJSONString(result.getData()));
        JSONArray arr = jsonObj.getJSONArray("data");
        //JSONArray.parseArray(JSONArray.toJSONString(result.getData()));//活动
        //JSONArray arr1 = JSONArray.parseArray(JSONObject.toJSONString(pagingBean.getData()));//路书
        List<Map<String,Object>> dlist=new ArrayList<Map<String, Object>>();
        //List<Map<String,Object>> dlist1=new ArrayList<Map<String, Object>>();
        for(int i =0;i<arr.size();i++){
            Map<String,Object> dmap=new HashMap<String, Object>();
            JSONObject jsonObject = arr.getJSONObject(i);
            dmap.put("id",jsonObject.getString("code"));
            dmap.put("title",jsonObject.getString("title"));
            dmap.put("totalVisits",jsonObject.getString("totalVisits"));
            dmap.put("fileNameUrl",jsonObject.getString("fileNameUrl"));
            dmap.put("collectionPlace",jsonObject.getString("collectionPlace"));
            dmap.put("activityStart",jsonObject.getString("activityStart"));
            dmap.put("activityStop",jsonObject.getString("activityStop"));
            dlist.add(dmap);
        }
        /*for(int i =0;i<arr1.size();i++){
            Map<String,Object> dmap=new HashMap<String, Object>();
            JSONObject jsonObject = arr.getJSONObject(i);
            dmap.put("",jsonObject.getString(""));
            dmap.put("",jsonObject.getString(""));
            dmap.put("",jsonObject.getString(""));
            dmap.put("",jsonObject.getString(""));
            dmap.put("",jsonObject.getString(""));
            dlist1.add(dmap);
        }*/
        /*map.put("masterDis",masterDis);//本月骑行最远距离
        map.put("masterCount",masterCount);//本月骑行最多次数
        map.put("selfAllDis",selfAllDis);//个人骑行总距离
        map.put("selfDis",selfDis);//个人骑行本月距离
        map.put("selfCount",selfCount);//个人骑行本月次数*/
        map.put("ridingData",list);//骑行首页数据
        map.put("ridingGuide",pagingBean.getData());//骑行路书
        map.put("activityData",dlist);
        //查询轨迹图地址
        //querySingleUserImg(result);
        return success(map);
    }

    /**
     * 查询单次骑行详情数据或按查询某月骑行记录详细数据
     */
    @RequestMapping(value="/querySingRidingDetailInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询单次骑行详情数据或按查询某月骑行记录详细数据", notes = "查询单次骑行详情数据或按查询某月骑行记录详细数据", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="骑行数据id",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="queryDate",value="查询日期(格式yyyy-mm)",dataType="String", paramType = "query")
    })
    public Result querySingRidingDetailInfo(HttpServletRequest request,@RequestBody RidingInfoDto rdto){
        log.info(">>>>>>>>>>>>> querySingRidingDetailInfo .requestParam param!!!  rdto:"+rdto);
        if(regUtil.isNull(rdto.getId())&&regUtil.isNull(rdto.getQueryDate())){//id为空
            return error(ResultMsg.IS_NULL);
        }
        MemberView dto = getRedisUserInfo(request);
        rdto.setMemberId(dto.getMemberId());
        List<RidingInfoDto> result = ridingManageService.queryAllRidingInfo(rdto);
        RidingInfoDto resultDto = ridingManageService.queryRidingInfo(rdto);//骑行总计
        Map<String,Object> resultMap=new HashMap<String, Object>();
        resultMap.put("result",result);
        resultMap.put("resultDto",resultDto);
        //查询轨迹图地址
        //querySingleUserImg(result);
        return success(resultMap);
    }

    /**
     * 骑行开始记录数据
     */
    @RequestMapping(value="/recordRidingData",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="骑行开始记录数据", notes = "骑行开始记录数据", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="ridingVehicleId",value="查询车辆id",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="startPosition",value="起点",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="passPosition",value="途径点(多个,分隔) 可为空",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="endPosition",value="终点",dataType="String", paramType = "query")
    })
    public Result recordRidingData(HttpServletRequest request,@RequestBody RidingInfoDto rdto){
        log.info(">>>>>>>>>>>>> recordRidingData .requestParam param!!!  rdto:"+rdto);
        //数据校验
        /*if(regUtil.isNull(rdto.getStartPosition(),rdto.getEndPosition())){//骑行开始结束点为空
            return error(ResultMsg.IS_NULL);
        }*/
        MemberView dto = getRedisUserInfo(request);
        rdto.setMemberId(dto.getMemberId());
        Map<String,Object> map=new HashMap<String, Object>();
        //查询用户是否默认车辆
        //ridingManageService.queryEntity(new TbVehicleInfoEntity()," and IS_DEFAULT=1 and STATUS=1 and memberId=:userId ",map);
        /*TbVehicleInfoEntity vehicleEntity = ridingManageService.queryEntity(new TbVehicleInfoEntity(), rdto.getRidingVehicleId(),"amoski_activity.");

        if(vehicleEntity!=null){*/
        TbRidingInfoEntity entity=new TbRidingInfoEntity();//骑行
        entity.setMemberId(dto.getMemberId());
        entity.setCreateUser(dto.getLoginname());
        entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        entity.setRidingVehicleId(rdto.getRidingVehicleId());
        /*TbRidingInfoDetailEntity entityDetail=new TbRidingInfoDetailEntity();//骑行详情
        ridingDtoChangeTo(rdto,entity);
        ridingDtoChangeTo(rdto,entityDetail);
        ridingManageService.addInitRidingData(entity,entityDetail);*/
        ridingManageService.addEntity(entity,true);
       /* }else{
            return error(ResultMsg.RIDING_VECHICLE_NOTEXIST);
        }*/
        return success(entity);
    }

    /**
     * 骑行上传文件接口
     */
    @RequestMapping(value="/ridingFileUpd",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="骑行上传文件接口", notes = "骑行上传文件接口", httpMethod = "POST" )
    public Result ridingFileUpd(HttpServletRequest request, MultipartFile file,String rid) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...ridingFileUpd!! filelength:"+file+">>>>>>>>>>>>>>>>>>rid:"+rid);
        if(file==null||regUtil.isNull(rid)){
            return error(ResultMsg.IS_NULL);
        }

        MemberView userDto=getRedisUserInfo(request);
        String baseUrl =request.getServletContext().getRealPath("/upload");
        TbRidingInfoEntity entity=new TbRidingInfoEntity();
        InputStream in = file.getInputStream();
        Result rs = uploadFile(file, baseUrl, "/ridingDataFile",userDto);//上传文件
        if(ResultMsg.SUCCESS.getCode().equals(rs.getCode())){//上传成功
            byte [] bt=new byte[in.available()];
            int n=0;
            StringBuffer json=new StringBuffer("");
            while((n=in.read(bt))!=-1){
                json.append(new String(bt));
                log.info(">>>>>>>>>>>>>>>>>byte length!!!!>>>>>>>>>>>>>json:"+json);
            }
            JSONObject jobj= JSON.parseObject(json.toString());
            String minkm=objChangeToStr(jobj.get("minkm"));//配速
            String totalTime=objChangeToStr(jobj.get("totalTime"));//累计用时
            //String elevationArray=objChangeToStr(jobj.get("elevationArray"));//海拔数据
            //String locationArray=objChangeToStr(jobj.get("locationArray"));//点数据
            //String weatherArray=objChangeToStr(jobj.get("weatherArray"));//天气数据
            String climbing=objChangeToStr(jobj.get("climbing"));//爬坡
            String topspeed=objChangeToStr(jobj.get("topspeed"));//极速
            String metre100Sprint=objChangeToStr(jobj.get("metre100Sprint"));//百米加速
            String mileage=objChangeToStr(jobj.get("mileage"));//里程
            //String dateTime=objChangeToStr(jobj.get("dateTime"));//时间
            String bendAngle=objChangeToStr(jobj.get("bendAngle"));//压弯
            //保存到数据库
            TbRidingInfoEntity newEntity = ridingManageService.queryEntity(entity, rid);
            Object data = rs.getData();
            JSONObject jsonObj = JSONObject.parseObject(JSONObject.toJSONString(data));
            String filePath = jsonObj.get("filePath").toString();
            String smallImgPath = objChangeToStr(jsonObj.get("smallImgPath"));
            baseUrl=StringUtils.getFilePath();
            if(newEntity!=null){//保存数据
                newEntity.setUpdateUser(userDto.getLoginname());
                newEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                newEntity.setTotalTime(totalTime);
                newEntity.setRidingFileUrl(filePath);
                newEntity.setBaseUrl(baseUrl);
                newEntity.setMemberId(userDto.getMemberId());
                newEntity.setTotalDistance(Double.parseDouble(mileage));
                newEntity.setAverageSpeed(minkm);
                //ridingManageService.updateEntity(newEntity);
                //骑行详情数据

                TbRidingInfoDetailEntity detailEntity=new TbRidingInfoDetailEntity();
                detailEntity.setRidingId(newEntity.getId());
                detailEntity.setClimbHeight(regUtil.isNull(climbing)?0:Double.parseDouble(climbing));
                detailEntity.setMaxAcceleratedSpeed(regUtil.isNull(metre100Sprint)?0:Double.parseDouble(metre100Sprint));
                detailEntity.setMaxSpeed(regUtil.isNull(topspeed)?0:Double.parseDouble(topspeed));
                detailEntity.setRidingBend(regUtil.isNull(bendAngle)?0:Double.parseDouble(bendAngle));
                //ridingManageService.addEntity(detailEntity,false);
                ridingManageService.updateAllRidingData(newEntity,detailEntity);
            }else{
                return error(ResultMsg.IS_NULL);
            }
            //写库
        }
        //ridingManageService.updateRidingInfo(entity);
        return success(rs);
    }

    /**
     * 骑行下载文件接口
     */
    @RequestMapping(value="/ridingFileDown",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="骑行下载文件接口", notes = "骑行下载文件接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="fileUrl",value="文件路径",dataType="String", paramType = "query"),
    })
    public void ridingFileDown(HttpServletRequest request,HttpServletResponse response, RidingInfoDto rdto) throws Exception{
        log.info(">>>>>>>>>>>>>>...ridingFileDown!! filelength:"+rdto+">>>>>>>>>>>>>>>>>>>>>>>>>>");
        /*if(file==null){
            return error(ResultMsg.IS_NULL);
        }*/
        //文件路径错误
        if(regUtil.isNull(rdto.getFileUrl())){
            return ;
        }
        MemberView userDto=getRedisUserInfo(request);
        FileInputStream in = regUtil.getFileStream(rdto.getFileUrl(), null);
        byte [] by=new byte[in.available()];
        int n=0;
        while((n=in.read(by))!=-1){
            response.getOutputStream().write(by);
            log.info(">>>>>>>>>>>>>>>>>byte length!!!!"+by.length+">>>>>>>>>>>>>");
        }
        if(in!=null){
            in.close();
        }
    }

    /**
     * 上传路书数据
     */
    @RequestMapping(value="/updRoadBookData",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="上传路书数据", notes = "上传路书数据", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="imgId",value="路书图片id(多个用,隔开)",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="ridingDesc",value="描述",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="lgt",value="经度",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="lat",value="纬度",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="ridingId",value="骑行id",dataType="String", paramType = "query")
    })
    public Result updRoadBookData(HttpServletRequest request,@RequestBody TbRidingTravelPlanEntity entity) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...updRoadBookData!! >>>>>>>>>>>>>entity:"+entity);
        if(regUtil.isNull(entity.getLgt(),entity.getLat(),entity.getRidingId())){
            return error(ResultMsg.IS_NULL);
        }
        MemberView userDto=getRedisUserInfo(request);
        entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        entity.setCreateUser(userDto.getId()+"");
        ridingManageService.addRidingTravelPlan(entity);
        return success();
    }

    /**
     * 骑行结束轨迹截图
     */
    @RequestMapping(value="/uploadRidingPic",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="骑行结束轨迹截图", notes = "骑行结束轨迹截图", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="rid",value="当前骑行数据id",dataType="String", paramType = "query"),
    })
    public Result uploadRidingPic(HttpServletRequest request,MultipartFile [] files,String rid) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...uploadRidingPic!! >>>>>>>>>>>>>entity:"+files.length);
        if(files==null||regUtil.isNull(rid)){
            return error(ResultMsg.IS_NULL);
        }
        MemberView userDto=getRedisUserInfo(request);
        String baseUrl =request.getServletContext().getRealPath("/upload");
        TbRidingInfoEntity entity=new TbRidingInfoEntity();
        Map<String,String> map=new HashMap<String, String>();
        String startStr="";
        boolean bl=true;
        for(int i=0;i<files.length;i++){//多次上传
            MultipartFile file=files[i];
            log.info("controller==============name:"+file.getOriginalFilename()+"===============byte:"+file.getSize()+"====file:");
            startStr=file.getOriginalFilename().split("_")[0];
            Result rs1 = uploadFile(file, baseUrl, "/RidingOverPic"+startStr,userDto);//上传文件
            if(ResultMsg.SUCCESS.getCode().equals(rs1.getCode())) {//上传成功
                //保存到数据库
                if(i==0){
                    entity = ridingManageService.queryEntity(entity, rid);
                }
                Object data = rs1.getData();
                JSONObject jsonObj = JSONObject.parseObject(JSONObject.toJSONString(data));
                String filePath = jsonObj.get("filePath").toString();
                String smallImgPath = jsonObj.get("smallImgPath").toString();
                map.put(startStr+"filePath",filePath);
                baseUrl=StringUtils.getFilePath();
                if(entity!=null) {//保存数据
                    if("little".equals(startStr)){
                        entity.setTrackImgUrl(filePath);
                    }else if("big".equals(startStr)){
                        entity.setRidingEndBackgroudImg(filePath);
                    }
                    log.info(">>>>>>>>>>>>>>>>>>>>startStr:"+startStr+">>>>>>>>>>filePath:"+filePath);
                }
            }else{
                bl=false;
                break;
            }
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>upload result:"+bl);
        if(bl){//上传都成功
            ridingManageService.updateEntity(entity);
        }else{
            return error(ResultMsg.FAIL,startStr);
        }
        return success(map);
    }

    /**
     * 上传骑行路途的照片
     */
    @RequestMapping(value="/uploadRidingPhotoPic",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="上传骑行路途的照片", notes = "上传骑行路途的照片", httpMethod = "POST" )
    public Result uploadRidingPhotoPic(HttpServletRequest request,MultipartFile file) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...uploadRidingPhotoPic!! >>>>>>>>>>>>>entity:"+file);
        MemberView userDto=getRedisUserInfo(request);
        String baseUrl =request.getServletContext().getRealPath("/upload");
        Result rs = uploadFile(file, baseUrl, "/userRidingPhotoPic",userDto);//上传文件
        return rs;
    }

    /**
     * 获取猎鹰服务id
     */
    @RequestMapping(value="/getServerId",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="获取猎鹰服务id", notes = "获取猎鹰服务id", httpMethod = "POST" )
    public Result getServerId(HttpServletRequest request) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...getServerId!! >>>>>>>>>>>>>");
        MapServerDataDto server = createFalconServer();
        if(server!=null&&!regUtil.isNull(server.getServerId())){
            MemberView userDto=getRedisUserInfo(request);
            TerminalDataDto termina = createTermina(userDto, server.getServerId());
            if(termina!=null){
                server.setTerminalId(termina.getTerminalId());
                server.setTerminalName(termina.getTerminalName());
                server.setTerminalDesc(termina.getTerminalDesc());
            }
        }
        return success(server);
    }

    /**
     * 获取用户终端id或者创建终端id
     */
    @RequestMapping(value="/getUserTerminalId",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="获取用户终端id或者创建终端id", notes = "获取用户终端id或者创建终端id", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="serverId",value="服务id",dataType="String", paramType = "query")
    })
    public Result getUserTerminalId(HttpServletRequest request,@RequestBody Map<String,String> map) throws Exception{
        String serverId=map.get("serverId");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...getUserTerminalId!! >>>>>>>>>>>>>serverId:"+serverId);
        MemberView userDto=getRedisUserInfo(request);
        TerminalDataDto termina = createTermina(userDto, serverId);
        return success(termina);
    }


    /**
     * 骑行结束异常 检测
     */
    @RequestMapping(value="/ridingOverExceptionCheck",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="骑行结束异常 检测", notes = "骑行结束异常 检测", httpMethod = "POST" )
    public Result ridingOverExceptionCheck(HttpServletRequest request) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...ridingOverExceptionCheck!! >>>>>>>>>>>>>");
        MemberView userDto=getRedisUserInfo(request);
        String baseUrl =request.getServletContext().getRealPath("/upload");
        return success();
    }
}
