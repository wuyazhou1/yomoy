package com.nsc.Amoski.entity;


import java.util.HashMap;
import java.util.Map;

/**
 * 全应用程序的常量 及配置文件解析
 * @author zhuyj
 *
 */
public class Constants {

    /**操作成功*/
    public static final String SUCCESS_SUCCESS  = "操作成功!";
    /**操作失败*/
    public static final String FAIL_FAIL  = "操作失败!";


    /**
     * 消息MAP集合<br/>
     * ("00", "操作成功");<br/>
     * ("01", "操作失败");<br/>
     * ("02", "验证码输入错误");<br/>
     * ("03", "该账号已经注册");<br/>
     * ("04", "该账号不存在");<br/>
     * ("05", "用户名或者密码错误");<br/>
     * ("06", "用户名不能为空");<br/>
     * ("07", "密码不能为空");<br/>
     * ("08", "未登录或已超时");<br/>
     * ("09", "未查询到有效数据");<br/>
     * ("10", "系统异常");<br/>
     * ("11", "无权限访问");<br/>
     * ("12", "密码不一致");<br/>
     * ("13", "参数无效");<br/>
     */
    public static final Map<String, String> MESSAGE_MAP = new HashMap<String, String>();
    static{
        MESSAGE_MAP.put("00", "操作成功");
        MESSAGE_MAP.put("01", "操作失败");
        MESSAGE_MAP.put("02", "验证码输入错误");
        MESSAGE_MAP.put("03", "该账号已经注册");
        MESSAGE_MAP.put("04", "该账号不存在");
        MESSAGE_MAP.put("05", "用户名或者密码错误");
        MESSAGE_MAP.put("06", "用户名不能为空");
        MESSAGE_MAP.put("07", "密码不能为空");
        MESSAGE_MAP.put("08", "未登录或已超时");
        MESSAGE_MAP.put("09", "未查询到有效数据");
        MESSAGE_MAP.put("10", "系统异常");
        MESSAGE_MAP.put("11", "无权限访问");
        MESSAGE_MAP.put("12", "密码不一致");
        MESSAGE_MAP.put("13", "参数无效");

    }



    /**
     * 获取消息MAP集合，单个值<br/>
     * ("00", "操作成功");<br/>
     * ("01", "操作失败");<br/>
     * ("02", "验证码输入错误");<br/>
     * ("03", "该账号已经注册");<br/>
     * ("04", "该账号不存在");<br/>
     * ("05", "用户名或者密码错误");<br/>
     * ("06", "用户名不能为空");<br/>
     * ("07", "密码不能为空");<br/>
     * ("08", "未登录或已超时");<br/>
     * ("09", "未查询到有效数据");<br/>
     * ("10", "系统异常");<br/>
     * ("11", "无权限访问");<br/>
     * ("12", "密码不一致");<br/>
     * ("13", "参数无效");<br/>
     */
    public static Map<String, String> getMessageUniq(String code){
        Map<String, String> result = new HashMap<String, String>();
        if(Constants.MESSAGE_MAP.containsKey(code)){
            result.put("code", code);
            result.put("message",MESSAGE_MAP.get(code));
        }
        return result;
    }

    /**
     * 带时间日期格式-样式
     */
    public static final String DATE_FULL = "yyyy-MM-dd HH:mm:ss";
    /**
     * 带时间日期格式/样式
     */
    public static final String DATE_FULL_ = "yyyy/MM/dd HH:mm:ss";
    /**
     * 短日期格式-样式
     */
    public static final String DATE_YMD = "yyyy-MM-dd";
    /**
     * 短日期格式/样式
     */
    public static final String DATE_YMD_ = "yyyy/MM/dd";

    /**
     * 切换到DB数据库
     */
    public static final String DATA_DB = "dataSourceDB";

}