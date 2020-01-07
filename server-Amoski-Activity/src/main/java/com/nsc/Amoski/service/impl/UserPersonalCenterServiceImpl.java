package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.UserPersonalCenterDao;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.parent.DynamicCollectionParent;
import com.nsc.Amoski.parent.DynamicSpotFabulousParent;
import com.nsc.Amoski.parent.FansAttentionParent;
import com.nsc.Amoski.entity.PagingBeanQuery;
import com.nsc.Amoski.service.UserPersonalCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserPersonalCenterServiceImpl implements UserPersonalCenterService {

    @Autowired
    private UserPersonalCenterDao UserPersonalCenterDao;

    @Override
    public Map<String, Object> PersonalCenterDatil(MemberView dto) {
        Map<String,Object> resultMap = new HashMap<>();
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(dto);
        //查询发布动态总条数
        resultMap.put("DynamicCount",UserPersonalCenterDao.queryDynamicCount(Parameter));
        //查询关注总条数
        resultMap.put("FollowCount",UserPersonalCenterDao.queryFollowCount(Parameter));
        //查询粉丝总条数
        resultMap.put("FansCount",UserPersonalCenterDao.queryFansCount(Parameter));
        //查询点赞总条数
        resultMap.put("FabulousCount",UserPersonalCenterDao.queryFabulousCount(Parameter));
        return resultMap;
    }

    @Override
    public Object queryDynamicList(PagingBeanQuery pagingBeanQuery) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(pagingBeanQuery);
        List list = UserPersonalCenterDao.queryDynamicList(Parameter);
        Integer count = UserPersonalCenterDao.queryDynamicCount(Parameter);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }

    @Override
    public Object queryFollowList(FansAttentionParent fansAttentionParent) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(fansAttentionParent);
        List list = UserPersonalCenterDao.queryFollowList(Parameter);
        Integer count = UserPersonalCenterDao.queryFollowCount(Parameter);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }

    @Override
    public Object queryFansList(FansAttentionParent fansAttentionParent) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(fansAttentionParent);
        List list = UserPersonalCenterDao.queryFansList(Parameter);
        Integer count = UserPersonalCenterDao.queryFansCount(Parameter);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }

    @Override
    public Object queryFabulousList(DynamicSpotFabulousParent dynamicSpotFabulousParent) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(dynamicSpotFabulousParent);
        List list = UserPersonalCenterDao.queryFabulousList(Parameter);
        Integer count = UserPersonalCenterDao.queryFabulousCount(Parameter);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }
    @Override
    public Object queryFabulousListTo(DynamicSpotFabulousParent dynamicSpotFabulousParent) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(dynamicSpotFabulousParent);
        List list = UserPersonalCenterDao.queryFabulousListTo(Parameter);
        Integer count = UserPersonalCenterDao.queryFabulousCount(Parameter);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }

    @Override
    public Object dynamicCollectionList(MemberView dto) {
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(dto);
        List<DynamicCollectionParent> list = UserPersonalCenterDao.dynamicCollectionList(Parameter);
        Integer count = UserPersonalCenterDao.dynamicCollectionCount(Parameter);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }
}
