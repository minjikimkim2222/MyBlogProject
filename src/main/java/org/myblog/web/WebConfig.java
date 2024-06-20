package org.myblog.web;

import org.myblog.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/myblog", "/myblog/userreg", "/myblog/loginform",
                        "/myblog/login",
                        "/css/**", "/resources/**", "/*.ico", "/error"
                );
    }
}
