package com.nsc.Amoski.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.dto.RidingInfoDto;
import com.nsc.Amoski.dto.TbGuideRouteInfoDto;
import com.nsc.Amoski.dto.TbRidingGuideInfoDto;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.util.DateUtils;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ridingGuideManage")
public class RidingGuideManageController extends RidingServerBaseController<RidingGuideManageController> {


    /**
     * 查询路书列表
     * @return  查询路书列表
     */

    @RequestMapping(value="/queryGuideList",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询路书列表", notes = "查询路书列表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="id(查询单个路书数据)",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="strengthgrade",value="强度等级",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="scenerygrad",value="风景等级",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="title",value="路书标题",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="status",value="状态(1.草稿 2.已发布 3.已删除)",dataType="String", paramType = "query"),
    })
    public Result queryGuideList(HttpServletResponse response, HttpServletRequest request, TbRidingGuideInfoDto dto){
        log.info(">>>>>>>>>>>>> queryGuideList .requestParam param!!!  dto:"+dto);
        PagingBean pBean = ridingManageService.queryAllTravelGuideInfo(dto);
        return success(pBean);
    }
    /**
     * 查询单条路书信息
     * @return  查询单条路书信息
     */

    @RequestMapping(value="/querySingGuide",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询单条路书信息", notes = "查询单条路书信息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="id(查询单个路书数据)",dataType="String", paramType = "query"),
    })
    public Result querySingGuide(HttpServletResponse response, HttpServletRequest request, TbRidingGuideInfoDto dto){
        log.info(">>>>>>>>>>>>> querySingGuide .requestParam param!!!  dto:"+dto.getId());
        if(regUtil.isNull(dto.getId())){
            return error(ResultMsg.IS_NULL);
        }
        TbRidingGuideInfoEntity entity = ridingManageService.queryEntity(new TbRidingGuideInfoEntity(), dto.getId().toString());
        return success(entity);
    }

    /**
     * 查询路书所有线路及线路所有途径点
     * @return  查询路书所有线路及线路所有途径点
     */

    @RequestMapping(value="/queryRouteList",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询路书所有线路及线路所有途径点", notes = "查询路书所有线路及线路所有途径点", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="id(查询单个路书数据)",dataType="String", paramType = "query"),
    })
    public Result queryRouteList(HttpServletResponse response, HttpServletRequest request, TbGuideRouteInfoDto dto){
        log.info(">>>>>>>>>>>>> queryRouteList .requestParam param!!!  dto:"+dto);
        if(regUtil.isNull(dto.getGuideId())){
            return error(ResultMsg.IS_NULL);
        }
        List<TbGuideRouteInfoDto> list = ridingManageService.queryGuideRouteInfo(dto,1);
        List<TbGuideRouteInfoDto> newList =new ArrayList<TbGuideRouteInfoDto>();
        Map<String,TbGuideRouteInfoDto> map=new HashMap<String, TbGuideRouteInfoDto>();//用来判断是否存在
        //处理数据
        for(TbGuideRouteInfoDto lDto:list){
            TbRoutePointInfoEntity point=new TbRoutePointInfoEntity();
            BeanUtils.copyProperties(lDto,point);
            point.setAboutdis(lDto.getPAboutdis());
            point.setId(lDto.getPId());
            point.setRidingtime(lDto.getPRidingtime());
            point.setRouteId(lDto.getId());
            /*point.setRemake(lDto.getRemake());
            point.setRouteIcon(lDto.getRouteIcon());
            point.setType(lDto.getType());
            point.setRouteName(lDto.getRouteName());
            point.setLat(lDto.getLat());
            point.setLng(lDto.getLng());
            point.setLineIntroduction(lDto.getLineIntroduction());
            point.setImgUrl1(lDto.getImgUrl1());
            point.setImgUrl2(lDto.getImgUrl2());
            point.setImgUrl3(lDto.getImgUrl3());
            point.setImgUrl(lDto.getImgUrl());
            point.setBaseUrl(lDto.getBaseUrl());
            point.setAddress(lDto.getAddress());*/
            if(!map.containsKey(lDto.getId().toString())){
                map.put(lDto.getId().toString(),lDto);
                newList.add(lDto);
            }
            if(!regUtil.isNull(point.getLat(),point.getLng(),point.getRouteName())){
                map.get(lDto.getId().toString()).getPointList().add(point);
            }
        }
        return success(newList);
    }

    /**
     * 新增路书数据
     */
    @RequestMapping(value="/addGuideData",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="新增路书数据", notes = "新增路书数据", httpMethod = "POST" )
    public Result addGuideData(HttpServletRequest request,TbRidingGuideInfoEntity entity) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...addGuideData!! >>>>>>>>>>>>>entity:"+entity);
        entity.setStatus(1);//草稿
        entity.setLookcount(0);//浏览量
        entity.setRidingtime("0");
        entity.setRoutepointcount(0);//途径点数量
        entity.setGuideType("1");
        entity.setCreateTime(DateUtils.getCurrentStamp());
        entity.setCreateUser("system");
        ridingManageService.addEntity(entity,true);
        return success(entity);
    }

    /**
     * 修改路书单数据
     */
    @RequestMapping(value="/updGuideStatus",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="修改路书状态(0.删除 1.草稿 2.发布)", notes = "修改路书状态(0.删除 1.草稿 2.发布)", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="id(修改路书id)",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="status",value="状态(0.删除 1.草稿 2.发布)",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="lookcount",value="浏览量",dataType="String", paramType = "query"),
    })
    public Result updGuideStatus(HttpServletRequest request, TbRidingGuideInfoEntity entity) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...updGuideStatus!! >>>>>>>>>>>>>entity:"+entity);
        if(regUtil.isNull(entity.getId())){
            return error(ResultMsg.IS_NULL);
        }
        TbRidingGuideInfoEntity newEntity = ridingManageService.queryEntity(new TbRidingGuideInfoEntity(), entity.getId().toString());
        if(newEntity!=null){
            if(entity.getStatus()>=0&&entity.getStatus()<=2){
                newEntity.setStatus(entity.getStatus());
            }
            if(!regUtil.isNull(entity.getLookcount())){//优化先放redis 然后一次更新
                newEntity.setLookcount(newEntity.getLookcount()+1);
            }
            ridingManageService.updateEntity(newEntity);
        }
        return success(newEntity);
    }


    /**
     * 修改路书所有数据
     */
    @RequestMapping(value="/updGuideData",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="修改路书数据", notes = "修改路书数据", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="id(修改路书id)",dataType="String", paramType = "query"),
    })
    public Result updGuideData(HttpServletRequest request, TbRidingGuideInfoEntity entity) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...updGuideData!! >>>>>>>>>>>>>entity:"+entity);
        if(regUtil.isNull(entity.getId())){
            return error(ResultMsg.IS_NULL);
        }
        TbRidingGuideInfoEntity newEntity = ridingManageService.queryEntity(new TbRidingGuideInfoEntity(), entity.getId().toString());
        if(newEntity!=null){
            entity.setCreateTime(newEntity.getCreateTime());
            ridingManageService.updateEntity(entity);
        }
        return success(newEntity);
    }

    /**
     * 新增或修改路书线路及途经点
     */
    @RequestMapping(value="/addOrUpdGuideRouteData",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="新增或修改路书线路及途经点", notes = "新增或修改路书线路及途经点", httpMethod = "POST" )
    public Result addOrUpdGuideRouteData(HttpServletRequest request, @RequestBody String dto) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...addOrUpdGuideRouteData!! >>>>>>>>>>>>>entity:"+dto);
        if(regUtil.isNull(dto)){
            return error(ResultMsg.IS_NULL);
        }
        ridingManageService.addOrUpdGuideRouteData(dto);
        return success();
    }


    /**
     * 上传骑行路途的照片
     */
    @RequestMapping(value="/uploadRoadImg",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="上传骑行路途的照片", notes = "上传骑行路途的照片", httpMethod = "POST" )
    public Result uploadRoadImg(HttpServletRequest request,MultipartFile file) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...uploadRoadImg!! >>>>>>>>>>>>>entity:"+file);
        MemberView userDto=getRedisUserInfo(request);
        String baseUrl =request.getServletContext().getRealPath("/upload");
        Result rs = uploadFile(file, baseUrl, "/uploadRoadImg",userDto);//上传文件
        return rs;
    }








    //以下接口暂时未使用

    /**
     * 修改路书线路
     */
    @RequestMapping(value="/updGuideRouteData",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="修改路书线路", notes = "修改路书线路", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="id(修改路书线路id)",dataType="String", paramType = "query"),
    })
    public Result updGuideRouteData(HttpServletRequest request,TbRidingGuideInfoEntity entity) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...updGuideRouteData!! >>>>>>>>>>>>>entity:"+entity);
        if(regUtil.isNull(entity.getId())){
            return error(ResultMsg.IS_NULL);
        }
        TbRidingGuideInfoEntity newEntity = ridingManageService.queryEntity(new TbRidingGuideInfoEntity(), entity.getId().toString());
        if(newEntity!=null){
            ridingManageService.updateEntity(entity);
        }
        return success();
    }

    /**
     * 新增线路途经点
     */
    @RequestMapping(value="/addRoutePointData",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="新增线路途经点", notes = "新增线路途经点", httpMethod = "POST" )
    public Result addRoutePointData(HttpServletRequest request,TbRoutePointInfoEntity entity) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...addRoutePointData!! >>>>>>>>>>>>>entity:"+entity);
        if(regUtil.isNull(entity.getRouteId())){
            return error(ResultMsg.IS_NULL);
        }
        TbRidingGuideInfoEntity newEntity = ridingManageService.queryEntity(new TbRidingGuideInfoEntity(), entity.getRouteId().toString());
        if(newEntity!=null){
            ridingManageService.addEntity(entity,true);
        }else{//线路不存在
            return error(ResultMsg.ROUTE_NOTEXIST_ERROR);
        }
        return success(entity);
    }

}
