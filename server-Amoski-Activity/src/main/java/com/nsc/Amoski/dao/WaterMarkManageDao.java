package com.nsc.Amoski.dao;


import com.nsc.Amoski.dto.WaterMakeInfoDto;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TbWaterMakeInfo;


public interface WaterMarkManageDao {

    /**
     * 根据部门id查询所有的水印(包括下级部门)
     * @param param 查询条件 默认当前用户部门及下级部门所有id
     * @return  所有水印信息
     */
    public PagingBean queryWaterMakeByDept(WaterMakeInfoDto param);

    /**
     * 更新水印信息
     * @param info  要更新的水印信息
     */
    public void updateWaterMarkInfo(TbWaterMakeInfo info);

    /**
     * 新增水印信息
     * @param info  要新增的水印信息
     */
    public void saveWaterMarkInfo(TbWaterMakeInfo info);
}
