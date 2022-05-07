package com.example.demo.config;

import com.example.demo.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")// 拦截所有请求判断token 是否合法来决定是否需要登录
                .excludePathPatterns("/user/login","/user/register","/**/export","/**/import","/code/**","/app/login","/images","/details","/file/**","/report/**","/swagger-ui.html/**","/swagger-resources/**","/webjars/**","/v2/**");
    }
    @Bean
    public JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();
    }
}
