package com.nsc.AmoskiUser.service.impl;

import com.nsc.AmoskiUser.dao.MemberManageDao;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.AmoskiUser.service.MemberManageService;
import com.nsc.Amoski.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service

@Transactional(transactionManager = "UserTransactionManager")
public class MemberManageServiceImpl implements MemberManageService {

    @Autowired
    @Lazy
    public MemberManageDao memberManageDao;

    @Override
    public PagingBean getMemberList(Map<String, Object> params) {
        List list = memberManageDao.findDepartmentList(params);
        Integer count = memberManageDao.findDepartmentCount(params);
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
    public Object getMemberImpowerRole(String memberId, String orgCode) {
        return memberManageDao.getMemberImpowerRole(memberId,orgCode);
    }
}
