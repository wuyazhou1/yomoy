package com.nsc.Amoski.entity;





import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

//日志总表
@Entity
@Table(name = "LOGGER")
@Data
public class LoggerEntity  implements  Serializable{
    //日志总id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "LOGGER_SEQUENCE")
    private Integer id;
    //会员id
    @Column(name = "MEMBER_ID")
    private String memberId;
    //关联类型1.退款审核日志表
    @Column(name = "ASSOCIATION_TYPE")
    private String associationType;
    //关联id
    @Column(name = "ASSOCIATION_ID")
    private String associationId;
    //创建人
    @Column(name = "CREATE_NAME")
    private String createName;
    //创建时间
    @Column(name = "CREATE_DATA")
    private Timestamp createData;

}
