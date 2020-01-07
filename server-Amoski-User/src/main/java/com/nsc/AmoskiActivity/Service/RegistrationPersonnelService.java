package com.nsc.AmoskiActivity.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nsc.Amoski.entity.PagingBean;

import java.util.List;
import java.util.Map;

public interface RegistrationPersonnelService {
    PagingBean RegistrationPersonnelList(Map<String, Object> params);

    List<Map<String, Object>> RegistrationPersonnelDetails(String id);

    void SaveActivitySignUp(JSONArray signUp);
}
