package com.nsc.Amoski.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.util.Map;

/**
 *  base模板实体类
 */
@Data
@XStreamAlias("xml") //设置根节点名
public class BaseMsg {

    //置顶别名首字母大写
    @XStreamAlias("ToUserName")
    private String toUserName;//开发者微信号
    private String FromUserName;//发送方帐号（一个OpenID）
    private String CreateTime;//消息创建时间 （整型）
    private String MsgType;//MsgType 文本类型

    public BaseMsg(){}
    public BaseMsg(Map<String,String> map){
        this.CreateTime=System.currentTimeMillis()/1000+"";
        this.FromUserName=map.get("ToUserName");
        this.toUserName=map.get("FromUserName");
    }
}