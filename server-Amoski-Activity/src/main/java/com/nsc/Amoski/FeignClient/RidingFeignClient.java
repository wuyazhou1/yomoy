package com.nsc.Amoski.FeignClient;


import com.nsc.Amoski.entity.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@FeignClient(name = "AmoskiRiding")//
@Component
public interface RidingFeignClient {

    @RequestMapping(value = "/ridingManage/queryUserDisCountRidingInfo2" , method = RequestMethod.POST)
    Object queryUserDisCountRidingInfo2(@RequestBody RidingInfoDto rdto);

    @RequestMapping(value = "/appRidingGuideManage/queryGuideListTwo" , method = RequestMethod.POST)
    Result queryGuideList(@RequestBody TbRidingGuideInfoDto dto);
    /*@RequestMapping(value = "/ridingManage/queryUserDisCountRidingInfo" , method = RequestMethod.POST)
    Result queryUserDisCountRidingInfo(HttpServletResponse response, HttpServletRequest request, @RequestBody RidingInfoDto ridingInfoDto);*/
}
