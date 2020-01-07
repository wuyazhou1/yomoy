package com.nsc.AmoskiActivity.Controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.util.BaseController;
import com.nsc.Amoski.util.ImgCompress;
import com.nsc.AmoskiActivity.Service.ActivityCreateService;
import com.nsc.AmoskiActivity.Service.CalendarImagesService;
import com.nsc.AmoskiActivity.Util.CreateActivityAnalysisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.nsc.Amoski.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RequestMapping(value="ActivityCreate")
@Api(value="ActivityCreate",description = "活动创建模块")
@Controller
public class ActivityCreateController  extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ActivityCreateController.class);

    public static String[] entityPath = new String[]{
            "com.nsc.Amoski.entity.TbActivityBasicsEntity",
            "com.nsc.Amoski.entity.TbActivityBillImageEntity",
            "com.nsc.Amoski.entity.TbActivityHotelEntity",
            "com.nsc.Amoski.entity.TbActivityHotelRoomEntity",
            "com.nsc.Amoski.entity.TbActivityOrdinationEntity",
            "com.nsc.Amoski.entity.TbActivityRoomImageEntity",
            "com.nsc.Amoski.entity.TbActivityRouteEntity",
            "com.nsc.Amoski.entity.TbActivityRouteImageEntity",
            "com.nsc.Amoski.entity.TbActivityScheduleEntity",
            //"com.nsc.Amoski.entity.TbActivitySignUpEntity",
            "com.nsc.Amoski.entity.TbActivitySynopsisEntity",
            "com.nsc.Amoski.entity.TbActivityTimeHistoryEntity",
            "com.nsc.Amoski.entity.TbCtivityInvoiceEntity",
            "com.nsc.Amoski.entity.TbHotelRestaurantEntity",
            "com.nsc.Amoski.entity.TbHotelRestaurantImageEntity",
            //"com.nsc.Amoski.entity.TbPeoplePutUpEntity",
            //"com.nsc.Amoski.entity.TbPeopleReceiveSendEntity",
            //"com.nsc.Amoski.entity.TbSignUpPeopleEntity"
    };
    public List<String> list = new ArrayList<String>(){{
        this.add("Tb_Activity_Basics where id= :id");
        this.add("Tb_Activity_Bill_Image where basics_id= :id");
        this.add("Tb_Activity_Hotel where basics_id= :id");
        this.add("Tb_Activity_Hotel_Room where basics_id= :id");
        this.add("Tb_Activity_Ordination where basics_id= :id");
        this.add("Tb_Activity_Room_Image where basics_id= :id");
        this.add("Tb_Activity_Route where basics_id= :id");
        this.add("Tb_Activity_Route_Image where basics_id= :id");
        this.add("Tb_Activity_Schedule where basics_id= :id");
        this.add("Tb_Activity_Synopsis where basics_id= :id");
        this.add("Tb_Activity_Time_History where basics_id= :id");
        this.add("Tb_Ctivity_Invoice where basics_id= :id");
        this.add("Tb_Hotel_Restaurant where basics_id= :id");
        this.add("Tb_Hotel_Restaurant_Image where basics_id= :id");
    }};


    @Autowired
    @Lazy
    private ActivityCreateService activityCreateService;

    @ResponseBody
    @RequestMapping(value = "QueryActivityList",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询活动集合接口", notes = "查询活动集合接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="state",value="状态",dataType="string", paramType = "string"),
            @ApiImplicitParam(name="title",value="标题",dataType="string", paramType = "string"),
    })
    public Object QueryActivityList(HttpServletRequest request,HttpServletResponse response) throws Exception {
        long starTime = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<String, Object>();
        //getHeaderParent(request);
        params= pageInfoVue(request);
        params.put("state",request.getParameter("state"));//状态
        params.put("title",request.getParameter("title"));//标题
        PagingBean pagingBean = activityCreateService.QueryActivityList(params);
        long endTime = System.currentTimeMillis();
        log.info("访问ActivityCreateController==》QueryActivityList"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return pagingBean;
    }

    @ResponseBody
    @RequestMapping(value = "deleteActivityList",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="删除活动集合接口", notes = "删除活动集合接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="id",dataType="string", paramType = "string"),
    })
    public Object deleteActivityList(HttpServletRequest request,HttpServletResponse response) throws Exception {
        long starTime = System.currentTimeMillis();
        //getHeaderParent(request);
        try {
            activityCreateService.deleteActivity(request.getParameter("id"));
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        long endTime = System.currentTimeMillis();
        log.info("访问ActivityCreateController==》deleteActivityList"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }



    @ResponseBody
    @RequestMapping(value = "QueryActivity",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询活动接口", notes = "查询活动接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="参数",dataType="string", paramType = "String"),
    })
    public Object QueryActivity(HttpServletRequest request,HttpServletResponse response) throws Exception {
        long starTime = System.currentTimeMillis();
        //getHeaderParent(request);
        Map<String,Object> list=activityCreateService.QueryActivity(request.getParameter("id"));
        long endTime = System.currentTimeMillis();
        log.info("访问ActivityCreateController==》QueryActivity"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return list;
    }

    @ResponseBody
    @RequestMapping(value = "SaveActivity",method = RequestMethod.POST)
    @ApiOperation(value="创建活动接口", notes = "创建活动接口", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="activity",value="活动json字符串",dataType="string", paramType = "insert"),
    })
    public Object SaveActivity(HttpServletRequest request,HttpServletResponse response) throws Exception {
        long starTime = System.currentTimeMillis();
        try {
            String activity1 = request.getParameter("activity");
            log.info("activity1"+activity1);
            JSONArray activity = JSON.parseArray(activity1);
            activityCreateService.saveActivity(activity);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        long endTime = System.currentTimeMillis();
        log.info("访问ActivityCreateController==》SaveActivity"+((endTime-starTime)/1000)+"开始毫秒"+starTime);
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }








    @ResponseBody
    @RequestMapping(value = "QueryEntryMustCompleted",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="查询表字段", notes = "查询表字段", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="参数",dataType="string", paramType = "String"),
    })
    public Object QueryEntryMustCompleted(HttpServletRequest request,HttpServletResponse response) throws Exception {
        try {
            List list=activityCreateService.QueryEntryMustCompleted(request.getParameter("table"));
            return this.resultData(list);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
    }


    @ResponseBody
    @RequestMapping(value = "saveFileImage",method = RequestMethod.POST)
    @ApiOperation(value="保存文件", notes = "保存文件", httpMethod = "POST" )
    @ApiImplicitParams({
            /*@ApiImplicitParam(name="activity",value="活动json字符串",dataType="string", paramType = "insert"),*/
    })
    public Object saveFileImage(HttpServletRequest request,HttpServletResponse response){
        List<Map<String,Object>> resultList = new ArrayList<>();
        Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
        for(Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                Map<String,Object> result = new HashMap<>();
                Calendar calendar = Calendar.getInstance();
                UUID uuid=UUID.randomUUID();
                String path = "";
                if(System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1){
                    path = "D:/home/uploadFile/images/createActivity/"+ StringUtils.getYearStr(calendar) +"/"+StringUtils.getMonthStr(calendar)+"/"+StringUtils.getDayStr(calendar)+"/"+StringUtils.getHourStr(calendar);
                }else{
                    path = "/home/uploadFile/images/createActivity/"+ StringUtils.getYearStr(calendar) +"/"+StringUtils.getMonthStr(calendar)+"/"+StringUtils.getDayStr(calendar)+"/"+StringUtils.getHourStr(calendar);
                }
                MultipartFile file = entry.getValue();
                String fileName = "saveFileImage"+uuid.toString().replaceAll("-","")+"."+
                        file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                File filepath = new File(path);
                if (!filepath.exists())
                    filepath.mkdirs();
                boolean b = saveFile(file, path, fileName);
                if(!b){
                    return this.resultData(EnumEntity.ProjectUtil.操作失败);
                }
                result.put("projectUrl","AmoskiUser");//项目路径
                result.put("filePathUrl","/ActivityCreate/getFile");//方法路径
                result.put("fileNameUrl",path+"/"+fileName);//文件路径
                result.put("name",file.getOriginalFilename());
                resultList.add(result);
            }
        }
        return this.resultData(resultList);
    }


    @ResponseBody
    @RequestMapping(value = "saveFile",method = RequestMethod.POST)
    @ApiOperation(value="保存文件", notes = "保存文件", httpMethod = "POST" )
    @ApiImplicitParams({
            /*@ApiImplicitParam(name="activity",value="活动json字符串",dataType="string", paramType = "insert"),*/
    })
    public Object saveFile(HttpServletRequest request,HttpServletResponse response){
        List<Map<String,Object>> resultList = new ArrayList<>();
        Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
        for(Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                Map<String,Object> result = new HashMap<>();
                Calendar calendar = Calendar.getInstance();
                UUID uuid=UUID.randomUUID();
                String path = "";
                if(System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1){
                    path = "D:/home/uploadFile/images/createActivity/"+ StringUtils.getYearStr(calendar) +"/"+StringUtils.getMonthStr(calendar)+"/"+StringUtils.getDayStr(calendar)+"/"+StringUtils.getHourStr(calendar);
                }else{
                    path = "/home/uploadFile/images/createActivity/"+ StringUtils.getYearStr(calendar) +"/"+StringUtils.getMonthStr(calendar)+"/"+StringUtils.getDayStr(calendar)+"/"+StringUtils.getHourStr(calendar);
                }
                MultipartFile file = entry.getValue();
                //String[] split = file.getOriginalFilename().split(".");
                String fileName = "fileNameUrl"+uuid.toString().replaceAll("-","")+"."+
                        file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                File filepath = new File(path);
                if (!filepath.exists())
                    filepath.mkdirs();
                boolean b = saveFile(file, path, fileName);
                if(!b){
                    return this.resultData(EnumEntity.ProjectUtil.操作失败);
                }
                result.put("projectUrl","AmoskiUser");//项目路径
                result.put("filePathUrl","/ActivityCreate/getFile");//方法路径
                result.put("fileNameUrl",path+"/"+fileName);//文件路径
                result.put("name",file.getOriginalFilename());
                resultList.add(result);
            }
        }
        return this.resultData(resultList);
    }



    @ResponseBody
    @RequestMapping(value = "saveImgCompressFile",method = RequestMethod.POST)
    @ApiOperation(value="保存文件", notes = "保存文件", httpMethod = "POST" )
    @ApiImplicitParams({
    })
    public Object saveImgCompressFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> requestMapAll = getRequestMapAll(request);
        requestMapAll.putAll(getHeaderParent(request));


        List<ActivityCalendarImagesEntity> resultList = new ArrayList<>();
        Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        for(Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                ActivityCalendarImagesEntity activityCalendarImagesEntity = new ActivityCalendarImagesEntity();
                UUID uuid=UUID.randomUUID();
                Calendar calendar = Calendar.getInstance();
                String path = "";
                if(System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1){
                    path = "D:/home/uploadFile/images/createActivity/"+ StringUtils.getYearStr(calendar) +"/"+StringUtils.getMonthStr(calendar)+"/"+StringUtils.getDayStr(calendar)+"/"+StringUtils.getHourStr(calendar);
                }else{
                    path = "/home/uploadFile/images/createActivity/"+ StringUtils.getYearStr(calendar) +"/"+StringUtils.getMonthStr(calendar)+"/"+StringUtils.getDayStr(calendar)+"/"+StringUtils.getHourStr(calendar);
                }
                MultipartFile file = entry.getValue();
                String filename = file.getOriginalFilename();
                String houZhui = filename.substring(filename.lastIndexOf(".") + 1);
                String fileName = "fileNameUrl"+uuid.toString().replaceAll("-","")+"."+houZhui;
                String imgCompress = "imgCompress"+uuid.toString().replaceAll("-","")+"."+houZhui;
                ShiroUser suser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
                File filepath = new File(path);
                if (!filepath.exists())
                    filepath.mkdirs();
                try {
                    //压缩图
                    ImgCompress icp=new ImgCompress(file);
                    int width=150, height=80;
                    icp.resize(width,height,path+"/"+imgCompress);
                } catch (Exception e) {
                    e.printStackTrace();
                    return this.resultData(EnumEntity.ProjectUtil.操作失败);
                }
                boolean b = saveFile(file, path, fileName);
                if(!b){
                    return this.resultData(EnumEntity.ProjectUtil.操作失败);
                }
                activityCalendarImagesEntity.setState("1");
                activityCalendarImagesEntity.setShowFileName(request.getParameter("name"));//显示文件名
                activityCalendarImagesEntity.setOrgCode(suser.getOrgCode());//门店代码
                activityCalendarImagesEntity.setBasicsId(request.getParameter("basicsId"));
                activityCalendarImagesEntity.setScheduleId(request.getParameter("scheduleId"));
                activityCalendarImagesEntity.setState(request.getParameter("state"));
                if(request.getParameter("uploadTime")!=null){
                    activityCalendarImagesEntity.setUploadTime(format.parse(request.getParameter("uploadTime")));
                }
                activityCalendarImagesEntity.setCreateDate(new Date());
                activityCalendarImagesEntity.setProjectUrl("AmoskiUser");//项目路径
                activityCalendarImagesEntity.setFilePathUrl("/ActivityCreate/getFile");//方法路径
                activityCalendarImagesEntity.setFilePath(path);
                activityCalendarImagesEntity.setFileNameUrl(fileName);//存放文件名称
                activityCalendarImagesEntity.setImgCompress(imgCompress);//压缩文件名称
                activityCreateService.saveEntityObject(activityCalendarImagesEntity);
                resultList.add(activityCalendarImagesEntity);
            }
        }
        return this.resultData(resultList);
    }

    @ResponseBody
    @RequestMapping(value = "getFile",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="获取文件", notes = "获取文件", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="fileNameUrl",value="活动文件路径",dataType="string", paramType = "insert"),
    })
    public Object getFile(HttpServletRequest request,HttpServletResponse response){
        String fileNameUrl = request.getParameter("filePath");
        byte[] arr=null;
        try {
            File file = new File(fileNameUrl);
            InputStream fis = new FileInputStream(file);
            arr = new byte[fis.available()];
            fis.read(arr);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arr;
    }




    /***
     * 保存文件
     * @param file
     * @return
     */
    private boolean saveFile(MultipartFile file, String path,String FileName) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                // 文件保存路径
                //String savePath = path + file.getOriginalFilename();
                String savePath = path +"/"+ FileName;
                // 转存文件
                file.transferTo(new File(savePath));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Autowired
    private CalendarImagesService calendarImagesService;

    @ResponseBody
    @RequestMapping(value = "queryCalendarImagesList",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="获取线程图片表", notes = "获取线程图片表", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="uploadTime",value="拍摄之间",dataType="string", paramType = "insert"),
    })
    public Object queryCalendarImagesList(HttpServletRequest request,HttpServletResponse response){
        Map<String, Object> params = new HashMap<String, Object>();
        //getHeaderParent(request);
        params= pageInfoVue(request);
        params.put("uploadTime",request.getParameter("uploadTime"));//拍摄时间
        params.put("scheduleId",request.getParameter("scheduleId"));
        PagingBean pagingBean = calendarImagesService.queryCalendarImagesList(params);
        return pagingBean;
    }


    @ResponseBody
    @RequestMapping(value = "deleteCalendarImages",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="删除日程图片", notes = "删除日程图片", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value="日程图片id",dataType="string", paramType = "insert"),
    })
    public Object deleteCalendarImages(HttpServletRequest request,HttpServletResponse response){
        try {
            calendarImagesService.deleteCalendarImages(request.getParameter("id"));
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }




    @ResponseBody
    @RequestMapping(value = "updateCalendarImages",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="修改日程相册", notes = "修改日程相册", httpMethod = "POST" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="ActivityCalendarImagesList",value="日程图片id",dataType="string", paramType = "insert"),
    })
    public Object updateCalendarImages(HttpServletRequest request,HttpServletResponse response){
        try {
            String activity1 = request.getParameter("ActivityCalendarImagesList");
            String start = "";
            log.info(activity1);
            JSONArray activity = JSON.parseArray(activity1);
            List<ActivityCalendarImagesEntity> list = new ArrayList<>();
            for(int i=0;i<activity.size();i++){
                JSONObject jsonObject = activity.getJSONObject(i);
                ActivityCalendarImagesEntity tbActivityBasicsEntity = (ActivityCalendarImagesEntity) CreateActivityAnalysisUtil.ActivityAnalysisUtil(jsonObject,ActivityCalendarImagesEntity.class);//活动基础表
                list.add(tbActivityBasicsEntity);
            }
            calendarImagesService.updateCalendarImages(list);
        } catch (Exception e) {
            e.printStackTrace();
            return this.resultData(EnumEntity.ProjectUtil.操作失败);
        }
        return this.resultData(EnumEntity.ProjectUtil.操作成功);
    }



















    @ResponseBody
    @RequestMapping(value = "downloadZip",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="修改日程相册", notes = "修改日程相册", httpMethod = "POST" )
    @ApiImplicitParams({
    })
    public void downloadZipFiles(HttpServletResponse response) {
        List<String> srcFiles = new ArrayList<String>(){{
            this.add("D:/home/uploadFile/images/2019/07/23/18/fileNameUrl1563876476926.png");
            this.add("D:/home/uploadFile/images/2019/07/23/18/fileNameUrl1563876476928.png");
            this.add("D:/home/uploadFile/images/2019/07/23/18/fileNameUrl1563876476929.png");
            this.add("D:/home/uploadFile/images/2019/07/23/18/fileNameUrl1563876755846.png");
            this.add("D:/home/uploadFile/images/2019/07/23/18/fileNameUrl1563876755859.png");
            this.add("D:/home/uploadFile/images/2019/07/23/18/imgCompress1563876476926.png");
            this.add("D:/home/uploadFile/images/2019/07/23/18/imgCompress1563876476928.png");
            this.add("D:/home/uploadFile/images/2019/07/23/18/imgCompress1563876476929.png");
            this.add("D:/home/uploadFile/images/2019/07/23/18/imgCompress1563876755846.png");
            this.add("D:/home/uploadFile/images/2019/07/23/18/imgCompress1563876755859.png");
        }};
        String zipFileName="download.zip";
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
     * 压缩文件
     *
     * @param filePaths 需要压缩的文件路径集合
     * @throws IOException
     */
    private void zipFile(List<String> filePaths, ZipOutputStream zos) {
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
                        List<String> filePathsTem = new ArrayList<String>();
                        for (File fileTem : files) {
                            filePathsTem.add(fileTem.toString());
                        }
                        zipFile(filePathsTem, zos);
                    }
                }
            }
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












    public static void main(String[] args){
        //原始的   UUID码
        UUID uuid=UUID.randomUUID();
        String uuidStr = uuid.toString();
        String uuidRep=uuidStr.replaceAll("-", "");
        System.out.println("原始UUID："+uuid);
        System.out.println("转成字符后的UUID:"+uuidStr);
        System.out.println("去掉-的UUID字符串:"+uuidRep);
        System.out.println(uuid.toString().replaceAll("-",""));
    }

}
