package com.nsc.Amoski.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 分页类
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("列表返回数据模型")
public class PagingBean<T> implements Serializable {

    public static Integer DEFAULT_PAGE_SIZE = 20;

    @ApiModelProperty("查询数据返回List集合")
    private List<T> data; //data 与datatales 加载的“dataSrc"对应
    @ApiModelProperty("总记录数")
    private int recordsTotal; //总记录数
    @ApiModelProperty("过滤后的记录数")
    private int recordsFiltered;//过滤后的记录数
    private int draw; //与前台对应  为了防止跨站脚本（XSS）
    public Map<String,Object> resultMap;

    public Map<String, Object> getResultMap() {
        return resultMap;
    }
    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }
    public List<T> getData() {
        return data;
    }
    public void setData(List<T> data) {
        this.data = data;
    }
    public int getRecordsTotal() {
        return recordsTotal;
    }
    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }
    public int getRecordsFiltered() {
        return recordsFiltered;
    }
    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }
    public int getDraw() {
        return draw;
    }
    public void setDraw(int draw) {
        this.draw = draw;
    }

}
