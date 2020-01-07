package com.nsc.Amoski.controller;


import com.alibaba.fastjson.JSON;
import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.parent.*;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.service.DynamicManageService;
import com.nsc.Amoski.service.MemberService;
import com.nsc.Amoski.uti.ResultUtil;
import com.nsc.Amoski.util.BaseController;
import com.nsc.Amoski.util.ImgCompress;
import com.nsc.Amoski.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Api(value="DynamicManageController",description = "动态管理模块")
@RequestMapping("/DynamicManage")
public class DynamicManageController extends BaseController {
    Logger log = LoggerFactory.getLogger(DynamicManageController.class);

    @Autowired
    public DynamicManageService dynamicManageService;

    @Autowired
    MemberService memberService;

    String REDIS_USERINFO_KEY="userInfo";




    @ResponseBody
    @RequestMapping(value = "releaseDynamic",method = RequestMethod.POST)
    @ApiOperation(value="发布动态", notes = "发布动态", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="type",          value="动态类型,1相册类型,2视频类型,3发布活动动态,4发布路书活动（必填）",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="relationTypeId",value="关联类型id",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="parentDynaminId",value="上级动态id",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="releaseAddress",value="发布地址（必填）",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="publishContent",value="发布内容（必填）",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="yAxis",         value="发布位置经度（必填）",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="xAxis",         value="发布位置纬度（必填）",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="score",         value="评分排序字段",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="state",         value="状态1为公开状态，2为私有状态（必填）",   dataType="string",paramType = "query"),
            @ApiImplicitParam(name="saveAlbum",     value="是否保存相册，0不保存相册，1保存相册",   dataType="string",paramType = "query"),
            @ApiImplicitParam(name="mentionListId",value="@我会员id,会员id以逗号隔开",dataType="string",paramType = "query"),

            @ApiImplicitParam(name="dynamicImageList[0].projectUrl",value="动态图片项目路径					",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="dynamicImageList[0].filePathUrl",value="动态图片方法路径				  	",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="dynamicImageList[0].filePath",value="动态图片路径		 			",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="dynamicImageList[0].imgCompress",value="动态压缩图片路径				",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="dynamicImageList[0].fileNameUrl",value="动态图片名称					",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="dynamicImageList[0].singleWidth",value="动态图片名称					",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="dynamicImageList[0].singleHeight",value="动态图片名称					",dataType="string", paramType = "query"),
    })
    public Object releaseDynamic(@Valid  @RequestBody ReleaseDynamicParent ReleaseDynamicParent, HttpServletRequest request){
        try {
            MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
            if(dto==null){
                if(StringUtils.isTextMember){
                    dto = new MemberView();
                    dto.setId(StringUtils.memberId);
                }else{
                    log.info("发布动态会员没有登入dto==>"+dto);
                    return ResultUtil.error(ResultMsg.NOT_LOGIN);
                }
            }
            log.info("进入发布动态ReleaseDynamicParent==》"+ReleaseDynamicParent);
            ReleaseDynamicParent.setMemberId(dto.getId().toString());
            ReleaseDynamicParent.setMemberName(dto.getName());
            ReleaseDynamicParent.setMemberImageUrl(/*dto.getHeadImgProject()+*/dto.getHeadImgFile());
            log.info("会员头id值dto.getId().toString()==>"+dto.getId().toString());
            log.info("会员登入名称dto.getLoginname()==>"+dto.getLoginname());
            log.info("会员头像路径dto.getHeadImgFile()==>"+dto.getHeadImgFile());
            log.info("发布动态对象ReleaseDynamicParent==》"+ReleaseDynamicParent);

            dynamicManageService.releaseDynamic(ReleaseDynamicParent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultMsg.UNKNOWN_EXCEPTION);
        }
        return ResultUtil.success();
    }

    @ResponseBody
    @RequestMapping(value = "dynamicShieldMember",method = RequestMethod.POST)
    @ApiOperation(value="动态会员屏蔽", notes = "动态会员屏蔽", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="shieldMemberId", value="要屏蔽的会员id(必填)",              dataType="string",paramType = "query")
    })
    public Object dynamicShieldMember(HttpServletResponse response,HttpServletRequest request,
                                      @RequestBody DynamicShieldEntity DynamicShieldEntity){
        MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(dto==null){
            if(StringUtils.isTextMember){
                dto = new MemberView();
                dto.setId(StringUtils.memberId);
            }else{
                return ResultUtil.error(ResultMsg.NOT_LOGIN);
            }
        }
        if(DynamicShieldEntity.getShieldMemberId()==null){
            return ResultUtil.error(ResultMsg.NOT_LOGIN);
        }
        DynamicShieldEntity.setMemberId(dto.getId());
        DynamicShieldEntity.setCreateDate(new Date());
        Integer count = dynamicManageService.queryObjectByEntityById(
                "select count(1) from DYNAMIC_SHIELD where member_id = :memberId and shield_Member_Id = :shieldMemberId",
                DynamicShieldEntity);
        if(count>0){
            log.info("该动态已屏蔽");
            Result res = new Result();
            res.setMsg("0");
            res.setCode("该动态已屏蔽");
            return res;
        }
        try {
            log.info("动态屏蔽会员，会员id为"+DynamicShieldEntity.getMemberId()+"屏蔽了"+DynamicShieldEntity.getShieldMemberId());
            dynamicManageService.dynamicShieldMember(DynamicShieldEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultMsg.IS_NULL);
        }
        return ResultUtil.success();
    }



    @ResponseBody
    @RequestMapping(value = "cancelDynamicShieldMember",method = RequestMethod.POST)
    @ApiOperation(value="取消动态会员屏蔽", notes = "取消动态会员屏蔽", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="shieldMemberId", value="要屏蔽的会员id(必填)",              dataType="string",paramType = "query")
    })
    public Object cancelDynamicShieldMember(HttpServletResponse response,HttpServletRequest request,
                                            @RequestBody DynamicShieldEntity DynamicShieldEntity){
        MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(dto==null){
            if(StringUtils.isTextMember){
                dto = new MemberView();
                dto.setId(StringUtils.memberId);
            }else{
                return ResultUtil.error(ResultMsg.NOT_LOGIN);
            }
        }
        if(DynamicShieldEntity.getShieldMemberId()==null){
            return ResultUtil.error(ResultMsg.NOT_LOGIN);
        }
        DynamicShieldEntity.setMemberId(dto.getId());
        DynamicShieldEntity.setCreateDate(new Date());
        Integer count = dynamicManageService.updateObjectByEntityById(
                "delete DYNAMIC_SHIELD where member_id = :memberId and shield_Member_Id = :shieldMemberId",
                DynamicShieldEntity);
        if(count>0){
            log.info("取消屏蔽成功，取消"+DynamicShieldEntity.getMemberId()+
                    "会员对"+DynamicShieldEntity.getShieldMemberId()+"会员的屏蔽");
            Result res = new Result();
            res.setMsg("0");
            res.setCode("取消屏蔽成功");
            return res;
        }else{
            log.info("没有找到屏蔽会员，没有找到"+DynamicShieldEntity.getMemberId()+
                    "会员对"+DynamicShieldEntity.getShieldMemberId()+"会员的屏蔽");
            Result res = new Result();
            res.setMsg("0");
            res.setCode("没有找到屏蔽会员");
            return res;
        }

    }


    @ResponseBody
    @RequestMapping(value = "deleteDynamic",method = RequestMethod.POST)
    @ApiOperation(value="删除动态", notes = "删除动态", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", value="动态id",              dataType="string",paramType = "query")
    })
    public Object deleteDynamic(HttpServletRequest request,@RequestBody ReleaseDynamicParent ReleaseDynamicParent){
        MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(dto==null){
            if(StringUtils.isTextMember){
                dto = new MemberView();
                dto.setId(StringUtils.memberId);
            }else{
                return ResultUtil.error(ResultMsg.NOT_LOGIN);
            }
        }
        try {
            ReleaseDynamicParent.setMemberId(dto.getId().toString());
            log.info("动态删除参数为"+ReleaseDynamicParent);
            log.info("动态删除，会员id为"+ReleaseDynamicParent.getMemberId()+"删除了动态id为"+ReleaseDynamicParent.getId());
            dynamicManageService.deleteDynamic(ReleaseDynamicParent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultMsg.IS_NULL);
        }
        return ResultUtil.success();
    }

    @ResponseBody
    @RequestMapping(value = "queryDynamicList",method = RequestMethod.POST)
    @ApiOperation(value="查询动态数据接口", notes = "查询动态数据接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize", value="页数",              dataType="string",paramType = "query"),
            @ApiImplicitParam(name="length",   value="一页多少条",        dataType="string",paramType = "query"),
            @ApiImplicitParam(name="yAxis",    value="Y_AXIS发布位置经度",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="xAxis",    value="X_AXIS发布位置纬度",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="type",     value="查询类型1关注，" +
                    "2热门，3附近，4自己发的动态，5动态id查询",            dataType="string",paramType = "query"),
            @ApiImplicitParam(name="memberId", value="会员id",              dataType="string",paramType = "query"),
            @ApiImplicitParam(name="distance", value="距离",              dataType="string",paramType = "query"),
            @ApiImplicitParam(name="id",       value="距离",              dataType="string",paramType = "query"),
    })
    public Result<List<ReleaseDynamicParent>> queryDynamicList(HttpServletRequest request,@RequestBody ReleaseDynamicParent ReleaseDynamicParent){
        long starTime = System.currentTimeMillis();
        MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(dto==null){
            if(StringUtils.isTextMember){
                dto = new MemberView();
                dto.setId(StringUtils.memberId);
            }else{
                return ResultUtil.error(ResultMsg.NOT_LOGIN);
            }
        }
        if(StringUtils.isEmpty(ReleaseDynamicParent.getMemberId())){//判断需要查询得会员id是否为空
            ReleaseDynamicParent.setMemberId(dto.getId().toString());
        }else if(!ReleaseDynamicParent.getType().equals("4")){//判断不是查询会员自己得动态，则只能查询自己的相关动态
            ReleaseDynamicParent.setMemberId(dto.getId().toString());
        }else{//查询外来会员动态需要输出日志信息
            log.info(dto.getId().toString()+"会员查看"+ReleaseDynamicParent.getMemberId()+"会员动态信息");
        }
        //ReleaseDynamicParent.setMemberId("118");
        PagingBean<ReleaseDynamicParent> pagingBean = dynamicManageService.queryDynamicList(ReleaseDynamicParent,dto);
        return ResultUtil.success(pagingBean);
    }



    @ResponseBody
    @RequestMapping(value = "dynamicComment",method = RequestMethod.POST)
    @ApiOperation(value="发布动态评论", notes = "发布动态评论", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="dynamicId",value="动态id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="replyCommentId",value="回复评论id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="replyMemberId",value="被回复会员id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="commentContent",value="评论内容",dataType="string", paramType = "query"),
    })
    public Object dynamicComment(@Valid @RequestBody DynamicCommentParent DynamicCommentParent,HttpServletRequest request){
        try {
            MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
            if(dto==null){
                if(StringUtils.isTextMember){
                    dto = new MemberView();
                    dto.setId(StringUtils.memberId);
                }else{
                    log.info("发布动态评论会员没有登入dto==>"+dto);
                    return ResultUtil.error(ResultMsg.NOT_LOGIN);
                }
            }
            DynamicCommentParent.setMemberId(dto.getId().toString());
            //DynamicCommentParent.setMemberId("2");
            /*Integer count = dynamicManageService.queryObjectByEntityById(
                    "select count(1) from DYNAMIC_COMMENT where member_id = :memberId and dynamic_Id = :dynamicId",
                    DynamicCommentParent);
            if(count>0){
                return ResultUtil.error(ResultMsg.COMMENT_QUANTITY);
            }*/
            DynamicCommentParent.setIsRead("0");
            DynamicCommentEntity DynamicCommentEntity = dynamicManageService.dynamicComment(DynamicCommentParent);
            BeanUtils.copyProperties(DynamicCommentEntity,DynamicCommentParent);
            DynamicCommentParent.setId(DynamicCommentEntity.getId().toString());
            DynamicCommentParent.setMemberId(dto.getId().toString());
            DynamicCommentParent.setMemberName(dto.getName());
            DynamicCommentParent.setMemberImages(dto.getHeadImgFile());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            log.info("创建时间"+DynamicCommentParent.getCreateDate());
            DynamicCommentParent.setCreateDate(format.format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultMsg.UNKNOWN_EXCEPTION);
        }
        log.info("发布评论成功");
        return ResultUtil.success(DynamicCommentParent);
    }



    @ResponseBody
    @RequestMapping(value = "dynamicFabulous",method = RequestMethod.POST)
    @ApiOperation(value="发布动态点赞", notes = "发布动态点赞", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="点赞id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="dynamicId",value="动态id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="isRead",value="是否查看点赞0未阅读，1已阅读",dataType="string", paramType = "query"),
    })
    public Object dynamicFabulous(@Valid @RequestBody DynamicSpotFabulousParent DynamicSpotFabulousParent,HttpServletRequest request){
        try {
            MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
            if(dto==null){
                if(StringUtils.isTextMember){
                    dto = new MemberView();
                    dto.setId(StringUtils.memberId);
                }else{
                    log.info("发布动态点赞会员没有登入dto==>"+dto);
                    return ResultUtil.error(ResultMsg.NOT_LOGIN);
                }
            }
            /*dto = new MemberView();
            dto.setId(2);*/
            DynamicSpotFabulousParent.setMemberId(dto.getId().toString());
            Integer count = dynamicManageService.queryObjectByEntityById(
                    "select count(1) from DYNAMIC_SPOT_FABULOUS where member_id = :memberId and dynamic_Id = :dynamicId and nvl(COMMENT_ID,'-1')='-1'",
                    DynamicSpotFabulousParent);
            if(count>0){
                dynamicManageService.updateObjectByEntityById(
                        "delete DYNAMIC_SPOT_FABULOUS where member_id = :memberId and dynamic_Id = :dynamicId and nvl(COMMENT_ID,'-1')='-1'",
                        DynamicSpotFabulousParent);
                log.info("取消点赞");
                return ResultUtil.success(ResultMsg.NUMBER_OF_POINTS);
            }
            DynamicSpotFabulousParent.setIsRead("0");
            dynamicManageService.dynamicFabulous(DynamicSpotFabulousParent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultMsg.UNKNOWN_EXCEPTION);
        }
        log.info("点赞成功");
        return ResultUtil.success();
    }



    @ResponseBody
    @RequestMapping(value = "commentFabulous",method = RequestMethod.POST)
    @ApiOperation(value="评论点赞", notes = "评论点赞", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="点赞id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="dynamicId",value="动态id(必填)",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="commentId",value="评论id(必填)",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="isRead",value="是否查看点赞0未阅读，1已阅读",dataType="string", paramType = "query"),
    })
    public Object commentFabulous(@Valid @RequestBody DynamicSpotFabulousParent DynamicSpotFabulousParent,HttpServletRequest request){
        try {
            MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
            if(dto==null){
                if(StringUtils.isTextMember){
                    dto = new MemberView();
                    dto.setId(StringUtils.memberId);
                }else{
                    log.info("评论点赞会员没有登入dto==>"+dto);
                    return ResultUtil.error(ResultMsg.NOT_LOGIN);
                }
            }
            /*dto = new MemberView();
            dto.setId(2);*/
            DynamicSpotFabulousParent.setMemberId(dto.getId().toString());
            Integer count = dynamicManageService.queryObjectByEntityById(
                    "select count(1) from DYNAMIC_SPOT_FABULOUS where member_id = :memberId and comment_Id = :commentId",
                    DynamicSpotFabulousParent);
            if(count>0){
                dynamicManageService.updateObjectByEntityById(
                        "delete DYNAMIC_SPOT_FABULOUS where member_id = :memberId and comment_Id = :commentId",
                        DynamicSpotFabulousParent);
                log.info("取消点赞");
                return ResultUtil.success(ResultMsg.NUMBER_OF_POINTS);
            }
            DynamicSpotFabulousParent.setIsRead("0");
            dynamicManageService.commentFabulous(DynamicSpotFabulousParent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultMsg.UNKNOWN_EXCEPTION);
        }
        log.info("点赞成功");
        return ResultUtil.success();
    }




    @ResponseBody
    @RequestMapping(value = "dynamicCollection",method = RequestMethod.POST)
    @ApiOperation(value="发布动态收藏", notes = "发布动态收藏", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="动态收藏id",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="dynamicId",value="动态id",dataType="string", paramType = "query"),
    })
    public Object dynamicCollection(@Valid @RequestBody DynamicCollectionParent DynamicCollectionParent, HttpServletRequest request){
        try {
            MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
            if(dto==null){
                if(StringUtils.isTextMember){
                    dto = new MemberView();
                    dto.setId(StringUtils.memberId);
                }else{
                    log.info("收藏会员没有登入dto==>"+dto);
                    return ResultUtil.error(ResultMsg.NOT_LOGIN);
                }
            }
            /*dto = new MemberView();
            dto.setId(2);*/
            DynamicCollectionParent.setMemberId(dto.getId().toString());
            Integer count = dynamicManageService.queryObjectByEntityById(
                    "select count(1) from DYNAMIC_COLLECTION where member_id = :memberId and dynamic_Id = :dynamicId",
                    DynamicCollectionParent);
            if(count>0){
                dynamicManageService.updateObjectByEntityById(
                        "delete DYNAMIC_COLLECTION where member_id = :memberId and dynamic_Id = :dynamicId",
                        DynamicCollectionParent);
                log.info("取消收藏");
                return ResultUtil.success(ResultMsg.HAVE_BEEN_COLLECTED);
            }
            DynamicCollectionParent.setMemberId(dto.getId().toString());
            DynamicCollectionParent.setIsRead("0");
            dynamicManageService.dynamicCollection(DynamicCollectionParent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultMsg.UNKNOWN_EXCEPTION);
        }
        log.info("收藏成功");
        return ResultUtil.success();
    }


    @RequestMapping(value = "dynamicFollow",method = RequestMethod.POST)
    @ApiOperation(value="粉丝关注", notes = "粉丝关注", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="fansMemberId",value="被关注会员id",dataType="string", paramType = "query"),
    })
    public Object dynamicFollow(@Valid @RequestBody FansAttentionParent FansAttentionParent ,HttpServletRequest request){
        try {
            MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
            if(dto==null){
                if(StringUtils.isTextMember){
                    dto = new MemberView();
                    dto.setId(StringUtils.memberId);
                }else{
                    log.info("收藏会员没有登入dto==>"+dto);
                    return ResultUtil.error(ResultMsg.NOT_LOGIN);
                }
            }
            /*dto = new MemberView();
            dto.setId(2);*/
            FansAttentionParent.setMemberId(dto.getId().toString());
            if(FansAttentionParent.getMemberId().trim().equals(FansAttentionParent.getFansMemberId().trim())){
                return ResultUtil.error(ResultMsg.IS_NULL);
            }
            Integer count = dynamicManageService.queryObjectByEntityById(
                    "select count(1) from FANS_ATTENTION where member_id = :memberId  and FANS_MEMBER_ID = :fansMemberId",
                    FansAttentionParent);
            if(count>0){
                dynamicManageService.updateObjectByEntityById(
                        "delete FANS_ATTENTION where member_id = :memberId  and FANS_MEMBER_ID = :fansMemberId",
                        FansAttentionParent);
                log.info("取消关注");
                return ResultUtil.success(ResultMsg.ALREADY_CONCERNED);
            }
            //DynamicCollectionParent.setIsRead("0");
            dynamicManageService.dynamicFansAttention(FansAttentionParent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultMsg.UNKNOWN_EXCEPTION);
        }
        Object obj = dynamicManageService.queryObjectByEntityByIdResult(
                "select * from FANS_ATTENTION a where exists " +
                        "(select * from FANS_ATTENTION b where b.member_id=a.FANS_MEMBER_ID and member_id = :memberId )"
                ,FansAttentionParent);
        log.info("关注成功");
        return ResultUtil.success(obj);
    }







    @RequestMapping(value = "dynamicFollowList",method = RequestMethod.POST)
    @ApiOperation(value="粉丝关注列表查询", notes = "粉丝关注列表查询", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize",          value="页数",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="length",            value="一页多少条",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="memberName",            value="搜索会员名称",dataType="string",paramType = "query"),
    })
    public Object dynamicFollowList(HttpServletRequest request, @RequestBody  FansAttentionParent FansAttentionParent){

        long starTime = System.currentTimeMillis();
        MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(dto==null){
            if(StringUtils.isTextMember){
                dto = new MemberView();
                dto.setId(StringUtils.memberId);
            }else{
                log.info("收藏会员没有登入dto==>"+dto);
                return ResultUtil.error(ResultMsg.NOT_LOGIN);
            }
        }
        /*dto = new MemberView();
        dto.setId(2);*/
        FansAttentionParent.setMemberId(dto.getId().toString());
        PagingBean pagingBean = dynamicManageService.dynamicFollowList(FansAttentionParent);
        return ResultUtil.success(pagingBean);
    }



    @RequestMapping(value = "dynamicFabulousList",method = RequestMethod.POST)
    @ApiOperation(value="动态点赞列表", notes = "动态点赞列表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize",          value="页数",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="length",            value="一页多少条",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="dynamicId",            value="动态id",dataType="string",paramType = "query"),
    })
    public Object dynamicFabulousList(@Valid @RequestBody DynamicSpotFabulousParent DynamicSpotFabulousParent,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(dto==null){
            if(StringUtils.isTextMember){
                dto = new MemberView();
                dto.setId(StringUtils.memberId);
            }else{
                log.info("动态点赞列表会员没有登入dto==>"+dto);
                return ResultUtil.error(ResultMsg.NOT_LOGIN);
            }
        }
        PagingBean pagingBean = dynamicManageService.dynamicFabulousList(DynamicSpotFabulousParent);
        return ResultUtil.success(pagingBean);
    }


    @RequestMapping(value = "dynamicCommentList",method = RequestMethod.POST)
    @ApiOperation(value="动态评论列表", notes = "动态评论列表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize",          value="页数",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="length",            value="一页多少条",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="dynamicId",            value="动态id",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="id",            value="评论id",dataType="string",paramType = "query"),
    })
    public Object dynamicCommentList( @RequestBody DynamicCommentParent DynamicCommentParent,HttpServletRequest request){
        long starTime = System.currentTimeMillis();
        log.info("dynamicCommentList==>DynamicCommentParent:"+DynamicCommentParent);
        MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(dto==null){
            if(StringUtils.isTextMember){
                dto = new MemberView();
                dto.setId(StringUtils.memberId);
            }else{
                log.info("收藏会员没有登入dto==>"+dto);
                return ResultUtil.error(ResultMsg.NOT_LOGIN);
            }
        }
        DynamicCommentParent.setMemberId(dto.getId().toString());
        PagingBean pagingBean = dynamicManageService.dynamicCommentList(DynamicCommentParent);
        log.info("dynamicCommentList==>PagingBean:"+pagingBean);
        return ResultUtil.success(pagingBean);
    }


    //@ResponseBody
    @RequestMapping(value = "saveImgCompressFile",method = RequestMethod.POST)
    @ApiOperation(value="保存文件", notes = "保存文件", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="files",value="文件",dataType="string", paramType = "query"),
    })
    public Object saveImgCompressFile(HttpServletRequest request,
                                      HttpServletResponse response,
                                      /*@RequestParam */String imgsSize,
                                      MultipartFile [] files) throws Exception {
        Map<String, Object> requestMapAll = getRequestMapAll(request);
        requestMapAll.putAll(getHeaderParent(request));
        List<Map<String,String>> resultList = new ArrayList<>();
        //Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
        //SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        for(MultipartFile file : files) {
            //if (!mFile.getValue().isEmpty()) {
            ActivityCalendarImagesEntity activityCalendarImagesEntity = new ActivityCalendarImagesEntity();
            UUID uuid=UUID.randomUUID();
            Calendar calendar = Calendar.getInstance();
            String path = "";
            if(System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1){
                path = "D:/home/uploadFile/images/createActivity/"+ StringUtils.getYearStr(calendar) +"/"+StringUtils.getMonthStr(calendar)+"/"+StringUtils.getDayStr(calendar)+"/"+StringUtils.getHourStr(calendar);
            }else{
                path = "/home/uploadFile/images/createActivity/"+ StringUtils.getYearStr(calendar) +"/"+StringUtils.getMonthStr(calendar)+"/"+StringUtils.getDayStr(calendar)+"/"+StringUtils.getHourStr(calendar);
            }
            //MultipartFile file = entry.getValue();
            String filename = file.getOriginalFilename();
            String houZhui = filename.substring(filename.lastIndexOf(".") + 1);
            String fileName = "fileNameUrl"+uuid.toString().replaceAll("-","")+"."+houZhui;
            String imgCompress = "imgCompress"+uuid.toString().replaceAll("-","")+"."+houZhui;
            File filepath = new File(path);
            if (!filepath.exists())
                filepath.mkdirs();
            boolean b = saveFile(file, path, fileName);
            if(!b){
                return this.resultData(EnumEntity.ProjectUtil.操作失败);
            }
            try {
                //压缩图
                ImgCompress icp=new ImgCompress(path+"/"+fileName);
                //int width=300, height=160;
                //icp.resize(width,height,path+"/"+imgCompress);
                icp.equalRatioResize(path+"/"+imgCompress);
            } catch (Exception e) {
                e.printStackTrace();
                return this.resultData(EnumEntity.ProjectUtil.操作失败);
            }
            Map<String,String> resultMap = new HashMap<>();
            resultMap.put("projectUrl","AmoskiActivity");
            resultMap.put("filePathUrl","/DynamicManage/getFile?fileNameUrl=");
            resultMap.put("filePath",path+"/"+fileName);
            resultMap.put("imgCompress",path+"/"+imgCompress);
            resultMap.put("fileNameUrl",filename);
            log.info(""+resultMap.size());
            resultList.add(resultMap);
            //}
        }
        return ResultUtil.success(resultList);//this.resultData(resultList);
    }


    //@ResponseBody
    @RequestMapping(value = "getFile",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="获取文件", notes = "获取文件", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="fileNameUrl",value="活动文件路径",dataType="string", paramType = "query"),
    })
    public void getFile(HttpServletRequest request,HttpServletResponse response){
        String fileNameUrl = request.getParameter("fileNameUrl");
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
    /***
     * 保存文件
     * @param file
     * @return
     */
    private boolean saveFile(MultipartFile file, String path,String FileName) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                // 文件保存路径
                //String savePath = path + file.getOriginalFilename();
                String savePath = path +"/"+ FileName;
                // 转存文件
                file.transferTo(new File(savePath));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }



}
