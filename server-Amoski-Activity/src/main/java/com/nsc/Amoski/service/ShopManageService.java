package com.nsc.Amoski.service;


import com.nsc.Amoski.dto.ProductAttributeDto;
import com.nsc.Amoski.dto.ProductInfoDto;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TbProductCategory;
import com.nsc.Amoski.entity.TbProductInfo;

import java.util.List;

public interface ShopManageService {
    /**
     * 查询所有产品信息
     * @param param 查询条件 查询所有产品
     * @return  所有产品信息
     */
    public PagingBean queryProductInfo(ProductInfoDto param);

    /**
     * 修改商品类别信息
     * @param info  修改商品类别信息
     */
    public void updateProductCategoryInfo(TbProductCategory info);

    /**
     * 新增商品类别信息
     * @param info  新增商品类别信息
     */
    public void addProductCategoryInfo(TbProductCategory info);

    /**
     * 查询商品属性
     * @param info  查询商品属性
     */
    public List<ProductAttributeDto> queryProductAttribute(ProductAttributeDto info);

    /**
     * 更新产品信息
     * @param info  要更新的产品信息
     */
    public void updateProductInfo(TbProductInfo info);

    /**
     * 新增产品信息
     * @param info  要新增的产品信息
     */
    public void addProductInfo(TbProductInfo info);
}