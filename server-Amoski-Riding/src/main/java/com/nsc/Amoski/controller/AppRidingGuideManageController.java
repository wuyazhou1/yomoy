package com.nsc.Amoski.controller;

import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.dto.TbGuideRouteInfoDto;
import com.nsc.Amoski.dto.TbRidingGuideInfoDto;
import com.nsc.Amoski.dto.TbRoutePointInfoDto;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appRidingGuideManage")
public class AppRidingGuideManageController extends RidingServerBaseController<AppRidingGuideManageController> {

    @RequestMapping(value="/test",method = {RequestMethod.POST,RequestMethod.GET})
    public Result test(HttpServletRequest request){
        log.info(">>>>>>>>>>>>> test .requestParam param!!!  rdto:");
        TbRidingGuideInfoDto activityEntity=new TbRidingGuideInfoDto();
        activityEntity.setPage(1);
        activityEntity.setLimit(5);
        //activityEntity.set
        Result result = activityServerApi.queryActivityList(activityEntity);
        return result;
    }

    /**
     * 查询路书列表
     * @return  查询路书列表
     */

    @RequestMapping(value="/queryGuideList",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询路书列表", notes = "查询路书列表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="searchVal",value="搜索字段",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="orderByType",value="排序类型(1.热门(第一条件浏览量排序（多>少），第二条件发布时间排序（新>老)" +
                    " 2.附近 (第一条件距离段（三个距离段:0-50/50-100/100-不限），第二条件浏览量（多>少）))",dataType="String", paramType = "query"),
    })
    public Result queryGuideList(HttpServletResponse response, HttpServletRequest request, @RequestBody TbRidingGuideInfoDto dto){
        log.info(">>>>>>>>>>>>> queryGuideList .requestParam param!!!  dto:"+dto);
        PagingBean pagingBean = ridingManageService.queryDistanceGuide(dto);
        return success(pagingBean.getData());
    }

    /**
     * 查询路书所有线路及线路所有途径点
     * @return  查询路书所有线路及线路所有途径点
     */

    @RequestMapping(value="/queryRouteList",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询路书所有线路及线路所有途径点", notes = "查询路书所有线路及线路所有途径点", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="guideId",value="id(查询单个路书数据)",dataType="String", paramType = "query"),
    })
    public Result queryRouteList(HttpServletResponse response, HttpServletRequest request, @RequestBody TbGuideRouteInfoDto dto){
        log.info(">>>>>>>>>>>>> queryRouteList .requestParam param!!!  dto:"+dto);
        if(regUtil.isNull(dto.getGuideId())){
            return error(ResultMsg.IS_NULL);
        }
        List<TbGuideRouteInfoDto> list = ridingManageService.queryGuideRouteInfo(dto,0);
        List<TbGuideRouteInfoDto> newList =new ArrayList<TbGuideRouteInfoDto>();
        Map<String,TbGuideRouteInfoDto> map=new HashMap<String, TbGuideRouteInfoDto>();//用来判断是否存在
        TbRidingGuideInfoEntity ridingEntity = ridingManageService.queryEntity(new TbRidingGuideInfoEntity(), dto.getGuideId() + "");
        ridingEntity.setLookcount(ridingEntity.getLookcount()+1);
        ridingManageService.updateEntity(ridingEntity);
        //处理数据
        for(TbGuideRouteInfoDto lDto:list){
            TbRoutePointInfoEntity point=new TbRoutePointInfoEntity();
            BeanUtils.copyProperties(lDto,point);
            point.setAboutdis(lDto.getPAboutdis());
            point.setId(lDto.getPId());
            point.setRidingtime(lDto.getPRidingtime());
            point.setRouteId(lDto.getId());
            if(!map.containsKey(lDto.getId().toString())){
                /*lDto.setLat(lDto.getStartlat());
                lDto.setLng(lDto.getStartlng());*/
                newList.add(lDto);
                map.put(lDto.getId().toString(),lDto);
            }if(!regUtil.isNull(point.getLat(),point.getLng(),point.getRouteName())){
                map.get(lDto.getId().toString()).getPointList().add(point);
            }
        }
        return success(newList);
    }
    /**
     * 路书图片加载
     */
    @RequestMapping(value="/getImg",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="路书图片加载", notes = "路书图片加载", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="fileUrl",value="文件路径",dataType="String", paramType = "query"),
    })
    public void getImg(HttpServletRequest request,HttpServletResponse response, RidingInfoDto rdto) throws Exception{
        log.info(">>>>>>>>>>>>>>...路书图片加载!! filelength:"+rdto+">>>>>>>>>>>>>>>>>>>>>>>>>>");
        /*if(file==null){
            return error(ResultMsg.IS_NULL);
        }*/
        //文件路径错误
        if(regUtil.isNull(rdto.getFileUrl())){
            return ;
        }
        MemberView userDto=getRedisUserInfo(request);
        FileInputStream in = regUtil.getFileStream(rdto.getFileUrl().substring(1), rdto.getFileUrl().substring(0,1));
        if(in!=null){
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
    }

    /**
     * 下载路书
     * @return  下载路书
     */

    @RequestMapping(value="/downRoadBook",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="下载路书", notes = "下载路书", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="guideId",value="路书id",dataType="String", paramType = "query"),
    })
    public Result downRoadBook(HttpServletResponse response, HttpServletRequest request, @RequestBody TbGuideRouteInfoDto dto){
        log.info(">>>>>>>>>>>>> downRoadBook .requestParam param!!!  dto:"+dto);
        if(regUtil.isNull(dto.getGuideId())){
            return error(ResultMsg.IS_NULL);
        }
        MemberView redisUserInfo = getRedisUserInfo(request);
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("memberId",redisUserInfo.getMemberId());
        map.put("guideId",dto.getGuideId());
        String whereSql=" MEMBER_ID=:memberId and GUIDE_ID=:guideId ";
        List<TbUserDownGuideEntity> downGuideEntity = ridingManageService.queryEntity(new TbUserDownGuideEntity(), whereSql, map);
        if(downGuideEntity==null||downGuideEntity.size()==0){
            TbRidingGuideInfoEntity tbRidingGuideInfoEntity = ridingManageService.queryEntity(new TbRidingGuideInfoEntity(), dto.getGuideId() + "");
            if(tbRidingGuideInfoEntity!=null){
                TbUserDownGuideEntity guideDownEntity=new TbUserDownGuideEntity();
                guideDownEntity.setCreateTime(DateUtils.getCurrentStamp());
                guideDownEntity.setGuideId(tbRidingGuideInfoEntity.getId());
                guideDownEntity.setMemberId(redisUserInfo.getId());
                guideDownEntity.setCreateUser(redisUserInfo.getLoginname());
                ridingManageService.addEntity(guideDownEntity,false);
            }else{
                return error(ResultMsg.IS_NULL);
            }
        }
        return success();
    }

    /**
     * 我的路书
     * @return  我的路书
     */

    @RequestMapping(value="/getMyRoadBook",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="我的路书", notes = "我的路书", httpMethod = "POST" )
    /*@ApiImplicitParams({
            @ApiImplicitParam(name="id",value="路书id",dataType="String", paramType = "query"),
    })*/
    public Result getMyRoadBook(HttpServletResponse response, HttpServletRequest request, @RequestBody TbRidingGuideInfoDto dto){
        log.info(">>>>>>>>>>>>> getMyRoadBook .requestParam param!!!  dto:"+dto);
        MemberView redisUserInfo = getRedisUserInfo(request);
        dto.setUserId(redisUserInfo.getId());
        PagingBean pagingBean = ridingManageService.queryMyRoadBook(dto);
        return success(pagingBean);
    }

    /**
     * 查询途径点详情
     * @return  查询途径点详情
     */

    @RequestMapping(value="/queryRouteInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询途径点详情", notes = "查询途径点详情", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="路书id",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="type",value="查询类型(1.路书简介 2.线路详情 3.途径点详情 4.日程详情)",dataType="String", paramType = "query"),
    })
    public Result queryRouteInfo(HttpServletResponse response, HttpServletRequest request, @RequestBody TbRoutePointInfoDto dto){
        log.info(">>>>>>>>>>>>> queryRouteInfo .requestParam param!!!  dto:"+dto);
        Object obj=null;
        if(regUtil.isNull(dto.getId())){
            return error(ResultMsg.IS_NULL);
        }
        if("1".equals(dto.getType())){
            obj=ridingManageService.queryEntity(new TbRidingGuideInfoEntity(), dto.getId());
        }else if("2".equals(dto.getType())){
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("firstRoute",ridingManageService.queryEntity(new TbRoutePointInfoEntity(), dto.getId().split(",")[0]));
            map.put("secondRoute",ridingManageService.queryEntity(new TbRoutePointInfoEntity(), dto.getId().split(",")[1]));
            obj=map;
        }else if("3".equals(dto.getType())){
            obj=ridingManageService.queryEntity(new TbRoutePointInfoEntity(), dto.getId());
        }else if("4".equals(dto.getType())){
            TbGuideRouteInfoEntity guideEntity = ridingManageService.queryEntity(new TbGuideRouteInfoEntity(), dto.getId());
            Map<String,Object> paraMap=new HashMap<String, Object>();
            paraMap.put("routeId",guideEntity.getId());
            String whereSql=" route_id=:routeId";
            List<TbRoutePointInfoEntity> list = ridingManageService.queryEntity(new TbRoutePointInfoEntity(), whereSql, paraMap);
            TbGuideRouteInfoDto routeDto=new TbGuideRouteInfoDto();
            BeanUtils.copyProperties(guideEntity,routeDto);
            routeDto.setPointList(list);
            obj=routeDto;
        }
        return success(obj);
    }


}
