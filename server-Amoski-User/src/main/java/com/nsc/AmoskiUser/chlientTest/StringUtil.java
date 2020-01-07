package com.nsc.AmoskiUser.chlientTest;

public class StringUtil {

	/**
	 * 判断obj是否为空
	 * @param obj需要判断的参数
	 * @return 如obj为空,则返回false,否则返回true
	 */
	public static boolean isNullOrEmpty(Object obj){
		return obj==null?false:true;
	}
	/**
	 * 根据单行字符长度参数为字符串添加换行符
	 * @param sourceString 源字符串
	 * @param lineSize     单行长度
	 * @return 按需添加换行符的字符串
	 */
	public static String changeNewLine(String sourceString,int lineSize){
		if(sourceString == null || sourceString.equals("-") || sourceString.equals("null")){
			return sourceString;
		}
		if(sourceString!=null&&sourceString.length()<=lineSize){
			return sourceString;
		}
		StringBuilder sb = new StringBuilder(sourceString);
		int count =0;
		for(int i =0;i<sb.length();i++){
			if(count==lineSize){
				sb.insert(i, "\n");
				count =0;
			}else{
				count++;
			}
		}
		return sb.toString();
	}
}
