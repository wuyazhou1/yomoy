package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.DynamicManageDao;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.parent.*;
import com.nsc.Amoski.service.DynamicManageService;
import com.nsc.Amoski.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class DynamicManageServiceImpl implements DynamicManageService {

    @Autowired
    private DynamicManageDao dynamicManageDao;

    @Override
    public void releaseDynamic(ReleaseDynamicParent releaseDynamicParent) {
        ReleaseDynamicEntity ReleaseDynamicEntity = new ReleaseDynamicEntity();
        BeanUtils.copyProperties(releaseDynamicParent,ReleaseDynamicEntity);
        ReleaseDynamicEntity.setCreateDate(new Date());
        //发布动态
        ReleaseDynamicEntity = dynamicManageDao.saveEntityByReleaseDynamicEntity(ReleaseDynamicEntity);
        if(StringUtils.isEmpty(releaseDynamicParent.getMentionListId())){

        }else{
            //添加@我数据
            dynamicManageDao.insertSqlUpdate(
                    "insert into DYNAMIC_CALL_ME select DYNAMIC_CALL_ME_SEQUENCE.nextval,'"+ReleaseDynamicEntity.getId().toString()+"',id,'0',sysdate " +
                            "from amoski_user.t_member where id in ( "+releaseDynamicParent.getMentionListId()+" )"
            ,releaseDynamicParent);
        }
        //上传图片
        if(releaseDynamicParent.getDynamicImageList()!=null && releaseDynamicParent.getDynamicImageList().size()>0){
            dynamicManageDao.saveDynamicImageList(releaseDynamicParent.getDynamicImageList(),ReleaseDynamicEntity.getId().toString());
        }
        //判断是否保存到我的相册
        if(releaseDynamicParent.getSaveAlbum()!=null &&
                releaseDynamicParent.getSaveAlbum().trim().equals("1")){
            dynamicManageDao.savePhotoPic(
                    "insert into TB_PHOTO_PIC" +
                            "(ID,IMG_URL,CREATE_TIME,CREATE_USER,SMALL_URL," +
                            "STATUS,BASE_URL,PHOTO_ID,MEMBER_ID,IMG_TYPE) " +
                    "values(TB_PHOTO_PIC_SEQUENCE.nextval, :filePath , sysdate , '" +releaseDynamicParent.getMemberName()+
                            "' , :imgCompress ,'1','','', '"+releaseDynamicParent.getMemberId()
                            +"' , '1')",
                    releaseDynamicParent.getDynamicImageList());
        }
        if(releaseDynamicParent.getParentDynaminId()!=null && !releaseDynamicParent.getParentDynaminId().trim().equals("")){
            //分享动态
            dynamicManageDao.bonusPointsDynamic(releaseDynamicParent.getParentDynaminId(),"3");
        }

    }

    @Override
    public PagingBean queryDynamicList(ReleaseDynamicParent ReleaseDynamicParent, MemberView dto) {
        List<ReleaseDynamicParent> list = dynamicManageDao.queryDynamicList(ReleaseDynamicParent,dto);
        int count = dynamicManageDao.queryDynamicCount(ReleaseDynamicParent,dto);
        PagingBean<ReleaseDynamicParent> pagelist = new PagingBean<ReleaseDynamicParent>();
        pagelist.setData(list);
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }

    @Override
    public DynamicCommentEntity dynamicComment(DynamicCommentParent dynamicCommentParent) {
        DynamicCommentEntity DynamicCommentEntity = new DynamicCommentEntity();
        BeanUtils.copyProperties(dynamicCommentParent,DynamicCommentEntity);
        DynamicCommentEntity.setCreateDate(new Date());
        //保存动态品论
        dynamicManageDao.dynamicComment(DynamicCommentEntity);
        if(dynamicCommentParent.getReplyCommentId()==null || dynamicCommentParent.getReplyCommentId().trim().equals("")){
            //评论成功，加分评论
            dynamicManageDao.bonusPointsDynamic(DynamicCommentEntity.getDynamicId(),"2");
        }
        return DynamicCommentEntity;
    }

    @Override
    public void dynamicFabulous(DynamicSpotFabulousParent dynamicSpotFabulousParent) {
        DynamicSpotFabulousEntity DynamicSpotFabulousEntity = new DynamicSpotFabulousEntity();
        BeanUtils.copyProperties(dynamicSpotFabulousParent,DynamicSpotFabulousEntity);
        DynamicSpotFabulousEntity.setCreateDate(new Date());
        //保存动态点赞
        dynamicManageDao.dynamicFabulous(DynamicSpotFabulousEntity);
        //动态点赞成功，加分点赞
        dynamicManageDao.bonusPointsDynamic(dynamicSpotFabulousParent.getDynamicId(),"1");
    }

    @Override
    public Integer queryObjectByEntityById(String s, Object dynamicSpotFabulousParent) {
        return dynamicManageDao.queryObjectByEntityById(s,dynamicSpotFabulousParent);
    }

    @Override
    public void dynamicCollection(DynamicCollectionParent dynamicCollectionParent) {
        DynamicCollectionEntity DynamicCollectionEntity = new DynamicCollectionEntity();
        BeanUtils.copyProperties(dynamicCollectionParent,DynamicCollectionEntity);
        DynamicCollectionEntity.setCreateDate(new Date());
        //保存app动态收藏表
        dynamicManageDao.dynamicCollection(DynamicCollectionEntity);
        //app动态收藏表成功，加分收藏
        dynamicManageDao.bonusPointsDynamic(DynamicCollectionEntity.getDynamicId(),"1");
    }

    @Override
    public void dynamicFansAttention(FansAttentionParent fansAttentionParent) {
        FansAttentionEntity FansAttentionEntity = new FansAttentionEntity();
        BeanUtils.copyProperties(fansAttentionParent,FansAttentionEntity);
        FansAttentionEntity.setCreateDate(new Date());
        //保存关注粉丝
        dynamicManageDao.dynamicFansAttention(FansAttentionEntity);
    }

    @Override
    public PagingBean dynamicFollowList(FansAttentionParent FansAttentionParent) {
        List list = dynamicManageDao.dynamicFollowList(FansAttentionParent);
        int count = dynamicManageDao.dynamicFollowCount(FansAttentionParent);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }

    @Override
    public Integer updateObjectByEntityById(String sql, Object obj) {
        return dynamicManageDao.updateObjectByEntityById(sql,obj);
    }

    @Override
    public PagingBean dynamicFabulousList(DynamicSpotFabulousParent dynamicSpotFabulousParent) {
        List list = dynamicManageDao.dynamicFabulousList(dynamicSpotFabulousParent);
        int count = dynamicManageDao.dynamicFabulousCount(dynamicSpotFabulousParent);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }

    @Override
    public PagingBean dynamicCommentList(DynamicCommentParent dynamicCommentParent) {
        List list = dynamicManageDao.dynamicCommentList(dynamicCommentParent);
        int count = dynamicManageDao.dynamicCommentCount(dynamicCommentParent);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }

    @Override
    public void commentFabulous(DynamicSpotFabulousParent DynamicSpotFabulousParent) {
        DynamicSpotFabulousEntity DynamicSpotFabulousEntity = new DynamicSpotFabulousEntity();
        BeanUtils.copyProperties(DynamicSpotFabulousParent,DynamicSpotFabulousEntity);
        DynamicSpotFabulousEntity.setCreateDate(new Date());
        //保存动态点赞
        dynamicManageDao.saveObjectEntity(DynamicSpotFabulousEntity);
    }

    @Override
    public Object queryObjectByEntityByIdResult(String s, Object o) {
        return dynamicManageDao.queryObjectByEntityByIdResult(s,o);
    }

    @Override
    public void deleteDynamic(ReleaseDynamicParent releaseDynamicParent) {
        dynamicManageDao.deleteDynamic(releaseDynamicParent);
    }

    @Override
    public void dynamicShieldMember(DynamicShieldEntity dynamicShieldEntity) {
        dynamicManageDao.dynamicShieldMember(dynamicShieldEntity);
    }
}
