//// src/main/java/com/example/intermediateapp/config/FilterConfig.java
//
//package com.example.intermediateapp.config;
//
//import com.example.intermediateapp.filter.AuthTokenFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//
//@Configuration
//public class FilterConfig {
//
//    @Autowired
//    private AuthTokenFilter authTokenFilter;
//
//    @Bean
//    public FilterRegistrationBean<AuthTokenFilter> loggingFilter(){
//        FilterRegistrationBean<AuthTokenFilter> registrationBean
//                = new FilterRegistrationBean<>();
//
//        registrationBean.setFilter(authTokenFilter);
//        registrationBean.addUrlPatterns("/forward/**");
//        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//
//        return registrationBean;
//    }
//}
