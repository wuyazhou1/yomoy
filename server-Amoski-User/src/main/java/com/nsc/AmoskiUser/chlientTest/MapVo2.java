package com.nsc.AmoskiUser.chlientTest;


public class MapVo2 {

	private String k;
	private String v1;
	private String v2;
	private String v3;
	private String v4;
	private String v5;
	private String v6;
	
	
	
	public MapVo2() {
	}
	
	
	public MapVo2(String k, String v1, String v2, String v3, String v4) {
		this.k = k;
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.v4 = v4;
	}
	
	public MapVo2(String k, String v1, String v2, String v3, String v4,String v5) {
		this.k = k;
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.v4 = v4;
		this.v5 = v5;
	}


	public MapVo2(MapVo mv) {
		this.k=mv.getK();
		this.v1=mv.getV();
	}


	public String getK() {
		return k;
	}
	public void setK(String k) {
		this.k = k;
	}
	public String getV1() {
		if(!StringUtil.isNullOrEmpty(this.v1)){
			this.v1="0";
		}
		return v1;
	}
	public void setV1(String v1) {
		this.v1 = v1;
	}
	public String getV2() {
		if(!StringUtil.isNullOrEmpty(this.v2)){
			this.v2="0";
		}
		return v2;
	}
	public void setV2(String v2) {
		this.v2 = v2;
	}
	public String getV3() {
		if(!StringUtil.isNullOrEmpty(this.v3)){
			this.v3="0";
		}
		return v3;
	}
	public void setV3(String v3) {
		this.v3 = v3;
	}
	public String getV4() {
		if(!StringUtil.isNullOrEmpty(this.v4)){
			this.v4="0";
		}
		return v4;
	}
	public void setV4(String v4) {
		this.v4 = v4;
	}
	public String getV5() {
		if(!StringUtil.isNullOrEmpty(this.v5)){
			this.v5="0";
		}
		return v5;
	}
	public void setV5(String v5) {
		this.v5 = v5;
	}


	public String getV6() {
		return v6;
	}


	public void setV6(String v6) {
		this.v6 = v6;
	}
	
}
