package com.nsc.Amoski.uti;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegUtil {
    private RegUtil(){}

	private static RegUtil singleton;
    public static RegUtil getSingleton(){
    	if(singleton==null){
    		singleton=new RegUtil();
    	}
        return singleton;
    }
    //手机号校验表达式
    public static final String REG_MOBILE="^[1][3,4,5,7,8,9][0-9]{9}$";

    //密码校验表达式
	public static final  String REG_PWD="^[A-Za-z0-9_-]{6,20}$";

	enum Type {
        INT,
        INTEGER,
        STRING,
        DOUBLE,
        DATE,
        BIGDECIMAL,
        TIMESTAMP,
        FLOAT,
        BOOLEAN,
        NULL
    }

    /**
     *@Author: yw
     *@Desciption:校验参数是否为空
     *@Date:11:20 2018/4/27
     *@param:No such property: code for class: Script1
     */
    public boolean isNull(Object... object) {
        if (object == null || object.equals("")) {
            return true;
        }
        String type=null;
        for (Object value : object) {
            if(value==null){
                return true;
            }
            type=value.getClass().getSimpleName().toUpperCase();
            System.out.println(type.equals(Type.INT.name()));
            if((type.equals(Type.INTEGER.name())||type.equals(Type.INT.name()))&&  value.equals(Integer.valueOf("0"))){
                return true;
            }
            if(type.equals(Type.STRING.name())&& (value.equals("")||value==null)){
                return true;
            }
            if(type.equals(Type.DOUBLE.name())&& (!Double.valueOf(value.toString()).isNaN() ||Double.valueOf(value.toString())==0D )){
                return true;
            }
            if(type.equals(Type.FLOAT.name())&& (!Float.valueOf(value.toString()).isNaN() ||Float.valueOf(value.toString())==0F )){
                return true;
            }
        }
        return false;
    }

  //object转字节
  	public byte[] objToByte(Object obj){
  		if(obj==null) return null;
  		byte [] bt=null;
  		try {
  			ByteArrayOutputStream bos=new ByteArrayOutputStream();
  			ObjectOutputStream oos=new ObjectOutputStream(bos);
  			oos.writeObject(obj);
  			oos.flush();
  			bt=bos.toByteArray();
  			oos.close();
  			bos.close();
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  		return bt;
  	}
  	//字节转object
  	public Object byteToObj(byte[] bt){
  		Object obj=null;
  		if(bt==null) return null;
  		try {
  			ByteArrayInputStream bis=new ByteArrayInputStream(bt);
  			ObjectInputStream ois=new ObjectInputStream(bis);
  			obj=ois.readObject();
  			ois.close();
  			bis.close();
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  		return obj;
  	}
  	
  	//为插入不为null字段设置默认值
  	public void setDefaultValueForObj(Object obj) throws Exception{
  		Class<? extends Object> cl = obj.getClass();
  		Field[] fields = cl.getDeclaredFields();
  		for (Field field : fields) {
  			field.setAccessible(true);
  			if(field.get(obj)==null){
  				setFieldValue(field,obj);
  			}
		}
  	}
  	//设置单个字段默认值
  	public void setFieldValue(Field field,Object obj) throws Exception{
  		System.out.println("+++++++++fieldTypeName:"+field.getType().getTypeName());
  		if("java.lang.String".equals(field.getType().getTypeName())){
  			field.set(obj, "");
  		}else if("java.lang.Integer".equals(field.getType().getTypeName())){
  			field.set(obj, 0);
  		}else if("java.lang.Double".equals(field.getType().getTypeName())){
  			field.set(obj, 0.0);
  		}else if("java.math.BigDecimal".equals(field.getType().getTypeName())){
  			field.set(obj, new BigDecimal(0));
  		}else if("java.sql.Timestamp".equals(field.getType().getTypeName())){
  			field.set(obj, Timestamp.valueOf("1990-01-01 00:00:00"));
  		}
  	}

  	/**
	 * 
	 * @param str 待匹配的文本
	 * @param reg 正则的表达式
	 * @return
	 */
    private   boolean match(String str, String reg) {
		return Pattern.matches(reg, str);
	}
  	/**
	 * 
	 * @param email 校验电子邮箱
	 * @return 
	 */
    public boolean isEmail(String email){
    	return match(email,"^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    }

	/**
	 *
	 * @param mobile 校验电子邮箱
	 * @return
	 */
	public boolean isMobile(String mobile){
		return match(mobile,REG_MOBILE);
	}

	/**
	 *
	 * @param pwd 校验密码是否合法
	 * @return
	 */
	public boolean isPwdLegal(String pwd){
		return match(pwd,REG_PWD);
	}
	/**
	 *
	 * @param idCrad 校验身份证号码
	 * @return
	 */
	public boolean isIDCrad(String idCrad){
		return match(idCrad,"^(\\d{6})()?(\\d{4})(\\d{2})(\\d{2})(\\d{3})(\\w)$");
	}


	/**
     * 判断是否是图片
     * @param type
     * @return
     */
    public boolean isImage(String type){
		
		Pattern p = null;  
        Matcher m = null;  
        boolean ismobile = false;   
        try {
        	 p = Pattern.compile("^image/\\w+$"); // 验证是否是数字
 	        m = p.matcher(type);  
 	       ismobile = m.matches(); 
		} catch (Exception e) {
			ismobile = false;   
			e.printStackTrace();
		}  
        return ismobile;  
	}

	/**
	 * 是否微信浏览器
	 * @Title: isWechat
	 * @author: pk
	 * @Description: TODO
	 * @return
	 * @return: boolean
	 */
	public static boolean isWechat(String ua) {
		if (ua.indexOf("micromessenger") > -1) {
			return true;//微信
		}
		return false;//非微信手机浏览器
	}

	/**
	   * 获取访问者IP地址
	   */
	public String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}



	/*public static void main(String arg []){
		RegUtil util = RegUtil.getSingleton();
		System.out.println("test  value:"+util.isPwdLegal("sdsdf1A_-45+"));
	}*/
}
