package com.nsc.Amoski.controller;

import com.nsc.Amoski.dto.ActivityBasicDto;
import com.nsc.Amoski.dto.ActivityScheduleDto;
import com.nsc.Amoski.dto.ActivitySignUpDto;
import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.util.DateUtils;
import com.nsc.Amoski.util.HttpUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/appRidingGuideManage")
public class AppRidingGuideManageController extends ActivityServerBaseController<AppRidingGuideManageController> {

    /**
     * 小程序查询活动列表信息
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "小程序查询活动列表信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "排序类型", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "x", value = "经度", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "y", value = "纬度", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sqrtValue", value = "公里", dataType = "int", paramType = "query")
    })
    @PostMapping(path = "/smallQueryActivityList")
    public Result smallQueryActivityList(@RequestBody ActivityBasicDto dto) {
        log.info(">>>>>>>>>>>>> smallQueryActivityList .requestParam param!!!  dto:" + dto);
        if (dto.getLimit() == 0) {
            dto.setLimit(200);
            dto.setPage(1);
        }
        if (!StringUtils.isEmpty(dto.getOrgName())) {
            Result rs = userApi.findByOrgName(dto.getOrgName());
            if (null != rs.getData()) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) rs.getData();
                ArrayList<Integer> arrayList = new ArrayList<>();
                for (Map<String, Object> datum : data) {
                    arrayList.add((Integer) datum.get("ID"));
                }
                dto.setDps(arrayList);
            } else {
                return success();
            }
        }
        PagingBean pagingBean = guideRouteManageService.smallQueryActivityList(dto);
        return success(pagingBean);
    }

    /**
     * 小程序查询当前正在进行的活动列表信息
     *
     * @param dto
     * @return
     */
    @PostMapping(path = "/smallQueryCurrentActivityList")
    @ApiOperation(value = "/小程序查询当前正在进行的活动列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "排序类型", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "x", value = "经度", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "y", value = "纬度", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sqrtValue", value = "公里", dataType = "int", paramType = "query")
    })
    public Result smallQueryCurrentActivityList(@RequestBody ActivityBasicDto dto) {
        log.info(">>>>>>>>>>>>> smallQueryCurrentActivityList .requestParam param!!!  dto:" + dto);
        if (dto.getLimit() == 0) {
            dto.setLimit(200);
            dto.setPage(1);
        }
        if (!StringUtils.isEmpty(dto.getOrgName())) {
            Result rs = userApi.findByOrgName(dto.getOrgName());
            if (null != rs.getData()) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) rs.getData();
                ArrayList<Integer> arrayList = new ArrayList<>();
                for (Map<String, Object> datum : data) {
                    arrayList.add((Integer) datum.get("ID"));
                }
                dto.setDps(arrayList);
            } else {
                return success();
            }
        }
        PagingBean pagingBean = guideRouteManageService.smallQueryCurrentActivityList(dto);
        return success(pagingBean);
    }

    /**
     * 查询活动列表信息
     *
     * @return 查询活动列表信息
     */
    @RequestMapping(value = "/queryActivityList", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "查询活动列表信息", notes = "查询活动列表信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "排序类型", dataType = "String", paramType = "query"),
    })
    public Result queryActivityList(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityBasicDto dto) throws Exception {
        log.info(">>>>>>>>>>>>> queryActivityList .requestParam param!!!  dto:" + dto);
        if (dto.getLimit() == 0) {
            dto.setLimit(200);
            dto.setPage(1);
        }
        PagingBean pagingBean = guideRouteManageService.queryActivityList(dto);
        return success(pagingBean);
    }

    /**
     * 查询单个活动详情信息
     *
     * @return 查询单个活动详情信息
     */
    @RequestMapping(value = "/queryActivityDetailInfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "查询单个活动详情信息", notes = "查询单个活动详情信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动id", dataType = "String", paramType = "query"),
    })
    public Result queryActivityDetailInfo(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityBasicDto dto) throws Exception {
        log.info(">>>>>>>>>>>>> queryActivityDetailInfo .requestParam param!!!  dto:" + dto);
        if (regUtil.isNull(dto.getId())) {
            return error(ResultMsg.IS_NULL);
        }
        Map<String, Object> map = new HashMap<>();
        //活动基本信息
        ActivityBasicDto activityBasicDtos = guideRouteManageService.queryActivityDetailInfo(dto, true);
        if (activityBasicDtos == null) {
            return error(ResultMsg.IS_NULL);
        }

        String whereSql = " basics_id=:basicsId";
        map.put("basicsId", activityBasicDtos.getCode());
        //活动报名人员表
        List<TbActivitySignUpEntity> activitySignUpList = userCenterManageService.querySignUpEntity(new TbActivitySignUpEntity(), map);
        map.clear();
        map.put("basicsId", activityBasicDtos.getId());
        //费用
        List<TbCtivityInvoiceEntity> activityInvoiceList = userCenterManageService.queryListEntity(new TbCtivityInvoiceEntity(), whereSql, map);
        //活动报名人员接送表
        /*List<TbPeopleReceiveSendEntity> peopleReceiveSendList = userCenterManageService.queryListEntity(new TbPeopleReceiveSendEntity(), whereSql, map);
        ActivitySignUpDto signUpDto=new ActivitySignUpDto();
        signUpDto.setArriveDate(new Date(DateUtils.getNullAsTimestamp().getTime()));
        signUpDto.setFlightDate(ordinationList.get(0).getActivityStart());
        signUpDto.setPlaceDeparture("全国");
        signUpDto.setDestination(ordinationList.get(0).getCollectionPlace());
        MemberView userDto=getRedisUserInfo(request,false);
        if(userDto!=null&&!regUtil.isNull(userDto.getTel())){
            ActivitySignUpDto info=new ActivitySignUpDto();
            info.setBasicsId(dto.getBasicsId());
            info.setTel(userDto.getTel());
            //航班信息
            List<ActivitySignUpDto> activitySignUpDtos = guideRouteManageService.queryActivitySignUpPeople(info);
            if(activitySignUpDtos!=null&&activitySignUpDtos.size()>0){
                signUpDto=activitySignUpDtos.get(0);
            }
        }*/
        //查询综合数据
        ActivityScheduleDto info = new ActivityScheduleDto();
        info.setBasicsId(activityBasicDtos.getId() + "");
        List<ActivityScheduleDto> list = guideRouteManageService.queryH5ActivityGuideInfo(info);
        List<ActivityScheduleDto> newList = new ArrayList<ActivityScheduleDto>();
        Map<String, ActivityScheduleDto> newMap = new HashMap<String, ActivityScheduleDto>();//用来判断是否存在
        //处理数据
        for (ActivityScheduleDto lDto : list) {
            TbActivityRouteEntity point = new TbActivityRouteEntity();
            BeanUtils.copyProperties(lDto, point);
            TbActivityHotelEntity hotel = new TbActivityHotelEntity();
            BeanUtils.copyProperties(lDto, hotel);
            lDto.setTbActivityHotelEntity(hotel);
            point.setId(lDto.getRid());
            if (newMap.containsKey(lDto.getId().toString())) {
                newMap.get(lDto.getId().toString()).getTbActivityRouteEntity().add(point);
            } else {
                newMap.put(lDto.getId().toString(), lDto);
                newList.add(lDto);
            }
        }
        //更新活动浏览量
        TbActivityBasics1Entity basicsEntity = userCenterManageService.queryEntity(new TbActivityBasics1Entity(), activityBasicDtos.getId() + "");
        basicsEntity.setTotalVisits(regUtil.isNull(basicsEntity.getTotalVisits()) ? 1 : basicsEntity.getTotalVisits() + 1);
        //userCenterManageService.updateEntity(basicsEntity);
        userCenterManageService.updateBasics(basicsEntity);
        activityBasicDtos.setTotalVisits(basicsEntity.getTotalVisits());
        map.clear();
        map.put("activity", activityBasicDtos);//活动所有基本信息
        map.put("activityInvoiceList", activityInvoiceList);//费用
        map.put("activitySignUpList", activitySignUpList);//报名人员
        map.put("list", newList);//日程
        return success(map);
    }

    /**
     * 查询活动报名票种信息
     *
     * @return 查询活动报名票种信息
     */

    @RequestMapping(value = "/queryActivityInvoice", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "查询活动报名票种信息", notes = "查询活动报名票种信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动id", dataType = "String", paramType = "query"),
    })
    public Result queryActivityInvoice(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityBasicDto dto) throws Exception {
        log.info(">>>>>>>>>>>>> queryActivityInvoice .requestParam param!!!  dto:" + dto);
        Map<String, Object> map = new HashMap<>();
        //活动基本信息
        map.put("basicsId", dto.getId());
        String whereSql = " basics_id=:basicsId";
        //费用
        List<TbCtivityInvoiceEntity> activityInvoiceList = userCenterManageService.queryListEntity(new TbCtivityInvoiceEntity(), whereSql, map);
        return success(activityInvoiceList);
    }

    /**
     * 查询活动路书详细信息
     *
     * @return 查询活动路书详细信息
     */

    @RequestMapping(value = "/queryH5ActivityGuideInfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "查询路书列表", notes = "查询活动路书详细信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "basicsId", value = "活动id", dataType = "String", paramType = "query"),
    })
    public Result queryH5ActivityGuideInfo(HttpServletResponse response, HttpServletRequest request, @RequestBody ActivityScheduleDto dto) throws Exception {
        log.info(">>>>>>>>>>>>> queryH5ActivityGuideInfo .requestParam param!!!  dto:" + dto);
        if (regUtil.isNull(dto.getBasicsId())) {
            return error(ResultMsg.IS_NULL);
        }
        Map<String, Object> map = new HashMap<>();
        String whereSql = " code=:basicsId";
        map.put("basicsId", dto.getBasicsId());
        //活动基本信息
        List<TbActivityBasicsEntity> activityList = userCenterManageService.queryListEntity(new TbActivityBasicsEntity(), whereSql, map);
        if (activityList == null || activityList.size() == 0) {
            return error(ResultMsg.ACTIVITY_NOTEXIST_ERROR);
        }
        TbActivityBasicsEntity activity = activityList.get(0);
        dto.setBasicsId(activity.getId() + "");
        map.put("basicsId", dto.getBasicsId());
        whereSql = " basics_id=:basicsId";
        //活动海报
        List<TbActivityBillImageEntity> bill = userCenterManageService.queryListEntity(new TbActivityBillImageEntity(), whereSql + " and type='2'", map);
        activity.setTbActivityBillImageEntity(bill);
        //活动日程时间安排
        List<TbActivityTimeHistoryEntity> activityTimeList = userCenterManageService.queryListEntity(new TbActivityTimeHistoryEntity(), whereSql, map);
        //活动简介
        List<TbActivitySynopsisEntity> synopsisList = userCenterManageService.queryListEntity(new TbActivitySynopsisEntity(), whereSql, map);

        //活动报名
        List<TbActivityOrdinationEntity> ordinationList = userCenterManageService.queryListEntity(new TbActivityOrdinationEntity(), whereSql, map);
        //费用
        //List<TbCtivityInvoiceEntity> ctivityInvoiceList = userCenterManageService.queryListEntity(new TbCtivityInvoiceEntity(), whereSql, map);

        //查询
        /*//活动报名人员表
        List<TbActivitySignUpEntity> ctivitySignUpList = userCenterManageService.queryListEntity(new TbActivitySignUpEntity(), whereSql, map);
        //活动报名人员表*/
        //List<TbPeopleReceiveSendEntity> peopleReceiveSendList = userCenterManageService.queryListEntity(new TbPeopleReceiveSendEntity(), whereSql, map);
        ActivitySignUpDto signUpDto = new ActivitySignUpDto();
        signUpDto.setArriveDate(new Date(DateUtils.getNullAsTimestamp().getTime()));
        signUpDto.setFlightDate(ordinationList.get(0).getActivityStart());
        signUpDto.setPlaceDeparture("全国");
        signUpDto.setDestination(ordinationList.get(0).getCollectionPlace());
        MemberView userDto = getRedisUserInfo(request, false);
        if (userDto != null && !regUtil.isNull(userDto.getTel())) {
            ActivitySignUpDto info = new ActivitySignUpDto();
            info.setBasicsId(dto.getBasicsId());
            info.setTel(userDto.getTel());
            //航班信息
            List<ActivitySignUpDto> activitySignUpDtos = guideRouteManageService.queryActivitySignUpPeople(info);
            if (activitySignUpDtos != null && activitySignUpDtos.size() > 0) {
                signUpDto = activitySignUpDtos.get(0);
            }
        }

        //查询综合数据
        List<ActivityScheduleDto> list = guideRouteManageService.queryH5ActivityGuideInfo(dto);
        List<ActivityScheduleDto> newList = new ArrayList<ActivityScheduleDto>();
        Map<String, ActivityScheduleDto> newMap = new HashMap<String, ActivityScheduleDto>();//用来判断是否存在
        //查询所有路书图片
        List<TbActivityRouteImageEntity> routeImgList = userCenterManageService.queryListEntity(new TbActivityRouteImageEntity(), whereSql, map);
        //处理数据
        for (ActivityScheduleDto lDto : list) {
            TbActivityRouteEntity point = new TbActivityRouteEntity();
            BeanUtils.copyProperties(lDto, point);
            TbActivityHotelEntity hotel = new TbActivityHotelEntity();
            BeanUtils.copyProperties(lDto, hotel);
            lDto.setTbActivityHotelEntity(hotel);
            point.setId(lDto.getRid());
            List<TbActivityRouteImageEntity> tbActivityRouteImageEntity = new ArrayList<>();
            for (TbActivityRouteImageEntity routeImg : routeImgList) {
                if (routeImg.getRouteId().equals(point.getId().toString())) {
                    tbActivityRouteImageEntity.add(routeImg);
                }
            }
            point.setTbActivityRouteImageEntity(tbActivityRouteImageEntity);
            if (newMap.containsKey(lDto.getId().toString())) {
                newMap.get(lDto.getId().toString()).getTbActivityRouteEntity().add(point);
            } else {
                newMap.put(lDto.getId().toString(), lDto);
                lDto.getTbActivityRouteEntity().add(point);
                newList.add(lDto);
            }
        }
        for (TbActivityTimeHistoryEntity timeDto : activityTimeList) {
            newMap.get(timeDto.getScheduleId()).getTbActivityTimeHistoryEntity().add(timeDto);
        }
        map.clear();
        map.put("activitySynopsis", synopsisList);
        map.put("ordinationList", ordinationList);
        map.put("signUpDto", signUpDto);
        map.put("activity", activity);
        map.put("list", newList);
        return success(map);
    }

    /**
     * 活动后台图片指向地址
     */
    @RequestMapping(value = "/getImg", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "app图片指向地址", notes = "app图片指向地址", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imgUrl", value = "图片路径", dataType = "String", paramType = "query"),
    })
    public void getImg(HttpServletResponse response, HttpServletRequest request, String imgUrl) throws Exception {
        log.info(">>>>>>>>>>>>>>>getImg....url:" + imgUrl);
        /*if(regUtil.isNull(imgUrl)){

        }*/
        InputStream in = null;
        if (imgUrl.startsWith("http")) {//请求图片资源
            in = HttpUtil.sendGet(imgUrl, "1");
        } else {
            in = regUtil.getFileStream(imgUrl);
        }
        if (in != null) {
            byte[] by = new byte[in.available()];
            int n = 0;
            while ((n = in.read(by)) != -1) {
                response.getOutputStream().write(by);
                log.info(">>>>>>>>>>>>>>>>>byte length!!!!" + by.length + ">>>>>>>>>>>>>");
            }
            if (in != null) {
                in.close();
            }
        }
    }

    @RequestMapping(value = "/getActivityImages", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "app获取图片", notes = "app获取图片", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "活动code值", dataType = "String", paramType = "query"),
    })
    public Object getActivityImages(HttpServletRequest request) {
        String code = request.getParameter("code");
        String ImageFilePage = guideRouteManageService.queryActityImages(code);
        byte[] arr = null;
        try {
            File file = new File(ImageFilePage);
            InputStream fis = new FileInputStream(file);
            arr = new byte[fis.available()];
            fis.read(arr);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arr;
    }


}
