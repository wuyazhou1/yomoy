package com.nsc.Amoski.dao;

import com.nsc.Amoski.entity.ShiroUser;
import com.nsc.Amoski.entity.TUserEntity;

public interface LoginNameCheckedDao {
    TUserEntity putIpAddrTUserEntity(String ipAddr);

    ShiroUser putIpAddrShiroUser(String ipAddr);
}
