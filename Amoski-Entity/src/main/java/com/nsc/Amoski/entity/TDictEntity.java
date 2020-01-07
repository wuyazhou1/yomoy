package com.nsc.Amoski.entity;



import javax.persistence.Column;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//字典类型表
@Entity
@Table(name = "T_DICT")
@Data
public class TDictEntity  implements  Serializable{
    //主键id
    @Id
    public String id;
    //字典代码
    @Column(name = "CODE")
    public String code;
    //字典类型代码
    @Column(name = "DICT_TYPE_CODE")
    public String dictTypeCode;
    //字典名字
    @Column(name = "DICT_KEY")
    public String dictKey;
    //字典值
    @Column(name = "DICT_VALUE")
    public String dictValue;
    //字典备注
    @Column(name = "REMARK")
    public String remark;
    //创建人
    @Column(name = "CREATE_NAME")
    public String createName;
    //创建时间
    @Column(name = "CREATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createDate;
    //修改人
    @Column(name = "UPDATE_NAME")
    public String updateName;
    //修改时间
    @Column(name = "UPDATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date updateDate;

}
