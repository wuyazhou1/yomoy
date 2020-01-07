package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.MemberWeiXinManageDao;
import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.TMemberDailEntity;
import com.nsc.Amoski.entity.TMemberEntity;
import com.nsc.Amoski.entity.TWeixinEntity;
import com.nsc.Amoski.service.MemberWeiXinManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class MemberWeiXinManageServiceImpl implements MemberWeiXinManageService {

    @Autowired
    @Lazy
    public MemberWeiXinManageDao memberWeiXinManageDao;


    @Override
    public void addWeiXin(MemberView memberView) {
        //Integer random = (int) (Math.random() * 10000);
        //memberView.setSalt(random.toString());
        TMemberEntity TMemberEntity = new TMemberEntity(memberView);
        TMemberEntity.setCreateDate(new Date());

        TMemberEntity = memberWeiXinManageDao.AddTMemberEntity(TMemberEntity);
        memberView.setId(TMemberEntity.getId());
        TMemberDailEntity TMemberDailEntity = new TMemberDailEntity(memberView);
        memberWeiXinManageDao.AddTMemberDailEntity(TMemberDailEntity);
        memberView.setMemberId(TMemberEntity.getId());
        memberWeiXinManageDao.addWeiXin(memberView);
        //memberWeiXinManageDao.updateMemberCreateDate(TMemberEntity);
    }

    @Override
    public Boolean checkedMeneberLogin(String loginName, String password) {
        return memberWeiXinManageDao.checkedMeneberLogin(loginName,password);
    }

    @Override
    public Boolean checkedWeiXin(String openId) {
        return memberWeiXinManageDao.checkedWeiXin(openId);
    }

    @Override
    public Boolean checkedLoginRepeat(String tel, String loginName) {
        return memberWeiXinManageDao.checkedLoginRepeat(tel,loginName);
    }

    @Override
    public TMemberEntity TelRegister(MemberView memberView) {
        return memberWeiXinManageDao.TelRegister(memberView);
    }

    @Override
    public MemberView findMemberView(String tel, String loginName,String openId) {
        return memberWeiXinManageDao.findMemberView(tel,loginName,openId);
    }

    @Override
    public Boolean UpdateMemberView(MemberView view) {
        TMemberEntity tMemberEntity = new TMemberEntity(view);
        /*if(tMemberEntity.getPassword()!=null){
            if(tMemberEntity.getSalt()==null){
                tMemberEntity.setSalt(PasswordUtil.encodeHex(PasswordUtil.generateSalt(PasswordUtil.getSaltSize())));
            }
            tMemberEntity.setPassword(PasswordUtil.setPassWordBySalt(tMemberEntity.getPassword(),tMemberEntity.getSalt()));
        }*/
        TWeixinEntity tWeixinEntity = new TWeixinEntity(view);
        memberWeiXinManageDao.updateTmemberEntity(tMemberEntity);
        TMemberDailEntity TMemberDailEntity = new TMemberDailEntity(view);
        memberWeiXinManageDao.updateTMemberDailEntity(TMemberDailEntity);
        if(view.getWxId()!=null && !view.getWxId().equals(""))
            memberWeiXinManageDao.updateTWeiXinEntity(tWeixinEntity);
        return null;
    }
}
