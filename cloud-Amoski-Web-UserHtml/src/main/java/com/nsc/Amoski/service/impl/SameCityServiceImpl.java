package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.SameCityDao;
import com.nsc.Amoski.entity.MemberDto;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.service.SameCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SameCityServiceImpl implements SameCityService {
    @Autowired
    private SameCityDao sameCityDao;

    @Override
    public void updateMemberYXAxis(MemberView memberView) {
        sameCityDao.updateMemberYXAxis(memberView);
    }

    @Override
    public List queryMemberByYXAxis(MemberView memberView) {
        return sameCityDao.queryMemberByYXAxis(memberView);
    }

    @Override
    public PagingBean<MemberDto> queryMemberByMemberName(MemberView memberView) {
        PagingBean<MemberDto> bean=new PagingBean<>();
        bean.setData(sameCityDao.queryMemberByMemberName(memberView));
        Integer count = sameCityDao.queryMemberByMemberNameCount(memberView);
        bean.setRecordsTotal(count);
        bean.setRecordsFiltered(count);
        return bean;
    }

    @Override
    public List queryMemberRanking(MemberView dto) {
        List list = sameCityDao.queryMemberRankingList(dto);
        return list;
    }

    @Override
    public void saveBackgroundImages(MemberView dto) {
        sameCityDao.saveBackgroundImages(dto);
    }
}
