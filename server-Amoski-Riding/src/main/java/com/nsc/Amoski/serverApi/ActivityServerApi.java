package com.nsc.Amoski.serverApi;

import com.nsc.Amoski.dto.TbRidingGuideInfoDto;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.TbActivityBasicsEntity;
import com.nsc.Amoski.entity.TbPhotoPic;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "AmoskiActivity")//
public interface ActivityServerApi {
    /*@RequestMapping(value = "Member/checkMember" , method = RequestMethod.POST)
    public boolean checkMember(String loginName,String password,String loginIdentification);*/

    /**
     * 查询用户某张图片
     * @param imgId
     * @return
     */
    @RequestMapping(value = "/userCenterManage/querySingleUserImg" , method = RequestMethod.POST)
    TbPhotoPic querySingleUserImg(@RequestParam(name = "imgId") Integer imgId);

    /**
     * 查询一张或多张图片根据图片ids
     * @param imgIds
     * @return
     */
    @RequestMapping(value = "/userCenterManage/queryImgByImgId" , method = RequestMethod.POST)
    TbPhotoPic querySingleUserImg(@RequestParam(name = "imgIds") String imgIds);

    /**
     * 查询活动列表数据
     * @param dto
     * @return
     */
    @RequestMapping(value = "/appRidingGuideManage/queryActivityList" , method = RequestMethod.POST)
    Result queryActivityList(@RequestBody TbRidingGuideInfoDto dto);

}
