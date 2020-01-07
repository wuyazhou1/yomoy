package com.nsc.Amoski.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.uti.ResultUtil;
import com.nsc.Amoski.util.*;
import com.nsc.Amoski.util.StringUtils;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;
import java.util.zip.ZipOutputStream;


/**
 *
 */
@RestController
@RequestMapping("/userCenterManage")
@Api(value="userCenterManage",description = "用户中心管理")
public class UserCenterManageController extends ActivityServerBaseController<UserCenterManageController> {

    /**
     * 查询所有消息
     * @return  用户信息消息
     */

    @RequestMapping(value="/queryUserMessage",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询用户所有消息", notes = "查询用户所有消息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="当前页",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="limit",value="每页条数",dataType="int", paramType = "query")
    })
    public Result queryUserMessage(HttpServletResponse response, HttpServletRequest request,@RequestBody UsreMessageDto param){
        log.info(">>>>>>>>>>>>> queryUserMessage .requestParam param:"+param);
        MemberView userDto=getRedisUserInfo(request,false);
        param.setMemberId(userDto.getId());
        PagingBean bean = userCenterManageService.queryUserMessage(param);

        return success(bean);
    }

    /**
     * 查询用户个人信息
     */
    @RequestMapping(value="/queryUserInfo",method = {RequestMethod.POST,RequestMethod.GET,RequestMethod.OPTIONS})
    @ApiOperation(value="查询用户个人信息", notes = "查询用户个人信息", httpMethod = "POST" )
    public Result queryUserInfo(HttpServletResponse response,HttpServletRequest request){
        MemberView userDto=getRedisUserInfo(request,true);//查询并更新
        userDto.setSalt("");
        userDto.setPassword("");
        return success(userDto);
    }
    /**
     * 个人头像上传照片
     */
    @RequestMapping(value="/uploadHeaderFile")
    @ApiOperation(value="个人资料上传头像", notes = "个人资料上传头像", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", paramType="query", value = "临时文件", dataType="file", required = true),
    })
    public Result uploadHeaderFile( HttpServletResponse response, HttpServletRequest request,
                                    MultipartFile file) throws Exception{
        log.info("上传头像开始==方法简介==个人资料上传头像==开始"+file.getName());
        MemberView userDto=getRedisUserInfo(request,false);//查询并更新
        String baseUrl = StringUtils.getFilePath()+ File.separator+"Activity";//request.getServletContext().getRealPath("/upload");
        log.info("开始路径"+baseUrl);
        String uploadUrl=File.separator+"Activity"+File.separator+"userHeaderPic";
        Result rs = uploadFile(file, baseUrl, uploadUrl,userDto.getId());//上传文件
        log.info("上传头像结束==方法简介==个人资料上传头像==结束"+file.getName());
        log.info("上传头像参数"+rs);
        return rs;
    }

    @RequestMapping(value="/updateUserInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="更新用户信息", notes = "更新用户信息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="name",value="昵称",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="headImgFile",value="会员头像文件路径",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="yearOfBirth",value="会员出生年月",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="address",value="会员地址",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="synopsis",value="简介",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="memberSex",value="性别",dataType="String", paramType = "query"),
    })
    public Result updateUserInfo(HttpServletResponse response,HttpServletRequest request,@RequestBody MemberView dto){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>updateUserInfo ！！！all param:"+dto);
        MemberView userDto=getRedisUserInfo(request,false);
        //数据校验
        if(regUtil.isNull(dto.getHeadImgFile(),dto.getName(),dto.getMemberSex(),dto.getYearOfBirth(),dto.getAddress())){//参数为空
            return error(ResultMsg.IS_NULL);
        }
        if(Integer.parseInt(dto.getMemberSex())>2||Integer.parseInt(dto.getMemberSex())<0){//参数错误
            log.info(">>>>>>>>>>>>>>....sex error ");
            return error(ResultMsg.IS_NULL);
        }
        if(dto.getName().length()>16){
            log.info(">>>>>>>>>>>>>>....name error ");
            return error(ResultMsg.IS_NULL);
        }
        try{//日期错误
            DateUtils.string2Time(dto.getYearOfBirth());
        }catch (Exception e){
            log.info(">>>>>>>>>>>>>>....date error ");
            return error(ResultMsg.IS_NULL);
        }
        userDto.setHeadImgFile(dto.getHeadImgFile());
        userDto.setHeadImgProject(StringUtils.getFilePath());
        userDto.setName(dto.getName());
        userDto.setMemberSex(dto.getMemberSex());
        userDto.setYearOfBirth(dto.getYearOfBirth());
        userDto.setAddress(dto.getAddress());
        userDto.setSynopsis(dto.getSynopsis());//简介
        MemberView memberView = userApi.updateMemberView(userDto);//更新
        String sessionId=request.getSession().getId();
        String appToken = request.getHeader("appToken");
        if(!RegUtil.getSingleton().isNull(appToken)){
            sessionId=appToken;
        }
        String reidsKey=REDIS_USERINFO_KEY+"_"+sessionId;
        log.info("userDto==》"+userDto);
        memberService.setRedisUserObj(reidsKey,userDto);
        //saveRedisUserInfo(request.getSession().getId(),userDto);//更新session
        return success();
    }

    @RequestMapping(value="/updateVehicleInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="更新用户绑定车辆信息", notes = "更新用户绑定车辆信息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="id",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="carName",value="车辆名称",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="carImg",value="车辆图片",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="carBrandId",value="车辆型号id",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="brandName",value="车辆品牌名称",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="brandTypeName",value="车辆品牌下型号名称",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="isDefault",value="是否默认车辆",dataType="String", paramType = "query")
    })
    public Result updateVehicleInfo(HttpServletResponse response,HttpServletRequest request,@RequestBody VehicleInfoDto param){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>updateVehicleInfo ！！！all param:"+param);
        if(regUtil.isNull(param.getId())){//修改id不能为空
            return error(ResultMsg.IS_NULL);
        }
        boolean bl=false;//是否修改默认车辆
        MemberView userDto=getRedisUserInfo(request,false);
        TbVehicleInfoEntity entity = userCenterManageService.querySingleBindVehicleInfo(param.getId() + "", userDto.getId() + "");
        if(regUtil.isNull(entity)){//修改车辆不存在
            log.info(">>>>>>>>>>>>>>>>>>update fail vehicle not exist!!!!!");
            return error(ResultMsg.IS_NULL);
        }
        if("1".equals(param.getIsDefault())&&!"1".equals(entity.getIsDefault())){//是否修改默认 并且当前车辆不为默认
            bl=true;
            entity.setIsDefault("1");
        }
        if(!regUtil.isNull(param.getCarImg())){//修改车辆图片
            entity.setCarImg(param.getCarImg());
        }
        if(!regUtil.isNull(param.getCarName())){//修改车辆名称
            entity.setCarName(param.getCarName());
        }
        if(!regUtil.isNull(param.getCarBrandId())){//修改车辆类型
            if(regUtil.isNull(param.getBrandName(),param.getBrandTypeName())){//名称不能为空
                log.info(">>>>>>>>>>>>>..update car info is not null  error");
                return error(ResultMsg.IS_NULL);
            }
            entity.setCarBrandId(param.getCarBrandId());
            entity.setBrandName(param.getBrandName());
            entity.setBrandTypeName(param.getBrandTypeName());
        }
        //更新
        userCenterManageService.updateSelfVehicle(entity,bl);//更新
        return success();
    }


    /**
     * 查询相册的照片
     * @param dto 查询参数 会员id
     * @return  相册所有照片信息
     */
    @RequestMapping(value="/queryUserPicByUserId",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询相册的照片", notes = "查询相册的照片", httpMethod = "GET" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="当前页",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="limit",value="每页条数",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="memberId",value="会员id",dataType="int", paramType = "query")
    })
    public Result queryUserPicByUserId(HttpServletResponse response,HttpServletRequest request,@RequestBody PhotoPicDto dto) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>queryUserPicByUserId ！！！all param:"+dto);
        MemberView userDto=getRedisUserInfo(request,false);
        if(userDto==null){
            if(StringUtils.isTextMember){
                userDto = new MemberView();
                userDto.setId(StringUtils.memberId);
            }else{
                return ResultUtil.error(ResultMsg.NOT_LOGIN);
            }
        }
        if(StringUtils.isEmpty(dto.getMemberId())){//会员id为空传入当前登入会员id，查询会员相册
            dto.setMemberId(userDto.getMemberId());
        }else{
            log.info(userDto.getMemberId()+"会员查询"+dto.getMemberId()+"会员的相册");
        }
        PagingBean list =userCenterManageService.queryUserPhotoImg(dto);//用户相册所有图片;
        /*List<PhotoPicDto> data = list.getData();//遍历数据分组
        Map<String,List<PhotoPicDto>> map=new HashMap<String, List<PhotoPicDto>>();
        for(PhotoPicDto pdto:data){
            String dtStr = DateUtils.string2TimeYs(pdto.getCreateTime(), DateUtils.DATE_FORMAT);
            List<PhotoPicDto> temp=null;
            if(map.containsKey(dtStr)){
                temp=map.get(dtStr);
            }else{
                temp=new ArrayList<PhotoPicDto>();
            }
            temp.add(pdto);
            map.put(dtStr,temp);
        }*/
        return success(list);
    }

    /**
     * app图片指向地址
     */
    @RequestMapping(value="/getImg",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="app图片指向地址", notes = "app图片指向地址", httpMethod = "GET" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="imgUrl",value="图片路径",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="baseUrl",value="基础路径",dataType="String", paramType = "query")
    })
    public void getImg( HttpServletResponse response, HttpServletRequest request,String imgUrl,String baseUrl) throws Exception{
        log.info(">>>>>>>>>>>>>>>getImg....url:"+imgUrl);
        /*if(regUtil.isNull(imgUrl)){

        }*/
        InputStream in =null;
        if(StringUtils.isEmpty(baseUrl)){
            if(System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1){
                if(imgUrl.indexOf("D:")==-1){
                    baseUrl = "D://uploadFile/images";
                }else{
                    log.info("app图片指向地址baseUrl==>"+baseUrl+",imgUrl==>"+imgUrl);
                    byte[] arr=null;
                    try {
                        File file = new File(imgUrl);
                        FileInputStream fis = new FileInputStream(file);
                        arr = new byte[fis.available()];
                        fis.read(arr);
                        response.getOutputStream().write(arr);
                        fis.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }else{
                if(imgUrl.indexOf("home")==-1){
                    baseUrl = "/home/uploadFile/images";
                }else{
                    log.info("app图片指向地址baseUrl==>"+baseUrl+",imgUrl==>"+imgUrl);
                    byte[] arr=null;
                    try {
                        File file = new File(imgUrl);
                        FileInputStream fis = new FileInputStream(file);
                        arr = new byte[fis.available()];
                        fis.read(arr);
                        response.getOutputStream().write(arr);
                        fis.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }
        log.info("访问图片"+baseUrl+imgUrl);
        log.info("app图片指向地址baseUrl==>"+baseUrl+",imgUrl==>"+imgUrl);
        if(imgUrl.startsWith("http")){//请求图片资源
            in = HttpUtil.sendGet(imgUrl, "1") ;
        }else{
            in=regUtil.getFileStream(imgUrl, baseUrl);
        }
        if(in!=null){
            byte [] by=new byte[in.available()];
            int n=0;
            while((n=in.read(by))!=-1){
                response.getOutputStream().write(by);
                log.info(">>>>>>>>>>>>>>>>>byte length!!!!"+by.length+">>>>>>>>>>>>>");
            }
            if(in!=null){
                in.close();
            }
        }
    }

    //@ResponseBody
    @RequestMapping(value = "getFile",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="获取文件", notes = "获取文件", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="fileNameUrl",value="活动文件路径",dataType="string", paramType = "query"),
    })
    public void getFile(HttpServletRequest request,HttpServletResponse response){
        String fileNameUrl = request.getParameter("fileNameUrl");
        byte[] arr=null;
        try {
            File file = new File(fileNameUrl);
            FileInputStream fis = new FileInputStream(file);
            arr = new byte[fis.available()];
            fis.read(arr);
            response.getOutputStream().write(arr);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return arr;
    }

    /**
     * 活动图片下载
     */
    @RequestMapping(value="/activityImgDown",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="活动图片下载", notes = "活动图片下载", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="fileNameUrl",value="文件路径",dataType="String", paramType = "query"),
    })
    public void activityImgDown(HttpServletRequest request,HttpServletResponse response, ActivityCalendarImagesDto param) throws Exception{
        log.info(">>>>>>>>>fileNameUrl:"+param.getFileNameUrl());
        String [] srcFiles=param.getFileNameUrl().split(",");
        String zipFileName="image.zip";
        try {
            response.reset(); // 重点突出
            response.setCharacterEncoding("UTF-8"); // 重点突出
            response.setContentType("application/x-msdownload"); // 不同类型的文件对应不同的MIME类型 // 重点突出
            // 对文件名进行编码处理中文问题
            zipFileName = new String(zipFileName.getBytes(), StandardCharsets.UTF_8);
            // inline在浏览器中直接显示，不提示用户下载
            // attachment弹出对话框，提示用户进行下载保存本地
            // 默认为inline方式
            response.setHeader("Content-Disposition", "attachment;filename=" + zipFileName);
            // --设置成这样可以不用保存在本地，再输出， 通过response流输出,直接输出到客户端浏览器中。
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            zipFile(srcFiles, zos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把活动相册保存到相册
     * @param dto 把活动相册保存到相册
     */
    @RequestMapping(value="/saveActivityPicToUserPic",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="把活动相册保存到相册", notes = "把活动相册保存到相册", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="ids",value="要删除的图片id集合(用,分割)",dataType="String", paramType = "update")
    })
    public Result saveActivityPicToUserPic(HttpServletResponse response,HttpServletRequest request,@RequestBody  PhotoPicDto dto){
        log.info(">>>>>>>>>>>>> saveActivityPicToUserPic .requestParam =========ids:"+dto.getIds());
        if(regUtil.isNull(dto.getIds())){
            return error(ResultMsg.IS_NULL);
        }
        MemberView userDto=getRedisUserInfo(request,false);
        List<TbPhotoPic> list=new ArrayList<>();
        //根据ids查询活动里所有相册
        String whereSql=" 1=1 and  exists (select * from (select regexp_substr(:ids,'[^,]+',1,rownum) did from dual"+
                " connect by rownum<=length(regexp_replace(:ids,'[^,]+'))+1) b where id=b.did)";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("ids",dto.getIds());
        List<ActivityCalendarImagesEntity> activityList = userCenterManageService.queryListEntity(new ActivityCalendarImagesEntity(), whereSql, map);
        for(ActivityCalendarImagesEntity entity:activityList){
            TbPhotoPic pic=new TbPhotoPic();
            pic.setImgType(1);
            pic.setSmallUrl(entity.getFilePath().replace(StringUtils.getFilePath(),"")+File.separator+entity.getImgCompress());
            pic.setBaseUrl(StringUtils.getFilePath());
            pic.setImgUrl(entity.getFilePath().replace(StringUtils.getFilePath(),"")+File.separator+entity.getFileNameUrl());
            pic.setCreateUser(userDto.getLoginname());
            pic.setMemberId(userDto.getId());
            pic.setStatus("1");
            pic.setCreateTime(DateUtils.getCurrentStamp());
            list.add(pic);
        }
        log.info(">>>>>>>>>saveActivityPicToUserPic addPicList:"+list);
        if(list.size()>0){
            userCenterManageService.addPhotoPic(list);
        }
        return success();
    }

    /**
     * 相册上传照片
     */
    @RequestMapping(value="/uploadFile")
    @ApiOperation(value="相册上传照片", notes = "相册上传照片", httpMethod = "POST" )
    public Result uploadFile( HttpServletResponse response, HttpServletRequest request,MultipartFile [] files) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...uploadFile!! filelength:"+files.length+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> fileNames = multipartRequest.getFileNames();
        String fileName1 = fileNames.hasNext() ? fileNames.next() : ""; // 得到文件名（注意。是content-type
        // 中的name="file1"，而不是真正的文件名）
        List<MultipartFile> files1 = multipartRequest.getFiles(fileName1);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..."+files1.size()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        if(files.length==0){
            return error(ResultMsg.IS_NULL);
        }
        MemberView userDto=getRedisUserInfo(request,false);
        String baseUrl =StringUtils.getFilePath()+ File.separator+"Activity";//request.getServletContext().getRealPath("/upload");
        List<TbPhotoPic> list=new ArrayList<TbPhotoPic>();
        String uploadUrl=File.separator+"Activity"+File.separator+"userPhotoPic";
        for (MultipartFile mFile : files) {
            Result rs = uploadFile(mFile, baseUrl,uploadUrl,userDto.getId());//上传文件
            if(ResultMsg.SUCCESS.getCode().equals(rs.getCode())){//上传成功
                String json=GsonUtil.dtoToJson(rs.getData());
                JSONObject jobj= JSON.parseObject(json);
                //写库
                TbPhotoPic pic=new TbPhotoPic();
                pic.setStatus("1");
                pic.setMemberId(userDto.getMemberId());
                pic.setCreateUser(userDto.getLoginname());
                pic.setCreateTime(new Timestamp(System.currentTimeMillis()));
                pic.setBaseUrl(StringUtils.getFilePath());
                pic.setImgType(1);
                pic.setImgUrl(jobj.get("originaImgPath").toString());
                pic.setSmallUrl(jobj.get("smallImgPath").toString());
                list.add(pic);
            }
        }
        userCenterManageService.addPhotoPic(list);
        return success(list);
    }


    /**
     * 查询所有水印
     * @param request
     * @param
     * @return 查询所有水印
     */
    @RequestMapping(value="/queryAllWaterMake",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询所有水印", notes = "查询所有水印", httpMethod = "GET" )
    public Result queryAllWaterMake(HttpServletResponse response,HttpServletRequest request){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>queryAllWaterMake ！！！all");
        WaterMakeInfoDto param =new WaterMakeInfoDto();
        param.setPage(1);
        param.setLimit(100);
        param.setStatus("1");
        PagingBean pageBean = waterMarkManageService.queryWaterMakeByDept(param);
        return success(pageBean.getData());
    }

    /**
     * 删除相册图片
     * @param dto 要删除相册图片id
     */
    @RequestMapping(value="/removePic",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="删除相册图片", notes = "删除相册图片", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="ids",value="要删除的图片id集合(用,分割)",dataType="String", paramType = "update")
    })
    public Result removePic(HttpServletResponse response,HttpServletRequest request,@RequestBody  PhotoPicDto dto){
        log.info(">>>>>>>>>>>>> removePic .requestParam =========ids:"+dto.getIds());
        MemberView userDto=getRedisUserInfo(request,false);
        dto.setMemberId(userDto.getMemberId());
        userCenterManageService.deleteUserPic(dto);
        return success();
    }
    /**
     * 查询用户某张图片
     * @param imgId 图片id
     */
    @RequestMapping(value="/querySingleUserImg",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询用户某张图片", notes = "查询用户某张图片", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="imgId",value="图片id",dataType="String", paramType = "query")
    })
    public TbPhotoPic querySingleUserImg(HttpServletRequest request,Integer imgId){
        MemberView userDto=getRedisUserInfo(request,false);
        log.info(">>>>>>>>>>>>> querySingleUserImg .requestParam =========uid:"+userDto.getId()+"========imgId:"+imgId);
        TbPhotoPic  pic = userCenterManageService.querySingleUserImg(userDto.getId(), imgId);
        return pic;
    }

    /**
     * 根据一个或多个图片id查询图片
     * @param imgIds 图片id
     */
    @RequestMapping(value="/queryImgByImgId",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="根据一个或多个图片id查询图片", notes = "根据一个或多个图片id查询图片", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="imgIds",value="用户id",dataType="String", paramType = "query"),
    })
    public List<TbPhotoPic> queryImgByImgId(HttpServletRequest request,String imgIds){
        log.info(">>>>>>>>>>>>> queryImgByImgId .requestParam ========imgIds:"+imgIds);
        List<TbPhotoPic> pics = userCenterManageService.queryImgByImgId(imgIds);
        return pics;
    }


    /**
     * 上传车辆照片
     */
    @RequestMapping(value="/uploadVehicleFile")
    @ApiOperation(value="上传车辆照片", notes = "上传车辆照片", httpMethod = "POST" )
    public Result uploadVehicleFile( HttpServletResponse response, HttpServletRequest request,MultipartFile file) throws Exception{
        log.info(file+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>...");
        MemberView userDto=getRedisUserInfo(request,false);
        String baseUrl =StringUtils.getFilePath()+ File.separator+"Activity";//request.getServletContext().getRealPath("/upload");
        String uploadUrl=File.separator+"Activity"+File.separator+"userVehiclePic";
        Result rs = uploadFile(file, baseUrl,uploadUrl ,userDto.getId());//上传文件
        return rs;
    }


    /**
     * 更新相片信息（删除修改状态）
     * @param ids 更新的相册数据id
     */
    /*@RequestMapping(value="/updateUserPicInfo",method = {RequestMethod.POST,RequestMethod.GET})
    public Result updateUserPicInfo(String userId,String ids){
        log.info(">>>>>>>>>>>>> updateUserPicInfo .requestParam userId:"+userId+"=========ids:"+ids);
        List<TbPhotoPic> photoList = photoManageService.queryUserPicById(userId,ids);
        if(photoList.size()>0){//长度大于零
            List<Object> updList=new ArrayList<Object>();
            for(TbPhotoPic pic:photoList){
                pic.setStatus("0");
                updList.add(pic);
            }
            photoManageService.deleteData(updList);
        }
        return success();
    }*/

    /**
     * 查询用户所有车辆信息
     * @param request
     * @param param
     * @return 用户所有车辆信息
     */
    @RequestMapping(value="/queryUserVehicleInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询用户所有车辆信息", notes = "查询用户所有车辆信息", httpMethod = "GET" )
    public Result queryUserVehicleInfo(HttpServletResponse response,HttpServletRequest request, VehicleInfoDto param){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>queryUserVehicleInfo ！！！all param:"+param);
        MemberView userDto=getRedisUserInfo(request,false);
        param.setMemberId(userDto.getMemberId());
        List<VehicleInfoDto> list = userCenterManageService.queryAllBindVehicleInfo(param);//用户所有车辆信息
        return success(list);
    }
    /**
     * 查询所有车辆品牌信息
     * @return 所有车辆品牌信息
     */
    @RequestMapping(value="/queryUserVehicleBrandInfo",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询所有车辆品牌信息", notes = "查询所有车辆品牌信息", httpMethod = "GET" )
    public Result queryUserVehicleBrandInfo(HttpServletResponse response){
        List<VehicleBrandDto> list = userCenterManageService.queryAllBindVehicleBrandInfo();//所有车辆品牌信息
        return success(list);
    }

    /**
     * 查询所有车辆品牌信息以及下面类型
     * @return 所有车辆品牌信息以及下面类型
     */
    @RequestMapping(value="/queryAllVehicleBrandAndType",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询所有车辆品牌信息以及下面类型", notes = "查询所有车辆品牌信息以及下面类型", httpMethod = "GET" )
    public Result queryAllVehicleBrandAndType(HttpServletResponse response){
        List<VehicleBrandDto> list = userCenterManageService.queryAllBindVehicleBrandInfo();//所有车辆品牌信息
        List<VehicleBrandTypeDto> typeList = userCenterManageService.queryVehicleBrandType();//所有车辆类型
        Map<Integer,List<VehicleBrandTypeDto>> map=new HashMap<Integer, List<VehicleBrandTypeDto>>();//所有车辆
        for (VehicleBrandTypeDto typeDto:typeList){//
            Integer key=typeDto.getBrandId();
            List<VehicleBrandTypeDto> tmpList=map.get(key);
            if(tmpList==null){
                tmpList=new ArrayList<VehicleBrandTypeDto>();
            }
            tmpList.add(typeDto);
            map.put(key,tmpList);
        }
        for(VehicleBrandDto dto:list){
            dto.setList(map.get(dto.getId()));
        }
        return success(list);
    }

    /**
     * 查询某个车辆品牌下所有车型信息
     * @return 所有某个品牌下所有车型信息
     */
    @RequestMapping(value="/queryVehicleTypeByBrandId",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询某个车辆品牌下所有车型信息", notes = "查询某个车辆品牌下所有车型信息", httpMethod = "GET" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="brandId",value="品牌id",dataType="int", paramType = "query")
    })
    public Result queryVehicleTypeByBrandId(HttpServletResponse response,VehicleBrandTypeDto param){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>queryVehicleTypeByBrandId ！！！all param:"+param);
        if(regUtil.isNull(param.getBrandId())){
            return error(ResultMsg.IS_NULL);
        }
        List<VehicleBrandTypeDto> list = userCenterManageService.queryVehicleBrandByBrandId(param);//所有车辆品牌信息
        return success(list);
    }

    /**
     * 用户绑定新车辆
     * @param param 车辆数据
     */
    @RequestMapping(value="/bindSelfVehicle",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="用户绑定新车辆", notes = "用户绑定新车辆", httpMethod = "POST" )
    @ApiImplicitParams({
        @ApiImplicitParam(name="carName",value="车辆名称",dataType="String", paramType = "query"),
        @ApiImplicitParam(name="carImg",value="车辆图片",dataType="String", paramType = "query"),
        @ApiImplicitParam(name="carBrandId",value="车辆型号id",dataType="String", paramType = "query"),
        @ApiImplicitParam(name="brandName",value="车辆品牌名称",dataType="String", paramType = "query"),
        @ApiImplicitParam(name="brandTypeName",value="车辆品牌下型号名称",dataType="String", paramType = "query")
    })
    public Result bindSelfVehicle(HttpServletResponse response,HttpServletRequest request,@RequestBody TbVehicleInfoEntity param){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>bindSelfVehicle ！！！all param:"+param);
        //数据校验
        if(regUtil.isNull(param.getBrandName(),param.getBrandTypeName(),param.getCarBrandId(),param.getCarImg(),param.getCarName())){//参数为空
            return error(ResultMsg.IS_NULL);
        }
        MemberView userDto=getRedisUserInfo(request,false);
        VehicleInfoDto vDto=new VehicleInfoDto();
        vDto.setMemberId(userDto.getId());
        List<VehicleInfoDto> vlist = userCenterManageService.queryAllBindVehicleInfo(vDto);//查询用户所有车辆  如果是第一辆设置为默认绑定车辆
        param.setMemberId(userDto.getMemberId());
        param.setCreateUser(userDto.getLoginname());
        param.setCreateTime(new Timestamp(System.currentTimeMillis()));
        param.setBaseUrl(StringUtils.getFilePath());
        param.setStatus("1");
        param.setIsDefault(vlist!=null&&vlist.size()>0?"0":"1");
        userCenterManageService.bindSelfVehicle(param);//所有车辆品牌信息
        return success();
    }

    /**
     * 删除用户车辆信息
     * @param dto 删除的车辆id
     */
    @ApiOperation(value="删除用户车辆信息", notes = "删除用户车辆信息", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="车辆信息id",dataType="String", paramType = "query")
    })
    @RequestMapping(value="/deleteUserVehicleInfo",method = RequestMethod.POST)
    public Result deleteUserVehicleInfo(HttpServletResponse response,HttpServletRequest request,@RequestBody VehicleInfoDto dto){
        log.info(">>>>>>>>>>>>>>>>>deleteUserVehicleInfo  param  dto:"+dto);
        if(regUtil.isNull(dto.getId())){
            return error(ResultMsg.IS_NULL);
        }
        MemberView userDto=getRedisUserInfo(request,false);
        dto.setMemberId(userDto.getId());
        userCenterManageService.deleteUserVehicleInfo(dto);
        return success();
    }

    /**
     * 用户默认勾选意见反馈
     */
    @RequestMapping(value="/getUserFeedback",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="用户默认勾选意见反馈", notes = "用户默认勾选意见反馈", httpMethod = "GET" )
    public Result getUserFeedback(HttpServletResponse response,String code){
        if(regUtil.isNull(code)){
            code="100";
        }
        //调用数据字典数据
        Object obj = userApi.GetDictZtree(code);
        return success(obj);
    }

    /**
     * 用户绑定手机
     */
    @RequestMapping(value="/userBindMobile",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="用户绑定手机", notes = "用户绑定手机", httpMethod = "GET" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="mobile",value="手机号",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="pwd",value="密码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="validCode",value="验证码",dataType="string", paramType = "query")
    })
    public Result userBindMobile(HttpServletResponse response,HttpServletRequest request,@RequestBody Map<String,String> map){
        String mobile=map.get("mobile"),validCode=map.get("validCode"),pwd=map.get("pwd");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>userBindMobile ！！！all moblie:"+mobile,"=====validCode:"+validCode+"=========pwd:"+pwd);
        if(regUtil.isNull(mobile,validCode,pwd)){//参数错误
            return error(ResultMsg.IS_NULL);
        }
        if(!regUtil.isMobile(mobile)){//手机号格式错误
            return error(ResultMsg.MOBILE_FORMAT_ERROR);
        }
        if(!regUtil.isPwdLegal(pwd)){//密码格式错误
            return error(ResultMsg.REGISTER_PWDILLEGALITY_ERROR);
        }
        //验证验证码是否正确 (0:成功  1:验证码失效或不存在  2:验证码错误)
        int result=validMobileCodeIsTrue(mobile,"4",validCode);
        switch (result) {
            case 1:
                return error(ResultMsg.MOBILECODE_NOTEXIST_ERROR);
            case 2:
                return error(ResultMsg.MOBILECODE_ERROR_ERROR);
            default:
                //调用用户服务 注册用户 注册成功获取返回用户信息
                MemberView userInfo = userApi.findMemberView(null, mobile, mobile,null);
                log.info(">>>>>>>>>>>>>>>userInfo:" + userInfo);
                if (userInfo != null) {//手机号已绑定
                    return error(ResultMsg.REGISTER_MOBILEEXIST_ERROR);
                }
                String uinq=UniqId.getRandomPwd(6);
                //调用用户服务 修改用户手机号信息
                userInfo=getRedisUserInfo(request,false);
                userInfo.setTel(mobile);
                userInfo.setLoginname(mobile);
                userInfo.setPassword(PasswordUtil.setPassWordBySalt(pwd,uinq));
                userInfo.setSalt(uinq);
                log.info(">>>>>>>>>>>>>>>userInfo:"+userInfo);
                userApi.updateMemberView(userInfo);
                //修改成功后改变redis数据
                saveRedisUserInfo(request,userInfo);
                return success();
        }
        /*String sessionId=request;
        MemberView userDto=getRedisUserInfo(sessionId,false);*/
    }

    /**
     * 修改手机验证原手机号
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value="/validOldMobile",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="修改手机验证原手机号", notes = "修改手机验证原手机号", httpMethod = "GET" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="mobile",value="手机号",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="validCode",value="验证码",dataType="string", paramType = "query")
    })
    public Result validOldMobile(HttpServletResponse response,HttpServletRequest request,@RequestBody Map<String,String> map){
        String mobile=map.get("mobile"),validCode=map.get("validCode");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>validOldMobile ！！！all moblie:"+mobile+"=====validCode:"+validCode+"=======");
        if(regUtil.isNull(mobile,validCode)){//参数错误
            return error(ResultMsg.IS_NULL);
        }
        if(!regUtil.isMobile(mobile)){//手机号格式错误
            return error(ResultMsg.MOBILE_FORMAT_ERROR);
        }

        //验证验证码是否正确 (0:成功  1:验证码失效或不存在  2:验证码错误)
        int result=validMobileCodeIsTrue(mobile,"5",validCode);
        switch (result) {
            case 1:
                return error(ResultMsg.MOBILECODE_NOTEXIST_ERROR);
            case 2:
                return error(ResultMsg.MOBILECODE_ERROR_ERROR);
            default:
                return success();
        }
    }

    /**
     * 修改绑定手机
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value="/updBindMobile",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="修改绑定手机", notes = "修改绑定手机", httpMethod = "GET" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="mobile",value="手机号",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="validCode",value="验证码",dataType="string", paramType = "query")
    })
    public Result updBindMobile(HttpServletResponse response,HttpServletRequest request,@RequestBody Map<String,String> map){
        String mobile=map.get("mobile"),validCode=map.get("validCode");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>updBindMobile ！！！all moblie:"+mobile,"=====validCode:"+validCode+"=======");
        if(regUtil.isNull(mobile,validCode)){//参数错误
            return error(ResultMsg.IS_NULL);
        }
        if(!regUtil.isMobile(mobile)){//手机号格式错误
            return error(ResultMsg.MOBILE_FORMAT_ERROR);
        }

        //验证验证码是否正确 (0:成功  1:验证码失效或不存在  2:验证码错误)
        int result=validMobileCodeIsTrue(mobile,"4",validCode);
        switch (result) {
            case 1:
                return error(ResultMsg.MOBILECODE_NOTEXIST_ERROR);
            case 2:
                return error(ResultMsg.MOBILECODE_ERROR_ERROR);
            default:
                //调用用户服务 注册用户 注册成功获取返回用户信息
                MemberView userInfo = userApi.findMemberView(null, mobile, mobile,null);
                if (userInfo != null) {//手机号已绑定
                    return error(ResultMsg.REGISTER_MOBILEEXIST_ERROR);
                }
                //调用用户服务 修改用户手机号信息
                userInfo=getRedisUserInfo(request,false);
                //调用用户服务 修改用户手机号信息
                userInfo.setTel(mobile);
                userInfo.setLoginname(mobile);
                log.info(">>>>>>>>>>>>>>>userInfo:"+userInfo);
                userApi.updateMemberView(userInfo);
                //修改成功后改变redis数据
                saveRedisUserInfo(request,userInfo);
                return success();
        }
    }

    /**
     * 用户修改密码
     */
    @RequestMapping(value="/userUpdPwd",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="用户修改密码", notes = "用户修改密码", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="pwd",value="用户密码",dataType="String", paramType  = "query"),
            @ApiImplicitParam(name="type",value="修改类型(1:设置密码 2:修改密码)",dataType="String", paramType  = "query"),
            @ApiImplicitParam(name="oldPwd",value="用户旧密码",dataType="String", paramType = "query")
    })
    public Result userUpdPwd(@RequestBody  Map<String,String> map,HttpServletResponse response,HttpServletRequest request){
        String pwd=map.get("pwd");
        String oldPwd=map.get("oldPwd");
        String type=map.get("type");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>userUpdPwd ！！！all pwd:"+pwd+"=======oldPwd:"+oldPwd+"=======type:"+type);
        MemberView userDto=getRedisUserInfo(request,false);
        if(regUtil.isNull(pwd,oldPwd)){//参数错误
            return error(ResultMsg.IS_NULL);
        }
        //消息
        TbUsreMessageEntity param=new TbUsreMessageEntity();
        if("1".equals(type)){//设置密码
            if(!pwd.equals(oldPwd)){//两次密码是否一致
                return error(ResultMsg.UPDPWD_TWODISFERENCE_ERROR);
            }
            param.setMsgContent("设置密码成功");
        }else{
            if(!userDto.getPassword().equals(PasswordUtil.setPassWordBySalt(oldPwd,userDto.getSalt()))){//旧密码错误
                return error(ResultMsg.FORGET_OLDPWD_ERROR);
            }
            param.setMsgContent("修改密码成功");
        }
        String uinq=UniqId.getRandomPwd(6);
        //调用用户服务 修改用户密码
        userDto.setPassword(PasswordUtil.setPassWordBySalt(pwd,uinq));
        userDto.setSalt(uinq);
        param.setMsgImg("");
        param.setMsgType("1");
        saveMsgEntity(param,userDto,2);
        return success();
    }
    /**
     * 浏览器微信绑定  回调
     */
    @RequestMapping(value="/userWechatBind",method = {RequestMethod.POST,RequestMethod.GET})
    public Result userWechatBind(HttpServletResponse response,HttpServletRequest request) throws Exception{
        String code=request.getParameter("code");
        WechatUserInfoDto dto1=wechatGetUserInfo(code,"web");
        //调用用户服务  保存至数据库
        MemberView userDto=getRedisUserInfo(request,false);
        MemberView userObj = wechatDtoChangeToMember(dto1,userDto);
        userApi.updateMemberView(userObj);//更新
        return success();
    }

    /**
     *查询用户是否认证和绑定车辆
     * @param request
     * @return
     */
    @ApiOperation(value="查询用户是否认证和绑定车辆", notes = "查询用户是否认证和绑定车辆", httpMethod = "POST" )
    @RequestMapping(value="/queryUserAutonymAndBindCar",method = {RequestMethod.POST,RequestMethod.GET})
    public Result queryUserAutonymAndBindCar(HttpServletRequest request){
        MemberView userDto=getRedisUserInfo(request,false);
        VehicleInfoDto vDto=new VehicleInfoDto();
        vDto.setMemberId(userDto.getId());
        List<VehicleInfoDto> vlist = userCenterManageService.queryAllBindVehicleInfo(vDto);//查询用户所有车辆  如果是第一辆设置为默认绑定车辆
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("isattestation",userDto.getIsattestation());
        map.put("bindVehicle",vlist!=null&&vlist.size()>0?vlist.get(0):null);
        return success(map);
    }

    /**
     *用户实名
     * @param request
     * @return
     */
    @ApiOperation(value="用户实名", notes = "用户实名", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="identityNumber",value="身份证号",dataType="String", paramType  = "query"),
            @ApiImplicitParam(name="realName",value="真实姓名",dataType="String", paramType = "query")
    })
    @RequestMapping(value="/userAutonym",method = {RequestMethod.POST,RequestMethod.GET})
    public Result userAutonym(HttpServletRequest request,@RequestBody Map<String,String> map){
        String identityNumber=map.get("identityNumber");
        String realName=map.get("realName");
        log.info(">>>>>>>>>>>>>>>>>>userAutonym  .request param  identityNumber:"+identityNumber+"=========realName:"+realName);
        if(regUtil.isNull(identityNumber,realName)){
            return error(ResultMsg.IS_NULL);
        }
        if(!regUtil.isIDCrad(identityNumber)){//校验身份证号码
            return error(ResultMsg.IDENTITY_FORMAT_ERROR);
        }
        MemberView userDto=getRedisUserInfo(request,false);
        if("1".equals(userDto.getIsattestation())){//用户已认证
            return error(ResultMsg.USER_VERIFIED_ERROR);
        }
        //调用实名
        String jsonStr = regUtil.validRealName(identityNumber, realName);//实名认证结果
        log.info(">>>>>>>>>>>>>>.>>>validRealName result:"+jsonStr);
        JSONObject jObj=JSONObject.parseObject(jsonStr);
        String error_code=jObj.get("error_code").toString();
        String msg=jObj.get("reason").toString();
        if(!"0".equals(error_code)){//错误代码
            return error(error_code,msg);
        }
        //入库
        userDto.setIdentityCard(identityNumber);
        userDto.setRealName(realName);
        userDto.setIsattestation("1");
        MemberView result = userApi.updateMemberView(userDto);//更新
        log.info(">>>>>>>>>>>>>>>>>>>>>>updateMemberView result:"+result);
        //保存redis
        saveRedisUserInfo(request,userDto);
        return success(userDto);
    }

    /**
     *用户提交意见反馈
     * @param request
     * @return
     */
    @ApiOperation(value="用户提交意见反馈", notes = "用户提交意见反馈", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="suggestionId",value="选择的建议id",dataType="String", paramType  = "query",required = false),
            @ApiImplicitParam(name="remake",value="备注建议(其他建议)",dataType="String", paramType = "query",required = false)
    })
    @RequestMapping(value="/userSubmitSuggest",method = {RequestMethod.POST,RequestMethod.GET})
    public Result userSubmitSuggest(HttpServletRequest request,@RequestBody TbUserFeedbackEntity entity){
        log.info(">>>>>>>>>>>>>>>>>>userSubmitSuggest  .request param  entity:"+entity);
        if(regUtil.isNull(entity.getRemake())&&regUtil.isNull(entity.getSuggestionId())){
            return error(ResultMsg.IS_NULL);
        }
        //入库
        MemberView userDto=getRedisUserInfo(request,false);
        entity.setMemberId(userDto.getId());
        userCenterManageService.addUserFeedback(entity);//新增
        return success();
    }

    /**
     *用户分享接口验证数据
     * @param request
     * @return
     */
    @ApiOperation(value="用户分享接口验证数据", notes = "用户分享接口验证数据", httpMethod = "POST" )
    @RequestMapping(value="/userShareConfig",method = {RequestMethod.POST,RequestMethod.GET})
    public Result userShareConfig(HttpServletRequest request,@RequestBody Map<String,String> map) throws Exception{
        String url=map.get("url");
        log.info(">>>>>>>>>>>>>>>>>>userShareConfig  .request param  url:"+url);
        Map<String, Object> resultMap = validJSSDK(URLDecoder.decode(url,"UTF-8"));
        return success(resultMap);
    }

    /**
     *用户登陆退出
     * @param request
     * @return
     */
    @ApiOperation(value="用户登陆退出", notes = "用户登陆退出", httpMethod = "POST" )
    @RequestMapping(value="/exitLogin",method = {RequestMethod.POST,RequestMethod.GET})
    public Result exitLogin(HttpServletRequest request) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>exitLogin  .request param");
        clearCacheData(request);
        return success();
    }


   /* *//**
     * 删除印信息
     * @param id 删除的水印id
     *//*
    @RequestMapping(value="/deleteWaterMarkInfo",method = RequestMethod.POST)
    public Result deleteWaterMarkInfo(String id){
        return success();
    }*/

    private static final String baseUploadUrl="/waterMakerImg";

}
