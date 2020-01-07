package com.nsc.Amoski.service;


import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.*;

import java.util.List;
import java.util.Map;

public interface UserCenterManageService {

    /**
     * 查询用户消息
     * @param param 查询条件 查询所有消息
     * @return  用户所有消息
     */
    PagingBean queryUserMessage(UsreMessageDto param);

    /**
     * 新增用户消息
     * @return
     */
    void addUserMessage(TbUsreMessageEntity param,MemberView userInfo,int type);

    /**
     * 查询用户相册图片
     * @param param 查询条件 用户id
     * @return  用户所有图片
     */
    PagingBean queryUserPhotoImg(PhotoPicDto param);

    /**
     * 新增相册图片
     */
    void addPhotoPic(List<TbPhotoPic> list);
    /**
     * 删除图片
     * @param param
     */
    void deleteUserPic(PhotoPicDto param);

    /**
     * 查询用户某张图片
     * @return
     */
    TbPhotoPic querySingleUserImg(Integer uid,Integer imgId);

    /**
     * 查询一张或多张图片根据图片ids
     * @return
     */
    List<TbPhotoPic> queryImgByImgId(String imgIds);

    /**
     * 查询所有绑定车辆信息
     * @return
     */
    List<VehicleInfoDto> queryAllBindVehicleInfo(VehicleInfoDto param);

    /**
     * 车辆绑定
     * @param param
     */
    void bindSelfVehicle(TbVehicleInfoEntity param);

    /**
     * 用户车辆信息修改
     * @param param
     */
    void updateSelfVehicle(TbVehicleInfoEntity param,boolean bl);
    /**
     * 用户默认车辆信息修改
     * @param userId 用户id
     */
    void updateDefaultVehicle(String userId);

    /**
     * 查询某辆绑定车辆信息
     * @return
     */
    TbVehicleInfoEntity querySingleBindVehicleInfo(String id,String userId);

    /**
     * 查询车辆所有品牌
     * @return
     */
    List<VehicleBrandDto> queryAllBindVehicleBrandInfo();
    /**
     * 查询所有车辆类型
     * @return
     */
    List<VehicleBrandTypeDto> queryVehicleBrandType();
    /**
     * 查询车辆所有品牌下的车型
     * @return
     */
    List<VehicleBrandTypeDto> queryVehicleBrandByBrandId(VehicleBrandTypeDto param);

    /**
     * 批量插入天气code
     * @param list
     */
    void addWeatherBean(List<WeatherDto> list);

    /**
     * 删除用户车辆信息
     * @param dto
     */
    void deleteUserVehicleInfo(VehicleInfoDto dto);
    /**
     * 新增用户意见反馈
     * @return
     */
    void addUserFeedback(TbUserFeedbackEntity param);

    /**
     * 查询手机号是否已报名
     * @return
     */
    List<ActivityApplyDto> queryMobileApply(ActivityApplyDto parma);

    /**
     * 查询品牌系列数据
     * @return
     */
    List<TbBrandSeries> queryBrandSeries(BrandSeriesDto dto);

    /**
     * 公共插入实体数据
     * @param entity
     */
    <T> void addEntity(T entity);

    /**
     * 公共插入实体数据
     * @param entity
     */
    <T> void addEntity(T entity,boolean bl);

    /**
     * 公共修改实体数据
     * @param entity
     */
    <T> void updateEntity(T entity);
    /**
     * 公共查询数据
     * @param entity
     */
    <T> List<T> queryListEntity(T entity, String whereSql, Map<String,Object> map);

    /**
     * 公共根据id查询数据
     * @param entity
     */
    <T> T queryEntity(T entity,String id);

    /**
     * 批量插入
     * @return
     */
    void bachInsert(List<Object> list);

    /**
     * 批量更新
     * @return
     */
    void bachUpdate(List<Object> list);

    void updateBasics(TbActivityBasics1Entity basicsEntity);

    List<TbActivitySignUpEntity> querySignUpEntity(TbActivitySignUpEntity tbActivitySignUpEntity, Map<String, Object> map);
}