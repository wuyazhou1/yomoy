package com.nsc.Amoski.uti;
import org.weixin4j.util.SHA1;

import java.util.*;
import java.util.Collections;
public class PayUtil {
    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }
    /*public static void main(String [] args){
        Map<String, String> packageParams = new HashMap<String, String>();
        packageParams.put("url", "http://17n97122k7.imwork.net/person/waterfx.html");
        packageParams.put("noncestr", "8eN5yL2poFChxQCYx27YXSbYO3hFKa9U");
        packageParams.put("jsapi_ticket", "LIKLckvwlJT9cWIhEQTwfAXKDgwZY1aK0UqzA-SwjjUo5ZAGaK71-zln-JaL2I2MXq4IM4jUMUOKZsgJjQrBXQ");
        packageParams.put("timestamp", "1557322134");
        String createLinkString = createLinkString(packageParams);
        System.out.println(">>>>>>>>>>>>>>>>>...createLinkString:"+createLinkString);
        String signature = SHA1.encode(createLinkString);
        System.out.println(">>>>>>>>>>>>>>>>>...signature:"+signature);
    }*/
}