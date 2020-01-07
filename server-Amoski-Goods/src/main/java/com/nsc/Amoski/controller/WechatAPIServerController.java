package com.nsc.Amoski.controller;

import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.uti.WeChatUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;

@RestController
@ResponseBody
@RequestMapping("/wechat")
public class WechatAPIServerController extends OrderServerBaseController<WechatAPIServerController>{

    @RequestMapping(value="/getWechatUserInfo",method = RequestMethod.GET)
    public String getWechatUserInfo(HttpServletRequest request){


        String signature=request.getParameter("signature");
        String timestamp=request.getParameter("timestamp");
        String nonce=request.getParameter("nonce");
        String echostr=request.getParameter("echostr");
        boolean jiami=WeChatUtil.checkSignature(signature, timestamp, nonce);//这里是对三个参数进行加密;
        System.out.println("加密"+jiami);
        System.out.println("本身"+signature);
        if(jiami){
            return echostr;
        }else{
            return "";
        }

    }

    /**
     * 微信消息时间监听
     * @param request
     * @return
     */
    @RequestMapping(value="/getWechatUserInfo",method = RequestMethod.POST)
    public String wechatEventLis(HttpServletRequest request, HttpServletResponse response) throws  Exception{
        System.out.println(">>>>>>>>>>>lllll");
        log.info(">>>>>>接受到消息了！！！！！！");
        Map<String, String> xmlData = getXmlData(request.getInputStream());//解析获取xml数据
        log.info(">>>>>>>..request msg:"+xmlData.toString());
        String respStr = dealDifferMsgType(xmlData);
        log.info(">>>>>>>>>>>>respStr:"+respStr);
        return respStr;
    }

    /**
     * 网页授权
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/loginOAuth")
    public String loginOAuth(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String responseUrl="";
        String returnStr=WeChatUtil.authorWebPage(responseUrl);
        return returnStr;
    }

    /**
     * 授权后回调获取用户信息
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getUserAuthInfo")
    public String getUserAuthInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String code=request.getParameter("code");
        log.info(">>>>>>>>>>>>>>>>> callback .responseUrl  code:"+code);
        String returnStr=WeChatUtil.getUserInfo(code);
        return returnStr;
    }

    /**
     * get请求，参数拼接在地址上
     * @param url 请求地址加参数
     * @return 响应
     */
    public String get(String url)
    {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(get);
            if(response != null && response.getStatusLine().getStatusCode() == 200)
            {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                byte [] bt=new byte[content.available()];
                int read = content.read(bt);
                result=new String(bt);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
                if(response != null)
                {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
