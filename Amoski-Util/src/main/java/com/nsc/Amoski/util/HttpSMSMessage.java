package com.nsc.Amoski.util;

import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.entity.TbActivityApplyEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpSMSMessage {
    private static Logger log= LoggerFactory.getLogger(HttpSMSMessage.class);
    private static final String sid = "357f1470fd8f563df8eece6a29b70129";
    private static final String token = "398423fcf69fea1d1a55968e67397e07";
    private static final String appid = "ffa5b2fce6434a40a18047737bc7b433";
    private static final String [] templateid = {"455495","465225"};//短信模板 0:手机号注册
    public static void main(String[] args){
        HttpSMSMessage httpSMSMessage = new HttpSMSMessage();
        String param = "123456";//验证码
        String mobile = "18107470228";
        String uid = "wusiyao18107470228";
        httpSMSMessage.sendSms(param, mobile,0);
    }

    /**
     * 发送手机验证码
     * @param code 手机验证码
     * @param mobile  手机号
     * @param temp 手机短信模板  0:免密登录 1:注册 2:忘记密码
     * @return
     */
    public static String sendSms(String code, String mobile,int temp) {
        String uid=DateUtils.getNowDay(DateUtils.DATE_FORMAT_YS)+"uid";
        String result = "";
        try {
            if(temp>=templateid.length||temp<0){//模板不存在
                return "1";
            }
            String url = getStringBuffer().append("/sendsms").toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sid", sid);
            jsonObject.put("token", token);
            jsonObject.put("appid", appid);
            jsonObject.put("templateid", templateid[temp]);
            jsonObject.put("param", code);
            jsonObject.put("mobile", mobile);
            jsonObject.put("uid", uid);

            String body = jsonObject.toJSONString();

            System.out.println("body = " + body);

            result = postJson(url, body, null);
            log.info("sendMsg>>>>>>>>>>>>>>>>>>.result:"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送报名成功提示
     * @param entity 用户报名信息
     * @return
     */
    public static String sendApplyMsg(TbActivityApplyEntity entity) {
        String uid=DateUtils.getNowDay(DateUtils.DATE_FORMAT_YS)+"uid";
        String result = "";
        try {
            String url = getStringBuffer().append("/sendsms").toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sid", sid);
            jsonObject.put("token", token);
            jsonObject.put("appid", appid);
            jsonObject.put("templateid", templateid[1]);
            jsonObject.put("param", entity.getName());
            jsonObject.put("mobile", entity.getMobile());
            jsonObject.put("uid", uid);

            String body = jsonObject.toJSONString();

            System.out.println("body = " + body);

            result = postJson(url, body, null);
            log.info("sendMsg>>>>>>>>>>>>>>>>>>.result:"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static StringBuffer getStringBuffer() {
        StringBuffer sb = new StringBuffer("https://");
        sb.append(SysConfig.getInstance().getProperty("rest_server")).append("/ol/sms");
        log.info(">>>>>>>>>>>sendMsg getStringBuffer >>>>>>>>>>>>>>str:"+sb.toString());
        return sb;
    }

    public static boolean isTest=Boolean.parseBoolean(SysConfig.getInstance().getProperty("is_test"));

    public static String postJson(String url, String body, String charset) {

        String result = null;

        if (isTest) {
            if (null == charset) {
                charset = "UTF-8";
            }
            CloseableHttpClient httpClient = null;
            HttpPost httpPost = null;
            try {
                httpClient = HttpConnectionManager.getInstance().getHttpClient();
                //HttpConnectionManager.getInstance().getHttpClient();
                httpPost = new HttpPost(url);

                // 设置连接超时,设置读取超时
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(10000)
                        .setSocketTimeout(10000)
                        .build();
                httpPost.setConfig(requestConfig);

                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-Type", "application/json;charset=utf-8");

                // 设置参数
                StringEntity se = new StringEntity(body, "UTF-8");
                httpPost.setEntity(se);
                HttpResponse response = httpClient.execute(httpPost);
                if (response != null) {
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity, charset);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            result = "config.properties中 is_test 属性值为false, 若已正确设置请求值，请设置为true后再次测试";
        }

        return result;
    }
}
