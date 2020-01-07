package com.nsc.Amoski.util;

import java.io.File;

/**
 * 全局父接口
 */
public interface GolConstant {
    //手机验证码rediskey  (规则  REDIS_MOBILECODE_KEY+"_"+mobile+"_"+type)
    String REDIS_MOBILECODE_KEY="mobileCode";

    //用户信息rediskey  （规则  REDIS_USERINFO_KEY+"_"+sessionId）
    String REDIS_USERINFO_KEY="userInfo";

    //骑行队伍信息redisKey
    String REDIS_RIDING_TEAM_INFO="teamInfo";

    //用户登录类型  （规则  REDIS_LOGIN_TYPE+"_"+sessionId
    String REDIS_LOGIN_TYPE="loginType";


    //app 请求头部 token  获取key
    String REQ_HEADER_TOKEN="appToken";

    //上传基础路径
    //String BASE_UPLOAD_URL="D:/testProject/images";
    String BASE_UPLOAD_URL= "/uploadFile/"+"images";

    //高德地图web服务key
    String MAP_WEBSERVER_KEY="58b4bca9d5c138250f53308c5bd4b1e8";

    //基础域名  http://yomoy.com.cn/
    String BASE_URL="http://yomoy.com.cn/";

}
