package com.nsc.Amoski.controller;

import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.dto.MapServerDataDto;
import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.dto.RidingInfoDto;
import com.nsc.Amoski.dto.TerminalDataDto;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.repository.BaseController;
import com.nsc.Amoski.serverApi.ActivityServerApi;
import com.nsc.Amoski.serverApi.UserServerApi;
import com.nsc.Amoski.service.MemberService;
import com.nsc.Amoski.service.RidingTeamManageService;
import com.nsc.Amoski.uti.ResultUtil;
import com.nsc.Amoski.util.*;
import com.nsc.Amoski.util.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.*;

public class RidingServerBaseController<T> extends BaseController {
    //打印日志对象
    Logger log;

    public RegUtil regUtil= RegUtil.getSingleton();


    //骑行数据service
    @Autowired
    RidingTeamManageService ridingManageService;

    @Autowired
    ActivityServerApi activityApi;

    @Autowired
    MemberService memberService;

    @Autowired
    UserServerApi userApi;

    @Autowired
    ActivityServerApi activityServerApi;


    @SuppressWarnings("unchecked")
    public RidingServerBaseController() {
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
     * 获取用户redis信息
     * sessionId 请求sessionid
     * bl 是否更新用户数据
     */
    public MemberView getRedisUserInfo(HttpServletRequest request){
        String sessionId=request.getSession().getId();
        String appToken = request.getHeader("appToken");
        if(!regUtil.isNull(request.getParameter("appToken"))){
            sessionId=request.getParameter("appToken");
        }
        if(!RegUtil.getSingleton().isNull(appToken)){
            sessionId=appToken;
        }
        String reidsKey=REDIS_USERINFO_KEY+"_"+sessionId;
        MemberView dto=memberService.getRedisUserObj(reidsKey,null);
        log.info(">>>>>>>>>>>>redis user info !!!!!sessionId:"+sessionId+">>>>>... dto:"+dto);
        return dto;
    }

    /**
     * 上传文件
     * @param file 文件
     * @param baseUrl  文件跟目录
     * @return
     * @throws Exception
     */
    public Result uploadFile(MultipartFile file, String baseUrl,String uploadUrl,MemberView userObj) throws Exception{
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
        //基础路径(绝对路径)
        baseUrl= StringUtils.getFilePath();
        //文件类型 判断是否为上传的数据格式
        log.info("File size is "+bytes.length+"=====File type is "+file.getContentType());
        String filePath,smallImgPath = null;
        if(regUtil.isImage(file.getContentType())||"/userRidingPic".equals(uploadUrl)){
            log.info("File type is picture");
            filePath=baseUrl+uploadUrl+"/originalImg";
            smallImgPath=baseUrl+uploadUrl+"/smallImg";
            filePath=regUtil.appendDtDir(filePath);//加年月日文件夹
            smallImgPath=regUtil.appendDtDir(smallImgPath);//加年月日文件夹
            File fileDir1 = new File(smallImgPath);
            if(!fileDir1.exists()){
                log.info("Creat dir "+smallImgPath);
                fileDir1.mkdirs();
            }
        }else{
            log.info("File type is not picture");
            filePath=baseUrl+uploadUrl;
            Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
            int hours = c.get(Calendar.HOUR_OF_DAY);
            filePath=regUtil.appendDtDir(filePath)+hours+"H"+File.separator;
        }

        // 判断目录是否存在
       /* String dirPath = Encypter.getValueByKey(UPLOAD_PIC_PATH);
        String nowDateStr=FILETYPEDIR+ File.separator;//当前文件类型目录*/
        File fileDir = new File(filePath);
        if(!fileDir.exists()){
            log.info("Creat dir "+filePath);
            fileDir.mkdirs();
        }
        // 文件名 用户id+用户类型+当前日期(时分秒)
        String fileName = userObj.getId()+"_" + DateUtils.getNowDay(DateUtils.FORMAT_YYYYMMDDHHMMSS)+"."+file.getContentType().split("/")[1];
        // 文件路径
        File newFile = new File(filePath+fileName);
        newFile.setReadable(true, true);
        // 转存文件
        file.transferTo(newFile);
        log.info("Upload save success,filePath is "+filePath+fileName);
        Map<String,Object> returnObj=new HashMap<String, Object>();
        returnObj.put("filePath",filePath.substring(filePath.indexOf(uploadUrl)).replace("\\","/")+fileName);//原图路径
        if(smallImgPath!=null){
            //压缩图
            ImgCompress icp=new ImgCompress(newFile);
            int width=150, height=80;
            icp.resize(width,height,smallImgPath+fileName);
            String return_base_url=/*request.getScheme()+"://"+request.getRemoteAddr()+":"+request.getServerPort()*/"http://17n97122k7.imwork.net/AmoskiActivity/";
            returnObj.put("smallImgPath",smallImgPath.substring(smallImgPath.indexOf(uploadUrl)).replace("\\","/")+fileName);//压缩图路径
            log.info(">>>>>>>...return url!!!!! return_base_url:"+return_base_url+"=====smallImgPath:"+smallImgPath);
        }
        return success(returnObj);
    }

    /**
     * 骑行dto转其他实体
     * @param dto
     * @param obj
     * @param <U>
     */
    public <U> U ridingDtoChangeTo(RidingInfoDto dto,U obj){
        if(obj instanceof TbRidingInfoEntity){//骑行单次总数据
            TbRidingInfoEntity entity=(TbRidingInfoEntity) obj;
            entity.setAverageSpeed(dto.getAverageSpeed());
            entity.setBaseUrl(dto.getBaseUrl());
            entity.setCreateTime(regUtil.isNull(dto.getCreateTime())?new Timestamp(System.currentTimeMillis()):dto.getCreateTime());
            entity.setCreateUser(dto.getCreateUser());
            entity.setEndPosition(dto.getEndPosition());
            entity.setMemberId(dto.getMemberId());
            entity.setPassPosition(dto.getPassPosition());
            entity.setRidingFileUrl(dto.getRidingFileUrl());
            entity.setStartPosition(dto.getStartPosition());
            entity.setTotalDistance(dto.getTotalDistance());
            entity.setTotalTime(dto.getTotalTime());
            entity.setTrackImgUrl(dto.getTrackImgUrl());
            entity.setUpdateTime(regUtil.isNull(dto.getUpdateTime())?new Timestamp(System.currentTimeMillis()):dto.getUpdateTime());
            entity.setUpdateUser(dto.getUpdateUser());
        }else if(obj instanceof TbRidingInfoDetailEntity){//骑行详情
            TbRidingInfoDetailEntity entity=(TbRidingInfoDetailEntity) obj;
            entity.setClimbHeight(dto.getClimbHeight());
            entity.setDegreePollution(dto.getDegreePollution());
            entity.setEmergencyBrakeTime(dto.getEmergencyBrakeTime());
            entity.setHumidity(dto.getHumidity());
            entity.setMaxAcceleratedSpeed(dto.getMaxAcceleratedSpeed());
            entity.setMaxSpeed(dto.getMaxSpeed());
            entity.setPhotoTime(dto.getPhotoTime());
            entity.setPmTwoFive(dto.getPmTwoFive());
            entity.setPunchPoint(dto.getPunchPoint());
            entity.setRidingBend(dto.getRidingBend());
        }
        return obj;
    }

    /**
     * json obj change to str
     * @param obj
     */
    public String objChangeToStr(Object obj){
        if(regUtil.isNull(obj)){
            return "";
        }else{
            return obj.toString();
        }
    }

    /**
     * 骑行轨迹图查询 调用活动服务
     * @param result 骑行数据
     */
    public TbPhotoPic querySingleUserImg(RidingInfoDto result){
        /*TbPhotoPic pic = activityApi.querySingleUserImg(result.getTrackImgId());
        log.info(">>>>>>>>>>>>method querySingleUserImg!!! result:"+result+"=============pic:"+pic);
        if(result!=null&&pic!=null){
            result.setBaseUrl(pic.getBaseUrl());
            result.setImgUrl(pic.getImgUrl());
            result.setSmallImgUrl(pic.getSmallUrl());
        }*/
        return null;
    }

    /**
     * 猎鹰api新增服务请求url
     */
    public static final String create_falcon_server_url="https://tsapi.amap.com/v1/track/service/add";

    /**
     * 猎鹰api新增终端请求url
     */
    public static final String create_terminal_url="https://tsapi.amap.com/v1/track/terminal/add";

    /**
     * 创建serviceId  猎鹰
     */
    public MapServerDataDto createFalconServer(){
        MapServerDataDto param =new MapServerDataDto();
        param.setMapKey(MAP_WEBSERVER_KEY);
        List<MapServerDataDto> list = ridingManageService.queryMapData(param);
        if(list!=null&&list.size()>0){
            param=list.get(0);
        }else{
            String str=UniqId.getallSymbolArrayStr(4);
            String result = HttpUtil.sendPost(create_falcon_server_url,"key="+MAP_WEBSERVER_KEY+"&name=amoski_riding_server"+str+"&desc=");
            log.info(">>>>>>>>>>>>>>>>create_terminal_url  result:"+result);
            JSONObject jsonObj = JSONObject.parseObject(result);
            String errcode = jsonObj.getString("errcode");
            if("10000".equals(errcode)){//成功
                JSONObject jsonObjData=JSONObject.parseObject(JSONObject.toJSONString(jsonObj.get("data")));
                param.setServerId(jsonObjData.getString("sid"));
                param.setServerName(jsonObjData.getString("name"));
                param.setServerDesc("");
                param.setCreateTime(new Timestamp(System.currentTimeMillis()));
                param.setCreateUser("system");
                ridingManageService.addEntity(param,false);
            }
        }
        log.info(">>>>>>>>>>> createFalconServer ！！！ >>>>>>param:"+param);
        return param;
    }

    /**
     * 创建terminalId终端  猎鹰
     */
    public TerminalDataDto createTermina(MemberView userDto,String serverId){
        TerminalDataDto param =new TerminalDataDto();
        param.setServerId(serverId);
        param.setMemberId(userDto.getId());
        List<TerminalDataDto> list = ridingManageService.queryTerminalData(param);
        if(list!=null&&list.size()>0){
            param=list.get(0);
        }else{
            param.setTerminalDesc(userDto.getId()+"_"+userDto.getLoginname());
            String result = HttpUtil.sendPost(create_terminal_url, "key=" + MAP_WEBSERVER_KEY + "&sid="+serverId+ "&name=amoski_riding_terminal_"+userDto.getId()+"&desc="+param.getTerminalDesc() );
            log.info(">>>>>>>>>>>>>>>>create_terminal_url  result:"+result);
            JSONObject jsonObj = JSONObject.parseObject(result);
            String errcode = jsonObj.getString("errcode");
            if("10000".equals(errcode)){//成功
                JSONObject jsonObjData=JSONObject.parseObject(JSONObject.toJSONString(jsonObj.get("data")));
                param.setTerminalId(jsonObjData.getString("tid"));
                param.setTerminalName(jsonObjData.getString("name"));
                param.setCreateTime(new Timestamp(System.currentTimeMillis()));
                param.setCreateUser(userDto.getLoginname());
                TbTerminalDataEntity entity=new TbTerminalDataEntity();
                BeanUtils.copyProperties(param,entity);
                ridingManageService.addEntity(entity,false);
            }else{
                return null;
            }
        }
        log.info(">>>>>>>>>>> createTermina ！！！ >>>>>>param:"+param);
        return param;
    }

    /**
     * 骑行数据文件解析
     */
    public void ridingFileDataResolver(String fileUrl){
        // 解析books.xml文件
        // 创建SAXReader的对象reader
        SAXReader reader = new SAXReader();
        try {
            FileInputStream in = regUtil.getFileStream(fileUrl, "");
            // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
            Document document = reader.read(in);
            // 通过document对象获取根节点bookstore
            Element data = document.getRootElement();
            // 通过element对象的elementIterator方法获取迭代器
            Iterator it = data.elementIterator();
            // 遍历迭代器，获取根节点中的信息（书籍）
            while (it.hasNext()) {
                log.info("=====开始遍历xml文件数据=====");
                Element book = (Element) it.next();
                // 获取book的属性名以及 属性值
                List<Attribute> bookAttrs = book.attributes();
                for (Attribute attr : bookAttrs) {
                    log.info("属性名：" + attr.getName() + "--属性值："
                            + attr.getValue());
                }
                Iterator itt = book.elementIterator();
                while (itt.hasNext()) {
                    Element bookChild = (Element) itt.next();
                    log.info("节点名：" + bookChild.getName() + "--节点值：" + bookChild.getStringValue());
                }
                log.info("=====结束遍历xml=====");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
