package com.nsc.Amoski.entity;

import lombok.Data;


@Data
public class PagingBeanQuery {

    private Integer startLength;
    private Integer endLength;

    /**
     * 初始化查询参数
     */
    public void InfoQueryParent(){
        try {
            int pagesize = Integer.parseInt(this.pageSize);
            int length = Integer.parseInt(this.length);
            startLength=(pagesize-1)*length;
            endLength=pagesize*length;
        } catch (Exception e) {
            throw new RuntimeException("请输入页数，和条数");
        }
    }


    private String pageSize;
    private String length;
}
