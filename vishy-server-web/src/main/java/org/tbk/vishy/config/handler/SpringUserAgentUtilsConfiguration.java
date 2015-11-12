package org.tbk.vishy.config.handler;

import com.github.theborakompanioni.spring.useragentutils.UserAgentHandlerMethodArgumentResolver;
import com.github.theborakompanioni.spring.useragentutils.UserAgentResolverHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.util.List;


/**
 * Created by void on 08.03.15.
 */
@Slf4j
@Configuration
public class SpringUserAgentUtilsConfiguration extends WebMvcConfigurerAdapter {

    @PostConstruct
    public void init() {
        log.info("SpringUserAgentUtilsConfiguration init");
    }

    @Bean
    public UserAgentResolverHandlerInterceptor userAgentResolverHandlerInterceptor() {
        return new UserAgentResolverHandlerInterceptor();
    }

    @Bean
    public UserAgentHandlerMethodArgumentResolver userAgentHandlerMethodArgumentResolver() {
        return new UserAgentHandlerMethodArgumentResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userAgentResolverHandlerInterceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userAgentHandlerMethodArgumentResolver());
    }
}
