package com.nsc.Amoski.controller;


import com.nsc.Amoski.entity.MemberDto;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.service.SameCityService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/SameCity")
public class SameCityController {
    @Autowired
    private SameCityService sameCityService;



    @ResponseBody
    @RequestMapping(value = "updateMemberYXAxis",method = RequestMethod.POST)
    @ApiOperation(value="更新会员地理位置经纬度", notes = "更新会员地理位置经纬度", httpMethod = "POST" )
    public Object updateMemberYXAxis(@RequestBody  MemberView MemberView){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            sameCityService.updateMemberYXAxis(MemberView);
        } catch (Exception e) {
            resultMap.put("code","001");
            resultMap.put("msg","系统繁忙");
            e.printStackTrace();
            return resultMap;
        }
        resultMap.put("code","000");
        resultMap.put("msg","更新成功");
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "queryMemberByYXAxis",method = RequestMethod.POST)
    @ApiOperation(value="查询经纬度范围内的所有会员", notes = "查询经纬度范围内的所有会员", httpMethod = "POST" )
    public Object queryMemberByYXAxis(@RequestBody  MemberView MemberView){
        List resultList = null;
        try {
            resultList = sameCityService.queryMemberByYXAxis(MemberView);
        } catch (Exception e) {
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("code","001");
            resultMap.put("msg","系统繁忙");
            e.printStackTrace();
            return resultMap;
        }
        return resultList;
    }

    @ResponseBody
    @RequestMapping(value = "queryMemberByYXAxisList",method = RequestMethod.POST)
    @ApiOperation(value="查询经纬度范围内的所有会员", notes = "查询经纬度范围内的所有会员", httpMethod = "POST" )
    public Object queryMemberByYXAxisList(@RequestBody  MemberView MemberView){
        Map<String,Object> resultMap = new HashMap<>();
        List resultList = null;
        try {
            resultList = sameCityService.queryMemberByYXAxis(MemberView);
        } catch (Exception e) {
            resultMap.put("code","001");
            resultMap.put("msg","系统繁忙");
            e.printStackTrace();
            return resultMap;
        }
        resultMap.put("code","0");
        resultMap.put("msg","成功");
        resultMap.put("data",resultList);
        return resultMap;
    }




    @ResponseBody
    @RequestMapping(value = "queryMemberByMemberName",method = RequestMethod.POST)
    @ApiOperation(value="查询经纬度范围内的会员名称", notes = "查询经纬度范围内的会员名称", httpMethod = "POST" )
    public PagingBean<MemberDto> queryMemberByMemberName(@RequestBody  MemberView MemberView){
        Map<String,Object> resultMap = new HashMap<>();
        PagingBean resultList = null;
        try {
            resultList = sameCityService.queryMemberByMemberName(MemberView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*resultMap.put("code","000");
        resultMap.put("msg","成功");
        resultMap.put("data",resultList);*/
        return resultList;
    }
    @ResponseBody
    @RequestMapping(value = "queryMemberRanking",method = RequestMethod.POST)
    @ApiOperation(value="查询经纬度范围内的会员名称", notes = "查询经纬度范围内的会员名称", httpMethod = "POST" )
    /*@Cacheable(value = "String",key = "T(String).valueOf('queryMemberRanking')." +
            "concat(#dto.xAxis.substring(0,12))." +
            "concat(#dto.yAxis.substring(0,12))." +
            "concat(#dto.cycle==null?'':#dto.cycle)."+
            "concat(#dto.distance==null?'':#dto.distance)")*/
    public List<MemberDto> queryMemberRanking(@RequestBody MemberView dto){
        List pagingBean = sameCityService.queryMemberRanking(dto);
        return pagingBean;
    }

    @ResponseBody
    @RequestMapping(value = "saveBackgroundImages",method = RequestMethod.POST)
    @ApiOperation(value="修改会员背景图片", notes = "修改会员背景图片", httpMethod = "POST" )
    public Object saveBackgroundImages(@RequestBody MemberView dto){
        Map<String,Object> resultMap = new HashMap<>();
        try {
            sameCityService.saveBackgroundImages(dto);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code","001");
            resultMap.put("msg","成功");
            return resultMap;
        }
        resultMap.put("code","000");
        resultMap.put("msg","成功");
        return resultMap;
    }

}
