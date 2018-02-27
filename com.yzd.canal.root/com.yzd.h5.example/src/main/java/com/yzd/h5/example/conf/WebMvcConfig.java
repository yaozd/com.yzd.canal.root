package com.yzd.h5.example.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    /**
     * 提前加载拦截器，解决注入问题
     * @return
     */
    @Bean
    LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }
    /**
     * 配置拦截器
     *
     * @param registry
     * @author yzd
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/account/doLogin")
                .excludePathPatterns("/account/doLogout");
    }

}
