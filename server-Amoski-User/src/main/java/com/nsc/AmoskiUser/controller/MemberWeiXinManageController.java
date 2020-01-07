package com.nsc.AmoskiUser.controller;

import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.TMemberEntity;
import com.nsc.Amoski.entity.jg.ReturnCode;
import com.nsc.AmoskiUser.service.MemberWeiXinManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value="MemberWeiXinManage")
@Api(value="MemberWeiXinManage",description = "会员管理控制层")
@Controller
public class MemberWeiXinManageController {
    Logger logger = LoggerFactory.getLogger(MemberWeiXinManageController.class);
    @Autowired
    @Lazy
    public MemberWeiXinManageService memberWeiXinManageService;
/*
    @RequestMapping(value = "WeiXinRegister",method = RequestMethod.POST)
    @ApiOperation(value="会员", notes = "微信注册接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键（自动生成）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="name",value="会员名称",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="loginname",value="登入会员名",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="password",value="登入密码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="salt",value="加密密钥",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="locked",value="是否可用",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="orgCode",value="部门代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="remark",value="备注",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="bindingIdentification",value="绑定标识备注（微信|QQ|账号|备用|备用）0代表没有绑定，1代表已经绑定",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="loginIdentification",value="登入标识（1微信，2账号）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateName",value="修改用户",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="WXId",value="主键（自动生成）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="memberId",value="会员id",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="openId",value="微信唯一标识",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="subscribe",value="关注状态（1是关注，0是未关注），未关注时获取不到其余信息",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="subscribeTime",value="用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="nickname",value="昵称",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="sex",value="用户的性别（1是男性，2是女性，0是未知）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="country",value="用户所在国家",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="province",value="用户所在省份",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="city",value="用户所在城市",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="language",value="用户的语言，简体中文为zh_CN",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="headImgUrl",value="用户头像",dataType="string", paramType = "insert"),
    })
    public Boolean WeiXinRegister(MemberView memberView){
        try {
            memberWeiXinManageService.addWeiXin(memberView);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequestMapping(value = "MemberRegister",method = RequestMethod.POST)
    @ApiOperation(value="会员", notes = "会员注册接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键（自动生成）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="name",value="会员名称",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="loginname",value="登入会员名",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="password",value="登入密码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="salt",value="加密密钥",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="locked",value="是否可用",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="orgCode",value="部门代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="remark",value="备注",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="bindingIdentification",value="绑定标识备注（微信|QQ|账号|备用|备用）0代表没有绑定，1代表已经绑定",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="loginIdentification",value="登入标识（1微信，2账号）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateName",value="修改用户",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="WXId",value="主键（自动生成）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="memberId",value="会员id",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="openId",value="微信唯一标识",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="subscribe",value="关注状态（1是关注，0是未关注），未关注时获取不到其余信息",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="subscribeTime",value="用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="nickname",value="昵称",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="sex",value="用户的性别（1是男性，2是女性，0是未知）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="country",value="用户所在国家",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="province",value="用户所在省份",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="city",value="用户所在城市",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="language",value="用户的语言，简体中文为zh_CN",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="headImgUrl",value="用户头像",dataType="string", paramType = "insert"),
    })
    public Boolean MemberRegister(MemberView memberView){
        try {
            memberWeiXinManageService.addMember(memberView);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequestMapping(value = "checkedMember",method = RequestMethod.POST)
    @ApiOperation(value="会员", notes = "会员登入接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="loginName",value="登入名称",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="password",value="登入密码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="tel",value="手机号码验证",dataType="string", paramType = "insert"),
    })
    public TMemberEntity checkedMember(String loginName, String password,String tel){
        try {
            TMemberEntity tMemberEntity = memberWeiXinManageService.checkedMember(loginName, password,tel);
            return tMemberEntity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    @RequestMapping(value = "WeiXinRegister",method = RequestMethod.POST)
    @ApiOperation(value="微信注册接口", notes = "微信注册接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键（自动生成）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="name",value="会员名称",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="loginname",value="登入会员名",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="password",value="登入密码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="salt",value="加密密钥",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="locked",value="是否可用",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="orgCode",value="部门代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="remark",value="备注",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="bindingIdentification",value="绑定标识备注（微信|QQ|账号|备用|备用）0代表没有绑定，1代表已经绑定",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="loginIdentification",value="登入标识（1微信，2账号）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateName",value="修改用户",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="WXId",value="主键（自动生成）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="memberId",value="会员id",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="openId",value="微信唯一标识",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="subscribe",value="关注状态（1是关注，0是未关注），未关注时获取不到其余信息",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="subscribeTime",value="用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="nickname",value="昵称",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="sex",value="用户的性别（1是男性，2是女性，0是未知）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="country",value="用户所在国家",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="province",value="用户所在省份",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="city",value="用户所在城市",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="language",value="用户的语言，简体中文为zh_CN",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="headImgUrl",value="用户头像",dataType="string", paramType = "insert"),
    })
    public MemberView WeiXinRegister(@RequestBody  MemberView memberView){
        long starTime = System.currentTimeMillis();
        try {
            memberWeiXinManageService.addWeiXin(memberView);
            return memberWeiXinManageService.findMemberView(null, null, memberView.getOpenId());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            long endTime = System.currentTimeMillis();
            logger.info("访问MemberManageController==》WeiXinRegister"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return null;
    }

    @RequestMapping(value = "MemberLogin",method = RequestMethod.POST)
    @ApiOperation(value="会员登入", notes = "会员登入", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="type",value="登入类型0账号手机登入，1微信登入",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="openId",value="微信唯一标识",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="loginName",value="登入会员名",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="password",value="登入密码",dataType="string", paramType = "insert"),
    })
    public MemberView MemberLogin(Integer type,String openId,String loginName,String password){
        long starTime = System.currentTimeMillis();
        try{
            switch (type){
                case 0://账号登入
                    Boolean aBoolean = memberWeiXinManageService.checkedMeneberLogin(loginName, password);
                    if(aBoolean)
                        return memberWeiXinManageService.findMemberView(loginName, loginName, null);
                    break;
                case 1://微信登入
                    Boolean aBoolean1 = memberWeiXinManageService.checkedWeiXin(openId);
                    if(aBoolean1)
                        return memberWeiXinManageService.findMemberView(loginName, loginName, openId);
                    break;
                default:
                    return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

            long endTime = System.currentTimeMillis();
            logger.info("访问MemberManageController==》MemberLogin"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        }
        return null;
    }

    @RequestMapping(value = "checkedLoginRepeat",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="验证手机号码或账号是否重复", notes = "验证手机号码或账号是否重复", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="tel",value="手机号码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="loginName",value="登入账号",dataType="string", paramType = "insert"),
    })
    public Result checkedLoginRepeat(String tel, String loginName){

        long starTime = System.currentTimeMillis();
        Map map = memberWeiXinManageService.checkedLoginRepeat(tel, loginName);
        long endTime = System.currentTimeMillis();
        logger.info("访问MemberManageController==》checkedLoginRepeat"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return new Result(ReturnCode.OK, map);
    }



    @RequestMapping(value = "TelRegister",method = RequestMethod.POST)
    @ApiOperation(value="手机号注册", notes = "手机号注册", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="bindingIdentification",value="绑定标识备注（微信|QQ|账号|备用|备用）0代表没有绑定，1代表已经绑定",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="loginIdentification",value="登入标识：0账号登入，1微信登入",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateName",value="修改用户",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="locked",value="是否可用（0不可用,1可用）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="orgCode",value="部门代码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="remark",value="备注",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="id",value="主键（自动生成）",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="name",value="会员名称",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="loginname",value="登入会员名",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="password",value="登入密码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="salt",value="加密密钥",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="tel",value="电话号码",dataType="string", paramType = "insert"),
    })
    public TMemberEntity TelRegister(@RequestBody  MemberView memberView){
        long starTime = System.currentTimeMillis();
        Integer random = (int) (Math.random() * 10000);
        /*if(!StringUtils.isEmpty(memberView.getPassword())){
            memberView.setSalt(random.toString());
            memberView.setPassword(PasswordUtil.setPassWordBySalt(memberView.getPassword(),memberView.getSalt()));
        }*/

        TMemberEntity tMemberEntity = memberWeiXinManageService.TelRegister(memberView);
        long endTime = System.currentTimeMillis();
        logger.info("访问MemberManageController==》TelRegister"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return tMemberEntity;
    }

    @RequestMapping(value = "updateMemberView",method = RequestMethod.POST)
    @ApiOperation(value="修改会员信息", notes = "修改会员信息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="主键（自动生成）",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="name",value="会员名称",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="loginname",value="登入会员名",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="password",value="登入密码",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="salt",value="加密密钥",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="locked",value="是否可用",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="orgCode",value="部门代码",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="remark",value="备注",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="bindingIdentification",value="绑定标识备注（微信|QQ|账号|备用|备用）0代表没有绑定，1代表已经绑定",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="loginIdentification",value="登入标识（1微信，2账号）",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="updateName",value="修改用户",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="updateDate",value="修改时间",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="WXId",value="主键（自动生成）",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="memberId",value="会员id",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="openId",value="微信唯一标识",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="subscribe",value="关注状态（1是关注，0是未关注），未关注时获取不到其余信息",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="subscribeTime",value="用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="nickname",value="昵称",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="sex",value="用户的性别（1是男性，2是女性，0是未知）",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="country",value="用户所在国家",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="province",value="用户所在省份",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="city",value="用户所在城市",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="language",value="用户的语言，简体中文为zh_CN",dataType="string", paramType = "update"),
            @ApiImplicitParam(name="headImgUrl",value="用户头像",dataType="string", paramType = "update"),
    })
    public MemberView UpdateMemberView(@RequestBody MemberView View){
        long starTime = System.currentTimeMillis();
        memberWeiXinManageService.UpdateMemberView(View);

        MemberView memberView = memberWeiXinManageService.findMemberView(null, null, View.getOpenId());
        long endTime = System.currentTimeMillis();
        logger.info("访问MemberManageController==》UpdateMemberView"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return memberView;
    }


    @RequestMapping(value = "findMemberView",method = RequestMethod.POST)
    @ApiOperation(value="获取会员信息", notes = "获取会员信息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="openId",value="微信唯一标识",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="tel",value="会员电话号码",dataType="string", paramType = "insert"),
            @ApiImplicitParam(name="loginName",value="会员登入名",dataType="string", paramType = "insert"),
            /*@ApiImplicitParam(name="request",value="请求作用域",dataType="string", paramType = "insert"),*/
    })
    public MemberView findMemberView(String openId ,String tel, String loginName){
        long starTime = System.currentTimeMillis();
        MemberView memberView = memberWeiXinManageService.findMemberView(tel, loginName, openId);
        long endTime = System.currentTimeMillis();
        logger.info("访问MemberManageController==》findMemberView"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return memberView;
    }


}
