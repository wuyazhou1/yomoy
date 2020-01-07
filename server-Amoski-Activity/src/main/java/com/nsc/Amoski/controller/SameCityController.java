package com.nsc.Amoski.controller;


import com.nsc.Amoski.FeignClient.RidingFeignClient;
import com.nsc.Amoski.FeignClient.UserServerApi;
import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.service.MemberService;
import com.nsc.Amoski.service.SameCityService;
import com.nsc.Amoski.uti.ResultUtil;
import com.nsc.Amoski.util.BaseController;
import com.nsc.Amoski.util.ImgCompress;
import com.nsc.Amoski.util.StringUtils;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@RestController
@Api(value="SameCity",description = "同城")
@RequestMapping("/SameCity")
public class SameCityController extends BaseController {
    private Logger log= LoggerFactory.getLogger(SameCityController.class);

    @Autowired
    private RidingFeignClient ridingFeignClient;

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserServerApi userServerApi;

    @Autowired
    private SameCityService SameCityService;


    private ResultMsg ResultMsg;

    private MemberView dto;

    @RequestMapping(value="/sameCityData",method = {RequestMethod.POST , RequestMethod.GET})
    @ApiOperation(value="同城骑行首页接口(路书和附近的人,同时更新当前会员经纬度)", notes = "同城骑行首页接口(路书和附近的人)", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="yAxis",value="经度",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="xAxis",value="纬度",dataType="string", paramType = "query"),
    })
    public Result sameCityData(HttpServletRequest request , HttpServletResponse response ,@RequestBody MemberView memberView){
        String yAxis = memberView.getYAxis();
        String xAxis = memberView.getXAxis();
        dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(isObjectValid()){
            return ResultUtil.error(this.ResultMsg);
        }
        dto.setYAxis(yAxis);
        dto.setXAxis(xAxis);
        if(isCheckValid()){
            return ResultUtil.error(this.ResultMsg);
        }
        Map<String,Object> result = new HashMap<>();
        TbRidingGuideInfoDto TbRidingGuideInfoDto = new TbRidingGuideInfoDto();
        TbRidingGuideInfoDto.setPage(1);
        TbRidingGuideInfoDto.setLimit(10);
        log.info("yAxis==>"+yAxis+"\t\txAxis==>"+xAxis);
        TbRidingGuideInfoDto.setLat(yAxis);
        TbRidingGuideInfoDto.setLng(xAxis);
        log.info("访问同城骑行首页接口(路书和附近的人,同时更新当前会员经纬度)\", notes = \"同城骑行首页接口(路书和附近的人)TbRidingGuideInfoDto==》"+TbRidingGuideInfoDto);
        Result GuideList = ridingFeignClient.queryGuideList(TbRidingGuideInfoDto);
        result.put("queryGuideList",GuideList.getData());//同城路书
        //获取当前用户经纬度位置（更新用户地理位置信息）
        userServerApi.updateMemberYXAxis(dto);
        //获取位置范围10公里内的会员
        dto.setPageSize("1");
        dto.setLength("10");
        if(dto.getYAxis().length()<13){
            int length = 13-dto.getYAxis().length();
            StringBuffer str = new StringBuffer("");
            for(int i=0;i<length;i++){
                str.append("0");
            }
            dto.setYAxis(dto.getYAxis()+str.toString());
        }
        if(dto.getXAxis().length()<13){
            int length = 13-dto.getXAxis().length();
            StringBuffer str = new StringBuffer("");
            for(int i=0;i<length;i++){
                str.append("0");
            }
            dto.setXAxis(dto.getXAxis()+str.toString());
        }

        result.putAll(SameCityService.sameCityData(dto));
        result.put("findMemberView",dto);
        /*Object o = SameCityService.sameCityData(dto);
        result.put("queryMemberByYXAxis",o);*/
        return ResultUtil.success(result);
    }

    @RequestMapping(value="/updateMemberYXAxis",method = {RequestMethod.POST , RequestMethod.GET})
    @ApiOperation(value="单独更新会员地理位置经纬度", notes = "单独更新会员地理位置经纬度", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="yAxis",value="经度",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="xAxis",value="纬度",dataType="string", paramType = "query"),
    })
    public Object updateMemberYXAxis(HttpServletRequest request , HttpServletResponse response ,
                                     @RequestParam String yAxis,
                                     @RequestParam String xAxis){
        dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(isObjectValid()){
            return ResultUtil.error(this.ResultMsg);
        }
        dto.setYAxis(yAxis);
        dto.setXAxis(xAxis);
        if(isCheckValid()){
            return ResultUtil.error(this.ResultMsg);
        }
        log.info("访问单独更新会员地理位置经纬度dto==》"+dto);
        try {
            //获取当前用户经纬度位置（更新用户地理位置信息）
            userServerApi.updateMemberYXAxis(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultMsg.UNKNOWN_EXCEPTION);
        }
        return ResultUtil.success();
    }

    //@RequestMapping(value="/queryMemberByYXAxisList",method = {RequestMethod.POST , RequestMethod.GET})
    /*@ApiOperation(value="单独查询经纬度范围内的所有会员", notes = "单独查询经纬度范围内的所有会员", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="yAxis",value="经度",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="xAxis",value="纬度",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="distance",value="距离",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="pageSize",value="页数",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="length",value="条数",dataType="string", paramType = "query"),
    })*/
    public Object queryMemberByYXAxisList(HttpServletRequest request , HttpServletResponse response ,
            @RequestParam String yAxis,
            @RequestParam String xAxis,
            @RequestParam String pageSize,
            @RequestParam String length,
            @RequestParam String distance){
        dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(isObjectValid()){
            return ResultUtil.error(this.ResultMsg);
        }
        dto.setYAxis(yAxis);
        dto.setXAxis(xAxis);
        if(isCheckValid()){
            return ResultUtil.error(this.ResultMsg);
        }
        dto.setPageSize(pageSize);
        dto.setLength(length);
        dto.setDistance(distance);
        try {
            //查询经纬度范围内的所有会员
            userServerApi.queryMemberByYXAxisList(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultMsg.UNKNOWN_EXCEPTION);
        }
        return ResultUtil.success();
    }



    @RequestMapping(value="/queryMemberByMemberName",method = {RequestMethod.POST , RequestMethod.GET})
    @ApiOperation(value="单独查询经纬度范围内的所有会员", notes = "单独查询经纬度范围内的所有会员", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="yAxis",value="经度",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="xAxis",value="纬度",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="distance",value="公里距离(距离不传就是全国)",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="pageSize",value="页数",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="length",value="条数",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="name",value="会员名称",dataType="string", paramType = "query"),
    })
    public Object queryMemberByMemberName(HttpServletRequest request , HttpServletResponse response ,
                                          @RequestBody MemberView MemberView){
        dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(isObjectValid()){
            return ResultUtil.error(this.ResultMsg);
        }
        dto.setYAxis(MemberView.getYAxis());
        dto.setXAxis(MemberView.getXAxis());
        if(isCheckValid()){
            return ResultUtil.error(this.ResultMsg);
        }
        dto.setName(MemberView.getName());
        dto.setPageSize(MemberView.getPageSize());
        dto.setLength(MemberView.getLength());
        dto.setDistance(MemberView.getDistance());
        if(!StringUtils.isEmpty(dto.getName())){
            dto.setId(null);
        }
        log.info("访问单独查询经纬度范围内的所有会员dto==》"+dto);
        PagingBean o = null;
        try {
            //查询经纬度范围内的所有会员
            o = userServerApi.queryMemberByMemberName(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultMsg.UNKNOWN_EXCEPTION);
        }
        return ResultUtil.success(o);
    }

    @RequestMapping(value="/queryMemberRanking",method = {RequestMethod.POST , RequestMethod.GET})
    @ApiOperation(value="会员排名接口", notes = "会员排名接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="yAxis",value="经度",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="xAxis",value="纬度",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="cycle",value="周期（本周填写1，上周填写8）",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="distance",value="公里距离(距离不传就是全国)",dataType="string", paramType = "query")
    })
    public Result queryMemberRanking(HttpServletRequest request , HttpServletResponse response ,
                                    @RequestBody MemberView MemberView){
        log.info("访问会员排名接口==MemberView"+MemberView);
        dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(isObjectValid()){
            return ResultUtil.error(this.ResultMsg);
        }
        dto.setYAxis(MemberView.getYAxis());
        dto.setXAxis(MemberView.getXAxis());
        dto.setCycle(MemberView.getCycle());
        if(isCheckValid()){
            return ResultUtil.error(this.ResultMsg);
        }
        dto.setDistance(MemberView.getDistance());



        dto.setPageSize("1");
        dto.setLength("100");
        List<MemberDto> o = null;
        MemberDto resultMemberDto = null;
        Map<String,Object> resultMap = new HashMap<>();
        try {
            //查询经纬度范围内的所有会员
            if(dto.getYAxis().length()<13){
                int length = 13-dto.getYAxis().length();
                StringBuffer str = new StringBuffer("");
                for(int i=0;i<length;i++){
                    str.append("0");
                }
                dto.setYAxis(dto.getYAxis()+str.toString());
            }
            if(dto.getXAxis().length()<13){
                int length = 13-dto.getXAxis().length();
                StringBuffer str = new StringBuffer("");
                for(int i=0;i<length;i++){
                    str.append("0");
                }
                dto.setXAxis(dto.getXAxis()+str.toString());
            }
            o = userServerApi.queryMemberRanking(dto);
            for(MemberDto MemberDto:o){
                if(MemberDto.getId().equals(dto.getId())){
                    resultMemberDto = new MemberDto();
                    BeanUtils.copyProperties(MemberDto,resultMemberDto);
                }
            }
            if(resultMemberDto==null){
                dto.setDistance(null);
                PagingBean<MemberDto> pagingBean = userServerApi.queryMemberByMemberName(dto);
                try {
                    resultMemberDto =  pagingBean.getData().get(0);
                } catch (Exception e) {
                    log.info("查询为空值异常dto==》"+dto);
                    e.printStackTrace();
                }
                resultMemberDto.setRn(null);
            }
            resultMap.put("MemberRanking",o);
            resultMap.put("member",resultMemberDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultMsg.UNKNOWN_EXCEPTION);
        }
        return ResultUtil.success(resultMap);
    }



    @RequestMapping(value="/knightHomePage",method = {RequestMethod.POST , RequestMethod.GET})
    @ApiOperation(value="骑士首页接口", notes = "骑士首页接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="会员id",dataType="string", paramType = "query"),
    })
    public Object knightHomePage(HttpServletRequest request , HttpServletResponse response ,@RequestBody MemberView MemberView ){
        log.info("访问骑士首页接口MemberView==》"+MemberView);
        dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(isObjectValid()){
            return ResultUtil.error(this.ResultMsg);
        }
        Map<String,Object> resultMap = SameCityService.knightHomePage(MemberView);
        Integer followed = SameCityService.queryFabulousByMemberView(dto,MemberView);
        resultMap.put("followed",followed);
        try {
            log.info("骑士首页会员"+MemberView.getId()+"更换图片前");
            resultMap.put("backgroundImages",
                    userServerApi.findMemberView(null,null,null,MemberView.getId().toString()).getBackgroundImages() );
            log.info("骑士首页会员"+MemberView.getId()+"更换图片后，图片路径为"+resultMap.get("backgroundImages"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultUtil.success(resultMap);
    }


    @RequestMapping(value="/getFabulousList",method = {RequestMethod.POST , RequestMethod.GET})
    @ApiOperation(value="获赞列表", notes = "获赞列表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="会员id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="pageSize",value="页数",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="length",value="条数",dataType="string", paramType = "query"),
    })
    public Object getFabulousList(HttpServletRequest request , HttpServletResponse response ,@RequestBody MemberView MemberView ){
        log.info("访问获赞列表MemberView==》"+MemberView);
        dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(isObjectValid()){
            return ResultUtil.error(this.ResultMsg);
        }
        MemberView.InfoQueryParent();
        PagingBean pagingBean = SameCityService.getFabulousList(MemberView);
        return ResultUtil.success(pagingBean);
    }


    //@ResponseBody
    @RequestMapping(value = "saveBackgroundImages",method = RequestMethod.POST)
    @ApiOperation(value="保存用户骑士背景图片", notes = "保存用户同城背景图片", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="files",value="文件",dataType="string", paramType = "query"),
    })
    public Object saveBackgroundImages(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @ApiParam(value = "files",required = true)  MultipartFile[] files) throws Exception {

        log.info("保存用户骑士背景图片saveBackgroundImages");
        String sessionId = getSessionId(request);
        dto=memberService.getRedisUserObj(sessionId,null);
        if(isObjectValid()){
            return ResultUtil.error(this.ResultMsg);
        }
        String backgroundImages = null;
        log.info("文件数量files==>"+files.length);
        for(MultipartFile file : files) {
            //if (!mFile.getValue().isEmpty()) {
            UUID uuid=UUID.randomUUID();
            Calendar calendar = Calendar.getInstance();
            String path = "";
            if(System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1){
                path = "D:/home/uploadFile/images/createActivity/"+ StringUtils.getYearStr(calendar) +"/"+StringUtils.getMonthStr(calendar)+"/"+StringUtils.getDayStr(calendar)+"/"+StringUtils.getHourStr(calendar);
            }else{
                path = "/home/uploadFile/images/backgroundImages/"+ StringUtils.getYearStr(calendar) +"/"+StringUtils.getMonthStr(calendar)+"/"+StringUtils.getDayStr(calendar)+"/"+StringUtils.getHourStr(calendar);
            }
            File filepath = new File(path);
            if (!filepath.exists())
                filepath.mkdirs();
            backgroundImages = path+"/"+"backgroundImages"+uuid.toString().replaceAll("-","");
            if (!file.isEmpty()) {
                try {
                    File file1 = new File(backgroundImages);
                    file.transferTo(file1);
                    dto.setBackgroundImages(backgroundImages);
                    userServerApi.saveBackgroundImages(dto);
                    memberService.setRedisUserObj(sessionId,dto);
                    log.info("保存用户骑士背景图片saveBackgroundImages"+backgroundImages);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResultUtil.error(ResultMsg.IS_NULL);
                }
            }
        }
        return ResultUtil.success(backgroundImages);//this.resultData(resultList);
    }


    //@ResponseBody
    @RequestMapping(value = "getBackgroundImages",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="获取骑士背景图片", notes = "获取骑士背景图片", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="fileNameUrl",value="活动文件路径",dataType="string", paramType = "query"),
    })
    public void getFile(HttpServletRequest request,HttpServletResponse response){
        String fileNameUrl = request.getParameter("fileNameUrl");
        log.info("访问获取骑士背景图片fileNameUrl==》"+fileNameUrl);
        byte[] arr=null;
        try {
            File file = new File(fileNameUrl);
            FileInputStream fis = new FileInputStream(file);
            arr = new byte[fis.available()];
            fis.read(arr);
            response.getOutputStream().write(arr);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return arr;
    }




    public Boolean isObjectValid(){
        if(dto==null){
            log.error("该用户没有登入");
            if(StringUtils.isTextMember){
                dto = new MemberView();
                dto.setId(StringUtils.memberId);
            }else{
                this.ResultMsg=ResultMsg.NOT_LOGIN;
                return true;
            }
        }
        return false;
    }

    public Boolean isCheckValid(){
        if(StringUtils.isEmpty(dto.getYAxis())||StringUtils.isEmpty(dto.getXAxis())){
            log.error("该用户没有经纬度信息");
            this.ResultMsg=ResultMsg.NOT_LOGIN;
            return true;
        }
        return false;
    }
}
