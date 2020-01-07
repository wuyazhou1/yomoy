package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.DictDao;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TDictEntity;
import com.nsc.Amoski.service.DictService;
import com.nsc.Amoski.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional
public class DictServiceImpl implements DictService {

    @Autowired
    @Lazy
    public DictDao dictDao;

    @Override
    public PagingBean DictList(Map<String, Object> params) {
        List list = dictDao.DictList(params);
        int count = dictDao.DictCount(params);
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
    public void CheckedDict(String id, String code) {
        dictDao.CheckedDict(id,code);
    }

    @Override
    public Object GetDictZtree(String parentCode) {
        return dictDao.GetDictZtree(parentCode);
    }
}
