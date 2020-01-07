package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.ShopManageDao;
import com.nsc.Amoski.dto.ProductAttributeDto;
import com.nsc.Amoski.dto.ProductInfoDto;
import com.nsc.Amoski.dto.VehicleInfoDto;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TbProductAttribute;
import com.nsc.Amoski.entity.TbProductCategory;
import com.nsc.Amoski.entity.TbProductInfo;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.RegUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ShopManageDaoImpl extends GenericRepositoryImpl implements ShopManageDao {

    /**
     * 查询所有产品信息
     * @param param 查询条件 查询所有产品
     * @return  所有产品信息
     */
    public PagingBean queryProductInfo(ProductInfoDto param){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder sql=new StringBuilder("select * from TB_PRODUCT_INFO p");
        StringBuilder countSql=new StringBuilder("select count(*) from TB_PRODUCT_INFO p");
        StringBuilder whereSql=new StringBuilder(" where 1=1 ");

        Map<String,Object> map=new HashMap<String, Object>();
        map.put("pageSize",param.getPageSize());
        map.put("start",param.getStart());
        //查询条件
        if(regUtil.isNull(param.getProductName())){//产品名称
            whereSql.append(" and p.product_name=:productName");
            map.put("productName",param.getProductName());
        }
        if(regUtil.isNull(param.getProductCategory())){//产品种类
            whereSql.append(" and p.product_category:productCategory");
            map.put("productCategory",param.getProductCategory());
        }
        if(regUtil.isNull(param.getStatus())){//商品状态（1.上架，0.下架)
            whereSql.append(" and p.status=:status");
            map.put("status",param.getStatus());
        }
        if(regUtil.isNull(param.getWherePrice())&&param.getWherePrice().indexOf("-")>0){//价格区间查询
            whereSql.append("p.product_price between :startPrice and :endPrice");

            String[] prices = param.getWherePrice().split("-");
            map.put("startPrice",prices[0]);
            map.put("endPrice",prices[1]);
        }

        String querySql=sql.append(whereSql).toString(),queryCountSql=countSql.append(whereSql).toString();
        querySql=querySql+" order by p.create_time desc";
        logger.info(">>>>queryProductInfo>>>>>queryCount sql:"+queryCountSql+">>>>>>>paramMap:"+map.toString());
        logger.info(">>>>queryProductInfo>>>>>query sql:"+querySql+">>>>>>>");
        int count= this.jdbcTemplate.queryForObject(queryCountSql,map,Integer.class);
        List<ProductInfoDto> list=this.jdbcTemplate.query(pageSql(querySql,map), map, new BeanPropertyRowMapper<ProductInfoDto>(ProductInfoDto.class));
        PagingBean bean=new PagingBean();
        bean.setData(list);
        bean.setRecordsTotal(count);
        bean.setRecordsFiltered(count);
        logger.info(">>>>queryProductInfo>>>>>where"+whereSql+">>>>>>>result:"+bean.toString());
        return bean;
    }

    /**
     * 修改商品类别信息
     * @param info  修改商品类别信息
     */
    public void updateProductCategoryInfo(TbProductCategory info){

    }

    /**
     * 新增商品类别信息
     * @param info  新增商品类别信息
     */
    public void addProductCategoryInfo(TbProductCategory info){

    }
    /**
     * 更新产品信息
     * @param info  要更新的产品信息
     */
    public void updateProductInfo(TbProductInfo info){
        this.update(info);
    }

    /**
     * 新增产品信息
     * @param info  要新增的产品信息
     */
    public void addProductInfo(TbProductInfo info){
        this.save(info);
    }


    /**
     * 查询商品属性
     * @param info  查询商品属性
     */
    public List<ProductAttributeDto> queryProductAttribute(ProductAttributeDto info){
        StringBuilder sql=new StringBuilder("select * from TB_PRODUCT_ATTRIBUTE i ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 ");
        Map<String,Object> map=new HashMap<String, Object>();
        //查询条件
        String querySql=sql.append(whereSql).toString();
        querySql=querySql+" order by i.create_time desc";
        logger.info(">>>>queryProductAttribute>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<ProductAttributeDto> list=this.jdbcTemplate.query(querySql, map, new BeanPropertyRowMapper<ProductAttributeDto>(ProductAttributeDto.class));
        logger.info(">>>>queryProductAttribute>>>>>where"+whereSql+">>>>>>>result:"+list);

        return list;
    }

    /**
     * 更新商品属性
     * @param info  更新商品属性
     *//*
    public void updateProductAttribute(TbProductAttribute info){
        this.save(info);
    }

    *//**
     * 新增产品信息
     * @param info  要新增的产品信息
     *//*
    public void addProductAttribute(TbProductInfo info){
        this.save(info);
    }*/
}
