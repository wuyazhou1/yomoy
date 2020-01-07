package com.nsc.Amoski.entity;

public class PageDto {

    private int page;
    private int limit;

    private int pageSize;
    private int start;

    public void setPage(int page){
        this.page=page;
    }
    public int getPage(){
        return this.page;
    }

    public void setLimit(int limit){
        this.limit=limit;
    }
    public int getLimit(){
        return this.limit;
    }
    public int getPageSize(){
        pageSize = this.limit*this.page;
        return pageSize;
    }
    public void InfoQueryParent(){
        getStart();
        getPageSize();
    }

    public int getStart(){
        int start=(this.page-1)*this.limit;
        start = start<0?0:start;
        return start;
    }

}
