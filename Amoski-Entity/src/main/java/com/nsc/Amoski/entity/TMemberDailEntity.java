package com.nsc.Amoski.entity;





import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
//会员详情表
@Entity
@Table(name = "T_MEMBER_DAIL")
@Data
public class TMemberDailEntity  implements  Serializable{
    public TMemberDailEntity(){}
    public TMemberDailEntity(MemberView memberView){
        this.setId(memberView.getDailId());
        this.setMemberId(memberView.getId().toString());
        this.setIdentityCard(memberView.getIdentityCard());
        this.setSex(memberView.getMemberSex());
        this.setHeadImgFile(memberView.getHeadImgFile());
        this.setHeadImgProject(memberView.getHeadImgProject());
        this.setYearOfBirth(memberView.getYearOfBirth());
        this.setAddress(memberView.getAddress());
        this.setIsattestation(memberView.getIsattestation());
        this.setSynopsis(memberView.getSynopsis());
        this.setRealName(memberView.getRealName());
    }

    //主键id
    @Id
    public String id;
    //会员id
    @Column(name = "MEMBER_ID")
    public String memberId;
    //真实姓名
    @Column(name = "REAL_NAME")
    public String realName;
    //身份证号码
    @Column(name = "IDENTITY_CARD")
    public String identityCard;
    //性别
    @Column(name = "SEX")
    public String sex;
    //会员头像ip项目名
    @Column(name = "HEAD_IMG_PROJECT")
    public String headImgProject;
    //会员头像文件路径
    @Column(name = "HEAD_IMG_FILE")
    public String headImgFile;
    //会员出生年月
    @Column(name = "YEAR_OF_BIRTH")
    public String yearOfBirth;
    //会员地址
    @Column(name = "ADDRESS")
    public String address;
    //是否认证
    @Column(name = "ISATTESTATION")
    public String isattestation;
    //简介
    @Column(name = "SYNOPSIS")
    public String synopsis;
}
