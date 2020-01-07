package com.nsc.Amoski.controller;


import com.alipay.api.AlipayApiException;
import com.nsc.Amoski.AlipayPay.AlipayPayUtils;
import com.nsc.Amoski.WXPay.WXPay;
import com.nsc.Amoski.WXPay.WXPayUtils;
import com.nsc.Amoski.entity.EnumEntity;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.*;

@RequestMapping(value="ActivityAlipayErrorCallback")
@Controller
public class ActivityAlipayErrorCallback {

    private Logger logger = LoggerFactory.getLogger(ActivityAlipayErrorCallback.class);

    @ResponseBody
    @RequestMapping(value = "AlipayErrorCallback",method = RequestMethod.POST)
    @ApiOperation(value="活动支付异步通知", notes = "活动支付异步通知", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="request",value="请求作用域",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="response",value="相应作用域",dataType="string", paramType = "query")
    })
    public void AlipayErrorCallback(HttpServletRequest request, HttpServletResponse response){
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            logger.info(name+"==>"+valueStr);
            params.put(name, valueStr);
        }
    }



    @ResponseBody
    @RequestMapping("AlipayPay")
    //@RequestMapping(value = "AlipayPay",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="活动支付", notes = "活动支付", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="request",value="请求作用域",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="response",value="相应作用域",dataType="string", paramType = "query")
    })
    public Object AlipayPay(HttpServletRequest request, HttpServletResponse response){
        AlipayPayUtils AlipayPayUtils = new AlipayPayUtils();
        UUID uuid=UUID.randomUUID();
        Object o = null;
        try {
            String openid = request.getSession().getAttribute("openid").toString();

            logger.info("is_weixin==》"+openid);
            if(Boolean.parseBoolean(openid)){
                /*o=WXPayUtils.getWxpayWxWapPayStr(request,
                        uuid.toString().replaceAll("-", ""),
                        "测试伍思遥",
                        "0.01",
                        "http://yomoy.com.cn/AmoskiWebHtmlUser/ActivityAlipayErrorCallback/AlipayErrorCallback");*/
            }else{
                o=WXPayUtils.getWxpayWapPayStr(request,
                        uuid.toString().replaceAll("-", ""),
                        "测试伍思遥",
                        "0.01",
                        "http://yomoy.com.cn/AmoskiWebHtmlUser/ActivityAlipayErrorCallback/AlipayErrorCallback");

            }
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return o;
    }


    @ResponseBody
    @RequestMapping(value = "getFile",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="获取文件", notes = "获取文件", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="fileNameUrl",value="活动文件路径",dataType="string", paramType = "insert"),
    })
    public Object getFile(HttpServletRequest request,HttpServletResponse response){
        String fileNameUrl = request.getParameter("filePath");
        byte[] arr=null;
        try {
            File file = new File(fileNameUrl);
            InputStream fis = new FileInputStream(file);
            arr = new byte[fis.available()];
            fis.read(arr);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arr;
    }

}
