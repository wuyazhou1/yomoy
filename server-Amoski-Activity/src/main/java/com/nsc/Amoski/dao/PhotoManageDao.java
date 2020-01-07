package com.nsc.Amoski.dao;


import com.nsc.Amoski.dto.ActivityCalendarImagesDto;
import com.nsc.Amoski.dto.PhotoPicDto;
import com.nsc.Amoski.dto.UserPhotoDto;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TbPhotoPic;
import com.nsc.Amoski.entity.TbUserPhoto;

import java.util.List;

public interface PhotoManageDao {

    /**
     * 根据会员id 查询会员相册
     * @param userId 查询条件  用户id
     * @return  所有相册信息
     */
    List<UserPhotoDto> queryUserPhotoByUserId(String userId);

    /**
     * 删除相册信息
     * @param list  要删除的相册集合
     */
    void deleteData(List<Object> list);

    /**
     * 根据相册id 查询会员相册
     * @param ids 查询条件  相册id
     * @return  所有相册信息
     */
    List<TbUserPhoto> queryUserPhotoByPhotoId(String userId,String ids);


    /**
     * 查询用户相片信息
     * @param dto 查询信息
     * @return
     */
    PagingBean queryUserPicByPhotoId(PhotoPicDto dto);

    /**
     * 查询所有用户信息
     * @param dto
     * @return
     */
    PagingBean queryAllMemberInfo(MemberView dto);

    /**
     * 查询用户相片  根据id
     * @param ids 相片ids
     * @return
     */
    List<TbPhotoPic> queryUserPicById(String userId,String ids);

    /**
     * 查询活动相册
     * @param imgageDto 图片dto
     * @return
     */
    PagingBean queryActivityImg(ActivityCalendarImagesDto imgageDto);
}
