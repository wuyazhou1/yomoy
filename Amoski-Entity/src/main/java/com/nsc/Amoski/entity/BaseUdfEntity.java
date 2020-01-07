package com.nsc.Amoski.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "T_USER")
@Data
public class BaseUdfEntity implements  Serializable{
    @Id
    private Integer id;
    @Column(name = "CREATE_NAME")
    private String createName;
    @Column(name = "CREATE_DATE")
    private String createDate;
    @Column(name = "UPDATE_NAME")
    private String updateName;
    @Column(name = "UPDATE_DATE")
    private String updateDate;

}
