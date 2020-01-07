package com.nsc.AmoskiUser.chlientTest;

import java.io.Serializable;


public class MapVo  implements Serializable {
	private String k;
	private String v;
	public String getK() {
		return k;
	}
	public void setK(String k) {
		this.k = k;
	}
	public String getV() {
		if(!StringUtil.isNullOrEmpty(this.v)){
			this.v="0";
		}
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	public MapVo() {
	}
	public MapVo(String k, String v) {
		this.k = k;
		this.v = v;
	}
	public MapVo(Object k, Object v) {
		this.k = k.toString();
		this.v = v.toString();
	}
	
	@Override
	public String toString() {
		return k+" "+v;
	}
}
