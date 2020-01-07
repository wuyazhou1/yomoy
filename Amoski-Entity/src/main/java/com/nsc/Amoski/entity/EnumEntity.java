package com.nsc.Amoski.entity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnumEntity {

    public enum ProjectUtil{
        操作成功("000","操作成功"),
        操作失败("001","操作失败"),
        验证码输入错误("002","验证码输入错误"),
        该账号已经注册("003","该账号已经注册"),
        该账号不存在("004","该账号不存在"),
        用户名或者密码错误("005","用户名或者密码错误"),
        用户名不能为空("006","用户名不能为空"),
        密码不能为空("007","密码不能为空"),
        未登录或已超时("008","未登录或已超时"),
        未查询到有效数据("009","未查询到有效数据"),
        系统异常("010","系统异常"),
        无权限访问("011","无权限访问"),
        密码不一致("012","密码不一致"),
        参数无效("013","参数无效"),
        抛出异常提示("014","此异常为自定义异常")


        ;
        private String code;
        private String title;
        private ProjectUtil(String code , String title){
            this.code = code;
            this.title = title;
        }
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public static String getCode(String code) {
            for (ProjectUtil ms : ProjectUtil.values()) {
                if (ms.getCode().equals(code) ) {
                    return ms.code;
                }
            }
            return null;
        }
        public static String getTitle(String title) {
            for (ProjectUtil ms : ProjectUtil.values()) {
                if (ms.getTitle().equals(title) ) {
                    return ms.title;
                }
            }
            return null;
        }
        public static ProjectUtil getProjectUtil(String code) {
            for (ProjectUtil ms : ProjectUtil.values()) {
                if (ms.getCode().equals(code) ) {
                    return ms;
                }
            }
            return null;
        }
        public static List<ProjectUtil> getAllEnum() {
            List<ProjectUtil> mss = new ArrayList<>();
            for (ProjectUtil ms : ProjectUtil.values()) {
                mss.add(ms);
            }
            return mss;
        }
        public static Map<String, Object> getMessageUniq(String code,Object object){
            Map<String, Object> result = new HashMap<String, Object>();
            ProjectUtil projectUtil = getProjectUtil(code);
            if(!StringUtils.isEmpty(projectUtil)){
                result.put("code", projectUtil.getCode());
                result.put("message",projectUtil.getTitle());
                result.put("resultData", object);
            }
            return result;
        }
        public static Map<String, Object> getMessageUniq(String code){
            Map<String, Object> result = new HashMap<String, Object>();
            ProjectUtil projectUtil = getProjectUtil(code);
            if(!StringUtils.isEmpty(projectUtil)){
                result.put("code", projectUtil.getCode());
                result.put("message",projectUtil.getTitle());
                result.put("resultData", "");
            }
            return result;
        }
    }
}
