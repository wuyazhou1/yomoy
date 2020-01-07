package com.nsc.Amoski.service.impl;

import com.nsc.Amoski.dao.LoginNameCheckedDao;
import com.nsc.Amoski.entity.TUserEntity;
import com.nsc.Amoski.service.LoginNameCheckedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class LoginNameCheckedServiceImpl implements LoginNameCheckedService {


    @Autowired
    @Lazy
    public LoginNameCheckedDao loginNameCheckedDao;

    @Override
    public HashMap<String,Object> findRequestShiro(String ipAddr) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("TUserEntity",loginNameCheckedDao.putIpAddrTUserEntity(ipAddr));
        resultMap.put("ShiroUser",loginNameCheckedDao.putIpAddrShiroUser(ipAddr));
        return (HashMap)resultMap;
    }

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf('findUserByLoginName-').concat(#username)")
    public TUserEntity findUserByLoginName(String username) {
        return null;
    }

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf('findMenuResourceByUserCode-').concat(#code)")
    public List<String> findMenuResourceByUserCode(String code) {
        return null;
    }

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf('findMenuResourceAll')")
    public List<String> findMenuResourceAll() {
        return null;
    }

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf('finRoleCodeByUserCode-').concat(#code)")
    public Collection<? extends String> finRoleCodeByUserCode(String code) {
        return null;
    }

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf(#code)")
    public Object putLoginToke(String code, Object obj) {
        return obj;
    }

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf('findMenuFrameListMap')")
    public List<Map<String, Object>> findMenuFrameListMap() {
        return null;
    }

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf(#parentMap)")
    public List<Map<String, Object>> findMenuByMenuFrameListMap(List<Map<String, Object>> parentMap) {
        List<Map<String,Object>> resultListMap = new ArrayList<>();
        for(Map<String, Object> map :parentMap){
            Map<String,Object> resultMap = new HashMap<>();
            StringBuffer startMenuSta = new StringBuffer();
            StringBuffer endMenuSta = new StringBuffer();
            List<Map<String, Object>> next_menu = null;
            startMenuSta.append("<li class=\"layui-nav-item\">");
            startMenuSta.append("<a class=\"\"  href=\"javascript:;\">"+map.get("MENU_NAME")+"</a>");
            if(map.get("NEXT_MENU")!=null){
                startMenuSta.append("<dl class=\"layui-nav-child\">");
                next_menu = findMenuByMenuFrameNextMenuListMap((List<Map<String, Object>>) map.get("NEXT_MENU"));
                resultMap.put("next_menu",next_menu);
                endMenuSta.append("</dl>");
            }
            endMenuSta.append("</li>");
            resultMap.put("menu_code",map.get("CODE"));
            resultMap.put("next_menu",next_menu);
            resultMap.put("start_menu",startMenuSta);
            resultMap.put("end_menu",endMenuSta);
            resultListMap.add(resultMap);
        }
        return resultListMap;
    }
    public List<Map<String,Object>> findMenuByMenuFrameNextMenuListMap(List<Map<String,Object>> parentMap ){
        List<Map<String,Object>> resultListMap = new ArrayList<>();
        for(Map<String, Object> map :parentMap){
            Map<String,Object> resultMap = new HashMap<>();
            StringBuffer startMenuSta = new StringBuffer();
            StringBuffer endMenuSta = new StringBuffer();
            List<Map<String, Object>> next_menu = null;
            if(map.get("NEXT_MENU")!=null&&((List<Map<String, Object>>)map.get("NEXT_MENU")).size()>0){
                startMenuSta.append("<dd><li class=\"layui-nav-item\">");
                startMenuSta.append("<a class=\"\" href=\"javascript:;\">"+ map.get("MENU_NAME")+"</a>");
                startMenuSta.append("<dl class=\"layui-nav-child\"><ul   lay-filter=\"test\">");
                next_menu = findMenuByMenuFrameNextMenuListMap((List<Map<String, Object>>) map.get("NEXT_MENU"));
                endMenuSta.append("</ul></dl></li></dd>");
            }else{
                startMenuSta.append("<dd><a href=\"javascript:;\" onclick=\"showPageIframe(this)\" data-href=\""+map.get("MENU_URL")+"\" data-name=\""+map.get("MENU_NAME")+"\" data-nameMenu=\"menu"+map.get("CODE")+"\">");
                endMenuSta.append(map.get("MENU_NAME")+"</a></dd>");
            }
            resultMap.put("menu_code",map.get("CODE"));
            resultMap.put("next_menu",next_menu);
            resultMap.put("start_menu",startMenuSta);
            resultMap.put("end_menu",endMenuSta);
            resultListMap.add(resultMap);
        }
        return resultListMap;
    }

    @Override
    @Cacheable(value = "String",key = "T(String).valueOf(#key).concat('getShiroRedisObject')")
    public Object getShiroRedisObject(Object key) {
        return null;
    }
}
