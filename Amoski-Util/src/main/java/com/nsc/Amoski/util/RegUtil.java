package com.nsc.Amoski.util;

import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.entity.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

    //查询map大写key转驼峰
	public Map<String,Object> mapKeyToLowerCase(Map<String,Object> map){
		Map<String,Object> resultMap=new HashMap<>();
		Set<String> strs = map.keySet();
		for(String str:strs){
			Object obj=map.get(str);
			String newKey=str.toLowerCase();
			if(newKey.indexOf("_")>0){
				String[] arr = newKey.split("_");
				newKey=arr[0]+arr[1].substring(0,1).toUpperCase()+arr[1].substring(1);
			}
			resultMap.put(newKey,obj);
		}
		return resultMap;
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
  	//json对象转dto
	public static <T> T JSONObjToDto(JSONObject jsonObj,T entity){
		for (Field field : entity.getClass().getDeclaredFields()){
			try {
				boolean flag = field.isAccessible();
				field.setAccessible(true);
				Object objVal = jsonObj.get(field.getName());
				if(objVal==null){
					continue;
				}
				if("java.lang.String".equals(field.getType().getTypeName())){
					field.set(entity, jsonObj.getString(field.getName()));
				}else if("java.lang.Integer".equals(field.getType().getTypeName())){
					field.set(entity, jsonObj.getIntValue(field.getName()));
				}else if("java.lang.Double".equals(field.getType().getTypeName())){
					field.set(entity, jsonObj.getDoubleValue(field.getName()));
				}else if("java.math.BigDecimal".equals(field.getType().getTypeName())){
					field.set(entity, jsonObj.getBigDecimal(field.getName()));
				}else if("java.sql.Timestamp".equals(field.getType().getTypeName())){
					field.set(entity, jsonObj.getTimestamp(field.getName()));
				}
				field.setAccessible(flag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	return entity;
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
	 * 实体类转Map
	 * @param object
	 * @return
	 */
	public static Map<String, Object> entityToMap(Object object) {
		Map<String, Object> map = new HashMap();
		for (Field field : object.getClass().getDeclaredFields()){
			try {
				boolean flag = field.isAccessible();
				field.setAccessible(true);
				Object o = field.get(object);
				map.put(field.getName(), o);
				field.setAccessible(flag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * Map转实体类
	 * @param map 需要初始化的数据，key字段必须与实体类的成员名字一样，否则赋值为空
	 * @param entity  需要转化成的实体类
	 * @return
	 */
	public static <T> T mapToEntity(Map<String, Object> map, Class<T> entity) {
		T t = null;
		try {
			t = entity.newInstance();
			for(Field field : entity.getDeclaredFields()) {
				if (map.containsKey(field.getName())) {
					boolean flag = field.isAccessible();
					field.setAccessible(true);
					Object object = map.get(field.getName());
					if (object!= null /*&& field.getType().isAssignableFrom(object.getClass())*/) {
						if("java.lang.String".equals(field.getType().getName())){
							field.set(t, object.toString());
						}else if("java.sql.Timestamp".equals(field.getType().getName())){
							field.set(t, new Timestamp((Long)object));
						}else{
							field.set(t, object);
						}
					}
					field.setAccessible(flag);
				}
			}
			LoggerFactory.getLogger(RegUtil.class).info(">>>>>>>>>>>>>>>>>map:"+map+"==entity:"+entity);
			return t;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
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
	public static String appcode = "7d69b9f54f644fdc8d9dc3852b7e092d";
	//实名认证
	public static String validRealName(String identityNumber,String realName){
		//请正确填写appcode,如果填写错误阿里云会返回 400错误或403错误
		//appcode查看地址 https://market.console.aliyun.com/imageconsole/
		//请求地址
		String url="http://1.api.apistore.cn/idcard3";
		//请求参数//430105198708290028
		String params = "realName="+realName+"&cardNo="+identityNumber;
		//发送请求
		String result = HttpUtils.APISTORE(url, params,appcode,"POST");
		return result;
	}

	/**
	 * 拼接日期
	 * @param dir
	 * @return
	 */
	public String appendDtDir(String dir){
		String dt=DateUtils.getNowDay(DateUtils.DATE_FORMAT);//当前时间
		String[] dtSplit = dt.split("-");
		return dir+File.separator+dtSplit[0]+"Y"+File.separator+dtSplit[1]+"M"+File.separator+dtSplit[2]+"D"+File.separator;
	}

	/**
	 * 得到文件流
	 * @param fileUrl
	 * @param baseFileUrl
	 * @return
	 */
	public FileInputStream getFileStream(String fileUrl,String baseFileUrl) throws Exception{
		Logger log= LoggerFactory.getLogger(RegUtil.class);
		if(isNull(baseFileUrl)){
			baseFileUrl= StringUtils.getFilePath();
		}
		FileInputStream in =null;
		log.info("getFileStream>>>>>>>>>>>>>>>>>>>>>>>url:"+baseFileUrl+fileUrl);
		File result = new File(baseFileUrl+fileUrl);//要读写的图片
		if (!result.exists()) {//校验该文件是否已存在
			log.info("file url >>>>>>>>>>>>>>>>>>>>>>>file not exists");
			return in;
		}
		in= new FileInputStream(result);
		return in;
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

	/**
	 * 得到文件流
	 * @param fileUrl
	 * @return
	 */
	public FileInputStream getFileStream(String fileUrl) throws Exception{
		Logger log= LoggerFactory.getLogger(RegUtil.class);
		FileInputStream in =null;
		log.info("getFileStream>>>>>>>>>>>>>>>>>>>>>>>url:"+fileUrl);
		File result = new File(fileUrl);//要读写的图片
		if (!result.exists()) {//校验该文件是否已存在
			log.info("file url >>>>>>>>>>>>>>>>>>>>>>>file not exists");
			return in;
		}
		in= new FileInputStream(result);
		return in;
	}


	public static void main(String arg []){
		RegUtil util = RegUtil.getSingleton();
		String str="TEAM_NAME";
		String newKey=str.toLowerCase();
		if(newKey.indexOf("_")>0){
			String[] s = newKey.split("_");
			newKey=s[0]+s[1].substring(0,1).toUpperCase()+s[1].substring(1);
			//newKey=newKey.replace("_",str.substring(idx+1,idx+2));
		}
		System.out.println(newKey);
	}
}
