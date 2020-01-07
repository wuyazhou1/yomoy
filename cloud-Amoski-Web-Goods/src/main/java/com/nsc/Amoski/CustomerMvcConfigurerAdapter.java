package com.nsc.Amoski;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CustomerMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置静态资源处理
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/WEB-INF/");
//                .addResourceLocations("classpath:/static2/")
//                .addResourceLocations("classpath:/public2/")
//                .addResourceLocations("classpath:/META-INF/resources2/");
    }

}
