package com.nsc.Amoski.entity.jg;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author 李阳
 * @date 2019/12/13 15:03
 */
@Data
@ToString
public class Result implements Serializable {

    //代码
    private Integer code;
    //消息
    private String msg;
    ///内容
    private Object data;

    public Result(ReturnCode returnCode) {
        this.code = returnCode.getCode();
        this.msg = returnCode.getMsg();
    }

    public Result(ReturnCode returnCode, Object data) {
        this.code = returnCode.getCode();
        this.msg = returnCode.getMsg();
        this.data = data;
    }
}
