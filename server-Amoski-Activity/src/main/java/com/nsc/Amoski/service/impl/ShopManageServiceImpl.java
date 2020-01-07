package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.ShopManageDao;
import com.nsc.Amoski.dao.WaterMarkManageDao;
import com.nsc.Amoski.dto.ProductAttributeDto;
import com.nsc.Amoski.dto.ProductInfoDto;
import com.nsc.Amoski.dto.WaterMakeInfoDto;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TbProductCategory;
import com.nsc.Amoski.entity.TbProductInfo;
import com.nsc.Amoski.entity.TbWaterMakeInfo;
import com.nsc.Amoski.service.ShopManageService;
import com.nsc.Amoski.service.WaterMarkManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class ShopManageServiceImpl implements ShopManageService {

    @Autowired
    ShopManageDao shopManageDao;

    /**
     * 查询所有产品信息
     * @param param 查询条件 查询所有产品
     * @return  所有产品信息
     */
    public PagingBean queryProductInfo(ProductInfoDto param){
        return shopManageDao.queryProductInfo(param);
    }

    /**
     * 修改商品类别信息
     * @param info  修改商品类别信息
     */
    public void updateProductCategoryInfo(TbProductCategory info){
        shopManageDao.updateProductCategoryInfo(info);
    }

    /**
     * 新增商品类别信息
     * @param info  新增商品类别信息
     */
    public void addProductCategoryInfo(TbProductCategory info){
        shopManageDao.addProductCategoryInfo(info);
    }

    /**
     * 查询商品属性
     * @param info  查询商品属性
     */
    public List<ProductAttributeDto> queryProductAttribute(ProductAttributeDto info){
        return shopManageDao.queryProductAttribute(info);
    }

    /**
     * 更新产品信息
     * @param info  要更新的产品信息
     */
    public void updateProductInfo(TbProductInfo info){
        shopManageDao.updateProductInfo(info);
    }

    /**
     * 新增产品信息
     * @param info  要新增的产品信息
     */
    public void addProductInfo(TbProductInfo info){
        shopManageDao.addProductInfo(info);
    }
}








































