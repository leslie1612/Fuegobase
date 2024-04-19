package org.chou.project.fuegobase.config;

import org.chou.project.fuegobase.middleware.DomainInterceptor;
import org.chou.project.fuegobase.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class InterceptorWebMvcConfig implements WebMvcConfigurer {
    private final AuthenticationService authenticationService;

    @Autowired
    public InterceptorWebMvcConfig(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DomainInterceptor(authenticationService)).addPathPatterns("/api/v1/databases/projects/*/**");
    }
}
