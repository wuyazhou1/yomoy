package com.nsc.Amoski.controller;


import com.nsc.Amoski.FeignClient.UserServerApi;
import com.nsc.Amoski.config.ParentValidatorFactoryMessage;
import com.nsc.Amoski.parent.queryActivityListParent;
import com.nsc.Amoski.service.EnrolmentActivitiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * @ClassName: EnrolmentActivities
 * @methodname:
 * @Description: 测试方法
 * @author: wusiyao
 * @date: 2019/10/31 15:03
 */
@RestController
@RequestMapping("/EnrolmentActivities")
public class EnrolmentActivitiesController extends ActivityServerBaseController<AppRidingGuideManageController> {

    private static final Logger log = LoggerFactory.getLogger(EnrolmentActivitiesController.class);

    @Autowired
    private UserServerApi UserServerApi;
    @Autowired
    private EnrolmentActivitiesService EnrolmentActivitiesService;

    @RequestMapping(value="/queryMandatoryField",method = {RequestMethod.POST,RequestMethod.GET})
    public Object queryMandatoryField(@Valid @RequestBody queryActivityListParent queryActivityListParent){
        log.info("queryActivityListParent.id info "+queryActivityListParent.getId());
//        List<Map<String,String>> list = (ArrayList<Map<String,String>>) UserServerApi.GetDictZtree("17");
//        List<String> result = new ArrayList<String>();
//        for(Map<String,String> map:list){
//            result.add(map.get("name"));
//        }
        List<String> list = EnrolmentActivitiesService.queryActivityListParent(queryActivityListParent.getId());
        log.info("查询出"+list.size()+"条数据，参数值"+queryActivityListParent.getId());
        return success(list);
    }
}
