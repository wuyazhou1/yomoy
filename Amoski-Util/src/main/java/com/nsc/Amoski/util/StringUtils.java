package com.nsc.Amoski.util;

import org.springframework.context.ApplicationContext;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class StringUtils {
    public static Integer memberId = 79;
    public static Boolean isTextMember = true;


    public static ApplicationContext applicationContext;

    public static String redisDatabase;
    public static String redisTimeOut;
    public static String redisPoolMaxIdle;
    public static String redisPoolMinIdle;
    public static String redisPoolMaxTotal;
    public static String redisPoolMaxMaxActive;
    public static String redisClusterNodes;

    private static String filePath;

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        StringUtils.filePath = filePath;
    }

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
        System.out.println(encryptBase64("amoski_activity"));
        System.out.println(decryptBASE64("d3VzaXlhb21heXVxaW5nbG9uZ2Rhbg=="));
    }

    public static ServletContext servletContext ;

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static void setServletContext(ServletContext servletContext) {
        StringUtils.servletContext = servletContext;
    }


    /**
     * 获取年
     * @param calendar
     * @return
     */
    public static String getYearStr(Calendar calendar){
        return calendar.get(Calendar.YEAR)+"";//获取年份
    }

    /**
     * 获取月
     * @param calendar
     * @return
     */
    public static String getMonthStr(Calendar calendar){
        int month = calendar.get(Calendar.MONTH) + 1;//获取月份
        return month < 10 ? "0" + month : month + "";
    }

    /**
     * 获取日
     * @param calendar
     * @return
     */
    public static String getDayStr(Calendar calendar){
        int day = calendar.get(Calendar.DATE);//获取日
        return day < 10 ? "0" + day : day + "";
    }

    /**
     * 获取时
     * @param calendar
     * @return
     */
    public static String getHourStr(Calendar calendar){
        return calendar.get(Calendar.HOUR_OF_DAY)+"";
    }

    /**
     * 判断路径中的文件夹是否存在，没有就创建
     * @param path
     */
    public static void isFileClip(String path){
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
