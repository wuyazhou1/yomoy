package com.nsc.Amoski.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

	/**
	 * jsonstr 2 dto
	 * @param jsonstr
	 * @return
	 */
	 public static Object jsonToDto(String jsonstr,Class<?> entityClass) { 
		 Object respt=null;
		 try {
			 Gson gson = new GsonBuilder().create();
			   respt=gson.fromJson(jsonstr, entityClass);
			   gson=null;
		} catch (Exception e) {
			respt=null;
			e.printStackTrace();
			//LogWriteUtil.getSingleton().writeLog("", " jsonToDto Exception: "+e.getMessage(), Constant.LOGLEVEL_ERROR, GsonUtil.class);
		}
		  return respt;
	 }
	 
	 /**
	  * obj to json
	  * @param obj
	  * @return
	  */
	 public  static String dtoToJson(Object obj){
		 String jsonStr=null;
		 try {
			// Gson生成json时候，会忽略掉值为null的key 
			 GsonBuilder gb=new GsonBuilder();
			 gb.disableHtmlEscaping();
			 Gson gson = gb.create();
			 jsonStr=gson.toJson(obj);
			 gson=null;
		} catch (Exception e) {
			e.printStackTrace();
			//LogWriteUtil.getSingleton().writeLog("", " dtoToJson Exception: "+e.getMessage(), Constant.LOGLEVEL_ERROR, GsonUtil.class);
		}
		 return jsonStr;
	 }
	 
	 
	 /*public static void main(String [] arg){
		 try {
			 Map<String, String> shareMap=new HashMap<String, String>();
				shareMap.put("noncestr", UniqId.getRandomStr(30));
				shareMap.put("jsapi_ticket", "fdsafdsaf");
				shareMap.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
				shareMap.put("url","fdasfdasfdsaffdsffds");
				System.out.println(" jsapi_ticket "+shareMap.get("jsapi_ticket")+"  url  "+shareMap.get("url"));
				
				System.out.println(dtoToJson(shareMap));
				
		} catch (Exception e) {
			// TODO: handle exception
		}
	 }*/
}
