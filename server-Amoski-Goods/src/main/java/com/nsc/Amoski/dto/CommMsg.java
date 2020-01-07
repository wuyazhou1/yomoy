package com.nsc.Amoski.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import javafx.scene.transform.Scale;

import java.util.Map;

/**
 *  消息文本模板实体类
 */
@XStreamAlias("xml")
public class CommMsg extends BaseMsg {

    private String MsgId;	//消息id，64位整型

    //文本消息内容 text
    private String Content;

    //图片 image
    private String PicUrl;	//图片链接（由系统生成）
    private String MediaId;	//图片消息媒体id，可以调用获取临时素材接口拉取数据。

    //语音消息 voice
    private String Format;	//语音格式，如amr，speex等
    private String Recognition;	//语音识别结果，UTF8编码

    //视频消息 video  小视频消息 shortvideo
    private String ThumbMediaId; 	//视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。

    //地理位置消息  location
    private String Location_X;	//地理位置维度
    private String Location_Y;	//地理位置经度
    private String Scale; //地图缩放大小
    private String Label;	//地理位置信息

    //链接消息  link
    private String Title;	//消息标题
    private String Description;	//消息描述
    private String Url;	//消息链接

    public CommMsg(Map<String,String> map, String Content){
        super(map);
        this.Content=Content;
        this.setMsgType("text");
    }


}
