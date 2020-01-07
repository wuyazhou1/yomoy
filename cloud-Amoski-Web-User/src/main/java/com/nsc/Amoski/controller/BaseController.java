package com.nsc.Amoski.controller;



import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Simon_Lee
 *
 */
public class BaseController {

    public Map<String,String> getHeaderParent(HttpServletRequest request){
        Map<String,String> resultMap = new HashMap<>();
        String realIP = request.getHeader("x-forwarded-for");
        String ip = request.getRemoteAddr();
        Enumeration<String> headerNames = request.getHeaderNames();
        for(;headerNames.hasMoreElements();){
            String name= (String)headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(name+"==>"+value);
            resultMap.put(name,value);
        }
        return resultMap;
    }

    /**
       * 获取访问者IP地址
       */
    public String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
// 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
// = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 查询默认自带分页start起始位置 end结束位置 orderDir排序机制 orderColumn排序列 pageNo 当前页数
     *
     * @param request
     * @return
     */
    public Map<String, Object> pageGrid(HttpServletRequest request) {

        String page = request.getParameter("page");// 当前页数
        String rows = request.getParameter("rows");// 一页多少条
        String orderColumn = request.getParameter("sidx");// 排序的字段
        String orderDir = request.getParameter("sord");// 排序的顺序asc or desc
        if (page == null || rows == null) {
            page = "1";
            rows = "-1";
        }
        int pageSize = Integer.valueOf(rows);
        int pageNo = Integer.valueOf(page);
        int start = (pageNo - 1) * pageSize;
        int end = 0;
        if (pageSize <= 0)// 前台取消分页
            end = 999999;
        else
            end = pageSize * pageNo;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("start", start);
        params.put("end", end);
        params.put("pageSize", pageSize);
        params.put("pageNo", pageNo);
        params.put("orderDir", orderDir);
        params.put("orderColumn", orderColumn);
        return params;
    }




    public void setCreateDatautil(Object object,HttpServletRequest request) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 获取obj类的字节文件对象
            Class c = object.getClass();
            // 获取该类的成员变量
            Field f = c.getDeclaredField("createName");
            // 取消语言访问检查
            f.setAccessible(true);
            // 给变量赋值
            f.set(object, "system");
            // 获取该类的成员变量
            Field d = c.getDeclaredField("createDate");
            // 取消语言访问检查
            d.setAccessible(true);
            // 给变量赋值
            d.set(object, new java.sql.Date(System.currentTimeMillis()));
            //d.set(object, format.format(new java.util.Date()));
            //d.set(object, new Date());
            setUpdateDatautil(object,request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setUpdateDatautil(Object object,HttpServletRequest request) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 获取obj类的字节文件对象
            Class c = object.getClass();
            // 获取该类的成员变量
            Field f = c.getDeclaredField("updateName");
            // 取消语言访问检查
            f.setAccessible(true);
            // 给变量赋值
            f.set(object, "system");
            // 获取该类的成员变量
            Field d = c.getDeclaredField("updateDate");
            // 取消语言访问检查
            d.setAccessible(true);
            // 给变量赋值
            d.set(object, new java.sql.Date(System.currentTimeMillis()));
            //d.set(object, format.format(new java.util.Date()));
            //d.set(object, new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}