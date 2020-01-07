package com.nsc.Amoski.uti;

import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.entity.Result;

/**
 *@Author: fh
 *@Desciption: 返回工具类
 *@Date:14:00 2018/5/30
 *@param:No such property: code for class: Script1
 */
public class ResultUtil {
	
    public static <T> Result success(T t){
        Result res=new Result();
        res.setMsg(ResultMsg.SUCCESS.getMessage());
        res.setCode(ResultMsg.SUCCESS.getCode());
        res.setData(t);
        return res;
    }
    public static Result success() {
        Result res=new Result();
        res.setMsg(ResultMsg.SUCCESS.getMessage());
        res.setCode(ResultMsg.SUCCESS.getCode());
        return res;
    }
    public static Result error(String code,String msg){
        Result res=new Result();
        res.setMsg(msg);
        res.setCode(code);
        return res;
    }

    public static Result error(ResultMsg msg){
        Result res=new Result();
        res.setMsg(msg.getMessage());
        res.setCode(msg.getCode());
        return res;
    }
    public static <T> Result error(ResultMsg msg,T t){
        Result res=new Result();
        res.setMsg(msg.getMessage());
        res.setCode(msg.getCode());
        res.setData(t);
        return res;
    }

}
