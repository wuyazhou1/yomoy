package com.nsc;


import com.nsc.Amoski.filter.PreRequestLogFilter;
import com.nsc.Amoski.util.StringUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.ZuulProxyConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableTransactionManagement
@SpringBootApplication
@EnableDiscoveryClient
@Import({ZuulProxyConfiguration.class})
@EnableSwagger2
@EnableFeignClients//跨服务调用
@EnableEurekaClient
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class AmoskiUserApplicationMain {
    private static final Log logger = LogFactory.getLog(AmoskiUserApplicationMain.class);

    public static void main(String[] args){
        ApplicationContext run = SpringApplication.run(AmoskiUserApplicationMain.class, args);
        StringUtils.applicationContext= run;
//        System.out.println("appctx.getBeanDefinitionCount="+run.getBeanDefinitionCount());
//        try {
//            ((ConfigurableApplicationContext)run).close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public PreRequestLogFilter getquestLogFilter(){
        return new PreRequestLogFilter();
    }

}
