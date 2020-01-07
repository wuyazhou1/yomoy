package com.nsc;

import com.nsc.Amoski.filter.PreRequestLogFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.ZuulProxyConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 李阳
 * @date 2019/12/16 17:48
 */
@SpringBootApplication
@EnableDiscoveryClient
@Import({ZuulProxyConfiguration.class})
@EnableSwagger2
public class AmoskiJGApplicationMain {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AmoskiJGApplicationMain.class, args);
    }

    public PreRequestLogFilter getquestLogFilter() {
        return new PreRequestLogFilter();
    }

}
