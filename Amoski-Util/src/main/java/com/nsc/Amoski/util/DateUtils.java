package com.nsc.Amoski.util;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

public class DateUtils {
	
	public final static String DATE_FORMAT = "yyyy-MM-dd";
	public final static String DATE_FORMAT_FILE = "yyyy_MM_dd";
	public final static String DATE_FORMAT_YYYYMMDD = "yyyyMMdd HH:mm:ss";
	public final static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";
	public final static String DATE_FORMAT_YS_TIME = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_FORMAT_YS = "yyyyMMdd";
	
	public final static  String FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
	
	public final static  String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	
	public final static  String FORMAT_YSREAL = "yyyyMMdd'T'HHmmssSSS";
	
	public final static String DATE_FORMAT_IBS_TIME = "yyyy-MM-dd HH:mm:ss.SSS";
	
	private static final String DATETIME_NULL = "2001-01-01 00:00:00.0";

    /**
     *@Author: yw
     *@Desciption: 获取当前的时间戳
     *@Date:14:09 2018/5/25
     *@param:No such property: code for class: Script1
     */
	public final static Timestamp getCurrentStamp(){
		Timestamp date=new Timestamp(System.currentTimeMillis());
		return date;
	}

	public static void main(String[] args) {
		Timestamp time= getCurrentStamp();
		String strn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
		System.out.println(strn);
	}

	public final static Timestamp string2TimeByIbs(String dateString) 
			  throws ParseException {
			   DateFormat dateFormat; 
			  dateFormat = new SimpleDateFormat(DATE_FORMAT_IBS_TIME);//设定格式 
			  //dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH); 
			  dateFormat.setLenient(false); 
			  Date timeDate = dateFormat.parse(dateString);//util类型
			  Timestamp dateTime = new Timestamp(timeDate.getTime());//Timestamp类型,timeDate.getTime()返回一个long型
			  return dateTime; 
			} 
	
	public final static Timestamp string2TimeByUnionpay(String dateString)
			  throws ParseException {
			   DateFormat dateFormat; 
			  dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);//设定格式 
			  //dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH); 
			  dateFormat.setLenient(false); 
			  Date timeDate = dateFormat.parse(dateString);//util类型
			  Timestamp dateTime = new Timestamp(timeDate.getTime());//Timestamp类型,timeDate.getTime()返回一个long型
			  return dateTime; 
			} 
	
	public final static Timestamp string2Time(String dateString) 
			  throws ParseException {
			   DateFormat dateFormat; 
			  dateFormat = new SimpleDateFormat(DATE_FORMAT);//设定格式 
			  //dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH); 
			  dateFormat.setLenient(false); 
			  Date timeDate = dateFormat.parse(dateString);//util类型
			  Timestamp dateTime = new Timestamp(timeDate.getTime());//Timestamp类型,timeDate.getTime()返回一个long型
			  return dateTime; 
			} 
	public final static Timestamp string2TimeYs(String dateString) 
			  throws ParseException {
			   DateFormat dateFormat; 
			  //dateFormat = new SimpleDateFormat(DATE_FORMAT_YS);
			  dateFormat = new SimpleDateFormat(DATE_FORMAT_YS_TIME);
			  dateFormat.setLenient(false); 
			  Date timeDate = dateFormat.parse(dateString);//util类型
			  Timestamp dateTime = new Timestamp(timeDate.getTime());//Timestamp类型,timeDate.getTime()返回一个long型
			  return dateTime; 
			} 
	
	public final static Timestamp string2TimeWx(String dateString) 
			  throws ParseException {
			   DateFormat dateFormat; 
			  dateFormat = new SimpleDateFormat(DATE_FORMAT_YS_TIME);
			  dateFormat.setLenient(false); 
			  Date timeDate = dateFormat.parse(dateString);//util类型
			  Timestamp dateTime = new Timestamp(timeDate.getTime());//Timestamp类型,timeDate.getTime()返回一个long型
			  return dateTime; 
			} 
	
	public final static Timestamp string2TimeYoYi(String dateString) 
			  throws ParseException {
			   DateFormat dateFormat; 
			  dateFormat = new SimpleDateFormat(FORMAT_YYYYMMDDHHMMSS);
			  dateFormat.setLenient(false); 
			  Date timeDate = dateFormat.parse(dateString);//util类型
			  Timestamp dateTime = new Timestamp(timeDate.getTime());//Timestamp类型,timeDate.getTime()返回一个long型
			  return dateTime; 
			} 
	   public final static String string2TimeYs(Date date) 
			  throws ParseException {
			  return string2TimeYs(date,null); 
			} 
	
	   public final static String string2TimeYs(Date date,String format)
				  throws ParseException {
		       if(date==null){
		    	  date=new Date();
		       }
			   DateFormat dateFormat; 
			   if(!RegUtil.getSingleton().isNull(format)){
				   dateFormat = new SimpleDateFormat(format);
			   } else {
				   dateFormat = new SimpleDateFormat(DATE_FORMAT_YS);
			   }
			  
			 return dateFormat.format(date); 
	   }
	public final static String string2TimeYs(java.sql.Date date, String format)
			throws Exception {
		if(date==null){
			date=new java.sql.Date(System.currentTimeMillis());
		}
		DateFormat dateFormat;
		if(!RegUtil.getSingleton().isNull(format)){
			dateFormat = new SimpleDateFormat(format);
		} else {
			dateFormat = new SimpleDateFormat(DATE_FORMAT_YS);
		}

		return dateFormat.format(date);
	}


	public static String getNowDay() {  
		String returnStr = null;  
		Date dNow = new Date();   //当前时间
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT); //设置时间格式
		returnStr= sdf.format(dNow); //格式化当前时间
        return returnStr;  
    }
	
	public static String getNow() {  
		String returnStr = null;  
		Date dNow = new Date();   //当前时间
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		SimpleDateFormat sdf=new SimpleDateFormat(FORMAT_YYYYMMDDHHMMSS); //设置时间格式
		returnStr= sdf.format(dNow); //格式化当前时间
        return returnStr;  
    }
	
	public static String getNowDay(String dateFormat) { 
		dateFormat=dateFormat==null?DATE_FORMAT:dateFormat;
		String returnStr = null;  
		Date dNow = new Date();   //当前时间
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		SimpleDateFormat sdf=new SimpleDateFormat(dateFormat); //设置时间格式
		returnStr= sdf.format(dNow); //格式化当前时间
        return returnStr;  
    }
	
	public static String getBeforeDay() {  
		String returnStr = null;  
		Date dNow = new Date();   //当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
		dBefore = calendar.getTime();   //得到前一天的时间
		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT); //设置时间格式
		returnStr = sdf.format(dBefore);    //格式化前一天
        return returnStr;  
    }
	
	public static String getNextDay() {  
		String returnStr = null;  
		Date dNow = new Date();   //当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, +1);  //设置为后一天
		dBefore = calendar.getTime();   //得到后一天的时间
		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT); //设置时间格式
		returnStr = sdf.format(dBefore);    //格式化后一天
        return returnStr;  
    }
	
	public static String getNextDayNum(int day,String dataFormat) {
		dataFormat=dataFormat==null?DATE_FORMAT:dataFormat;
		String returnStr = null;  
		Date dNow = new Date();   //当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, day);  //设置为后一天
		dBefore = calendar.getTime();   //得到后一天的时间
		SimpleDateFormat sdf=new SimpleDateFormat(DATE_FORMAT); //设置时间格式
		returnStr = sdf.format(dBefore);    //格式化后一天
        return returnStr;  
    }

	public static long getNextDayNum(int day) {
		String returnStr = null;
		Date dNow = new Date();   //当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, day);  //设置为后一天
		dBefore = calendar.getTime();   //得到后一天的时间
		return dBefore.getTime();
	}
	
	public static String getBeforeDay(String format) {  
		String returnStr = null;  
		Date dNow = new Date();   //当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); //得到日历
		calendar.setTime(dNow);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
		dBefore = calendar.getTime();   //得到前一天的时间
		SimpleDateFormat sdf=new SimpleDateFormat(format); //设置时间格式
		returnStr = sdf.format(dBefore);    //格式化前一天
        return returnStr;  
    }
	
	public static String getMonday() {  
		String returnStr = null;  
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
		returnStr= df.format(cal.getTime()); //格式化当前时间
        return returnStr;  
    }
	
	public static String getOtherDay(int day) {  
		String returnStr = null;  
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.DAY_OF_WEEK, day);  
		returnStr= df.format(cal.getTime()); 
        return returnStr;  
    }
	
	
	public static String getMonthFirstDay() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.DATE, 1);  
        return DateFormatUtils.format(cal, DATE_FORMAT);  
	}
	
	   public static String getMonthLastDay() {  
	        Calendar cal = Calendar.getInstance();  
	        cal.set(Calendar.DATE, 1);// 设为当前月的1号  
	        cal.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号  
	        cal.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天  
	        return DateFormatUtils.format(cal, DATE_FORMAT);  
	    } 
	
	   
	   public static String getPreviousMonthFirst() {  
	        Calendar cal = Calendar.getInstance();  
	        cal.set(Calendar.DATE, 1);// 设为当前月的1号  
	        cal.add(Calendar.MONTH, -1);  
	        return DateFormatUtils.format(cal, DATE_FORMAT);  
	    }  
	   
	   public static String getPreviousMonthEnd() {  
	        Calendar cal = Calendar.getInstance();  
	    
	        cal.set(Calendar.DATE, 1);// 设为当前月的1号  
	        cal.add(Calendar.MONTH, 0);//  
	        cal.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天  
	        return DateFormatUtils.format(cal, DATE_FORMAT);  
	    }  
	   

	   
	   public static int daysBetween(String smdate,String bdate) throws ParseException{  
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
	        Calendar cal = Calendar.getInstance();    
	        cal.setTime(sdf.parse(smdate));    
	        long time1 = cal.getTimeInMillis();                 
	        cal.setTime(sdf.parse(bdate));    
	        long time2 = cal.getTimeInMillis();         
	        long between_days=(time2-time1)/(1000*3600*24);  
	            
	       return Integer.parseInt(String.valueOf(between_days));     
	    } 
	   
	   public static int getWeekDay(){  
		   Date today = new Date();
	        Calendar c=Calendar.getInstance();
	        c.setTime(today);
	        int weekday=c.get(Calendar.DAY_OF_WEEK); 
	       return weekday;     
	    } 
	   
	   public static int getWeekDayByTradeing(){  
		   Date today = new Date();
	        Calendar c=Calendar.getInstance();
	        c.setTime(today);
	        int weekday=c.get(Calendar.DAY_OF_WEEK); 
	        int weekdaytradeing=0;
	        switch(weekday) { 
			   case 1: 
				   weekdaytradeing=7; 
			   break; 
			   case 2: 
				   weekdaytradeing=1; 
			   break;
			   case 3: 
				   weekdaytradeing=2; 
			   break;
			   case 4: 
				   weekdaytradeing=3; 
			   break;
			   case 5: 
				   weekdaytradeing=4; 
			   break;
			   case 6: 
				   weekdaytradeing=5; 
			   break;
			   case 7: 
				   weekdaytradeing=6; 
			   break;
			   } 
	       return weekdaytradeing;     
	    } 
	   
	   
	   public  static Timestamp getNullAsTimestamp() throws Exception{
			Timestamp  timestamp_temp=timestamp(DATETIME_NULL);
			return timestamp_temp;
		}
	   
	   /**
		 * 
		 * timestamp: date string 转换为 timestamp
		 */
		public  static Timestamp timestamp(String date)throws Exception{
				Timestamp  expired_date=new Timestamp(System.currentTimeMillis());
				try {
					if(date!=null){
						expired_date=Timestamp.valueOf(date);
					}
					
				} catch (Exception e) {
					throw e;
				}
				return expired_date;
			}
		
	   /*public static void main(String[] args) {

		   System.out.println(DateUtils.getCurrentStamp());
		   System.out.println(DateUtils.getBeforeDay());
		System.out.println(DateUtils.getMonday());
		System.out.println(DateUtils.getOtherDay(6));
		System.out.println(DateUtils.getMonthFirstDay());
		System.out.println(DateUtils.getMonthLastDay());
	}*/
 
}
