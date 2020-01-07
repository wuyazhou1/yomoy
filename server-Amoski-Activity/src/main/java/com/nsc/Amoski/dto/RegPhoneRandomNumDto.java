package com.nsc.Amoski.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegPhoneRandomNumDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String movePhone;
	private String vaildCode;
	private Long   timeStamp;
	
	@Override
	public String toString() {
		return "RegPhoneRandomNumDto [movePhone=" + movePhone + ", vaildCode=" + vaildCode + ", timeStamp=" + timeStamp
				+ "]";
	}
	
}
