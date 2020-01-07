package com.nsc;


import com.nsc.Amoski.config.SpringContextUtil;
import com.nsc.Amoski.filter.PreRequestLogFilter;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.ZuulProxyConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableTransactionManagement
@SpringBootApplication()
@EnableDiscoveryClient
@Import({ZuulProxyConfiguration.class})
@EnableSwagger2
@EnableFeignClients
@EnableRedisHttpSession
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class AmoskiActivityApplicationMain {
    private static final Log logger = LogFactory.getLog(AmoskiActivityApplicationMain.class);

    public static void main(String[] args){
        SpringContextUtil springContextUtil = new SpringContextUtil();
        ApplicationContext applicationContext = SpringApplication.run(AmoskiActivityApplicationMain.class, args);
        springContextUtil.setApplicationContext(applicationContext);
    }

    public PreRequestLogFilter getquestLogFilter(){
        return new PreRequestLogFilter();
    }

}
