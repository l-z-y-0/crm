package com.xxx.crm.config;

import com.xxx.crm.interceptors.NologinInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*@Configuration*/
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    public NologinInterceptor createInterceptor(){
        return new NologinInterceptor();
    }

/*    @Override
    public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(createInterceptor())
                        .addPathPatterns("/**")
                        .excludePathPatterns("/index", "/checkLogin", "/css/**", "/images/**", "/js/**", "/lib/**");
    }*/
}
