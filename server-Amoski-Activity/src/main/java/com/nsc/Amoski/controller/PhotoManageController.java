package com.nsc.Amoski.controller;

import com.nsc.Amoski.dto.PhotoPicDto;
import com.nsc.Amoski.dto.UserPhotoDto;
import com.nsc.Amoski.entity.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/photoManage")
public class PhotoManageController extends ActivityServerBaseController<PhotoManageController> {

    /**
     * 查询用户所有相册
     * @param userId 查询参数 用户id
     * @return  所有相册信息
     */
    @RequestMapping(value="/queryUserPhoto",method = {RequestMethod.POST,RequestMethod.GET})
    public Result queryUserPhoto(String userId){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>queryUserPhoto ！！！all userId:"+userId);
        List<UserPhotoDto> list =null;
        if(!regUtil.isNull(userId)){
            list = photoManageService.queryUserPhotoByUserId(userId);//查询用户所有相册
        }
        return success(list);
    }

    /**
     * 更新相册信息（删除修改状态）
     * @param ids 更新的相册数据id
     */
    @RequestMapping(value="/updateUserPhotoInfo",method = RequestMethod.POST)
    public Result updateUserPhotoInfo(String userId,String ids){
        log.info(">>>>>>>>>>>>> updateUserPhotoInfo .requestParam userId:"+userId+"=========ids:"+ids);
        List<TbUserPhoto> photoList = photoManageService.queryUserPhotoByPhotoId(userId,ids);
        if(photoList.size()>0){//长度大于零
            List<Object> updList=new ArrayList<Object>();
            for(TbUserPhoto photo:photoList){
                photo.setStatus("0");
                updList.add(photo);
            }
            photoManageService.deleteData(updList);
        }
        return success();
    }
    /**
     * 查询相册的照片
     * @param dto 查询参数 相册id 会员id
     * @return  相册所有照片信息
     */
    @RequestMapping(value="/queryUserPicByPhotoId",method = {RequestMethod.POST,RequestMethod.GET})
    public Result queryUserPicByPhotoId(PhotoPicDto dto){
        dto.setStatus("1");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>queryUserPicByPhotoId ！！！all dto:"+dto);
        PagingBean list =photoManageService.queryUserPicByPhotoId(dto);//查询用户所有相册;
        /*if(!regUtil.isNull(dto.getMemberId())){
            list = photoManageService.queryUserPicByPhotoId(dto);//查询用户所有相册
        }*/
        return success(list);
    }
    /**
     * 查询所有用户信息
     * @param dto 查询参数
     * @return  相册所有用户信息
     */
    @RequestMapping(value="/queryAllMemberInfo",method = {RequestMethod.POST,RequestMethod.GET})
    public Result queryAllMemberInfo(MemberView dto){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>queryAllMemberInfo ！！！all dto:"+dto);
        PagingBean list =null;

        return success(list);
    }
    /**
     * 更新相片信息（删除修改状态）
     * @param ids 更新的相册数据id
     */
    @RequestMapping(value="/updateUserPicInfo",method = {RequestMethod.POST,RequestMethod.GET})
    public Result updateUserPicInfo(String userId,String ids){
        log.info(">>>>>>>>>>>>> updateUserPicInfo .requestParam userId:"+userId+"=========ids:"+ids);
        List<TbPhotoPic> photoList = photoManageService.queryUserPicById(userId,ids);
        if(photoList.size()>0){//长度大于零
            List<Object> updList=new ArrayList<Object>();
            for(TbPhotoPic pic:photoList){
                pic.setStatus("0");
                updList.add(pic);
            }
            photoManageService.deleteData(updList);
        }
        return success();
    }

   /* *//**
     * 删除印信息
     * @param id 删除的水印id
     *//*
    @RequestMapping(value="/deleteWaterMarkInfo",method = RequestMethod.POST)
    public Result deleteWaterMarkInfo(String id){
        return success();
    }*/

    private static final String baseUploadUrl="/waterMakerImg";

}
