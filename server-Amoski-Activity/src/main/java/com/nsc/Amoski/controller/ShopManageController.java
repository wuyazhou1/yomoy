package com.nsc.Amoski.controller;

import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ShopManageController extends ActivityServerBaseController<ShopManageController> {

    /**
     * 查询所有商品种类
     * @return 所有商品种类
     */
    @RequestMapping(value="/queryProductCategory",method = {RequestMethod.POST,RequestMethod.GET})
    public Result queryProductCategory(){
        //PagingBean data = shopManageService.queryProductInfo(param);
        return success();
    }

    /**
     * 修改商品类别信息
     * @param info 修改的商品类别信息
     */
    @RequestMapping(value="/updateProductCategoryInfo",method = RequestMethod.POST)
    public Result updateProductCategoryInfo(TbProductCategory info){
        log.info("updateProductCategoryInfo  entity >>>>>>>>>>>param:"+info);
        //shopManageService.updateProductInfo(info);
        return success();
    }

    /**
     * 新增商品类别信息
     * @param info 新增商品类别信息
     */
    @RequestMapping(value="/addProductCategoryInfo",method = RequestMethod.POST)
    public Result addProductCategoryInfo(TbProductCategory info){
        info.setCreateTime(new Timestamp(System.currentTimeMillis()));
        info.setCreateUser("admin");
        log.info("sava entity >>>>>>>>>>>entity:"+info);
        //shopManageService.addProductInfo(info);//新增
        return success();
    }

    /**
     * 查询所有产品信息
     * @return 所有商品信息
     */
    @RequestMapping(value="/queryProductInfo",method = {RequestMethod.POST,RequestMethod.GET})
    public Result queryProductInfo(ProductInfoDto param){
        log.info(">>>>>>>>>>>>..request param:"+param.toString());
        PagingBean data = shopManageService.queryProductInfo(param);
        return success(data);
    }

    /**
     * 修改产品信息
     * @param info 修改产品信息
     */
    @RequestMapping(value="/updateProductInfo",method = RequestMethod.POST)
    public Result updateProductInfo(TbProductInfo info){
        info.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        info.setUpdateUser("admin");
        log.info("update entity >>>>>>>>>>>entity:"+info);
        shopManageService.updateProductInfo(info);
        return success();
    }

    /**
     * 新增产品信息
     * @param info 新增产品信息
     */
    @RequestMapping(value="/addProductInfo",method = RequestMethod.POST)
    public Result addProductInfo(TbProductInfo info){
        info.setCreateTime(new Timestamp(System.currentTimeMillis()));
        info.setCreateUser("admin");
        log.info("sava entity >>>>>>>>>>>entity:"+info);
        shopManageService.addProductInfo(info);//新增
        return success();
    }

    /**
     * 上传商品图片
     * @return
     */
    @RequestMapping("/uploadProductFile")
    public Result uploadProductFile(MultipartFile file, HttpServletRequest request) throws Exception{
        String baseUrl =request.getServletContext().getRealPath("/upload");
        String uploadUrl= File.separator+"Activity"+File.separator+"product";
        Result result = uploadFile(file, baseUrl, uploadUrl,0);//上传文件
        return result;
    }

    /**
     * 查询所有商品属性
     * @return 查询所有商品属性
     */
    @RequestMapping(value="/queryProductAttribute",method = {RequestMethod.POST,RequestMethod.GET})
    public Result queryProductAttribute(ProductAttributeDto param){
        log.info(">>>>>>>>>>>>..queryProductAttribute param:"+param.toString());
        List<ProductAttributeDto> list=shopManageService.queryProductAttribute(param);
        return success(list);
    }

    /**
     * 修改商品属性
     * @param info 修改商品属性
     */
    @RequestMapping(value="/updateProductAttribute",method = RequestMethod.POST)
    public Result updateProductAttribute(TbProductAttribute info){
        log.info("updateProductAttribute entity >>>>>>>>>>>entity:"+info);
        if(regUtil.isNull(info.getId())){
            return error(ResultMsg.IS_NULL);
        }
        userCenterManageService.updateEntity(info);
        return success();
    }

    /**
     * 新增商品属性
     * @param info 新增商品属性
     */
    @RequestMapping(value="/addProductAttribute",method = RequestMethod.POST)
    public Result addProductAttribute(TbProductAttribute info){
        info.setCreateTime(new Timestamp(System.currentTimeMillis()));
        info.setCreateUser("admin");
        log.info("addProductAttribute entity >>>>>>>>>>>entity:"+info);
        userCenterManageService.addEntity(info);
        return success();
    }







    /**
     *查询品牌数据
     * @param request
     * @return
     */
    @ApiOperation(value="查询品牌数据", notes = "查询品牌数据", httpMethod = "POST" )
    @RequestMapping(value="/queryBrandInfo",method = {RequestMethod.POST,RequestMethod.GET})
    public Result queryBrandInfo(HttpServletRequest request) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>queryBrandInfo  .request param ");
        List<VehicleBrandDto> brandList = userCenterManageService.queryAllBindVehicleBrandInfo();//所有车辆品牌信息
        return success(brandList);
    }

    /**
     *新增品牌数据
     * @param request
     * @return
     */
    @ApiOperation(value="新增品牌数据", notes = "新增品牌数据", httpMethod = "POST" )
    @RequestMapping(value="/addBrandInfo",method = {RequestMethod.POST,RequestMethod.GET})
    public Result addBrandInfo(HttpServletRequest request,TbVehicleBrandEntity entity) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>addBrandInfo  .request param  entity:"+entity);
        //数据校验
        if(regUtil.isNull(entity.getBrandIco()))

        entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        entity.setCreateUser("system");
        userCenterManageService.addEntity(entity);
        return success();
    }

    /**
     *修改品牌数据
     * @param request
     * @return
     */
    @ApiOperation(value="修改品牌数据", notes = "修改品牌数据", httpMethod = "POST" )
    @RequestMapping(value="/updateBrandInfo",method = {RequestMethod.POST,RequestMethod.GET})
    public Result updateBrandInfo(HttpServletRequest request,TbVehicleBrandEntity entity) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>updateBrandInfo  .request param  entity:"+entity);
        //数据校验
        if(regUtil.isNull(entity.getId())){
            return error(ResultMsg.IS_NULL);
        }
        //查询数据库数据 如果能查询到  则做更新
        TbVehicleBrandEntity newEntity = userCenterManageService.queryEntity(new TbVehicleBrandEntity(), entity.getId() + "");
        if(newEntity!=null){//修改
            newEntity.setBrandIco("");

            userCenterManageService.addEntity(newEntity);
        }else{
            return error(ResultMsg.IS_NULL);
        }
        return success();
    }



    /**
     *查询品牌系列数据
     * @param request
     * @return
     */
    @ApiOperation(value="查询品牌系列数据", notes = "查询品牌系列数据", httpMethod = "POST" )
    @RequestMapping(value="/queryBrandSeries",method = {RequestMethod.POST,RequestMethod.GET})
    public Result queryBrandSeries(HttpServletRequest request,BrandSeriesDto dto) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>queryBrandSeries  .request param  dto:"+dto);
        List<TbBrandSeries> list = userCenterManageService.queryBrandSeries(dto);
        return success(list);
    }

    /**
     *新增品牌系列数据
     * @param request
     * @return
     */
    @ApiOperation(value="新增品牌系列数据", notes = "新增品牌系列数据", httpMethod = "POST" )
    @RequestMapping(value="/addBrandSeries",method = {RequestMethod.POST,RequestMethod.GET})
    public Result addBrandSeries(HttpServletRequest request,TbBrandSeries entity) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>addBrandSeries  .request param  entity:"+entity);
        //数据校验

        entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        entity.setCreateUser("system");
        userCenterManageService.addEntity(entity);
        return success();
    }

    /**
     *修改品牌系列数据
     * @param request
     * @return
     */
    @ApiOperation(value="修改品牌系列数据", notes = "修改品牌系列数据", httpMethod = "POST" )
    @RequestMapping(value="/updateBrandSeries",method = {RequestMethod.POST,RequestMethod.GET})
    public Result updateBrandSeries(HttpServletRequest request,TbBrandSeries entity) throws Exception{
        log.info(">>>>>>>>>>>>>>>>>>updateBrandSeries  .request param  entity:"+entity);
        //数据校验
        if(regUtil.isNull(entity.getId())){
            return error(ResultMsg.IS_NULL);
        }
        //查询数据库数据 如果能查询到  则做更新
        TbBrandSeries newEntity = userCenterManageService.queryEntity(new TbBrandSeries(), entity.getId() + "");
        if(newEntity!=null){//修改

            userCenterManageService.addEntity(newEntity);
        }else{
            return error(ResultMsg.IS_NULL);
        }

        userCenterManageService.addEntity(entity);
        return success();
    }
}
