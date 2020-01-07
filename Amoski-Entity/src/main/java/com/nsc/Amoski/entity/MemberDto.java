package com.nsc.Amoski.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberDto  implements Serializable {
    private String rn;
    //会员id
    private String id;
    private String sex;
    private String name;
    private String memberImage;
    private String distanceSum;
    private String sqrtvalue;
}
