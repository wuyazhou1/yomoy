package com.nsc.Amoski.controller;

import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.TbUsreMessageEntity;
import com.nsc.Amoski.repository.BaseController;
import com.nsc.Amoski.FeignClient.UserServerApi;
import com.nsc.Amoski.service.*;
import com.nsc.Amoski.uti.PayUtil;
import com.nsc.Amoski.uti.ResultUtil;
import com.nsc.Amoski.uti.WeChatUtil;
import com.nsc.Amoski.util.*;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.weixin4j.util.SHA1;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ActivityServerBaseController<T> extends BaseController {
    //打印日志对象
    Logger log;

    public RegUtil regUtil= RegUtil.getSingleton();

    @Autowired
    UserServerApi userApi;

    @Autowired
    MemberService memberService;

    @Autowired
    WaterMarkManageService waterMarkManageService;

    @Autowired
    PhotoManageService photoManageService;

    @Autowired
    ShopManageService shopManageService;

    @Autowired
    UserCenterManageService userCenterManageService;

    @Autowired
    GuideRouteManageService guideRouteManageService;

    @Autowired
    ActivityOrderManageService activityOrderManageService;

    //微信网页授权成功跳转页面
    public static String webWechatLoginPageUrl;

    //引导关注公众号页面
    public static String attentionWechatUrl;


    @SuppressWarnings("unchecked")
    public ActivityServerBaseController() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        log= LoggerFactory.getLogger((Class<T>) type.getActualTypeArguments()[0]);
        //System.out.println("Dao实现类是：" + entityClass.getName());
    }

    public Result success(){
        Result result = ResultUtil.success();
        log.info("===============responseData:"+GsonUtil.dtoToJson(result));
        return result;
    }
    public <T> Result success(T t){
        Result result = ResultUtil.success(t);
        log.info("===============responseData:"+ GsonUtil.dtoToJson(result));
        return result;
    }
    public Result error(String code,String msg){
        Result result = ResultUtil.error(code,msg);
        log.info("===============responseData:"+GsonUtil.dtoToJson(result));
        return result;
    }

    public Result error(ResultMsg msg){
        Result result = ResultUtil.error(msg);
        log.info("===============responseData:"+ GsonUtil.dtoToJson(result));
        return result;
    }
    public <T> Result error(ResultMsg msg,T t){
        Result result = ResultUtil.error(msg,t);
        log.info("===============responseData:"+GsonUtil.dtoToJson(result));
        return result;
    }

    /**
     * 验证验证码是否错误
     * @param mobile 手机号
     * @param type  验证码类型(1:注册 2:免密登录 3:忘记密码)
     * @param code  用户输入验证码
     * @return  0:成功  1:验证码失效或不存在  2:验证码错误
     */
    public int validMobileCodeIsTrue(String mobile,String type,String code){
        int result=0;//返回值
        String redisKey=REDIS_MOBILECODE_KEY+"_"+mobile+"_"+type;
        Object codeObj=memberService.getTelChecked(redisKey,null);//获取redis验证码
        log.info(">>>>>>>>>>>>>>>>>>redisKey:"+redisKey);
        if(codeObj!=null){
            RegPhoneRandomNumDto dto=(RegPhoneRandomNumDto)codeObj;
            if(code==null||!code.equals(dto.getVaildCode())){//验证码错误
                result=2;
            }else{
                //memberService.saveMobileValidCode(redisKey,null);//获取redis验证码
            }
        }else{//验证码不存在或已失效
            result=1;
        }
        return result;
    }
    /**
     * 保存用户信息
     */
    public void saveRedisUserInfo(HttpServletRequest request, MemberView dto){
        String sessionId=request.getSession().getId();
        String appToken = request.getHeader("appToken");
        if(!RegUtil.getSingleton().isNull(appToken)){
            sessionId=appToken;
        }
        String reidsKey=REDIS_USERINFO_KEY+"_"+sessionId;//用户保存redis key
        memberService.setRedisUserObj(reidsKey,dto);
        log.info(">>>>>>>>>>>>redis user info !!!!! sessionId:"+sessionId+"======dto:"+dto);
    }
    /**
     * 登出清除用户信息
     */
    public void clearCacheData(HttpServletRequest request){
        log.info(">>>>>>>>>>>>clearCacheData");
        String sessionId=request.getSession().getId();
        String appToken = request.getHeader("appToken");
        if(!RegUtil.getSingleton().isNull(appToken)){
            sessionId=appToken;
        }
        String reidsKey=REDIS_USERINFO_KEY+"_"+sessionId;//用户保存redis key
        memberService.clearRedisUserObj(reidsKey);
    }
    /**
     * 获取用户redis信息
     * sessionId 请求sessionid
     * bl 是否更新用户数据
     */
    public MemberView getRedisUserInfo(HttpServletRequest request,boolean bl){
        String sessionId=request.getSession().getId();
        String appToken = request.getHeader("appToken");
        if(!RegUtil.getSingleton().isNull(appToken)){
            sessionId=appToken;
        }
        String reidsKey=REDIS_USERINFO_KEY+"_"+sessionId;
        MemberView dto=memberService.getRedisUserObj(reidsKey,null);
        if(bl&&dto!=null){
            MemberView dbMemberInfo=null;
            if(!regUtil.isNull(dto.getOpenId())){
                dbMemberInfo = userApi.findMemberView(dto.getOpenId(), null, null,null);//查询用户信息
            }else{
                dbMemberInfo = userApi.findMemberView(null, dto.getTel(), dto.getLoginname(),null);//查询用户信息
            }
            memberService.setRedisUserObj(reidsKey,dbMemberInfo);//更新redis中用户数据
            dto=dbMemberInfo;
        }
        log.info(">>>>>>>>>>>>redis user info !!!!!sessionId:"+sessionId+">>>>>... dto:"+dto);
        return dto;
    }

    /**
     * 微信用户信息获取
     * @param code
     * @return
     * @throws Exception
     */
    public WechatUserInfoDto wechatGetUserInfo(String code,String type) throws Exception{
        log.info(">>>>>>>>>>>>>>>>> callback .responseUrl  code:"+code);
        AccessToken dto= WeChatUtil.getAuthorCode(code,memberService,type);
        log.info(">>>>>>>>>>>>>>>>> user info  dto:"+dto);
        WechatUserInfoDto dto1=null;
        if(dto!=null){
            String accessToken = dto.getAccess_token(),infoType="";
            if("web".equals(type)){
                accessToken=WeChatUtil.getAccessToken(memberService);
                infoType=WeChatUtil.USER_INFO_TYPE;
            }
            String userInfo=WeChatUtil.getUserInfo(dto.getOpenid(),accessToken,infoType);
            log.info("获取从微信获取得数据userInfo==》"+userInfo);
            //保存用户信息
            dto1=(WechatUserInfoDto)GsonUtil.jsonToDto(userInfo, WechatUserInfoDto.class);
            log.info("微信数据转换成实体类:"+dto1);
        }
        return dto1;
    }

    /**
     * 微信信息dto转换成用户信息dto
     * @param dto
     * @return
     */
    public MemberView wechatDtoChangeToMember(WechatUserInfoDto dto,MemberView userObj){
        //设置微信属性
        userObj.setOpenId(dto.getOpenid());
        userObj.setHeadImgUrl(dto.getHeadimgurl());
        userObj.setNickname(dto.getNickname());
        userObj.setName(dto.getNickname());
        userObj.setCity(dto.getCity());
        userObj.setProvince(dto.getProvince());
        userObj.setCountry(dto.getCountry());
        userObj.setLanguage(dto.getLanguage());
        userObj.setSex(dto.getSex());
        userObj.setHeadImgFile(dto.getHeadimgurl());
        userObj.setMemberSex(dto.getSex());
        userObj.setOrgCode("-1");
        userObj.setBindingIdentification("1");
        userObj.setAddress(dto.getProvince()+"、"+dto.getCountry());
        userObj.setLoginname("");
        //userObj.setBindingIdentification("1");//绑定标识
        //dto1.getUnionid();//用户公众平台唯一id
        log.info("wechatDtoChangeToMember method>>>>>>>>>>>>dto:"+dto+">>>>>>>>>>userObj:"+userObj+">>>>>>>>>>>>>");
        return userObj;
    }

    /**
     * 上传文件
     * @param file 文件
     * @param baseUrl  文件跟目录
     * @return
     * @throws Exception
     */
    public Result uploadFile(MultipartFile file, String baseUrl,String uploadUrl,int id) throws Exception{
        // 判断是否上传了文件
        if (file.isEmpty()) {
            log.info("Not upload file");
            return error(ResultMsg.UPLOADFILE_EMPTY_ERROR);
        }
        // 判断文件是否太大
        byte[] bytes = file.getBytes();
        /*if (bytes.length > Long.parseLong(Encypter.getValueByKey(MAX_PIC_SIZE))) {
            logUtil.writeInfoLog("File too big size is "+bytes.length+",max size is "+Long.parseLong(Encypter.getValueByKey(MAX_PIC_SIZE)));
            return error(ResultMsg.UPLOADFILE_SOBIG);
        }*/
        //文件类型 判断是否为图片
        log.info("File size is "+bytes.length+"=====File type is "+file.getContentType());
        if(!regUtil.isImage(file.getContentType())){
            log.info("File type not picture");
            return error(ResultMsg.UPLOADFILE_TYPE_ERROR);
        }
        //基础路径(绝对路径)
        //baseUrl=BASE_UPLOAD_URL;
        baseUrl =StringUtils.getFilePath();//request.getServletContext().getRealPath("/upload");
        log.info(">>>>>>>>>>>>.....baseUrl:"+baseUrl);

        // 判断目录是否存在
       /* String dirPath = Encypter.getValueByKey(UPLOAD_PIC_PATH);
        String nowDateStr=FILETYPEDIR+ File.separator;//当前文件类型目录*/
        String originaImgPath=baseUrl+uploadUrl+"originalImg";
        String smallImgPath=baseUrl+uploadUrl+"smallImg";
        originaImgPath=appendDtDir(originaImgPath);//加年月日文件夹
        smallImgPath=appendDtDir(smallImgPath);//加年月日文件夹
        File fileDir = new File(originaImgPath);
        if(!fileDir.exists()){
            log.info("Creat dir "+originaImgPath);
            fileDir.mkdirs();
        }
        File fileDir1 = new File(smallImgPath);
        if(!fileDir1.exists()){
            log.info("Creat dir "+smallImgPath);
            fileDir1.mkdirs();
        }
        // 文件名 用户id+用户类型+当前日期
        String fileName = id + DateUtils.getNowDay(DateUtils.FORMAT_YYYYMMDDHHMMSSSSS)+"."+file.getContentType().split("/")[1];
        // 文件路径
        String filePath = originaImgPath + fileName;
        File newFile = new File(filePath);
        newFile.setReadable(true, true);
        // 转存文件
        file.transferTo(newFile);
        log.info("Upload save success,filePath is "+filePath);

        //压缩图
        ImgCompress icp=new ImgCompress(newFile);
        int width=450, height=680;
        icp.resize(width,height,smallImgPath+fileName);
        Map<String,Object> returnObj=new HashMap<String, Object>();
        String return_base_url=/*request.getScheme()+"://"+request.getRemoteAddr()+":"+request.getServerPort()*/"http://17n97122k7.imwork.net/AmoskiActivity/";
        returnObj.put("originaImgPath",originaImgPath.substring(originaImgPath.indexOf(uploadUrl))+fileName);//原图路径
        returnObj.put("smallImgPath",smallImgPath.substring(smallImgPath.indexOf(uploadUrl))+fileName);//压缩图路径
        log.info(">>>>>>>...return url!!!!! return_base_url:"+return_base_url+"=====originaImgPath:"+originaImgPath+"=====smallImgPath:"+smallImgPath);
        return success(returnObj);
    }

    /**
     * 压缩文件
     *
     * @param filePaths 需要压缩的文件路径集合
     * @throws IOException
     */
    public void zipFile(String [] filePaths, ZipOutputStream zos) {
        //设置读取数据缓存大小
        byte[] buffer = new byte[4096];
        try {
            //循环读取文件路径集合，获取每一个文件的路径
            for (String filePath : filePaths) {
                File inputFile = new File(filePath);
                //判断文件是否存在
                if (inputFile.exists()) {
                    //判断是否属于文件，还是文件夹
                    if (inputFile.isFile()) {
                        //创建输入流读取文件
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));
                        //将文件写入zip内，即将文件进行打包
                        zos.putNextEntry(new ZipEntry(inputFile.getName()));
                        //写入文件的方法，同上
                        int size = 0;
                        //设置读取数据缓存大小
                        while ((size = bis.read(buffer)) > 0) {
                            zos.write(buffer, 0, size);
                        }
                        //关闭输入输出流
                        zos.closeEntry();
                        bis.close();
                    } else {  //如果是文件夹，则使用穷举的方法获取文件，写入zip
                        File[] files = inputFile.listFiles();
                        String [] filePathsTem = new String [files.length];
                        for(int i=0;i<files.length;i++){
                            filePathsTem[i]=files[i].toString();
                        }
                        zipFile(filePathsTem, zos);
                    }
                }
            }
            //将文件写入zip内，即将文件进行打包
            zos.putNextEntry(new ZipEntry("emp.txt"));
            zos.write(buffer, 0, 1);
            zos.closeEntry();
            log.info(">>>>>>>>>>>pakage finish!!!!!!!!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != zos) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 拼接日期
     * @param dir
     * @return
     */
    public String appendDtDir(String dir){
        return regUtil.appendDtDir(dir);
    }

    /**
     * 获取所有省份代码
     * @return
     */
    public String getProvince(){
        String jsonStr = HttpUtil.sendGet("http://www.weather.com.cn/data/city3jdata/china.html");
        return jsonStr;
    }

    /**
     * 获取省份所有城市代码
     * @return
     */
    public String getCityByProvinceCode(String provinceCode){
        String jsonStr = HttpUtil.sendGet("http://www.weather.com.cn/data/city3jdata/provshi/"+provinceCode+".html");
        return jsonStr;
    }

    /**
     * 获取城市所有区域代码
     * @return
     */
    public String getAreaByCityCode(String cityCode){
        String jsonStr = HttpUtil.sendGet("http://www.weather.com.cn/data/city3jdata/station/"+cityCode+".html");
        return jsonStr;
    }

    /**
     * 拼接
     * @return
     */
    public String getA(String cityCode){
        String jsonStr = HttpUtil.sendGet("http://www.weather.com.cn/data/city3jdata/station/"+cityCode+".html");
        return jsonStr;
    }

    /**
     * 新增系统消息
     * @param param
     * @param userInfo
     * @param type
     */
    public void saveMsgEntity(TbUsreMessageEntity param, MemberView userInfo, int type){
        log.info(">>>>>>>>>>>>>>>saveMsgEntity !!  ================type:"+type);
        //消息
        param.setCreateTime(new Timestamp(System.currentTimeMillis()));
        param.setCreateUser(userInfo.getLoginname());
        param.setMemberId(userInfo.getId());
        param.setStatus("1");
        userCenterManageService.addUserMessage(param,userInfo,type);
    }

    /**
     * 获取微信服务号分享的参数
     * @param url 动态获取,  分享的页面实际路径, 不能带# 可以带参数
     * @return
     */
    //access_token和jsapi_ticket两个小时有效期,用redis作为缓存
    public Map<String,Object> validJSSDK(String url) throws Exception{
        Map<String,Object> map = null;
        try {
            String ticket = WeChatUtil.getJsapiTicket(memberService);
            // ticket="LIKLckvwlJT9cWIhEQTwfAXKDgwZY1aK0UqzA-SwjjXSN9bN3Dr0r7wc9fD62N0fvUNhFNOuUo8sICkwp0l3tg";
            if(regUtil.isNull(ticket)){
                throw new Exception("jsapiTicket 获取失败");
            }
            // 获取随机字符串,这里是UUID 工具就不贴出来了(32位)
            String nonceStr = UniqId.getallSymbolArrayStr(32);
            // 获时间戳
            String timestamp = System.currentTimeMillis() / 1000 + "";
            Map<String, String> packageParams = new HashMap<String, String>();
            packageParams.put("url",url);
            packageParams.put("noncestr", nonceStr);
            packageParams.put("jsapi_ticket", ticket);
            packageParams.put("timestamp", timestamp);
            log.info(">>>>>>>>>>>>>>>>>>validJSSDK!!!!!  packageParams:"+packageParams);
            // 获得拼接好的参数,按照ASCLL大小排序
            String createLinkString = PayUtil.createLinkString(packageParams);
            log.info(">>>>>>>>>>>>>>>>>>validJSSDK!!!!!  createLinkString:"+createLinkString);
            //SHA1签名,该类继承了weixin4J的WeixinSupport类, 使用的是父类的方法
            String signature = SHA1.encode(createLinkString);
            // 参数封装,返回前台
            map = new HashMap<String, Object>();
            map.put("appId",WeChatUtil.APPID);
            map.put("nonceStr",nonceStr);
            map.put("signature",signature);
            map.put("timestamp",timestamp);
            log.info(">>>>>>>>>>>>>>>>>>validJSSDK!!!!!  map:"+map);
        } catch (DataException e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    public final static String applyFileName="applyPeopleMenu.txt";
    /**
     * 写发送邮件日志
     *
     */
    public synchronized static void writeEmailLog(ActivityApplyDto dto){
        String path = File.separator+"home"+File.separator+"file"+File.separator;
        File logFile=new File(path);
        if(!logFile.exists()){
            logFile.mkdirs();
        }
        logFile=new File(path+applyFileName);
        BufferedWriter bw=null;
        try {
            bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile,true)));
            String logStr=dto.getName()+","+dto.getMobile()+","+dto.getCount()+","+DateUtils.string2TimeYs(dto.getCreateTime(),DateUtils.DATE_FORMAT_YS_TIME);//写日志
        	 /*SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
         	 String strDt = sdf.format(new Date());

             if(RegUtil.getSingleton().isNull(dto.getRespMessge())){//如果发送成功则把发送信息写入日志 否则把错误信息写入日志
            	 logStr=strDt+"    sendMsg log >>>>>> send success!! "+"sendUserName："+dto.getUserName()+"-----sendEmailName："+dto.getEmailName()+"-----sendTitle："+dto.getTitle()+"-----sendContent："+dto.getContent();
             }else{
            	 logStr=strDt+"    sendMsg log >>>>>> send fail!! "+dto.getRespMessge();
             }*/
            System.out.println("+++++sendMsgLog:"+logStr);
            bw.write(logStr);
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String,String> getCallBackParamter(HttpServletRequest request){
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            log.info(name+"==>"+valueStr);
            params.put(name, valueStr);
        }
        return params;
    }
}
