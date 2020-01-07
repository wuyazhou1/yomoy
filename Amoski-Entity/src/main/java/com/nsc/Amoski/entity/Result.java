package com.nsc.Amoski.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.nsc.Amoski.entity.jg.ReturnCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *@Author: fh
 *@Desciption:统一返回json的格式
 *@Date:14:00 2018/5/30
 *@param:No such property: code for class: Script1
 */
@Data
@NoArgsConstructor
@ApiModel("信息封装类型")
public class Result<T> implements Serializable {
    //提示信息
    @ApiModelProperty("提示信息")
    private String msg;
    //提示码
    @ApiModelProperty("提示码")
    private String code ;
    ///内容
    @ApiModelProperty("内容")
    private T data;


    public Result(ReturnCode returnCode) {
        this.msg = returnCode.getMsg();
        this.code = returnCode.getCode().toString();
    }

    public Result(ReturnCode returnCode, T data) {
        this.msg = returnCode.getMsg();
        this.code = returnCode.getCode().toString();
        this.data = data;
    }
}
