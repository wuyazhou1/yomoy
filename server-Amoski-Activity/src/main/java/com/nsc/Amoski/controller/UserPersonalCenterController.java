package com.nsc.Amoski.controller;

import com.nsc.Amoski.FeignClient.RidingFeignClient;
import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.RidingInfoDto;
import com.nsc.Amoski.parent.DynamicCollectionParent;
import com.nsc.Amoski.parent.DynamicSpotFabulousParent;
import com.nsc.Amoski.parent.FansAttentionParent;
import com.nsc.Amoski.parent.ReleaseDynamicParent;
import com.nsc.Amoski.service.MemberService;
import com.nsc.Amoski.service.UserPersonalCenterService;
import com.nsc.Amoski.uti.ResultUtil;
import com.nsc.Amoski.util.BaseController;
import com.nsc.Amoski.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RestController
@Api(value="UserPersonalCenterController",description = "我的个人中心详情")
@RequestMapping("/UserPersonalCenter")
public class UserPersonalCenterController  extends BaseController {

    private Logger log= LoggerFactory.getLogger(UserPersonalCenterController.class);

    @Autowired
    private RidingFeignClient RidingFeignClient;

    @Autowired
    private UserPersonalCenterService UserPersonalCenterService;

    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "PersonalCenterDetail",method = RequestMethod.POST)
    @ApiOperation(value="查询我的个人中心详情", notes = "查询我的个人中心详情", httpMethod = "POST" )
    public Object PersonalCenterDatil(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> resultMap = new HashMap<>();
        MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(dto==null){
            if(StringUtils.isTextMember){
                dto = new MemberView();
                dto.setId(StringUtils.memberId);
            }else{
                return ResultUtil.error(ResultMsg.NOT_LOGIN);
            }
        }
        RidingInfoDto ridingInfoDto = new RidingInfoDto();
        ridingInfoDto.setMemberId(dto.getMemberId());
        Object result = RidingFeignClient.queryUserDisCountRidingInfo2(ridingInfoDto);
        Map<String,Object> PersonalCenterDatil = UserPersonalCenterService.PersonalCenterDatil(dto);
        resultMap.put("queryUserDisCountRidingInfo",result);//获取骑行数据
        resultMap.put("PersonalCenterDatil",PersonalCenterDatil);//个人详情数据
        return ResultUtil.success(resultMap);
    }

    @RequestMapping(value = "queryDynamicList",method = RequestMethod.POST)
    @ApiOperation(value="查询动态列表", notes = "查询动态列表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize",          value="页数",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="length",            value="一页多少条",dataType="string",paramType = "query"),
    })
    public Object queryDynamicList(HttpServletRequest request,@RequestBody ReleaseDynamicParent ReleaseDynamicParent){
        MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(dto==null){
            if(StringUtils.isTextMember){
                ReleaseDynamicParent.setMemberId(StringUtils.memberId.toString());
                ReleaseDynamicParent.InfoQueryParent();
            }else{
                return ResultUtil.error(ResultMsg.NOT_LOGIN);
            }
        }else{
            ReleaseDynamicParent.setMemberId(dto.getId().toString());
            ReleaseDynamicParent.InfoQueryParent();
        }
        Object obj = UserPersonalCenterService.queryDynamicList(ReleaseDynamicParent);
        log.info("查询动态列表==>"+obj);
        return ResultUtil.success(obj);
    }

    @RequestMapping(value = "queryFollowList",method = RequestMethod.POST)
    @ApiOperation(value="查询我关注列表", notes = "查询我关注列表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize",          value="页数",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="length",            value="一页多少条",dataType="string",paramType = "query"),
    })
    public Object queryFollowList(HttpServletRequest request,@RequestBody FansAttentionParent FansAttentionParent){
        MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(dto==null){
            if(StringUtils.isTextMember){
                FansAttentionParent.setMemberId(StringUtils.memberId.toString());
                FansAttentionParent.InfoQueryParent();
            }else{
                return ResultUtil.error(ResultMsg.NOT_LOGIN);
            }
        }else{
            FansAttentionParent.setMemberId(dto.getId().toString());
            FansAttentionParent.InfoQueryParent();
        }
        Object obj = UserPersonalCenterService.queryFollowList(FansAttentionParent);
        log.info("查询我关注列表==>"+obj);
        return ResultUtil.success(obj);
    }



    @RequestMapping(value = "queryFansList",method = RequestMethod.POST)
    @ApiOperation(value="查询我的粉丝列表", notes = "查询我的粉丝列表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize",          value="页数",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="length",            value="一页多少条",dataType="string",paramType = "query"),
    })
    public Object queryFansList(HttpServletRequest request,@RequestBody FansAttentionParent FansAttentionParent){
        MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(dto==null){
            if(StringUtils.isTextMember){
                FansAttentionParent.setMemberId(StringUtils.memberId.toString());
                FansAttentionParent.InfoQueryParent();
            }else{
                return ResultUtil.error(ResultMsg.NOT_LOGIN);
            }
        }else{
            FansAttentionParent.setMemberId(dto.getId().toString());
            FansAttentionParent.InfoQueryParent();
        }
        Object obj = UserPersonalCenterService.queryFansList(FansAttentionParent);
        log.info("查询我的粉丝列表==>"+obj);
        return ResultUtil.success(obj);
    }


    @RequestMapping(value = "queryFabulousList",method = RequestMethod.POST)
    @ApiOperation(value="查询我点的赞列表", notes = "查询我点的赞列表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize",          value="页数",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="length",            value="一页多少条",dataType="string",paramType = "query"),
    })
    public Object queryFabulousList(HttpServletRequest request,@RequestBody DynamicSpotFabulousParent DynamicSpotFabulousParent){
        MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(dto==null){
            if(StringUtils.isTextMember){
                DynamicSpotFabulousParent.setMemberId(StringUtils.memberId.toString());
                DynamicSpotFabulousParent.InfoQueryParent();
            }else{
                return ResultUtil.error(ResultMsg.NOT_LOGIN);
            }
        }else{
            DynamicSpotFabulousParent.setMemberId(dto.getId().toString());
            DynamicSpotFabulousParent.InfoQueryParent();
        }
        Object obj = UserPersonalCenterService.queryFabulousList(DynamicSpotFabulousParent);
        log.info("查询我的收到的赞列表==>"+obj);
        return ResultUtil.success(obj);
    }

    /*@RequestMapping(value = "queryFabulousListTo",method = RequestMethod.POST)
    @ApiOperation(value="查询我点的赞列表", notes = "查询我点的赞列表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize",          value="页数",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="length",            value="一页多少条",dataType="string",paramType = "query"),
    })*/
    public Object queryFabulousListTo(HttpServletRequest request,@RequestBody DynamicSpotFabulousParent DynamicSpotFabulousParent){
        MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(dto==null){
            if(StringUtils.isTextMember){
                DynamicSpotFabulousParent.setMemberId(StringUtils.memberId.toString());
                DynamicSpotFabulousParent.InfoQueryParent();
            }else{
                return ResultUtil.error(ResultMsg.NOT_LOGIN);
            }
        }else{
            DynamicSpotFabulousParent.setMemberId(dto.getId().toString());
            DynamicSpotFabulousParent.InfoQueryParent();
        }
        Object obj = UserPersonalCenterService.queryFabulousListTo(DynamicSpotFabulousParent);
        log.info("查询我的收到的赞列表==>"+obj);
        return ResultUtil.success(obj);
    }


    @RequestMapping(value = "dynamicCollectionList",method = RequestMethod.POST)
    @ApiOperation(value="动态收藏列表", notes = "动态收藏列表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageSize",          value="页数",dataType="string",paramType = "query"),
            @ApiImplicitParam(name="length",            value="一页多少条",dataType="string",paramType = "query"),
    })
    public Object dynamicCollectionList(HttpServletRequest request,@RequestBody MemberView memberDto){
        MemberView dto=memberService.getRedisUserObj(getSessionId(request),null);
        if(dto==null){
            if(StringUtils.isTextMember){
                dto = new MemberView();
                dto.setId(StringUtils.memberId);
            }else{
                log.info("收藏会员没有登入");
                return ResultUtil.error(ResultMsg.NOT_LOGIN);
            }
        }
        dto.setPageSize(memberDto.getPageSize());
        dto.setLength(memberDto.getLength());
        dto.InfoQueryParent();

        Object o = UserPersonalCenterService.dynamicCollectionList(dto);
        return ResultUtil.success(o);
    }
}
