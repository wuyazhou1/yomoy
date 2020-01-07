package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.UserCenterManageDao;
import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.FeignClient.UserServerApi;
import com.nsc.Amoski.service.UserCenterManageService;
import com.nsc.Amoski.util.RegUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class UserCenterManageServiceImpl implements UserCenterManageService {

    @Autowired
    UserCenterManageDao userCenterManageDao;

    private Logger log= LoggerFactory.getLogger(UserCenterManageServiceImpl.class);

    @Autowired
    UserServerApi userApi;
    /**
     * 查询用户消息
     * @param param 查询条件 查询所有消息
     * @return  用户所有消息
     */
    public PagingBean queryUserMessage(UsreMessageDto param){
        return userCenterManageDao.queryUserMessage(param);
    }

    /**
     * 新增用户消息
     * @return
     */
    public void addUserMessage(TbUsreMessageEntity param,MemberView userInfo,int type){
        RegUtil regUtil=RegUtil.getSingleton();
        if(regUtil.isNull(userInfo.getSex())){//设置默认性别 男
            userInfo.setSex("1");
            userInfo.setMemberSex(userInfo.getSex());
        }
        if(type==1){//注册
            TMemberEntity entity= userApi.TelRegister(userInfo);
            userInfo.setMemberId(entity.getId());
        }else if(type==2){//忘记密码
            userApi.updateMemberView(userInfo);
        }else if(type==3){//微信注册登陆
            userInfo = userApi.WeiXinRegister(userInfo);
        }
        log.info("userInfo对象"+userInfo);
        log.info("userInfo.getName()对象"+userInfo.getName());

        if(regUtil.isNull(userInfo.getName())){//设置默认昵称 amoskiXXXXXXXX  amoski+8位数会员编码
            log.info(">>>>>>>>>>>>>>>save after userInfo:"+userInfo);
            Integer id=10000000+userInfo.getMemberId();
            userInfo.setId(userInfo.getMemberId());
            userInfo.setName("amoski"+id);
            userInfo.setNickname(userInfo.getName());
            userApi.updateMemberView(userInfo);
        }
        param.setMemberId(userInfo.getMemberId());
        userCenterManageDao.addUserMessage(param);
    }

    /**
     * 查询用户相册图片
     * @param param 查询条件 查询所有消息
     * @return  用户所有图片
     */
    public PagingBean queryUserPhotoImg(PhotoPicDto param){
        return userCenterManageDao.queryUserPhotoImg(param);
    }

    /**
     * 删除图片
     * @param param
     */
    public void deleteUserPic(PhotoPicDto param){
        userCenterManageDao.deleteUserPic(param);
    }
    /**
     * 新增相册图片
     */
    public void addPhotoPic(List<TbPhotoPic> list){
        userCenterManageDao.addPhotoPic(list);
    }

    /**
     * 查询用户某张图片
     * @return
     */
    public TbPhotoPic querySingleUserImg(Integer uid,Integer imgId){
        return userCenterManageDao.querySingleUserImg(uid,imgId);
    }

    /**
     * 查询一张或多张图片根据图片ids
     * @return
     */
    public List<TbPhotoPic> queryImgByImgId(String imgIds){
        return userCenterManageDao.queryImgByImgId(imgIds);
    }

    /**
     * 查询所有绑定车辆信息
     * @return
     */
    public List<VehicleInfoDto> queryAllBindVehicleInfo(VehicleInfoDto param){
        return userCenterManageDao.queryAllBindVehicleInfo(param);
    }

    /**
     * 车辆绑定
     * @param param
     */
    public void bindSelfVehicle(TbVehicleInfoEntity param){
        userCenterManageDao.bindSelfVehicle(param);
    }

    /**
     * 用户车辆信息修改
     * @param param
     */
    public void updateSelfVehicle(TbVehicleInfoEntity param,boolean bl){
        if(bl){//更新默认
            userCenterManageDao.updateDefaultVehicle(param.getMemberId()+"");
        }
        userCenterManageDao.updateSelfVehicle(param);
    }
    /**
     * 用户默认车辆信息修改
     * @param userId 用户id
     */
    public void updateDefaultVehicle(String userId){
        userCenterManageDao.updateDefaultVehicle(userId);
    }

    /**
     * 查询车辆所有品牌
     * @return
     */
    public List<VehicleBrandDto> queryAllBindVehicleBrandInfo(){
        return userCenterManageDao.queryAllBindVehicleBrandInfo();
    }

    /**
     * 查询所有车辆类型
     * @return
     */
    public List<VehicleBrandTypeDto> queryVehicleBrandType(){
        return userCenterManageDao.queryVehicleBrandType();
    }
    /**
     * 查询车辆所有品牌下的车型
     * @return
     */
    public List<VehicleBrandTypeDto> queryVehicleBrandByBrandId(VehicleBrandTypeDto param){
        return userCenterManageDao.queryVehicleBrandByBrandId(param);
    }

    /**
     * 查询某辆绑定车辆信息
     * @return
     */
    public TbVehicleInfoEntity querySingleBindVehicleInfo(String id,String userId){
        return userCenterManageDao.querySingleBindVehicleInfo(id,userId);
    }

    /**
     * 查询品牌系列数据
     * @return
     */
    public List<TbBrandSeries> queryBrandSeries(BrandSeriesDto dto){
        return userCenterManageDao.queryBrandSeries(dto);
    }

    /**
     * 批量插入天气code
     * @param list
     */
    public void addWeatherBean(List<WeatherDto> list){
        userCenterManageDao.addWeatherBean(list);
    }

    /**
     * 删除用户车辆信息
     * @param dto
     */
    public void deleteUserVehicleInfo(VehicleInfoDto dto){
        userCenterManageDao.deleteUserVehicleInfo(dto);
    }
    /**
     * 新增用户意见反馈
     * @return
     */
    public void addUserFeedback(TbUserFeedbackEntity param){
        userCenterManageDao.addUserFeedback(param);
    }

    /**
     * 公共插入实体数据
     * @param entity
     */
    public <T> void addEntity(T entity){
        userCenterManageDao.addEntity(entity);
    }

    /**
     * 公共插入实体数据
     * @param entity
     */
    public <T> void addEntity(T entity,boolean bl){
        userCenterManageDao.addEntity(entity,bl);
    }

    /**
     * 公共修改实体数据
     * @param entity
     */
    public <T> void updateEntity(T entity){
        userCenterManageDao.updateEntity(entity);
        //userCenterManageDao.update(entity);
    }

    /**
     * 批量插入
     * @return
     */
    public void bachInsert(List<Object> list){
        userCenterManageDao.bachInsert(list);
    }

    /**
     * 批量更新
     * @return
     */
    public void bachUpdate(List<Object> list){
        userCenterManageDao.bachUpdate(list);
    }

    @Override
    public void updateBasics(TbActivityBasics1Entity basicsEntity) {
        userCenterManageDao.updateBasics(basicsEntity);
    }

    @Override
    public List<TbActivitySignUpEntity> querySignUpEntity(TbActivitySignUpEntity tbActivitySignUpEntity, Map<String, Object> map) {
        return userCenterManageDao.querySignUpEntity(tbActivitySignUpEntity,map);
    }

    /**
     * 公共查询数据
     * @param entity
     */
    public <T> List<T> queryListEntity(T entity, String whereSql, Map<String,Object> map) {
        return userCenterManageDao.queryListEntity(entity,whereSql,map);
    }

    /**
     * 公共根据id查询数据
     * @param entity
     */
    public <T> T queryEntity(T entity,String id) {
        return userCenterManageDao.queryEntity(entity,id);
    }

    /**
     * 查询手机号是否已报名
     * @return
     */
    public List<ActivityApplyDto> queryMobileApply(ActivityApplyDto parma){
        return userCenterManageDao.queryMobileApply(parma);
    }
}








































