package com.nsc.AmoskiUser.client;


//import org.springframework.cloud.openfeign.FeignClient;

//@FeignClient(name = "AmoskiUser")
public interface ActivityClient {

    //@RequestMapping(value = "DepartmentManage/getZtreeDatail" , method = RequestMethod.POST)
    public Object getZtreeDatail(String id,String loginIdentification);

}
