package com.nsc.Amoski.controller;

import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.util.HttpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/threeApi")
public class ThreeApiController extends ActivityServerBaseController<ThreeApiController> {


    /**
     * 获取手机天气
     * @return
     */
    public Result getWeather(HttpServletRequest request, HttpServletResponse response){
        //http://www.weather.com.cn/data/city3jdata/provshi/10107.html  获取市、地区信息
        //http://www.weather.com.cn/data/city3jdata/station/1010702.html   获取区信息
        //http://www.weather.com.cn/weather/101010200.shtml  获取地区

        String jsonStr = HttpUtil.sendGet("http://www.weather.com.cn/data/city3jdata/china.html");
        log.info(">>>>>>>>>>>>>>>>>>weather json !!! json:"+jsonStr);
        return success();
    }

    /*public static void main(String [] args){//http://www.weather.com.cn/data/cityinfo/1012504.html
        //String jsonStr = HttpUtil.sendGet("http://www.weather.com.cn/data/city3jdata/station/101250401.html");
        String jsonStr = HttpUtil.sendPost("http://m.weather.com.cn/data/101301103.html",null);
        System.out.println(">>>>>>.json"+jsonStr);
    }*/






}
