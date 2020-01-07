package com.nsc.Amoski.util;


import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.nsc.Amoski.entity.EnumEntity;
import com.nsc.Amoski.entity.TUserEntity;

/**
 *
 * @author Simon_Lee
 *
 */
public class BaseController extends com.nsc.Amoski.repository.BaseController {

    /**
     * 查询默认自带分页start起始位置 pageSize结束位置 orderDir排序机制 orderColumn排序列 draw 前后对应值
     *
     * @param request
     * @return
     */
    public Map<String, Object> pageInfo(HttpServletRequest request) {
        String start = request.getParameter("start");// 排序的列号
        String length = request.getParameter("length");// 一页多少条
        String order = request.getParameter("order[0][column]");// 排序的列号
        String orderDir = request.getParameter("order[0][dir]");// 排序的顺序asc or
        // desc
        String orderColumn = request.getParameter("columns[" + order + "][data]");// 排序的列名
        String draw = request.getParameter("draw");// 重绘次数 和前台对应.

        if (!StringUtils.isEmpty(request.getParameter("orderDir"))) {
            orderDir = request.getParameter("orderDir");
        }
        if (!StringUtils.isEmpty(request.getParameter("orderDir"))) {
            orderColumn = request.getParameter("orderColumn");
        }
        if (start == null || length == null) {
            start = "0";
            length = "-1";
        }

        int pageSize = Integer.valueOf(length);
        int pageNo = (Integer.valueOf(start) + pageSize) / pageSize;
        if (pageSize <= 0)// 前台取消分页
            pageSize = 999999;
        else
            pageSize = pageSize * pageNo;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("start", start);
        params.put("pageSize", pageSize);
        params.put("pageNo", pageNo);
        params.put("orderDir", orderDir);
        params.put("orderColumn", orderColumn);
        params.put("draw", draw);

        return params;
    }


    public Map<String, Object> pageInfoVue(HttpServletRequest request) {
        Integer sumber = null;
        try {
            sumber = Integer.parseInt(request.getParameter("start"));// 排序的列号
        } catch (Exception e) {
            sumber = 1;
        }
        Integer length = Integer.parseInt(request.getParameter("length"));// 一页多少条
        Integer start = (sumber-1)*length;
        Integer end = sumber*length;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("start",start);
        params.put("pageSize",end);
        params.put("pageNode","1");
        params.put("draw",null);
        params.put("orderDir",null);
        params.put("orderColumn",null);
        return params;
    }

    /**
     * 获取请求作用域中的所有数据
     * @param request
     * @return
     */
    public Map<String, Object> getRequestMapAll(HttpServletRequest request){
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String name =enums.nextElement();
            String value = request.getParameter(name);
            if(!StringUtils.isEmpty(value))
                map.put(name, value);
        }
        return map;
    }

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


    /**
     * 返回不带数据集的消息对象 适合增删改 ("00", "操作成功"); ("01", "操作失败"); ("02", "验证码输入错误");
     * ("03", "该账号已经注册"); ("04", "该账号不存在"); ("05", "用户名或者密码错误"); ("06",
     * "用户名不能为空"); ("07", "密码不能为空"); ("08", "未登录或已超时"); ("09", "未查询到有效数据");
     * ("10", "系统异常"); ("11", "无权限访问");("12", "密码不一致");("13", "参数无效");
     */
    public Map<String, Object> resultData(EnumEntity.ProjectUtil projectUtil) {
        Map<String, Object> result = EnumEntity.ProjectUtil.getMessageUniq(projectUtil.getCode().toString());
        return result;
    }
    public Map<String, Object> resultData(EnumEntity.ProjectUtil projectUtil,String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", projectUtil.getCode());
        result.put("message",message);
        result.put("resultData", "");
        return result;
    }
    public Map<String, Object> resultData(Object obj) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "00");
        result.put("message","操作成功");
        result.put("resultData", obj);
        return result;
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

    public TUserEntity getTUserEntityByRequest(HttpServletRequest request){
        TUserEntity TUserEntity = new TUserEntity();
        TUserEntity.setId("1");
        TUserEntity.setCode("1");
        TUserEntity.setName("伍思遥");
        TUserEntity.setLoginname("wusiyao");
        TUserEntity.setPassword("13907478559wu");
        TUserEntity.setSalt("wusiyao");
        TUserEntity.setLocked("1");
        TUserEntity.setOrgCode("0");
        TUserEntity.setRemark("");
        return TUserEntity;
    }

    public String getSessionId(HttpServletRequest request){
        String sessionId=request.getSession().getId();
        String appToken = request.getHeader("appToken");
        if(!isNull(request.getParameter("appToken"))){
            sessionId=request.getParameter("appToken");
        }
        if(!RegUtil.getSingleton().isNull(appToken)){
            sessionId=appToken;
        }
        String reidsKey=REDIS_USERINFO_KEY+"_"+sessionId;
        return reidsKey;
    }
    public boolean isNull(Object... object) {
        if (object == null || object.equals("")) {
            return true;
        }
        String type=null;
        for (Object value : object) {
            if(value==null){
                return true;
            }
            type=value.getClass().getSimpleName().toUpperCase();
            System.out.println(type.equals(RegUtil.Type.INT.name()));
            if((type.equals(RegUtil.Type.INTEGER.name())||type.equals(RegUtil.Type.INT.name()))&&  value.equals(Integer.valueOf("0"))){
                return true;
            }
            if(type.equals(RegUtil.Type.STRING.name())&& (value.equals("")||value==null)){
                return true;
            }
            if(type.equals(RegUtil.Type.DOUBLE.name())&& (!Double.valueOf(value.toString()).isNaN() ||Double.valueOf(value.toString())==0D )){
                return true;
            }
            if(type.equals(RegUtil.Type.FLOAT.name())&& (!Float.valueOf(value.toString()).isNaN() ||Float.valueOf(value.toString())==0F )){
                return true;
            }
        }
        return false;
    }

}