package com.nsc;


import com.nsc.Amoski.filter.PreRequestLogFilter;
import com.nsc.Amoski.nettyServer.NettyServer;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.ZuulProxyConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableTransactionManagement
@SpringBootApplication
@EnableDiscoveryClient
@Import({ZuulProxyConfiguration.class})
@EnableSwagger2
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class AmoskiRidingTeamApplicationMain implements CommandLineRunner{
    private static final Log logger = LogFactory.getLog(AmoskiRidingTeamApplicationMain.class);

    @Autowired
    private NettyServer nettyServer;

    public static void main(String[] args){
        SpringApplication.run(AmoskiRidingTeamApplicationMain.class,args);
    }

    public PreRequestLogFilter getquestLogFilter(){
        return new PreRequestLogFilter();
    }

    @Override
    public void run(String... strings) throws Exception {
        nettyServer.init();
    }
}
