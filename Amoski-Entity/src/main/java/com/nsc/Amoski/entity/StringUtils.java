package com.nsc.Amoski.entity;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public class StringUtils {
    public static Boolean isEmpty(Object obj){
        if(obj == null){
            return true;
        }
        String str = obj.toString();
        if(str.equals("") ){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 解密
     *
     * @param key
     * @return
     */
    public static String decryptBASE64(String key){
        try {
            return new String((new BASE64Decoder()).decodeBuffer(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     * @param key
     * @return
     */
    public static String encryptBase64(String key){
        return (new BASE64Encoder().encode(key.getBytes()));
    }

    public static void main(String[] args) {
        System.out.println(encryptBase64("amoski_riding"));
        System.out.println(decryptBASE64("YW1vc2tpX3JpZGluZw=="));
    }
}
