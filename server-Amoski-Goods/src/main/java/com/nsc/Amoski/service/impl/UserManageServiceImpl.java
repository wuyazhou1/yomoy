package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.config.SpringContextUtil;
import com.nsc.Amoski.service.UserManageService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.Serializable;


@Service
public class UserManageServiceImpl implements UserManageService {

    @Cacheable(value = "String",key = "#name")
    @Override
    public Integer findData(@PathVariable("name")Serializable name) {
        Integer returnStr = null;
        returnStr = ((int)(Math.random()*1000));
        System.out.println(returnStr);
        return returnStr;
    }

    @Override
    public String findMathRandom(@PathVariable("name") Serializable name){
        String returnStr = null;
        returnStr = "下班啦"+((int)(Math.random()*1000));
        System.out.println(returnStr);
        return returnStr;
    }
}
