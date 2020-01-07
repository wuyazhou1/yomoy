package com.nsc;


import com.nsc.Amoski.filter.PreRequestLogFilter;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.ZuulProxyConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableTransactionManagement
@SpringBootApplication
@EnableDiscoveryClient
@Import({ZuulProxyConfiguration.class})
@EnableSwagger2
@EnableScheduling        //使能定时任务
@EnableFeignClients
//@EnableRedisHttpSession
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class AmoskiRidingApplicationMain {
    private static final Log logger = LogFactory.getLog(AmoskiRidingApplicationMain.class);

    public static void main(String[] args){
        SpringApplication.run(AmoskiRidingApplicationMain.class,args);
    }

    public PreRequestLogFilter getquestLogFilter(){
        return new PreRequestLogFilter();
    }

}
