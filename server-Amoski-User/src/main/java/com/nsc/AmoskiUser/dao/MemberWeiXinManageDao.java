package com.nsc.AmoskiUser.dao;

import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.TMemberDailEntity;
import com.nsc.Amoski.entity.TMemberEntity;
import com.nsc.Amoski.entity.TWeixinEntity;

import java.util.Map;

public interface MemberWeiXinManageDao {


    TMemberEntity AddTMemberEntity(TMemberEntity tMemberEntity);

    void addWeiXin(MemberView memberView);

    Boolean checkedMeneberLogin(String loginName, String password);

    Boolean checkedWeiXin(String openId);

    Map checkedLoginRepeat(String tel, String loginName);

    TMemberEntity TelRegister(MemberView memberView);

    MemberView findMemberView(String tel, String loginName,String openId);

    void updateTmemberEntity(TMemberEntity tMemberEntity);

    void updateTWeiXinEntity(TWeixinEntity tWeixinEntity);

    void AddTMemberDailEntity(TMemberDailEntity tMemberDailEntity);

    void updateTMemberDailEntity(TMemberDailEntity tMemberDailEntity);
}
