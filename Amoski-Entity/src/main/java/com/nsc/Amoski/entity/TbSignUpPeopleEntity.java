package com.nsc.Amoski.entity;





import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
//活动报名人表
@Entity
@Table(name = "TB_SIGN_UP_PEOPLE")
@Data
public class TbSignUpPeopleEntity  implements  Serializable{
    //汽车牌照
    @Column(name = "VEHICLE_LICENSE_PLATE")
    private String vehicleLicensePlate;
    //地址（省市区）
    @Column(name = "ADDRESS")
    private String address;
    //俱乐部
    @Column(name = "CLUB")
    private String club;
    //创建人
    @Column(name = "CREATE_NAME")
    private String createName;
    //创建时间
    @Column(name = "CREATE_DATA")
    private String createData;
    //修改人
    @Column(name = "UPDATE_NAME")
    private String updateName;
    //修改时间
    @Column(name = "UPDATE_DATE")
    private String updateDate;
    //报名人员id
    @Id
    private String id;
    //活动报名id
    @Column(name = "SIGN_UP_ID")
    private String signUpId;
    //活动基础id
    @Column(name = "BASICS_ID")
    private String basicsId;
    //姓名
    @Column(name = "NAME")
    private String name;
    //头像
    @Column(name = "IMAGE_URL")
    private String imageUrl;
    //电话
    @Column(name = "TEL")
    private String tel;
    //身份证
    @Column(name = "IDENTITY_NUMBER")
    private String identityNumber;
    //身份(骑士)
    @Column(name = "IDENTITY")
    private String identity;
    //摩托牌照
    @Column(name = "MOTORCYCLE_LICENSE")
    private String motorcycleLicense;

}
