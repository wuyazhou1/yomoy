package com.nsc.Amoski.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 李阳
 * @date 2019/12/13 20:46
 */
@Data
@ToString
@ApiModel(value = "JGIMMessage", description = "极光IM消息对象")
public class JGIMMessage implements Serializable {

    @ApiModelProperty(value = "版本号", name = "version")
    private Integer version;
    @ApiModelProperty(value = "发送目标类型", name = "target_type")
    private String target_type;
    @ApiModelProperty(value = "目标id", name = "target_id")
    private String target_id;
    @ApiModelProperty(value = "发送消息者的身份", name = "from_type")
    private String from_type;
    @ApiModelProperty(value = "发送者的username", name = "from_id")
    private String from_id;
    @ApiModelProperty(value = "发消息类型", name = "msg_type")
    private String msg_type;
    @ApiModelProperty(value = "消息体", name = "body")
    private Body body;

    @Data
    @ToString
    @ApiModel(value = "Body", description = "消息体")
    public static class Body {

        @ApiModelProperty(value = "文字", name = "text")
        private String text;
        @ApiModelProperty(value = "选填的json对象", name = "extras")
        private Map<String, String> extras;
        @ApiModelProperty(value = "", name = "numberExtras")
        private Map<String, Number> numberExtras;
        @ApiModelProperty(value = "", name = "booleanExtras")
        private Map<String, Boolean> booleanExtras;
        @ApiModelProperty(value = "媒体链接", name = "media_id")
        private String media_id;
        @ApiModelProperty(value = "文件的crc32校验码", name = "media_crc32")
        private Long media_crc32;
        @ApiModelProperty(value = "图片原始宽度", name = "width")
        private Integer width;
        @ApiModelProperty(value = "图片原始高度", name = "height")
        private Integer height;
        @ApiModelProperty(value = "图片格式", name = "format")
        private String format;
        @ApiModelProperty(value = "文件大小", name = "fsize")
        private Integer fsize;
        @ApiModelProperty(value = "音频时长", name = "duration")
        private Integer duration;
        @ApiModelProperty(value = "音频hash值", name = "hash")
        private String hash;

    }

}