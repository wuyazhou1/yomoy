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
public class AmoskiWebActivityApplicationMain {
    private static final Log logger = LogFactory.getLog(AmoskiWebActivityApplicationMain.class);

    public static void main(String[] args){
        SpringApplication.run(AmoskiWebActivityApplicationMain.class,args);
    }

    public PreRequestLogFilter getquestLogFilter(){
        return new PreRequestLogFilter();
    }

}
