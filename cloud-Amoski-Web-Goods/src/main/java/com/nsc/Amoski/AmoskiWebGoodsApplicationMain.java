package com.nsc.Amoski;

import com.nsc.Amoski.filter.PreRequestLogFilter;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.ZuulProxyConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@Import({ZuulProxyConfiguration.class})
public class AmoskiWebGoodsApplicationMain {
    private static final Log logger = LogFactory.getLog(AmoskiWebGoodsApplicationMain.class);

    public static void main(String[] args){
        SpringApplication.run(AmoskiWebGoodsApplicationMain.class,args);
    }

    public PreRequestLogFilter getquestLogFilter(){
        return new PreRequestLogFilter();
    }

}
