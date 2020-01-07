package com.nsc.Amoski.chlientTest;

import org.springframework.context.ApplicationContext;

public class StringUtil {
	public static ApplicationContext application;
	//
	public static String redisDatabase;
	//
	public static String redisTimeOut;
	//<!-- 最大空闲连接数 -->
	public static String redisPoolMaxIdle;
	//
	public static String redisPoolMinIdle;
	//<!-- 最大连接数 -->
	public static String redisPoolMaxTotal;
	//
	public static String redisPoolMaxMaxActive;
	//
	public static String redisClusterNodes;


	//<!-- 每次释放连接的最大数目 -->
	public static String numTestsPerEvictionRun="1024";
	//<!-- 释放连接的扫描间隔（毫秒） -->
	public static String timeBetweenEvictionRunsMillis="30000";
    //<!-- 连接最小空闲时间 -->
	public static String minEvictableIdleTimeMillis="1800000";
	//<!-- 连接空闲多久后释放, 当空闲时间>该值 且 空闲连接>最大空闲连接数 时直接释放 -->
	private static String softMinEvictableIdleTimeMillis="10000";
	//<!-- 获取连接时的最大等待毫秒数,小于零:阻塞不确定的时间,默认-1 -->
	public static String maxWaitMillis="1500";
	//<!-- 在获取连接的时候检查有效性, 默认false -->
	public static String testOnBorrow="true";
    //<!-- 在空闲时检查有效性, 默认false -->
	public static String testWhileIdle="true";
	//<!-- 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时,默认true -->
	public static String blockWhenExhausted="false";






	/**
	 * 判断obj是否为空
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
