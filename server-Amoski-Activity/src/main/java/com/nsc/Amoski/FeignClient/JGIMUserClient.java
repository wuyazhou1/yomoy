package com.nsc.Amoski.FeignClient;

import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.jg.JGIMUser;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author 李阳
 * @date 2019/12/18 9:18
 */
@FeignClient(name = "AmoskiJG")
@Component
public interface JGIMUserClient {

    /**
     * 注册用户
     *
     * @param jgimUser
     * @return
     */
    @RequestMapping(path = "/imUser/userRegester", method = RequestMethod.POST)
    Result regester(@RequestBody JGIMUser jgimUser);

}
