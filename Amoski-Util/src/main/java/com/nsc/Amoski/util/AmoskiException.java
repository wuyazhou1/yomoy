package com.nsc.Amoski.util;


import com.nsc.Amoski.entity.EnumEntity;

/**
 * 项目自定义异常
 * @author zyj
 *
 */
public class AmoskiException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private String code = "-9999";
	private String message = "";

	public AmoskiException(EnumEntity.ProjectUtil enums) {
		this.code = enums.getCode();
		this.message = enums.getTitle();
	}

	public AmoskiException(String message) {
		this.code = "014";
		this.message = message;
	}
	
	public AmoskiException(String code,String msg) {
        this.code = code;
        this.message = msg;
    }

    public AmoskiException(String code,String msg, Throwable cause) {
        super(cause);
        this.code = code;
        this.message = msg;
    }


	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
}
