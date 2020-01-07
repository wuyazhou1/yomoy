package com.nsc.Amoski.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//部门表
@Entity
@Table(name = "T_DEPARTMENT")
@Data
public class TDepartmentEntity  implements  Serializable{
    //主键（自动生成）
    @Id
    public String id;
    //部门名称
    @Column(name = "ORG_NAME")
    public String orgName;
    //上级部门
    @Column(name = "PARENT_ID")
    public String parentId;
    //备注
    @Column(name = "REMARK")
    public String remark;
    //null
    @Column(name = "CREATE_NAME")
    public String createName;
    //null
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}