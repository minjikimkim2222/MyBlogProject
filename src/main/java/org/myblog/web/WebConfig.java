package org.myblog.web;

import org.myblog.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 로그인 처리를 위한 인터셉터
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

    // HiddenHttpMethodFilter 등록 !!
    @Bean
    public HiddenHttpMethodFilter httpMethodFilter(){
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();

        return hiddenHttpMethodFilter;
    }
}
