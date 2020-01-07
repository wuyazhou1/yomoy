package com.nsc.Amoski.parent;


import com.nsc.Amoski.config.MyValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class pagingBeanParent {

    private Integer startLength;
    private Integer endLength;

    /**
     * 初始化查询参数
     */
    public void InfoQueryParent(){
        int pagesize = Integer.parseInt(this.pageSize);
        int length = Integer.parseInt(this.length);
        startLength=(pagesize-1)*length;
        endLength=pagesize*length;
    }


    @MyValidator(name="pageSize" ,ColumnName = "页数",min=0)
    private String pageSize;
    @MyValidator(name="length" ,ColumnName = "当前页条数",min=0)
    private String length;
    private String parametersZero;
    private String parametersOne;
    private String parametersTwo;
    private String parametersThree;
    private String parametersFour;
    private String parametersFive;
    private String parametersSix;
    private String parametersSeven;
    private String parametersEight;
    private String parametersNine;
    private String parametersTen;
}
