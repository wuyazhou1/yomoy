package com.nsc.Amoski.chlientTest;

public enum LOGTYPE {
	LOGIN("登录日志"), SYSTEM("系统日志"), OPER("操作日志");
	private String value;

	private LOGTYPE(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
