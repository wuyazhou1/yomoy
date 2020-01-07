package com.nsc.Amoski.util;


import org.apache.commons.lang3.RandomStringUtils;

public class UniqId {

	private static final char[] allSymbolArray = new char[] { '1', '2', '3', '4',
		'5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
		'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
		'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
		'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
		'U', 'V', 'W', 'X', 'Y', 'Z' };
	private static final char[] upperCaseSymbolArray = new char[] { '1', '2', '3', '4',
		'5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
		'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
		'U', 'V', 'W', 'X', 'Y', 'Z' };
	
	private static final char[] numSymbolArray = new char[] { '1', '2', '3', '4',
		'5', '6', '7', '8', '9', '0'};
	
	public static String getTimeMillis() {
	     return String.valueOf(System.currentTimeMillis());
	}
	
	public static String getRandomStr(int length) {
		 String randomStr= RandomStringUtils.random(length,upperCaseSymbolArray);
	     return  randomStr;
	}
	
	public static String getallSymbolArrayStr(int length) {
		 String randomStr= RandomStringUtils.random(length,allSymbolArray);  
	     return  randomStr;
	}
	
	public static String getRandomPwd(int pwslength){
		return RandomStringUtils.random(pwslength,numSymbolArray);
	}
	
	public static String getPayOrderId(String accountId,String payType,String randomNum){
		  String str=System.nanoTime()+accountId+payType+randomNum;
		  return str;
	}
	
	
    public static String replaceNumToX(String srcStr,int type){
		String rsaStr=srcStr;
		switch (type) {
		case 1://用户名
			if(srcStr!=null&&srcStr.length()>=2){
				rsaStr="*"+srcStr.substring(1);
				if(srcStr.length()>2){
					rsaStr="**"+srcStr.substring(srcStr.length()-1,srcStr.length());
				}
			}
			break;
		case 2://身份证号码
			if(srcStr!=null&&srcStr.length()>9){
				rsaStr=srcStr.substring(0,4)+"****"+srcStr.substring(srcStr.length()-4,srcStr.length());
			}
			break;
		case 3://银行卡号
			if(srcStr!=null&&srcStr.length()>10){
				rsaStr=srcStr.substring(0,4)+"****"+srcStr.substring(srcStr.length()-4,srcStr.length());
			}
			break;
		case 4://手机号
			if(srcStr!=null&&srcStr.length()>10){
				rsaStr=srcStr.substring(0,3)+"****"+srcStr.substring(srcStr.length()-4,srcStr.length());
			}
			break;
		default:
			break;
		}
		return rsaStr;
	}
	
	   public static void main(String [] arg){
			try {
				  
				System.out.println(UniqId.getallSymbolArrayStr(12));
			} catch (Exception e) {
				e.printStackTrace();
			}
	} 
 
	
	 
}
