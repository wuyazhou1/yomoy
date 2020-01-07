package com.nsc.Amoski.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.dto.CityForbidRidingInfoDto;
import com.nsc.Amoski.dto.ForbidDetailInfoDto;
import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.dto.RidingInfoDto;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.util.GsonUtil;
import com.nsc.Amoski.util.RegUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Controller
@RequestMapping("/forbidRidingManage")
public class ForbidRidingManageController extends RidingServerBaseController<ForbidRidingManageController> {

    /**
     * 查询所有禁摩城市禁摩信息
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value="/queryAllCityForbidInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询所有禁摩城市禁摩信息", notes = "查询所有禁摩城市禁摩信息", httpMethod = "GET" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="当前页",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="limit",value="每页条数",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="status",value="状态(0:消禁 1:禁止)",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="cityName",value="城市代码",dataType="string", paramType = "query")
    })
    public Result queryAllCityForbidInfo(HttpServletResponse response, HttpServletRequest request,@RequestBody CityForbidRidingInfoDto dto){
        log.info(">>>>>>>>>>>>>>>>>request queryAllCityForbidInfo param dto:"+dto);
        PagingBean bean = ridingManageService.queryAllCityForbidInfo(dto);
        return success(bean);
    }

    /**
     * 查询所有禁摩城市
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value="/queryAllCityForbid",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询所有禁摩城市", notes = "查询所有禁摩城市", httpMethod = "GET" )
    public Result queryAllCityForbid(HttpServletResponse response, HttpServletRequest request){
        log.info(">>>>>>>>>>>>>>>>>request queryAllCityForbid param");
        List<Map<String, Object>> list = ridingManageService.queryAllCityForbid();
        return success(list);
    }

    /**
     * 查询单个城市禁摩信息
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value="/querySingForbidDetailInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询单个禁摩城市详情信息", notes = "查询单个禁摩城市详情信息", httpMethod = "GET" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="cityCode",value="城市代码",dataType="string", paramType = "query")
    })
    public Result querySingForbidDetailInfo(HttpServletResponse response, HttpServletRequest request, @RequestBody CityForbidRidingInfoDto param){
        log.info(">>>>>>>>>>>>>>>>>request querySingForbidDetailInfo");
        TbCityForbidRidingInfoEntity dto = ridingManageService.querySingCityForbidInfo(param);
        return success(dto);
    }

    /**
     * 根据城市代码查询禁摩区域
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value="/queryForbidEreaByCityCode",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="根据城市代码查询禁摩区域", notes = "根据城市代码查询禁摩区域", httpMethod = "GET" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="cityCode",value="城市代码",dataType="string", paramType = "query")
    })
    public Result queryForbidEreaByCityCode(HttpServletResponse response, HttpServletRequest request, @RequestBody ForbidDetailInfoDto param){
        log.info(">>>>>>>>>>>>>>>>>request queryForbidEreaByCityCode param:"+param);
        List<TbForbidDetailInfoEntity> list = ridingManageService.queryForbidEreaInfo(param);
        return success(list);
    }

    /**
     * 添加或修改区域信息
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value="/addForbidEreaInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="添加或修改区域信息", notes = "添加或修改区域信息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="区域id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="forbidRoadName",value="禁摩路线名称(多个用,隔开)",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="forbidRoadPoint",value="禁摩路线经纬度(多个用,隔开)",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="status",value="状态(0:消禁 1:禁止)",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="forbidName",value="路线名称",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="forbidType",value="路线类型(1.路线 2.区域)",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="forbidId",value="禁摩信息id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="cityCode",value="城市代码",dataType="string", paramType = "query")
    })
    public Result addForbidEreaInfo(HttpServletResponse response, HttpServletRequest request, @RequestBody List<TbForbidDetailInfoEntity> list){
        log.info(">>>>>>>>>>>>>>>>>request addForbidEreaInfo  entity:"+list);
        RegUtil regUtil=RegUtil.getSingleton();
        List<Object> updateList=new ArrayList<Object>();
        List<Object> insertList=new ArrayList<Object>();
        TbCityForbidRidingInfoEntity rentity=null;
        if(list!=null&&list.size()>0){
            rentity=new TbCityForbidRidingInfoEntity();
            Map<String,Object> map=new HashMap<String, Object>();
            map.put("id",list.get(0).getForbidId());
            map.put("cityCode",list.get(0).getCityCode());
            List<TbCityForbidRidingInfoEntity> templist = ridingManageService.queryEntity(rentity, " id=:id and city_code=:cityCode", map);
            log.info(">>>>>>>>>>>>>>>.templist:"+templist);
            if(templist==null||templist.size()==0){
                return error(ResultMsg.FORBID_CITY_NOT_EXISTS);
            }
            boolean bl=false;
            for (TbForbidDetailInfoEntity entity : list) {
                entity.setStatus("1");
                /*if(regUtil.isNull(entity.getForbidName(),entity.getForbidRoadName(),entity.getForbidRoadPoint(),entity.getForbidType())){//参数有误
                    bl=true;
                    break;
                }*/
                if (regUtil.isNull(entity.getId())) {//添加
                    entity.setCreateUser("system");
                    entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    insertList.add(entity);
                } else {//修改
                    updateList.add(entity);
                }
            }
            if(bl){
                return error(ResultMsg.IS_NULL);
            }
            log.info(">>>>>>>>>>>>>>>.insertList:"+insertList);
            log.info(">>>>>>>>>>>>>>>.updateList:"+updateList);
            rentity=templist.get(0);
            int count=insertList.size()+updateList.size();
            if(rentity.getForbidAreaCount()==count){//如果相等则不修改
                rentity=null;
            }else{
                rentity.setForbidAreaCount(count);
            }
            log.info(">>>>>>>>>>>>>>>.rentity:"+rentity);
        }
        /*for (TbForbidDetailInfoEntity entity : list){
            if(regUtil.isNull(entity.getId())){//添加
                ridingManageService.addForbidErea(entity);
            }else{//修改
                Map<String,Object> map=new HashMap<String, Object>();
                map.put("id",entity.getForbidId());
                map.put("cityCode",entity.getCityCode());
                List<TbForbidDetailInfoEntity> list = ridingManageService.queryEntity(entity, " id=:id and city_code=:cityCode", map);
                if(list!=null&&list.size()>0){
                    TbForbidDetailInfoEntity upEntity = list.get(0);
                    upEntity.setForbidRoadName(entity.getForbidRoadName());
                    upEntity.setForbidRoadPoint(entity.getForbidRoadPoint());
                    ridingManageService.updateEntity(entity);
                }
            }
        }*/
        ridingManageService.saveForbidErea(insertList,updateList,rentity);
        return success();
    }

    /**
     * 添加禁摩城市信息
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value="/addCityForbidInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="添加禁摩城市信息", notes = "添加禁摩城市信息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="cityName",value="城市名称",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="cityCode",value="城市代码",dataType="string", paramType = "query")
    })
    public Result addCityForbidInfo(HttpServletResponse response, HttpServletRequest request, @RequestBody TbCityForbidRidingInfoEntity entity){
        log.info(">>>>>>>>>>>>>>>>>request addCityForbidInfo  entity:"+entity);
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("cityCode",entity.getCityCode());
        List<TbCityForbidRidingInfoEntity> list = ridingManageService.queryEntity(entity, " city_code=:cityCode", map);
        if(list!=null&&list.size()>0){//城市已存在
            return error(ResultMsg.FORBID_CITY_EXISTS);
        }
        entity.setStatus("1");
        entity.setForbidAreaCount(0);
        entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        entity.setCreateUser("system");
        entity.setUpdateUser("system");
        entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        ridingManageService.addEntity(entity,false);
        return success();
    }

    /**
     * 保存政策信息
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value="/savePolicyInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="保存政策信息", notes = "保存政策信息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="禁摩信息id",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="policyDesc",value="政策描述",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="cityCode",value="城市代码",dataType="string", paramType = "query")
    })
    public Result savePolicyInfo(HttpServletResponse response, HttpServletRequest request, @RequestBody TbCityForbidRidingInfoEntity entity){
        log.info(">>>>>>>>>>>>>>>>>request savePolicyInfo  entity:"+entity);
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("id",entity.getId());
        map.put("cityCode",entity.getCityCode());
        List<TbCityForbidRidingInfoEntity> list = ridingManageService.queryEntity(entity, " id=:id and city_code=:cityCode", map);
        if(list!=null&&list.size()>0){
            TbCityForbidRidingInfoEntity upEntity = list.get(0);
            upEntity.setPolicyDesc(entity.getPolicyDesc());
            upEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            upEntity.setUpdateUser("systemPolicy");
            ridingManageService.updateEntity(upEntity);
        }
        return success();
    }

    /**
     * 消禁
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value="/cancelForbidErea",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="消禁", notes = "消禁", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="forbidId",value="禁摩信息id",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="cityCode",value="城市代码",dataType="string", paramType = "query")
    })
    public Result cancelForbidErea(HttpServletResponse response, HttpServletRequest request, @RequestBody TbForbidDetailInfoEntity entity){
        log.info(">>>>>>>>>>>>>>>>>request cancelForbidErea  entity:"+entity);
        if(regUtil.isNull(entity.getForbidId(),entity.getCityCode())){
            return error(ResultMsg.IS_NULL);
        }
        ridingManageService.cancelForbidArea(entity);
        return success();
    }
}
