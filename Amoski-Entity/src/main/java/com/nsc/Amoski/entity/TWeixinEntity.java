package com.nsc.Amoski.entity;




import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
//微信表
@Entity
@Table(name = "T_WEIXIN")
@Data
public class TWeixinEntity  implements  Serializable{
    public TWeixinEntity(MemberView view){
        this.setId(view.getWxId());
        this.setMemberId(view.getId().toString());
        this.setOpenId(view.getOpenId());
        this.setSubscribe(view.getSubscribe());
        this.setSubscribeTime(view.getSubscribeTime());
        this.setNickname(view.getNickname());
        this.setSex(view.getSex());
        this.setCountry(view.getCountry());
        this.setProvince(view.getProvince());
        this.setCity(view.getCity());
        this.setLanguage(view.getLanguage());
        this.setHeadImgUrl(view.getHeadImgUrl());
    }
    //主键（自动生成）
    @Id
    public String id;
    //会员id
    @Column(name = "MEMBER_ID")
    public String memberId;
    //微信唯一标识
    @Column(name = "OPEN_ID")
    public String openId;
    //关注状态（1是关注，0是未关注），未关注时获取不到其余信息
    @Column(name = "SUBSCRIBE")
    public String subscribe;
    //用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
    @Column(name = "SUBSCRIBE_TIME")
    public String subscribeTime;
    //昵称
    @Column(name = "NICKNAME")
    public String nickname;
    //用户的性别（1是男性，2是女性，0是未知）
    @Column(name = "SEX")
    public String sex;
    //用户所在国家
    @Column(name = "COUNTRY")
    public String country;
    //用户所在省份
    @Column(name = "PROVINCE")
    public String province;
    //用户所在城市
    @Column(name = "CITY")
    public String city;
    //用户的语言，简体中文为zh_CN
    @Column(name = "LANGUAGE")
    public String language;
    //用户头像
    @Column(name = "HEAD_IMG_URL")
    public String headImgUrl;
    @Column(name = "UNIONID")
    public String unionId;


    public TWeixinEntity() {

    }
}
