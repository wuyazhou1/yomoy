package com.nsc.Amoski.uti;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.dto.AccessToken;
import com.nsc.Amoski.dto.JsapiTicket;
import com.nsc.Amoski.service.MemberService;
import com.nsc.Amoski.util.HttpUtil;
import com.nsc.Amoski.util.RegUtil;
import com.nsc.Amoski.util.UniqId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信相关
 */
public class WeChatUtil {// implements Constant

	//private static final //logWriteUtil //logWriteUtil = //logWriteUtil.getSingleton();

	private static final String ACCESS_TOKEN = "accessToken";

	private static final String PAGE_ACCESS_TOKEN = "pageAccessToken";
	
	private static final String THIRDLOGIN_ACCESS_TOKEN = "thirdAccessToken";
	
	private static final String JSAPI_TICKET = "jsapiTicket";

	public static final String USER_INFO_TYPE = "1";


	private static Logger log= LoggerFactory.getLogger(WeChatUtil.class);

	/**
	 * 获取JSAPI_TICKET URL
	 */
	private static final String API_JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	
	/**
	 * 第三方登录授权（获取code）pc
	 */
	private static final String THIRD_PART_LOGIN = "https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";
	

	/**
	 * 5400秒，90分钟
	 */
	private static final int SECONDS_5400 = 5400;


	/**
	 * 获取openId 小程序
	 */
	private static final String SMALL_API_OPENID = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=AppSecret&js_code=CODE&grant_type=authorization_code";

	/**
	 * 小程序appId
	 */
	private static final String SMALLAPPID = "wxff7ffce4f3cb556a";


	/**
	 * 第三方登录使用
	 * loginAppId
	 */
	private static final String LOGINAPPID = "wxcfca560da6abf615";

	/**
	 * 第三方登录使用
	 * LOGINAPPSECRET
	 */
	private static final String LOGINAPPSECRET = "8f4fcbce7355c31999162a08e13b3424F";

	/**
	 * 小程序秘钥
	 * SMALLAPPSECRET
	 */
	private static final String SMALLAPPSECRET = "b02e00315b2b690ccd4969a190ebd414";



	/**
	 * 微信公众号appid
	 */
	public static final String APPID="wxb32615d0ad90ffa8";
	/**
	 * 微信公众号秘钥
	 */
	public static final String APPSECRET="bdcd3b303db0ebaa870fca3fdd00ce40";

	/**
	 * app appid
	 */
	public static final String APP_APPID="wx941dc5dccc68dee3";
	/**
	 * app APPSECRET
	 */
	public static final String APP_APPSECRET="cecfaae035206e45a332f271a788650e";

	/**
	 * 请求access_token url
	 */
	public static final String ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	/**
	 * 获取code 用来获取用户access_token
	 * scope:snsapi_base()、snsapi_userinfo(直接)
	 *
	 */
	static final  String USER_CODE_URL="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=778899#wechat_redirect";//&connect_redirect=1


	/**
	 *根据code获取用户access_token（网页授权） 用来获取用户信息 url
	 */
	static final String USER_ACCESS_TOKEN_URL="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	/**
	 *根据code获取用户access_token（网页授权） 用来获取用户信息 url
	 */
	static final String REFRESH_USER_ACCESS_TOKEN_URL="https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";

	/**
	 * 请求用户信息 url
	 */
	static final String USER_INFO_URL="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	/**
	 * 请求用户信息 url
	 */
	static final String USER_INFO_REAL_URL="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";





	/**
     * 用户网页授权
	 * @param responseUrl
     * @return
     * @throws Exception
	 */
	public static String authorWebPage(String responseUrl) throws Exception {
		String requestUrl= USER_CODE_URL.replace("APPID",APPID).replace("REDIRECT_URI",URLEncoder.encode(responseUrl,"UTF-8")).replace("SCOPE","snsapi_base");//snsapi_userinfo
		//String s = HttpUtil.sendGet(requestUrl);
		log.info(">>>>>>>>>>requsetUrl:"+requestUrl+"\n==============responseData:");
		return requestUrl;
	}
	/**
	 * 用户获取网页授权ACCESS_TOKEN
	 * @param code
	 * @param type web:网页  app:app
	 * @return
	 * @throws Exception
	 */
	public static AccessToken getAuthorCode(String code,MemberService memberService,String type) throws Exception {
		AccessToken tokenObj=null;
		RegUtil regUtil=RegUtil.getSingleton();
        String requestUrl ="";
        String appid=APPID,appsecret=APPSECRET;
        if("app".equals(type)){
            appid=APP_APPID;appsecret=APP_APPSECRET;
        }
		log.info(">>>>>>>appid:"+appid+"=====appsecret:"+appsecret);
        if(tokenObj!=null){//刷新token
            requestUrl= REFRESH_USER_ACCESS_TOKEN_URL.replace("APPID",appid).replace("REFRESH_TOKEN",tokenObj.getRefresh_token());
        }else{
            requestUrl= USER_ACCESS_TOKEN_URL.replace("APPID",appid).replace("SECRET",appsecret).replace("CODE",code);
        }
        String resText=HttpUtil.sendGet(requestUrl);
        log.info(">>>>>>>>>>requsetUrl:"+requestUrl+"\n==============responseData:"+resText);
        JSONObject jsonTokenObj = JSONObject.parseObject(resText);
        String accToken=jsonTokenObj.getString("access_token");
        if(!regUtil.isNull(accToken)){
            tokenObj=new AccessToken();
            tokenObj.setAccess_token(accToken);
            tokenObj.setExpires_in(System.currentTimeMillis());
            tokenObj.setOpenid(jsonTokenObj.getString("openid"));
            tokenObj.setRefresh_token(jsonTokenObj.getString("refresh_token"));
            tokenObj.setScope(jsonTokenObj.getString("scope"));
            log.info(">>>>>>>>>>>>>>>>>>set redis token!!!!!  tokenObj:"+tokenObj);
            //memberService.setSingValue(keyType,tokenObj);//重新获取的放入redis
        }
		/*String keyType=PAGE_ACCESS_TOKEN+type;
        Map<String, Object> map = isRefrushToken(memberService, tokenObj, keyType);//判断是否需要重新获取
        boolean bl=(Boolean) map.get("bl");
        tokenObj=(AccessToken) map.get("token");
		// 如果不存在则调用API获取
		if(bl){

		}*/
		return tokenObj;
	}

	/**
	 * 获取用户信息
	 * @param oppendId 用户oppenid
	 * @param accessToken  用户access_token
	 * @return
	 * @throws Exception
	 */
	public static String getUserInfo(String oppendId,String accessToken,String type) throws Exception {
		String baseUrl=USER_INFO_TYPE.equals(type)?USER_INFO_REAL_URL:USER_INFO_URL;
		String requestUrl= baseUrl.replace("ACCESS_TOKEN",accessToken).replace("OPENID",oppendId);
		String s = HttpUtil.sendGet(requestUrl);
		log.info(">>>>>>>>>>requsetUrl:"+requestUrl+"\n==============responseData:"+s);
		return s;
	}



	/**
	 * 获取openId的code的URL
	 * @param req
	 * @throws Exception
	 */
	public static String getOpenIdCodeUrl(HttpServletRequest req) throws Exception{
		
		////logWriteUtil.writeLog(LOGTYPE_UTIL, "Getting openId code url...", LOGLEVEL_INFO, WeChatUtil.class);
		
		// 接收用于获取openId的code的URL
		String receiveCodeUrl = req.getScheme()+"://"+req.getServerName()+"/chinese/exclude/weChat/openIdCallBack.do";
		
		// 获取openId的code的URL
		String getOpenIdCodeUrl = USER_CODE_URL.replace("APPID", APPID).replace("REDIRECT_URI", URLEncoder.encode(receiveCodeUrl, "UTF-8"));

		////logWriteUtil.writeLog(LOGTYPE_UTIL, "openId code url is"+getOpenIdCodeUrl, LOGLEVEL_INFO, WeChatUtil.class);
		
		return getOpenIdCodeUrl;
	}
	
	/**
	 * 获取openId的code的URL
	 * @param req
	 * @throws Exception
	 */
	public static String getAutoLoginOpenIdCodeUrl(HttpServletRequest req) throws Exception{
		
		////logWriteUtil.writeLog(LOGTYPE_UTIL, "Getting openId code url...", LOGLEVEL_INFO, WeChatUtil.class);
		
		// 接收用于获取openId的code的URL
		String receiveCodeUrl = req.getScheme()+"://"+req.getServerName()+"/chinese/exclude/weChat/autoLoginOpenIdCallBack.do";
		
		// 获取openId的code的URL
		String getOpenIdCodeUrl = USER_CODE_URL.replace("APPID", APPID).replace("REDIRECT_URI", URLEncoder.encode(receiveCodeUrl, "UTF-8"));

		//logWriteUtil.writeLog(LOGTYPE_UTIL, "openId code url is"+getOpenIdCodeUrl, LOGLEVEL_INFO, WeChatUtil.class);
		
		return getOpenIdCodeUrl;
	}
	
	/**
	 * 获取jsapi_ticket
	 * jsapi_ticket是公众号用于调用微信JS接口的临时票据
	 * @return
	 */
	public static synchronized String getJsapiTicket(MemberService memberService){
		Long currTime=System.currentTimeMillis();
		boolean bl=false;
		JsapiTicket jsapiTicketdto=null;
		// 判断REDIS是否存在
		Object  jsapiTicketObj = memberService.getSingValue(JSAPI_TICKET,null);
		log.info("redis JsapiTicket is "+jsapiTicketObj);
		String jsapiTicket="";
		if(jsapiTicketObj!=null){
			jsapiTicketdto=(JsapiTicket)jsapiTicketObj;
			Long startTime=jsapiTicketdto.getExpires_in();
			if((currTime-startTime)>6000*1000){//6000秒  需要重新刷新
				bl=true;
			}else{
				jsapiTicket=jsapiTicketdto.getTicket();
			}
		}else{
			bl=true;
		}
		// 如果不存在则调用API获取
		if(bl){
			String resText = HttpUtil.sendGet(API_JSAPI_TICKET.replace("ACCESS_TOKEN", WeChatUtil.getAccessToken(memberService)));
			log.info(">>>>>>>>>>>>>>>request resText:"+resText);
			jsapiTicketdto = JSON.parseObject(resText,
					JsapiTicket.class);
			log.info(">>>>>>>>>>>>>>>>>>set redis jsapiTicketdto!!!!!  jsapiTicketdto:"+jsapiTicketdto);
			if("0".equals(jsapiTicketdto.getErrcode())){
				jsapiTicketdto.setExpires_in(System.currentTimeMillis());
				memberService.setSingValue(JSAPI_TICKET,jsapiTicketdto);//重新获取的放入redis
				jsapiTicket=jsapiTicketdto.getTicket();
			}
		}
		log.info(">>>>>>>>>>>>>>>>>>return jsapiTicket:"+jsapiTicket);
		return jsapiTicket;
	}
	
	/**
	 * 获取access_token 
	 * access_token是公众号的全局唯一接口调用凭据，公众号调用各接口时都需使用access_token
	 * @return
	 */
	public static synchronized String getAccessToken(MemberService memberService){
		AccessToken tokenObj=null;
		String accToken="";
		String keyType=PAGE_ACCESS_TOKEN;
        Map<String, Object> map = isRefrushToken(memberService,tokenObj,keyType);//判断是否需要重新获取
        boolean bl=(Boolean) map.get("bl");
        tokenObj=(AccessToken) map.get("token");
		if(tokenObj!=null&&!bl){
			accToken=tokenObj.getAccess_token();
		}
		// 如果不存在则调用API获取
		if(bl){
			RegUtil regUtil=RegUtil.getSingleton();
			String resText = HttpUtil.sendGet(ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET));
			log.info("Response text is "+resText);
			JSONObject jsonTokenObj = JSONObject.parseObject(resText);
			//
			//String shareAccessToken = RedisUtils.get("shareAccessToken");// 获取分享使用的token
			accToken=jsonTokenObj.getString("access_token");
			if(!regUtil.isNull(accToken)){
				tokenObj=new AccessToken();
				tokenObj.setAccess_token(accToken);
				tokenObj.setExpires_in(System.currentTimeMillis());
				log.info(">>>>>>>>>>>>>>>>>>set redis token!!!!!  tokenObj:"+tokenObj);
				memberService.setSingValue(keyType,tokenObj);//重新获取的放入redis
			}
		}
		log.info("return token is "+accToken);
		return accToken;
	}

	/**
	 * 判断是否重新请求token
	 */
	public static Map<String,Object> isRefrushToken(MemberService memberService, AccessToken tokenObj, String keyType){
	    Map<String,Object> map=new HashMap<>();
		Long currTime=System.currentTimeMillis();
		boolean bl=false;
		RegUtil regUtil=RegUtil.getSingleton();
		// 判断REDIS是否存在
		Object  accessToken = memberService.getSingValue(keyType,null);
		log.info("redis token is "+accessToken);
		if(accessToken!=null){
			tokenObj=(AccessToken)accessToken;
			if(!regUtil.isNull(tokenObj.getAccess_token())){
				Long startTime=tokenObj.getExpires_in();
				if((currTime-startTime)>6000*1000){//6000秒  需要重新刷新
					bl=true;
				}
			}else{
				bl=true;
			}
		}else{
			bl=true;
		}
        map.put("bl",bl);
		map.put("token",tokenObj);
		return map;
	}
	
	/**
	 * 获取access_token 公共的redis
	 * access_token是公众号的全局唯一接口调用凭据，公众号调用各接口时都需使用access_token
	 * @return
	 */
	public static void setRedisToken(Object accessToken){
		
		//logWriteUtil.writeLog(LOGTYPE_UTIL, "set token to other redis...", LOGLEVEL_INFO, WeChatUtil.class);
		
		/*JedisOptionUtil jedisUtil=new JedisOptionUtil(DEVICE_ADVERISTER_TYPE);
		try {
			// 刷新REDIS的Access token
			jedisUtil.setKeyObj(ACCESS_TOKEN, accessToken,SECONDS_5400);
		} finally {
			jedisUtil.returnResource();
		}*/
		//logWriteUtil.writeLog(LOGTYPE_UTIL, "Access token is "+accessToken.toString(), LOGLEVEL_INFO, WeChatUtil.class);
	}
	
	public static String getThirdLoginUrl(String redirectUrl) throws Exception{
		redirectUrl=URLEncoder.encode("https://www.luckicloud.com/chinese/exclude/payMobile/alipayReturnUrl.do","utf-8");
		//String redirectUrl="/chinese/mobile/html/index.html";
		//获取登录授权url
		String url="weixin://"+USER_CODE_URL.replace("APPID", LOGINAPPID).replace("REDIRECT_URI", redirectUrl).replace("STATE", UniqId.getallSymbolArrayStr(16));
		//logWriteUtil.writeLog(LOGTYPE_UTIL, "getThirdLoginUrl url: "+url, LOGLEVEL_INFO, WeChatUtil.class);
		return url;
	}
	
	public static String getThirdLoginAccessToken(/*UserRedis userRedis,*/ String code) throws Exception{
		//logWriteUtil.writeLog(LOGTYPE_UTIL, "Getting third login access token...", LOGLEVEL_INFO, WeChatUtil.class);
		
		// 判断REDIS是否存在   //THIRDLOGIN_ACCESS_TOKEN redis key
		//Object  accessToken = userRedis.getObj(THIRDLOGIN_ACCESS_TOKEN);
		
		// 如果不存在则调用API获取

		String url=USER_ACCESS_TOKEN_URL.replace("APPID", LOGINAPPID).replace("SECRET", LOGINAPPSECRET).replace("CODE",code);
		
		//logWriteUtil.writeLog(LOGTYPE_UTIL, "getThirdLoginUrl url: "+url, LOGLEVEL_INFO, WeChatUtil.class);
		
		String resText = HttpUtil.sendGet(url);
		
		//logWriteUtil.writeLog(LOGTYPE_UTIL, "Response text is "+resText, LOGLEVEL_INFO, WeChatUtil.class);
		
		return resText;
		
	}
	
	/**
	 * 根据code获取openId
	 * @param code
	 * @return
	 */
	public static Map getUserOpenidByCode(String code){
		String openIdUrl = SMALL_API_OPENID.replace("APPID", SMALLAPPID).replace("AppSecret", SMALLAPPSECRET).replace("CODE", code);
		String resText = HttpUtil.sendGet(openIdUrl);
		Map map = JSONObject.parseObject(resText, Map.class);
		return map;
	}
	// 与接口配置信息中的Token要一致
	private static String token = "W7em7Cp8KUsbvndih5hA";

	/**
	 * 验证签名
	 *
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp,
										 String nonce) {
		String[] arr = new String[] { token, timestamp, nonce };
		// 将token、timestamp、nonce三个参数进行字典序排序
		//Arrays.sort(arr);
		sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;

		try {
			md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		content = null;
		// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 *
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	/**
	 * 将字节转换为十六进制字符串
	 *
	 * @param mByte
	 * @return
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];

		String s = new String(tempArr);
		return s;
	}

	public static void sort(String a[]) {
		for (int i = 0; i < a.length - 1; i++) {
			for (int j = i + 1; j < a.length; j++) {
				if (a[j].compareTo(a[i]) < 0) {
					String temp = a[i];
					a[i] = a[j];
					a[j] = temp;
				}
			}
		}
	}
}
