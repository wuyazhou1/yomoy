package com.nsc.Amoski.dto;


import lombok.Data;

import java.sql.Timestamp;


//微信用户信息dto
@Data
public class WechatUserInfoDto {
    private String openid;//用户的唯一标识
    private String nickname;//用户昵称
    private String sex;//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    private String province;//用户个人资料填写的省份
    private String city;//普通用户个人资料填写的城市
    private String language;//语言
    private String country;//国家，如中国为CN
    private String headimgurl;//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
    //private String privilege;//用户特权信息，json 数组，如微信沃卡用户为（chinaunicom??
    private String unionid;//只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。

    private String subscribe;//是否订阅了公众号

    private long subscribe_time;//订阅时间
    private String subscribe_scene; //返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENEPROFILE LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_OTHERS 其他
    private String [] tagid_list;//用户被打上的标签ID列表
    private String qr_scene;//二维码扫码场景（开发者自定义）
    private String qr_scene_str;//二维码扫码场景描述（开发者自定义）

}
