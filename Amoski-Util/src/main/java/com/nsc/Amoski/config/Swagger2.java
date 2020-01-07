package com.nsc.Amoski.config;


import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.service.Parameter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling
@EnableSwagger2
public class Swagger2 {
    @Bean
    public Docket createRestApi(){
        //添加head参数start
        /*ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("x-access-token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());*/

        System.out.println("http://localhost:8222/swagger-ui.html");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.nsc.Amoski.controller"))

                .paths(PathSelectors.any())
                .build()
                //.globalOperationParameters(pars)

                ;
    }

    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("*")
                //.description("Swagger首页的标题下的描述")
                //.termsOfServiceUrl("https://localhost:8222/Swagger2")
                .build();
    }


}
