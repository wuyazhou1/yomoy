package com.nsc.Amoski.uti;

import com.nsc.Amoski.util.UniqId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 微信相关
 */
public class WeChatUtil{// implements Constant

	//private static final //logWriteUtil //logWriteUtil = //logWriteUtil.getSingleton();

	private static final String ACCESS_TOKEN = "accessToken";
	
	private static final String THIRDLOGIN_ACCESS_TOKEN = "thirdAccessToken";
	
	private static final String JSAPI_TICKET = "jsapiTicket";

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
	private static final String SMALLAPPID = "wxf0febafb03162be6";


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
	private static final String SMALLAPPSECRET = "5ff74767c0456e3e0ad190ca62e2daae";



	/**
	 * 微信公众号appid
	 */
	static final String APPID="wxb32615d0ad90ffa8";
	/**
	 * 微信公众号秘钥
	 */
	static final String APPSECRET="69fa4ef6d357353ba3b972723f99bc1a";

	/**
	 * 请求access_token url
	 */
	static final String ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	/**
	 * 获取code 用来获取用户access_token
	 * scope:snsapi_base()、snsapi_userinfo(直接)
	 *
	 */
	static final  String USER_CODE_URL="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=778899#wechat_redirect";


	/**
	 *根据code获取用户access_token（网页授权） 用来获取用户信息 url
	 */
	static final String USER_ACCESS_TOKEN_URL="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	/**
	 * 请求用户信息 url
	 */
	static final String USER_INFO_URL="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";


	/**
	 * 用户网页授权
	 * @param responseUrl
	 * @return
	 * @throws Exception
	 */
	public static String authorWebPage(String responseUrl) throws Exception {
		String requestUrl= USER_CODE_URL.replace("APPID",APPID).replace("REDIRECT_URI",URLEncoder.encode(responseUrl,"UTF-8")).replace("SCOPE","snsapi_base");
		String s = HttpUtil.sendGet(requestUrl);
		log.info(">>>>>>>>>>requsetUrl:"+requestUrl+"\n==============responseData:"+s);
		return s;
	}
	/**
	 * 用户授权获取用户信息
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static String getUserInfo(String code) throws Exception {
		String requestUrl= USER_ACCESS_TOKEN_URL.replace("APPID",APPID).replace("SECRET",APPSECRET).replace("CODE",code);
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
	public static synchronized String getJsapiTicket(/*UserRedis userRedis*/){
		
		//logWriteUtil.writeLog(LOGTYPE_UTIL, "Getting jsapi ticket...", LOGLEVEL_INFO, WeChatUtil.class);
		
		// 判断REDIS是否存在
		Object  jsapiTicket = "";//userRedis.getObj(JSAPI_TICKET);
		
		// 如果不存在则调用API获取
		if(jsapiTicket == null || jsapiTicket.toString().equals("")){

			//logWriteUtil.writeLog(LOGTYPE_UTIL, "Jsapi ticket lose efficacy", LOGLEVEL_INFO, WeChatUtil.class);
			
			String resText = HttpUtil.sendGet(API_JSAPI_TICKET.replace("ACCESS_TOKEN", WeChatUtil.getAccessToken()));
			
			//logWriteUtil.writeLog(LOGTYPE_UTIL, "Response text is "+resText, LOGLEVEL_INFO, WeChatUtil.class);
			
			/*JSONObject json = JSONObject.fromObject(resText);

			jsapiTicket = json.getString("ticket");*/
			
			// 刷新REDIS的JSAPI TICKET
			//userRedis.set(JSAPI_TICKET, jsapiTicket, SECONDS_5400);

			//logWriteUtil.writeLog(LOGTYPE_UTIL, "Refresh redis jsapi ticket success", LOGLEVEL_INFO, WeChatUtil.class);
		}
		
		
		//logWriteUtil.writeLog(LOGTYPE_UTIL, "Jsapi ticket is "+jsapiTicket, LOGLEVEL_INFO, WeChatUtil.class);
		
		return jsapiTicket.toString();
	}
	
	/**
	 * 获取access_token 
	 * access_token是公众号的全局唯一接口调用凭据，公众号调用各接口时都需使用access_token
	 * @return
	 */
	public static synchronized String getAccessToken(/*UserRedis userRedis*/){
		
		//logWriteUtil.writeLog(LOGTYPE_UTIL, "Getting access token...", LOGLEVEL_INFO, WeChatUtil.class);
		
		// 判断REDIS是否存在
		Object  accessToken = "";//userRedis.getObj(ACCESS_TOKEN);
		// 如果不存在则调用API获取
		if(accessToken == null || accessToken.toString().equals("")){

			//logWriteUtil.writeLog(LOGTYPE_UTIL, "Access token lose efficacy", LOGLEVEL_INFO, WeChatUtil.class);
			
			String resText = HttpUtil.sendGet(ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET));
			
			//logWriteUtil.writeLog(LOGTYPE_UTIL, "Response text is "+resText, LOGLEVEL_INFO, WeChatUtil.class);

			/*JSONObject json = JSONObject.fromObject(resText);
			
			accessToken = json.getString("access_token");*/
			
			// 刷新REDIS的Access token
			//userRedis.set(ACCESS_TOKEN, accessToken, SECONDS_5400);

			//logWriteUtil.writeLog(LOGTYPE_UTIL, "Refresh redis access token success", LOGLEVEL_INFO, WeChatUtil.class);
		}
		
		//logWriteUtil.writeLog(LOGTYPE_UTIL, "Access token is "+accessToken, LOGLEVEL_INFO, WeChatUtil.class);
		//setRedisToken(accessToken);//放入公共redis 供其他地方使用
		return accessToken.toString();
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
	 *//*
	public static String getUserOpenidByCode(String code){
		String openId="";
		String openIdUrl = API_OPENID.replace("APPID", APPID).replace("AppSecret", APPSECRET).replace("CODE", code);
		String resText = HttpUtil.sendGet(openIdUrl);
		//logWriteUtil.writeLog(LOGTYPE_CONTROLLER, "Get openId return "+resText, LOGLEVEL_INFO, WeChatController.class);
		JSONObject jsonTexts = JSONObject.fromObject(resText);
		// openId
		Object obj = jsonTexts.get("openid");
		if(obj!=null){
			openId=obj.toString();
		}
		return openId;
	}*/
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
