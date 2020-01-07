package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.WaterMarkManageDao;
import com.nsc.Amoski.dto.WaterMakeInfoDto;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TbWaterMakeInfo;
import com.nsc.Amoski.service.WaterMarkManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class WaterMarkManageServiceImpl implements WaterMarkManageService {

    @Autowired
    WaterMarkManageDao waterMarkManageDao;

    /**
     * 根据部门id查询所有的水印(包括下级部门)
     * @param param 查询条件 默认当前用户部门及下级部门所有id
     * @return  所有水印信息
     */
    public PagingBean queryWaterMakeByDept(WaterMakeInfoDto param){
        return waterMarkManageDao.queryWaterMakeByDept(param);
    }
    /**
     * 更新水印信息
     * @param info  要更新的水印信息
     */
    public void updateWaterMarkInfo(TbWaterMakeInfo info){
        waterMarkManageDao.updateWaterMarkInfo(info);
    }
    /**
     * 新增水印信息
     * @param info  要新增的水印信息
     */
    public void saveWaterMarkInfo(TbWaterMakeInfo info){
        waterMarkManageDao.saveWaterMarkInfo(info);
    }
}








































