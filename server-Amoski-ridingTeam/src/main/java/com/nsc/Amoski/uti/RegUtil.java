package com.nsc.Amoski.uti;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;


public class RegUtil {
	private RegUtil(){};
	private  final static RegUtil reg=new RegUtil();
	public static RegUtil getUtil(){
		return reg;
	}
	
	

	
	public  boolean isNull(String objs){
		if(objs==null||"".equals(objs)||"null".equals(objs)||" ".equals(objs)) return true;
		return false;
	}
	public boolean isNull(String ...obj){
		if(obj==null){
			  return true;
		}
		for (int i = 0; i < obj.length; i++) {
			 if(isNull(obj[i])){
				  return true;
			 }
		}
		return false; 
		
	}
	 /**
	  * 根据身份证号码计算年龄
	  * @param idCrad 合法身份证号码
	  * @return 返回周岁
	 *  @throws ParseException 
	  */
	 public  int ageIdCrad(String  idCrad) throws ParseException{
		 if(isIDCrad(idCrad)){
			 String ageStr=idCrad.substring(6,13);
			 SimpleDateFormat simp=new SimpleDateFormat("yyyyMMdd");
			 Date oldDate=simp.parse(ageStr);
			 Date date=new Date();
			 long time=(date.getTime()-oldDate.getTime())/(3600*24*1000);
			 return (int) (time/365);
		 }
		 return 0;
	 }
	
	public boolean isNumber(String str){
		
		Pattern p = null;  
        Matcher m = null;  
        boolean ismobile = false;   
        try {
        	 p = Pattern.compile("^\\d{1,15}||-{1}\\d{1,15}$"); // 验证是否是数字
 	        m = p.matcher(str);  
 	       ismobile = m.matches(); 
		} catch (Exception e) {
			ismobile = false;   
			e.printStackTrace();
		}  
        return ismobile;  
	}
	
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
	 * 
	 * @param idCrad 校验身份证号码
	 * @return 
	 */
    public boolean isIDCrad(String idCrad){
    	return match(idCrad,"^(\\d{6})()?(\\d{4})(\\d{2})(\\d{2})(\\d{3})(\\w)$");
    }
    
    
    /** 
     * 校验银行卡卡号 
     * 校验过程： 
	    1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。 
	    2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。 
	    3、将奇数位总和加上偶数位总和，结果应该可以被10整除。       
     */  
    public boolean isBankCard(String bankCard) {  
         if(bankCard.length() < 15 || bankCard.length() > 19) {
             return false;
         }
         char bit = this.getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));  
         if(bit == 'N'){  
             return false;  
         }  
         return bankCard.charAt(bankCard.length() - 1) == bit;  
    }
	
	
	    /** 
	  * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位 
	  * @param nonCheckCodeBankCard 
	  * @return 
	  */  
	 public  char getBankCardCheckCode(String nonCheckCodeBankCard){  
	     if(nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0  
	             || !nonCheckCodeBankCard.matches("\\d+")) {  
	         //如果传的不是数据返回N  
	         return 'N';  
	     }  
	     char[] chs = nonCheckCodeBankCard.trim().toCharArray();  
	     int luhmSum = 0;  
	     for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {  
	         int k = chs[i] - '0';  
	         if(j % 2 == 0) {  
	             k *= 2;  
	             k = k / 10 + k % 10;  
	         }  
	         luhmSum += k;             
	     }  
	     return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');  
	 }  

    
    /**
	 * 
	 * @param idCrad 校验手机号码
	 * @return 
	 */
    public boolean isMobileNum(String mobile){
    	return match(mobile,"^1(3|4|5|7|8)\\d{9}$");
    }
    
    /**
	 * 
	 * @param idCrad 校验电子邮箱
	 * @return 
	 */
    public boolean isEmail(String email){
    	return match(email,"^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    }
	
    
    public static void main(String[] args) {
		///李春
    	boolean fla=RegUtil.getUtil().isIDCrad("320223197901133000");
    	
    	
    	
    	System.out.println(fla+"==="+RegUtil.getUtil().isImage("image/jpeg"));
    	System.out.println(fla+"==="+RegUtil.getUtil().isMobileNum("17863654513"));
	}
    
	public  String replaceNumToX(String srcStr,int type){
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
	 * 功能：前台交易构造HTTP POST自动提交表单<br>
	 * @param action 表单提交地址<br>
	 * @param hiddens 以MAP形式存储的表单键值<br>
	 * @param encoding 上送请求报文域encoding字段的值<br>
	 * @return 构造好的HTTP POST交易表单<br>
	 */
	public  String createAutoFormHtml(String reqUrl, Map<String, String> hiddens,String encoding) {
		String html=null;
		try {
			StringBuffer sf = new StringBuffer();
			sf.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset="+encoding+"\"/></head><body>");
			sf.append("<form id = \"pay_form\" action=\"" + reqUrl
					+ "\" method=\"post\">");
			if (null != hiddens && 0 != hiddens.size()) {
				Set<Entry<String, String>> set = hiddens.entrySet();
				Iterator<Entry<String, String>> it = set.iterator();
				while (it.hasNext()) {
					Entry<String, String> ey = it.next();
					String key = ey.getKey();
					String value = ey.getValue();
					sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\""
							+ key + "\" value=\"" + value + "\"/>");
				}
			}
			sf.append("</form>");
			sf.append("</body>");
			sf.append("<script type=\"text/javascript\">");
			sf.append("document.all.pay_form.submit();");
			sf.append("</script>");
			sf.append("</html>");
			html= sf.toString();
			sf=null;
		} catch (Exception e) {
			e.printStackTrace();
			 html=null;
		}
		return html;
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
}
