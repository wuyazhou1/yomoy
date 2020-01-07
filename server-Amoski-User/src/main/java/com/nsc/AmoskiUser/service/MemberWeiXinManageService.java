package com.nsc.AmoskiUser.service;

import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.TMemberEntity;

import java.util.Map;

public interface MemberWeiXinManageService {
    void addWeiXin(MemberView memberView);


    Boolean checkedMeneberLogin(String loginName, String password);

    Boolean checkedWeiXin(String openId);

    Map checkedLoginRepeat(String tel, String loginName);

    TMemberEntity TelRegister(MemberView memberView);

    MemberView findMemberView(String tel, String loginName,String openId);

    Boolean UpdateMemberView(MemberView view);
}
