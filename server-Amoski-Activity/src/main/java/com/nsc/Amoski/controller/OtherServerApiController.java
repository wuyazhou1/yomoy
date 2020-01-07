package com.nsc.Amoski.controller;

import com.nsc.Amoski.entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otherServerApi")
public class OtherServerApiController extends ActivityServerBaseController<OtherServerApiController> {


    @RequestMapping("/getUserImgInfo")
    public Result getUserImgInfo(String uid,String imgId){


        return success();
    }






}
