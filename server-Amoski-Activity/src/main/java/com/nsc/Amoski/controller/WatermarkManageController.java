package com.nsc.Amoski.controller;

import com.nsc.Amoski.dto.WaterMakeInfoDto;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TbWaterMakeInfo;
import com.nsc.Amoski.util.HttpUtil;
import com.nsc.Amoski.util.StringUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/watermarkManage")
public class WatermarkManageController extends ActivityServerBaseController<WatermarkManageController> {

    /**
     * 水印管理-查询登陆用户所在部门以及子部门
     * @return 所有部门信息
     */
    @RequestMapping(value="/queryUserDeptInfo",method = {RequestMethod.POST,RequestMethod.GET})
    public Object queryUserDeptInfo(){
        /*ShiroUser requestShiro = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        log.info(">>>>>>>>>>>>>queryUserDeptInfo  requestShiro:"+requestShiro);*/
        Object obj= userApi.queryUserDeptInfo("0","0");//查询部门数据
        return obj;
    }

    /**
     * 根据部门id查询所有的水印(包括下级部门)
     * @param param 查询参数
     * @return  所有水印信息
     */
    @RequestMapping(value="/queryWaterMakeByDept",method = {RequestMethod.POST,RequestMethod.GET})
    public Result queryWaterMakeByDept(WaterMakeInfoDto param){
        List<Map<String,Object>> returnMap = (List<Map<String,Object>>)userApi.queryUserDeptInfo(param.getDeptId(),"0");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>all deptInfo:"+returnMap.toString());
        PagingBean page=null;
        if(returnMap!=null){
            String deptIds="";
            for (Map<String,Object> map :returnMap) {
                String id=map.get("id").toString();
                deptIds+=map.get("id")+",";
            }
            param.setDeptId(deptIds);
            page = waterMarkManageService.queryWaterMakeByDept(param);
        }
        return success(page);
    }

    /**
     * 更新水印信息
     * @param param 更新的水印数据
     */
    @RequestMapping(value="/updateWaterMarkInfo",method = RequestMethod.POST)
    public Result updateWaterMarkInfo(TbWaterMakeInfo param) throws Exception{
        log.info("update updateWaterMarkInfo >>>>>>>>>>>entity:"+param);
        //获取登陆用户信息
        /*param.setCreateTime(new Timestamp(System.currentTimeMillis()));
        param.setCreateUser("admin");*/
        TbWaterMakeInfo tbWaterMakeInfo = userCenterManageService.queryEntity(new TbWaterMakeInfo(), param.getId().toString());
        tbWaterMakeInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        tbWaterMakeInfo.setUpdateUser("admin1");
        tbWaterMakeInfo.setRemake(param.getRemake());
        tbWaterMakeInfo.setSmallImgUrl(param.getSmallImgUrl());
        tbWaterMakeInfo.setImgBaseUrl(param.getImgBaseUrl());
        tbWaterMakeInfo.setImgUrl(param.getImgUrl());
        tbWaterMakeInfo.setStatus(param.getStatus());
        tbWaterMakeInfo.setType(param.getType());
        log.info("update entity >>>>>>>>>>>entity:"+tbWaterMakeInfo);
        waterMarkManageService.updateWaterMarkInfo(tbWaterMakeInfo);//更新
        return success();
    }

    /**
     * 新增水印信息
     * @param param 新增的水印数据
     */
    @RequestMapping(value="/addWaterMarkInfo",method = RequestMethod.POST)
    public Result addWaterMarkInfo(TbWaterMakeInfo param){
        //获取登陆用户信息
        param.setCreateTime(new Timestamp(System.currentTimeMillis()));
        param.setCreateUser("admin");
        param.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        param.setUpdateUser("admin");
        log.info("sava entity >>>>>>>>>>>entity:"+param);
        waterMarkManageService.saveWaterMarkInfo(param);//新增
        return success();
    }

    /**
     * 删除印信息
     * @param id 删除的水印id
     */
    @RequestMapping(value="/deleteWaterMarkInfo",method = RequestMethod.POST)
    public Result deleteWaterMarkInfo(String id){
        return success();
    }

    /**
     * 上传水印图片
     * @return
     */
    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file, HttpServletRequest request) throws Exception{
        String baseUrl = StringUtils.getFilePath() + File.separator+"Activity";//request.getServletContext().getRealPath("/upload");
        String uploadUrl=File.separator+"Activity"+File.separator+"waterMakerImg";
        Result result = uploadFile(file, baseUrl, uploadUrl,0);//上传文件
        return result;
    }

    /**
     * app图片指向地址
     */
    @RequestMapping(value="/getImg",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value="app图片指向地址", notes = "app图片指向地址", httpMethod = "GET" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="imgUrl",value="图片路径",dataType="String", paramType = "query")
    })
    public void getImg(HttpServletResponse response, HttpServletRequest request, String imgUrl) throws Exception{
        log.info(">>>>>>>>>>>>>>>getImg....url:"+imgUrl);
        /*if(regUtil.isNull(imgUrl)){

        }*/
        InputStream in =regUtil.getFileStream(imgUrl, null);
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

}
