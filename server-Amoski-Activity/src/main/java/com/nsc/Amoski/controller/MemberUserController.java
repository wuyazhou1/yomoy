package com.nsc.Amoski.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.entity.jg.ReturnCode;
import com.nsc.Amoski.uti.WeChatDecoder;
import com.nsc.Amoski.uti.WeChatUtil;
import com.nsc.Amoski.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.*;


@RestController
@RequestMapping(value = "memberUser")
@Api(value = "memberUser", description = "memberUser")
@Controller
public class MemberUserController extends ActivityServerBaseController<MemberUserController> {

    /**
     * 小程序获取openid和sessionKey
     *
     * @param code
     * @return
     */
    @ApiOperation(value = "小程序获取openid和sessionKey")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "授权码", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(path = "/getOpenId")
    public Result getOpenId(@RequestParam String code) {
        Map map = WeChatUtil.getUserOpenidByCode(code);
        log.info("==================={}", map);
        return new Result(ReturnCode.OK, map);
    }

    /**
     * 解密小程序的加密数据
     *
     * @param data
     * @return
     */
    @ApiOperation(value = "解密小程序的加密数据", httpMethod = "POST")
    @PostMapping(path = "/decryptData")
    public Result decryptData(@RequestBody HashMap<String, String> data) {
        if (StringUtils.isEmpty(data.get("iv"))) return error(ResultMsg.IS_NULL);
        if (StringUtils.isEmpty(data.get("session_key"))) return error(ResultMsg.IS_NULL);
        if (StringUtils.isEmpty(data.get("encryptedData"))) return error(ResultMsg.IS_NULL);
        String dataInfo = WeChatDecoder.getData(data.get("encryptedData").toString(), data.get("session_key").toString(), data.get("iv").toString());
        log.info("==================={}", dataInfo);
        return new Result(ReturnCode.OK, JSONObject.parseObject(dataInfo, Map.class));
    }

    /**
     * 网页授权
     *
     * @param response
     * @param type     1.微信登陆  2.登陆后微信绑定
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/loginOAuth", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "网页授权", notes = "授权后回调获取用户信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型(1.微信登陆  2.登陆后微信绑定)", dataType = "string", paramType = "query")
    })
    public void loginOAuth(HttpServletResponse response, HttpServletRequest request, String type, String gotoUrl) throws Exception {
        String responseUrl = GolConstant.BASE_URL + "AmoskiActivity/memberUser/getUserAuthInfo?gotoUrl=" + gotoUrl;
        if ("2".equals(type)) {//微信信息绑定  根据一个参数区分
            responseUrl = GolConstant.BASE_URL + "AmoskiActivity/userCenterManage/userWechatBind";
        }
        String returnStr = WeChatUtil.authorWebPage(responseUrl);
        response.sendRedirect(returnStr);
        //request.getRequestDispatcher(returnStr).forward(request,response);
        //response.sendRedirect(returnStr);//重定向
        //return success(returnStr);
        /*response.setContentType("text/html;charset=utf-8");
        response.getWriter().print(returnStr);*/
    }

    /**
     * 授权后回调获取用户信息
     *
     * @param request
     * @param type:web app:app
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getUserAuthInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "授权后回调获取用户信息", notes = "授权后回调获取用户信息", httpMethod = "POST")
    public Result getUserAuthInfo(HttpServletRequest request, HttpServletResponse response, String type, String code, String gotoUrl) throws Exception {
        /*String type=map.get("type");
        String code=map.get("code");*/
        log.info(">>>>>>>>>>>>>param  gotoUrl:" + gotoUrl);
        if (regUtil.isNull(type)) {
            type = "web";
        }
        if (regUtil.isNull(code)) {
            return error(ResultMsg.IS_NULL);
        }
        WechatUserInfoDto dto1 = wechatGetUserInfo(code, type);
        log.info("====================response url:" + (regUtil.isNull(gotoUrl) ? webWechatLoginPageUrl : webWechatLoginPageUrl.substring(0, webWechatLoginPageUrl.indexOf("/AmoskiWebActivity")) + gotoUrl));
        if (dto1 != null) {
            if ("0".equals(dto1.getSubscribe()) && "web".equals(type)) {//公众号引导页
                response.sendRedirect(attentionWechatUrl);
                return null;
            } else {
                log.info("dto1.getOpenid()" + dto1.getOpenid());
                MemberView userObj = userApi.findMemberView(dto1.getOpenid(), null, null, null);
                log.info("userObj" + userObj);
                if (userObj == null) {
                    userObj = new MemberView();
                    wechatDtoChangeToMember(dto1, userObj);//转换
                    //消息
                    TbUsreMessageEntity param = new TbUsreMessageEntity();
                    param.setMsgContent("欢迎登录优摩游");
                    param.setMsg_title("微信注册成功");
                    param.setMsgImg("");
                    param.setMsgType("1");
                    saveMsgEntity(param, userObj, 3);
                    //userObj = userApi.WeiXinRegister(userObj);
                }
                log.info(">>>>>>>>>>>>>>>>> wechat return info  dto1:" + dto1 + ">>>>>>>>>>>>>>>userObj:" + userObj);
                if (userObj != null) {//保存成功
                    //用户信息写入redis
                    saveRedisUserInfo(request, userObj);
                    //request.getRequestDispatcher("http://192.168.5.185:8081/person/#").forward(request,response);
                    if ("app".equals(type)) {
                        return success(request.getSession().getId());
                    } else {
                        response.sendRedirect(regUtil.isNull(gotoUrl) ? webWechatLoginPageUrl : webWechatLoginPageUrl.substring(0, webWechatLoginPageUrl.indexOf("/AmoskiWebActivity")) + gotoUrl);
                        //request.getRequestDispatcher(webWechatLoginPageUrl).forward(request,response);
                        return null;
                    }
                    //return success();
                }
            }

        }
        return error(ResultMsg.FAIL);
    }

    /**
     * 跳转首页
     */
    @RequestMapping(value = "/gotoIndexPage", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "跳转首页", notes = "跳转首页", httpMethod = "POST")
    public void gotoIndexPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info(">>>>>>>>>gotoIndexPage:" + webWechatLoginPageUrl);
        request.getRequestDispatcher(webWechatLoginPageUrl).forward(request, response);
    }

    /**
     * app微信快捷登陆
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/appWechatLogin", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "app微信快捷登陆", notes = "app微信快捷登陆", httpMethod = "POST")
    public Result appWechatLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody WechatUserInfoDto dto) throws Exception {
        log.info(">>>>>>>>>>>>>>>>> appWechatLogin!!   dto:" + dto);
        MemberView userObj = userApi.findMemberView(dto.getOpenid(), null, null, null);
        if (userObj == null) {
            userObj = new MemberView();
            wechatDtoChangeToMember(dto, userObj);//转换
            TbUsreMessageEntity param = new TbUsreMessageEntity();
            param.setMsgContent("欢迎登录优摩游");
            param.setMsg_title("微信注册成功");
            param.setMsgImg("");
            param.setMsgType("1");
            saveMsgEntity(param, userObj, 3);
            //userObj=userApi.WeiXinRegister(userObj);
        }
        log.info(">>>>>>>>>>>>>>>userObj:" + userObj);
        if (userObj != null) {//保存成功
            //用户信息写入redis
            saveRedisUserInfo(request, userObj);
            //return success();
        } else {
            return error(ResultMsg.FAIL);
        }
        Result rs = error(ResultMsg.SUCCESS.getCode(), userObj.getMemberId().toString());
        rs.setData(request.getSession().getId());
        return rs;
    }

    /*@RequestMapping(value="/wechatLogin",method = RequestMethod.GET)
    @ApiOperation(value="登陆", notes = "微信授权登陆", httpMethod = "POST" )
    public Result wechatLogin(){
        log.info("request param  mobile:"+mobile+"=====pwd:"+pwd);
        //校验参数是否有效
        if(regUtil.isNull(mobile,pwd)){//参数有为空
            return error(ResultMsg.IS_NULL);
        }
        if(!regUtil.isMobile(mobile)){//手机号格式错误
            return error(ResultMsg.MOBILE_FORMAT_ERROR);
        }
        if(!regUtil.isPwdLegal(pwd)){//密码格式错误
            return error(ResultMsg.REGISTER_PWDILLEGALITY_ERROR);
        }
        Object bl=userApi.queryUserDeptInfo("0","0");
        log.info(">>>>>>>>>>>result:"+bl==null?null:bl.toString());
        //调用用户服务获取用户信息

        //用户信息写入redis
        return success();
    }*/

    /**
     * 账户密码登陆
     *
     * @return
     */
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "登陆", notes = "账户密码登陆", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pwd", value = "密码", dataType = "string", paramType = "query")
    })

    public Result userLogin(@RequestBody Map<String, String> map, HttpServletResponse response, HttpServletRequest request) {
        long starTime = System.currentTimeMillis();
        String mobile = map.get("mobile");
        String pwd = map.get("pwd");
        log.info("request param  mobile:" + mobile + "=====pwd:" + pwd);
        //校验参数是否有效
        if (regUtil.isNull(mobile, pwd)) {//参数有为空
            return error(ResultMsg.IS_NULL);
        }
        if (!regUtil.isMobile(mobile)) {//手机号格式错误
            return error(ResultMsg.MOBILE_FORMAT_ERROR);
        }
        if (!regUtil.isPwdLegal(pwd)) {//密码格式错误
            return error(ResultMsg.REGISTER_PWDILLEGALITY_ERROR);
        }
        /*String redisKey=REDIS_LOGIN_TYPE+"_"+request.getSession().getId();
        memberService.getSingValue(redisKey,1);//登陆类型*/
        log.info(">>>>>>>>>>>result:");
        //调用用户服务登陆接口
        MemberView userObj = userApi.MemberLogin(0, null, mobile, pwd);
        if (userObj == null) {//登陆失败  账号或密码错误
            return error(ResultMsg.USER_ACCOUNTORPWD_ERROR);
        }
        //用户信息写入redis
        saveRedisUserInfo(request, userObj);
        long endTime = System.currentTimeMillis();
        log.info("访问DictController==》GetDictZtreeData" + ((endTime - starTime) / 1000));
        Result rs = error(ResultMsg.SUCCESS.getCode(), userObj.getMemberId().toString());
        rs.setData(request.getSession().getId());
        log.info("登入返回信息==》" + GsonUtil.dtoToJson(rs));
        return rs;
    }

    /**
     * 发送手机验证码
     * mobile 手机号
     * type 验证码类型(1:注册 2:免密登录 3:忘记密码 4:绑定手机号或修改手机号)
     *
     * @return
     */
    @RequestMapping(value = "/getMobileCode", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "获取手机验证", notes = "发送手机验证码", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "验证码类型(1:注册 2:免密登录 3:忘记密码 4:绑定手机号或修改手机号,5.验证原手机)", dataType = "string", paramType = "query")
    })
    public Result sendMobileCode(@RequestBody Map<String, String> map, HttpServletRequest request, HttpServletResponse response) {
        long starTime = System.currentTimeMillis();
        String mobile = map.get("mobile");
        String type = map.get("type");
        /*String mobile=paramMap.get("mobile").toString();
        int type=Integer.parseInt(paramMap.get("type").toString());*/
        log.info("request param  mobile:" + mobile + "=====type:" + type);
        if (regUtil.isNull(mobile, type)) {
            return error(ResultMsg.IS_NULL);
        }
        if (Integer.parseInt(type) < 1 || Integer.parseInt(type) > 5) {//参数不在范围内
            return error(ResultMsg.IS_NULL);
        }
        if (!regUtil.isMobile(mobile)) {//手机号格式错误
            return error(ResultMsg.MOBILE_FORMAT_ERROR);
        }
        //调用用户服务 注册用户 注册成功获取返回用户信息
        MemberView userInfo = userApi.findMemberView(null, mobile, mobile, null);
        //如果type=3 获取用户信息判断是否存在
        if ("3".equals(type)) {
            if (userInfo == null) {//手机号未注册
                return error(ResultMsg.USER_NOTEXISTS);
            }
        } else if ("1".equals(type) || "4".equals(type)) {
            if (userInfo != null) {//手机号已注册
                return error(ResultMsg.REGISTER_MOBILEEXIST_ERROR);
            }
        }
        //每次发送之前看验证码是否已失效  如果未失效 则提示验证码可以继续使用
        String redisKey = REDIS_MOBILECODE_KEY + "_" + mobile + "_" + type;
        log.info(">>>>>>>>>>>>>>>>>>get mobile valid code redisKey:" + redisKey);
        Object redisCodeObj = memberService.getTelChecked(redisKey, null);//获取redis验证码

        RegPhoneRandomNumDto codeObj = null;
        if (redisCodeObj != null) {
            codeObj = (RegPhoneRandomNumDto) redisCodeObj;
            Long time = (System.currentTimeMillis() - codeObj.getTimeStamp()) / 1000;
            if (time <= 120) {//验证码还有效
                return error(ResultMsg.TWO_EFFECT_ERROR, 120 - time);
            }
        }
        //发送手机验证码
        codeObj = new RegPhoneRandomNumDto();
        codeObj.setMovePhone(mobile);
        codeObj.setTimeStamp(System.currentTimeMillis());
        codeObj.setVaildCode(UniqId.getRandomPwd(4)/*"9999"*/);
        memberService.saveMobileValidCode(redisKey, codeObj);
        String sendMsgStr = HttpSMSMessage.sendSms(codeObj.getVaildCode(), mobile, 0);//发送短信验证码
        JSONObject objects = JSONObject.parseObject(sendMsgStr);
        String code = objects.get("code").toString();
        if ("000000".equals(code)) {//发送成功
            //保存至redis   key:短信类型(免密登录、注册、修改密码)+mobile
            memberService.saveMobileValidCode(redisKey, codeObj);
        } else {
            String msg = objects.get("msg").toString();
            return error(code, msg);//发送错误
        }
        long endTime = System.currentTimeMillis();
        log.info("访问DictController==》GetDictZtreeData" + ((endTime - starTime) / 1000));
        return success(/*codeObj.getVaildCode()*/);
    }


    /**
     * 手机号注册和忘记密码
     *
     * @return
     */
    @RequestMapping(value = "/register", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "注册", notes = "手机号注册和忘记密码", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pwd", value = "密码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型(1.注册 3.忘记密码)", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "validCode", value = "验证码", dataType = "string", paramType = "query")
    })
    public Result userRegister(@RequestBody Map<String, String> map, HttpServletResponse response, HttpServletRequest request) {
        String mobile = map.get("mobile");
        String pwd = map.get("pwd");
        String type = map.get("type");
        String validCode = map.get("validCode");
        log.info("request param  mobile:" + mobile + "=====pwd:" + pwd + "=====type:" + type + "=====validCode:" + validCode);
        //校验参数是否有效
        if (regUtil.isNull(mobile, validCode, validCode, type)) {//参数有为空
            return error(ResultMsg.IS_NULL);
        }
        if (!regUtil.isMobile(mobile)) {//手机号格式错误
            return error(ResultMsg.MOBILE_FORMAT_ERROR);
        }
        if (!regUtil.isPwdLegal(pwd)) {//密码格式错误
            return error(ResultMsg.REGISTER_PWDILLEGALITY_ERROR);
        }
        if (!"1".equals(type) && !"3".equals(type)) {//type格式错误
            return error(ResultMsg.IS_NULL);
        }
        //验证验证码是否正确 (0:成功  1:验证码失效或不存在  2:验证码错误)
        int result = validMobileCodeIsTrue(mobile, type, validCode);
        switch (result) {
            case 1:
                return error(ResultMsg.MOBILECODE_NOTEXIST_ERROR);
            case 2:
                return error(ResultMsg.MOBILECODE_ERROR_ERROR);
            default:
                //调用用户服务 注册用户 注册成功获取返回用户信息
                MemberView userInfo = userApi.findMemberView(null, mobile, mobile, null);
                log.info(">>>>>>>>>>>>>>>userInfo:" + userInfo);
                if ("1".equals(type)) {
                    if (userInfo != null) {//手机号已注册
                        return error(ResultMsg.REGISTER_MOBILEEXIST_ERROR);
                    }
                    String uinq = UniqId.getRandomPwd(6);
                    userInfo = new MemberView();
                    userInfo.setLoginname(mobile);
                    userInfo.setPassword(PasswordUtil.setPassWordBySalt(pwd, uinq));
                    userInfo.setSalt(uinq);
                    userInfo.setTel(mobile);
                    userInfo.setOrgCode("-1");
                    userInfo.setBindingIdentification("0");
                    //消息
                    TbUsreMessageEntity param = new TbUsreMessageEntity();
                    param.setMsgContent("欢迎登录优摩游");
                    param.setMsg_title("注册成功");
                    param.setMsgImg("");
                    param.setMsgType("1");
                    saveMsgEntity(param, userInfo, 1);
                    //userApi.TelRegister(userInfo);
                } else if ("3".equals(type)) {
                    if (userInfo == null) {//手机号未注册
                        return error(ResultMsg.USER_NOTEXISTS);
                    }
                    //如有需要 还要判断账号是否被禁用 停用
                    String uinq = UniqId.getRandomPwd(6);
                    //调用用户服务 修改用户密码
                    userInfo.setPassword(PasswordUtil.setPassWordBySalt(pwd, uinq));
                    userInfo.setSalt(uinq);
                    //消息
                    TbUsreMessageEntity param = new TbUsreMessageEntity();
                    param.setMsgContent("重置密码成功");
                    param.setMsg_title("重置密码");
                    param.setMsgImg("");
                    param.setMsgType("1");
                    saveMsgEntity(param, userInfo, 2);
                }
                saveRedisUserInfo(request, userInfo);
                //清除session手机验证码信息
                /*String redisKey=REDIS_MOBILECODE_KEY+"_"+mobile+"_"+type;
                memberService.saveMobileValidCode(redisKey,null);*/
                Result rs = error(ResultMsg.SUCCESS.getCode(), userInfo.getMemberId().toString());
                rs.setData(request.getSession().getId());
                return rs;
        }
    }

    /**
     * 免密登录 验证码登录
     *
     * @return
     */
    @RequestMapping(value = "/codeLoginAndForgetPwd", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "验证码登录", notes = "免密登录 验证码登录", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "validCode", value = "验证码", dataType = "string", paramType = "query")
    })
    public Result validCodeLogin(@RequestBody Map<String, String> map, HttpServletResponse response, HttpServletRequest request) {
        String mobile = map.get("mobile");
        String validCode = map.get("validCode");
        log.info("request param  acoount_id:" + mobile + "=====validCode:" + validCode);
        //校验参数是否有效
        if (regUtil.isNull(mobile, validCode)) {//参数有为空
            return error(ResultMsg.IS_NULL);
        }
        if (!regUtil.isMobile(mobile)) {//手机号格式错误
            return error(ResultMsg.MOBILE_FORMAT_ERROR);
        }
        //验证验证码是否正确 (0:成功  1:验证码失效或不存在  2:验证码错误)
        int result = validMobileCodeIsTrue(mobile, "2", validCode);

        switch (result) {
            case 1:
                return error(ResultMsg.MOBILECODE_NOTEXIST_ERROR);
            case 2:
                return error(ResultMsg.MOBILECODE_ERROR_ERROR);
            default:
                //调用用户服务 获取用户信息
                MemberView userInfo = userApi.findMemberView(null, mobile, mobile, null);
                //成功则把用户信息写入redis  否则返回错误代码提示到页面
                if (userInfo == null) {//自动注册
                    userInfo = new MemberView();
                    userInfo.setLoginname(mobile);
                    userInfo.setTel(mobile);
                    userInfo.setBindingIdentification("0");
                    userInfo.setOrgCode("-1");
                    TbUsreMessageEntity param = new TbUsreMessageEntity();
                    param.setMsgContent("欢迎登录优摩游");
                    param.setMsg_title("注册成功");
                    param.setMsgType("1");
                    param.setMsgImg("");
                    saveMsgEntity(param, userInfo, 1);
                    //userApi.TelRegister(userInfo);
                }
                //如有需要 还要判断账号是否被禁用 停用
                saveRedisUserInfo(request, userInfo);
                //清除session手机验证码信息
               /* String redisKey=REDIS_MOBILECODE_KEY+"_"+mobile+"_"+2;
                memberService.saveMobileValidCode(redisKey,null);*/
                Result rs = error(ResultMsg.SUCCESS.getCode(), userInfo.getMemberId().toString());
                rs.setData(request.getSession().getId());
                return rs;
        }
    }

    @RequestMapping("/saveWeatherAddr")
    public Result saveWeatherAddr(HttpServletResponse response, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>statrt time:" + startTime);
        String jsonStr = HttpUtil.sendGet("http://www.weather.com.cn/data/city3jdata/china.html");
        System.out.println(">>>>>>>>>>>>>>>>>>weather list !!! list:" + jsonStr);
        JSONObject obj = JSONObject.parseObject(jsonStr);
        Set<String> strs = obj.keySet();
        List<WeatherDto> list = new ArrayList<WeatherDto>();
        for (String str : strs) {
            String objStr = obj.get(str).toString();
            String jsonStr1 = HttpUtil.sendGet("http://www.weather.com.cn/data/city3jdata/provshi/" + str + ".html");
            JSONObject obj1 = JSONObject.parseObject(jsonStr1);
            Set<String> strs1 = obj1.keySet();
            for (String str1 : strs1) {
                String objStr1 = obj1.get(str1).toString();
                String jsonStr2 = HttpUtil.sendGet("http://www.weather.com.cn/data/city3jdata/station/" + str + str1 + ".html");
                JSONObject obj2 = JSONObject.parseObject(jsonStr2);
                Set<String> strs2 = obj2.keySet();
                for (String str2 : strs2) {
                    String objStr2 = obj2.get(str2).toString();
                    WeatherDto dto = new WeatherDto();
                    dto.setCode(str + str1 + str2);
                    dto.setProvince(objStr);
                    dto.setCity(objStr1);
                    dto.setArea(objStr2);
                    dto.setAllAddr(objStr + "-" + objStr1 + "-" + objStr2);
                    dto.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    dto.setCreateUser("admin");
                    list.add(dto);
                }
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(">>>>>>>>>>>>>>>>>>weather list !!! list:" + list);
        userCenterManageService.addWeatherBean(list);
        long endTime = System.currentTimeMillis();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>end time:" + endTime + "======use time:" + (endTime - startTime));

        return success(list);
    }

    /**
     * 用户分享接口验证数据
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "用户分享接口验证数据", notes = "用户分享接口验证数据", httpMethod = "POST")
    @RequestMapping(value = "/userShareConfig", method = {RequestMethod.POST, RequestMethod.GET})
    public Result userShareConfig(HttpServletRequest request, @RequestBody Map<String, String> map) throws Exception {
        String url = map.get("url");
        log.info(">>>>>>>>>>>>>>>>>>userShareConfig  .request param  url:" + url);
        Map<String, Object> resultMap = validJSSDK(URLDecoder.decode(url, "UTF-8"));
        return success(resultMap);
    }

    /**
     * 用户活动报名
     */
    @RequestMapping(value = "/userActivityApply", method = {RequestMethod.POST, RequestMethod.GET})
    public Result userActivityApply(HttpServletRequest request, ActivityApplyDto dto) throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>userActivityApply  .request param  dto:" + dto);
        //校验参数是否有效
        if (regUtil.isNull(dto.getName(), dto.getMobile(), dto.getCount())) {//参数有为空
            return error(ResultMsg.IS_NULL);
        }
        if (!regUtil.isMobile(dto.getMobile())) {//手机号格式错误
            return error(ResultMsg.MOBILE_FORMAT_ERROR);
        }
        /*//查询手机号是否已报名
        List<ActivityApplyDto> apply = userCenterManageService.queryMobileApply(dto);
        if(apply!=null&&apply.size()>0){
            return error(ResultMsg.MOBILE_APPLY_ERROR);
        }*/
        dto.setCreateTime(new Timestamp(System.currentTimeMillis()));
        dto.setCreateUser("system");
        TbActivityApplyEntity entity = new TbActivityApplyEntity();
        BeanUtils.copyProperties(dto, entity);
        ActivityServerBaseController.writeEmailLog(dto);
        /*userCenterManageService.addEntity(entity);*/
        //发送短信
        /*String sendMsgStr = HttpSMSMessage.sendApplyMsg(entity);
        JSONObject objects = JSONObject.parseObject(sendMsgStr);
        String code=objects.get("code").toString();
        if(!"000000".equals(code)){//发送成功
            String msg=objects.get("msg").toString();
            return error(code,msg);//发送错误
        }*/
        return success();
    }

    /**
     * 检查版本更新  安卓当前版本
     */
    @RequestMapping(value = "/checkVersionUpd", method = {RequestMethod.POST, RequestMethod.GET})
    public Result checkVersionUpd(HttpServletRequest request, @RequestBody Map<String, Object> map) throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>checkVersionUpd  .request param  map:" + map);
        String version = map.get("version").toString();
        //Map<String,Object> map=new HashMap<>();
        //校验参数是否有效
        if (regUtil.isNull(version)) {//参数有为空
            return error(ResultMsg.IS_NULL);
        }
        String code = "110";
        //调用数据字典数据
        Object obj = userApi.GetDictZtree(code);
        log.info(">>>>>>...getDic data!!  obj:" + obj);
        String jsonStr = JSONObject.toJSONString(obj);
        JSONArray arry = JSONArray.parseArray(jsonStr);
        for (int i = 0; i < arry.size(); i++) {
            JSONObject jsonObject = arry.getJSONObject(i);
            String key = jsonObject.get("id").toString();
            Object val = jsonObject.get("name").toString();
            if ("force".equals(key)) {
                if ("false".equals(val)) {//转下bl类型
                    val = false;
                } else {
                    val = true;
                }
            } else if ("version".equals(key)) {
                if (Integer.parseInt(version) < Integer.parseInt(val.toString())) {//用户版本低于服务器版本 提示更新
                    val = true;
                } else {
                    val = false;
                }
            } else if ("versionDesc".equals(key)) {
                val = jsonObject.get("remark");
            }
            //放入返回
            map.put(key, val);
        }
        /*map.put("update",false);
        map.put("force",false);
        map.put("versionDesc","");
        map.clear();*/
        return success(map);
    }

    /**
     * 检查版本更新  ios当前版本
     */
    @RequestMapping(value = "/checkVersionUpdIos", method = {RequestMethod.POST, RequestMethod.GET})
    public Result checkVersionUpdIos(HttpServletRequest request, @RequestBody Map<String, Object> map) throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>checkVersionUpdIos  .request param  map:" + map);
        String version = map.get("version").toString();
        //Map<String,Object> map=new HashMap<>();
        //校验参数是否有效
        if (regUtil.isNull(version)) {//参数有为空
            return error(ResultMsg.IS_NULL);
        }
        String code = "111";
        //调用数据字典数据
        Object obj = userApi.GetDictZtree(code);
        log.info(">>>>>>...getDic data!!  obj:" + obj);
        String jsonStr = JSONObject.toJSONString(obj);
        JSONArray arry = JSONArray.parseArray(jsonStr);
        for (int i = 0; i < arry.size(); i++) {
            JSONObject jsonObject = arry.getJSONObject(i);
            String key = jsonObject.get("id").toString();
            Object val = jsonObject.get("name").toString();
            if ("force".equals(key)) {
                if ("false".equals(val)) {//转下bl类型
                    val = false;
                } else {
                    val = true;
                }
            } else if ("version".equals(key)) {
                if (Integer.parseInt(version.replace(".", "")) < Integer.parseInt(val.toString())) {//用户版本低于服务器版本 提示更新
                    val = true;
                } else {
                    val = false;
                }
            } else if ("versionDesc".equals(key)) {
                val = jsonObject.get("remark");
            }
            //放入返回
            map.put(key, val);
        }
        /*map.put("update",false);
        map.put("force",false);
        map.put("versionDesc","");
        map.clear();*/
        return success(map);
    }

    /**
     * 查询活动相册
     */
    @RequestMapping(value = "/queryActivityImg", method = {RequestMethod.POST, RequestMethod.GET})
    public Result queryActivityImg(HttpServletRequest request, @RequestBody ActivityCalendarImagesDto param) throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>queryActivityImg  .request param  map:" + param);
        //Map<String,Object> map=new HashMap<>();
        PagingBean list = photoManageService.queryActivityImg(param);
        /*List<PhotoPicDto> data = list.getData();//遍历数据分组
        Map<String,List<PhotoPicDto>> map=new HashMap<String, List<PhotoPicDto>>();
        for(PhotoPicDto pdto:data){
            String dtStr = DateUtils.string2TimeYs(pdto.getCreateTime(), DateUtils.DATE_FORMAT);
            List<PhotoPicDto> temp=null;
            if(map.containsKey(dtStr)){
                temp=map.get(dtStr);
            }else{
                temp=new ArrayList<PhotoPicDto>();
            }
            temp.add(pdto);
            map.put(dtStr,temp);
        }*/
        return success(list);
    }
}
