package com.nsc.Amoski.dao.Impl;

import com.nsc.Amoski.dao.LoginNameCheckedDao;
import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TUserEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;


@Repository
public class LoginNameCheckedDaoImpl implements LoginNameCheckedDao {
    @Override
    @Cacheable(value = "String",key = "T(String).valueOf(#ipAddr).concat('-TUserEntity')")
    public TUserEntity putIpAddrTUserEntity(String ipAddr) {
        return null;
    }

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf(#ipAddr).concat('-ShiroUser')")
    public ShiroUser putIpAddrShiroUser(String ipAddr) {
        return null;
    }
}
