package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.FeignClient.UserServerApi;
import com.nsc.Amoski.dao.SameCityDao;
import com.nsc.Amoski.dao.UserPersonalCenterDao;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.service.SameCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SameCityServiceImpl implements SameCityService {


    @Autowired
    private UserServerApi userServerApi;


    @Autowired
    private UserPersonalCenterDao UserPersonalCenterDao;

    @Autowired
    private SameCityDao sameCityDao;

    @Override
    /*@Cacheable(value = "String",key = "T(String).valueOf('sameCityData')." +
            "concat(#dto.xAxis.substring(0,#dto.xAxis.length()-2))." +
            "concat(#dto.yAxis.substring(0,#dto.yAxis.length()-2))." +
            "concat(#dto.cycle==null?'':#dto.cycle)."+
            "concat(#dto.name==null?'':#dto.name)")*/
    public Map<String,Object> sameCityData(MemberView dto) {
        Map<String,Object> result=new HashMap<>();
        result.put("wholeCountryMember",userServerApi.queryMemberByYXAxis(dto));
        dto.setDistance("2000");
        result.put("sameCityMember",userServerApi.queryMemberByYXAxis(dto));
        return result;
    }

    @Override
    public Map<String, Object> knightHomePage(MemberView memberView) {
        Map<String,Object> resultMap = new HashMap<>();
        SqlParameterSource Parameter = new BeanPropertySqlParameterSource(memberView);
        //查询关注总条数
        resultMap.put("FollowCount",UserPersonalCenterDao.queryFollowCount(Parameter));
        //查询粉丝总条数
        resultMap.put("FansCount",UserPersonalCenterDao.queryFansCount(Parameter));
        //查询获赞总条数
        resultMap.put("getFabulous",UserPersonalCenterDao.queryBePraisedCount(Parameter));
        MemberView memberView1 = userServerApi.findMemberView(null, null, null, memberView.getId().toString());
        memberView1.setPassword(null);
        memberView1.setSalt(null);
        memberView1.setLoginname(null);
        memberView1.setTel(null);
        memberView1.setLocked(null);
        memberView1.setBindingIdentification(null);
        //查询会员信息
        resultMap.put("Member",memberView1);
        return resultMap;
    }

    @Override
    public PagingBean getFabulousList(MemberView memberView) {
        List list = sameCityDao.getFabulousList(memberView);
        int count = sameCityDao.getFabulousCount(memberView);
        PagingBean pagelist = new PagingBean();
        pagelist.setData(list);
        pagelist.setRecordsFiltered(count);
        pagelist.setRecordsTotal(count);
        return pagelist;
    }

    @Override
    public Integer queryFabulousByMemberView(MemberView dto, MemberView memberView) {
        return sameCityDao.queryFabulousByMemberView(dto,memberView);
    }
}