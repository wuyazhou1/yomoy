package com.nsc.Amoski.config;

import com.nsc.Amoski.filter.UserLoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebCrossConfigurer
        extends WebMvcConfigurerAdapter {

//    @Bean
//    public HandlerInterceptor getCrossFilter(){
//        return new CrossFilter();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 多个拦截器组成一个拦截器链
//        // addPathPatterns 用于添加拦截规则
//        // excludePathPatterns 用户排除拦截
//        registry.addInterceptor(getCrossFilter()).addPathPatterns("/*/**");
//        super.addInterceptors(registry);
//    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/*/**")
//                .allowedOrigins("http://192.168.5.155:8555")
//                .allowCredentials(true)
//                .allowedMethods("PUT","GET","POST","OPTIONS")
//                .allowedHeaders("x-requested-with,content-type,X-Custom-Header")
//                .maxAge(3600);
//    }

}