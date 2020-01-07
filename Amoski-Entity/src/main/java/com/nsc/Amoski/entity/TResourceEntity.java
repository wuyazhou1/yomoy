package com.nsc.Amoski.entity;








import javax.persistence.Column;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//元素表
@Entity
@Table(name = "T_RESOURCE")
@Data
public class TResourceEntity  implements  Serializable{
    //主键（自动生成）
    @Id
    public String id;
    //元素代码
    @Column(name = "CODE")
    public String code;
    //菜单代码
    @Column(name = "MENU_CODE")
    public String menuCode;
    //元素名称
    @Column(name = "RES_NAME")
    public String resName;
    //元素类型
    @Column(name = "RES_TYPE")
    public String resType;
    //元素备注
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