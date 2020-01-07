package com.nsc.Amoski.entity;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


@Data
public class MemberView extends PagingBeanQuery  implements Serializable {

    private static final long serialVersionUID= 9031883889042479723L;

    //主键（自动生成）
    public Integer id;
    //会员名称
    public String name;
    //登入会员名
    public String loginname;
    //登入密码
    public String password;
    //加密密钥
    public String salt;
    //电话号码
    public String tel;
    //是否可用
    public String locked;
    //部门代码
    public String orgCode;
    //备注
    public String remark;
    //绑定标识备注（微信|QQ|账号|备用|备用）0代表没有绑定，1代表已经绑定
    public String bindingIdentification;
    //登入标识
    public String loginIdentification;
    //修改用户
    public String updateName;
    //修改时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date updateDate;



    //主键id
    public String dailId;
    //会员id
    public String memberDailId;
    //真实姓名
    public String realName;
    //身份证号码
    public String identityCard;
    //性别
    public String memberSex;
    //会员头像ip项目名
    public String headImgProject;
    //会员头像文件路径
    public String headImgFile;
    //会员出生年月
    public String yearOfBirth;
    //会员地址
    public String address;
    //是否认证
    public String isattestation;
    //简介
    public String synopsis;






    //主键（自动生成）
    @Id
    public String wxId;
    //会员id
    public Integer memberId;
    //微信唯一标识
    public String openId;
    //关注状态（1是关注，0是未关注），未关注时获取不到其余信息
    public String subscribe;
    //用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
    public String subscribeTime;
    //昵称
    public String nickname;
    //用户的性别（1是男性，2是女性，0是未知）
    public String sex;
    //用户所在国家
    public String country;
    //用户所在省份
    public String province;
    //用户所在城市
    public String city;
    //用户的语言，简体中文为zh_CN
    public String language;
    //用户头像
    public String headImgUrl;
    //经度Y轴
    public String yAxis;
    //纬度X轴
    public String xAxis;
    //距离
    private String distance;
    private String backgroundImages;


    /**
     * 查询条件参数，虚拟返回值
     */
    private String distanceSum;
    private String cycle;
}
