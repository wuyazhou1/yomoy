package com.nsc.Amoski.config;

import com.nsc.Amoski.interceptor.UserLoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebAppConfigurer 
        extends WebMvcConfigurerAdapter {

    @Bean
    public HandlerInterceptor getUserLoginFilter(){
        return new UserLoginFilter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(getUserLoginFilter()).addPathPatterns("/ridingManage/**");
        super.addInterceptors(registry);
    }

    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/memberUser/**")
                .allowedOrigins("http://192.168.5.155:8555")
                .allowCredentials(true)
                .allowedMethods("PUT","GET","POST","OPTIONS")
                .allowedHeaders("x-requested-with,content-type,X-Custom-Header")
                .maxAge(3600);
    }*/

}