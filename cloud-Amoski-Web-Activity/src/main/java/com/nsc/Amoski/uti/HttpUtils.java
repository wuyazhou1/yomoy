package com.nsc.Amoski.uti;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpUtils {
    /**
     * APISTORE_GET
     * @param strUrl
     * @param param
     * @param appcode
     * @return
     */
    public static String APISTORE(String strUrl, String param, String appcode, String Method) {

        String returnStr = null; // 返回结果定义
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(strUrl + "?" + param);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Authorization", "APPCODE " + appcode);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod(Method);
            httpURLConnection.setUseCaches(false); // 不用缓存
            httpURLConnection.connect();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            reader.close();
            returnStr = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return returnStr;
    }


    /*// 发起请求,获取内容
    public static void main(String[] args) {
        //请正确填写appcode,如果填写错误阿里云会返回 400错误或403错误
        //appcode查看地址 https://market.console.aliyun.com/imageconsole/
        String appcode = "7d69b9f54f644fdc8d9dc3852b7e092d";
        //请求地址
        String url="http://1.api.apistore.cn/idcard3";
        //请求参数
        String params = "realName=喻韬&cardNo=430105198708290028";

        //发送请求
        String result = APISTORE(url, params, appcode,"POST");
        //输出结果
        System.out.println(result);
    }*/
}