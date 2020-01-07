package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.PhotoManageDao;
import com.nsc.Amoski.dto.ActivityCalendarImagesDto;
import com.nsc.Amoski.dto.PhotoPicDto;
import com.nsc.Amoski.dto.UserPhotoDto;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TbPhotoPic;
import com.nsc.Amoski.entity.TbUserPhoto;
import com.nsc.Amoski.service.PhotoManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class PhotoManageServiceImpl implements PhotoManageService {

    @Autowired
    PhotoManageDao photoManageDao;
    /**
     * 根据会员id 查询会员相册
     * @param userId 查询条件  用户id
     * @return  所有相册信息
     */
    public List<UserPhotoDto> queryUserPhotoByUserId(String userId){
        return photoManageDao.queryUserPhotoByUserId(userId);
    }

    /**
     * 删除相册信息
     * @param list  要删除的相册集合
     */
    public void deleteData(List<Object> list){
        photoManageDao.deleteData(list);
    }


    /**
     * 根据相册id 查询会员相册
     * @param ids 查询条件  相册ids
     * @return  所有相册信息
     */
    public List<TbUserPhoto> queryUserPhotoByPhotoId(String userId,String ids){
        return photoManageDao.queryUserPhotoByPhotoId(userId,ids);
    }

    /**
     * 查询用户相片信息
     * @param dto 用户id
     * @return
     */
    public PagingBean queryUserPicByPhotoId(PhotoPicDto dto){
        return photoManageDao.queryUserPicByPhotoId(dto);
    }

    /**
     * 查询所有用户信息
     * @param dto
     * @return
     */
    public PagingBean queryAllMemberInfo(MemberView dto){
        return photoManageDao.queryAllMemberInfo(dto);
    }

    /**
     * 查询用户相片  根据id
     * @param ids 相片ids
     * @return
     */
    public List<TbPhotoPic> queryUserPicById(String userId,String ids){
        return photoManageDao.queryUserPicById(userId,ids);
    }

    /**
     * 查询活动相册
     * @param imgageDto 图片dto
     * @return
     */
    public PagingBean queryActivityImg(ActivityCalendarImagesDto imgageDto){
        return photoManageDao.queryActivityImg(imgageDto);
    }

}








































