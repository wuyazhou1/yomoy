package com.nsc.Amoski.serverApi;

import com.nsc.Amoski.entity.MemberView;
import com.nsc.Amoski.entity.TMemberEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "AmoskiUser")//
public interface UserServerApi {
    /*@RequestMapping(value = "Member/checkMember" , method = RequestMethod.POST)
    public boolean checkMember(String loginName,String password,String loginIdentification);*/

    /**
     * 查询部门信息
     * @param id
     * @param loginIdentification
     * @return
     */
    @RequestMapping(value = "DepartmentManage/getZtreeDatail" , method = RequestMethod.POST)
    Object queryUserDeptInfo(@RequestParam(name = "id") String id, @RequestParam(name = "loginIdentification") String loginIdentification);

    /**
     *查询数据字典
     * @param parentCode
     * @return
     */
    @RequestMapping(value = "Dict/GetDictZtree" , method = RequestMethod.POST)
    Object GetDictZtree(@RequestParam(name = "parentCode") String parentCode);

}
