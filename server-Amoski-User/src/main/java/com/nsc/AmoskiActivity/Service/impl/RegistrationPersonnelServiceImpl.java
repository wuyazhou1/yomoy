package com.nsc.AmoskiActivity.Service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.entity.*;
import com.nsc.Amoski.util.StringUtils;
import com.nsc.AmoskiActivity.Controller.RegistrationPersonnelController;
import com.nsc.AmoskiActivity.Dao.RegistrationPersonnelDao;
import com.nsc.AmoskiActivity.Service.RegistrationPersonnelService;
import com.nsc.AmoskiActivity.Util.CreateActivityAnalysisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Transactional(transactionManager = "ActivityTransactionManager")
public class RegistrationPersonnelServiceImpl implements RegistrationPersonnelService {
    private static final Logger log = LoggerFactory.getLogger(RegistrationPersonnelController.class);
    @Autowired
    private RegistrationPersonnelDao registrationPersonnelDao;


    @Override
    public PagingBean RegistrationPersonnelList(Map<String, Object> params) {
        List list = registrationPersonnelDao.RegistrationPersonnelList(params);
        Integer count = registrationPersonnelDao.RegistrationPersonnelCount(params);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        if (StringUtils.isEmpty(params.get("draw")))
            pagelist.setDraw(0);
        else
            pagelist.setDraw(Integer.valueOf(params.get("draw").toString()));
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }

    @Override
    public List<Map<String, Object>> RegistrationPersonnelDetails(String id) {
        List<TbActivityBasicsEntity> list = registrationPersonnelDao.queryHotelNameRoomType(id);
        Map<String, Object>  resultMap = registrationPersonnelDao.queryHotelNameRoomType(list);
        return registrationPersonnelDao.RegistrationPersonnelDetails(id,resultMap);
    }

    @Override
    public void SaveActivitySignUp(JSONArray signUpList) {
        for(int e=0;e<signUpList.size();e++){
            JSONObject signUp = signUpList.getJSONObject(e);
            JSONObject tbActivitySignUpEntity = signUp.getJSONObject("tbActivitySignUpEntity");//活动报名人员
            TbActivitySignUpEntity TbActivitySignUpEntity = (TbActivitySignUpEntity) CreateActivityAnalysisUtil.ActivityAnalysisUtil(tbActivitySignUpEntity, TbActivitySignUpEntity.class);//活动报名人员
            JSONArray tbPeopleReceiveSendEntityList = signUp.getJSONArray("tbPeopleReceiveSendEntity");//接送机标
            List TbPeopleReceiveSendList = new ArrayList();//接送机标
            for(int i=0;i<tbPeopleReceiveSendEntityList.size();i++){//接送机标
                TbPeopleReceiveSendEntity TbPeopleReceiveSendEntity = (TbPeopleReceiveSendEntity) CreateActivityAnalysisUtil.ActivityAnalysisUtil(tbPeopleReceiveSendEntityList.getJSONObject(i), TbPeopleReceiveSendEntity.class);//活动报名人员
                TbPeopleReceiveSendEntity.setPeopleId(TbActivitySignUpEntity.getId().toString());
                TbPeopleReceiveSendList.add(TbPeopleReceiveSendEntity);
            }
            JSONArray tbPeoplePutUpEntityList = signUp.getJSONArray("tbPeoplePutUpEntity");//住宿管理
            List TbPeoplePutUpList = new ArrayList();
            for(int i=0;i<tbPeoplePutUpEntityList.size();i++){//住宿管理
                TbPeoplePutUpEntity TbPeoplePutUpEntity = (TbPeoplePutUpEntity) CreateActivityAnalysisUtil.ActivityAnalysisUtil(tbPeoplePutUpEntityList.getJSONObject(i), TbPeoplePutUpEntity.class);//住宿管理
                TbPeoplePutUpEntity.setPeopleId(TbActivitySignUpEntity.getId().toString());
                TbPeoplePutUpList.add(TbPeoplePutUpEntity);
            }
            //删除接送机信息
            registrationPersonnelDao.deleteObject(TbActivitySignUpEntity);
            //修改人员活动报名信息
            registrationPersonnelDao.updateUpdate(TbActivitySignUpEntity);
            //修改人员住宿信息
            if(TbPeoplePutUpList.size()>0)
                registrationPersonnelDao.updateUpdateList(TbPeoplePutUpList);
            //插入接送机信息
            if(TbPeopleReceiveSendList.size()>0)
                registrationPersonnelDao.addObjectList(TbPeopleReceiveSendList);
        }
    }
}
