package com.nsc.Amoski.filter;

import com.nsc.Amoski.AmoskiWebRidingApplicationMain;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AmoskiWebRidingApplicationMain.class);
    }

}