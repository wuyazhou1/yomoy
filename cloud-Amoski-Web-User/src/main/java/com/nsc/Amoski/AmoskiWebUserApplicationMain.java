package com.nsc.Amoski;

import com.nsc.Amoski.chlientTest.StringUtil;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.ZuulProxyConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@Import({ZuulProxyConfiguration.class})
public class AmoskiWebUserApplicationMain {
    private static final Log logger = LogFactory.getLog(AmoskiWebUserApplicationMain.class);

    public static void main(String[] args){
        ApplicationContext app = SpringApplication.run(AmoskiWebUserApplicationMain.class, args);
        StringUtil.application = app;
    }


}
