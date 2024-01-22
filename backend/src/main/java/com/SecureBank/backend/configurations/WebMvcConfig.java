package com.SecureBank.backend.configurations;


import com.SecureBank.backend.controllers.interceptors.AuthenticationInterceptor;
import com.SecureBank.backend.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  private final AuthenticationService authenticationService;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    System.out.println("MESSAGE: add interceptors");
    registry.addInterceptor(new AuthenticationInterceptor(authenticationService)).addPathPatterns("/authenticated/**");  //maybe change to * ?
  }
}

